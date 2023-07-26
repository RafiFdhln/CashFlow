package com.example.cashflow.data.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cashflow.data.transaksi.Transaksi
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class UserRepository {

    var _user: MutableLiveData<FirebaseUser> = MutableLiveData()
    var _userData: MutableLiveData<User> = MutableLiveData()
    var _isRegistered: MutableLiveData<Boolean> = MutableLiveData()
    var _loginResult: MutableLiveData<Boolean> = MutableLiveData()
    private val db = Firebase.firestore.collection("users")
    private val auth = Firebase.auth

    init {
        if (auth.currentUser != null) {
            _user.value = auth.currentUser
            _loginResult.postValue(true)
            getData()
        }
    }

    fun register(email: String, password: String, nama: String, umur: Int) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = User(email, nama, umur, 0.0)
                db.document(email).set(user)
                _isRegistered.postValue(true)
                Log.i("TEST", "register: Berhasil Register")
            } else {
                Log.i("TEST", "register: Gagal Register", task.exception)
            }
        }
    }

    fun login(email: String, password: String) {
        Log.i("TEST", "Login..........")
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.postValue(auth.currentUser)
                    _loginResult.postValue(true)
                    getData()
                    Log.i("TEST", "register: berhasil Login", task.exception)
                } else {
                    _loginResult.postValue(false)
                    Log.i("TEST", "register: Gagal Login", task.exception)
                }
            }.addOnFailureListener {
                Log.i("TEST", "register: failure Login")
            }
    }

    fun logout() {
        auth.signOut()
        Log.i("TEST", "logout: SIGNOUT")
    }

    fun editData(nama: String, umur: Int) {
        db.document(auth.currentUser!!.email!!).update("nama", nama)
        db.document(auth.currentUser!!.email!!).update("umur", umur)
    }

    fun editUang(jumlah: Double, jenis: String) {
        db.document(auth.currentUser!!.email!!).get().addOnSuccessListener { document ->
            if (document != null) {
                val newUang = document.data!!.get("uang").toString().toDouble() + jumlah
                db.document(auth.currentUser!!.email!!).update("uang", newUang)
            } else {
                Log.d("TEST", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("TEST", "get failed with ", exception)
            }
    }

    fun getData() {
        db.document(auth.currentUser!!.email!!).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("TEST", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                _userData.value = snapshot.toObject<User>()
            } else {
                Log.d("TEST", "Current data: null")
            }
        }
    }



}