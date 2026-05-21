package com.froyder.personaltrainer.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.froyder.personaltrainer.data.mock.MockData
import com.froyder.personaltrainer.data.model.User
import com.froyder.personaltrainer.presentation.AppViewModel
import com.froyder.personaltrainer.presentation.onboarding.BodyInfoScreen
import com.froyder.personaltrainer.presentation.onboarding.EquipmentPickerScreen
import com.froyder.personaltrainer.presentation.onboarding.GoalPickerScreen
import com.froyder.personaltrainer.presentation.HomeScreen
import com.froyder.personaltrainer.presentation.LoadingScreen
import com.froyder.personaltrainer.presentation.PlanGenerationState
import com.froyder.personaltrainer.presentation.auth.AuthScreen
import com.froyder.personaltrainer.presentation.auth.AuthViewModel
import com.froyder.personaltrainer.presentation.auth.GuestUpgradeScreen
import com.froyder.personaltrainer.presentation.common.EmptyPlanScreen
import com.froyder.personaltrainer.presentation.common.OfflineBanner
import com.froyder.personaltrainer.presentation.menu.MenuScreen
import com.froyder.personaltrainer.presentation.menu.MenuViewModel
import com.froyder.personaltrainer.presentation.onboarding.LevelPickerScreen
import com.froyder.personaltrainer.presentation.onboarding.OnboardingViewModel
import com.froyder.personaltrainer.presentation.onboarding.PlanLoadingScreen
import com.froyder.personaltrainer.presentation.progress.ProgressScreen
import com.froyder.personaltrainer.presentation.splash.SplashScreen
import com.froyder.personaltrainer.presentation.theme.ThemeViewModel
import com.froyder.personaltrainer.presentation.workout.WorkoutScreen
import com.froyder.personaltrainer.utils.NetworkMonitor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Auth : Screen("auth")
    object GoalPicker : Screen("onboarding_goal")
    object LevelPicker : Screen("onboarding_level")
    object BodyInfo : Screen("onboarding_body")
    object EquipmentPicker : Screen("onboarding_equipment")
    object PlanLoading : Screen("plan_loading")
    object Home : Screen("home")
    object Workout : Screen("workout")
    object Progress : Screen("progress")
    object Menu : Screen("menu")
    object GuestUpgrade : Screen("guest_upgrade")
}

