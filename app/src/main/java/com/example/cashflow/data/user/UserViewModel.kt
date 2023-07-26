package com.example.cashflow.data.user

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class UserViewModel : ViewModel() {
    private var _user: MutableLiveData<FirebaseUser> = MutableLiveData()
    val user : LiveData<FirebaseUser>
        get() =_user

    private var _userData: MutableLiveData<User> = MutableLiveData()
    val userData : LiveData<User>
        get() =_userData

    private var _isRegistered: MutableLiveData<Boolean> = MutableLiveData()
    val isRegistered: LiveData<Boolean> get() = _isRegistered

    private var _loginResult: MutableLiveData<Boolean> = MutableLiveData()
    val loginResult: LiveData<Boolean>
        get() = _loginResult

    private val repository = UserRepository()

    init{
        _user = repository._user
        _userData = repository._userData
        _isRegistered = repository._isRegistered
        _loginResult = repository._loginResult
    }

    fun login(email: String, password: String){
        repository.login(email, password)
    }

    fun register(email: String, password: String, nama: String, umur: Int){
        repository.register(email, password, nama, umur)
    }

    fun logout(){
        repository.logout()
    }

    fun editData(nama: String, umur: Int){
        repository.editData(nama, umur)
    }

    fun getData(){
        repository.getData()
    }

    fun isNameValid(nama: String) : Boolean{
        return (nama.isNotBlank())
    }

    fun isUmurValid(umur: String) : Boolean{
        return (umur.isNotBlank())
    }

    fun isEmailValid(email: String) : Boolean{
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotBlank())
    }

    fun isPasswordValid(password: String) : Boolean{
        return password.length > 5
    }
}