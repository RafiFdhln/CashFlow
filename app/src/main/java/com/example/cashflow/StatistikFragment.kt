package com.example.cashflow

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cashflow.data.transaksi.*
import com.example.cashflow.data.user.User
import com.example.cashflow.data.user.UserViewModel
import com.example.cashflow.databinding.FragmentStatistikBinding
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ir.mahozad.android.PieChart
import java.text.SimpleDateFormat

class StatistikFragment : Fragment(R.layout.fragment_statistik) {
    private lateinit var binding: FragmentStatistikBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatistikBinding.bind(view)


        val collection = Firebase.firestore.collection("/users/cakrapand@gmail.com/transaksi")
        val query1 = collection.whereEqualTo("jenis", "Income")
        val countQuery1 = query1.count()
        countQuery1.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            val snapshot = task.result
            val Income = "${snapshot.count}".toFloat()
            val pieChart = binding.pieChart
            binding.tvCount.text = "${snapshot.count}"

            pieChart.slices = listOf(
                PieChart.Slice(Income, Color.BLUE),
            )
        }

    }
}