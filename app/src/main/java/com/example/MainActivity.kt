package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppScreen
import com.example.ui.screens.*
import com.example.ui.theme.TatTelDarkGreen
import com.example.ui.theme.TatTelLightBg
import com.example.ui.theme.TatTelLightGreen
import com.example.ui.theme.TatTelTheme
import com.example.ui.viewmodel.TatTelViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TatTelTheme {
                val viewModel: TatTelViewModel = viewModel()
                TatTelApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TatTelApp(viewModel: TatTelViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val userProgress by viewModel.userProgress.collectAsStateWithLifecycle()
    val lessons by viewModel.lessons.collectAsStateWithLifecycle()
    val exerciseState by viewModel.exerciseState.collectAsStateWithLifecycle()
    val noticeMessage by viewModel.notificationMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(noticeMessage) {
        noticeMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.dismissNotice()
        }
    }

    // Intercept back navigation when in sub-screens
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.handleBackPress()
    }

    val showBottomNav = currentScreen in listOf(AppScreen.HOME, AppScreen.LESSONS, AppScreen.PROFILE)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = TatTelLightBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomNav) {
                NavigationBar(
                    containerColor = TatTelLightBg,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.HOME,
                        onClick = { viewModel.navigateTo(AppScreen.HOME) },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen == AppScreen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Главная"
                            )
                        },
                        label = { Text("Главная") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TatTelDarkGreen,
                            selectedTextColor = TatTelDarkGreen,
                            indicatorColor = TatTelLightGreen
                        ),
                        modifier = Modifier.testTag("nav_tab_home")
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.LESSONS,
                        onClick = { viewModel.navigateTo(AppScreen.LESSONS) },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen == AppScreen.LESSONS) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                                contentDescription = "Уроки"
                            )
                        },
                        label = { Text("Уроки") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TatTelDarkGreen,
                            selectedTextColor = TatTelDarkGreen,
                            indicatorColor = TatTelLightGreen
                        ),
                        modifier = Modifier.testTag("nav_tab_lessons")
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.PROFILE,
                        onClick = { viewModel.navigateTo(AppScreen.PROFILE) },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen == AppScreen.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                                contentDescription = "Профиль"
                            )
                        },
                        label = { Text("Профиль") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TatTelDarkGreen,
                            selectedTextColor = TatTelDarkGreen,
                            indicatorColor = TatTelLightGreen
                        ),
                        modifier = Modifier.testTag("nav_tab_profile")
                    )
                }
            }
        }
    ) { innerPadding ->
        when (currentScreen) {
            AppScreen.HOME -> {
                HomeScreen(
                    progress = userProgress,
                    lessons = lessons,
                    onStartLesson = { lessonId -> viewModel.startLesson(lessonId) },
                    onNavigateToLessons = { viewModel.navigateTo(AppScreen.LESSONS) },
                    onNavigateToProfile = { viewModel.navigateTo(AppScreen.PROFILE) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            AppScreen.LESSONS -> {
                LessonsScreen(
                    lessons = lessons,
                    progress = userProgress,
                    onStartLesson = { lessonId -> viewModel.startLesson(lessonId) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            AppScreen.PROFILE -> {
                ProfileScreen(
                    progress = userProgress,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            AppScreen.EXERCISE -> {
                ExerciseScreen(
                    state = exerciseState,
                    onSelectOption = { option -> viewModel.selectOption(option) },
                    onNextQuestion = { viewModel.nextQuestion() },
                    onQuit = { viewModel.returnToHome() }
                )
            }
            AppScreen.COMPLETION -> {
                CompletionScreen(
                    state = exerciseState,
                    onRepeatLesson = { viewModel.repeatLesson() },
                    onReturnHome = { viewModel.returnToHome() }
                )
            }
        }
    }
}
