// commonMain/kotlin/com/gibson/spica/presentation/GoalViewModel.kt

package com.gibson.spica.presentation

import com.gibson.spica.data.ServiceLocator
import com.gibson.spica.data.model.Goal
import com.gibson.spica.data.repository.GoalRepository
import com.gibson.spica.data.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

// Expect class for multiplatform ViewModel scope (assuming custom implementation)
expect open class SharedViewModel() {
    val viewModelScope: CoroutineScope
}

data class GoalUiState(
    val goals: List<Goal> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class GoalViewModel(
    private val goalRepository: GoalRepository = ServiceLocator.goalRepository,
    private val authRepository: AuthRepository = ServiceLocator.authRepository
) : SharedViewModel() {

    private val _uiState = MutableStateFlow(GoalUiState())
    val uiState: StateFlow<GoalUiState> = _uiState.asStateFlow()

    private val userId: String = authRepository.getCurrentUserId() 
        ?: throw IllegalStateException("GoalViewModel initialized before user login.")

    init {
        loadGoals()
    }

    private fun loadGoals() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                // Subscribe to the real-time stream of goals for the current user
                goalRepository.getGoalsStream(userId)
                    .catch { e -> 
                        _uiState.update { it.copy(error = "Stream error: ${e.message}", isLoading = false) }
                    }
                    .collect { goals ->
                        _uiState.update { it.copy(goals = goals, isLoading = false) }
                    }
            } catch (e: Exception) {
                // Handle immediate errors during setup
                _uiState.update { it.copy(error = "Setup error: ${e.message}", isLoading = false) }
            }
        }
    }

    fun toggleGoalStatus(goal: Goal) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // If completing, set completion time. If uncompleting, set it to null.
                val completionTime = if (!goal.isCompleted) Clock.System.now() else null

                goalRepository.updateGoalStatus(
                    goalId = goal.id, 
                    userId = goal.userId, 
                    isCompleted = !goal.isCompleted, 
                    completedAt = completionTime
                )
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to update goal: ${e.message}") }
            }
        }
    }

    fun addGoal(title: String, description: String) {
        if (title.isBlank()) return // Basic validation

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newGoal = Goal(
                    userId = userId, 
                    title = title, 
                    description = description,
                    createdAt = Clock.System.now()
                )
                goalRepository.addGoal(newGoal)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to add goal: ${e.message}") }
            }
        }
    }
}
