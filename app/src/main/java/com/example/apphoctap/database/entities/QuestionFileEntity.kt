package com.example.apphoctap.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_file")
data class QuestionFileEntity(
    @PrimaryKey(autoGenerate = true)
    val fileLocalId: Long = 0,
    val fileId: Int? = null,        // ID từ server, null nếu chưa đồng bộ
    val fileName: String,           // Tên file hiển thị
    val fileType: String = "quiz",  // Loại file (mặc định là quiz)
    val createdAt: String,            // Thời gian tạo (timestamp)
    val lastModified: String,         // Thời gian chỉnh sửa gần nhất
    val totalQuestions: Int = 0,    // Tổng số câu hỏi trong file
    val description: String? = null,// Mô tả ngắn về file
    val isSynced: Boolean = false,  // Đã đồng bộ với server hay chưa
    val localFilePath: String? = null, // Đường dẫn đến file cục bộ (nếu đã tải xuống)
    val isDownloaded: Boolean = false  // Đã tải xuống thiết bị hay chưa
)
