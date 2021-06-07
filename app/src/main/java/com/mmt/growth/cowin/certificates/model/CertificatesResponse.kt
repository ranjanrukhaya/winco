package com.mmt.growth.cowin.certificates.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CertificatesResponse(
    val code: Int,
    val message: String,
    val result: Result?,
    var success: Boolean
)

data class Result(
    val extendedUser: ExtendedUser?
)

data class ExtendedUser(
    val cowinCertificates: List<CowinCertificate>?
)

data class CowinCertificate(
    val beneficiaryList: List<Beneficiary>?,
    val mobileNumber: String
)

@Parcelize
data class Beneficiary(
    val beneficiaryRefId: String,
    val birthYear: String,
    val certificateType: String,
    val certificateUrl: String,
    val doseDetails: DoseDetails?,
    val doseRequired: Int,
    val gender: String,
    val isActive: Boolean,
    val name: String,
    val nextAppointment: NextAppointment?,
    val updatedDate: Long,
    val vaccinationStatus: String,
    val vaccine: String
) : Parcelable

@Parcelize
data class NextAppointment(
    val appointmentDate: Long,
    val dose: Int,
    val slot: String
) : Parcelable

@Parcelize
data class DoseDetails(
    val dose1: Dose?,
    val dose2: Dose?
) : Parcelable

@Parcelize
data class Dose(
    val date: Long
) : Parcelable
