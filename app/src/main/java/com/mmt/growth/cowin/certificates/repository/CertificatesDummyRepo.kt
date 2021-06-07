package com.mmt.growth.cowin.certificates.repository

import com.mmt.growth.cowin.certificates.model.*

object CertificatesDummyRepo {
    fun fetchCertificates(): CertificatesResponse = certificates
}

private val beneficiary1 =
    Beneficiary(
        "121",
        "1987",
        "Type",
        "www.google.com",
        null,
        1,
        "Male",
        true,
        "Ranjan",
        NextAppointment(1622092640568, 2, "9 to 11"),
        1622092640568,
        "PARTIAL_VACCINATED",
        "Covishield"
    )

private val beneficiary2 =
    Beneficiary(
        "121",
        "1991",
        "Type",
        "www.google.com",
        null,
        2,
        "Female",
        true,
        "Isha",
        NextAppointment(1624406400000, 2, "9 to 11"),
        1622092640568,
        "PARTIAL_VACCINATED",
        "Covaxin"
    )

private val beneficiary3 =
    Beneficiary(
        "121",
        "1991",
        "Type",
        "www.google.com",
        null,
        1,
        "Female",
        true,
        "Isha",
        null,
        1622092640568,
        "FULLY_VACCINATED",
        "Covaxin"
    )

private val beneficiary4 =
    Beneficiary(
        "121",
        "1991",
        "Type",
        "www.google.com",
        null,
        1,
        "Female",
        true,
        "Isha",
        null,
        1622092640568,
        "NOT_VACCINATED",
        "Covaxin"
    )

private val beneficiary5 =
    Beneficiary(
        "121",
        "1991",
        "Type",
        "www.google.com",
        null,
        2,
        "Female",
        true,
        "Isha",
        null,
        1622092640568,
        "NOT_VACCINATED",
        "Covaxin"
    )

private val beneficiary6 =
    Beneficiary(
        "121",
        "1991",
        "Type",
        "www.google.com",
        null,
        1,
        "Female",
        true,
        "Isha",
        NextAppointment(1624406400000, 1, "9 to 11"),
        1622092640568,
        "NOT_VACCINATED",
        "Covaxin"
    )

private val cowinCertificates1 =
    CowinCertificate(listOf(beneficiary6, beneficiary4, beneficiary5), "995392923")
private val cowinCertificates2 = CowinCertificate(listOf(beneficiary3), "885392923")
private val cowinCertificates3 = CowinCertificate(listOf(beneficiary2, beneficiary1), "775392923")

private val extendedUser =
    //ExtendedUser(listOf(cowinCertificates1))
    ExtendedUser(listOf(cowinCertificates1, cowinCertificates2, cowinCertificates3))

private val result = Result(extendedUser)

private val certificates = CertificatesResponse(200, "Done", result, true)

