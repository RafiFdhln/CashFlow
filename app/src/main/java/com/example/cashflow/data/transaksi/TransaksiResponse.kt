package com.example.cashflow.data.transaksi

data class TransaksiResponse (var transaksi: MutableList<Transaksi> = mutableListOf(), var idTransaksi: MutableList<String> = mutableListOf())
