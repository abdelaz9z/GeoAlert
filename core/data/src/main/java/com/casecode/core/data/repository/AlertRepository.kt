package com.casecode.core.data.repository

import com.casecode.core.data.model.Alert

interface AlertRepository {
    suspend fun addAlert(userId: String, alert: Alert)
    suspend fun deleteAlert(userId: String, alertId: String)
    suspend fun updateAlert(userId: String, alert: Alert)
    suspend fun getAlert(userId: String, alertId: String): Alert?
    suspend fun getAlerts(userId: String): List<Alert>
}
