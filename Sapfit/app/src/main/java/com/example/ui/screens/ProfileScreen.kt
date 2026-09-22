package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.AppState
import com.example.data.AppViewModel
import com.example.data.ProfileSubscreen
import com.example.i18n.Str
import com.example.model.ExplainerBanner
import com.example.model.ExplainerSection
import com.example.ui.components.ExplainerBannerView
import com.example.ui.components.SapfitLogo
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (ExplainerSection.PROFILE !in state.dismissedExplainers) {
            ExplainerBannerView(
                banner = ExplainerBanner(
                    section = ExplainerSection.PROFILE,
                    title = Str.explainerProfileTitle(lang),
                    text = Str.explainerProfileText(lang)
                ),
                onDismiss = { viewModel.dismissExplainer(it) }
            )
        }

        // App Brand Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(AppTheme.colors.cardBackground)
                .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SapfitLogo(
                isDark = state.isDarkTheme,
                modifier = Modifier.size(54.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Sapfit",
                    color = AppTheme.colors.textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (state.isDarkTheme) 
                        (if (lang == AppLanguage.EN) "Dark Theme Active (Dark Logo)" else "Активирована тёмная тема (Тёмное лого)")
                    else 
                        (if (lang == AppLanguage.EN) "Light Theme Active (Light Logo)" else "Активирована светлая тема (Светлое лого)"),
                    color = AppTheme.colors.textSecondary,
                    fontSize = 11.sp
                )
            }
        }

        // Section 1: АККАУНТ
        SettingsSection(title = Str.sectionAccount(lang)) {
            SettingsRow(
                icon = Icons.Filled.AccountCircle,
                title = Str.accountTitle(lang),
                subtitle = Str.accountSub(lang),
                showChevron = true,
                onClick = { viewModel.openProfileSubscreen(ProfileSubscreen.ACCOUNT) }
            )
        }

        // Section 2: УЧЁБА
        SettingsSection(title = Str.sectionStudy(lang)) {
            SettingsRow(
                icon = Icons.Filled.AccessTime,
                title = Str.studyScheduleTitle(lang),
                subtitle = Str.studyScheduleSub(lang),
                showChevron = true,
                hasDivider = true,
                onClick = { viewModel.openProfileSubscreen(ProfileSubscreen.LESSONS) }
            )
            SettingsRow(
                icon = Icons.Filled.Shield,
                title = Str.focusModeTitle(lang),
                subtitle = Str.focusModeSub(lang),
                showChevron = true,
                onClick = { viewModel.openProfileSubscreen(ProfileSubscreen.FOCUS) }
            )
        }

        // Section 3: ДАННЫЕ И ВИД
        SettingsSection(title = Str.sectionDataView(lang)) {
            SettingsRow(
                icon = Icons.Filled.Lock,
                title = Str.privacyTitle(lang),
                subtitle = Str.privacySub(lang),
                showChevron = true,
                hasDivider = true,
                onClick = { viewModel.openProfileSubscreen(ProfileSubscreen.PRIVACY) }
            )

            // Language Switcher Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AppTheme.colors.surfaceContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Language,
                            contentDescription = "Language",
                            tint = AppTheme.colors.accentText,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = Str.languageTitle(lang),
                            color = AppTheme.colors.textPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = Str.languageSub(lang),
                            color = AppTheme.colors.textSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                // Interactive Language Switch Capsule
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(AppTheme.colors.surfaceContainer)
                        .padding(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (lang == AppLanguage.RU) AppTheme.colors.accentPrimary else androidx.compose.ui.graphics.Color.Transparent)
                            .clickable { viewModel.setLanguage(AppLanguage.RU) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "RU",
                            color = if (lang == AppLanguage.RU) AppTheme.colors.onAccent else AppTheme.colors.textMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (lang == AppLanguage.EN) AppTheme.colors.accentPrimary else androidx.compose.ui.graphics.Color.Transparent)
                            .clickable { viewModel.setLanguage(AppLanguage.EN) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "EN",
                            color = if (lang == AppLanguage.EN) AppTheme.colors.onAccent else AppTheme.colors.textMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            HorizontalDivider(
                color = AppTheme.colors.surfaceContainer,
                thickness = 0.5.dp,
                modifier = Modifier.padding(start = 58.dp)
            )
            
            // Theme toggle row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AppTheme.colors.surfaceContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Palette,
                            contentDescription = "Тема",
                            tint = AppTheme.colors.accentText,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = Str.appearanceTitle(lang),
                            color = AppTheme.colors.textPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (state.isDarkTheme) Str.darkThemeDesc(lang) else Str.lightThemeDesc(lang),
                            color = AppTheme.colors.textSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                Switch(
                    checked = state.isDarkTheme,
                    onCheckedChange = { viewModel.toggleTheme() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = AppTheme.colors.onAccent,
                        checkedTrackColor = AppTheme.colors.accentPrimary,
                        uncheckedThumbColor = AppTheme.colors.textMuted,
                        uncheckedTrackColor = AppTheme.colors.surfaceContainer
                    ),
                    modifier = Modifier.testTag("switch_theme")
                )
            }
        }

        // Appearance sub-screen entry (spec §12)
        SettingsRow(
            icon = Icons.Filled.Palette,
            title = Str.appearanceTitle(lang),
            subtitle = if (state.isDarkTheme) Str.darkThemeDesc(lang) else Str.lightThemeDesc(lang),
            showChevron = true,
            onClick = { viewModel.openProfileSubscreen(ProfileSubscreen.APPEARANCE) }
        )

        // Section 4: ПОМОЩЬ
        SettingsSection(title = Str.sectionHelp(lang)) {
            SettingsRow(
                icon = Icons.Filled.Help,
                title = Str.helpTitle(lang),
                subtitle = Str.helpSub(lang),
                showChevron = true,
                onClick = { viewModel.openProfileSubscreen(ProfileSubscreen.HELP) }
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            color = AppTheme.colors.textFaint,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(AppTheme.colors.cardBackground)
                .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(20.dp))
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    showChevron: Boolean = false,
    hasDivider: Boolean = false,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AppTheme.colors.surfaceContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = AppTheme.colors.accentText,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column {
                    Text(
                        text = title,
                        color = AppTheme.colors.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        color = AppTheme.colors.textSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            if (showChevron) {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = AppTheme.colors.textFaint,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        if (hasDivider) {
            HorizontalDivider(
                color = AppTheme.colors.surfaceContainer,
                thickness = 0.5.dp,
                modifier = Modifier.padding(start = 58.dp)
            )
        }
    }
}
