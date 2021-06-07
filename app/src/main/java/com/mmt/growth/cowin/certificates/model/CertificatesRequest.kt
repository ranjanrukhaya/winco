package com.mmt.growth.cowin.certificates.model

data class CertificatesRequest(
    val mobile: String,
    val token: String? = null
)