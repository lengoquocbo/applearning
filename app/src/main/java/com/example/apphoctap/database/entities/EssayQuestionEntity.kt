package com.example.apphoctap.database.entities

import androidx.room.*

@Entity(
    tableName = "essay_question",
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
data class EssayQuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val questionLocalId: Long = 0,
    val questionId: String? = null,  // ID từ server
    val fileLocalId: Long,           // Liên kết với file chứa câu hỏi
    val questionText: String,        // Nội dung câu hỏi
    val suggestedAnswer: String? = null, // Gợi ý câu trả lời
    val difficulty: String? = null,  // Độ khó (easy, medium, hard)
    val tags: String? = null,        // Các tag (dạng CSV hoặc JSON)
    val orderInFile: Int = 0,        // Thứ tự trong file
    val isSynced: Boolean = false    // Đã đồng bộ với server hay chưa
)