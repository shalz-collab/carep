package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.PreCareDao
import com.example.data.local.entity.AppointmentEntity
import com.example.data.local.entity.EmergencyContact
import com.example.data.local.entity.FirstAidItem
import com.example.data.local.entity.MedicalReportEntity
import com.example.data.local.entity.PregnancyInfo
import com.example.data.local.entity.PregnancyWeekInfo
import com.example.data.local.entity.UserProfile

@Database(
    entities = [
        UserProfile::class,
        PregnancyInfo::class,
        EmergencyContact::class,
        AppointmentEntity::class,
        MedicalReportEntity::class,
        FirstAidItem::class,
        PregnancyWeekInfo::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PreCareDatabase : RoomDatabase() {
    abstract fun preCareDao(): PreCareDao

    companion object {
        @Volatile
        private var INSTANCE: PreCareDatabase? = null

        fun getInstance(context: Context): PreCareDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PreCareDatabase::class.java,
                    "precare_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
