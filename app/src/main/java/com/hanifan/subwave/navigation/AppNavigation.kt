package com.hanifan.subwave.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.hanifan.subwave.ui.screen.home.HomeScreen
import com.hanifan.subwave.ui.screen.login.LoginScreen

@Composable
fun AppNavigation(
    viewModel: NavigationViewModel = hiltViewModel<NavigationViewModel>()
) {
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(startDestination)

    NavDisplay(
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Routes.LoginRoute> {
                LoginScreen()
            }
            entry<Routes.HomeRoute> {
                HomeScreen(
                    onNavigate = { route -> backStack.add(route) }
                )
            }
            entry<Routes.SectionDetailRoute> {
                Scaffold {
                    Text("asdasdas", modifier = Modifier
                        .padding(it))
                }
            }
        }
    )
}