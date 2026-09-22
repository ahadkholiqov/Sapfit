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
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.AppState
import com.example.data.AppViewModel
import com.example.data.ProfileSubscreen
import com.example.i18n.Str
import com.example.ui.theme.AppTheme

/** Router: renders the current profile sub-screen, or nothing if none. */
@Composable
fun ProfileSubscreenHost(state: AppState, viewModel: AppViewModel) {
    val sub = state.profileSubscreen ?: return
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.appBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        when (sub) {
            ProfileSubscreen.ACCOUNT -> AccountBody(state, viewModel)
            ProfileSubscreen.LESSONS -> LessonsBody(state, viewModel)
            ProfileSubscreen.FOCUS -> FocusBody(state, viewModel)
            ProfileSubscreen.PRIVACY -> PrivacyBody(state, viewModel)
            ProfileSubscreen.APPEARANCE -> AppearanceBody(state, viewModel)
            ProfileSubscreen.HELP -> HelpBody(state, viewModel)
        }
    }
}

@Composable
private fun PsHeader(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 14.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(AppTheme.colors.cardBackground)
                .border(0.5.dp, AppTheme.colors.borderDefault, CircleShape)
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = null,
                tint = AppTheme.colors.textPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = title,
            color = AppTheme.colors.textPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun LabelValueCard(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Text(
            text = label.uppercase(),
            color = AppTheme.colors.textFaint,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.6.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(text = value, color = AppTheme.colors.textPrimary, fontSize = 14.sp)
    }
}

@Composable
private fun FormatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = AppTheme.colors.textPrimary, fontSize = 13.sp)
        Text(text = value, color = AppTheme.colors.accentText, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PillCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 2.dp),
        content = content
    )
}

@Composable
private fun HintText(text: String) {
    Text(
        text = text,
        color = AppTheme.colors.textMuted,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
    )
}

@Composable
private fun DangerButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.dangerRed, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = AppTheme.colors.dangerRed, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun SecondaryButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = AppTheme.colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

// ==================== ACCOUNT ====================

@Composable
private fun AccountBody(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        PsHeader(Str.accountTitle(lang)) { viewModel.closeProfileSubscreen() }
        Column(
            modifier = Modifier.padding(horizontal = 20.dp).padding(top = 8.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LabelValueCard(Str.psAccountEmailLabel(lang), Str.psAccountEmailValue(lang))
            LabelValueCard(Str.psAccountSubLabel(lang), Str.psAccountSubValue(lang))
            LabelValueCard(Str.psAccountLinksLabel(lang), Str.psAccountLinksValue(lang))
            Spacer(Modifier.height(10.dp))
            DangerButton(Str.psAccountDelete(lang)) { /* mock */ }
        }
    }
}

// ==================== LESSONS ====================

@Composable
private fun LessonsBody(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        PsHeader(Str.studyScheduleTitle(lang)) { viewModel.closeProfileSubscreen() }
        Column(
            modifier = Modifier.padding(horizontal = 20.dp).padding(top = 8.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PillCard {
                FormatRow(Str.psLessonsLength(lang), Str.psLessonsLengthValue(lang))
                FormatRow(Str.psLessonsTime(lang), Str.psLessonsTimeValue(lang))
                FormatRow(Str.psLessonsRest(lang), Str.psLessonsRestValue(lang))
            }
            HintText(Str.psLessonsHint(lang))
        }
    }
}

// ==================== FOCUS ====================

@Composable
private fun FocusBody(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        PsHeader(Str.focusModeTitle(lang)) { viewModel.closeProfileSubscreen() }
        Column(
            modifier = Modifier.padding(horizontal = 20.dp).padding(top = 8.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = Str.psFocusIntro(lang),
                color = AppTheme.colors.textSecondary,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
            )
            PillCard {
                FormatRow("TikTok", Str.psFocusBlock(lang))
                FormatRow("Instagram", Str.psFocusBlock(lang))
                FormatRow("YouTube", Str.psFocusNotifOnly(lang))
                FormatRow("Telegram", Str.psFocusNotifOnly(lang))
            }
            HintText(Str.psFocusHint(lang))
        }
    }
}

