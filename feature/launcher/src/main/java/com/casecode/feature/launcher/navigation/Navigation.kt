package com.casecode.feature.launcher.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.casecode.core.designsystem.component.dialog.ErrorDialog

@Composable
fun AppLauncherNavigation() {
    val navController = rememberNavController()
    var showErrorDialog by remember { mutableStateOf("") }
    var keyLogin by remember { mutableStateOf(true) }
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = if (!keyLogin) SIGN_IN_ROUTE else SPLASH_ROUTE
    ) {
        splashScreen(
            onMoveActionRoute = { route ->
                navController.navigate(route)
            }
        )

        signInScreen(
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

    if (showErrorDialog.isNotEmpty()) {
        ErrorDialog(error = showErrorDialog, onDismissClick = { showErrorDialog = "" })
    }
}
