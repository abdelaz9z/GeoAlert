package com.casecode.geoalert.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.casecode.feature.account.navigation.accountScreen
import com.casecode.feature.home.navigation.HOME_ROUTE
import com.casecode.feature.home.navigation.homeScreen
import com.casecode.feature.tasks.navigation.remindersScreen
import com.casecode.geoalert.ui.GeoAlertAppState

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun GeoAlertNavHost(
    appState: GeoAlertAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_ROUTE,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeScreen(onTopicClick = { /* handle home topic click */ })
        remindersScreen(onTopicClick = { /* handle tasks topic click */ })
        accountScreen()
    }
}
