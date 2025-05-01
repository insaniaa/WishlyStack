package com.tiyasinsania0090.wishlystack.screen

import androidx.lifecycle.ViewModel
import com.tiyasinsania0090.wishlystack.model.Wish

class MainViewModel : ViewModel() {

    val data = listOf(
        Wish(
            id = 1,
            name = "Laptop Baru",
            type = "Elektronik",
            price = "15000000",
            priority = "Tinggi",
            notes = "Untuk keperluan kuliah dan freelance desain"
        ),
        Wish(
            id = 2,
            name = "Kamera Mirrorless",
            type = "Elektronik",
            price = "7000000",
            priority = "Sedang",
            notes = "Ingin mendalami fotografi dan dokumentasi"
        ),
        Wish(
            id = 3,
            name = "Liburan ke Bali",
            type = "Pengalaman",
            price = "5000000",
            priority = "Rendah",
            notes = "Reward setelah UAS semester ini"
        ),
        Wish(
            id = 4,
            name = "Meja Belajar Estetik",
            type = "Furniture",
            price = "1200000",
            priority = "Sedang",
            notes = "Biar lebih semangat dan nyaman belajar di kosan"
        ),
        Wish(
            id = 5,
            name = "Headphone ANC",
            type = "Elektronik",
            price = "2000000",
            priority = "Tinggi",
            notes = "Biar bisa fokus pas ngoding atau edit desain"
        )
    )
}
