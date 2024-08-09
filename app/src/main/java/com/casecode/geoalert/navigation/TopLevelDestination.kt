package com.casecode.geoalert.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.casecode.core.designsystem.icon.GeoAlertIcons
import com.casecode.geoalert.R

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    HOME(
        selectedIcon = GeoAlertIcons.Home,
        unselectedIcon = GeoAlertIcons.HomeBorder,
        iconTextId = R.string.feature_home_title,
        titleTextId = R.string.feature_home_title,
    ),
    REMINDERS(
        selectedIcon = GeoAlertIcons.Task,
        unselectedIcon = GeoAlertIcons.TaskBorder,
        iconTextId = R.string.feature_reminders_title,
        titleTextId = R.string.feature_reminders_title,
    ),
    ACCOUNT(
        selectedIcon = GeoAlertIcons.Account,
        unselectedIcon = GeoAlertIcons.AccountBorder,
        iconTextId = R.string.feature_account_title,
        titleTextId = R.string.feature_account_title,
    ),
}
