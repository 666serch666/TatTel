package com.example.data

import android.content.Context
import android.content.SharedPreferences

class VocabularyRepository(context: Context? = null) {

    private val prefs: SharedPreferences? = context?.getSharedPreferences("tattel_prefs", Context.MODE_PRIVATE)

    val greetingsWords = listOf(
        Word(
            id = "w1",
            tatarWord = "Исәнме",
            russianTranslation = "Привет",
            transcription = "[исән-ме]",
            usageHint = "Дружеское приветствие при встрече"
        ),
        Word(
            id = "w2",
            tatarWord = "Рәхмәт",
            russianTranslation = "Спасибо",
            transcription = "[рәх-мәт]",
            usageHint = "Выражение благодарности и признательности"
        ),
        Word(
            id = "w3",
            tatarWord = "Әйе",
            russianTranslation = "Да",
            transcription = "[әй-е]",
            usageHint = "Утвердительный ответ и согласие"
        ),
        Word(
            id = "w4",
            tatarWord = "Юк",
            russianTranslation = "Нет",
            transcription = "[йук]",
            usageHint = "Отрицательный ответ или несогласие"
        ),
        Word(
            id = "w5",
            tatarWord = "Сау бул",
            russianTranslation = "До свидания",
            transcription = "[сау бул]",
            usageHint = "Пожелание здоровья при прощании (букв. «будь здоров»)"
        )
    )

    val allLessons: List<Lesson> = listOf(
        Lesson(
            id = "greetings",
            title = "Приветствия",
            subtitle = "Базовые слова вежливости",
            description = "Исәнме, Рәхмәт, Әйе, Юк, Сау бул",
            isAvailable = true,
            wordCount = 5,
            estimatedMinutes = 3,
            words = greetingsWords
        ),
        Lesson(
            id = "introductions",
            title = "Знакомство",
            subtitle = "Как представиться и спросить имя",
            description = "Исем, танышу, хәлләр",
            isAvailable = false,
            wordCount = 6,
            estimatedMinutes = 4
        ),
        Lesson(
            id = "family",
            title = "Семья",
            subtitle = "Близкие и родственники",
            description = "Әни, әти, бала, гаилә",
            isAvailable = false,
            wordCount = 6,
            estimatedMinutes = 4
        ),
        Lesson(
            id = "food",
            title = "Еда",
            subtitle = "Традиционные блюда и трапеза",
            description = "Чәй, өчпочмак, аш, су",
            isAvailable = false,
            wordCount = 7,
            estimatedMinutes = 5
        ),
        Lesson(
            id = "daily_life",
            title = "Повседневная жизнь",
            subtitle = "Город, дом и расписание",
            description = "Шәһәр, өй, вакыт",
            isAvailable = false,
            wordCount = 6,
            estimatedMinutes = 4
        )
    )

    fun generateQuestionsForLesson(lessonId: String = "greetings"): List<Question> {
        val lessonWords = greetingsWords.shuffled()
        val allTranslations = greetingsWords.map { it.russianTranslation }

        return lessonWords.mapIndexed { index, word ->
            val otherTranslations = allTranslations.filter { it != word.russianTranslation }
            val distractors = otherTranslations.shuffled().take(3)
            val options = (distractors + word.russianTranslation).shuffled()
            Question(
                id = index + 1,
                word = word,
                options = options,
                correctAnswer = word.russianTranslation
            )
        }
    }

    fun loadUserProgress(): UserProgress {
        val completedCount = prefs?.getInt("completed_lessons_count", 0) ?: 0
        val isGreetingsDone = prefs?.getBoolean("lesson_greetings_done", false) ?: false
        val wordsCount = prefs?.getInt("words_learned", 0) ?: (if (isGreetingsDone) 5 else 0)
        val totalCorrect = prefs?.getInt("total_correct", 0) ?: 0
        val totalMistakes = prefs?.getInt("total_mistakes", 0) ?: 0

        val completedSet = mutableSetOf<String>()
        if (isGreetingsDone) {
            completedSet.add("greetings")
        }

        return UserProgress(
            completedLessons = completedSet,
            completedLessonCount = if (completedSet.isNotEmpty()) 1 else completedCount,
            wordsLearned = wordsCount,
            totalCorrect = totalCorrect,
            totalMistakes = totalMistakes
        )
    }

    fun saveLessonCompleted(lessonId: String, correctAnswers: Int, mistakes: Int) {
        prefs?.edit()?.apply {
            putBoolean("lesson_${lessonId}_done", true)
            putInt("completed_lessons_count", 1)
            putInt("words_learned", 5)
            val currentCorrect = prefs.getInt("total_correct", 0)
            val currentMistakes = prefs.getInt("total_mistakes", 0)
            putInt("total_correct", currentCorrect + correctAnswers)
            putInt("total_mistakes", currentMistakes + mistakes)
            apply()
        }
    }
}
