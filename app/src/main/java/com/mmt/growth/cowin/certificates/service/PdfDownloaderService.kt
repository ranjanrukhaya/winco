package com.mmt.growth.cowin.certificates.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.mmt.growth.cowin.CowinConstants
import com.mmt.growth.cowin.certificates.data.db.CertificatesDb
import com.mmt.growth.cowin.certificates.model.Beneficiary
import com.mmt.growth.cowin.certificates.repository.CertificatesDbRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.URL

class PdfDownloaderService : IntentService(TAG) {

    companion object {
        private const val TAG = "PdfDownloaderService"
    }

    lateinit var certificatesDbRepository: CertificatesDbRepository

    override fun onCreate() {
        super.onCreate()
        val certificatesDao =
            CertificatesDb.getInstance(this@PdfDownloaderService).certificatesDao()
        certificatesDbRepository = CertificatesDbRepository(certificatesDao)
    }

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            CoroutineScope(Dispatchers.IO).launch {
                val beneficiary =
                    intent.getParcelableExtra<Beneficiary>(CowinConstants.BENEFICIARY_KEY)
                beneficiary?.certificateUrl?.let {
                    try {
                        val url = URL(it)
                        val inputStream = url.openStream()
                        val byteArray = inputStream.readBytes()
                        val fileName = beneficiary.beneficiaryRefId + ".pdf"
                        val outputStream =
                            this@PdfDownloaderService.openFileOutput(
                                fileName,
                                MODE_PRIVATE
                            )
                        outputStream.write(byteArray)

                        inputStream.close()
                        outputStream.close()
                        certificatesDbRepository.updateCertificate(
                            fileName,
                            beneficiary.beneficiaryRefId,
                            System.currentTimeMillis()
                        )
                    } catch (e: Exception) {
                        e.message?.let { Log.e("Model", it) }
                    }
                }

            }
        }
    }


}