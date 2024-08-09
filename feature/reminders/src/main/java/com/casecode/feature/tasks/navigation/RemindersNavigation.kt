package com.casecode.feature.tasks.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.casecode.feature.tasks.RemindersRoute

const val REMINDERS_ROUTE = "reminders_route"

fun NavController.navigateToReminders(navOptions: NavOptions) = navigate(REMINDERS_ROUTE, navOptions)

fun NavGraphBuilder.remindersScreen(
    onTopicClick: (String) -> Unit
) {
    composable(route = REMINDERS_ROUTE) {
        RemindersRoute(
            onTopicClick = onTopicClick
        )
    }
}