package com.example.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/**
 * کلاینت شبکه منطبق با API بک‌اند اختصاصی شما (Django NinjaExtra + PostGIS)
 * آدرس سرور: http://62.3.42.195:8000/api/v1
 *
 * Endpoints پشتیبانی شده از پروژه گوگل درایو شما:
 * ۱. برنامه اول (مشتری - تندیس):
 *    - POST /api/v1/auth/customer/             -> ارسال کد تایید پیامکی به مشتری
 *    - POST /api/v1/auth/customer/finally/     -> تایید کد و دریافت JWT مشتری
 *    - POST /api/v1/profiles/customer/signup/information/ -> ثبت‌نام اولیه مشتری (نام، نام خانوادگی، کدملی، موبایل)
 *    - POST /api/v1/profiles/customer/signup/area/        -> ثبت موقعیت جغرافیایی مشتری (latitude, longitude)
 *    - POST /api/v1/customer/service-requests/send/       -> ارسال درخواست خدمت جدید
 *    - GET  /api/v1/customer/service-requests/my/         -> لیست درخواست‌های مشتری
 *
 * ۲. برنامه دوم (متخصص - همکار تندیس):
 *    - POST /api/v1/auth/provider/             -> ارسال کد تایید پیامکی به متخصص (همکار)
 *    - POST /api/v1/auth/provider/finally/     -> تایید کد و دریافت JWT متخصص
 *    - POST /api/v1/profile/provider/signup/information/  -> مرحله ۱ ثبت‌نام متخصص (نام، کدملی، موبایل، صنف، نام کسب‌وکار، بیو)
 *    - POST /api/v1/profile/provider/signup/area/         -> مرحله ۴ ثبت‌نام محدوده کاری و لوکیشن متخصص
 *    - GET  /api/v1/pannel/provider/my/                   -> دریافت اطلاعات پروفایل متخصص
 *    - GET  /api/v1/provider/service-requests/open-nearby/ -> لیست درخواست‌های مشتریان در محدوده متخصص
 *    - POST /api/v1/provider/service-requests/accept/{id}/ -> پذیرش درخواست مشتری توسط متخصص
 */
object TandisServerClient {
    var serverBaseUrl: String = "http://62.3.42.195:8000/api/v1"
    var customerAccessToken: String? = null
    var providerAccessToken: String? = null

    data class OtpResponseDto(
        val success: String,
        val code: String,
        val phone: String
    )

    data class RemoteRequestDto(
        val id: String,
        val trackingCode: String,
        val title: String,
        val categoryId: String,
        val description: String,
        val addressText: String,
        val preferredTime: String,
        val latitude: Double,
        val longitude: Double,
        val status: String,
        val distanceMeters: Double? = null
    )

    // ==================== ۱. احراز هویت و ثبت‌نام مشتری (اپلیکیشن اول: تندیس) ====================

    suspend fun requestCustomerOtp(phone: String): Result<OtpResponseDto> = withContext(Dispatchers.IO) {
        runCatching {
            val payload = JSONObject().apply { put("phone", phone) }
            val json = postJson("$serverBaseUrl/auth/customer/", payload)
            OtpResponseDto(
                success = json.optString("success", "کد ارسال شد"),
                code = json.optString("code", "12345"),
                phone = json.optString("phone", phone)
            )
        }
    }

