package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.AppState
import com.example.data.AppViewModel
import com.example.data.TechniqueRegistry
import com.example.i18n.Str
import com.example.ui.theme.AppTheme

@Composable
fun Wave2Screen(state: AppState, viewModel: AppViewModel) {
    val step = state.wave2Step
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.appBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Progress
        val progress = step.toFloat() / 7f
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(AppTheme.colors.insetControlBackground)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(AppTheme.colors.accentPrimary)
            )
        }

        AnimatedContent(
            targetState = step,
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInHorizontally(tween(260)) { it / 4 } + fadeIn(tween(260)))
                        .togetherWith(slideOutHorizontally(tween(220)) { -it / 4 } + fadeOut(tween(220)))
                } else {
                    (slideInHorizontally(tween(260)) { -it / 4 } + fadeIn(tween(260)))
                        .togetherWith(slideOutHorizontally(tween(220)) { it / 4 } + fadeOut(tween(220)))
                }
            },
            label = "w2_step",
            modifier = Modifier.fillMaxSize()
        ) { s ->
            when (s) {
                0 -> IntroStep(state, viewModel)
                1 -> SubjectStep(state, viewModel)
                2 -> DemoStep(state, viewModel)
                3 -> TechStep(state, viewModel, techIndex = 0)
                4 -> TechStep(state, viewModel, techIndex = 1)
                5 -> TechStep(state, viewModel, techIndex = 2)
                6 -> FormatStep(state, viewModel)
                7 -> ConfigStep(state, viewModel)
                else -> IntroStep(state, viewModel)
            }
        }

        // Back arrow on steps 1..7
        if (step in 1..7) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 12.dp, start = 16.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.cardBackground)
                    .border(0.5.dp, AppTheme.colors.borderDefault, CircleShape)
                    .clickable { viewModel.wave2Back() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = AppTheme.colors.textPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun IntroStep(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp)
            .padding(top = 80.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = Str.w2IntroTitle(lang),
            color = AppTheme.colors.textPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = Str.w2IntroSub(lang),
            color = AppTheme.colors.textSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(36.dp))
        W2Primary(text = Str.w2Start(lang), enabled = true, onClick = { viewModel.wave2Next() })
        Spacer(Modifier.height(10.dp))
        W2Secondary(text = Str.w2Cancel(lang), onClick = { viewModel.wave2Cancel() })
    }
}

@Composable
private fun SubjectStep(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    val subjects = Str.w2Subjects(lang)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 68.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = Str.w2SubjectTitle(lang),
            color = AppTheme.colors.textPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 27.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        subjects.forEach { s ->
            W2Row(selected = state.wave2Subject == s, label = s, onClick = { viewModel.setWave2Subject(s) })
        }
        Spacer(Modifier.height(8.dp))
        W2Primary(
            text = Str.onbNext(lang),
            enabled = state.wave2Subject.isNotBlank(),
            onClick = { viewModel.wave2Next() }
        )
    }
}

@Composable
private fun DemoStep(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    val topic = Str.w2DemoTopicFor(lang, state.wave2Subject)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 68.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = Str.w2DemoLead(lang),
            color = AppTheme.colors.textSecondary,
            fontSize = 13.sp
        )
        Text(
            text = topic,
            color = AppTheme.colors.textPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 32.sp
        )
        Text(
            text = Str.w2DemoSub(lang),
            color = AppTheme.colors.textSecondary,
            fontSize = 13.sp,
            lineHeight = 19.sp,
            modifier = Modifier.padding(top = 6.dp)
        )
        Spacer(Modifier.height(12.dp))
        W2Primary(text = Str.w2DemoStart(lang), enabled = true, onClick = { viewModel.wave2Next() })
    }
}

@Composable
private fun TechStep(state: AppState, viewModel: AppViewModel, techIndex: Int) {
    val lang = state.appLanguage
    val techIds = listOf("feynman", "dual_coding", "analogies")
    val techId = techIds[techIndex]
    val tech = TechniqueRegistry.byId(techId) ?: return
    val rating = state.wave2TechRatings[techId] ?: -1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 68.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "${techIndex + 1} / 3 · ${Str.w2TechIntro(lang)}",
            color = AppTheme.colors.accentText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.6.sp
        )
        Text(
            text = tech.name(lang),
            color = AppTheme.colors.textPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = tech.desc(lang) ?: tech.short(lang),
            color = AppTheme.colors.textTertiary,
            fontSize = 13.sp,
            lineHeight = 20.sp
        )
        tech.example(lang)?.let { ex ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppTheme.colors.cardBackground)
                    .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Text(text = ex, color = AppTheme.colors.textSecondary, fontSize = 12.sp, lineHeight = 18.sp)
            }
        }

        Spacer(Modifier.height(8.dp))
        Text(
            text = Str.w2TechRating(lang),
            color = AppTheme.colors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        val labels = listOf(
            0 to Str.w2RateLoved(lang),
            1 to Str.w2RateOk(lang),
            2 to Str.w2RateMeh(lang),
            3 to Str.w2RateNo(lang)
        )
        labels.forEach { (idx, label) ->
            W2Row(selected = rating == idx, label = label, onClick = { viewModel.rateWave2Tech(techId, idx) })
        }
        Spacer(Modifier.height(8.dp))
        W2Primary(
            text = Str.onbNext(lang),
            enabled = rating >= 0,
            onClick = { viewModel.wave2Next() }
        )
    }
}

