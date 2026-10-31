package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppScreen
import com.example.data.ExerciseState
import com.example.data.Lesson
import com.example.data.UserProgress
import com.example.data.VocabularyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TatTelViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = VocabularyRepository(application.applicationContext)

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _userProgress = MutableStateFlow(repository.loadUserProgress())
    val userProgress: StateFlow<UserProgress> = _userProgress.asStateFlow()

    private val _lessons = MutableStateFlow(repository.allLessons)
    val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

    private val _exerciseState = MutableStateFlow(ExerciseState())
    val exerciseState: StateFlow<ExerciseState> = _exerciseState.asStateFlow()

    private val _activeLessonId = MutableStateFlow("greetings")
    val activeLessonId: StateFlow<String> = _activeLessonId.asStateFlow()

    private val _notificationMessage = MutableStateFlow<String?>(null)
    val notificationMessage: StateFlow<String?> = _notificationMessage.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun showNotice(message: String) {
        _notificationMessage.value = message
    }

    fun dismissNotice() {
        _notificationMessage.value = null
    }

    fun startLesson(lessonId: String = "greetings") {
        val lesson = repository.allLessons.find { it.id == lessonId }
        if (lesson != null && !lesson.isAvailable) {
            _notificationMessage.value = "Урок «${lesson.title}» в разработке и откроется в следующем обновлении!"
            return
        }

        _activeLessonId.value = lessonId
        val questions = repository.generateQuestionsForLesson(lessonId)
        _exerciseState.value = ExerciseState(
            currentQuestionIndex = 0,
            questions = questions,
            selectedOption = null,
            isAnswered = false,
            isCorrect = false,
            correctCount = 0,
            errorCount = 0,
            isFinished = false
        )
        _currentScreen.value = AppScreen.EXERCISE
    }

    fun selectOption(option: String) {
        val current = _exerciseState.value
        if (current.isAnswered) return // prevent changing answer after submit

        val question = current.currentQuestion ?: return
        val isCorrect = option == question.correctAnswer

        val newCorrectCount = if (isCorrect) current.correctCount + 1 else current.correctCount
        val newErrorCount = if (!isCorrect) current.errorCount + 1 else current.errorCount

        _exerciseState.update {
            it.copy(
                selectedOption = option,
                isAnswered = true,
                isCorrect = isCorrect,
                correctCount = newCorrectCount,
                errorCount = newErrorCount
            )
        }
    }

    fun nextQuestion() {
        val current = _exerciseState.value
        val nextIndex = current.currentQuestionIndex + 1

        if (nextIndex < current.questions.size) {
            _exerciseState.update {
                it.copy(
                    currentQuestionIndex = nextIndex,
                    selectedOption = null,
                    isAnswered = false,
                    isCorrect = false
                )
            }
        } else {
            // Exercise complete
            _exerciseState.update {
                it.copy(isFinished = true)
            }
            repository.saveLessonCompleted(
                lessonId = _activeLessonId.value,
                correctAnswers = current.correctCount,
                mistakes = current.errorCount
            )
            _userProgress.value = repository.loadUserProgress()
            _currentScreen.value = AppScreen.COMPLETION
        }
    }

    fun repeatLesson() {
        startLesson(_activeLessonId.value)
    }

    fun returnToHome() {
        _userProgress.value = repository.loadUserProgress()
        _currentScreen.value = AppScreen.HOME
    }

    fun handleBackPress(): Boolean {
        return when (_currentScreen.value) {
            AppScreen.EXERCISE -> {
                // Return to previous screen (Home or Lessons)
                _currentScreen.value = AppScreen.HOME
                true
            }
            AppScreen.COMPLETION -> {
                _currentScreen.value = AppScreen.HOME
                true
            }
            AppScreen.LESSONS, AppScreen.PROFILE -> {
                _currentScreen.value = AppScreen.HOME
                true
            }
            AppScreen.HOME -> {
                false // Allow system default exit
            }
        }
    }
}
