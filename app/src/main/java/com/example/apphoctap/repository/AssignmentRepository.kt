package com.example.apphoctap.repository

import android.util.Log
import com.example.apphoctap.network.api.AssignmentApi
import javax.inject.Inject

class AssignmentRepository @Inject constructor(
    private val AssignmentApi: AssignmentApi
) {
    //Hàm lấy dữ liệu từ Room

    //Hàm refresh dữ liệu assignment bằng API

}