package com.casecode.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.casecode.feature.home.HomeRoute

const val HOME_ROUTE = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(HOME_ROUTE, navOptions)

fun NavGraphBuilder.homeScreen(
    onTopicClick: (String) -> Unit
) {
    composable(route = HOME_ROUTE) {
        HomeRoute(
            onTopicClick = onTopicClick
        )
    }
}