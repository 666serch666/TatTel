package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Lesson
import com.example.ui.theme.*

@Composable
fun TatTelTopBar(
    title: String = "TatTel",
    subtitle: String? = "Изучение татарского языка",
    modifier: Modifier = Modifier,
    actionIcon: ImageVector? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(TatTelDarkGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Т",
                        color = TatTelLightBg,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = TatTelDarkGreen,
                    fontWeight = FontWeight.Bold
                )
            }
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TatTelTextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        if (actionIcon != null && onActionClick != null) {
            IconButton(
                onClick = onActionClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(TatTelLightGreen)
            ) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = "Действие",
                    tint = TatTelDarkGreen
                )
            }
        }
    }
}

@Composable
fun LessonCard(
    lesson: Lesson,
    isCompleted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBackground = if (lesson.isAvailable) TatTelSurface else Color(0xFFF0EFE9)
    val borderColor = if (lesson.isAvailable) TatTelLightGreen else Color(0xFFE2E1D9)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag("lesson_card_${lesson.id}"),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = if (lesson.isAvailable) 2.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status Icon Container
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        when {
                            isCompleted -> TatTelSuccessBg
                            lesson.isAvailable -> TatTelLightGreen
                            else -> Color(0xFFE6E5DC)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        isCompleted -> Icons.Default.CheckCircle
                        lesson.isAvailable -> Icons.Default.PlayArrow
                        else -> Icons.Default.Lock
                    },
                    contentDescription = if (lesson.isAvailable) "Доступен" else "Заблокирован",
                    tint = when {
                        isCompleted -> TatTelSuccess
                        lesson.isAvailable -> TatTelDarkGreen
                        else -> TatTelTextMuted
                    },
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = lesson.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (lesson.isAvailable) TatTelMainText else TatTelTextMuted
                    )

                    // Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when {
                            isCompleted -> TatTelSuccessBg
                            lesson.isAvailable -> TatTelLightGreen
                            else -> Color(0xFFE6E5DC)
                        }
                    ) {
                        Text(
                            text = when {
                                isCompleted -> "Пройден"
                                lesson.isAvailable -> "Доступен"
                                else -> "Скоро"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = when {
                                isCompleted -> TatTelSuccess
                                lesson.isAvailable -> TatTelDarkGreen
                                else -> TatTelTextMuted
                            },
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = lesson.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (lesson.isAvailable) TatTelTextSecondary else TatTelTextMuted
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${lesson.wordCount} слов",
                        style = MaterialTheme.typography.labelSmall,
                        color = TatTelTextMuted
                    )
                    Text(
                        text = " • ",
                        style = MaterialTheme.typography.labelSmall,
                        color = TatTelTextMuted
                    )
                    Text(
                        text = "~${lesson.estimatedMinutes} мин",
                        style = MaterialTheme.typography.labelSmall,
                        color = TatTelTextMuted
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, TatTelCardBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = TatTelSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = TatTelTextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = TatTelDarkGreen,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TatTelTextMuted
            )
        }
    }
}
