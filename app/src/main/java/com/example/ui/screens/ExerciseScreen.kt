package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ExerciseState
import com.example.ui.theme.*

@Composable
fun ExerciseScreen(
    state: ExerciseState,
    onSelectOption: (String) -> Unit,
    onNextQuestion: () -> Unit,
    onQuit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showQuitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showQuitDialog = true
    }

    val currentQuestion = state.currentQuestion
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = TatTelLightBg,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { showQuitDialog = true },
                        modifier = Modifier.testTag("exercise_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть урок",
                            tint = TatTelMainText
                        )
                    }

                    Text(
                        text = "Вопрос ${state.currentQuestionIndex + 1} из ${state.questions.size}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TatTelDarkGreen
                    )

                    // Error counter badge
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (state.errorCount > 0) TatTelErrorBg else TatTelLightGreen
                    ) {
                        Text(
                            text = "Ошибок: ${state.errorCount}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (state.errorCount > 0) TatTelError else TatTelDarkGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val animatedProgress = if (state.questions.isEmpty()) 0f
                else ((state.currentQuestionIndex + 1).toFloat() / state.questions.size.toFloat())

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = TatTelDarkGreen,
                    trackColor = TatTelLightGreen
                )
            }
        },
        bottomBar = {
            // Feedback banner when answered
            AnimatedVisibility(
                visible = state.isAnswered,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it })
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    color = if (state.isCorrect) TatTelSuccessBg else TatTelErrorBg,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (state.isCorrect) TatTelSuccess else TatTelError),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (state.isCorrect) Icons.Default.Check else Icons.Default.Close,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (state.isCorrect) "Дөрес! (Верно!)" else "Хата (Неверно)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (state.isCorrect) TatTelSuccess else TatTelError
                                )
                                if (!state.isCorrect && currentQuestion != null) {
                                    Text(
                                        text = "Правильный ответ: ${currentQuestion.correctAnswer}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TatTelMainText
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        val isLastQuestion = state.currentQuestionIndex == state.questions.size - 1
                        Button(
                            onClick = onNextQuestion,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("exercise_next_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.isCorrect) TatTelDarkGreen else TatTelError,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = if (isLastQuestion) "Завершить урок" else "Далее",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (currentQuestion != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Question Prompt
                Text(
                    text = "Выберите правильный перевод",
                    style = MaterialTheme.typography.labelLarge,
                    color = TatTelTextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Prominent Tatar Word Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .border(1.dp, TatTelCardBorder, RoundedCornerShape(24.dp)),
                    colors = CardDefaults.cardColors(containerColor = TatTelSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp, horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TatTelLightGreen
                        ) {
                            Text(
                                text = "Татарча",
                                style = MaterialTheme.typography.labelSmall,
                                color = TatTelDarkGreen,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Word text
                        Text(
                            text = currentQuestion.word.tatarWord,
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold,
                            color = TatTelDarkGreen,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Transcription
                        Text(
                            text = currentQuestion.word.transcription,
                            style = MaterialTheme.typography.titleMedium,
                            color = TatTelTextMuted,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Context hint
                        Text(
                            text = currentQuestion.word.usageHint,
                            style = MaterialTheme.typography.bodySmall,
                            color = TatTelTextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 4 Translation Options
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    currentQuestion.options.forEachIndexed { index, option ->
                        val isSelected = state.selectedOption == option
                        val isThisCorrect = option == currentQuestion.correctAnswer

                        // Colors depending on answered state
                        val (bgColor, borderColor, textColor) = when {
                            !state.isAnswered -> {
                                if (isSelected) {
                                    Triple(TatTelLightGreen, TatTelDarkGreen, TatTelDarkGreen)
                                } else {
                                    Triple(TatTelSurface, TatTelCardBorder, TatTelMainText)
                                }
                            }
                            isThisCorrect -> {
                                Triple(TatTelSuccessBg, TatTelSuccess, TatTelSuccess)
                            }
                            isSelected && !isThisCorrect -> {
                                Triple(TatTelErrorBg, TatTelError, TatTelError)
                            }
                            else -> {
                                Triple(TatTelSurface.copy(alpha = 0.5f), TatTelCardBorder.copy(alpha = 0.5f), TatTelTextMuted)
                            }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(58.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = if (isSelected || (state.isAnswered && isThisCorrect)) 2.dp else 1.dp,
                                    color = borderColor,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable(enabled = !state.isAnswered) {
                                    onSelectOption(option)
                                }
                                .testTag("option_button_$index"),
                            colors = CardDefaults.cardColors(containerColor = bgColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected || (state.isAnswered && isThisCorrect)) borderColor.copy(alpha = 0.2f)
                                                else TatTelLightBg
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${index + 1}",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = textColor
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = textColor
                                    )
                                }

                                if (state.isAnswered) {
                                    if (isThisCorrect) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Правильно",
                                            tint = TatTelSuccess,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    } else if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Неверно",
                                            tint = TatTelError,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

    // Confirmation dialog before exiting exercise midway
    if (showQuitDialog) {
        AlertDialog(
            onDismissRequest = { showQuitDialog = false },
            title = {
                Text(
                    text = "Завершить урок?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TatTelDarkGreen
                )
            },
            text = {
                Text(
                    text = "Текущий прогресс в этом упражнении будет сброшен. Вы хотите вернуться на главный экран?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TatTelMainText
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showQuitDialog = false
                        onQuit()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TatTelDarkGreen)
                ) {
                    Text("Выйти")
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuitDialog = false }) {
                    Text("Продолжить", color = TatTelTextSecondary)
                }
            },
            containerColor = TatTelSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }
}
