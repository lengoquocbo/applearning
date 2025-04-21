package com.example.apphoctap.repository

import android.util.Log
import com.example.apphoctap.database.dao.AssignmentDao
import com.example.apphoctap.network.api.AssignmentApi
import javax.inject.Inject

class AssignmentRepository @Inject constructor(
    private val AssignmentApi: AssignmentApi,
    private val AssignmentDao: AssignmentDao,
) {
    //Hàm lấy dữ liệu từ Room
    fun getAssignmentFlow(classID : String) = AssignmentDao.getAssignmentsByClass(classID)

    //Hàm refresh dữ liệu assignment bằng API

}