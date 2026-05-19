package com.froyder.personaltrainer.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.froyder.personaltrainer.data.WorkoutPlan
import com.froyder.personaltrainer.data.model.Equipment
import com.froyder.personaltrainer.data.model.FitnessGoal
import com.froyder.personaltrainer.data.model.FitnessLevel
import com.froyder.personaltrainer.data.model.User
import com.froyder.personaltrainer.data.model.WorkoutSession
import com.froyder.personaltrainer.data.remote.createHttpClient
import com.froyder.personaltrainer.data.repository.GeminiRepository
import com.froyder.personaltrainer.data.repository.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel : ViewModel() {
    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    fun setName(name: String) = _state.update { it.copy(name = name) }
    fun setAge(age: Int) = _state.update { it.copy(age = age) }
    fun setWeight(weight: Float) = _state.update { it.copy(weightKg = weight) }
    fun setHeight(height: Float) = _state.update { it.copy(heightCm = height) }
    fun setGoal(goal: FitnessGoal) = _state.update { it.copy(goal = goal) }
    fun setLevel(level: FitnessLevel) = _state.update { it.copy(level = level) }
    fun setEquipment(equipment: Equipment) = _state.update { it.copy(equipment = equipment) }
    fun setDaysPerWeek(days: Int) = _state.update { it.copy(daysPerWeek = days) }

    fun buildUser(): User = _state.value.run {
        User(
            id = "user_1",
            name = name,
            age = age,
            weightKg = weightKg,
            heightCm = heightCm,
            goal = goal,
            level = level,
            equipment = equipment,
            daysPerWeek = daysPerWeek
        )
    }

    fun prefillFromUser(user: User) {
        _state.update {
            it.copy(
                name = user.name,
                age = user.age,
                weightKg = user.weightKg,
                heightCm = user.heightCm,
                goal = user.goal,
                level = user.level,
                equipment = user.equipment,
                daysPerWeek = user.daysPerWeek
            )
        }
    }

    fun reset() {
        _state.value = OnboardingState()
    }
}

data class OnboardingState(
    val name: String = "",
    val age: Int = 0,
    val weightKg: Float = 0f,
    val heightCm: Float = 0f,
    val goal: FitnessGoal = FitnessGoal.STAY_ACTIVE,
    val level: FitnessLevel = FitnessLevel.BEGINNER,
    val equipment: Equipment = Equipment.NO_EQUIPMENT,
    val daysPerWeek: Int = 3
)