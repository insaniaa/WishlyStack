package com.tiyasinsania0090.wishlystack.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "wish",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Wish(
    @PrimaryKey val id: Int,
    val userId: String,
    val name: String,
    val categoryId: Int,
    val price: Double,
    val priority: String,
    val description: String?,
    val picture: String?
)