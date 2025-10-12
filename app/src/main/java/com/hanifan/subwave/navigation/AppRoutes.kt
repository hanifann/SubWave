package com.hanifan.subwave.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Routes: NavKey {
    @Serializable
    data object HomeRoute: Routes

    @Serializable
    data object SectionDetailRoute: Routes

    @Serializable
    data object LoginRoute: Routes
}