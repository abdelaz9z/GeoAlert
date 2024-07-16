package com.casecode.feature.launcher.presentation.sign_in.screen

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.casecode.core.designsystem.theme.GeoAlertTheme
import com.casecode.core.designsystem.theme.bleachedSilk
import com.casecode.feature.launcher.R
import com.casecode.feature.launcher.domain.utils.Resource
import com.casecode.feature.launcher.presentation.sign_in.viewmodel.SignInViewModel

@Composable
fun SignInScreen(
    signInViewModel: SignInViewModel,
    onSignInSuccess: (String) -> Unit,
    onSignInFailure: (String) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity ?: throw IllegalStateException("Context is not an Activity")
    val versionName = remember { getVersionName(context) }
    val signInResult by signInViewModel.signInResult.collectAsState()

    LaunchedEffect(key1 = signInResult) {
        when (signInResult) {
            is Resource.Success -> {
                // Handle success and navigate to MainActivity
                onSignInSuccess(ClassNames.MAIN_ACTIVITY.className)
            }

            is Resource.Error -> {
                // Handle error
                onSignInFailure(
                    ((signInResult as Resource.Error).message ?: "Unknown error").toString()
                )
            }

            else -> Unit
        }
    }

    // Implement your sign-in screen UI here
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.address_concept),
            contentDescription = stringResource(R.string.address_concept)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.welcome_to_geo_alert),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.geo_alert_description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = { signInViewModel.signIn(activity) },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = bleachedSilk,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google_logo),
                contentDescription = stringResource(R.string.google_logo),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.sign_in_with_google),
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(90.dp))
        Text(
            text = stringResource(R.string.version, versionName),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

fun getVersionName(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName
    } catch (e: Exception) {
        e.printStackTrace()
        context.getString(R.string.unknown)
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    GeoAlertTheme {
        val signInViewModel: SignInViewModel = hiltViewModel()
        SignInScreen(signInViewModel = signInViewModel, {}, {})
    }
}

enum class ClassNames(val className: String) {
    APP_LAUNCHER_ACTIVITY("com.casecode.feature.launcher.presentation.AppLauncherActivity"),
    //    MAIN_ACTIVITY("com.casecode.feature.main.presentation.MainActivity"),
    MAIN_ACTIVITY("com.casecode.geoalert.ui.MainActivity"),
}