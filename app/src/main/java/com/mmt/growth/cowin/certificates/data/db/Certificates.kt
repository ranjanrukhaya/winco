package com.mmt.growth.cowin.certificates.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "certificates_table")
data class Certificates(
    var uuid: String? = null,
    @PrimaryKey(autoGenerate = false)
    var beneficiaryRefId: String,
    var name: String,
    var registeredMobile: String,
    var birthYear: String,
    var gender: String,
    var doseRequired: Int,
    var vaccinationStatus: String,
    var vaccine: String,
    var doseDetail1Date: Long? = null,
    var doseDetail2Date: Long? = null,
    var certificateType: String,
    var isActive: Boolean,
    var certificateUrl: String,
    var nextAppointmentDate: Long? = null,
    var nextAppointmentSlot: String? = null,
    var nextAppointmentDose: Int? = null,
    var updatedDateApi: Long,
    var cotravelerId: String? = null,
    var localFileName: String? = null,
    var createdDate: Long,
    var updatedDate: Long
) {

}