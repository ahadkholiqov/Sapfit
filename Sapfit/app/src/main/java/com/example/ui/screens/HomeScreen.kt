package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppState
import com.example.data.AppViewModel
import com.example.data.HomeMode
import com.example.i18n.Str
import com.example.model.AttachmentKind
import com.example.ui.components.ReminderBannerStack
import com.example.ui.components.SapfitLogo
import com.example.ui.theme.AppTheme
import kotlinx.coroutines.delay

private const val PHRASE_TICK_MS = 4000L

@Composable
fun HomeScreen(
    state: AppState,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val lang = state.appLanguage
    val phrases = Str.homePhrases(lang)
    val currentPhrase = phrases[state.homePhraseIndex % phrases.size]

    LaunchedEffect(state.homePhraseIndex, lang) {
        delay(PHRASE_TICK_MS)
        viewModel.advanceHomePhrase()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Brand header + quick theme toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SapfitLogo(
                    isDark = state.isDarkTheme,
                    modifier = Modifier.size(38.dp)
                )
                Text(
                    text = "Sapfit",
                    color = AppTheme.colors.textPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
            }
            IconButton(
                onClick = { viewModel.toggleTheme() },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(AppTheme.colors.surfaceContainer)
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = if (state.isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                    contentDescription = null,
                    tint = AppTheme.colors.accentText,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        ReminderBannerStack(
            banners = state.reminderBanners,
            onBannerClick = { viewModel.clickReminderBanner(it) },
            onDismiss = { viewModel.dismissReminderBanner(it) }
        )

        // Wave-2 trigger banner (spec §3): shown after wave 1, until wave 2 is
        // completed. Does not auto-dismiss — user either takes it or defers.
        if (state.wave1Completed && !state.wave2Completed && !state.wave2Started) {
            Wave2TriggerBanner(
                title = Str.w2BannerTitle(lang),
                text = Str.w2BannerText(lang),
                primary = Str.w2BannerRun(lang),
                secondary = Str.w2BannerCancel(lang),
                onPrimary = { viewModel.startWave2() },
                onSecondary = { /* stays visible per spec — no dismiss */ }
            )
        }

        // Phrase carousel — 22sp semibold, fade transition
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = currentPhrase,
                transitionSpec = {
                    (fadeIn(tween(220)) + slideInVertically(tween(220)) { it / 6 })
                        .togetherWith(fadeOut(tween(180)))
                },
                label = "home_phrase"
            ) { phrase ->
                Text(
                    text = phrase,
                    color = AppTheme.colors.textPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Two large mode buttons (toggle, not launch)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ModeButton(
                title = Str.modeQuick(state.appLanguage),
                icon = Icons.Filled.Bolt,
                selected = state.homeMode == HomeMode.QUICK,
                onClick = { viewModel.setHomeMode(HomeMode.QUICK) },
                testTag = "mode_quick",
                modifier = Modifier.weight(1f)
            )
            ModeButton(
                title = Str.modeProject(state.appLanguage),
                icon = Icons.Filled.ShowChart,
                selected = state.homeMode == HomeMode.PROJECT,
                onClick = { viewModel.setHomeMode(HomeMode.PROJECT) },
                testTag = "mode_project",
                modifier = Modifier.weight(1f)
            )
        }

        // Attached materials chip row
        AnimatedVisibility(visible = state.homeAttachments.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                state.homeAttachments.forEach { att ->
                    AttachmentChip(
                        label = att.label,
                        onRemove = { viewModel.removeAttachment(att.id) }
                    )
                }
            }
        }

        // Gemini-style row: attach + input + send
        GeminiInputRow(state = state, viewModel = viewModel)
    }
}

@Composable
private fun ModeButton(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val bg by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.activeSegmentBackground else AppTheme.colors.cardBackground,
        animationSpec = tween(240),
        label = "mode_bg"
    )
    val border by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.activeSegmentBorder else AppTheme.colors.borderDefault,
        animationSpec = tween(240),
        label = "mode_border"
    )
    val content by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.accentText else AppTheme.colors.textPrimary,
        animationSpec = tween(240),
        label = "mode_content"
    )
    val iconTint by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.accentText else AppTheme.colors.accentSoftIcon,
        animationSpec = tween(240),
        label = "mode_icon"
    )

    Box(
        modifier = modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .border(0.5.dp, border, RoundedCornerShape(20.dp))
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = title,
                color = content,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun AttachmentChip(label: String, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(AppTheme.colors.activeSegmentBackground)
            .border(0.5.dp, AppTheme.colors.activeSegmentBorder, RoundedCornerShape(14.dp))
            .padding(start = 8.dp, end = 6.dp, top = 5.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.AttachFile,
            contentDescription = null,
            tint = AppTheme.colors.accentText,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = AppTheme.colors.accentText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
                modifier = Modifier.size(10.dp)
            )
        }
    }
}

