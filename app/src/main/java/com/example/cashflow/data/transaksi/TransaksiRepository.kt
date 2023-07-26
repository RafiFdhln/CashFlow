package com.example.cashflow.data.transaksi

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cashflow.data.user.BulanIniCallback
import com.example.cashflow.data.user.BulanIniResponse
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class TransaksiRepository {

    var _listTransaksi: MutableLiveData<MutableList<Transaksi>> = MutableLiveData(mutableListOf())
    var _listIdTransaksi: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    private val db = Firebase.firestore.collection("users")
    private val auth = Firebase.auth

    init {
        if (auth.currentUser != null) {
            updateData()
        }
    }

    fun addTransaksi(kateogri: String, deskripsi: String, jumlah: Double, jenis: String, date: Date) {
        db.document(auth.currentUser!!.email!!).collection("transaksi")
            .add(Transaksi(kateogri, deskripsi, jumlah, jenis, date))
    }

    fun editTransaksi(id: String, kategori: String, deskripsi: String, jumlah: Double, jenis: String) {
        db.document(auth.currentUser!!.email!!).collection("transaksi").document(id)
            .update("kategori", kategori)
        db.document(auth.currentUser!!.email!!).collection("transaksi").document(id)
            .update("deskripsi", deskripsi)
        db.document(auth.currentUser!!.email!!).collection("transaksi").document(id)
            .update("jumlah", jumlah)
        db.document(auth.currentUser!!.email!!).collection("transaksi").document(id)
            .update("jenis", jenis)
    }

    fun deleteTransaksi(id: String) {
        db.document(auth.currentUser!!.email!!).collection("transaksi").document(id).delete()
    }

    fun filter(jenis: String, tanggalMulai: Date, tanggalSelesai: Date, callback: TransaksiCallback){
        db.document(auth.currentUser!!.email!!).collection("transaksi")
            .whereEqualTo("jenis", jenis).whereGreaterThanOrEqualTo("tanggal", tanggalMulai).whereLessThanOrEqualTo("tanggal", tanggalSelesai).orderBy("tanggal")
            .get()
            .addOnSuccessListener { documents ->
                val response = TransaksiResponse()
                for (document in documents) {
                    response.transaksi.add(document.toObject<Transaksi>())
                    response.idTransaksi.add(document.id)
                }
                callback.onResponse(response)
            }
            .addOnFailureListener { exception ->
                Log.w("TEST", "Error getting documents: ", exception)
            }
    }

    fun filterByBulan(tanggalMulai: Date, tanggalSelesai: Date, callback: TransaksiCallback){
        db.document(auth.currentUser!!.email!!).collection("transaksi")
            .whereGreaterThanOrEqualTo("tanggal", tanggalMulai).whereLessThanOrEqualTo("tanggal", tanggalSelesai).orderBy("tanggal")
            .get()
            .addOnSuccessListener { documents ->
                val response = TransaksiResponse()
                for (document in documents) {
                    response.transaksi.add(document.toObject<Transaksi>())
                    response.idTransaksi.add(document.id)
                }
                callback.onResponse(response)
            }
            .addOnFailureListener { exception ->
                Log.w("TEST", "Error getting documents: ", exception)
            }
    }

    fun updateData() {
        db.document(auth.currentUser!!.email!!).collection("transaksi").addSnapshotListener { value, e ->
            if (e != null) {
                Log.w("TEST", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (value != null) {
                val listTransaksi: MutableList<Transaksi> = mutableListOf()
                val listIdTransaksi: MutableList<String> = mutableListOf()
                for (doc in value){
                    listTransaksi.add(doc.toObject<Transaksi>())
                    listIdTransaksi.add(doc.id)
                }
                _listTransaksi.value = listTransaksi
                _listIdTransaksi.value = listIdTransaksi
            } else {
                Log.d("TEST", "Current data: null")
            }
        }
    }

    fun bulanIni(tanggalMulai: Date, tanggalSelesai: Date, callback: BulanIniCallback){
        db.document(auth.currentUser!!.email!!).collection("transaksi")
            .whereGreaterThanOrEqualTo("tanggal", tanggalMulai).whereLessThanOrEqualTo("tanggal", tanggalSelesai)
            .get()
            .addOnSuccessListener { documents ->
                val response = BulanIniResponse()
                for (document in documents) {
                    Log.i("TEST", "${document.data}")
                    if(document.data.get("jenis").toString() == "Income"){
                        response.income += document.data.get("jumlah").toString().toDouble()
                    }else{
                        response.expense += document.data.get("jumlah").toString().toDouble()
                    }
                }
                callback.onResponse(response)
            }
            .addOnFailureListener { exception ->
                Log.w("TEST", "Error getting documents: ", exception)
            }
    }

    fun test(date: Date){
        Firebase.firestore.collection("test").add(date)
    }

}