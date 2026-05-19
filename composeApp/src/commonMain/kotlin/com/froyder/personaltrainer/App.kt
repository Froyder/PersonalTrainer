package com.froyder.personaltrainer

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.froyder.personaltrainer.data.remote.createHttpClient
import com.froyder.personaltrainer.data.repository.GeminiRepository
import com.froyder.personaltrainer.data.repository.LocalRepository
import com.froyder.personaltrainer.navigation.NavGraph
import com.froyder.personaltrainer.navigation.Screen
import com.froyder.personaltrainer.presentation.AppViewModel
import com.froyder.personaltrainer.presentation.auth.AuthViewModel
import com.froyder.personaltrainer.presentation.menu.MenuViewModel
import com.froyder.personaltrainer.presentation.onboarding.OnboardingViewModel
import com.froyder.personaltrainer.presentation.theme.DarkModePreference
import com.froyder.personaltrainer.presentation.theme.ThemeViewModel
import com.froyder.personaltrainer.presentation.theme.resolveColorScheme
import com.froyder.personaltrainer.utils.isSystemDarkMode

@Composable
fun App(localRepository: LocalRepository) {
    val themeViewModel = viewModel { ThemeViewModel(localRepository.settings) }
    val preferences by themeViewModel.preferences.collectAsState()
    val systemDark = isSystemDarkMode()

    val isDark = when (preferences.darkMode) {
        DarkModePreference.SYSTEM -> systemDark
        DarkModePreference.LIGHT -> false
        DarkModePreference.DARK -> true
    }

    MaterialTheme(
        colorScheme = resolveColorScheme(preferences.colorScheme, isDark)
    ) {
        val onboardingViewModel = viewModel { OnboardingViewModel() }
        val authViewModel = viewModel { AuthViewModel() }
        val appViewModel = viewModel {
            AppViewModel(
                geminiRepository = GeminiRepository(createHttpClient()),
                localRepository = localRepository
            )
        }
        val menuViewModel = viewModel {
            MenuViewModel(
                localRepository = localRepository,
                appViewModel = appViewModel
            )
        }

        val navController = remember { mutableStateOf<NavHostController?>(null) }
        LaunchedEffect(Unit) {
            authViewModel.authStateFlow.collect { isLoggedIn ->
                if (!isLoggedIn) {
                    appViewModel.clearInMemoryState()
                    onboardingViewModel.reset()
                    authViewModel.reset()
                    navController.value?.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }

        NavGraph(
            onboardingViewModel = onboardingViewModel,
            appViewModel = appViewModel,
            authViewModel = authViewModel,
            menuViewModel = menuViewModel,
            themeViewModel = themeViewModel,
            onNavControllerReady = { navController.value = it }
        )
    }
}