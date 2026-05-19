package com.froyder.personaltrainer.presentation.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.froyder.personaltrainer.data.model.Equipment
import com.froyder.personaltrainer.data.model.FitnessGoal
import com.froyder.personaltrainer.data.model.FitnessLevel
import com.froyder.personaltrainer.utils.screenPadding

// Reusable scaffold for all onboarding steps
@Composable
fun OnboardingScaffold(
    step: Int,
    totalSteps: Int = 4,
    title: String,
    subtitle: String,
    onNext: () -> Unit,
    nextEnabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(screenPadding())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Step indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                (1..totalSteps).forEach { i ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (i <= step)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.outlineVariant
                            )
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Header
            Text(
                text = "Step $step",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.5.sp
            )
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(28.dp))
            content()
        }

        Button(
            onClick = onNext,
            enabled = nextEnabled,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "CONTINUE",
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun <T> OptionCard(
    option: T,
    label: String,
    description: String,
    selected: Boolean,
    onSelect: (T) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelect(option) },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(
                width = if (selected) 2.dp else 0.5.dp,
                color = if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outlineVariant
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (selected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (selected)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (selected)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// --- Screen 1: Goal ---
@Composable
fun GoalPickerScreen(onNext: () -> Unit, viewModel: OnboardingViewModel) {
    val state by viewModel.state.collectAsState()

    OnboardingScaffold(
        step = 1,
        title = "What's your goal?",
        subtitle = "We'll build your plan around this.",
        onNext = onNext
    ) {
        listOf(
            Triple(FitnessGoal.LOSE_WEIGHT, "Lose Weight", "Burn fat and improve body composition"),
            Triple(FitnessGoal.GAIN_MUSCLE, "Gain Muscle", "Build strength and increase muscle mass"),
            Triple(FitnessGoal.IMPROVE_ENDURANCE, "Improve Endurance", "Boost stamina and cardiovascular health"),
            Triple(FitnessGoal.STAY_ACTIVE, "Stay Active", "Maintain a healthy and active lifestyle")
        ).forEach { (goal, label, desc) ->
            OptionCard(
                option = goal,
                label = label,
                description = desc,
                selected = state.goal == goal,
                onSelect = { viewModel.setGoal(it) }
            )
        }
    }
}

// --- Screen 2: Fitness Level ---
@Composable
fun LevelPickerScreen(onNext: () -> Unit, viewModel: OnboardingViewModel) {
    val state by viewModel.state.collectAsState()

    OnboardingScaffold(
        step = 2,
        title = "Your fitness level?",
        subtitle = "Be honest — we'll adjust as you progress.",
        onNext = onNext
    ) {
        listOf(
            Triple(FitnessLevel.BEGINNER, "Beginner", "Little or no workout experience"),
            Triple(FitnessLevel.INTERMEDIATE, "Intermediate", "Workout regularly for 6+ months"),
            Triple(FitnessLevel.ADVANCED, "Advanced", "Training seriously for 2+ years")
        ).forEach { (level, label, desc) ->
            OptionCard(
                option = level,
                label = label,
                description = desc,
                selected = state.level == level,
                onSelect = { viewModel.setLevel(it) }
            )
        }
    }
}

// --- Screen 3: Body Info ---
@Composable
fun BodyInfoScreen(onNext: () -> Unit, viewModel: OnboardingViewModel) {
    val state by viewModel.state.collectAsState()
    var nameInput by remember { mutableStateOf(state.name) }
    var ageInput by remember { mutableStateOf(if (state.age > 0) state.age.toString() else "") }
    var weightInput by remember { mutableStateOf(if (state.weightKg > 0) state.weightKg.toString() else "") }
    var heightInput by remember { mutableStateOf(if (state.heightCm > 0) state.heightCm.toString() else "") }

    val isValid = nameInput.isNotBlank()
            && ageInput.toIntOrNull() != null
            && weightInput.toFloatOrNull() != null
            && heightInput.toFloatOrNull() != null

    OnboardingScaffold(
        step = 3,
        title = "About you",
        subtitle = "Used to personalize your plan.",
        onNext = {
            viewModel.setName(nameInput)
            viewModel.setAge(ageInput.toInt())
            viewModel.setWeight(weightInput.toFloat())
            viewModel.setHeight(heightInput.toFloat())
            onNext()
        },
        nextEnabled = isValid
    ) {
        // Name
        Text(
            text = "NAME",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.5.sp
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            placeholder = { Text("Your name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        Spacer(Modifier.height(14.dp))

        // Age
        Text(
            text = "AGE",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.5.sp
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = ageInput,
            onValueChange = { ageInput = it },
            placeholder = { Text("Years") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        Spacer(Modifier.height(14.dp))

        // Weight + Height side by side
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "WEIGHT",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.5.sp
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    placeholder = { Text("kg") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "HEIGHT",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.5.sp
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = heightInput,
                    onValueChange = { heightInput = it },
                    placeholder = { Text("cm") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
        }
    }
}

// --- Screen 4: Equipment ---
@Composable
fun EquipmentPickerScreen(onNext: () -> Unit, viewModel: OnboardingViewModel) {
    val state by viewModel.state.collectAsState()

    OnboardingScaffold(
        step = 4,
        title = "Your equipment?",
        subtitle = "We'll only plan exercises you can actually do.",
        onNext = onNext
    ) {
        listOf(
            Triple(Equipment.NO_EQUIPMENT, "No Equipment", "Bodyweight exercises only"),
            Triple(Equipment.HOME_BASICS, "Home Basics", "Dumbbells, resistance bands, pull-up bar"),
            Triple(Equipment.FULL_GYM, "Full Gym", "Access to a fully equipped gym")
        ).forEach { (equipment, label, desc) ->
            OptionCard(
                option = equipment,
                label = label,
                description = desc,
                selected = state.equipment == equipment,
                onSelect = { viewModel.setEquipment(it) }
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "DAYS PER WEEK",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.5.sp
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            (2..6).forEach { days ->
                val selected = state.daysPerWeek == days
                Box(modifier = Modifier.weight(1f)) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setDaysPerWeek(days) },
                        shape = RoundedCornerShape(8.dp),
                        color = if (selected)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            width = if (selected) 2.dp else 0.5.dp,
                            color = if (selected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Text(
                            text = "$days",
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (selected)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}