package com.froyder.personaltrainer.presentation.splash

import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.froyder.personaltrainer.navigation.Screen
import com.froyder.personaltrainer.presentation.AppViewModel
import com.froyder.personaltrainer.presentation.PlanGenerationState
import com.froyder.personaltrainer.presentation.SyncState
import com.froyder.personaltrainer.presentation.auth.AuthViewModel
import com.froyder.personaltrainer.utils.getCurrentTimeMillis
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.froyder.personaltrainer.presentation.LoadingScreen
import personaltrainer.composeapp.generated.resources.Res
import personaltrainer.composeapp.generated.resources.dumbbell

@Composable
fun SplashScreen(
    authViewModel: AuthViewModel,
    appViewModel: AppViewModel,
    onNavigate: (String) -> Unit
) {

    LaunchedEffect(Unit) {
        val startTime = getCurrentTimeMillis()

        val isGuest = authViewModel.isGuestMode.value

        when {
            // Returning guest — load local data and go home
            isGuest -> {
                val userId = "guest_local"
                appViewModel.initForUser(userId)

                var waited = 0L
                while (appViewModel.syncState.value != SyncState.Done && waited < 3000L) {
                    delay(100)
                    waited += 100
                }

                val elapsed = getCurrentTimeMillis() - startTime
                val remaining = 1500L - elapsed
                if (remaining > 0) delay(remaining)

                val destination = when {
                    appViewModel.planState.value is PlanGenerationState.Success -> Screen.Home.route
                    appViewModel.currentUser.value != null -> Screen.Home.route
                    else -> Screen.GoalPicker.route
                }
                onNavigate(destination)
            }

            // Not logged in and not guest → Auth
            !authViewModel.isLoggedIn -> {
                val elapsed = getCurrentTimeMillis() - startTime
                val remaining = 1500L - elapsed
                if (remaining > 0) delay(remaining)
                onNavigate(Screen.Auth.route)
            }

            // Logged in user → sync and go home
            else -> {
                val userId = authViewModel.currentUserId
                appViewModel.initForUser(userId)

                var waited = 0L
                while (appViewModel.syncState.value != SyncState.Done && waited < 5000L) {
                    delay(100)
                    waited += 100
                }

                val elapsed = getCurrentTimeMillis() - startTime
                val remaining = 1500L - elapsed
                if (remaining > 0) delay(remaining)

                val destination = when {
                    appViewModel.planState.value is PlanGenerationState.Success -> Screen.Home.route
                    appViewModel.currentUser.value != null -> Screen.Home.route
                    else -> Screen.GoalPicker.route
                }
                onNavigate(destination)
            }
        }
    }

    // Loading UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadingScreen(
                title = "Personal",
                subtitle = "TRAINER",
                message = ""
            )

            PulsingDotsIndicator()
        }
    }
}

@Composable
fun PulsingDotsIndicator() {
    val infiniteTransition = rememberInfiniteTransition()

    val dot1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 900
                1.0f at 0
                0.5f at 150
                0.2f at 300
            },
            repeatMode = RepeatMode.Restart
        )
    )
    val dot2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 900
                0.5f at 0
                1.0f at 450
                0.5f at 600
            },
            repeatMode = RepeatMode.Restart
        )
    )
    val dot3Alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 900
                0.2f at 0
                0.5f at 600
                1.0f at 750
            },
            repeatMode = RepeatMode.Restart
        )
    )

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        listOf(dot1Alpha, dot2Alpha, dot3Alpha).forEach { alpha ->
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = alpha)
                    )
            )
        }
    }
}