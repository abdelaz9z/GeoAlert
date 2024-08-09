@file:Suppress("ktlint:standard:max-line-length")

package com.casecode.feature.search

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [SearchResultUiState] for Composable previews.
 */
class SearchUiStatePreviewParameterProvider : PreviewParameterProvider<SearchResultUiState> {
    override val values: Sequence<SearchResultUiState> = sequenceOf(
        SearchResultUiState.Success(
//            alerts = topics.mapIndexed { i, topic ->
//                FollowableTopic(topic = topic, isFollowed = i % 2 == 0)
//            },
//            remindersLists = newsResources,
        ),
    )
}
