package com.casecode.feature.launcher.di

import android.content.Context
import com.casecode.core.common.network.Dispatcher
import com.casecode.core.common.network.GeoAlertDispatchersDispatchers
import com.casecode.feature.launcher.data.repository.AppLauncherRepositoryImp
import com.casecode.feature.launcher.domain.repository.AppLauncherRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
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

}
