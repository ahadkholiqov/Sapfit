package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppState
import com.example.data.AppViewModel
import com.example.data.SessionStage
import com.example.i18n.Str
import com.example.model.Familiarity
import com.example.model.SessionAtom
import com.example.ui.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun SessionScreen(state: AppState, viewModel: AppViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.appBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SessionTopBar(state = state, onClose = { viewModel.closeSession() })

            AnimatedContent(
                targetState = state.sessionStage,
                transitionSpec = {
                    (slideInHorizontally(tween(260)) { it / 3 } + fadeIn(tween(240)))
                        .togetherWith(slideOutHorizontally(tween(220)) { -it / 3 } + fadeOut(tween(200)))
                },
                label = "session_stage",
                modifier = Modifier.fillMaxSize()
            ) { stage ->
                when (stage) {
                    SessionStage.PROCESSING -> ProcessingBody(state, viewModel)
                    SessionStage.BRANCH -> BranchBody(state, viewModel)
                    SessionStage.ATOM -> AtomBody(state, viewModel)
                    SessionStage.DONE -> DoneBody(state, viewModel)
                }
            }
        }
    }
}

@Composable
private fun SessionTopBar(state: AppState, onClose: () -> Unit) {
    val fraction = when (state.sessionStage) {
        SessionStage.PROCESSING -> 0.05f
        SessionStage.BRANCH -> 0.15f
        SessionStage.ATOM -> {
            val total = state.sessionAtoms.size.coerceAtLeast(1)
            0.2f + 0.75f * (state.sessionAtomIndex.toFloat() / total)
        }
        SessionStage.DONE -> 1f
    }
    val animated by animateFloatAsState(fraction, tween(350), label = "prog")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 12.dp, bottom = 12.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = state.sessionTopicTitle,
                color = AppTheme.colors.textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterEnd)
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
        Spacer(Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(AppTheme.colors.insetControlBackground)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animated)
                    .background(AppTheme.colors.accentPrimary)
            )
        }
    }
}

// ==================== PROCESSING ====================

@Composable
private fun ProcessingBody(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    val phrases = listOf(
        Str.sessProcessing1(lang),
        Str.sessProcessing2(lang),
        Str.sessProcessing3(lang)
    )
    var idx by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        for (i in 1 until phrases.size) {
            delay(900)
            idx = i
        }
        delay(900)
        viewModel.finishSessionProcessing()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SessionSpinner()
            Spacer(Modifier.height(18.dp))
            AnimatedContent(
                targetState = phrases[idx],
                transitionSpec = { fadeIn(tween(220)).togetherWith(fadeOut(tween(180))) },
                label = "sess_phrase"
            ) { p ->
                Text(text = p, color = AppTheme.colors.textSecondary, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun SessionSpinner() {
    val transition = rememberInfiniteTransition(label = "spin")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(800, easing = LinearEasing)),
        label = "ang"
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

// ==================== BRANCH ====================

@Composable
private fun BranchBody(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 8.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = Str.branchTitle(lang),
            color = AppTheme.colors.textPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 27.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        BranchCard(
            title = Str.branchNoviceTitle(lang),
            sub = Str.branchNoviceSub(lang),
            onClick = { viewModel.pickSessionBranch(Familiarity.NOVICE) }
        )
        BranchCard(
            title = Str.branchPartialTitle(lang),
            sub = Str.branchPartialSub(lang),
            onClick = { viewModel.pickSessionBranch(Familiarity.PARTIAL) }
        )
        BranchCard(
            title = Str.branchExpertTitle(lang),
            sub = Str.branchExpertSub(lang),
            onClick = { viewModel.pickSessionBranch(Familiarity.EXPERT) }
        )
    }
}

@Composable
private fun BranchCard(title: String, sub: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = AppTheme.colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(2.dp))
            Text(text = sub, color = AppTheme.colors.textSecondary, fontSize = 11.sp)
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = AppTheme.colors.accentSoftIcon,
            modifier = Modifier.size(18.dp)
        )
    }
}

// ==================== ATOM ====================

