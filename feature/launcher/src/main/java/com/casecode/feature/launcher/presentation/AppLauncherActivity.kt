package com.casecode.feature.launcher.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.casecode.core.designsystem.theme.GeoAlertTheme
import com.casecode.feature.launcher.navigation.AppLauncherNavigation
//import com.casecode.core.common.ui.theme.GeoAlertTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppLauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeoAlertTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppLauncherNavigation()
                }
            }
        }
    }
}