// ==================== PRIVACY ====================

@Composable
private fun PrivacyBody(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        PsHeader(Str.privacyTitle(lang)) { viewModel.closeProfileSubscreen() }
        Column(
            modifier = Modifier.padding(horizontal = 20.dp).padding(top = 8.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AppTheme.colors.cardBackground)
                    .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = Str.psPrivacyUseTraining(lang),
                    color = AppTheme.colors.textPrimary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.weight(1f).padding(end = 10.dp)
                )
                Switch(
                    checked = state.prefUseAnswersForTraining,
                    onCheckedChange = { viewModel.togglePrefUseAnswersForTraining() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = AppTheme.colors.onAccent,
                        checkedTrackColor = AppTheme.colors.accentPrimary,
                        uncheckedThumbColor = AppTheme.colors.textMuted,
                        uncheckedTrackColor = AppTheme.colors.insetControlBackground
                    )
                )
            }
            Spacer(Modifier.height(6.dp))
            SecondaryButton(Str.psPrivacyExport(lang)) { /* mock */ }
        }
    }
}

// ==================== APPEARANCE ====================

@Composable
private fun AppearanceBody(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    val themeLabel = if (state.isDarkTheme) Str.psAppearanceThemeDark(lang) else Str.psAppearanceThemeLight(lang)
    val langLabel = if (lang == AppLanguage.EN) Str.psAppearanceLangValueEn(lang) else Str.psAppearanceLangValueRu(lang)

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        PsHeader(Str.appearanceTitle(lang)) { viewModel.closeProfileSubscreen() }
        Column(
            modifier = Modifier.padding(horizontal = 20.dp).padding(top = 8.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AppTheme.colors.cardBackground)
                    .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = Str.psAppearanceTheme(lang), color = AppTheme.colors.textPrimary, fontSize = 13.sp)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = themeLabel, color = AppTheme.colors.accentText, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Switch(
                        checked = state.isDarkTheme,
                        onCheckedChange = { viewModel.toggleTheme() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AppTheme.colors.onAccent,
                            checkedTrackColor = AppTheme.colors.accentPrimary,
                            uncheckedThumbColor = AppTheme.colors.textMuted,
                            uncheckedTrackColor = AppTheme.colors.insetControlBackground
                        )
                    )
                }
            }
            PillCard {
                FormatRow(Str.psAppearanceFont(lang), Str.psAppearanceFontValue(lang))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AppTheme.colors.cardBackground)
                    .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = Str.psAppearanceLang(lang), color = AppTheme.colors.textPrimary, fontSize = 13.sp)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(AppTheme.colors.insetControlBackground)
                        .padding(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(AppLanguage.RU to "RU", AppLanguage.EN to "EN").forEach { (l, label) ->
                        val selected = lang == l
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (selected) AppTheme.colors.accentPrimary else androidx.compose.ui.graphics.Color.Transparent)
                                .clickable { viewModel.setLanguage(l) }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (selected) AppTheme.colors.onAccent else AppTheme.colors.textMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            Text(
                text = langLabel,
                color = AppTheme.colors.textMuted,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

// ==================== HELP ====================

@Composable
private fun HelpBody(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        PsHeader(Str.helpTitle(lang)) { viewModel.closeProfileSubscreen() }
        Column(
            modifier = Modifier.padding(horizontal = 20.dp).padding(top = 8.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HelpCard(Str.psHelpScienceTitle(lang), Str.psHelpScienceSub(lang))
            HelpCard(Str.psHelpFaqTitle(lang), Str.psHelpFaqSub(lang))
            HelpCard(Str.psHelpFeedbackTitle(lang), Str.psHelpFeedbackSub(lang))
        }
    }
}

@Composable
private fun HelpCard(title: String, sub: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(14.dp))
            .clickable { /* mock */ }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = title, color = AppTheme.colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Text(text = sub, color = AppTheme.colors.textSecondary, fontSize = 12.sp, lineHeight = 17.sp)
    }
}
