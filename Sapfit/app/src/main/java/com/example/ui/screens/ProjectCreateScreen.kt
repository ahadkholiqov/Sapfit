package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppState
import com.example.data.AppViewModel
import com.example.data.ProjectCreateStage
import com.example.data.ProjectGoal
import com.example.i18n.Str
import com.example.model.Familiarity
import com.example.model.RoadmapAnchor
import com.example.ui.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun ProjectCreateScreen(state: AppState, viewModel: AppViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.appBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        AnimatedContent(
            targetState = state.projectCreateStage,
            transitionSpec = { fadeIn(tween(240)).togetherWith(fadeOut(tween(200))) },
            label = "pc_stage",
            modifier = Modifier.fillMaxSize()
        ) { stage ->
            when (stage) {
                ProjectCreateStage.FORM -> FormBody(state, viewModel)
                ProjectCreateStage.PROCESSING -> ProcessingBody(state, viewModel)
                ProjectCreateStage.CALIBRATION -> CalibrationBody(state, viewModel)
                ProjectCreateStage.IDLE -> Box(Modifier.fillMaxSize())
            }
        }

        // Close (cancel) button top-right, only in FORM stage.
        if (state.projectCreateStage == ProjectCreateStage.FORM) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 16.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.cardBackground)
                    .border(0.5.dp, AppTheme.colors.borderDefault, CircleShape)
                    .clickable { viewModel.cancelProjectCreation() },
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
    }
}

// ==================== FORM ====================

@Composable
private fun FormBody(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    val canSubmit = state.newProjectTitle.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 60.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = Str.pcTitle(lang),
            color = AppTheme.colors.textPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(4.dp))

        PcField(
            label = Str.pcNameLabel(lang),
            placeholder = Str.pcNameHint(lang),
            value = state.newProjectTitle,
            onValueChange = { viewModel.updateNewProjectTitle(it) }
        )

        Text(
            text = Str.pcGoalLabel(lang),
            color = AppTheme.colors.textMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 4.dp)
        )
        val goals = listOf(
            ProjectGoal.TEST to Str.pcGoalTest(lang),
            ProjectGoal.EXAM to Str.pcGoalExam(lang),
            ProjectGoal.SELF to Str.pcGoalSelf(lang)
        )
        goals.forEach { (g, label) ->
            PcSelectableRow(
                selected = state.draftGoal == g,
                label = label,
                onClick = { viewModel.setDraftGoal(g) }
            )
        }

        Text(
            text = Str.subjectLabel(lang),
            color = AppTheme.colors.textMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 4.dp)
        )
        PcField(
            label = "",
            placeholder = Str.selectSubject(lang),
            value = state.newProjectSubject,
            onValueChange = { viewModel.setNewProjectSubject(it) }
        )

        PcField(
            label = Str.pcDeadlineLabel(lang),
            placeholder = Str.pcDeadlineHint(lang),
            value = state.newProjectDeadline,
            onValueChange = { viewModel.updateNewProjectDeadline(it) }
        )

        Spacer(Modifier.height(6.dp))
        PcPrimary(
            text = Str.pcCreateBtn(lang),
            enabled = canSubmit,
            onClick = { viewModel.submitProjectDraft() }
        )
        PcSecondary(
            text = Str.pcCancelBtn(lang),
            onClick = { viewModel.cancelProjectCreation() }
        )
    }
}

// ==================== PROCESSING ====================

