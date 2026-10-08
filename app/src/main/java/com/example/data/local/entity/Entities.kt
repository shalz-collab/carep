package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val fullName: String = "",
    val age: String = "",
    val bloodGroup: String = "",
    val allergies: String = "",
    val medicalConditions: String = "",
    val isSetupCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "pregnancy_info")
data class PregnancyInfo(
    @PrimaryKey val id: Int = 1,
    val isPregnant: Boolean = true,
    val lmpDate: String = "", // YYYY-MM-DD
    val eddDate: String = "", // YYYY-MM-DD
    val currentWeek: Int = 24,
    val isFirstPregnancy: Boolean = true,
    val notes: String = ""
)

@Entity(tableName = "emergency_contact")
data class EmergencyContact(
    @PrimaryKey val id: Int = 1,
    val contactName: String = "",
    val phoneNumber: String = "",
    val relationship: String = "Partner / Family"
)

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val doctorName: String,
    val specialization: String = "Obstetrician & Gynecologist",
    val hospitalName: String,
    val date: String,
    val time: String,
    val notes: String = "",
    val status: String = "Confirmed", // Confirmed, Completed, Cancelled
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "medical_reports")
data class MedicalReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val reportDate: String,
    val reportType: String, // Blood Test, Ultrasound, Urine Routine, Glucose Tolerance
    val extractedText: String = "",
    val validationStatus: String = "VALID_MEDICAL_REPORT",
    val summary: String = "",
    val rawJsonResults: String = "", // JSON string of extracted lab values
    val aiAssessment: String = "",
    val urgencyLevel: String = "Normal", // Normal, Attention, Urgent
    val recommendedDoctorType: String = "Gynecologist",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "first_aid_items")
data class FirstAidItem(
    @PrimaryKey val id: String,
    val title: String,
    val category: String, // Pregnancy First Aid, General First Aid, Women's Health, Emergency Warning Signs
    val summary: String,
    val whatMayBeHappening: String,
    val immediateSteps: String, // Newline separated or JSON
    val thingsToAvoid: String,
    val whenToContactDoctor: String,
    val emergencyWarning: String,
    val isEmergency: Boolean = false
)

@Entity(tableName = "pregnancy_weeks")
data class PregnancyWeekInfo(
    @PrimaryKey val weekNumber: Int,
    val trimester: Int, // 1, 2, 3
    val babyDevelopment: String,
    val maternalChanges: String,
    val careGuidance: String,
    val nutritionGuidance: String,
    val commonSymptoms: String,
    val warningSigns: String,
    val babySizeComparison: String
)
