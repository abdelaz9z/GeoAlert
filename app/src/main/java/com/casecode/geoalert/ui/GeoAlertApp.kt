package com.casecode.geoalert.ui
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.WindowInsets
//import androidx.compose.foundation.layout.WindowInsetsSides
//import androidx.compose.foundation.layout.consumeWindowInsets
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.only
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.safeDrawing
//import androidx.compose.foundation.layout.windowInsetsPadding
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.SnackbarDuration.Indefinite
//import androidx.compose.material3.SnackbarDuration.Short
//import androidx.compose.material3.SnackbarHost
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.SnackbarResult.ActionPerformed
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
//import androidx.compose.material3.adaptive.WindowAdaptiveInfo
//import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.composed
//import androidx.compose.ui.draw.drawWithContent
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.testTag
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.semantics.semantics
//import androidx.compose.ui.semantics.testTagsAsResourceId
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.navigation.NavDestination
//import androidx.navigation.NavDestination.Companion.hierarchy
//import com.casecode.core.designsystem.component.GeoAlertBackground
//import com.casecode.core.designsystem.component.GeoAlertGradientBackground
//import com.casecode.core.designsystem.component.GeoAlertNavigationSuiteScaffold
//import com.casecode.core.designsystem.component.GeoAlertTopAppBar
//import com.casecode.core.designsystem.icon.GeoAlertIcons
//import com.casecode.core.designsystem.theme.GradientColors
//import com.casecode.core.designsystem.theme.LocalGradientColors
//import com.casecode.feature.settings.SettingsDialog
//import com.casecode.geoalert.navigation.GeoAlertNavHost
//import com.casecode.geoalert.navigation.TopLevelDestination
//import com.casecode.feature.settings.R as settingsR
//
//@OptIn(ExperimentalMaterial3AdaptiveApi::class)
//@Composable
//fun GeoAlertApp(
//    appState: NiaAppState,
//    modifier: Modifier = Modifier,
//    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
//) {
//    val shouldShowGradientBackground =
//        appState.currentTopLevelDestination == TopLevelDestination.FOR_YOU
//    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
//
//    GeoAlertBackground(modifier = modifier) {
//        GeoAlertGradientBackground(
//            gradientColors = if (shouldShowGradientBackground) {
//                LocalGradientColors.current
//            } else {
//                GradientColors()
//            },
//        ) {
//            val snackbarHostState = remember { SnackbarHostState() }
//
//            val isOffline by appState.isOffline.collectAsStateWithLifecycle()
//
//            // If user is not connected to the internet show a snack bar to inform them.
//            val notConnectedMessage = stringResource(com.casecode.geoalert.R.string.not_connected)
//            LaunchedEffect(isOffline) {
//                if (isOffline) {
//                    snackbarHostState.showSnackbar(
//                        message = notConnectedMessage,
//                        duration = Indefinite,
//                    )
//                }
//            }
//
//            GeoAlertApp(
//                appState = appState,
//                snackbarHostState = snackbarHostState,
//                showSettingsDialog = showSettingsDialog,
//                onSettingsDismissed = { showSettingsDialog = false },
//                onTopAppBarActionClick = { showSettingsDialog = true },
//                windowAdaptiveInfo = windowAdaptiveInfo,
//            )
//        }
//    }
//}
//
//@Composable
//@OptIn(
//    ExperimentalMaterial3Api::class,
//    ExperimentalComposeUiApi::class,
//    ExperimentalMaterial3AdaptiveApi::class,
//)
//internal fun GeoAlertApp(
//    appState: NiaAppState,
//    snackbarHostState: SnackbarHostState,
//    showSettingsDialog: Boolean,
//    onSettingsDismissed: () -> Unit,
//    onTopAppBarActionClick: () -> Unit,
//    modifier: Modifier = Modifier,
//    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
//) {
//    val unreadDestinations by appState.topLevelDestinationsWithUnreadResources
//        .collectAsStateWithLifecycle()
//    val currentDestination = appState.currentDestination
//
//    if (showSettingsDialog) {
//        SettingsDialog(
//            onDismiss = { onSettingsDismissed() },
//        )
//    }
//
//    GeoAlertNavigationSuiteScaffold(
//        navigationSuiteItems = {
//            appState.topLevelDestinations.forEach { destination ->
//                val hasUnread = unreadDestinations.contains(destination)
//                val selected = currentDestination
//                    .isTopLevelDestinationInHierarchy(destination)
//                item(
//                    selected = selected,
//                    onClick = { appState.navigateToTopLevelDestination(destination) },
//                    icon = {
//                        Icon(
//                            imageVector = destination.unselectedIcon,
//                            contentDescription = null,
//                        )
//                    },
//                    selectedIcon = {
//                        Icon(
//                            imageVector = destination.selectedIcon,
//                            contentDescription = null,
//                        )
//                    },
//                    label = { Text(stringResource(destination.iconTextId)) },
//                    modifier =
//                    Modifier
//                        .testTag("NiaNavItem")
//                        .then(if (hasUnread) Modifier.notificationDot() else Modifier),
//                )
//            }
//        },
//        windowAdaptiveInfo = windowAdaptiveInfo,
//    ) {
//        Scaffold(
//            modifier = modifier.semantics {
//                testTagsAsResourceId = true
//            },
//            containerColor = Color.Transparent,
//            contentColor = MaterialTheme.colorScheme.onBackground,
//            contentWindowInsets = WindowInsets(0, 0, 0, 0),
//            snackbarHost = { SnackbarHost(snackbarHostState) },
//        ) { padding ->
//            Column(
//                Modifier
//                    .fillMaxSize()
//                    .padding(padding)
//                    .consumeWindowInsets(padding)
//                    .windowInsetsPadding(
//                        WindowInsets.safeDrawing.only(
//                            WindowInsetsSides.Horizontal,
//                        ),
//                    ),
//            ) {
//                // Show the top app bar on top level destinations.
//                val destination = appState.currentTopLevelDestination
//                val shouldShowTopAppBar = destination != null
//                if (destination != null) {
//                    GeoAlertTopAppBar(
//                        titleRes = destination.titleTextId,
//                        navigationIcon = GeoAlertIcons.Search,
//                        navigationIconContentDescription = stringResource(
//                            id = settingsR.string.feature_settings_top_app_bar_navigation_icon_description,
//                        ),
//                        actionIcon = GeoAlertIcons.Settings,
//                        actionIconContentDescription = stringResource(
//                            id = settingsR.string.feature_settings_top_app_bar_action_icon_description,
//                        ),
//                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//                            containerColor = Color.Transparent,
//                        ),
//                        onActionClick = { onTopAppBarActionClick() },
//                        onNavigationClick = { appState.navigateToSearch() },
//                    )
//                }
//
//                Box(
//                    // Workaround for https://issuetracker.google.com/338478720
//                    modifier = Modifier.consumeWindowInsets(
//                        if (shouldShowTopAppBar) {
//                            WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
//                        } else {
//                            WindowInsets(0, 0, 0, 0)
//                        },
//                    ),
//                ) {
//                    GeoAlertNavHost(
//                        appState = appState,
//                        onShowSnackbar = { message, action ->
//                            snackbarHostState.showSnackbar(
//                                message = message,
//                                actionLabel = action,
//                                duration = Short,
//                            ) == ActionPerformed
//                        },
//                    )
//                }
//
//                // TODO: We may want to add padding or spacer when the snackbar is shown so that
//                //  content doesn't display behind it.
//            }
//        }
//    }
//}
//
//private fun Modifier.notificationDot(): Modifier =
//    composed {
//        val tertiaryColor = MaterialTheme.colorScheme.tertiary
//        drawWithContent {
//            drawContent()
//            drawCircle(
//                tertiaryColor,
//                radius = 5.dp.toPx(),
//                // This is based on the dimensions of the NavigationBar's "indicator pill";
//                // however, its parameters are private, so we must depend on them implicitly
//                // (NavigationBarTokens.ActiveIndicatorWidth = 64.dp)
//                center = center + Offset(
//                    64.dp.toPx() * .45f,
//                    32.dp.toPx() * -.45f - 6.dp.toPx(),
//                ),
//            )
//        }
//    }
//
//private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
//    this?.hierarchy?.any {
//        it.route?.contains(destination.name, true) ?: false
//    } ?: false
