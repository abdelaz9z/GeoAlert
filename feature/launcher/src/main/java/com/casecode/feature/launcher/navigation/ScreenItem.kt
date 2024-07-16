package com.casecode.feature.launcher.navigation

sealed class ScreenItem(val route: String) {
    data object SplashScreen : ScreenItem(route = "splash_screen")
    data object SignInScreen : ScreenItem(route = "sign_in_screen")
}
