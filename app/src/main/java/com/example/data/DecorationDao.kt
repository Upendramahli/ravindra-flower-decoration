package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DecorationDao {
    @Query("SELECT * FROM decoration_items ORDER BY id DESC")
    fun getAllDecorations(): Flow<List<DecorationItem>>

    @Query("SELECT * FROM decoration_items WHERE category = :category ORDER BY id DESC")
    fun getDecorationsByCategory(category: String): Flow<List<DecorationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDecoration(item: DecorationItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<DecorationItem>)

    @Update
    suspend fun updateDecoration(item: DecorationItem)

    @Delete
    suspend fun deleteDecoration(item: DecorationItem)

    @Query("DELETE FROM decoration_items")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM decoration_items")
    suspend fun getCount(): Int
}
