package com.casecode.core.data.di

import android.content.Context
import com.casecode.core.common.network.Dispatcher
import com.casecode.core.common.network.GeoAlertDispatchersDispatchers
import com.casecode.core.data.repository.AlertRepository
import com.casecode.core.data.repository.AlertRepositoryImpl
import com.casecode.core.data.repository.AppLauncherRepository
import com.casecode.core.data.repository.AppLauncherRepositoryImp
import com.casecode.core.data.repository.AuthService
import com.casecode.core.data.repository.AuthServiceImpl
import com.casecode.core.data.repository.UserRepository
import com.casecode.core.data.repository.UserRepositoryImpl
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAppLauncherRepository(
        @ApplicationContext context: Context,
        googleIdOption: GetGoogleIdOption,
        firebaseAuth: FirebaseAuth,
        @Dispatcher(GeoAlertDispatchersDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ): AppLauncherRepository {
        return AppLauncherRepositoryImp(context, googleIdOption, firebaseAuth, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        usersRef: DatabaseReference,
        firebaseAuth: FirebaseAuth
    ): UserRepository {
        return UserRepositoryImpl(usersRef, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideAlertRepository(
        usersRef: DatabaseReference
    ): AlertRepository {
        return AlertRepositoryImpl(usersRef)
    }

    @Provides
    @Singleton
    fun provideAuthService(
        auth: FirebaseAuth
    ): AuthService {
        return AuthServiceImpl(auth)
    }

}
