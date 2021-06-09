package com.mmt.growth.cowin.pdfviewer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.mmt.BuildConfig
import com.mmt.R
import com.mmt.databinding.ActivityCeritificateViewerBinding
import com.mmt.growth.cowin.CowinConstants
import com.mmt.growth.cowin.certificates.model.Beneficiary
import java.io.File

class CertificateViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCeritificateViewerBinding
    private var beneficiary: Beneficiary? = null
    private var certificateUri: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCeritificateViewerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        beneficiary = intent.getParcelableExtra(CowinConstants.BENEFICIARY_KEY)

        binding.webviewCertificate.webViewClient = WebViewClient()
        binding.webviewCertificate.settings.setSupportZoom(true)
        binding.webviewCertificate.settings.javaScriptEnabled = true
        binding.webviewCertificate.settings.allowContentAccess = true
        binding.webviewCertificate.settings.allowFileAccess = true

        beneficiary?.let {
            binding.viewerHeader.text = it.name
            binding.viewerSubHeader.text = it.gender + ", " + it.birthYear
            //binding.webviewCertificate.loadUrl(CowinConstants.LOAD_URL + it.certificateUrl)
            loadPhotoFromInternalStorage(it.beneficiaryRefId + ".pdf")

        }

//        val st = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/certificate.pdf");
//        binding.webviewCertificate.loadUrl(st.absolutePath)
//        binding.pdfView.fromFile(st).show()

        binding.sharePdf.setOnClickListener {
            shareCertificate()
        }

        binding.backIcon.setOnClickListener {
            onBackPressed()
        }
    }

    private fun shareCertificate() {
        certificateUri?.let {
            val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, it);
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
            shareIntent.type = "*/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_title))
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)))
        }
    }

    private fun loadPhotoFromInternalStorage(fileName: String) {
        val files = filesDir.listFiles()
        files?.filter { it.canRead() && it.isFile && it.name == fileName }?.map {
            certificateUri = it
            binding.pdfView.fromFile(it).show()
            //binding.webviewCertificate.loadUrl(it.absolutePath)
        }
    }
}