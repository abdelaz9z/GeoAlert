package com.casecode.core.data.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthService {
    val currentUserId: String
    val currentUser: Flow<FirebaseUser?>
    val hasUser: Boolean
}