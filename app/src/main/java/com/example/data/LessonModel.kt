package com.example.data

data class Word(
    val id: String,
    val tatarWord: String,
    val russianTranslation: String,
    val transcription: String,
    val usageHint: String
)

data class Lesson(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val isAvailable: Boolean,
    val wordCount: Int,
    val estimatedMinutes: Int,
    val words: List<Word> = emptyList()
)

data class Question(
    val id: Int,
    val word: Word,
    val options: List<String>,
    val correctAnswer: String
)

data class ExerciseState(
    val currentQuestionIndex: Int = 0,
    val questions: List<Question> = emptyList(),
    val selectedOption: String? = null,
    val isAnswered: Boolean = false,
    val isCorrect: Boolean = false,
    val correctCount: Int = 0,
    val errorCount: Int = 0,
    val isFinished: Boolean = false
) {
    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)

    val progress: Float
        get() = if (questions.isEmpty()) 0f else (currentQuestionIndex.toFloat() / questions.size.toFloat())
}

data class UserProgress(
    val completedLessons: Set<String> = emptySet(),
    val completedLessonCount: Int = 0,
    val wordsLearned: Int = 0,
    val totalCorrect: Int = 0,
    val totalMistakes: Int = 0,
    val currentLevel: String = "Уровень 1: Начинающий"
) {
    val accuracyPercentage: Int
        get() {
            val total = totalCorrect + totalMistakes
            return if (total == 0) 100 else ((totalCorrect.toFloat() / total.toFloat()) * 100).toInt()
        }
}

enum class AppScreen {
    HOME,
    LESSONS,
    PROFILE,
    EXERCISE,
    COMPLETION
}
