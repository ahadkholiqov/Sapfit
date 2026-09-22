package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppState
import com.example.data.AppViewModel
import com.example.i18n.Str
import com.example.ui.components.ProgressBar3Color
import com.example.ui.theme.*

@Composable
fun ProjectDetailScreen(
    state: AppState,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val lang = state.appLanguage
    val project = state.projects.find { it.id == state.selectedProjectId }
        ?: state.projects.firstOrNull()
        ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Navigation Bar with Back & More Options Menu
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.cardBackground)
                    .border(0.5.dp, AppTheme.colors.borderDefault, CircleShape)
                    .clickable { viewModel.closeProjectDetail() }
                    .testTag("btn_back_project_detail"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = Str.back(lang),
                    tint = AppTheme.colors.accentText,
                    modifier = Modifier.size(18.dp)
                )
            }

            Box {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(AppTheme.colors.cardBackground)
                        .border(0.5.dp, AppTheme.colors.borderDefault, CircleShape)
                        .clickable { viewModel.toggleProjectMenu() }
                        .testTag("btn_menu_project_detail"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreHoriz,
                        contentDescription = "Options",
                        tint = AppTheme.colors.accentText,
                        modifier = Modifier.size(18.dp)
                    )
                }

                DropdownMenu(
                    expanded = state.isProjectMenuOpen,
                    onDismissRequest = { viewModel.toggleProjectMenu() },
                    modifier = Modifier.background(AppTheme.colors.surfaceContainer)
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.CalendarToday, contentDescription = null, tint = AppTheme.colors.accentSoftIcon, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(if (lang == com.example.data.AppLanguage.EN) "Change deadline" else "Изменить дедлайн", color = AppTheme.colors.textTertiary, fontSize = 12.sp)
                            }
                        },
                        onClick = { viewModel.toggleProjectMenu() }
                    )
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Pause, contentDescription = null, tint = AppTheme.colors.accentSoftIcon, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(if (project.paused) (if (lang == com.example.data.AppLanguage.EN) "Resume" else "Возобновить") else (if (lang == com.example.data.AppLanguage.EN) "Pause" else "Приостановить"), color = AppTheme.colors.textTertiary, fontSize = 12.sp)
                            }
                        },
                        onClick = { viewModel.togglePauseProject(project.id) }
                    )
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.PushPin, contentDescription = null, tint = AppTheme.colors.accentSoftIcon, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(if (project.pinned) (if (lang == com.example.data.AppLanguage.EN) "Unpin priority" else "Снять приоритет") else (if (lang == com.example.data.AppLanguage.EN) "Set priority" else "Поставить приоритет"), color = AppTheme.colors.textTertiary, fontSize = 12.sp)
                            }
                        },
                        onClick = { viewModel.togglePinProject(project.id) }
                    )
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Delete, contentDescription = null, tint = AppTheme.colors.dangerRed, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(if (lang == com.example.data.AppLanguage.EN) "Delete project" else "Удалить проект", color = AppTheme.colors.dangerRed, fontSize = 12.sp)
                            }
                        },
                        onClick = { viewModel.deleteProject(project.id) }
                    )
                }
            }
        }

        // Title and Meta Description
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = project.title,
                color = AppTheme.colors.textPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = project.meta,
                color = AppTheme.colors.textSecondary,
                fontSize = 11.sp
            )
        }

        // Large 3-Color Progress Bar
        ProgressBar3Color(progress = project.progress, barHeight = 10)

        // Materials List Section
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "${if (lang == com.example.data.AppLanguage.EN) "MATERIALS" else "МАТЕРИАЛЫ"} (${project.materialsCount})",
                color = AppTheme.colors.textFaint,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )

            project.materialsList.forEach { mat ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(AppTheme.colors.cardBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (mat.iconType) {
                                "photo" -> Icons.Filled.Photo
                                "link" -> Icons.Filled.Link
                                else -> Icons.Filled.Description
                            },
                            contentDescription = null,
                            tint = AppTheme.colors.accentSoftIcon,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Text(
                        text = mat.name,
                        color = AppTheme.colors.textPrimary,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable {
                        viewModel.addMaterialToProject(
                            project.id,
                            if (lang == com.example.data.AppLanguage.EN)
                                "New material (${project.materialsCount + 1})"
                            else
                                "Новый материал (${project.materialsCount + 1})"
                        )
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = AppTheme.colors.textSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = if (lang == com.example.data.AppLanguage.EN) "Add material" else "Добавить материал",
                    color = AppTheme.colors.textSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Session schedule — short list (spec §9)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = Str.pdSchedule(lang).uppercase(),
                color = AppTheme.colors.textFaint,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.6.sp
            )
            val schedule = listOf(
                "1" to project.nextSession,
                "2" to (if (lang == com.example.data.AppLanguage.EN) "in 2 days, 18:00" else "через 2 дня, 18:00"),
                "3" to (if (lang == com.example.data.AppLanguage.EN) "in 4 days, 18:00" else "через 4 дня, 18:00")
            )
            schedule.forEach { (num, when_) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppTheme.colors.cardBackground)
                        .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "#$num", color = AppTheme.colors.textSecondary, fontSize = 12.sp)
                    Text(text = when_, color = AppTheme.colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        // Format of work for this project (spec §9)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(AppTheme.colors.cardBackground)
                .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = Str.pdFormatTitle(lang).uppercase(),
                color = AppTheme.colors.accentText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.6.sp
            )
            Spacer(Modifier.height(4.dp))
            FormatRow(label = Str.pdFormatLength(lang), value = "30 min")
            FormatRow(label = Str.pdFormatTime(lang), value = Str.pdTimeEvening(lang))
            FormatRow(label = Str.pdFormatInterleave(lang), value = Str.pdIntStandard(lang))
        }

        // Schedule Card Block
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(AppTheme.colors.cardBackground)
                .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (lang == com.example.data.AppLanguage.EN) "BEFORE DEADLINE" else "ДО ДЕДЛАЙНА",
                    color = AppTheme.colors.textFaint,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (lang == com.example.data.AppLanguage.EN) "Sessions left" else "Осталось сессий", color = AppTheme.colors.textTertiary, fontSize = 11.sp)
                    Text("${project.sessionsLeft}", color = AppTheme.colors.textMuted, fontSize = 11.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (lang == com.example.data.AppLanguage.EN) "Next session" else "Следующая", color = AppTheme.colors.textTertiary, fontSize = 11.sp)
                    Text(project.nextSession, color = AppTheme.colors.textMuted, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f, fill = false))

        // Primary Continue Learning Action Button
        Button(
            onClick = { viewModel.openSession(project.title) },
            colors = ButtonDefaults.buttonColors(
                containerColor = AppTheme.colors.accentPrimary,
                contentColor = AppTheme.colors.onAccent
            ),
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("btn_continue_learning")
        ) {
            Text(if (lang == com.example.data.AppLanguage.EN) "Continue learning" else "Продолжить обучение", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun FormatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = AppTheme.colors.textPrimary, fontSize = 13.sp)
        Text(text = value, color = AppTheme.colors.accentText, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}
