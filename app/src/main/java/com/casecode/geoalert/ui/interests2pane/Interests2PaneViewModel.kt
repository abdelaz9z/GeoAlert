package com.casecode.geoalert.ui.interests2pane
//
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import com.google.samples.apps.nowinandroid.feature.interests.navigation.TOPIC_ID_ARG
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.StateFlow
//import javax.inject.Inject
//
//@HiltViewModel
//class Interests2PaneViewModel @Inject constructor(
//    private val savedStateHandle: SavedStateHandle,
//) : ViewModel() {
//    val selectedTopicId: StateFlow<String?> =
//        savedStateHandle.getStateFlow(TOPIC_ID_ARG, savedStateHandle[TOPIC_ID_ARG])
//
//    fun onTopicClick(topicId: String?) {
//        savedStateHandle[TOPIC_ID_ARG] = topicId
//    }
//}
