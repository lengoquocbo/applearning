package com.example.apphoctap.database.dao

import androidx.room.*
import com.example.apphoctap.database.entities.ClassCacheEntitiy

@Dao
interface ClassCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(classCache: ClassCacheEntitiy)

    @Query("SELECT * FROM class_cache")
    suspend fun getAllClasses(): List<ClassCacheEntitiy>

    @Query("SELECT * FROM class_cache WHERE classId = :classId")
    suspend fun getClassById(classId: String): ClassCacheEntitiy?
}