package com.froyder.personaltrainer.presentation.workout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.froyder.personaltrainer.data.Exercise
import com.froyder.personaltrainer.data.TrainingDay
import com.froyder.personaltrainer.data.model.ExerciseLog
import com.froyder.personaltrainer.data.model.SetLog
import com.froyder.personaltrainer.data.model.WorkoutSession
import com.froyder.personaltrainer.utils.screenPadding

@Composable
fun WorkoutScreen(
    trainingDay: TrainingDay,
    planId: String,
    userId: String,
    onFinish: () -> Unit,
    onSaveSession: (WorkoutSession) -> Unit
) {
    val viewModel = viewModel(key = trainingDay.id) {
        WorkoutViewModel(trainingDay, planId)
    }
    val state by viewModel.state.collectAsState()
    val isResting by viewModel.isResting.collectAsState()
    val restSeconds by viewModel.restSeconds.collectAsState()

    val scrollState = rememberScrollState()

    if (state.isCompleted) {
        WorkoutCompletedScreen(onFinish = onFinish)
        return
    }

    val currentExercise = trainingDay.exercises[state.currentExerciseIndex]
    val isLastExercise = state.currentExerciseIndex == trainingDay.exercises.lastIndex

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(screenPadding())
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Header
        Text(
            text = "Active",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.5.sp
        )
        Text(
            text = trainingDay.focusLabel.uppercase(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(16.dp))

        // Progress header
        WorkoutProgressHeader(
            current = state.currentExerciseIndex + 1,
            total = trainingDay.exercises.size
        )

        Spacer(Modifier.height(24.dp))

        // Exercise card
        ExerciseCard(
            exercise = currentExercise,
            exerciseLog = state.exerciseLogs.getOrNull(state.currentExerciseIndex),
            isResting = isResting,
            restSeconds = restSeconds,
            onLogSet = { setLog ->
                viewModel.logSet(state.currentExerciseIndex, setLog)
                viewModel.startRestTimer(currentExercise.restSeconds)  // 👈 start timer
            },
            onSkipRest = { viewModel.skipRestTimer() }
        )

        Spacer(Modifier.height(24.dp))

        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.currentExerciseIndex > 0) {
                OutlinedButton(
                    onClick = {
                        viewModel.setCurrentExercise(state.currentExerciseIndex - 1)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        "PREV",
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Button(
                onClick = {
                    if (isLastExercise) {
                        val session = viewModel.completeWorkout(userId)
                        onSaveSession(session)
                    } else {
                        viewModel.setCurrentExercise(state.currentExerciseIndex + 1)
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (isLastExercise) "FINISH" else "NEXT",
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun WorkoutProgressHeader(current: Int, total: Int) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "EXERCISE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "$current / $total",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { current.toFloat() / total.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    exerciseLog: ExerciseLog?,
    isResting: Boolean,
    restSeconds: Int,
    onLogSet: (SetLog) -> Unit,
    onSkipRest: () -> Unit
) {
    var repsInput by remember(exercise.id) { mutableStateOf(exercise.reps.toString()) }
    var weightInput by remember(exercise.id) { mutableStateOf("") }
    val completedSets = exerciseLog?.completedSets ?: emptyList()

    var showDetail by remember(exercise.id) { mutableStateOf(false) }

    if (showDetail) {
        ExerciseDetailDialog(
            exercise = exercise,
            onDismiss = { showDetail = false }
        )
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        // Accent layer
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, top = 5.dp)
                .matchParentSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {}

        // Content layer
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 5.dp, bottom = 5.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .clickable { showDetail = true }
            ) {
                // Exercise name + muscle group
                Text(
                    text = exercise.muscleGroup.name
                        .lowercase()
                        .replaceFirstChar { it.uppercase() }
                        .uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.5.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Tap for instructions",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )

                Spacer(Modifier.height(12.dp))

                // Info chips
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "${exercise.sets} sets × ${exercise.reps} reps",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = "Rest ${exercise.restSeconds}s",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (exercise.notes.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = exercise.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // REST TIMER — shows after logging a set
                if (isResting) {
                    RestTimerBlock(
                        seconds = restSeconds,
                        totalSeconds = exercise.restSeconds,
                        onSkip = onSkipRest
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }

                // Completed sets
                if (completedSets.isNotEmpty()) {
                    Text(
                        text = "LOGGED SETS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    completedSets.forEach { set ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Set ${set.setNumber}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = buildString {
                                    append("${set.reps} reps")
                                    if (set.weightKg > 0) append(" @ ${set.weightKg}kg")
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }

                // Log new set
                Text(
                    text = "LOG SET ${completedSets.size + 1}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isResting)
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.5.sp
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = repsInput,
                        onValueChange = { if (!isResting) repsInput = it },
                        label = { Text("Reps") },
                        enabled = !isResting,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { if (!isResting) weightInput = it },
                        label = { Text("kg (opt)") },
                        enabled = !isResting,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (isResting) return@Button
                        val reps = repsInput.toIntOrNull() ?: return@Button
                        onLogSet(
                            SetLog(
                                setNumber = completedSets.size + 1,
                                reps = reps,
                                weightKg = weightInput.toFloatOrNull() ?: 0f,
                                completed = true
                            )
                        )
                        weightInput = ""
                    },
                    enabled = !isResting,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = if (isResting) "RESTING..." else "LOG SET",
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RestTimerBlock(
    seconds: Int,
    totalSeconds: Int,
    onSkip: () -> Unit
) {
    val progress = if (totalSeconds > 0) seconds.toFloat() / totalSeconds.toFloat() else 0f

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "REST TIME",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.5.sp
        )
        Spacer(Modifier.height(12.dp))

        Box(contentAlignment = Alignment.Center) {
            // Circular progress
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(80.dp),
                strokeWidth = 6.dp,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outlineVariant
            )
            // Countdown number
            Text(
                text = "$seconds",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(Modifier.height(12.dp))

        // Skip button
        Box(
            modifier = Modifier
                .clickable(onClick = onSkip)
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "SKIP REST",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun WorkoutCompletedScreen(onFinish: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(screenPadding())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🎉", style = MaterialTheme.typography.displayLarge)
        Spacer(Modifier.height(24.dp))
        Text(
            text = "WORKOUT",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "COMPLETE",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Great work! Keep it up.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(40.dp))
        Button(
            onClick = onFinish,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "BACK TO HOME",
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}