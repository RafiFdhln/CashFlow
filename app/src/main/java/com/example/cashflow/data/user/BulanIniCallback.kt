package com.example.cashflow.data.user

import com.example.cashflow.data.transaksi.TransaksiResponse

interface BulanIniCallback {
    fun onResponse(response: BulanIniResponse)
}