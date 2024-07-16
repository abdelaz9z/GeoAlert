package com.casecode.geoalert.navigation
//
///**
// * Top-level navigation graph. Navigation is organized as explained at
// * https://d.android.com/jetpack/compose/nav-adaptive
// *
// * The navigation graph defined in this file defines the different top level routes. Navigation
// * within each route is handled using state and Back Handlers.
// */
//@Composable
//fun GeoAlertNavHost(
//    appState: GeoAlertAppState,
//    onShowSnackbar: suspend (String, String?) -> Boolean,
//    modifier: Modifier = Modifier,
//    startDestination: String = FOR_YOU_ROUTE,
//) {
//    val navController = appState.navController
//    NavHost(
//        navController = navController,
//        startDestination = startDestination,
//        modifier = modifier,
//    ) {
//        forYouScreen(onTopicClick = navController::navigateToInterests)
//        bookmarksScreen(
//            onTopicClick = navController::navigateToInterests,
//            onShowSnackbar = onShowSnackbar,
//        )
//        searchScreen(
//            onBackClick = navController::popBackStack,
//            onInterestsClick = { appState.navigateToTopLevelDestination(INTERESTS) },
//            onTopicClick = navController::navigateToInterests,
//        )
//        interestsListDetailScreen()
//    }
//}
