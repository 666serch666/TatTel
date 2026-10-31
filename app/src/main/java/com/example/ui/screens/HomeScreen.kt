package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppScreen
import com.example.data.Lesson
import com.example.data.UserProgress
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    progress: UserProgress,
    lessons: List<Lesson>,
    onStartLesson: (String) -> Unit,
    onNavigateToLessons: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isGreetingsCompleted = progress.completedLessons.contains("greetings")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TatTelLightBg),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        item {
            TatTelTopBar(
                title = "TatTel",
                subtitle = "Татар теле • Просто и понятно",
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Welcome & Current Level Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, TatTelCardBorder, RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = TatTelSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TatTelLightGreen
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = TatTelDarkGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = progress.currentLevel,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TatTelDarkGreen,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Text(
                            text = if (isGreetingsCompleted) "1 / 5 уроков" else "0 / 5 уроков",
                            style = MaterialTheme.typography.labelMedium,
                            color = TatTelTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Исәнмесез! Хәерле көн!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TatTelDarkGreen
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isGreetingsCompleted)
                            "Отличная работа! Вы освоили первый урок вежливости. Повторите для закрепления."
                        else
                            "Начните с пяти самых нужных слов: приветствия, благодарность и ответы.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TatTelTextSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Progress Bar
                    val progressRatio = if (isGreetingsCompleted) 0.20f else 0.05f
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Прогресс обучения",
                                style = MaterialTheme.typography.labelSmall,
                                color = TatTelTextSecondary
                            )
                            Text(
                                text = "${(progressRatio * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = TatTelDarkGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { progressRatio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = TatTelDarkGreen,
                            trackColor = TatTelLightGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Primary Action Button (Кнопка продолжения)
                    Button(
                        onClick = { onStartLesson("greetings") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("continue_learning_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TatTelDarkGreen,
                            contentColor = TatTelLightBg
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = if (isGreetingsCompleted) Icons.Default.PlayArrow else Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isGreetingsCompleted) "Повторить урок «Приветствия»" else "Продолжить обучение",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Quick Stats Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Слов изучено",
                    value = "${progress.wordsLearned}",
                    subtitle = "из 5 доступных",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Точность",
                    value = "${progress.accuracyPercentage}%",
                    subtitle = "по ответам",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Word of the Day / Fact
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = TatTelLightGreen)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(TatTelDarkGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Lightbulb,
                            contentDescription = null,
                            tint = TatTelLightBg,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Слово дня: Рәхмәт",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TatTelDarkGreen
                        )
                        Text(
                            text = "Переводится как «Спасибо». В татарском языке также говорят «Зур рәхмәт» — «Большое спасибо».",
                            style = MaterialTheme.typography.bodySmall,
                            color = TatTelMainText
                        )
                    }
                }
            }
        }

        // Section Title: Список уроков
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 22.dp, end = 22.dp, top = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Доступные уроки",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TatTelMainText
                )
                TextButton(onClick = onNavigateToLessons) {
                    Text(
                        text = "Все уроки",
                        color = TatTelDarkGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Show Lessons (First is greetings, others future)
        items(count = lessons.size) { index ->
            val lesson = lessons[index]
            val isCompleted = progress.completedLessons.contains(lesson.id)
            LessonCard(
                lesson = lesson,
                isCompleted = isCompleted,
                onClick = { onStartLesson(lesson.id) },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )
        }
    }
}
