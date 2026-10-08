package com.example.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// Generic API response structure used by PreCare shared backend
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ApiError? = null
)

data class ApiError(
    val code: String,
    val message: String
)

data class ProfileDto(
    val id: String? = null,
    val name: String,
    val age: String,
    val bloodGroup: String,
    val allergies: String,
    val medicalConditions: String
)

data class PregnancyDto(
    val profileId: String,
    val lastMenstrualPeriod: String,
    val estimatedDueDate: String,
    val currentWeek: Int,
    val trimester: Int,
    val firstPregnancy: Boolean
)

data class AppointmentDto(
    val id: String? = null,
    val profileId: String,
    val doctorName: String,
    val hospitalName: String,
    val date: String,
    val time: String,
    val notes: String
)

data class MayaChatRequest(
    val profileId: String,
    val message: String,
    val currentWeek: Int? = null
)

data class MayaChatResponse(
    val safetyLevel: String, // NORMAL, ATTENTION, EMERGENCY
    val response: String,
    val seekImmediateCare: Boolean
)

data class HospitalDto(
    val name: String,
    val address: String,
    val distance: String,
    val phone: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val openNow: Boolean = true
)

interface PreCareApi {
    @POST("profiles")
    suspend fun createProfile(@Body profile: ProfileDto): Response<ApiResponse<ProfileDto>>

    @GET("profiles/{id}")
    suspend fun getProfile(@Path("id") id: String): Response<ApiResponse<ProfileDto>>

    @POST("pregnancy")
    suspend fun savePregnancy(@Body pregnancy: PregnancyDto): Response<ApiResponse<PregnancyDto>>

    @GET("pregnancy/{profileId}")
    suspend fun getPregnancy(@Path("profileId") profileId: String): Response<ApiResponse<PregnancyDto>>

    @POST("appointments")
    suspend fun createAppointment(@Body appointment: AppointmentDto): Response<ApiResponse<AppointmentDto>>

    @GET("appointments/{profileId}")
    suspend fun getAppointments(@Path("profileId") profileId: String): Response<ApiResponse<List<AppointmentDto>>>

    @DELETE("appointments/{id}")
    suspend fun deleteAppointment(@Path("id") id: String): Response<ApiResponse<Unit>>

    @POST("maya/chat")
    suspend fun sendMayaMessage(@Body request: MayaChatRequest): Response<ApiResponse<MayaChatResponse>>

    @GET("hospitals/nearby")
    suspend fun getNearbyHospitals(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<ApiResponse<List<HospitalDto>>>
}

object NetworkClient {
    // Centralized API configuration (can be updated via settings or environment)
    var baseUrl: String = "http://10.0.2.2:5000/api/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    val api: PreCareApi by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PreCareApi::class.java)
    }
}
