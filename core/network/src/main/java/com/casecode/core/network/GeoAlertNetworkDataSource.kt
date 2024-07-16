package com.casecode.core.network

/**
 * Interface representing network calls to the NIA backend
 */
interface GeoAlertNetworkDataSource {
    suspend fun getTopics(ids: List<String>? = null): List<String>

    suspend fun getNewsResources(ids: List<String>? = null): List<String>

    suspend fun getTopicChangeList(after: Int? = null): List<String>

    suspend fun getNewsResourceChangeList(after: Int? = null): List<String>
}
