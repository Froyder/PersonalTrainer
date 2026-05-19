package com.froyder.personaltrainer.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.froyder.personaltrainer.utils.screenPadding

@Composable
fun EmptyPlanScreen(
    message: String,
    onRetry: () -> Unit
) {
    val isQuotaError = message.contains("quota", ignoreCase = true)
            || message.contains("Empty response", ignoreCase = true)
    val isNetworkError = message.contains("internet", ignoreCase = true)
            || message.contains("connection", ignoreCase = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(screenPadding())
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Error icon
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.errorContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when {
                        isNetworkError -> "📡"
                        isQuotaError -> "⏳"
                        else -> "⚠️"
                    },
                    style = MaterialTheme.typography.displaySmall
                )
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = when {
                    isNetworkError -> "No"
                    isQuotaError -> "Service"
                    else -> "Something"
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
                letterSpacing = 1.5.sp
            )
            Text(
                text = when {
                    isNetworkError -> "CONNECTION"
                    isQuotaError -> "IS BUSY"
                    else -> "WENT WRONG"
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = when {
                    isNetworkError -> "Please check your internet connection\nand try again."
                    isQuotaError -> "The AI service is temporarily busy.\nPlease wait a moment and try again."
                    else -> "We couldn't generate your plan.\nPlease try again."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(40.dp))

            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "TRY AGAIN",
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}