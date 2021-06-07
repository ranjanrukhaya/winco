package com.mmt.growth.cowin.certificates.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CertificatesDao {

    @Insert
    fun insert(certificatesList: List<Certificates>)

    @Query("SELECT * from certificates_table")
    fun getAllCertificates(): List<Certificates>
}