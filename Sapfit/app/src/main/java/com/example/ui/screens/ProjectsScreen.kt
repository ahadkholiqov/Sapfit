package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppState
import com.example.data.AppViewModel
import com.example.data.ProjectsSubTab
import com.example.data.SortBy
import com.example.i18n.Str
import com.example.model.ExplainerBanner
import com.example.model.ExplainerSection
import com.example.model.HistoryItem
import com.example.model.Project
import com.example.ui.components.ExplainerBannerView
import com.example.ui.components.ProgressBar3Color
import com.example.ui.theme.*

@Composable
fun ProjectsScreen(
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
        if (ExplainerSection.PROJECTS !in state.dismissedExplainers) {
            ExplainerBannerView(
                banner = ExplainerBanner(
                    section = ExplainerSection.PROJECTS,
                    title = Str.explainerProjectsTitle(lang),
                    text = Str.explainerProjectsText(lang)
                ),
                onDismiss = { viewModel.dismissExplainer(it) }
            )
        }

        // Header Row with Sort Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Str.tabProjects(lang),
                color = AppTheme.colors.textPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Box {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(AppTheme.colors.cardBackground)
                        .border(0.5.dp, AppTheme.colors.borderDefault, CircleShape)
                        .clickable { viewModel.toggleSortDropdown() }
                        .testTag("btn_sort_projects"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Tune,
                        contentDescription = Str.sortByLabel(lang),
                        tint = AppTheme.colors.accentText,
                        modifier = Modifier.size(16.dp)
                    )
                }

                DropdownMenu(
                    expanded = state.isSortDropdownOpen,
                    onDismissRequest = { viewModel.toggleSortDropdown() },
                    modifier = Modifier.background(AppTheme.colors.surfaceContainer)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                Str.sortByDeadline(lang),
                                color = if (state.sortBy == SortBy.DEADLINE) AppTheme.colors.accentText else AppTheme.colors.textTertiary,
                                fontSize = 12.sp
                            )
                        },
                        onClick = { viewModel.setSortBy(SortBy.DEADLINE) }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                Str.sortByMaterials(lang),
                                color = if (state.sortBy == SortBy.MATERIALS) AppTheme.colors.accentText else AppTheme.colors.textTertiary,
                                fontSize = 12.sp
                            )
                        },
                        onClick = { viewModel.setSortBy(SortBy.MATERIALS) }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                Str.sortByProgress(lang),
                                color = if (state.sortBy == SortBy.PROGRESS) AppTheme.colors.accentText else AppTheme.colors.textTertiary,
                                fontSize = 12.sp
                            )
                        },
                        onClick = { viewModel.setSortBy(SortBy.PROGRESS) }
                    )
                }
            }
        }

        // SubTab Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(AppTheme.colors.insetControlBackground)
                .border(width = 0.5.dp, color = AppTheme.colors.borderDefault, shape = RoundedCornerShape(24.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SubTabPill(
                title = Str.subtabActive(lang),
                icon = Icons.Filled.Folder,
                isSelected = state.projectsSubTab == ProjectsSubTab.ACTIVE,
                onClick = { viewModel.selectProjectsSubTab(ProjectsSubTab.ACTIVE) },
                testTag = "tab_projects_active",
                modifier = Modifier.weight(1f)
            )
            SubTabPill(
                title = Str.subtabHistory(lang),
                icon = Icons.Filled.History,
                isSelected = state.projectsSubTab == ProjectsSubTab.HISTORY,
                onClick = { viewModel.selectProjectsSubTab(ProjectsSubTab.HISTORY) },
                testTag = "tab_projects_history",
                modifier = Modifier.weight(1f)
            )
        }

        // SubTab Content with transition
        AnimatedContent(
            targetState = state.projectsSubTab,
            transitionSpec = {
                fadeIn(animationSpec = androidx.compose.animation.core.tween(220)) togetherWith fadeOut(animationSpec = androidx.compose.animation.core.tween(180))
            },
            label = "projects_subtab_transition"
        ) { subTab ->
            if (subTab == ProjectsSubTab.ACTIVE) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val sortedProjects = state.projects.sortedWith(
                        compareByDescending<Project> { it.pinned }.then(
                            when (state.sortBy) {
                                SortBy.DEADLINE -> compareBy { it.deadlineDays ?: 999 }
                                SortBy.MATERIALS -> compareByDescending { it.materialsCount }
                                SortBy.PROGRESS -> compareByDescending { it.progress.locked + it.progress.learning }
                            }
                        )
                    )

                    if (sortedProjects.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (lang == com.example.data.AppLanguage.EN) "No active projects.\nCreate one on Home screen!" else "Нет активных проектов.\nСоздай новый на Главной!",
                                color = AppTheme.colors.textMuted,
                                fontSize = 13.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    } else {
                        sortedProjects.forEach { project ->
                            ProjectCardItem(
                                project = project,
                                lang = lang,
                                onClick = { viewModel.openProjectDetail(project.id) }
                            )
                        }
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (state.historyItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (lang == com.example.data.AppLanguage.EN) "History is empty" else "История пока пуста",
                                color = AppTheme.colors.textMuted,
                                fontSize = 13.sp
                            )
                        }
                    } else {
                        state.historyItems.forEach { history ->
                            HistoryCardItem(
                                item = history,
                                lang = lang,
                                onOpen = { viewModel.openSession(history.title) },
                                onConvert = { viewModel.convertHistoryToProject(history.id) },
                                onDelete = { viewModel.deleteHistoryItem(history.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SubTabPill(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) AppTheme.colors.activeSegmentBackground else Color.Transparent)
            .border(
                width = if (isSelected) 0.5.dp else 0.dp,
                color = if (isSelected) AppTheme.colors.activeSegmentBorder else Color.Transparent,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) AppTheme.colors.accentText else AppTheme.colors.textSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                color = if (isSelected) AppTheme.colors.accentText else AppTheme.colors.textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ProjectCardItem(
    project: Project,
    lang: com.example.data.AppLanguage,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(AppTheme.colors.cardBackground)
            .border(
                width = if (project.pinned) 1.dp else 0.5.dp,
                color = if (project.pinned) AppTheme.colors.activeSegmentBorder else AppTheme.colors.borderDefault,
                shape = RoundedCornerShape(22.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
            .testTag("project_card_${project.id}")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Title & Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    if (project.pinned) {
                        Icon(
                            imageVector = Icons.Filled.PushPin,
                            contentDescription = "Pinned",
                            tint = AppTheme.colors.accentText,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = project.title,
                        color = AppTheme.colors.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if (project.paused) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(AppTheme.colors.appBackground)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(if (lang == com.example.data.AppLanguage.EN) "paused" else "на паузе", color = AppTheme.colors.textMuted, fontSize = 9.sp)
                        }
                    }
                }

                Text(
                    text = "${project.materialsCount} ${if (lang == com.example.data.AppLanguage.EN) "materials" else "материалов"}",
                    color = AppTheme.colors.textSecondary,
                    fontSize = 11.sp
                )
            }

            // Tags
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppTheme.colors.surfaceContainer)
                        .padding(horizontal = 9.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = project.subject,
                        color = AppTheme.colors.accentSoftIcon,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (project.deadlineDays != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(AppTheme.colors.surfaceContainer)
                            .padding(horizontal = 9.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (lang == com.example.data.AppLanguage.EN) "Deadline: ${project.deadlineDays}d" else "Дедлайн: ${project.deadlineDays} дней",
                            color = AppTheme.colors.accentSoftIcon,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Progress bar
            ProgressBar3Color(progress = project.progress, barHeight = 6)
        }
    }
}

@Composable
private fun HistoryCardItem(
    item: HistoryItem,
    lang: com.example.data.AppLanguage,
    onOpen: () -> Unit,
    onConvert: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(22.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    color = AppTheme.colors.textPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = item.date,
                    color = AppTheme.colors.textMuted,
                    fontSize = 10.sp
                )
            }

            Text(
                text = item.preview,
                color = AppTheme.colors.textSecondary,
                fontSize = 11.sp
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = if (lang == com.example.data.AppLanguage.EN) "Open" else "Открыть",
                    color = AppTheme.colors.accentText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable(onClick = onOpen)
                )
                Text(
                    text = Str.convertToProject(lang),
                    color = AppTheme.colors.accentText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable(onClick = onConvert)
                )
                Text(
                    text = Str.delete(lang),
                    color = AppTheme.colors.dangerRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable(onClick = onDelete)
                )
            }
        }
    }
}
