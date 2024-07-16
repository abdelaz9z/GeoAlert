package com.casecode.geoalert.navigation
//
//import androidx.compose.ui.graphics.vector.ImageVector
//import com.casecode.core.designsystem.icon.GeoAlertIcons
//import com.casecode.geoalert.R
//
///**
// * Type for the top level destinations in the application. Each of these destinations
// * can contain one or more screens (based on the window size). Navigation from one screen to the
// * next within a single destination will be handled directly in composables.
// */
//enum class TopLevelDestination(
//    val selectedIcon: ImageVector,
//    val unselectedIcon: ImageVector,
//    val iconTextId: Int,
//    val titleTextId: Int,
//) {
//    FOR_YOU(
//        selectedIcon = GeoAlertIcons.Upcoming,
//        unselectedIcon = GeoAlertIcons.UpcomingBorder,
//        iconTextId = R.string.feature_foryou_title,
//        titleTextId = R.string.app_name,
//    ),
//    BOOKMARKS(
//        selectedIcon = GeoAlertIcons.Bookmarks,
//        unselectedIcon = GeoAlertIcons.BookmarksBorder,
//        iconTextId = R.string.feature_bookmarks_title,
//        titleTextId = R.string.feature_bookmarks_title,
//    ),
//    INTERESTS(
//        selectedIcon = GeoAlertIcons.Grid3x3,
//        unselectedIcon = GeoAlertIcons.Grid3x3,
//        iconTextId = R.string.feature_search_interests,
//        titleTextId = R.string.feature_search_interests,
//    ),
//}
