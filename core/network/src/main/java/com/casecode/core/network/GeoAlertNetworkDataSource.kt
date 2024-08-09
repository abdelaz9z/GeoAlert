package com.casecode.core.network

import com.casecode.core.common.result.Result
import com.casecode.core.network.model.Alert
import com.casecode.core.network.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Interface representing network calls to the GeoAlert backend
 */
interface GeoAlertNetworkDataSource {
    suspend fun addUser(user: User)
    suspend fun deleteUser(userId: String)
    suspend fun updateUser(user: User)
    suspend fun getUser(userId: String): User?
    suspend fun getUsers(): List<User>

    suspend fun addAlert(userId: String, alert: Alert)
    suspend fun deleteAlert(userId: String, alertId: String)
    suspend fun updateAlert(userId: String, alert: Alert)
    suspend fun getAlert(userId: String, alertId: String): Alert?
    suspend fun getAlerts(userId: String): List<Alert>
}