@Composable
private fun FormatStep(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    val canProceed = state.wave2FormatTime.isNotBlank() &&
        state.wave2FormatLength.isNotBlank() &&
        state.wave2FormatInterleave.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 68.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = Str.w2FormatTitle(lang),
            color = AppTheme.colors.textPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(4.dp))

        FormatBlock(
            question = Str.w2FormatQ1(lang),
            options = listOf(Str.w2FormatMorning(lang), Str.w2FormatEvening(lang)),
            selected = state.wave2FormatTime,
            onPick = { viewModel.setWave2FormatTime(it) }
        )
        FormatBlock(
            question = Str.w2FormatQ2(lang),
            options = listOf(Str.w2FormatShort(lang), Str.w2FormatLong(lang)),
            selected = state.wave2FormatLength,
            onPick = { viewModel.setWave2FormatLength(it) }
        )
        FormatBlock(
            question = Str.w2FormatQ3(lang),
            options = listOf(Str.w2FormatMix(lang), Str.w2FormatSingle(lang)),
            selected = state.wave2FormatInterleave,
            onPick = { viewModel.setWave2FormatInterleave(it) }
        )
        Spacer(Modifier.height(8.dp))
        W2Primary(
            text = Str.onbNext(lang),
            enabled = canProceed,
            onClick = { viewModel.wave2Next() }
        )
    }
}

@Composable
private fun FormatBlock(question: String, options: List<String>, selected: String, onPick: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = question,
            color = AppTheme.colors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 6.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { opt ->
                W2PillOption(
                    selected = selected == opt,
                    label = opt,
                    onClick = { onPick(opt) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ConfigStep(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    val topThree = state.wave2TechRatings
        .toList()
        .sortedBy { it.second } // 0=loved first
        .take(3)
        .mapNotNull { (id, _) -> TechniqueRegistry.byId(id)?.name(lang) }
    val formatLine = listOf(
        state.wave2FormatTime,
        state.wave2FormatLength,
        state.wave2FormatInterleave
    ).filter { it.isNotBlank() }.joinToString(" · ")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 68.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = Str.w2ConfigTitle(lang),
            color = AppTheme.colors.textPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = Str.w2ConfigSub(lang),
            color = AppTheme.colors.textSecondary,
            fontSize = 13.sp,
            lineHeight = 19.sp
        )
        Spacer(Modifier.height(4.dp))
        ConfigCard(title = Str.w2ConfigTechniques(lang), body = topThree.joinToString(" · "))
        ConfigCard(title = Str.w2ConfigFormat(lang), body = formatLine)
        Spacer(Modifier.height(8.dp))
        W2Primary(text = Str.w2Finish(lang), enabled = true, onClick = { viewModel.wave2Finish() })
        W2Secondary(text = Str.w2EditLater(lang), onClick = { viewModel.wave2Finish() })
    }
}

@Composable
private fun ConfigCard(title: String, body: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title.uppercase(),
            color = AppTheme.colors.accentText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.6.sp
        )
        Text(
            text = body.ifBlank { "—" },
            color = AppTheme.colors.textTertiary,
            fontSize = 13.sp,
            lineHeight = 19.sp
        )
    }
}

// ==================== primitives ====================

@Composable
private fun W2Row(selected: Boolean, label: String, onClick: () -> Unit) {
    val bg by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.activeSegmentBackground else AppTheme.colors.cardBackground,
        animationSpec = tween(180), label = "w2r_bg"
    )
    val border by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.activeSegmentBorder else AppTheme.colors.borderDefault,
        animationSpec = tween(180), label = "w2r_border"
    )
    val text by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.accentText else AppTheme.colors.textPrimary,
        animationSpec = tween(180), label = "w2r_text"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(0.5.dp, border, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(text = label, color = text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun W2PillOption(selected: Boolean, label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val bg by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.activeSegmentBackground else AppTheme.colors.cardBackground,
        animationSpec = tween(180), label = "w2p_bg"
    )
    val border by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.activeSegmentBorder else AppTheme.colors.borderDefault,
        animationSpec = tween(180), label = "w2p_border"
    )
    val text by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.accentText else AppTheme.colors.textPrimary,
        animationSpec = tween(180), label = "w2p_text"
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(0.5.dp, border, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = text, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun W2Primary(text: String, enabled: Boolean, onClick: () -> Unit) {
    val bg by animateColorAsState(
        targetValue = if (enabled) AppTheme.colors.accentPrimary else AppTheme.colors.insetControlBackground,
        animationSpec = tween(200), label = "w2p_bg"
    )
    val fg by animateColorAsState(
        targetValue = if (enabled) AppTheme.colors.onAccent else AppTheme.colors.textMuted,
        animationSpec = tween(200), label = "w2p_fg"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .clickable(enabled = enabled) { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = fg, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun W2Secondary(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = AppTheme.colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
