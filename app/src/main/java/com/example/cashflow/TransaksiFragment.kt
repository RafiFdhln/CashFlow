package com.example.cashflow

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cashflow.data.transaksi.*
import com.example.cashflow.data.user.User
import com.example.cashflow.data.user.UserViewModel
import com.example.cashflow.databinding.FragmentTransaksiBinding
import java.text.SimpleDateFormat
import java.util.*

class TransaksiFragment : Fragment(R.layout.fragment_transaksi) {

    private val userViewModel: UserViewModel by activityViewModels()
    private val transaksiViewModel: TransaksiViewModel by activityViewModels()
    private val transaksiAdapter = TransaksiAdapter()
    private lateinit var binding: FragmentTransaksiBinding
    private var tanggalMulai = SimpleDateFormat("yyyy-MM-dd").parse("1000-01-01")
    private var tanggalSelesai = SimpleDateFormat("yyyy-MM-dd").parse("3000-01-01")
    var jenis = "Semua"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTransaksiBinding.bind(view)

        reset()

        val dialogMulai = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            binding.tvTanggalMulai.text = "$dayOfMonth/${month + 1}/$year"
            val parseDate = "$year-${month + 1}-$dayOfMonth"
            tanggalMulai = SimpleDateFormat("yyyy-MM-dd").parse(parseDate)
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(
            Calendar.DAY_OF_MONTH))

        val dialogSelesai = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            binding.tvTanggalSelesai.text = "$dayOfMonth/${month + 1}/$year"
            val parseDate = "$year-${month + 1}-$dayOfMonth"
            tanggalSelesai = SimpleDateFormat("yyyy-MM-dd").parse(parseDate)
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(
            Calendar.DAY_OF_MONTH))

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
            getData()
        })

        transaksiAdapter.setOnItemClickCallback(object: TransaksiAdapter.OnItemClickCallback{
            override fun onItemClicked(idTransaksi: String, data: Transaksi) {
                transaksiViewModel.deleteTransaksi(idTransaksi, data.jumlah, data.jenis)
                getData()
            }
        })

        binding.spnJenis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                jenis = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(activity, "on nothing selected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnTambah.setOnClickListener{
            startActivity(Intent(activity, TambahActivity::class.java))
        }

        binding.btnPickDateMulai.setOnClickListener {
            dialogMulai.show()
        }

        binding.btnPickDateSelesai.setOnClickListener {
            dialogSelesai.show()
        }

        binding.btnFilter.setOnClickListener {
            getData()
            Toast.makeText(activity, "$tanggalMulai - $tanggalSelesai", Toast.LENGTH_LONG).show()
        }

        binding.btnReset.setOnClickListener {
            reset()
        }

    }

    fun getData(){
        transaksiViewModel.getTransaksi(jenis, tanggalMulai, tanggalSelesai, object: TransaksiCallback{
            override fun onResponse(response: TransaksiResponse) {
                transaksiAdapter.updateListData(response.transaksi, response.idTransaksi)
                binding.rvTransaksi.layoutManager = LinearLayoutManager(activity)
                binding.rvTransaksi.adapter = transaksiAdapter
                binding.loading.visibility = View.GONE
                binding.rvTransaksi.visibility = View.VISIBLE
            }
        })
    }

    fun reset(){
        tanggalMulai = SimpleDateFormat("yyyy-MM-dd").parse("1000-01-01")
        tanggalSelesai = SimpleDateFormat("yyyy-MM-dd").parse("3000-01-01")
        binding.tvTanggalMulai.text = "Tanggal Mulai"
        binding.tvTanggalSelesai.text = "Tanggal Selesai"
        binding.spnJenis.setSelection(0)
        jenis = "Semua"
        getData()
    }

}