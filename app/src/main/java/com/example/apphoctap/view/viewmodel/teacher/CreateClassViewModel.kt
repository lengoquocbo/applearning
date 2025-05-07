import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apphoctap.model.CreateClassRequest
import com.example.apphoctap.network.RetrofitInstance
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateClassViewModel : ViewModel() {

    private val repository = RetrofitInstance.classRepository

    private val _createClassResult = MutableLiveData<Result<String>>()
    val createClassResult: LiveData<Result<String>> get() = _createClassResult

    fun createClass(
        classID: String,
        teacherID: String,
        className: String,
        description: String,
        enrollmentKey: String
    ) {
        val createAt = getCurrentDateTime()

        val newClass = CreateClassRequest(
            classID = classID,
            teacherID = teacherID,
            className = className,
            description = description,
            createAt = createAt,
            enrollmentKey = enrollmentKey
        )

        viewModelScope.launch {
            try {
                val response = repository.createClass(newClass)
                if (response.isSuccessful) {
                    val body = response.body()
                    _createClassResult.value = Result.success(body?.message ?: "Tạo lớp thành công")
                } else {
                    _createClassResult.value = Result.failure(Exception("Lỗi: ${response.code()}"))
                }
            } catch (e: Exception) {
                _createClassResult.value = Result.failure(e)
            }
        }
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}
