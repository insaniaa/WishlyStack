package com.tiyasinsania0090.wishlystack.screen

import androidx.lifecycle.ViewModel
import com.tiyasinsania0090.wishlystack.model.Wish

class MainViewModel : ViewModel() {
    val wishList = listOf(
        Wish(1, "Laptop Baru", "Elektronik", "15000000", "Tinggi", "Untuk keperluan kuliah"),
        Wish(2, "Sepatu", "Fashion", "500000", "Sedang", "Butuh buat jalan-jalan"),
        Wish(3, "Meja Belajar", "Furniture", "800000", "Rendah", "Biar lebih fokus")
    )
}
