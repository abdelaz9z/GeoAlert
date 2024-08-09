package com.casecode.core.data.repository

import com.casecode.core.data.model.Alert
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AlertRepositoryImpl @Inject constructor(
    private val usersRef: DatabaseReference
) : AlertRepository {

    override suspend fun addAlert(userId: String, alert: Alert) {
        val alertsRef = usersRef.child(userId).child("alerts").child(alert.id)
        alertsRef.setValue(alert)
    }

    override suspend fun deleteAlert(userId: String, alertId: String) {
        val alertRef = usersRef.child(userId).child("alerts").child(alertId)
        alertRef.removeValue()
    }

    override suspend fun updateAlert(userId: String, alert: Alert) {
        val alertsRef = usersRef.child(userId).child("alerts").child(alert.id)
        alertsRef.setValue(alert)
    }

    override suspend fun getAlert(userId: String, alertId: String): Alert? {
        val alertSnapshot = usersRef.child(userId).child("alerts").child(alertId).get().await()
        return alertSnapshot.getValue(Alert::class.java)
    }

    override suspend fun getAlerts(userId: String): List<Alert> {
        val alertsSnapshot = usersRef.child(userId).child("alerts").get().await()
        return alertsSnapshot.children.mapNotNull { it.getValue(Alert::class.java) }
    }
}
