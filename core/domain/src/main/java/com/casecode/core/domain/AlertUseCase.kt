package com.casecode.core.domain

import com.casecode.core.data.model.Alert
import com.casecode.core.data.repository.AlertRepository
import javax.inject.Inject

/**
 * Use case for adding an alert for a specific user.
 *
 * @property alertRepository The repository for managing alerts in the data source.
 *
 * @constructor Creates an instance of [AddAlertUseCase] and injects the required dependencies.
 *
 * @param alertRepository An instance of [AlertRepository] used to perform operations on alerts.
 */
class AddAlertUseCase @Inject constructor(
    private val alertRepository: AlertRepository
) {
    /**
     * Adds a new alert to the specified user's alerts in the data source.
     *
     * @param userId The ID of the user to whom the alert will be added.
     * @param alert The [Alert] object to be added.
     */
    suspend operator fun invoke(userId: String, alert: Alert) {
        alertRepository.addAlert(userId, alert)
    }
}

/**
 * Use case for deleting an alert for a specific user.
 *
 * @property alertRepository The repository for managing alerts in the data source.
 *
 * @constructor Creates an instance of [DeleteAlertUseCase] and injects the required dependencies.
 *
 * @param alertRepository An instance of [AlertRepository] used to perform operations on alerts.
 */
class DeleteAlertUseCase @Inject constructor(
    private val alertRepository: AlertRepository
) {
    /**
     * Deletes an alert from the specified user's alerts in the data source.
     *
     * @param userId The ID of the user from whom the alert will be deleted.
     * @param alertId The ID of the alert to be deleted.
     */
    suspend operator fun invoke(userId: String, alertId: String) {
        alertRepository.deleteAlert(userId, alertId)
    }
}

/**
 * Use case for updating an alert for a specific user.
 *
 * @property alertRepository The repository for managing alerts in the data source.
 *
 * @constructor Creates an instance of [UpdateAlertUseCase] and injects the required dependencies.
 *
 * @param alertRepository An instance of [AlertRepository] used to perform operations on alerts.
 */
class UpdateAlertUseCase @Inject constructor(
    private val alertRepository: AlertRepository
) {
    /**
     * Updates an existing alert in the specified user's alerts in the data source.
     *
     * @param userId The ID of the user whose alert will be updated.
     * @param alert The [Alert] object containing the updated information.
     */
    suspend operator fun invoke(userId: String, alert: Alert) {
        alertRepository.updateAlert(userId, alert)
    }
}

/**
 * Use case for retrieving a specific alert for a user.
 *
 * @property alertRepository The repository for managing alerts in the data source.
 *
 * @constructor Creates an instance of [GetAlertUseCase] and injects the required dependencies.
 *
 * @param alertRepository An instance of [AlertRepository] used to perform operations on alerts.
 */
class GetAlertUseCase @Inject constructor(
    private val alertRepository: AlertRepository
) {
    /**
     * Retrieves a specific alert from the specified user's alerts in the data source.
     *
     * @param userId The ID of the user whose alert will be retrieved.
     * @param alertId The ID of the alert to be retrieved.
     * @return The [Alert] object if found, or null if the alert does not exist.
     */
    suspend operator fun invoke(userId: String, alertId: String): Alert? {
        return alertRepository.getAlert(userId, alertId)
    }
}

/**
 * Use case for retrieving all alerts for a specific user.
 *
 * @property alertRepository The repository for managing alerts in the data source.
 *
 * @constructor Creates an instance of [GetAlertsUseCase] and injects the required dependencies.
 *
 * @param alertRepository An instance of [AlertRepository] used to perform operations on alerts.
 */
class GetAlertsUseCase @Inject constructor(
    private val alertRepository: AlertRepository
) {
    /**
     * Retrieves all alerts from the specified user's alerts in the data source.
     *
     * @param userId The ID of the user whose alerts will be retrieved.
     * @return A list of [Alert] objects for the user.
     */
    suspend operator fun invoke(userId: String): List<Alert> {
        return alertRepository.getAlerts(userId)
    }
}
