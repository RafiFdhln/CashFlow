package com.example.cashflow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.cashflow.data.user.User
import com.example.cashflow.data.user.UserViewModel
import com.example.cashflow.databinding.FragmentProfilBinding

class ProfilFragment : Fragment(R.layout.fragment_profil) {

    private val userViewModel : UserViewModel by activityViewModels()
    private lateinit var binding : FragmentProfilBinding



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfilBinding.bind(view)

        userViewModel.userData.observe(viewLifecycleOwner, Observer<User>{
            if(it == null){
                startActivity(Intent(activity, AuthActivity::class.java))
                requireActivity().finish()
            }else{
                binding.tvNama.text = userViewModel.userData.value!!.nama
                binding.tvEmail.text = userViewModel.userData.value!!.email
                binding.tvUmur.text = userViewModel.userData.value!!.umur.toString()
            }
        })

        binding.btnEdit.setOnClickListener{
            val intent = Intent(activity, EditProfilActivity::class.java)
            intent.putExtra(EditProfilActivity.EXTRA_EMAIL, binding.tvEmail.text.toString())
            intent.putExtra(EditProfilActivity.EXTRA_NAMA, binding.tvNama.text.toString())
            intent.putExtra(EditProfilActivity.EXTRA_UMUR, binding.tvUmur.text.toString())
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener{
            userViewModel.logout()
            startActivity(Intent(activity, AuthActivity::class.java))
            requireActivity().finish()
        }
    }

}