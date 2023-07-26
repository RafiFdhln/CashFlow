package com.example.cashflow.data.transaksi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cashflow.data.user.BulanIniCallback
import com.example.cashflow.data.user.UserRepository
import com.google.firebase.auth.FirebaseUser
import java.util.*

class TransaksiViewModel : ViewModel() {

    private var _user: MutableLiveData<FirebaseUser> = MutableLiveData()
    val user : LiveData<FirebaseUser>
        get() =_user

    private var _listTransaksi: MutableLiveData<MutableList<Transaksi>> = MutableLiveData()
    val listTransaksi : LiveData<MutableList<Transaksi>>
        get() =_listTransaksi

    private val transakasiRepository = TransaksiRepository()
    private val userRepository = UserRepository()

    init{
        _user = userRepository._user
        _listTransaksi = transakasiRepository._listTransaksi
    }

    fun getTransaksi(jenis: String, tanggalMulai: Date, tanggalSelesai: Date, callback: TransaksiCallback){

        if(jenis == "Semua"){
            transakasiRepository.filterByBulan(tanggalMulai, tanggalSelesai, callback)
        }else{
            transakasiRepository.filter(jenis, tanggalMulai, tanggalSelesai, callback)
        }
    }

    fun addTransaksi(kategori: String, deskripsi: String, jumlah: Double, jenis: String, date: Date){
        var uang = jumlah
        if(jenis == "Expenses"){
            uang = -jumlah
        }
        transakasiRepository.addTransaksi(kategori, deskripsi, jumlah, jenis, date)
        userRepository.editUang(uang, jenis)
    }

    fun deleteTransaksi(id: String, jumlah: Double, jenis: String){
        var uang = jumlah
        if(jenis == "Income"){
            uang = -jumlah
        }
        transakasiRepository.deleteTransaksi(id)
        userRepository.editUang(uang, jenis)
    }

    fun bulanIni(tanggalMulai: Date, tanggalSelesai: Date, callback: BulanIniCallback){
        transakasiRepository.bulanIni(tanggalMulai, tanggalSelesai, callback)
    }


}