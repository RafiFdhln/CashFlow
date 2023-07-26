package com.example.cashflow

import  android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.cashflow.data.user.UserViewModel
import com.example.cashflow.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: com.example.cashflow.databinding.FragmentRegisterBinding

    companion object {
        private const val STATE_NAMA = "state_nama"
        private const val STATE_UMUR = "state_umur"
        private const val STATE_EMAIL = "state_email"
        private const val STATE_PASSWORD = "state_password"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        userViewModel.isRegistered.observe(viewLifecycleOwner, Observer<Boolean>{ isRegistered->
            binding.loading.visibility = View.GONE
            if(isRegistered){
                startActivity(Intent(activity, MainActivity::class.java))
                requireActivity().finish()
            }
        })

        binding.btnLogin.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, LoginFragment())
                addToBackStack(null)
                commit()
            }
        }

        binding.btnDaftar.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val umur = binding.etUmur.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (!userViewModel.isNameValid(nama)) {
                binding.etNama.error = "NAMA JGN KOSONG BANG"
                return@setOnClickListener
            }

            if (!userViewModel.isUmurValid(nama)) {
                binding.etUmur.error = "UMUR JGN KOSONG BANG"
                return@setOnClickListener
            }

            if (!userViewModel.isEmailValid(email)) {
                binding.etEmail.error = "EMAIL YG BNER LAH"
                return@setOnClickListener
            }

            if (!userViewModel.isPasswordValid(password)) {
                binding.etPassword.error = "PASSWORD YG BNER LAH"
                return@setOnClickListener
            }

            binding.loading.visibility = View.VISIBLE
            userViewModel.register(email, password, nama, umur.toInt())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_NAMA, binding.etNama.text.toString().trim())
        outState.putString(STATE_UMUR, binding.etUmur.text.toString().trim())
        outState.putString(STATE_EMAIL, binding.etEmail.text.toString().trim())
        outState.putString(STATE_PASSWORD, binding.etPassword.text.toString().trim())
    }
}