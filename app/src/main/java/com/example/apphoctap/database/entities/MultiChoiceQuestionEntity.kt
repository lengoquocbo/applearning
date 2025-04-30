package com.example.apphoctap.database.entities

import androidx.room.*

@Entity(
    tableName = "multi_choice_question",
    foreignKeys = [
        ForeignKey(
            entity = QuestionFileEntity::class,
            parentColumns = ["fileLocalId"],
            childColumns = ["fileLocalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("fileLocalId")]
)
data class MultiChoiceQuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val questionLocalId: Long = 0,
    val questionId: String? = null,  // ID từ server
    val fileLocalId: Long,           // Liên kết với file chứa câu hỏi
    val questionText: String,        // Nội dung câu hỏi
    val options: String,             // Các phương án trả lời (JSON array)
    val correctAnswerIndex: Int,     // Chỉ số của đáp án đúng
    val explanation: String? = null, // Giải thích đáp án
    val difficulty: String? = null,  // Độ khó (easy, medium, hard)
    val tags: String? = null,        // Các tag (dạng CSV hoặc JSON)
    val orderInFile: Int = 0,        // Thứ tự trong file
    val isSynced: Boolean = false    // Đã đồng bộ với server hay chưa
)