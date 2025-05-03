package com.example.apphoctap.repository

import com.example.apphoctap.database.dao.ClassCacheDao
import com.example.apphoctap.database.entities.ClassCacheEntitiy
import com.example.apphoctap.model.ClassUiModel
import com.example.apphoctap.network.api.ClassApi
import com.example.apphoctap.model.ClassResponse
import com.example.apphoctap.network.api.ClassStudentApi
import com.example.apphoctap.utils.AccessDeniedError
import com.example.apphoctap.utils.ApiError
import com.example.apphoctap.utils.InvalidEnrollmentKeyError
import com.example.apphoctap.utils.NetworkError
import com.example.apphoctap.utils.NetworkMonitor
import com.example.apphoctap.utils.ResultAction
import com.example.apphoctap.utils.UiState
import retrofit2.Response
import javax.inject.Inject


class ClassRepository @Inject constructor(
    private val classCacheDao: ClassCacheDao,
    private val classApi: ClassApi,
    private val networkMonitor: NetworkMonitor,
    private val classStudentApi: ClassStudentApi
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

    suspend fun getClasses(): List<ClassUiModel> {
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
            val response = classApi.getAllClasses()

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

    suspend fun leaveClass(classId : String, studentId : String) : UiState<Boolean> {
        return try {
            val response = classStudentApi.leaveClass(classId, studentId)
            if (response.isSuccessful) {
                classCacheDao.deleteClass(classId)
                UiState.Success(true)
            } else {
                UiState.Error("Error: ${response.code()} - ${response.message()}")
            }
        } catch (e : Exception) {
            UiState.Error(e.message ?: "Unknown error occurred")
        }
    }

    suspend fun joinClassByEnrollmentKey(enrollmentKey: String): ResultAction<ClassUiModel> {
        val cacheClass = classCacheDao.getClassByEnrollmentKey(enrollmentKey)
        if (cacheClass != null) {
            return ResultAction.Success(cacheClass.toUiModel())
        }

        if(networkMonitor.isNetworkAvailable()) return ResultAction.Error(NetworkError("Không có kết nối mạng"))

        return try{
            val response = classApi.getClassByEnrollmentKey(enrollmentKey)
            if (response.isSuccessful && response.body() != null){

                val classResponse = response.body()!!

                classCacheDao.insertClass(classResponse.toCacheEntity())
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

}