package com.example.apphoctap.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
 data class Assignment  (
  val assignmentID:Int,
  val clasID: Classes,
  val title: String,
  val type:String,
  val creatAT:String,
  val limited:String
 ) : Parcelable
