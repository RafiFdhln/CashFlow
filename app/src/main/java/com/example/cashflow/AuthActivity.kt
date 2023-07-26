package com.example.cashflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.cashflow.data.user.UserViewModel
import com.example.cashflow.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseUser

class AuthActivity : AppCompatActivity() {

    private val userViewModel : UserViewModel by viewModels()
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel.user.observe(this, Observer<FirebaseUser> { user ->
            if (user != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
    }
}