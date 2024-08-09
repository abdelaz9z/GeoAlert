package com.casecode.feature.search

import com.casecode.core.data.model.Alert
import com.casecode.core.data.model.Lists

sealed interface SearchResultUiState {
    data object Loading : SearchResultUiState

    /**
     * The state query is empty or too short. To distinguish the state between the
     * (initial state or when the search query is cleared) vs the state where no search
     * result is returned, explicitly define the empty query state.
     */
    data object EmptyQuery : SearchResultUiState

    data object LoadFailed : SearchResultUiState

    data class Success(
        val alerts: List<Alert> = emptyList(),
        val remindersLists: List<Lists> = emptyList(),
    ) : SearchResultUiState {
        fun isEmpty(): Boolean = alerts.isEmpty() && remindersLists.isEmpty()
    }

    /**
     * A state where the search contents are not ready. This happens when the *Fts tables are not
     * populated yet.
     */
    data object SearchNotReady : SearchResultUiState
}
