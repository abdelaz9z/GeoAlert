package com.casecode.feature.launcher.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.casecode.feature.launcher.presentation.splash.SplashRoute

const val SPLASH_ROUTE = "splash_route"

fun NavController.navigateToSplash(navOptions: NavOptions) = navigate(SPLASH_ROUTE, navOptions)

fun NavGraphBuilder.splashScreen(
    onMoveActionRoute: (String) -> Unit
) {
    composable(route = SPLASH_ROUTE) {
        SplashRoute(
            onMoveActionRoute = onMoveActionRoute
        )
    }
}
