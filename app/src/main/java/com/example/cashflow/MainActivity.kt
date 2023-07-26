package com.example.cashflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.cashflow.data.transaksi.Transaksi
import com.example.cashflow.data.transaksi.TransaksiRepository
import com.example.cashflow.data.transaksi.TransaksiViewModel
import com.example.cashflow.data.user.User
import com.example.cashflow.data.user.UserRepository
import com.example.cashflow.data.user.UserViewModel
import com.example.cashflow.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val userViewModel : UserViewModel by viewModels()
    private val transaksiViewModel : TransaksiViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val homeFragment = HomeFragment()
    private val transaksiFragment = TransaksiFragment()
    private val statistikFragment = StatistikFragment()
    private val profilFragment = ProfilFragment()
    private var title : String = "Home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.navBottom.menu.get(2).isEnabled = false
        setActionBarTitle(title)

        userViewModel.user.observe(this, Observer<FirebaseUser>{ user ->
            if(user == null){
                startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                finish()
            }
        })

        binding.navBottom.setOnItemSelectedListener {
            setMode(it.itemId)
            true
        }
    }


    fun setMode(selectedMode: Int) {
        when (selectedMode){
            R.id.home -> {
                navigateToFragment(homeFragment)
                title ="Home"
            }
            R.id.transaksi -> {
                navigateToFragment(transaksiFragment)
                title ="Transaksi"
            }
//            R.id.statistik -> {
//                navigateToFragment(statistikFragment)
//                title ="Statistik"
//            }
            R.id.profil -> {
                navigateToFragment(profilFragment)
                title ="Profil"
            }
        }
        setActionBarTitle(title)
    }

    private fun navigateToFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }
}