@Composable
private fun GeminiInputRow(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    val canSend = state.homeInputText.isNotBlank() || state.homeAttachments.isNotEmpty()

    Column(modifier = Modifier.fillMaxWidth()) {
        // Attach menu (appears above the row when open)
        AnimatedVisibility(visible = state.isAttachMenuOpen) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(AppTheme.colors.cardBackground)
                    .border(0.5.dp, AppTheme.colors.borderDropdown, RoundedCornerShape(18.dp))
                    .padding(vertical = 4.dp)
            ) {
                AttachMenuItem(
                    icon = Icons.Filled.CameraAlt,
                    label = Str.attachCamera(lang),
                    onClick = { viewModel.addAttachment(AttachmentKind.CAMERA) }
                )
                AttachMenuItem(
                    icon = Icons.Filled.Image,
                    label = Str.attachGallery(lang),
                    onClick = { viewModel.addAttachment(AttachmentKind.IMAGE) }
                )
                AttachMenuItem(
                    icon = Icons.Filled.InsertDriveFile,
                    label = Str.attachFile(lang),
                    onClick = { viewModel.addAttachment(AttachmentKind.FILE) }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(AppTheme.colors.cardBackground)
                .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(24.dp))
                .padding(horizontal = 6.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Attach button
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .clickable { viewModel.toggleAttachMenu() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AttachFile,
                    contentDescription = null,
                    tint = AppTheme.colors.textSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Text input
            Box(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                if (state.homeInputText.isEmpty()) {
                    Text(
                        text = Str.homeInputPlaceholder(lang),
                        color = AppTheme.colors.textMuted,
                        fontSize = 14.sp
                    )
                }
                BasicTextField(
                    value = state.homeInputText,
                    onValueChange = { viewModel.updateHomeInput(it) },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = AppTheme.colors.textPrimary,
                        fontSize = 14.sp
                    ),
                    cursorBrush = SolidColor(AppTheme.colors.accentPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_input")
                )
            }

            // Send button
            val sendBg by animateColorAsState(
                targetValue = if (canSend) AppTheme.colors.accentPrimary else AppTheme.colors.insetControlBackground,
                animationSpec = tween(200),
                label = "send_bg"
            )
            val sendTint by animateColorAsState(
                targetValue = if (canSend) AppTheme.colors.onAccent else AppTheme.colors.textMuted,
                animationSpec = tween(200),
                label = "send_tint"
            )
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(sendBg)
                    .clickable(enabled = canSend) { viewModel.sendFromHome() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    tint = sendTint,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun Wave2TriggerBanner(
    title: String,
    text: String,
    primary: String,
    secondary: String,
    onPrimary: () -> Unit,
    onSecondary: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(AppTheme.colors.activeSegmentBackground)
            .border(0.5.dp, AppTheme.colors.activeSegmentBorder, RoundedCornerShape(18.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = title, color = AppTheme.colors.accentText, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        Text(text = text, color = AppTheme.colors.textSecondary, fontSize = 11.sp, lineHeight = 15.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(AppTheme.colors.accentPrimary)
                    .clickable { onPrimary() }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = primary, color = AppTheme.colors.onAccent, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(AppTheme.colors.cardBackground)
                    .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(14.dp))
                    .clickable { onSecondary() }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = secondary, color = AppTheme.colors.textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun AttachMenuItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppTheme.colors.accentSoftIcon,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label,
            color = AppTheme.colors.textPrimary,
            fontSize = 14.sp
        )
    }
}
