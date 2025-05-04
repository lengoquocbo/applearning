package com.example.apphoctap.database.entities

import androidx.room.*

@Entity(tableName = "class_cache")
data class ClassCacheEntitiy(
    @PrimaryKey
    val classId: String,
    val className: String,
    val teacherName: String,
    val description: String,
    val enrollmentKey : String,
    val lastSyncTime: Long, // Thời gian đồng bộ lần cuối
    val lastAccessTime : Long? = null //Thời gian truy cập lần cuối
)