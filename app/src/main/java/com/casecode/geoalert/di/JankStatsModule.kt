package com.casecode.geoalert.di

import android.app.Activity
import android.view.Window
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object JankStatsModule {

    @Provides
    fun providesWindow(activity: Activity): Window = activity.window

}
