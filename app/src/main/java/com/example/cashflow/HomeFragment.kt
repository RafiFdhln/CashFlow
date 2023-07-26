package com.example.cashflow

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cashflow.data.transaksi.TransaksiViewModel
import com.example.cashflow.data.user.User
import androidx.lifecycle.Observer
import com.example.cashflow.data.transaksi.Transaksi
import com.example.cashflow.data.user.BulanIniCallback
import com.example.cashflow.data.user.BulanIniResponse
import com.example.cashflow.data.user.UserViewModel
import com.example.cashflow.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val userViewModel: UserViewModel by activityViewModels()
    private val transaksiViewModel: TransaksiViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    val bulan = Calendar.getInstance().get(Calendar.MONTH) + 1
    val tahun = Calendar.getInstance().get(Calendar.YEAR)
    val tanggalMulai = SimpleDateFormat("yyyy-MM-dd").parse("$tahun-$bulan-01")
    val tanggalSelesai = SimpleDateFormat("yyyy-MM-dd").parse("$tahun-$bulan-30")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        userViewModel.userData.observe(viewLifecycleOwner, Observer<User>{
            if(it == null){
                startActivity(Intent(activity, AuthActivity::class.java))
                requireActivity().finish()
            }else{
                binding.tvUang.text = "Rp. ${userViewModel.userData.value!!.uang}"
                binding.tvNama.text = userViewModel.userData.value!!.nama
            }
        })

        transaksiViewModel.listTransaksi.observe(viewLifecycleOwner, Observer<MutableList<Transaksi>>{
            getData(tanggalMulai, tanggalSelesai)
        })

        getData(tanggalMulai, tanggalSelesai)
    }

    fun getData(tanggalMulai: Date, tanggalSelesai: Date){
        transaksiViewModel.bulanIni(tanggalMulai, tanggalSelesai, object: BulanIniCallback{
            override fun onResponse(response: BulanIniResponse) {
                binding.tvIncome.text = "Rp${response.income}"
                binding.tvExpense.text = "Rp${response.expense}"
                binding.loading.visibility = View.GONE
//                binding.linearLayout.visibility = View.VISIBLE
            }
        })
    }
}