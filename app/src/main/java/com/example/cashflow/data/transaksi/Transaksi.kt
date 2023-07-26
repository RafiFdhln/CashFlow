package com.example.cashflow.data.transaksi

import java.util.*

data class Transaksi(val kategori: String = "", val deskripsi: String = "", val jumlah: Double = 0.0, val jenis: String = "", val tanggal: Date = Date())