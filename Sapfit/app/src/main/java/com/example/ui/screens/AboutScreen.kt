package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AboutSubTab
import com.example.data.AppState
import com.example.data.AppViewModel
import com.example.i18n.Str
import com.example.model.ExplainerBanner
import com.example.model.ExplainerSection
import com.example.ui.components.ExplainerBannerView
import com.example.ui.theme.*

@Composable
fun AboutScreen(
    state: AppState,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val lang = state.appLanguage

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (ExplainerSection.ABOUT !in state.dismissedExplainers) {
            ExplainerBannerView(
                banner = ExplainerBanner(
                    section = ExplainerSection.ABOUT,
                    title = Str.explainerAboutTitle(lang),
                    text = Str.explainerAboutText(lang)
                ),
                onDismiss = { viewModel.dismissExplainer(it) }
            )
        }

        // Screen Title
        Text(
            text = Str.tabAbout(lang),
            color = AppTheme.colors.textPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        // 3 Icon Tabs (Stats / Methods / Observations)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(AppTheme.colors.insetControlBackground)
                .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(20.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconTabButton(
                icon = Icons.Filled.BarChart,
                contentDescription = Str.statsTab(lang),
                isSelected = state.aboutSubTab == AboutSubTab.STATS,
                onClick = { viewModel.selectAboutSubTab(AboutSubTab.STATS) },
                testTag = "tab_about_stats",
                modifier = Modifier.weight(1f)
            )
            IconTabButton(
                icon = Icons.Filled.TrackChanges,
                contentDescription = Str.methodsTab(lang),
                isSelected = state.aboutSubTab == AboutSubTab.METHODS,
                onClick = { viewModel.selectAboutSubTab(AboutSubTab.METHODS) },
                testTag = "tab_about_methods",
                modifier = Modifier.weight(1f)
            )
            IconTabButton(
                icon = Icons.Filled.Visibility,
                contentDescription = Str.obsTab(lang),
                isSelected = state.aboutSubTab == AboutSubTab.OBSERVATIONS,
                onClick = { viewModel.selectAboutSubTab(AboutSubTab.OBSERVATIONS) },
                testTag = "tab_about_observations",
                modifier = Modifier.weight(1f)
            )
        }

        // SubTab Content with transition
        AnimatedContent(
            targetState = state.aboutSubTab,
            transitionSpec = {
                fadeIn(animationSpec = androidx.compose.animation.core.tween(220)) togetherWith fadeOut(animationSpec = androidx.compose.animation.core.tween(180))
            },
            label = "about_subtab_transition"
        ) { subTab ->
            when (subTab) {
                AboutSubTab.STATS -> StatsSubTabContent(state = state)
                AboutSubTab.METHODS -> MethodsSubTabContent(state = state, viewModel = viewModel)
                AboutSubTab.OBSERVATIONS -> ObservationsSubTabContent(state = state)
            }
        }
    }
}

@Composable
private fun IconTabButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag(testTag)
            .height(40.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) AppTheme.colors.surfaceContainer else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isSelected) AppTheme.colors.accentText else AppTheme.colors.textSecondary,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun StatsSubTabContent(state: AppState) {
    val lang = state.appLanguage

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Conversational AI Insight Summary
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(AppTheme.colors.cardBackground)
                .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Text(
                text = if (lang == com.example.data.AppLanguage.EN) "This week you studied more often than usual — especially in the evening. Physics is progressing smoother than algebra." else "На этой неделе ты занимался чаще обычного — особенно вечером. Физика идёт стабильнее алгебры.",
                color = AppTheme.colors.textTertiary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }

        // 2x2 Grid of Key Metrics
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricTile(value = "14h", label = if (lang == com.example.data.AppLanguage.EN) "per week" else "за неделю", modifier = Modifier.weight(1f))
            MetricTile(value = "38", label = if (lang == com.example.data.AppLanguage.EN) "sessions" else "разборов", modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricTile(value = "3", label = if (lang == com.example.data.AppLanguage.EN) "projects" else "проекта", modifier = Modifier.weight(1f))
            MetricTile(value = "86%", label = if (lang == com.example.data.AppLanguage.EN) "completed" else "завершено", modifier = Modifier.weight(1f))
        }

        // Heatmap Matrix
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(AppTheme.colors.cardBackground)
                .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (lang == com.example.data.AppLanguage.EN) "Monthly Activity" else "Активность за месяц",
                    color = AppTheme.colors.textMuted,
                    fontSize = 10.sp
                )

                // 10x3 heatmap grid
                val heatPattern = listOf(
                    0, 1, 2, 0, 1, 3, 2, 1, 0, 1,
                    2, 3, 2, 1, 0, 1, 2, 3, 2, 1,
                    0, 1, 2, 1, 0, 1, 2, 3, 3, 2
                )

                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    for (row in 0 until 3) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            for (col in 0 until 10) {
                                val intensity = heatPattern.getOrElse(row * 10 + col) { 0 }
                                val cellColor = when (intensity) {
                                    1 -> Color(0x336366F1)
                                    2 -> Color(0x776366F1)
                                    3 -> AppTheme.colors.accentPrimary
                                    else -> AppTheme.colors.appBackground
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(cellColor)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricTile(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = value,
                color = AppTheme.colors.textPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                color = AppTheme.colors.textSecondary,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun MethodsSubTabContent(
    state: AppState,
    viewModel: AppViewModel
) {
    val lang = state.appLanguage

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(AppTheme.colors.cardBackground)
                .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(18.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = if (lang == com.example.data.AppLanguage.EN) "Current Priorities" else "Текущие приоритеты",
                    color = AppTheme.colors.textMuted,
                    fontSize = 10.sp
                )

                state.methodPriorities.forEach { method ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = method.name,
                                color = AppTheme.colors.textPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${method.percentage}%",
                                color = AppTheme.colors.accentText,
                                fontSize = 11.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(AppTheme.colors.appBackground)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(method.percentage / 100f)
                                    .background(AppTheme.colors.accentPrimary)
                            )
                        }
                    }
                }
            }
        }

        // Button to open Techniques Registry
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(AppTheme.colors.surfaceContainer)
                .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(18.dp))
                .clickable { viewModel.openRegistry() }
                .padding(14.dp)
                .testTag("btn_open_registry"),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.GridView,
                    contentDescription = Str.registryBtn(lang),
                    tint = AppTheme.colors.accentText,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Str.registryBtn(lang),
                    color = AppTheme.colors.accentText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ObservationsSubTabContent(state: AppState) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        state.observations.forEach { obs ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(AppTheme.colors.cardBackground)
                    .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = obs.date,
                        color = AppTheme.colors.textMuted,
                        fontSize = 10.sp
                    )
                    Text(
                        text = obs.text,
                        color = AppTheme.colors.textTertiary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}
