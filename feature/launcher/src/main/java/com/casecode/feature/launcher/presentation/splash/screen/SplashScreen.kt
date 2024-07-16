package com.casecode.feature.launcher.presentation.splash.screen

import android.content.Context
import android.content.Intent
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.casecode.feature.launcher.R
import com.casecode.feature.launcher.domain.utils.Resource
import com.casecode.feature.launcher.navigation.ScreenItem
import com.casecode.feature.launcher.presentation.sign_in.screen.ClassNames
import com.casecode.feature.launcher.presentation.splash.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    onMoveActionRoute: (String) -> Unit
) {
    val context = LocalContext.current
    val scale = remember { Animatable(1f) }
    val progressLogout = remember { mutableStateOf(false) }

    SplashScreenEffect(
        onMoveActionRoute = onMoveActionRoute,
        splashViewModel = splashViewModel,
        context = context,
        scale = scale,
        progressLogout = progressLogout
    )

    SplashScreenUI(scale = scale, progressLogout = progressLogout)
}

@Composable
fun SplashScreenEffect(
    onMoveActionRoute: (String) -> Unit,
    splashViewModel: SplashViewModel,
    context: Context,
    scale: Animatable<Float, AnimationVector1D>,
    progressLogout: MutableState<Boolean>
) {
    val signInStatus by splashViewModel.signInStatus.collectAsState()

    LaunchedEffect(key1 = signInStatus) {
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(durationMillis = 500, easing = {
                OvershootInterpolator(2f).getInterpolation(it)
            })
        )

        delay(3000L)

        when (signInStatus) {
            is Resource.Success -> {
                if ((signInStatus as Resource.Success<Boolean>).data) {
                    val intent = Intent(
                        context,
                        Class.forName(ClassNames.MAIN_ACTIVITY.className)
                    )
                    context.startActivity(intent)
                } else {
                    onMoveActionRoute(ScreenItem.SignInScreen.route)
                }
            }

            is Resource.Error -> {
                onMoveActionRoute(ScreenItem.SignInScreen.route)
            }

            is Resource.Loading -> {
                // Show loading indicator if needed
                progressLogout.value = true
            }

            else -> Unit
        }
    }
}


@Composable
fun SplashScreenUI(
    scale: Animatable<Float, AnimationVector1D>,
    progressLogout: MutableState<Boolean>
) {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SplashScreenImage(scale)
            if (progressLogout.value) {
                SplashScreenLoading()
            }
        }
    }
}

@Composable
fun SplashScreenImage(scale: Animatable<Float, AnimationVector1D>) {
    Image(
        painter = painterResource(id = R.drawable.address_concept),
        contentDescription = "Logo",
        modifier = Modifier
            .width(150.dp)
            .height(150.dp)
            .scale(scale.value)
    )
}

@Composable
fun SplashScreenLoading() {

    CircularProgressIndicator(
        modifier = Modifier
            .size(60.dp)
            .padding(8.dp),
        strokeCap = StrokeCap.Round,
        trackColor = Color.Gray.copy(alpha = 0.3f),
        strokeWidth = 7.dp,
    )
    Text(
        modifier = Modifier.padding(top = 10.dp),
        text = stringResource(R.string.logout_is_loading),
        style = TextStyle(
            fontSize = 16.sp, fontWeight = FontWeight.Bold
        )
    )
}

fun NavController.moveToLogin() {
    this.navigate(ScreenItem.SignInScreen.route) {
        popUpTo(ScreenItem.SplashScreen.route) { inclusive = true }
    }
}