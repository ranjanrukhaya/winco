package com.mmt.growth.cowin.pdfviewer.ui

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.mmt.databinding.ActivityCeritificateViewerBinding
import com.mmt.growth.cowin.CowinConstants
import com.mmt.growth.cowin.certificates.model.Beneficiary

class CertificateViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCeritificateViewerBinding
    private var beneficiary: Beneficiary? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCeritificateViewerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        beneficiary = intent.getParcelableExtra(CowinConstants.BENEFICIARY_KEY)

        binding.webviewCertificate.webViewClient = WebViewClient()
        binding.webviewCertificate.settings.setSupportZoom(true)
        binding.webviewCertificate.settings.javaScriptEnabled = true

        beneficiary?.let {
            binding.viewerHeader.text = it.name
            binding.viewerSubHeader.text = it.gender + ", " + it.birthYear
            binding.webviewCertificate.loadUrl(CowinConstants.LOAD_URL + it.certificateUrl)
        }

        binding.backIcon.setOnClickListener {
            onBackPressed()
        }
    }
}