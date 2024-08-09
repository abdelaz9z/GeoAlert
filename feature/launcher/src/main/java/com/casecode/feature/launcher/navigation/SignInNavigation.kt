package com.casecode.feature.launcher.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.casecode.feature.launcher.presentation.sign_in.SignInRoute

const val SIGN_IN_ROUTE = "sign_in_route"

fun NavController.navigateToSignIn(navOptions: NavOptions) = navigate(SIGN_IN_ROUTE, navOptions)

fun NavGraphBuilder.signInScreen(
    onSignInSuccess: (String) -> Unit,
    onSignInFailure: (String) -> Unit,
) {
    composable(route = SIGN_IN_ROUTE) {
        SignInRoute(
            onSignInSuccess = onSignInSuccess,
            onSignInFailure = onSignInFailure,
        )
    }
}