@Composable
fun NavGraph(
    onboardingViewModel: OnboardingViewModel,
    appViewModel: AppViewModel,
    authViewModel: AuthViewModel,
    menuViewModel: MenuViewModel,
    themeViewModel: ThemeViewModel,
    onNavControllerReady: (NavHostController) -> Unit
) {
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val bottomNavScreens = listOf(Screen.Home, Screen.Progress, Screen.Menu)
    val showBottomBar = currentRoute in bottomNavScreens.map { it.route }

    val planState by appViewModel.planState.collectAsState()
    val currentUser = appViewModel.currentUser.value ?: User()

    val progressScrollState = rememberScrollState()
    val menuScrollState = rememberScrollState()

    val networkMonitor = remember { NetworkMonitor() }
    var isOnline by remember { mutableStateOf(networkMonitor.isConnected()) }

    LaunchedEffect(Unit) {
        onNavControllerReady(navController)
    }

    LaunchedEffect(Unit) {
        while (true) {
            isOnline = networkMonitor.isConnected()
            delay(3000)
        }
    }

    Scaffold(
        topBar = {
            OfflineBanner(isVisible = !isOnline)
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    modifier = Modifier
                        .height(64.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ){
                    NavigationBarItem(
                        selected = currentRoute == Screen.Home.route,
                        onClick = {
                            if (currentRoute != Screen.Home.route) {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = { Text("🏠") },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Progress.route,
                        onClick = {
                            if (currentRoute == Screen.Progress.route) {
                                // Already on Progress — scroll to top
                                navController.currentBackStackEntry // trigger scroll
                                scope.launch { progressScrollState.animateScrollTo(0) }
                            } else {
                                navController.navigate(Screen.Progress.route) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = { Text("📊") },
                        label = { Text("Progress") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Menu.route,
                        onClick = {
                            if (currentRoute == Screen.Menu.route) {
                                // Already on Menu — scroll to top
                                navController.currentBackStackEntry // trigger scroll
                                scope.launch { menuScrollState.animateScrollTo(0) }
                            } else {
                                navController.navigate(Screen.Menu.route) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon = { Text("☰") },
                        label = { Text("Menu") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    authViewModel = authViewModel,
                    appViewModel = appViewModel,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Auth.route) {
                AuthScreen(
                    onAuthSuccess = {
                        val userId = authViewModel.currentUserId
                        appViewModel.initForUser(userId)
                        navController.navigate(Screen.Splash.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    },
                    onContinueAsGuest = {
                        authViewModel.continueAsGuest()
                        navController.navigate(Screen.GoalPicker.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    },
                    viewModel = authViewModel
                )
            }
            composable(Screen.GoalPicker.route) {
                GoalPickerScreen(
                    onNext = { navController.navigate(Screen.LevelPicker.route) },
                    viewModel = onboardingViewModel
                )
            }
            composable(Screen.LevelPicker.route) {
                LevelPickerScreen(
                    onNext = { navController.navigate(Screen.BodyInfo.route) },
                    viewModel = onboardingViewModel
                )
            }
            composable(Screen.BodyInfo.route) {
                BodyInfoScreen(
                    onNext = { navController.navigate(Screen.EquipmentPicker.route) },
                    viewModel = onboardingViewModel
                )
            }
            composable(Screen.EquipmentPicker.route) {
                EquipmentPickerScreen(
                    onNext = { navController.navigate(Screen.PlanLoading.route) },
                    viewModel = onboardingViewModel
                )
            }
            composable(Screen.PlanLoading.route) {
                PlanLoadingScreen(
                    onboardingViewModel = onboardingViewModel,
                    authViewModel = authViewModel,
                    appViewModel = appViewModel,
                    onPlanReady = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.GoalPicker.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                val plan = (planState as? PlanGenerationState.Success)?.plan
                    ?: MockData.samplePlan
                HomeScreen(
                    user = currentUser,
                    plan = plan,
                    onStartWorkout = { dayIndex ->
                        appViewModel.selectDay(dayIndex)
                        navController.navigate(Screen.Workout.route)
                    }
                )
            }
            composable(Screen.Home.route) {
                when (val state = planState) {
                    is PlanGenerationState.Loading -> {
                        LoadingScreen(
                            title = "Preparing",
                            subtitle = "NEXT WEEK",
                            message = "Great job completing this week!\nYour next plan is on its way."
                        )
                    }
                    is PlanGenerationState.Success -> {
                        HomeScreen(
                            user = currentUser,
                            plan = state.plan,
                            onStartWorkout = { dayIndex ->
                                appViewModel.selectDay(dayIndex)
                                navController.navigate(Screen.Workout.route)
                            }
                        )
                    }
                    is PlanGenerationState.Error -> {
                        EmptyPlanScreen(
                            message = state.message,
                            onRetry = { appViewModel.retryPlanGeneration() }
                        )
                    }
                    else -> {
                        EmptyPlanScreen(
                            message = "No plan found.",
                            onRetry = { appViewModel.retryPlanGeneration() }
                        )
                    }
                }
            }
            composable(Screen.Workout.route) {
                val plan = (planState as? PlanGenerationState.Success)?.plan
                    ?: MockData.samplePlan
                val dayIndex by appViewModel.selectedDayIndex.collectAsState()
                val trainingDay = plan.weeklyDays.getOrNull(dayIndex) ?: plan.weeklyDays.firstOrNull { !it.isCompleted } ?: return@composable

                WorkoutScreen(
                    trainingDay = trainingDay,
                    planId = plan.id,
                    userId = authViewModel.currentUserId,
                    onBack = { navController.popBackStack() },
                    onFinish = {
                        appViewModel.markDayCompleted(trainingDay.id)
                        val isGuest = authViewModel.isGuestMode.value
                        if (isGuest) {
                            navController.navigate(Screen.GuestUpgrade.route) {
                                popUpTo(Screen.Home.route) { inclusive = false }
                            }
                        } else {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    },
                    onSaveSession = { session ->
                        appViewModel.saveSession(session)
                    }
                )
            }
            composable(Screen.Progress.route) {
                val plan = (planState as? PlanGenerationState.Success)?.plan
                    ?: MockData.samplePlan
                val sessions = appViewModel.getCompletedSessions()
                val streak = appViewModel.calculateStreak()
                ProgressScreen(
                    scrollState = progressScrollState,
                    sessions = sessions,
                    plan = plan,
                    streak = streak
                )
            }
            composable(Screen.Menu.route) {
                MenuScreen(
                    scrollState = menuScrollState,
                    viewModel = menuViewModel,
                    authViewModel = authViewModel,
                    themeViewModel = themeViewModel,
                    userId = authViewModel.currentUserId,
                    onLogout = {
                        appViewModel.clearInMemoryState()
                        onboardingViewModel.reset()
                        authViewModel.reset()
                        authViewModel.logout()
                        navController.navigate(Screen.Auth.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onRegeneratePlan = {
                        appViewModel.currentUser.value?.let { user ->
                            onboardingViewModel.prefillFromUser(user)
                        }
                        navController.navigate(Screen.GoalPicker.route)
                    },
                    onDeleteAccount = {
                        val userId = authViewModel.currentUserId
                        authViewModel.deleteAccount {
                            appViewModel.deleteAllUserData(userId)
                            onboardingViewModel.reset()
                            authViewModel.reset()
                            navController.navigate(Screen.Auth.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    onCreateAccount = {
                        // Clear guest data and go to Auth
                        appViewModel.clearInMemoryState()
                        onboardingViewModel.reset()
                        authViewModel.exitGuestMode()
                        navController.navigate(Screen.Auth.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.GuestUpgrade.route) {
                GuestUpgradeScreen(
                    onCreateAccount = {
                        appViewModel.clearInMemoryState()
                        onboardingViewModel.reset()
                        authViewModel.exitGuestMode()
                        navController.navigate(Screen.Auth.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onSkip = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}