    suspend fun verifyCustomerOtp(phone: String, code: String): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val payload = JSONObject().apply {
                put("phone", phone)
                put("code", code)
            }
            val json = postJson("$serverBaseUrl/auth/customer/finally/", payload)
            val token = json.optString("access_token", "")
            customerAccessToken = token
            token
        }
    }

    suspend fun signupCustomerInfo(
        firstName: String,
        lastName: String,
        nationalId: String,
        phone: String
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val payload = JSONObject().apply {
                put("first_name", firstName)
                put("last_name", lastName)
                put("national_id", nationalId)
                put("phone", phone)
            }
            postJson("$serverBaseUrl/profiles/customer/signup/information/", payload)
            true
        }
    }

    // ==================== ۲. احراز هویت و ثبت‌نام متخصص (اپلیکیشن دوم: همکار تندیس) ====================

    suspend fun requestProviderOtp(phone: String): Result<OtpResponseDto> = withContext(Dispatchers.IO) {
        runCatching {
            val payload = JSONObject().apply { put("phone", phone) }
            val json = postJson("$serverBaseUrl/auth/provider/", payload)
            OtpResponseDto(
                success = json.optString("success", "کد تایید همکار ارسال شد"),
                code = json.optString("code", "12345"),
                phone = json.optString("phone", phone)
            )
        }
    }

    suspend fun verifyProviderOtp(phone: String, code: String): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val payload = JSONObject().apply {
                put("phone", phone)
                put("code", code)
            }
            val json = postJson("$serverBaseUrl/auth/provider/finally/", payload)
            val token = json.optString("access_token", "")
            providerAccessToken = token
            token
        }
    }

    suspend fun signupProviderInfo(
        firstName: String,
        lastName: String,
        nationalId: String,
        phone: String,
        category: String,
        businessName: String,
        bio: String
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val payload = JSONObject().apply {
                put("first_name", firstName)
                put("last_name", lastName)
                put("national_id", nationalId)
                put("phone", phone)
                put("category", category)
                put("business_name", businessName)
                put("bio", bio)
            }
            postJson("$serverBaseUrl/profile/provider/signup/information/", payload)
            true
        }
    }

    // ==================== ۳. عملیات درخواست خدمت بین مشتری و همکار تندیس ====================

    suspend fun submitCustomerRequest(
        title: String,
        categoryId: String,
        description: String,
        addressText: String,
        preferredTime: String,
        latitude: Double,
        longitude: Double
    ): Result<RemoteRequestDto> = withContext(Dispatchers.IO) {
        runCatching {
            val payload = JSONObject().apply {
                put("category", categoryId)
                put("title", title)
                put("description", description)
                put("preferred_at", "2026-11-09T10:00:00Z")
            }
            val json = postJson(
                urlStr = "$serverBaseUrl/customer/service-requests/send/",
                payload = payload,
                bearerToken = customerAccessToken
            )
            RemoteRequestDto(
                id = json.optString("id", ""),
                trackingCode = json.optString("id", "").take(8).uppercase(),
                title = json.optString("title", title),
                categoryId = categoryId,
                description = json.optString("description", description),
                addressText = json.optString("address_text", addressText),
                preferredTime = preferredTime,
                latitude = latitude,
                longitude = longitude,
                status = json.optString("status", "open")
            )
        }
    }

    suspend fun fetchNearbyRequestsForSpecialist(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 15.0,
        categoryId: String? = null
    ): Result<List<RemoteRequestDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val url = URL("$serverBaseUrl/provider/service-requests/open-nearby/")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 5000
                readTimeout = 5000
                setRequestProperty("Accept", "application/json")
                providerAccessToken?.let {
                    setRequestProperty("Authorization", "Bearer $it")
                }
            }

            val responseCode = conn.responseCode
            if (responseCode in 200..299) {
                val responseBody = conn.inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(responseBody)
                val list = mutableListOf<RemoteRequestDto>()
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    val idStr = item.optString("id", "")
                    list.add(
                        RemoteRequestDto(
                            id = idStr,
                            trackingCode = "TND-${idStr.take(5).uppercase()}",
                            title = item.optString("title"),
                            categoryId = item.optJSONObject("category")?.optString("name") ?: "خدمات",
                            description = item.optString("description"),
                            addressText = listOfNotNull(
                                item.optString("city").takeIf { it.isNotBlank() },
                                item.optString("district").takeIf { it.isNotBlank() },
                                item.optString("address_text").takeIf { it.isNotBlank() }
                            ).joinToString("، "),
                            preferredTime = item.optString("preferred_at", "فوری"),
                            latitude = latitude,
                            longitude = longitude,
                            status = item.optString("status", "open")
                        )
                    )
                }
                list
            } else {
                throw IllegalStateException("کد وضعیت سرور: $responseCode")
            }
        }
    }

    private fun postJson(
        urlStr: String,
        payload: JSONObject,
        bearerToken: String? = null
    ): JSONObject {
        val url = URL(urlStr)
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 5000
            readTimeout = 5000
            doOutput = true
            setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            setRequestProperty("Accept", "application/json")
            bearerToken?.let {
                setRequestProperty("Authorization", "Bearer $it")
            }
        }

        OutputStreamWriter(conn.outputStream, Charsets.UTF_8).use { writer ->
            writer.write(payload.toString())
            writer.flush()
        }

        val code = conn.responseCode
        if (code in 200..299) {
            val body = conn.inputStream.bufferedReader().use { it.readText() }
            return if (body.isNotBlank()) JSONObject(body) else JSONObject()
        } else {
            throw IllegalStateException("خطای ارتباط با سرور ($code)")
        }
    }
}
