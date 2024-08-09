package com.casecode.core.network.retrofit

import com.casecode.core.network.BuildConfig
import com.casecode.core.network.GeoAlertNetworkDataSource
import com.casecode.core.network.model.Alert
import com.casecode.core.network.model.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Lazy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

/**
 * API declaration for GeoAlert Network API
 */
private interface RetrofitGeoAlertNetworkApi {

    // User-related endpoints
    @POST("users")
    suspend fun addUser(@Body user: User)

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userId: String)

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") userId: String, @Body user: User)

    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: String): User

    @GET("users")
    suspend fun getUsers(): List<User>

    // Alert-related endpoints
    @POST("users/{userId}/alerts")
    suspend fun addAlert(@Path("userId") userId: String, @Body alert: Alert)

    @DELETE("users/{userId}/alerts/{alertId}")
    suspend fun deleteAlert(@Path("userId") userId: String, @Path("alertId") alertId: String)

    @PUT("users/{userId}/alerts/{alertId}")
    suspend fun updateAlert(
        @Path("userId") userId: String,
        @Path("alertId") alertId: String,
        @Body alert: Alert
    )

    @GET("users/{userId}/alerts/{alertId}")
    suspend fun getAlert(@Path("userId") userId: String, @Path("alertId") alertId: String): Alert

    @GET("users/{userId}/alerts")
    suspend fun getAlerts(@Path("userId") userId: String): List<Alert>
}

private const val GEO_ALERT_BASE_URL = BuildConfig.BACKEND_URL

@Serializable
private data class NetworkResponse<T>(
    val data: T
)

@Singleton
internal class RetrofitGeoAlertNetwork @Inject constructor(
    private val networkJson: Json,
    okhttpCallFactory: Lazy<Call.Factory>
) : GeoAlertNetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(GEO_ALERT_BASE_URL)
        .callFactory { okhttpCallFactory.get().newCall(it) }
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(RetrofitGeoAlertNetworkApi::class.java)

    override suspend fun addUser(user: User) {
        networkApi.addUser(user)
    }

    override suspend fun deleteUser(userId: String) {
        networkApi.deleteUser(userId)
    }

    override suspend fun updateUser(user: User) {
        networkApi.updateUser(user.id, user)
    }

    override suspend fun getUser(userId: String): User? {
        return try {
            networkApi.getUser(userId)
        } catch (e: Exception) {
            // Handle error
            null
        }
    }

    override suspend fun getUsers(): List<User> {
        return try {
            networkApi.getUsers()
        } catch (e: Exception) {
            // Handle error
            emptyList()
        }
    }

    override suspend fun addAlert(userId: String, alert: Alert) {
        networkApi.addAlert(userId, alert)
    }

    override suspend fun deleteAlert(userId: String, alertId: String) {
        networkApi.deleteAlert(userId, alertId)
    }

    override suspend fun updateAlert(userId: String, alert: Alert) {
        networkApi.updateAlert(userId, alert.id, alert)
    }

    override suspend fun getAlert(userId: String, alertId: String): Alert? {
        return try {
            networkApi.getAlert(userId, alertId)
        } catch (e: Exception) {
            // Handle error
            null
        }
    }

    override suspend fun getAlerts(userId: String): List<Alert> {
        return try {
            networkApi.getAlerts(userId)
        } catch (e: Exception) {
            // Handle error
            emptyList()
        }
    }

}
