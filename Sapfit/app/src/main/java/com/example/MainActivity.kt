package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppViewModel
import com.example.data.MainTab
import com.example.data.ProjectCreateStage
import com.example.ui.components.CapsuleNavigationBar
import com.example.ui.screens.*
import com.example.ui.theme.AppTheme
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appViewModel: AppViewModel = viewModel()
            val state by appViewModel.uiState.collectAsState()

            MyApplicationTheme(darkTheme = state.isDarkTheme) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.appBackground)
                ) {
                    if (!state.wave1Completed) {
                        OnboardingScreen(state = state, viewModel = appViewModel)
                        return@Box
                    }
                    if (state.projectCreateStage != ProjectCreateStage.IDLE) {
                        ProjectCreateScreen(state = state, viewModel = appViewModel)
                        return@Box
                    }
                    if (state.wave2Started) {
                        Wave2Screen(state = state, viewModel = appViewModel)
                        return@Box
                    }
                    if (state.profileSubscreen != null) {
                        ProfileSubscreenHost(state = state, viewModel = appViewModel)
                        return@Box
                    }
                    AnimatedContent(
                        targetState = state.isSessionOpen,
                        transitionSpec = {
                            if (targetState) {
                                (slideInVertically { height -> height } + fadeIn()) togetherWith (slideOutVertically { height -> -height / 3 } + fadeOut())
                            } else {
                                (slideInVertically { height -> -height / 3 } + fadeIn()) togetherWith (slideOutVertically { height -> height } + fadeOut())
                            }
                        },
                        label = "session_modal_transition"
                    ) { isSession ->
                        if (isSession) {
                            // Working Session Modal Screen (No Bottom Navigation Bar)
                            SessionScreen(
                                state = state,
                                viewModel = appViewModel
                            )
                        } else {
                            // Main Scaffold Container with Bottom Navigation Bar
                            Box(modifier = Modifier.fillMaxSize()) {
                                // Active Screen Content
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .statusBarsPadding()
                                ) {
                                    val currentScreenKey = when {
                                        state.isRegistryOpen -> "registry"
                                        state.selectedProjectId != null -> "project_detail_${state.selectedProjectId}"
                                        else -> "tab_${state.currentTab.name}"
                                    }

                                    AnimatedContent(
                                        targetState = currentScreenKey,
                                        transitionSpec = {
                                            fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
                                        },
                                        label = "screen_content_transition"
                                    ) { screenKey ->
                                        when {
                                            state.isRegistryOpen -> {
                                                RegistryScreen(
                                                    state = state,
                                                    viewModel = appViewModel
                                                )
                                            }
                                            state.selectedProjectId != null -> {
                                                ProjectDetailScreen(
                                                    state = state,
                                                    viewModel = appViewModel
                                                )
                                            }
                                            else -> {
                                                when (state.currentTab) {
                                                    MainTab.HOME -> HomeScreen(state = state, viewModel = appViewModel)
                                                    MainTab.PROJECTS -> ProjectsScreen(state = state, viewModel = appViewModel)
                                                    MainTab.ABOUT -> AboutScreen(state = state, viewModel = appViewModel)
                                                    MainTab.PROFILE -> ProfileScreen(state = state, viewModel = appViewModel)
                                                }
                                            }
                                        }
                                    }
                                }

                                // Bottom Capsule Navigation Bar
                                CapsuleNavigationBar(
                                    currentTab = state.currentTab,
                                    onTabSelected = { appViewModel.selectTab(it) },
                                    appLanguage = state.appLanguage,
                                    modifier = Modifier.align(Alignment.BottomCenter)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
