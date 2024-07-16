package com.casecode.core.data.di

import com.casecode.core.data.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

//    @Binds
//    internal abstract fun bindsUserDataRepository(
//        userDataRepository: OfflineFirstUserDataRepository,
//    ): UserDataRepository

}
