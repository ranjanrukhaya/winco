package com.mmt.growth.cowin.certificates.ui

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import com.mmt.growth.cowin.CowinConstants
import com.mmt.growth.cowin.certificates.data.db.Certificates
import com.mmt.growth.cowin.certificates.data.db.CertificatesDb
import com.mmt.growth.cowin.certificates.model.CertificatesResponse
import com.mmt.growth.cowin.certificates.repository.CertificatesDbRepository
import com.mmt.growth.cowin.certificates.repository.CertificatesDummyRepo
import com.mmt.growth.cowin.certificates.repository.CertificatesRepository
import com.mmt.growth.cowin.certificates.service.PdfDownloaderService
import com.mmt.growth.cowin.certificates.util.Resource
import com.mmt.growth.cowin.certificates.util.UserCertificatesUtil.getCertificateResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.URL


class UserCertificatesViewModel(
    private val context: Context,
    val mobile: String?,
    val token: String?
) : ViewModel() {

    private var certificates: MutableLiveData<CertificatesResponse> = MutableLiveData()
    private var certificatesRepository: CertificatesRepository
    private lateinit var certificatesDbRepository: CertificatesDbRepository

    //private var certificateResource : MutableLiveData<Resource<CertificatesResponse>> = MutableLiveData()
    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        certificatesRepository = CertificatesRepository()
        initDatabase()
        //getCertificatesRx()
        getCertificatesCoroutine()
        //getCertificateResource()
        //getDummyCertificates()
    }

    private fun initDatabase() {
        val certificatesDao = CertificatesDb.getInstance(context).certificatesDao()
        certificatesDbRepository = CertificatesDbRepository(certificatesDao)
    }

    private fun getCertificateResource() {
        viewModelScope.launch {
            val result = certificatesRepository.getCertificateResource()
            //certificateResource = result
            when (result) {
                is Resource.Success -> {
                    certificates.value = result.data
                }
                is Resource.Error -> {

                }
            }
        }
    }

    private fun getCertificatesCoroutine() {
        var certificatesApi: CertificatesResponse
        var certificatesFromDb = emptyList<Certificates>()

        loading.value = true
        viewModelScope.launch {
            if (token == null) {
                delay(2000)
                certificatesFromDb = certificatesDbRepository.getAllCertificates()
                certificates.value = getCertificateResponse(certificatesFromDb)
                loading.value = false
            } else {
                certificatesApi = certificatesRepository.getCertificatesCoroutine()
                certificatesFromDb = certificatesDbRepository.getAllCertificates()
                certificates.value = certificatesApi
                loading.value = false

                // Insert in db
                if (certificatesApi.success
                    && certificatesApi.result?.extendedUser?.cowinCertificates?.isNotEmpty() == true
                ) {
                    certificatesDbRepository.insert(certificatesApi)
                    // Download certificates in internal storage and update table
                    downloadCertificateToInternalStorage(certificatesApi)
                    
                }
            }

        }

    }

    private fun downloadCertificateToInternalStorage(certificatesResponse: CertificatesResponse) {
        certificatesResponse.result?.extendedUser?.cowinCertificates?.forEach { cowinCertificate ->
            cowinCertificate.beneficiaryList?.forEach { beneficiary ->

                val downloaderService = Intent(context, PdfDownloaderService::class.java).apply {
                    putExtra(CowinConstants.BENEFICIARY_KEY, beneficiary)
                }
                context.startService(downloaderService)


                /*try {
                    val url = URL(beneficiary.certificateUrl)
                    val inputStream = url.openStream()
                    val byteArray = inputStream.readBytes()
                    val outputStream =
                        context.openFileOutput(beneficiary.beneficiaryRefId + ".pdf", MODE_PRIVATE)
                    outputStream.write(byteArray)

                    inputStream.close();
                    outputStream.close()
                } catch (e: Exception) {
                    e.message?.let { Log.e("Model", it) }
                }*/

            }
        }
    }

    private fun getDummyCertificates() {
        certificates.value = CertificatesDummyRepo.fetchCertificates()
    }

    private fun getCertificatesRx() {
        certificatesRepository.fetchBeneficiaries()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    handleResult(it)
                }, {
                    handleError(it)
                }
            )
        //certificatesRepository.fetchBeneficiaries()

    }

    private fun handleError(throwable: Throwable) {
        Log.d("UserCertificatesView", throwable.message ?: "Error")
    }

    private fun handleResult(certificatesResponse: CertificatesResponse) {
        certificates.value = certificatesResponse
    }


    fun getCertificatesData(): LiveData<CertificatesResponse> {
        return certificates
    }

    fun isLoading(): LiveData<Boolean> = loading
}