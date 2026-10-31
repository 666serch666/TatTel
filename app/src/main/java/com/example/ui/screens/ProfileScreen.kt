package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProgress
import com.example.data.VocabularyRepository
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    progress: UserProgress,
    modifier: Modifier = Modifier
) {
    var showAlphabetDialog by remember { mutableStateOf(false) }
    var showDictionaryDialog by remember { mutableStateOf(false) }

    val courseProgressPercent = if (progress.completedLessonCount > 0) 20 else 0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TatTelLightBg),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        item {
            TatTelTopBar(
                title = "TatTel",
                subtitle = "Профиль ученика",
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // User Avatar Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, TatTelCardBorder, RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = TatTelSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(TatTelLightGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Аватар",
                            tint = TatTelDarkGreen,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Ученик TatTel",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TatTelDarkGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = progress.currentLevel,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TatTelTextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TatTelLightGreen
                        ) {
                            Text(
                                text = "Локальный профиль (без регистрации)",
                                style = MaterialTheme.typography.labelSmall,
                                color = TatTelDarkGreen,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Section: Общий прогресс
        item {
            Text(
                text = "Общий прогресс",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TatTelMainText,
                modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 16.dp, bottom = 8.dp)
            )
        }

        // Progress Overview Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp)
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
                            text = "Освоение курса",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TatTelMainText
                        )
                        Text(
                            text = "$courseProgressPercent%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TatTelDarkGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { courseProgressPercent / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = TatTelDarkGreen,
                        trackColor = TatTelLightGreen
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (progress.completedLessonCount > 0)
                            "Пройден 1 урок из 5 тематических уроков курса."
                        else
                            "Пройдите первый урок «Приветствия», чтобы начать заполнять прогресс.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TatTelTextSecondary
                    )
                }
            }
        }

        // Stats Grid
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Пройдено уроков",
                    value = "${progress.completedLessonCount}",
                    subtitle = "из 5 доступных",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Изучено слов",
                    value = "${progress.wordsLearned}",
                    subtitle = "татарских слов",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Useful Reference Cards
        item {
            Text(
                text = "Справочные материалы",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TatTelMainText,
                modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 16.dp, bottom = 8.dp)
            )
        }

        // My vocabulary button
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, TatTelCardBorder, RoundedCornerShape(16.dp))
                    .clickable { showDictionaryDialog = true },
                colors = CardDefaults.cardColors(containerColor = TatTelSurface)
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
                            .clip(RoundedCornerShape(12.dp))
                            .background(TatTelLightGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Book, contentDescription = null, tint = TatTelDarkGreen)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Словарь базового урока",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TatTelMainText
                        )
                        Text(
                            text = "5 практических слов с переводом",
                            style = MaterialTheme.typography.bodySmall,
                            color = TatTelTextSecondary
                        )
                    }
                    Text(
                        text = "Открыть",
                        style = MaterialTheme.typography.labelSmall,
                        color = TatTelDarkGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Tatar alphabet button
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, TatTelCardBorder, RoundedCornerShape(16.dp))
                    .clickable { showAlphabetDialog = true },
                colors = CardDefaults.cardColors(containerColor = TatTelSurface)
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
                            .clip(RoundedCornerShape(12.dp))
                            .background(TatTelLightGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Translate, contentDescription = null, tint = TatTelDarkGreen)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Особые буквы татарского языка",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TatTelMainText
                        )
                        Text(
                            text = "Ә, Ө, Ү, Җ, Ң, Һ и их произношение",
                            style = MaterialTheme.typography.bodySmall,
                            color = TatTelTextSecondary
                        )
                    }
                    Text(
                        text = "Открыть",
                        style = MaterialTheme.typography.labelSmall,
                        color = TatTelDarkGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // About TatTel info
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = TatTelLightGreen)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "О приложении TatTel",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TatTelDarkGreen
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "TatTel — минималистичный нативный инструмент для освоения разговорного татарского языка. Версия 1.0 (прототип). Работает полностью автономно на вашем устройстве без необходимости входа в аккаунт.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TatTelMainText,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }

    // Modal Dialog: Dictionary of 5 words
    if (showDictionaryDialog) {
        val words = VocabularyRepository().greetingsWords
        AlertDialog(
            onDismissRequest = { showDictionaryDialog = false },
            title = {
                Text(
                    text = "Словарь: Приветствия",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TatTelDarkGreen
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    words.forEach { word ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TatTelLightBg,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = word.tatarWord,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TatTelDarkGreen
                                    )
                                    Text(
                                        text = word.russianTranslation,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TatTelMainText
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${word.transcription} • ${word.usageHint}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TatTelTextMuted
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDictionaryDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = TatTelDarkGreen)
                ) {
                    Text("Закрыть")
                }
            },
            containerColor = TatTelSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Modal Dialog: Tatar letters guide
    if (showAlphabetDialog) {
        val letters = listOf(
            Triple("Ә ә", "Мягкое «а», среднее между «а» и «э»", "Әни (мама), Әйе (да)"),
            Triple("Ө ө", "Мягкое огубленное «о» (как немецкое ö)", "Өй (дом), Өчпочмак"),
            Triple("Ү ү", "Мягкое глухое «у» (как немецкое ü)", "Үрдәк (утка), Үз (сам)"),
            Triple("Җ җ", "Мягкое звонкое «дж»", "Җәй (лето), Җыр (песня)"),
            Triple("Ң ң", "Носовое заднеязычное «н» (как английское ng)", "Яңа (новый), Соң (поздно)"),
            Triple("Һ һ", "Мягкий придыхательный звук «х» (как английское h)", "Һәм (и), Һава (воздух)")
        )

        AlertDialog(
            onDismissRequest = { showAlphabetDialog = false },
            title = {
                Text(
                    text = "Особые буквы татарского алфавита",
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
                    letters.forEach { (letter, sound, example) ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = TatTelLightBg,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(TatTelLightGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = letter.split(" ")[0],
                                        fontWeight = FontWeight.Bold,
                                        color = TatTelDarkGreen,
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = letter,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = TatTelDarkGreen
                                    )
                                    Text(
                                        text = sound,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TatTelMainText
                                    )
                                    Text(
                                        text = "Пример: $example",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TatTelTextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showAlphabetDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = TatTelDarkGreen)
                ) {
                    Text("Понятно")
                }
            },
            containerColor = TatTelSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }
}
