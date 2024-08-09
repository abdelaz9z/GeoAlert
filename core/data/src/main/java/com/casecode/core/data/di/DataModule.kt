package com.casecode.core.data.di

import com.casecode.core.data.util.ConnectivityManagerNetworkMonitor
import com.casecode.core.data.util.NetworkMonitor
import com.casecode.core.data.util.TimeZoneBroadcastMonitor
import com.casecode.core.data.util.TimeZoneMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor

    @Binds
    internal abstract fun binds(impl: TimeZoneBroadcastMonitor): TimeZoneMonitor
}
