package com.mmt.growth.cowin.certificates.repository

import com.mmt.growth.cowin.certificates.data.db.Certificates
import com.mmt.growth.cowin.certificates.data.db.CertificatesDao
import com.mmt.growth.cowin.certificates.model.CertificatesResponse

class CertificatesDbRepository(private val certificatesDao: CertificatesDao) {

    val certificatesList: List<Certificates> = certificatesDao.getAllCertificates()

    suspend fun insert(certificatesResponse: CertificatesResponse) {
        val certificates = mutableListOf<Certificates>()
        certificatesResponse.result?.extendedUser?.cowinCertificates?.forEach { cowinCertificate ->
            cowinCertificate.beneficiaryList?.forEach { beneficiary ->
                val certificate = Certificates(
                    null,
                    beneficiary.beneficiaryRefId,
                    beneficiary.name,
                    cowinCertificate.mobileNumber,
                    beneficiary.birthYear,
                    beneficiary.gender,
                    beneficiary.doseRequired,
                    beneficiary.vaccinationStatus,
                    beneficiary.vaccine,
                    beneficiary.doseDetails?.dose1?.date,
                    beneficiary.doseDetails?.dose2?.date,
                    beneficiary.certificateType,
                    beneficiary.isActive,
                    beneficiary.certificateUrl,
                    beneficiary.nextAppointment?.appointmentDate,
                    beneficiary.nextAppointment?.slot,
                    beneficiary.nextAppointment?.dose,
                    beneficiary.updatedDate,
                    null,
                    null,
                    null,
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
                )
                certificates.add(certificate)
            }

        }
        if (certificates.isNotEmpty()) {
            certificatesDao.insert(certificates)
        }
    }

    suspend fun update() {

    }

    suspend fun getAllCertificates(): List<Certificates> {
        return certificatesList
    }
}