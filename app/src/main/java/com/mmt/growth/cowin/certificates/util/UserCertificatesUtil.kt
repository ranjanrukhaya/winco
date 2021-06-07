package com.mmt.growth.cowin.certificates.util

import com.mmt.growth.cowin.certificates.data.db.Certificates
import com.mmt.growth.cowin.certificates.model.*

object UserCertificatesUtil {

    fun getBeneficiaryCount(certificates: List<CowinCertificate>): Int {
        var total = 0
        certificates.forEach { cowinCertificate ->
            total += cowinCertificate.beneficiaryList?.size ?: 0
        }
        return total
    }

    fun getCertificateResponse(certificates: List<Certificates>): CertificatesResponse {

        val certificateMobileMap = mutableMapOf<String, MutableList<Beneficiary>>()
        certificates.forEach { certificate ->
            val doseDetails = DoseDetails(certificate.doseDetail1Date?.let { Dose(it) },
                certificate.doseDetail2Date?.let { Dose(it) })
            val nextAppointment = certificate.nextAppointmentDate?.let {
                certificate.nextAppointmentDose?.let { it1 ->
                    certificate.nextAppointmentSlot?.let { it2 ->
                        NextAppointment(
                            it,
                            it1,
                            it2
                        )
                    }
                }
            }
            val beneficiary = Beneficiary(
                certificate.beneficiaryRefId,
                certificate.birthYear,
                certificate.certificateType,
                certificate.certificateUrl,
                doseDetails,
                certificate.doseRequired,
                certificate.gender,
                certificate.isActive,
                certificate.name,
                nextAppointment,
                certificate.updatedDateApi,
                certificate.vaccinationStatus,
                certificate.vaccine
            )
            val currentList =
                certificateMobileMap[certificate.registeredMobile] ?: mutableListOf<Beneficiary>()
            currentList.add(beneficiary)
            certificateMobileMap[certificate.registeredMobile] = currentList
        }
        val cowinCertificates = mutableListOf<CowinCertificate>()
        certificateMobileMap.forEach { (mobile, list) ->
            run {
                val cowinCertificate = CowinCertificate(list, mobile)
                cowinCertificates.add(cowinCertificate)
            }
        }

        val extendedUser = ExtendedUser(cowinCertificates)
        val result = Result(extendedUser)
        return CertificatesResponse(200, "Success", result, true)
    }
}
