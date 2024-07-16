package com.casecode.core.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val geoAlertDispatchers: GeoAlertDispatchersDispatchers)

enum class GeoAlertDispatchersDispatchers {
    Default,
    Main,
    Unconfined,
    IO
}
