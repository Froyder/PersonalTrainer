package com.froyder.personaltrainer.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.froyder.personaltrainer.presentation.AppViewModel
import com.froyder.personaltrainer.presentation.LoadingScreen
import com.froyder.personaltrainer.presentation.PlanGenerationState
import com.froyder.personaltrainer.presentation.auth.AuthViewModel
import com.froyder.personaltrainer.presentation.common.EmptyPlanScreen
import com.froyder.personaltrainer.presentation.splash.PulsingDotsIndicator
import com.froyder.personaltrainer.utils.screenPadding
import org.jetbrains.compose.resources.painterResource
import personaltrainer.composeapp.generated.resources.Res
import personaltrainer.composeapp.generated.resources.dumbbell

@Composable
fun PlanLoadingScreen(
    onboardingViewModel: OnboardingViewModel,
    authViewModel: AuthViewModel,
    appViewModel: AppViewModel,
    onPlanReady: () -> Unit
) {
    val planState by appViewModel.planState.collectAsState()

    LaunchedEffect(Unit) {
        val user = onboardingViewModel.buildUser().copy(id = authViewModel.currentUserId)
        appViewModel.saveUser(user)
        appViewModel.generatePlan(user)
    }

    LaunchedEffect(planState) {
        if (planState is PlanGenerationState.Success) onPlanReady()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(screenPadding())
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = planState) {
            is PlanGenerationState.Loading, PlanGenerationState.Idle -> {
                LoadingScreen(
                    title = "Building",
                    subtitle = "YOUR PLAN",
                    message = "Crafting a personalized workout plan\njust for you. This will only take a moment."
                )
            }

            is PlanGenerationState.Error -> {
                EmptyPlanScreen(
                    message = state.message,
                    onRetry = {
                        val user = onboardingViewModel.buildUser()
                            .copy(id = authViewModel.currentUserId)
                        appViewModel.generatePlan(user)
                    }
                )
            }

            else -> {}
        }
    }
}