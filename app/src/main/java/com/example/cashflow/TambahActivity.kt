package com.example.cashflow

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.example.cashflow.data.transaksi.TransaksiViewModel
import com.example.cashflow.data.user.UserViewModel
import com.example.cashflow.databinding.ActivityTambahBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*

class TambahActivity : AppCompatActivity() {

    private val userViewModel : UserViewModel by viewModels()
    private val transaksiViewModel : TransaksiViewModel by viewModels()
    private lateinit var binding: ActivityTambahBinding
    private lateinit var date: Date
    private lateinit var jenis: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            binding.etDate.setText("$dayOfMonth/${month + 1}/$year")
            val parseDate = "$year-${month + 1}-$dayOfMonth"
            date = SimpleDateFormat("yyyy-MM-dd").parse(parseDate)
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

        binding.btnDate.setOnClickListener {
            dialog.show()
        }
//        binding.btnTambah2.setOnClickListener {
//            val dialog = BottomSheetDialog(this)
//
//            val view = layoutInflater.inflate(R.layout.bottom_sheet, null)
//            dialog.setContentView(view)
//            dialog.show()
//        }
        binding.spnJenis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                jenis = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.btnTambah.setOnClickListener {
            transaksiViewModel.addTransaksi(binding.etKategori.text.toString(), binding.etDeskripsi.text.toString(), binding.etJumlah.text.toString().toDouble(), jenis, date)
            finish()
        }
    }
}