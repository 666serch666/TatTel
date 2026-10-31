package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Lesson
import com.example.data.UserProgress
import com.example.data.VocabularyRepository
import com.example.ui.theme.*

@Composable
fun LessonsScreen(
    lessons: List<Lesson>,
    progress: UserProgress,
    onStartLesson: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedUpcomingLesson by remember { mutableStateOf<Lesson?>(null) }
    var showWordsPreview by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TatTelLightBg),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        item {
            TatTelTopBar(
                title = "Уроки",
                subtitle = "Тематический курс татарского языка",
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Active Course Info Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, TatTelCardBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = TatTelSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Базовый модуль",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TatTelDarkGreen
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TatTelLightGreen
                        ) {
                            Text(
                                text = "1 доступен",
                                style = MaterialTheme.typography.labelSmall,
                                color = TatTelDarkGreen,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Освойте разговорные фразы шаг за шагом. Первым шагом станут главные слова вежливости.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TatTelTextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedButton(
                        onClick = { showWordsPreview = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TatTelDarkGreen)
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Словарь первого урока (5 слов)")
                    }
                }
            }
        }

        item {
            Text(
                text = "Список уроков курса",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TatTelMainText,
                modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 16.dp, bottom = 8.dp)
            )
        }

        items(count = lessons.size) { index ->
            val lesson = lessons[index]
            val isCompleted = progress.completedLessons.contains(lesson.id)

            LessonCard(
                lesson = lesson,
                isCompleted = isCompleted,
                onClick = {
                    if (lesson.isAvailable) {
                        onStartLesson(lesson.id)
                    } else {
                        selectedUpcomingLesson = lesson
                    }
                },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )
        }
    }

    // Modal dialog for upcoming lesson information
    selectedUpcomingLesson?.let { lesson ->
        AlertDialog(
            onDismissRequest = { selectedUpcomingLesson = null },
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(TatTelLightGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = TatTelDarkGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Урок «${lesson.title}»",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TatTelDarkGreen
                )
            },
            text = {
                Column {
                    Text(
                        text = lesson.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TatTelMainText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Этот урок находится в подготовке и будет открыт в следующем выпуске TatTel. Сейчас вам доступен урок «Приветствия»!",
                        style = MaterialTheme.typography.bodySmall,
                        color = TatTelTextSecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedUpcomingLesson = null
                        onStartLesson("greetings")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TatTelDarkGreen)
                ) {
                    Text("Открыть «Приветствия»")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedUpcomingLesson = null }) {
                    Text("Понятно", color = TatTelTextSecondary)
                }
            },
            containerColor = TatTelSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Modal dialog with the 5 words preview
    if (showWordsPreview) {
        val words = VocabularyRepository().greetingsWords
        AlertDialog(
            onDismissRequest = { showWordsPreview = false },
            title = {
                Text(
                    text = "Слова первого урока",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TatTelDarkGreen
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    words.forEach { word ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TatTelLightBg,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = word.tatarWord,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TatTelDarkGreen
                                    )
                                    Text(
                                        text = word.transcription,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TatTelTextMuted
                                    )
                                }
                                Text(
                                    text = word.russianTranslation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = TatTelMainText
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showWordsPreview = false
                        onStartLesson("greetings")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TatTelDarkGreen)
                ) {
                    Text("Начать упражнение")
                }
            },
            dismissButton = {
                TextButton(onClick = { showWordsPreview = false }) {
                    Text("Закрыть", color = TatTelTextSecondary)
                }
            },
            containerColor = TatTelSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }
}
