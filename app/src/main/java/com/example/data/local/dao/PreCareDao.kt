package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.AppointmentEntity
import com.example.data.local.entity.EmergencyContact
import com.example.data.local.entity.FirstAidItem
import com.example.data.local.entity.MedicalReportEntity
import com.example.data.local.entity.PregnancyInfo
import com.example.data.local.entity.PregnancyWeekInfo
import com.example.data.local.entity.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface PreCareDao {
    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfileOnce(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfile)

    // Pregnancy Info
    @Query("SELECT * FROM pregnancy_info WHERE id = 1")
    fun getPregnancyInfo(): Flow<PregnancyInfo?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePregnancyInfo(info: PregnancyInfo)

    // Emergency Contact
    @Query("SELECT * FROM emergency_contact WHERE id = 1")
    fun getEmergencyContact(): Flow<EmergencyContact?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEmergencyContact(contact: EmergencyContact)

    // Appointments
    @Query("SELECT * FROM appointments ORDER BY id DESC")
    fun getAllAppointments(): Flow<List<AppointmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity): Long

    @Update
    suspend fun updateAppointment(appointment: AppointmentEntity)

    @Query("DELETE FROM appointments WHERE id = :id")
    suspend fun deleteAppointment(id: Long)

    // Medical Reports
    @Query("SELECT * FROM medical_reports ORDER BY id DESC")
    fun getAllMedicalReports(): Flow<List<MedicalReportEntity>>

    @Query("SELECT * FROM medical_reports WHERE id = :id")
    suspend fun getMedicalReportById(id: Long): MedicalReportEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicalReport(report: MedicalReportEntity): Long

    @Query("DELETE FROM medical_reports WHERE id = :id")
    suspend fun deleteMedicalReport(id: Long)

    // First Aid Items
    @Query("SELECT * FROM first_aid_items ORDER BY title ASC")
    fun getAllFirstAidItems(): Flow<List<FirstAidItem>>

    @Query("SELECT * FROM first_aid_items WHERE id = :id")
    suspend fun getFirstAidItemById(id: String): FirstAidItem?

    @Query("SELECT * FROM first_aid_items WHERE title LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' OR summary LIKE '%' || :query || '%'")
    fun searchFirstAid(query: String): Flow<List<FirstAidItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFirstAidItems(items: List<FirstAidItem>)

    @Query("SELECT COUNT(*) FROM first_aid_items")
    suspend fun getFirstAidCount(): Int

    // Pregnancy Weeks
    @Query("SELECT * FROM pregnancy_weeks ORDER BY weekNumber ASC")
    fun getAllPregnancyWeeks(): Flow<List<PregnancyWeekInfo>>

    @Query("SELECT * FROM pregnancy_weeks WHERE weekNumber = :weekNumber")
    suspend fun getPregnancyWeek(weekNumber: Int): PregnancyWeekInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPregnancyWeeks(weeks: List<PregnancyWeekInfo>)

    @Query("SELECT COUNT(*) FROM pregnancy_weeks")
    suspend fun getPregnancyWeeksCount(): Int

    // Reset Data
    @Query("DELETE FROM user_profile")
    suspend fun clearUserProfile()

    @Query("DELETE FROM pregnancy_info")
    suspend fun clearPregnancyInfo()

    @Query("DELETE FROM emergency_contact")
    suspend fun clearEmergencyContact()

    @Query("DELETE FROM appointments")
    suspend fun clearAppointments()

    @Query("DELETE FROM medical_reports")
    suspend fun clearMedicalReports()
}