@Composable
private fun AtomBody(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    val atom = state.sessionAtoms.getOrNull(state.sessionAtomIndex) ?: return
    val branch = state.sessionBranch ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 8.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "${state.sessionAtomIndex + 1} / ${state.sessionAtoms.size}",
            color = AppTheme.colors.accentText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.6.sp
        )
        Text(
            text = atom.title,
            color = AppTheme.colors.textPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 27.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        when (branch) {
            Familiarity.NOVICE -> NoviceAtom(atom, state, viewModel)
            Familiarity.PARTIAL -> PartialAtom(atom, state, viewModel)
            Familiarity.EXPERT -> ExpertAtom(atom)
        }

        Spacer(Modifier.height(4.dp))

        val canProceed = when (branch) {
            Familiarity.NOVICE -> state.sessionSelectedOption >= 0
            Familiarity.PARTIAL -> state.sessionSelectedOption >= 0
            Familiarity.EXPERT -> true
        }
        PrimaryPill(
            text = Str.sessContinueBtn(lang),
            enabled = canProceed,
            onClick = { viewModel.advanceInAtom() }
        )
    }
}

@Composable
private fun NoviceAtom(atom: SessionAtom, state: AppState, viewModel: AppViewModel) {
    ExplanationCard(text = atom.explainer)
    Spacer(Modifier.height(6.dp))
    Text(
        text = atom.tryQuestion,
        color = AppTheme.colors.textPrimary,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp
    )
    OptionList(
        options = atom.options,
        selected = state.sessionSelectedOption,
        correctIndex = atom.correctIndex,
        onSelect = { viewModel.selectSessionOption(it) }
    )
}

@Composable
private fun PartialAtom(atom: SessionAtom, state: AppState, viewModel: AppViewModel) {
    Text(
        text = atom.tryQuestion,
        color = AppTheme.colors.textPrimary,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp
    )
    OptionList(
        options = atom.options,
        selected = state.sessionSelectedOption,
        correctIndex = atom.correctIndex,
        onSelect = { viewModel.selectSessionOption(it) }
    )
    if (state.sessionShowExplainer) {
        Spacer(Modifier.height(4.dp))
        ExplanationCard(text = atom.explainer)
    }
}

@Composable
private fun ExpertAtom(atom: SessionAtom) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = atom.expertQuestion,
            color = AppTheme.colors.textTertiary,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun ExplanationCard(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = text,
            color = AppTheme.colors.textTertiary,
            fontSize = 14.sp,
            lineHeight = 21.sp
        )
    }
}

@Composable
private fun OptionList(
    options: List<String>,
    selected: Int,
    correctIndex: Int,
    onSelect: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEachIndexed { idx, label ->
            val isSelected = selected == idx
            val isCorrect = isSelected && idx == correctIndex
            val isWrong = isSelected && idx != correctIndex
            val bg by animateColorAsState(
                targetValue = when {
                    isSelected -> AppTheme.colors.activeSegmentBackground
                    else -> AppTheme.colors.cardBackground
                },
                animationSpec = tween(200), label = "opt_bg"
            )
            val border by animateColorAsState(
                targetValue = when {
                    isCorrect -> AppTheme.colors.progressLocked
                    isWrong -> AppTheme.colors.dangerRed
                    isSelected -> AppTheme.colors.activeSegmentBorder
                    else -> AppTheme.colors.borderDefault
                },
                animationSpec = tween(200), label = "opt_border"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) AppTheme.colors.accentText else AppTheme.colors.textPrimary,
                animationSpec = tween(200), label = "opt_text"
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(bg)
                    .border(0.5.dp, border, RoundedCornerShape(14.dp))
                    .clickable { onSelect(idx) }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(text = label, color = textColor, fontSize = 14.sp)
            }
        }
    }
}

// ==================== DONE ====================

@Composable
private fun DoneBody(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp)
            .padding(top = 60.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(AppTheme.colors.activeSegmentBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = AppTheme.colors.accentText,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = Str.sessDoneTitle(lang),
            color = AppTheme.colors.textPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = Str.sessDoneSub(lang),
            color = AppTheme.colors.textSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 19.sp
        )
        Spacer(Modifier.height(36.dp))
        PrimaryPill(text = Str.sessToHome(lang), enabled = true, onClick = { viewModel.finishSession() })
    }
}

@Composable
private fun PrimaryPill(text: String, enabled: Boolean, onClick: () -> Unit) {
    val bg by animateColorAsState(
        targetValue = if (enabled) AppTheme.colors.accentPrimary else AppTheme.colors.insetControlBackground,
        animationSpec = tween(200), label = "pp_bg"
    )
    val fg by animateColorAsState(
        targetValue = if (enabled) AppTheme.colors.onAccent else AppTheme.colors.textMuted,
        animationSpec = tween(200), label = "pp_fg"
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
