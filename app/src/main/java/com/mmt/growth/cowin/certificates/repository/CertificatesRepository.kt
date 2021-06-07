package com.mmt.growth.cowin.certificates.repository

import com.mmt.growth.cowin.certificates.data.CowinService
import com.mmt.growth.cowin.certificates.model.CertificatesResponse
import com.mmt.growth.cowin.certificates.util.Resource
import io.reactivex.Observable

class CertificatesRepository {

    fun fetchBeneficiaries(): Observable<CertificatesResponse> {
        return CowinService.create().getCertificates()
    }

    suspend fun getCertificatesCoroutine(): CertificatesResponse {
        return CowinService.create().getCertificatesCoroutine()
        //return CowinService.create().getEmptyCertificatesCoroutine()
        //return CowinService.create().getErrorCertificatesCoroutine()
    }

    suspend fun getCertificateResource(): Resource<CertificatesResponse> {
        val response = try {
            CowinService.create().getEmptyCertificatesCoroutine()
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "An error occured")
        }

        return Resource.Success(response)
    }
}