@Composable
private fun ProcessingBody(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    val phrases = listOf(Str.pcProcessing1(lang), Str.pcProcessing2(lang), Str.pcProcessing3(lang))
    var idx by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        for (i in 1 until phrases.size) {
            delay(1000)
            idx = i
        }
        delay(1000)
        viewModel.finishProjectProcessing()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PcSpinner()
            Spacer(Modifier.height(18.dp))
            AnimatedContent(
                targetState = phrases[idx],
                transitionSpec = { fadeIn(tween(220)).togetherWith(fadeOut(tween(180))) },
                label = "pc_phrase"
            ) { p ->
                Text(text = p, color = AppTheme.colors.textSecondary, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun PcSpinner() {
    val transition = rememberInfiniteTransition(label = "pcspin")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(800, easing = LinearEasing)),
        label = "pcang"
    )
    val track = AppTheme.colors.insetControlBackground
    val accent = AppTheme.colors.accentPrimary
    Canvas(modifier = Modifier.size(36.dp)) {
        val stroke = 3f
        drawArc(color = track, startAngle = 0f, sweepAngle = 360f, useCenter = false, style = Stroke(width = stroke))
        rotate(angle) {
            drawArc(color = accent, startAngle = 0f, sweepAngle = 90f, useCenter = false, style = Stroke(width = stroke))
        }
    }
}

// ==================== CALIBRATION ====================

@Composable
private fun CalibrationBody(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 40.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = Str.pcCalibTitle(lang),
            color = AppTheme.colors.textPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = Str.pcCalibSub(lang),
            color = AppTheme.colors.textSecondary,
            fontSize = 13.sp,
            lineHeight = 19.sp
        )
        Spacer(Modifier.height(4.dp))
        state.draftAnchors.forEach { anchor ->
            CalibrationCard(anchor = anchor, lang = lang) { f ->
                viewModel.setAnchorFamiliarity(anchor.id, f)
            }
        }
        Spacer(Modifier.height(8.dp))
        PcPrimary(
            text = Str.pcCalibDone(lang),
            enabled = true,
            onClick = { viewModel.finalizeProjectCreation() }
        )
    }
}

@Composable
private fun CalibrationCard(
    anchor: RoadmapAnchor,
    lang: com.example.data.AppLanguage,
    onPick: (Familiarity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = anchor.title,
            color = AppTheme.colors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(AppTheme.colors.insetControlBackground)
                .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(12.dp))
                .padding(3.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            listOf(
                Familiarity.NOVICE to Str.famNovice(lang),
                Familiarity.PARTIAL to Str.famPartial(lang),
                Familiarity.EXPERT to Str.famExpert(lang)
            ).forEach { (f, label) ->
                CalibToggle(
                    label = label,
                    selected = anchor.familiarity == f,
                    onClick = { onPick(f) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CalibToggle(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val bg by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.activeSegmentBackground else androidx.compose.ui.graphics.Color.Transparent,
        animationSpec = tween(180), label = "ct_bg"
    )
    val border by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.activeSegmentBorder else androidx.compose.ui.graphics.Color.Transparent,
        animationSpec = tween(180), label = "ct_border"
    )
    val text by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.accentText else AppTheme.colors.textSecondary,
        animationSpec = tween(180), label = "ct_text"
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .border(0.5.dp, border, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = text, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

// ==================== primitives ====================

@Composable
private fun PcField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (label.isNotBlank()) {
            Text(
                text = label,
                color = AppTheme.colors.textMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(AppTheme.colors.cardBackground)
                .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(14.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            if (value.isEmpty()) {
                Text(text = placeholder, color = AppTheme.colors.textMuted, fontSize = 14.sp)
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(color = AppTheme.colors.textPrimary, fontSize = 14.sp),
                cursorBrush = SolidColor(AppTheme.colors.accentPrimary),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PcSelectableRow(selected: Boolean, label: String, onClick: () -> Unit) {
    val bg by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.activeSegmentBackground else AppTheme.colors.cardBackground,
        animationSpec = tween(180), label = "pcs_bg"
    )
    val border by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.activeSegmentBorder else AppTheme.colors.borderDefault,
        animationSpec = tween(180), label = "pcs_border"
    )
    val text by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.accentText else AppTheme.colors.textPrimary,
        animationSpec = tween(180), label = "pcs_text"
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
private fun PcPrimary(text: String, enabled: Boolean, onClick: () -> Unit) {
    val bg by animateColorAsState(
        targetValue = if (enabled) AppTheme.colors.accentPrimary else AppTheme.colors.insetControlBackground,
        animationSpec = tween(200), label = "pcp_bg"
    )
    val fg by animateColorAsState(
        targetValue = if (enabled) AppTheme.colors.onAccent else AppTheme.colors.textMuted,
        animationSpec = tween(200), label = "pcp_fg"
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
private fun PcSecondary(text: String, onClick: () -> Unit) {
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
