package com.example.cashflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler


class Splashscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        supportActionBar?.hide()

        Handler().postDelayed({
            startActivity(Intent(this@Splashscreen, AuthActivity::class.java))
            finish()
        }, 3000)

    }
}