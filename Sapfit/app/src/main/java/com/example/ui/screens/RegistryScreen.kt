package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppState
import com.example.data.AppViewModel
import com.example.data.TechniqueEntry
import com.example.data.TechniqueRegistry
import com.example.i18n.Str
import com.example.ui.theme.AppTheme

@Composable
fun RegistryScreen(
    state: AppState,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val lang = state.appLanguage
    val techniques = TechniqueRegistry.all
    val detailed = techniques.filter { it.isDetailed }
    val short = techniques.filter { !it.isDetailed }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(AppTheme.colors.cardBackground)
                        .border(0.5.dp, AppTheme.colors.borderDefault, CircleShape)
                        .clickable { viewModel.closeRegistry() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ChevronLeft,
                        contentDescription = null,
                        tint = AppTheme.colors.accentText,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    text = Str.registryBtn(lang),
                    color = AppTheme.colors.textPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // 3 detailed cards, one per row
            detailed.forEach { t ->
                DetailedTechniqueCard(entry = t, lang = lang, onOpen = { viewModel.openTechniqueDetail(t.id) })
            }

            Spacer(Modifier.height(4.dp))
            Text(
                text = if (lang == com.example.data.AppLanguage.EN) "MORE TECHNIQUES" else "ЕЩЁ ТЕХНИКИ",
                color = AppTheme.colors.textFaint,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.8.sp
            )

            // 8 short cards in a 2-column grid (rendered as rows-of-2)
            short.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row.forEach { t ->
                        ShortTechniqueCard(
                            entry = t, lang = lang, modifier = Modifier.weight(1f),
                            onClick = { viewModel.openTechniqueDetail(t.id) }
                        )
                    }
                    if (row.size == 1) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }

        // Detail modal
        val sel = state.selectedTechniqueId?.let { TechniqueRegistry.byId(it) }
        AnimatedVisibility(
            visible = sel != null,
            enter = fadeIn(tween(200)) + slideInVertically(tween(240)) { it / 4 },
            exit = fadeOut(tween(180)) + slideOutVertically(tween(200)) { it / 4 }
        ) {
            if (sel != null) TechniqueDetailModal(entry = sel, state = state, onClose = { viewModel.closeTechniqueDetail() })
        }
    }
}

@Composable
private fun DetailedTechniqueCard(
    entry: TechniqueEntry,
    lang: com.example.data.AppLanguage,
    onOpen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.activeSegmentBorder, RoundedCornerShape(18.dp))
            .clickable { onOpen() }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = entry.name(lang),
            color = AppTheme.colors.textPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = entry.short(lang),
            color = AppTheme.colors.textSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
        )
        val tags = entry.tags(lang)
        if (tags.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                tags.take(4).forEach { tag -> TagChip(tag) }
            }
        }
        Text(
            text = (if (lang == com.example.data.AppLanguage.EN) "Read more" else "Подробнее") + " →",
            color = AppTheme.colors.accentText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ShortTechniqueCard(
    entry: TechniqueEntry,
    lang: com.example.data.AppLanguage,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = entry.name(lang),
            color = AppTheme.colors.textPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 16.sp
        )
        Text(
            text = entry.short(lang),
            color = AppTheme.colors.textSecondary,
            fontSize = 10.sp,
            lineHeight = 14.sp
        )
    }
}

@Composable
private fun TagChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(AppTheme.colors.insetControlBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text = text, color = AppTheme.colors.textSecondary, fontSize = 10.sp)
    }
}

@Composable
private fun TechniqueDetailModal(
    entry: TechniqueEntry,
    state: AppState,
    onClose: () -> Unit
) {
    val lang = state.appLanguage
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.appBackground.copy(alpha = 0.85f))
            .clickable(enabled = true, onClick = onClose),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(AppTheme.colors.cardBackground)
                .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp)
                .clickable(enabled = false) {},
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.name(lang),
                    color = AppTheme.colors.textPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = AppTheme.colors.textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            entry.desc(lang)?.let { DetailBlock(if (lang == com.example.data.AppLanguage.EN) "Description" else "Описание", it) }
            entry.rationale(lang)?.let { DetailBlock(if (lang == com.example.data.AppLanguage.EN) "Why it works" else "Обоснование", it) }
            entry.example(lang)?.let { DetailBlock(if (lang == com.example.data.AppLanguage.EN) "Example" else "Пример", it) }
            entry.task(lang)?.let { DetailBlock(if (lang == com.example.data.AppLanguage.EN) "Try it" else "Тестовое задание", it) }

            val tags = entry.tags(lang)
            if (tags.isNotEmpty()) {
                Text(
                    text = if (lang == com.example.data.AppLanguage.EN) "TAGS" else "ТЕГИ",
                    color = AppTheme.colors.textFaint,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.6.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    tags.forEach { tag -> TagChip(tag) }
                }
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
private fun DetailBlock(label: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label.uppercase(),
            color = AppTheme.colors.accentText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.6.sp
        )
        Text(
            text = body,
            color = AppTheme.colors.textTertiary,
            fontSize = 13.sp,
            lineHeight = 20.sp
        )
    }
}
