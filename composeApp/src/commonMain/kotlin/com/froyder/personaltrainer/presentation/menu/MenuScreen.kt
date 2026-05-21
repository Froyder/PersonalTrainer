package com.froyder.personaltrainer.presentation.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.froyder.personaltrainer.presentation.auth.AuthViewModel
import com.froyder.personaltrainer.presentation.theme.ColorScheme
import com.froyder.personaltrainer.presentation.theme.DarkModePreference
import com.froyder.personaltrainer.presentation.theme.ThemeViewModel
import com.froyder.personaltrainer.presentation.theme.resolveColorScheme
import com.froyder.personaltrainer.utils.isSystemDarkMode
import com.froyder.personaltrainer.utils.screenPadding

@Composable
fun MenuScreen(
    scrollState: ScrollState,
    viewModel: MenuViewModel,
    authViewModel: AuthViewModel,
    themeViewModel: ThemeViewModel,
    userId: String,
    onLogout: () -> Unit,
    onRegeneratePlan: () -> Unit,
    onDeleteAccount: () -> Unit,
    onCreateAccount: () -> Unit
) {
    val user by viewModel.user.collectAsState()
    var activeDialog by remember { mutableStateOf<String?>(null) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    val themePrefs by themeViewModel.preferences.collectAsState()

    LaunchedEffect(userId) { viewModel.loadUser(userId) }

    // Dialogs
    when (activeDialog) {
        "name" -> EditFieldDialog(
            title = "Edit Name",
            currentValue = user?.name ?: "",
            onConfirm = { viewModel.updateName(it); activeDialog = null },
            onDismiss = { activeDialog = null }
        )
        "age" -> EditFieldDialog(
            title = "Edit Age",
            currentValue = user?.age?.toString() ?: "",
            keyboardType = KeyboardType.Number,
            onConfirm = {
                it.toIntOrNull()?.let { age -> viewModel.updateAge(age) }
                activeDialog = null
            },
            onDismiss = { activeDialog = null }
        )
        "weight" -> EditFieldDialog(
            title = "Edit Weight",
            currentValue = user?.weightKg?.toString() ?: "",
            keyboardType = KeyboardType.Decimal,
            onConfirm = {
                it.toFloatOrNull()?.let { w -> viewModel.updateWeight(w) }
                activeDialog = null
            },
            onDismiss = { activeDialog = null }
        )
        "height" -> EditFieldDialog(
            title = "Edit Height",
            currentValue = user?.heightCm?.toString() ?: "",
            keyboardType = KeyboardType.Decimal,
            onConfirm = {
                it.toFloatOrNull()?.let { h -> viewModel.updateHeight(h) }
                activeDialog = null
            },
            onDismiss = { activeDialog = null }
        )
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            title = {
                Text(
                    "DELETE ACCOUNT",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            },
            text = {
                Text(
                    "Are you sure you want to permanently delete your account and all your data? This action cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirmDialog = false
                    onDeleteAccount()
                }) {
                    Text(
                        "DELETE",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("CANCEL", letterSpacing = 1.sp)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(screenPadding())
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Header
        Text(
            text = "Your",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.5.sp
        )
        Text(
            text = "MENU",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(28.dp))

        val isGuest by authViewModel.isGuestMode.collectAsState()

        if (isGuest) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "GUEST MODE",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Your data is saved locally only. Sign up to sync across devices and never lose your progress.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = onCreateAccount,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "CREATE ACCOUNT",
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }

        // --- Profile Section ---
        MenuSectionHeader("Profile")
        user?.let { u ->
            val weightLabel = if (u.useImperialUnits)
                "${(u.weightKg * 2.205f).toInt()} lbs"
            else "${u.weightKg} kg"
            val heightLabel = if (u.useImperialUnits)
                "${(u.heightCm / 2.54f).toInt()} in"
            else "${u.heightCm} cm"

            MenuCard {
                MenuEditRow("Name", u.name) { activeDialog = "name" }
                MenuEditRow("Age", "${u.age} years") { activeDialog = "age" }
                MenuEditRow("Weight", weightLabel) { activeDialog = "weight" }
                MenuEditRow("Height", heightLabel, showDivider = false) { activeDialog = "height" }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Units toggle
        user?.let { u ->
            MenuCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Imperial Units",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "lbs and inches",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = u.useImperialUnits,
                        onCheckedChange = { viewModel.toggleUnits() }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // --- Plan Section ---
        MenuSectionHeader("Workout Plan")
        MenuCard {
            MenuActionRow(
                label = "Regenerate Plan",
                description = "Go through survey and create a new plan",
                onClick = onRegeneratePlan,
                showDivider = false
            )
        }

        Spacer(Modifier.height(24.dp))

        // --- Notifications Section ---
        MenuSectionHeader("Notifications")
        MenuCard {
            user?.let { u ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Daily Reminder",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "Get reminded to work out",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = u.notificationsEnabled,
                        onCheckedChange = { enabled ->
                            viewModel.updateNotificationSettings(
                                enabled = enabled,
                                hour = u.reminderHour,
                                minute = u.reminderMinute
                            )
                        }
                    )
                }

                if (u.notificationsEnabled) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                    TimePickerRow(
                        hour = u.reminderHour,
                        minute = u.reminderMinute,
                        onTimeSelected = { hour, minute ->
                            viewModel.updateNotificationSettings(
                                enabled = true,
                                hour = hour,
                                minute = minute
                            )
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // --- Appearance Section ---
        MenuSectionHeader("Appearance")
        MenuCard {
            Text(
                "COLOR SCHEME",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.5.sp
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ColorScheme.entries.forEach { scheme ->
                    ColorSchemeChip(
                        modifier = Modifier.weight(1f),
                        scheme = scheme,
                        selected = themePrefs.colorScheme == scheme,
                        onClick = { themeViewModel.setColorScheme(scheme) }
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Text(
                "DARK MODE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.5.sp
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DarkModePreference.entries.forEach { mode ->
                    FilterChip(
                        selected = themePrefs.darkMode == mode,
                        onClick = { themeViewModel.setDarkMode(mode) },
                        label = {
                            Text(
                                when (mode) {
                                    DarkModePreference.SYSTEM -> "System"
                                    DarkModePreference.LIGHT -> "Light"
                                    DarkModePreference.DARK -> "Dark"
                                },
                                fontWeight = if (themePrefs.darkMode == mode)
                                    FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // --- Account Section ---
        if (!isGuest) {
            Spacer(Modifier.height(24.dp))
            MenuSectionHeader("Account")
            MenuCard {
                MenuActionRow(
                    label = "Log Out",
                    description = "Sign out of your account",
                    onClick = onLogout
                )
                MenuActionRow(
                    label = "Delete Account",
                    description = "Permanently delete your account and data",
                    onClick = { showDeleteConfirmDialog = true },
                    isDestructive = true,
                    showDivider = false
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

// Reusable card container for menu sections
@Composable
fun MenuCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            content = content
        )
    }
}

@Composable
fun MenuSectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = 1.5.sp
    )
    Spacer(Modifier.height(8.dp))
}

@Composable
fun MenuEditRow(
    label: String,
    value: String,
    showDivider: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "›",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
    if (showDivider) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Composable
fun MenuActionRow(
    label: String,
    description: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false,
    showDivider: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (isDestructive)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.onSurface
            )
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            "›",
            style = MaterialTheme.typography.titleLarge,
            color = if (isDestructive)
                MaterialTheme.colorScheme.error
            else
                MaterialTheme.colorScheme.primary
        )
    }
    if (showDivider) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Composable
fun ColorSchemeChip(
    modifier: Modifier = Modifier,
    scheme: ColorScheme,
    selected: Boolean,
    onClick: () -> Unit
) {
    val isDark = isSystemDarkMode()
    val colors = resolveColorScheme(scheme, isDark)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(10.dp))  // 👈 square with rounded corners
                .background(colors.primary)
                .border(
                    width = if (selected) 2.5.dp else 0.dp,
                    color = if (selected)
                        MaterialTheme.colorScheme.onSurface
                    else
                        Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                )
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = scheme.name.lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}

@Composable
fun TimePickerRow(
    hour: Int,
    minute: Int,
    onTimeSelected: (Int, Int) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    val timeLabel = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"

    if (showPicker) {
        TimePickerDialog(
            initialHour = hour,
            initialMinute = minute,
            onConfirm = { h, m ->
                onTimeSelected(h, m)
                showPicker = false
            },
            onDismiss = { showPicker = false }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showPicker = true }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Reminder Time",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                timeLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "›",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var hour by remember { mutableStateOf(initialHour) }
    var minute by remember { mutableStateOf(initialMinute) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.padding(vertical = 20.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurface,
        title = {
            Column {
                Text(
                    text = "Reminder",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "SET TIME",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hour picker
                NumberPicker(
                    value = hour,
                    range = 0..23,
                    onValueChange = { hour = it },
                    label = "HH"
                )
                Text(
                    text = ":",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                // Minute picker
                NumberPicker(
                    value = minute,
                    range = 0..59,
                    onValueChange = { minute = it },
                    label = "MM"
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .clickable { onConfirm(hour, minute) }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    "SET",
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            Box(
                modifier = Modifier
                    .clickable(onClick = onDismiss)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    "CANCEL",
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

@Composable
fun NumberPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.sp
        )
        Spacer(Modifier.height(8.dp))
        IconButton(
            onClick = {
                onValueChange(if (value >= range.last) range.first else value + 1)
            }
        ) {
            Text(
                "▲",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Text(
            text = value.toString().padStart(2, '0'),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        IconButton(
            onClick = {
                onValueChange(if (value <= range.first) range.last else value - 1)
            }
        ) {
            Text(
                "▼",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}