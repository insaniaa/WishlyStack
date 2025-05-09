package com.tiyasinsania0090.wishlystack.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tiyasinsania0090.wishlystack.model.Wish
import com.tiyasinsania0090.wishlystack.model.Category

@Database(entities = [Wish::class, Category::class], version = 1)
abstract class WishlistDb : RoomDatabase() {
    abstract val wishListDao: WishlistDao
    abstract val categoryDao: CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: WishlistDb? = null

        fun getInstance(context: Context): WishlistDb{
            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WishlistDb::class.java,
                        "wishlist_db.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}


