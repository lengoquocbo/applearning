package com.example.apphoctap.repository

import android.content.Context
import com.example.apphoctap.database.dao.AssignmentDao
import com.example.apphoctap.model.Assignment
import com.example.apphoctap.network.api.AssignmentApi
import com.example.apphoctap.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AssignmentRepository @Inject constructor(
    private val AssignmentApi: AssignmentApi,
    private val AssignmentDao: AssignmentDao,
    private val context: Context
) {

    //Hàm lấy danh sách Assignment bằng Class ID
    suspend fun getAssignmentsByClass(classId: String): List<Assignment> {
        return withContext(Dispatchers.IO) {
            var assignment = AssignmentDao.getAssignmentsByClass(classId)

            if(assignment == null) {
                if(NetworkUtils.isNetworkAvailable(context)){
                    val response = AssignmentApi.getAssignmentByClassId(classId)
                    if(response.isSuccessful){
                        assignment = response.body() ?: throw Exception("API returned null")
                        assignment?.let { AssignmentDao.insertAssignment(it) }
                    } else throw Exception("Failed to fetch assignments from API")
                }
            }
            return@withContext assignment
        }
    }
}