package com.casecode.core.network.retrofit

import androidx.tracing.trace
import com.casecode.core.network.BuildConfig
import com.casecode.core.network.GeoAlertNetworkDataSource
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit API declaration for NIA Network API
 */
private interface RetrofitGeoAlertNetworkApi {
    @GET(value = "topics")
    suspend fun getTopics(@Query("id") ids: List<String>?): NetworkResponse<List<String>>

    @GET(value = "newsresources")
    suspend fun getNewsResources(@Query("id") ids: List<String>?): NetworkResponse<List<String>>

    @GET(value = "changelists/topics")
    suspend fun getTopicChangeList(@Query("after") after: Int?): List<String>

    @GET(value = "changelists/newsresources")
    suspend fun getNewsResourcesChangeList(@Query("after") after: Int?): List<String>
}

private const val GEO_ALERT_BASE_URL = BuildConfig.BACKEND_URL

/**
 * Wrapper for data provided from the [GEO_ALERT_BASE_URL]
 */
@Serializable
private data class NetworkResponse<T>(
    val data: T,
)

/**
 * [Retrofit] backed [GeoAlertNetworkDataSource]
 */
@Singleton
internal class RetrofitGeoAlertNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : GeoAlertNetworkDataSource {

    private val networkApi = trace("RetrofitNiaNetwork") {
        Retrofit.Builder()
            .baseUrl(GEO_ALERT_BASE_URL)
            // We use callFactory lambda here with dagger.Lazy<Call.Factory>
            // to prevent initializing OkHttp on the main thread.
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(RetrofitGeoAlertNetworkApi::class.java)
    }

    override suspend fun getTopics(ids: List<String>?): List<String> =
        networkApi.getTopics(ids = ids).data

    override suspend fun getNewsResources(ids: List<String>?): List<String> =
        networkApi.getNewsResources(ids = ids).data

    override suspend fun getTopicChangeList(after: Int?): List<String> =
        networkApi.getTopicChangeList(after = after)

    override suspend fun getNewsResourceChangeList(after: Int?): List<String> =
        networkApi.getNewsResourcesChangeList(after = after)
}