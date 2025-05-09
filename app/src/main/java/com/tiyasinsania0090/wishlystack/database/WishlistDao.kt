package com.tiyasinsania0090.wishlystack.database

import androidx.room.*
import com.tiyasinsania0090.wishlystack.model.Wish
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wish: Wish)

    @Update
    suspend fun update(wish: Wish)

    @Query("DELETE FROM wish WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM wish")
    fun getAll(): Flow<List<Wish>>

    @Query("SELECT * FROM wish WHERE id = :id")
    suspend fun getWishById(id: Int): Wish?

    @Query("SELECT COUNT(*) FROM wish WHERE categoryId = :categoryId")
    suspend fun getBarangCountByKategori(categoryId: Int): Int

//    @Transaction
//    @Query("SELECT * FROM wish WHERE id = :wishId")
//    suspend fun getWishWithCategoryById(wishId: Int): WishWithCategory?
}
