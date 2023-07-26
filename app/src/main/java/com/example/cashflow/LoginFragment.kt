package com.example.cashflow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.cashflow.data.user.UserViewModel
import com.example.cashflow.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseUser

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val userViewModel : UserViewModel by activityViewModels()
    private lateinit var binding : FragmentLoginBinding

//    companion object {
//        private const val STATE_EMAIL = "state_email"
//        private const val STATE_PASSWORD = "state_password"
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.b    ind(view)

//        if (savedInstanceState != null) {
//            binding.etEmail.setText(savedInstanceState.getString(STATE_EMAIL))
//            binding.etPassword.setText(savedInstanceState.getString(STATE_PASSWORD))
//        }

        userViewModel.loginResult.observe(viewLifecycleOwner, Observer<Boolean> {
            binding.loading.visibility = View.GONE
            if (it) {
            }else{
                Toast.makeText(activity, "SALAH BANG", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if(!userViewModel.isEmailValid(email)){
                binding.etEmail.error = "EMAIL YG BNER LAH"
                return@setOnClickListener
            }

            if(!userViewModel.isPasswordValid(password)){
                binding.etPassword.error = "PASSWORD YG BNER LAH"
                return@setOnClickListener
            }

            binding.loading.visibility = View.VISIBLE
            userViewModel.login(email, password)
        }

        binding.btnDaftar.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, RegisterFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString(STATE_EMAIL, binding.etEmail.text.toString().trim())
//        outState.putString(STATE_PASSWORD, binding.etPassword.text.toString().trim())
//    }
}