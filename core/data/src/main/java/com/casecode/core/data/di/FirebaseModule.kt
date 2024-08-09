package com.casecode.core.data.di

import com.casecode.core.data.repository.UserRepository
import com.casecode.core.data.repository.UserRepositoryImpl
import com.casecode.core.network.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideSignInRequest(): GetGoogleIdOption {
        val hashedNonce = createHashedNonce()

        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .setNonce(hashedNonce)
            .build()
    }

    private fun createHashedNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    @Provides
    fun provideFirebaseDatabase(): DatabaseReference = Firebase.database.getReference("users")

}