package com.casecode.feature.launcher.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.casecode.core.designsystem.component.dialog.ErrorDialog
import com.casecode.feature.launcher.presentation.sign_in.screen.SignInScreen
import com.casecode.feature.launcher.presentation.sign_in.viewmodel.SignInViewModel
import com.casecode.feature.launcher.presentation.splash.screen.SplashScreen

@Composable
fun AppLauncherNavigation() {
    val navController = rememberNavController()
    var showErrorDialog by remember { mutableStateOf("") }
    var keyLogin by remember { mutableStateOf(false) }
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = if (!keyLogin) ScreenItem.SignInScreen.route else ScreenItem.SplashScreen.route
    ) {
        composable(ScreenItem.SplashScreen.route) {
            SplashScreen(onMoveActionRoute = { route ->
                navController.navigate(route)
            })
        }

        composable(ScreenItem.SignInScreen.route) {
            val signInViewModel = hiltViewModel<SignInViewModel>()
            SignInScreen(
                signInViewModel = signInViewModel,
                onSignInSuccess = { className ->
                    keyLogin = true
                    val intent = Intent(context, Class.forName(className))
                    context.startActivity(intent)
                },
                onSignInFailure = { error ->
                    showErrorDialog = error
                }
            )
        }
    }

    if (showErrorDialog.isNotEmpty()) {
        ErrorDialog(error = showErrorDialog, onDismissClick = { showErrorDialog = "" })
    }
}
