package com.casecode.geoalert.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.casecode.core.data.util.NetworkMonitor
import com.casecode.core.data.util.TimeZoneMonitor
import com.casecode.core.ui.TrackDisposableJank
import com.casecode.feature.account.navigation.ACCOUNT_ROUTE
import com.casecode.feature.account.navigation.navigateToAccount
import com.casecode.feature.home.navigation.HOME_ROUTE
import com.casecode.feature.home.navigation.navigateToHome
import com.casecode.feature.tasks.navigation.REMINDERS_ROUTE
import com.casecode.feature.tasks.navigation.navigateToReminders
import com.casecode.geoalert.navigation.TopLevelDestination
import com.casecode.geoalert.navigation.TopLevelDestination.HOME
import com.casecode.geoalert.navigation.TopLevelDestination.REMINDERS
import com.casecode.geoalert.navigation.TopLevelDestination.ACCOUNT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.TimeZone

@Composable
fun rememberGeoAlertAppState(
    networkMonitor: NetworkMonitor,
//    userNewsResourceRepository: UserNewsResourceRepository,
    timeZoneMonitor: TimeZoneMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): GeoAlertAppState {
    NavigationTrackingSideEffect(navController)
    return remember(
        navController,
        coroutineScope,
        networkMonitor,
//        userNewsResourceRepository,
        timeZoneMonitor,
    ) {
        GeoAlertAppState(
            navController = navController,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor,
//            userNewsResourceRepository = userNewsResourceRepository,
            timeZoneMonitor = timeZoneMonitor,
        )
    }
}

@Stable
class GeoAlertAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
//    userNewsResourceRepository: UserNewsResourceRepository,
    timeZoneMonitor: TimeZoneMonitor,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            HOME_ROUTE -> HOME
            REMINDERS_ROUTE -> REMINDERS
            ACCOUNT_ROUTE -> ACCOUNT
            else -> null
        }

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    /**
     * The top level destinations that have unread news resources.
     */
//    val topLevelDestinationsWithUnreadResources: StateFlow<Set<TopLevelDestination>> =
//        userNewsResourceRepository.observeAllForFollowedTopics()
//            .combine(userNewsResourceRepository.observeAllBookmarked()) { forYouNewsResources, bookmarkedNewsResources ->
//                setOfNotNull(
//                    FOR_YOU.takeIf { forYouNewsResources.any { !it.hasBeenViewed } },
//                    BOOKMARKS.takeIf { bookmarkedNewsResources.any { !it.hasBeenViewed } },
//                )
//            }
//            .stateIn(
//                coroutineScope,
//                SharingStarted.WhileSubscribed(5_000),
//                initialValue = emptySet(),
//            )

    val currentTimeZone = timeZoneMonitor.currentTimeZone
        .stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(5_000),
            TimeZone.currentSystemDefault(),
        )

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {

        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            HOME -> navController.navigateToHome(topLevelNavOptions)
            REMINDERS -> navController.navigateToReminders(topLevelNavOptions)
            ACCOUNT -> navController.navigateToAccount(topLevelNavOptions)
        }
    }

//    fun navigateToSearch() = navController.navigateToSearch()
}

/**
 * Stores information about navigation events to be used with JankStats
 */
@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    TrackDisposableJank(navController) { metricsHolder ->
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            metricsHolder.state?.putState("Navigation", destination.route.toString())
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}
