package com.example.apphoctap.database.dao

import androidx.room.*
import com.example.apphoctap.database.entities.ClassCacheEntitiy
import retrofit2.http.DELETE

@Dao
interface ClassCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClasses(classCache: List<ClassCacheEntitiy>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(classCache: ClassCacheEntitiy)

    @Query("SELECT * FROM class_cache")
    suspend fun getAllClasses(): List<ClassCacheEntitiy>

    @Query("SELECT * FROM class_cache WHERE classId = :classId")
    suspend fun getClassById(classId: String): ClassCacheEntitiy?

    @Query("Delete FROM class_cache WHERE classId = :classId")
    suspend fun deleteClass(classId : String)

    @Query("SELECT * FROM class_cache WHERE enrollmentKey = :enrollmentKey")
    suspend fun getClassByEnrollmentKey(enrollmentKey: String): ClassCacheEntitiy?

}