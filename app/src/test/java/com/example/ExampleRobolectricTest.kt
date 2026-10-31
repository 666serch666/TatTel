package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.VocabularyRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("TatTel", appName)
    }

    @Test
    fun `test vocabulary repository greetings words`() {
        val repo = VocabularyRepository()
        assertEquals(5, repo.greetingsWords.size)

        val tatarWords = repo.greetingsWords.map { it.tatarWord }
        assertTrue(tatarWords.contains("Исәнме"))
        assertTrue(tatarWords.contains("Рәхмәт"))
        assertTrue(tatarWords.contains("Әйе"))
        assertTrue(tatarWords.contains("Юк"))
        assertTrue(tatarWords.contains("Сау бул"))
    }

    @Test
    fun `test question generation has 4 options and correct answer`() {
        val repo = VocabularyRepository()
        val questions = repo.generateQuestionsForLesson("greetings")
        assertEquals(5, questions.size)

        for (q in questions) {
            assertEquals(4, q.options.size)
            assertTrue(q.options.contains(q.correctAnswer))
        }
    }
}
