package com.example.cashflow.data.transaksi

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.cashflow.R
import java.time.ZoneId

class TransaksiAdapter() : RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder>() {

    private val _listTransaksi: MutableList<Transaksi> = mutableListOf()
    private val _listIdTransaksi: MutableList<String> = mutableListOf()
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(idTransaksi: String, data: Transaksi)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class TransaksiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvKategori : TextView = itemView.findViewById(R.id.tvKategori)
        var tvDeskripsi : TextView = itemView.findViewById(R.id.tvDeskripsi)
        var tvJumlah : TextView = itemView.findViewById(R.id.tvJumlah)
        var tvJenis : TextView = itemView.findViewById(R.id.tvJenis)
        var tvDate : TextView = itemView.findViewById(R.id.tvDate)
        var btnDelete : ImageButton = itemView.findViewById(R.id.btnDelete)
        var layout : LinearLayout = itemView.findViewById(R.id.layoutList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_transaksi, parent, false)
        return TransaksiViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        holder.itemView.apply {
            holder.tvKategori.text = _listTransaksi[position].kategori
            holder.tvDeskripsi.text = _listTransaksi[position].deskripsi
            holder.tvJenis.text = _listTransaksi[position].jenis
            val hari = _listTransaksi[position].tanggal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().dayOfMonth
            val bulan = _listTransaksi[position].tanggal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().monthValue
            val tahun = _listTransaksi[position].tanggal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().year
            holder.tvDate.text = "$hari/$bulan/$tahun"
            holder.tvJumlah.text = _listTransaksi[position].jumlah.toString()
            if(_listTransaksi[position].jenis == "Expenses"){
                holder.layout.setBackgroundResource(R.drawable.rounded4)
            }

            holder.btnDelete.setOnClickListener {onItemClickCallback.onItemClicked(_listIdTransaksi[holder.adapterPosition],_listTransaksi[holder.adapterPosition])}

        }
    }

    override fun getItemCount(): Int {
        return _listTransaksi.size
    }

    fun updateListData(newTransaksi : MutableList<Transaksi>, newIdTransaksi: MutableList<String>){
        _listTransaksi.clear()
        _listTransaksi.addAll(newTransaksi)

        _listIdTransaksi.clear()
        _listIdTransaksi.addAll(newIdTransaksi)
    }
}