package com.mmt.growth.cowin.certificates.data.db

import androidx.room.*

@Dao
interface CertificatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(certificatesList: List<Certificates>)

    @Query("SELECT * from certificates_table")
    fun getAllCertificates(): List<Certificates>

    @Update(entity = Certificates::class)
    fun update(newCertificates: Certificates)

    @Query("UPDATE certificates_table SET localFileName = :file, updatedDate =:updateTime WHERE beneficiaryRefId = :id")
    fun updateCertificate(file: String, id: String, updateTime: Long)
}