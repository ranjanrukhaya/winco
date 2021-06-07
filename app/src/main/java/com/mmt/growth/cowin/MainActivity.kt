package com.mmt.growth.cowin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mmt.R
import com.mmt.databinding.ActivityMainBinding
import com.mmt.growth.cowin.certificates.ui.UserCertificatesActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.api.setOnClickListener {
            val intent = Intent(this, UserCertificatesActivity::class.java)
            intent.putExtra(CowinConstants.MOBILE_KEY, "9988776655")
            intent.putExtra(CowinConstants.TOKEN_KEY, "sxs2dsad2s")
            startActivity(intent)
        }

        binding.db.setOnClickListener {
            val intent = Intent(this, UserCertificatesActivity::class.java)
            startActivity(intent)
        }
    }
}