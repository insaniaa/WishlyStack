package com.tiyasinsania0090.wishlystack.database

import androidx.room.*
import com.tiyasinsania0090.wishlystack.model.Category
import com.tiyasinsania0090.wishlystack.model.Wish
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)
    @Update
    suspend fun update(category: Category)

    @Query("SELECT * FROM category")
    fun getAll(): Flow<List<Category>>

    @Query("DELETE FROM category WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM category WHERE id = :id")
    suspend fun getById(id: Int): Category?
}
