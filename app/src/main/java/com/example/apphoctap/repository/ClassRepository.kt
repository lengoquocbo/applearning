package com.example.apphoctap.repository

import android.util.Log
import com.example.apphoctap.database.dao.ClassCacheDao
import com.example.apphoctap.database.entities.ClassCacheEntitiy
import com.example.apphoctap.model.ClassUiModel
import com.example.apphoctap.network.api.ClassApi
import com.example.apphoctap.model.ClassResponse
import com.example.apphoctap.model.DeleteStudentResponse
import com.example.apphoctap.network.api.ClassStudentApi
import com.example.apphoctap.utils.AccessDeniedError
import com.example.apphoctap.utils.ApiError
import com.example.apphoctap.utils.InvalidEnrollmentKeyError
import com.example.apphoctap.utils.NetworkError
import com.example.apphoctap.utils.NetworkMonitor
import com.example.apphoctap.utils.NotFoundError
import com.example.apphoctap.utils.ResultAction
import com.example.apphoctap.utils.SessionManager
import com.example.apphoctap.utils.UiState
import com.example.apphoctap.utils.UnauthorizedError
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Channel
import javax.inject.Inject

class ClassRepository @Inject constructor(
    private val classCacheDao: ClassCacheDao,
    private val classApi: ClassApi,
    private val networkMonitor: NetworkMonitor,
    private val classStudentApi: ClassStudentApi,
    private val sessionManager: SessionManager,
    private val chatClient : ChatClient
) {

    // Hàm chuyển đổi từ ClassResponse sang UI model
    private fun ClassResponse.toUiModel(): ClassUiModel {
        return ClassUiModel(
            classId = classID,
            className = className,
            teacherName = teacherName,
            description = description,
            enrollmentKey = enrollmentKey,
            isFromCache = false
        )
    }

    // Hàm chuyển đổi từ ClassCacheEntity sang UI model
    private fun ClassCacheEntitiy.toUiModel(): ClassUiModel {
        return ClassUiModel(
            classId = classId,
            className = className,
            teacherName = teacherName,
            description = description,
            enrollmentKey = enrollmentKey,
            isFromCache = true
        )
    }

    // Chuyển từ ClassResponse sang ClassCacheEntity
    private fun ClassResponse.toCacheEntity(): ClassCacheEntitiy {
        return ClassCacheEntitiy(
            classId = classID,
            className = className,
            teacherName = teacherName,
            description = description,
            enrollmentKey = enrollmentKey,
            lastSyncTime = System.currentTimeMillis()
        )
    }

    private fun ClassResponse.toCacheEntityWhenCreate(): ClassCacheEntitiy {
        return ClassCacheEntitiy(
            classId = classID,
            className = className,
            teacherName = teacherName,
            description = description,
            enrollmentKey = enrollmentKey,
            lastSyncTime = System.currentTimeMillis(),
            lastAccessTime = System.currentTimeMillis()
        )
    }

    suspend fun getNearbyAccessClasses() : List<ClassUiModel>{
        val cachedClasses = classCacheDao.getRecentlyAccessedClasses()
        return cachedClasses.map { it.toUiModel() }
    }

    suspend fun getClasses(): List<ClassUiModel> {
        Log.d("ClassRepository", "Lấy dữ liệu từ cache (cho fallback nếu cần)")

        val cachedClasses = classCacheDao.getAllClasses()

        if (!networkMonitor.isNetworkAvailable()) {
            Log.d("ClassRepository", "Không có mạng, dùng cache (dù cũ)")
            return cachedClasses.map { it.toUiModel() }
        }

        // Bỏ qua cache, LUÔN LẤY từ API nếu có mạng
        return try {
            Log.d("ClassRepository", "Gọi API để lấy dữ liệu mới")
            val response = classApi.getClassByStudentId()

            if (response.isSuccessful && response.body() != null) {
                val classesFromApi = response.body()!!

                // --- Đồng bộ cache xoá các lớp không còn trên server ---
                /*
                    Ví dụ :
                    cachedClassIds = setOf("classA", "classB", "classC")
                    apiClassIds = setOf("classA", "classC")
                    deletedClassIds = cachedClassIds - apiClassIds
                                    = {"classA", "classB", "classC"} - {"classA", "classC"}
                                    = {"classB"}
                 */
                val apiClassIds = classesFromApi.map { it.classID }.toSet()
                val cachedClassIds = cachedClasses.map { it.classId }.toSet()
                val deletedClassIds = cachedClassIds - apiClassIds

                if (deletedClassIds.isNotEmpty()) {
                    classCacheDao.deleteClassesByIds(deletedClassIds.toList())
                }

                // --- Cập nhật cache ---
                classCacheDao.insertClasses(
                    classesFromApi.map { it.toCacheEntity() }
                )

                classesFromApi.map { it.toUiModel() }
            } else {
                Log.d("ClassRepository", "API lỗi: ${response.code()}, fallback cache")
                cachedClasses.map { it.toUiModel() }
            }
        } catch (e: Exception) {
            Log.d("ClassRepository", "Lỗi mạng/API: ${e.message}, fallback cache")
            cachedClasses.map { it.toUiModel() }
        }
    }


    /**
     * Kiểm tra xem cache có cần refresh hay không
     * @return true nếu cần refresh, false nếu cache vẫn còn hiệu lực
     */
    private fun shouldRefreshCache(cachedClasses: List<ClassCacheEntitiy>): Boolean {
        if (cachedClasses.isEmpty()) return true

        val currentTime = System.currentTimeMillis()
        val oldestSyncTime = cachedClasses.minOfOrNull { it.lastSyncTime } ?: 0L

        // Cache được coi là hết hạn sau 15 phút
        return (currentTime - oldestSyncTime) > CACHE_REFRESH_INTERVAL
    }

    companion object {
        // Cache được giữ trong 15 phút
        private const val CACHE_REFRESH_INTERVAL = 30 * 60 * 1000L // 30 phút in milliseconds
    }

    suspend fun leaveClass(classId: String): ResultAction<Unit> {
        return try {
            // ở đây cần lấy ra enrolment key trong kết quả tra về
            val response  = classStudentApi.leaveClass(classId)
            Log.d("LeaveClassRepository", "Response: $response")
            val enrollmentKey = response.body()!!.enrollmentKey
            if (response.isSuccessful) {
                classCacheDao.deleteClass(classId)
                val userId = sessionManager.getUserId().toString()
                val channelClient = chatClient.channel("messaging", enrollmentKey)
                channelClient.removeMembers(listOf(userId)).enqueue { result ->
                    if (result is io.getstream.result.Result.Success) {
                        val channel: Channel = result.value
                        Log.d("Delete member", "Học sinh hiện có ${channel.members}")
                    } else {
                        Log.d("Delete member", "Lỗi khi xóa member: ${result.isFailure}")
                    }
                }
                ResultAction.Success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Lỗi không xác định"
                when (response.code()) {
                    401 -> ResultAction.Error(UnauthorizedError("Không có quyền truy cập: $errorBody"))
                    403 -> ResultAction.Error(AccessDeniedError("Bạn không có quyền rời lớp này: $errorBody"))
                    404 -> ResultAction.Error(NotFoundError("Không tìm thấy lớp học hoặc học sinh: $errorBody"))
                    else -> ResultAction.Error(ApiError("Lỗi khi rời lớp: $errorBody", response.code()))
                }
            }
        } catch (e: Exception) {
            Log.e("LeaveClass", "Exception: ${e.message}, stacktrace: ${e.stackTraceToString()}", e)
            ResultAction.Error(NetworkError("Lỗi kết nối: ${e.message}"))
        }
    }

    suspend fun joinClassByEnrollmentKey(enrollmentKey: String): ResultAction<ClassUiModel> {
        Log.d("Join", "Join class")
        val cacheClass = classCacheDao.getClassByEnrollmentKey(enrollmentKey)
        if (cacheClass != null) {
            Log.d("Join", "cache null")
            return ResultAction.Success(cacheClass.toUiModel())
        }

        if (!networkMonitor.isNetworkAvailable()) {
            return ResultAction.Error(NetworkError("Không có kết nối mạng"))
        }

        return try{
            val response = classApi.getClassByEnrollmentKey(enrollmentKey)
            if (response.isSuccessful && response.body() != null){
                val classResponse : ClassResponse = response.body()!!
                classCacheDao.insertClass(classResponse.toCacheEntityWhenCreate())
                classStudentApi.addStudentToClass(classResponse.classID)

                val userId = sessionManager.getUserId().toString()
                val channelClient = chatClient.channel("messaging", channelId = enrollmentKey)
                channelClient.addMembers(listOf(userId)).enqueue { result ->
                    if (result is io.getstream.result.Result.Success) {
                        val channel: Channel = result.value
                        Log.d("List Channel", "Channel created successfully: ${channel.name}")
                    } else {
                        Log.d("List Channel", "Channel creation failed: ${result.isFailure}")
                    }
                }

                ResultAction.Success(classResponse.toUiModel())
            } else {
                val errorBody = response.errorBody()?.string() ?: "Lỗi không xác định"
                when (response.code()) {
                    404 -> ResultAction.Error(InvalidEnrollmentKeyError("Mã đăng ký không hợp lệ"))
                    403 -> ResultAction.Error(AccessDeniedError("Bạn không có quyền tham gia lớp học này"))
                    else -> ResultAction.Error(ApiError(errorBody, response.code()))
                }
            }
        } catch (e: Exception) {
            ResultAction.Error(NetworkError("Lỗi kết nối: ${e.message}"))
        }
    }

    suspend fun updateClassAccessTime(classId: String) {
        val currentTime = System.currentTimeMillis()
        classCacheDao.updateLastAccessTime(classId, currentTime)
    }

    suspend fun getClassesOfTeacher(teacherId: String): List<ClassUiModel> {
        // Bước 1: Luôn kiểm tra cache trước (Local First)
        val cachedClasses = classCacheDao.getAllClasses()

        // Bước 2: Kiểm tra nếu cache chưa quá hạn và có dữ liệu thì trả về
        if (cachedClasses.isNotEmpty() && !shouldRefreshCache(cachedClasses)) {
            return cachedClasses.map { it.toUiModel() }
        }

        // Bước 3: Kiểm tra kết nối mạng
        if (!networkMonitor.isNetworkAvailable()) {
            // Không có mạng, trả về cache dù có thể đã cũ
            return cachedClasses.map { it.toUiModel() }
        }

        // Bước 4: Có mạng, thử lấy dữ liệu mới
        return try {
            val response = classApi.getClassesByTeacherID(teacherId)

            if (response.isSuccessful && response.body() != null) {
                val classes = response.body()!!

                // Lưu vào cache
                classCacheDao.insertClasses(classes.map { it.toCacheEntity() })

                // Trả về dữ liệu mới
                classes.map { it.toUiModel() }
            } else {
                // Lỗi API, fallback về cache
                cachedClasses.map { it.toUiModel() }
            }
        } catch (e: Exception) {
            // Lỗi mạng, fallback về cache
            cachedClasses.map { it.toUiModel() }
        }
    }

}