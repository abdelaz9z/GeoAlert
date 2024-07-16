package com.casecode.geoalert.ui.interests2pane
//
//import androidx.activity.compose.BackHandler
//import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
//import androidx.compose.material3.adaptive.layout.AnimatedPane
//import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
//import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
//import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
//import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
//import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
//import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.key
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.saveable.Saver
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.navArgument
//import com.google.samples.apps.nowinandroid.feature.interests.InterestsRoute
//import com.google.samples.apps.nowinandroid.feature.interests.navigation.INTERESTS_ROUTE
//import com.google.samples.apps.nowinandroid.feature.interests.navigation.TOPIC_ID_ARG
//import com.google.samples.apps.nowinandroid.feature.topic.TopicDetailPlaceholder
//import com.google.samples.apps.nowinandroid.feature.topic.navigation.TOPIC_ROUTE
//import com.google.samples.apps.nowinandroid.feature.topic.navigation.createTopicRoute
//import com.google.samples.apps.nowinandroid.feature.topic.navigation.navigateToTopic
//import com.google.samples.apps.nowinandroid.feature.topic.navigation.topicScreen
//import java.util.UUID
//
//private const val DETAIL_PANE_NAVHOST_ROUTE = "detail_pane_route"
//
//fun NavGraphBuilder.interestsListDetailScreen() {
//    composable(
//        route = INTERESTS_ROUTE,
//        arguments = listOf(
//            navArgument(TOPIC_ID_ARG) {
//                type = NavType.StringType
//                defaultValue = null
//                nullable = true
//            },
//        ),
//    ) {
//        InterestsListDetailScreen()
//    }
//}
//
//@Composable
//internal fun InterestsListDetailScreen(
//    viewModel: Interests2PaneViewModel = hiltViewModel(),
//) {
//    val selectedTopicId by viewModel.selectedTopicId.collectAsStateWithLifecycle()
//    InterestsListDetailScreen(
//        selectedTopicId = selectedTopicId,
//        onTopicClick = viewModel::onTopicClick,
//    )
//}
//
//@OptIn(ExperimentalMaterial3AdaptiveApi::class)
//@Composable
//internal fun InterestsListDetailScreen(
//    selectedTopicId: String?,
//    onTopicClick: (String) -> Unit,
//) {
//    val listDetailNavigator = rememberListDetailPaneScaffoldNavigator(
//        initialDestinationHistory = listOfNotNull(
//            ThreePaneScaffoldDestinationItem(ListDetailPaneScaffoldRole.List),
//            ThreePaneScaffoldDestinationItem<Nothing>(ListDetailPaneScaffoldRole.Detail).takeIf {
//                selectedTopicId != null
//            },
//        ),
//    )
//    BackHandler(listDetailNavigator.canNavigateBack()) {
//        listDetailNavigator.navigateBack()
//    }
//
//    var nestedNavHostStartDestination by remember {
//        mutableStateOf(selectedTopicId?.let(::createTopicRoute) ?: TOPIC_ROUTE)
//    }
//    var nestedNavKey by rememberSaveable(
//        stateSaver = Saver({ it.toString() }, UUID::fromString),
//    ) {
//        mutableStateOf(UUID.randomUUID())
//    }
//    val nestedNavController = key(nestedNavKey) {
//        rememberNavController()
//    }
//
//    fun onTopicClickShowDetailPane(topicId: String) {
//        onTopicClick(topicId)
//        if (listDetailNavigator.isDetailPaneVisible()) {
//            // If the detail pane was visible, then use the nestedNavController navigate call
//            // directly
//            nestedNavController.navigateToTopic(topicId) {
//                popUpTo(DETAIL_PANE_NAVHOST_ROUTE)
//            }
//        } else {
//            // Otherwise, recreate the NavHost entirely, and start at the new destination
//            nestedNavHostStartDestination = createTopicRoute(topicId)
//            nestedNavKey = UUID.randomUUID()
//        }
//        listDetailNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
//    }
//
//    ListDetailPaneScaffold(
//        value = listDetailNavigator.scaffoldValue,
//        directive = listDetailNavigator.scaffoldDirective,
//        listPane = {
//            AnimatedPane {
//                InterestsRoute(
//                    onTopicClick = ::onTopicClickShowDetailPane,
//                    highlightSelectedTopic = listDetailNavigator.isDetailPaneVisible(),
//                )
//            }
//        },
//        detailPane = {
//            AnimatedPane {
//                key(nestedNavKey) {
//                    NavHost(
//                        navController = nestedNavController,
//                        startDestination = nestedNavHostStartDestination,
//                        route = DETAIL_PANE_NAVHOST_ROUTE,
//                    ) {
//                        topicScreen(
//                            showBackButton = !listDetailNavigator.isListPaneVisible(),
//                            onBackClick = listDetailNavigator::navigateBack,
//                            onTopicClick = ::onTopicClickShowDetailPane,
//                        )
//                        composable(route = TOPIC_ROUTE) {
//                            TopicDetailPlaceholder()
//                        }
//                    }
//                }
//            }
//        },
//    )
//}
//
//@OptIn(ExperimentalMaterial3AdaptiveApi::class)
//private fun <T> ThreePaneScaffoldNavigator<T>.isListPaneVisible(): Boolean =
//    scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded
//
//@OptIn(ExperimentalMaterial3AdaptiveApi::class)
//private fun <T> ThreePaneScaffoldNavigator<T>.isDetailPaneVisible(): Boolean =
//    scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
