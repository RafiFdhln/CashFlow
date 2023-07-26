package com.example.cashflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.cashflow.data.user.UserViewModel
import com.example.cashflow.databinding.ActivityEditProfilBinding

class EditProfilActivity : AppCompatActivity() {

    private val userViewModel : UserViewModel by viewModels()
    private lateinit var binding : ActivityEditProfilBinding

    companion object{
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_UMUR = "extra_umur"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etEmail.setText(intent.getStringExtra(EXTRA_EMAIL))
        binding.etNama.setText(intent.getStringExtra(EXTRA_NAMA))
        binding.etUmur.setText(intent.getStringExtra(EXTRA_UMUR))

        binding.btnSave.setOnClickListener {
            userViewModel.editData(binding.etNama.text.toString().trim(), binding.etUmur.text.toString().trim().toInt())
            finish()
        }

        binding.imgBack.setOnClickListener {
            startActivity(Intent(this, ProfilFragment::class.java))
            finish()
        }
    }
}