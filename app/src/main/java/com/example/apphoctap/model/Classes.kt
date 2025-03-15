package com.example.apphoctap.model

    import android.os.Parcelable
    import kotlinx.parcelize.Parcelize

    @Parcelize
    data class Classes(
        val classID:String,
        val teacherID: Teacher,
        val className:String
    ) : Parcelable