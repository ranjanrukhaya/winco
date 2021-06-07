package com.mmt.growth.cowin.certificates.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Certificates::class], version = 1)
abstract class CertificatesDb : RoomDatabase() {

    abstract fun certificatesDao(): CertificatesDao

    companion object {

        @Volatile
        private var INSTANCE: CertificatesDb? = null

        @JvmStatic
        fun getInstance(context: Context): CertificatesDb =
            (INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }) as CertificatesDb

        private fun buildDatabase(context: Context) =
            context.applicationContext?.let {
                Room.databaseBuilder(it, CertificatesDb::class.java, "cowin.db")
                    .allowMainThreadQueries()
                    .build()
            }
    }
}
