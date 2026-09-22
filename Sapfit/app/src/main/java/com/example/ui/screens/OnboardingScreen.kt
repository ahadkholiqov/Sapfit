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
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.AppState
import com.example.data.AppViewModel
import com.example.data.OnbStep
import com.example.i18n.Str
import com.example.model.OnboardingAnswers
import com.example.ui.components.SapfitLogo
import com.example.ui.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreen(state: AppState, viewModel: AppViewModel) {
    val step = state.onbStep

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.appBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Progress bar (thin, top).
        if (step in OnbStep.PROFILE..OnbStep.DONE) {
            val progress = (step.toFloat() / OnbStep.DONE.toFloat()).coerceIn(0f, 1f)
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
            label = "onb_step",
            modifier = Modifier.fillMaxSize()
        ) { current ->
            when (current) {
                OnbStep.WELCOME -> WelcomeStep(state, viewModel)
                OnbStep.PROFILE -> ProfileStep(state, viewModel)
                in OnbStep.Q_FIRST..OnbStep.Q_LAST -> QuestionStep(state, viewModel, current)
                OnbStep.PROCESSING -> ProcessingStep(state, viewModel)
                OnbStep.OBSERVATIONS -> ObservationsStep(state, viewModel)
                OnbStep.MINI_INTRO -> MiniIntroStep(state, viewModel)
                OnbStep.MINI_Q -> MiniQuestionStep(state, viewModel)
                OnbStep.MINI_DONE -> MiniDoneStep(state, viewModel)
                OnbStep.DONE -> DoneStep(state, viewModel)
                else -> WelcomeStep(state, viewModel)
            }
        }

        // Skip-demo button — visible on steps 1..13 (spec §4).
        if (step in OnbStep.PROFILE..OnbStep.OBSERVATIONS) {
            Text(
                text = Str.onbSkip(state.appLanguage),
                color = AppTheme.colors.textMuted,
                fontSize = 11.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 14.dp, end = 18.dp)
                    .clickable { viewModel.onbSkipDemo() }
            )
        }

        // Back arrow — steps 1..16 (welcome and final "done" have no back).
        if (step in OnbStep.PROFILE..OnbStep.MINI_DONE) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 10.dp, start = 16.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.cardBackground)
                    .border(0.5.dp, AppTheme.colors.borderDefault, CircleShape)
                    .clickable { viewModel.onbBack() },
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

// ==================== Screen 1 — Welcome ====================

@Composable
private fun WelcomeStep(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    val a = state.onbAnswers
    val canProceed = a.agreedTerms && a.agreedPrivacy

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp)
            .padding(top = 60.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SapfitLogo(isDark = state.isDarkTheme, modifier = Modifier.size(96.dp))
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Sapfit",
            color = AppTheme.colors.textPrimary,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp
        )
        Spacer(Modifier.height(14.dp))
        Text(
            text = Str.obWelcomeGreeting(lang),
            color = AppTheme.colors.textSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(Modifier.height(48.dp))

        PrimaryButton(
            text = Str.obSignInGoogle(lang),
            enabled = canProceed,
            onClick = { viewModel.onbNext() }
        )
        Spacer(Modifier.height(10.dp))
        SecondaryButton(
            text = Str.obSignInApple(lang),
            enabled = canProceed,
            onClick = { viewModel.onbNext() }
        )

        Spacer(Modifier.height(28.dp))

        CheckboxRow(
            checked = a.agreedTerms,
            label = Str.obAgreeTerms(lang),
            onToggle = { viewModel.updateOnbAnswers { it.copy(agreedTerms = !it.agreedTerms) } }
        )
        Spacer(Modifier.height(10.dp))
        CheckboxRow(
            checked = a.agreedPrivacy,
            label = Str.obAgreePrivacy(lang),
            onToggle = { viewModel.updateOnbAnswers { it.copy(agreedPrivacy = !it.agreedPrivacy) } }
        )
    }
}

// ==================== Screen 2 — Profile ====================

@Composable
private fun ProfileStep(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    val a = state.onbAnswers
    val canProceed = a.name.isNotBlank() && a.age.isNotBlank() && a.situation.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 60.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        StepTitle(Str.obProfileTitle(lang))
        StepSubtitle(Str.obProfileSub(lang))

        Spacer(Modifier.height(4.dp))

        LabeledField(
            label = Str.obNameLabel(lang),
            placeholder = Str.obNameHint(lang),
            value = a.name,
            onValueChange = { v -> viewModel.updateOnbAnswers { it.copy(name = v) } }
        )
        LabeledField(
            label = Str.obAgeLabel(lang),
            placeholder = Str.obAgeHint(lang),
            value = a.age,
            onValueChange = { v -> viewModel.updateOnbAnswers { it.copy(age = v.filter { c -> c.isDigit() }.take(2)) } }
        )

        Text(
            text = Str.obSituationLabel(lang),
            color = AppTheme.colors.textMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 4.dp)
        )
        val situations = listOf(
            "school" to Str.obSituationSchool(lang),
            "exam" to Str.obSituationExam(lang),
            "uni" to Str.obSituationUni(lang),
            "self" to Str.obSituationSelf(lang)
        )
        situations.forEach { (id, label) ->
            SelectableRow(
                selected = a.situation == id,
                label = label,
                onClick = { viewModel.updateOnbAnswers { it.copy(situation = id) } }
            )
        }

        Spacer(Modifier.height(8.dp))
        PrimaryButton(
            text = Str.obStartSetup(lang),
            enabled = canProceed,
            onClick = { viewModel.onbNext() }
        )
    }
}

// ==================== Screens 3–12 — Ten questions ====================

@Composable
private fun QuestionStep(state: AppState, viewModel: AppViewModel, step: Int) {
    val lang = state.appLanguage
    val a = state.onbAnswers
    val qIdx = OnbStep.questionIndex(step) // 0..9

    val prompt = when (qIdx) {
        0 -> Str.obQ1Prompt(lang); 1 -> Str.obQ2Prompt(lang); 2 -> Str.obQ3Prompt(lang)
        3 -> Str.obQ4Prompt(lang); 4 -> Str.obQ5Prompt(lang); 5 -> Str.obQ6Prompt(lang)
        6 -> Str.obQ7Prompt(lang); 7 -> Str.obQ8Prompt(lang); 8 -> Str.obQ9Prompt(lang)
        9 -> Str.obQ10Prompt(lang); else -> ""
    }
    val options = when (qIdx) {
        0 -> Str.obQ1Options(lang); 1 -> Str.obQ2Options(lang); 2 -> Str.obQ3Options(lang)
        3 -> Str.obQ4Options(lang); 4 -> Str.obQ5Options(lang); 5 -> Str.obQ6Options(lang)
        6 -> Str.obQ7Options(lang); 7 -> Str.obQ8Options(lang); 8 -> Str.obQ9Options(lang)
        else -> emptyList()
    }
    val selected = readAnswer(a, qIdx)
    val isFree = qIdx == 9
    val canProceed = if (isFree) true else selected >= 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 68.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "${qIdx + 1} / 10",
            color = AppTheme.colors.accentText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.6.sp
        )
        Text(
            text = prompt,
            color = AppTheme.colors.textPrimary,
            fontSize = 19.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 26.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        )

        if (isFree) {
            LabeledField(
                label = "",
                placeholder = Str.obQ10Placeholder(lang),
                value = a.q10Free,
                onValueChange = { v -> viewModel.updateOnbAnswers { it.copy(q10Free = v) } },
                minLines = 3
            )
        } else {
            options.forEachIndexed { idx, label ->
                SelectableRow(
                    selected = selected == idx,
                    label = label,
                    onClick = {
                        viewModel.updateOnbAnswers { writeAnswer(it, qIdx, idx) }
                    }
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        PrimaryButton(
            text = Str.onbNext(lang),
            enabled = canProceed,
            onClick = { viewModel.onbNext() }
        )
    }
}

private fun readAnswer(a: OnboardingAnswers, qIdx: Int): Int = when (qIdx) {
    0 -> a.q1Sleep; 1 -> a.q2Screen; 2 -> a.q3Focus; 3 -> a.q4Start; 4 -> a.q5PrepStyle
    5 -> a.q6WhyClear; 6 -> a.q7Recall; 7 -> a.q8Hours; 8 -> a.q9Feeling; else -> -1
}

private fun writeAnswer(a: OnboardingAnswers, qIdx: Int, value: Int): OnboardingAnswers = when (qIdx) {
    0 -> a.copy(q1Sleep = value); 1 -> a.copy(q2Screen = value); 2 -> a.copy(q3Focus = value)
    3 -> a.copy(q4Start = value); 4 -> a.copy(q5PrepStyle = value); 5 -> a.copy(q6WhyClear = value)
    6 -> a.copy(q7Recall = value); 7 -> a.copy(q8Hours = value); 8 -> a.copy(q9Feeling = value)
    else -> a
}

// ==================== Screen 13 — Processing ====================

@Composable
private fun ProcessingStep(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    val phrases = listOf(
        Str.obProcessing1(lang),
        Str.obProcessing2(lang),
        Str.obProcessing3(lang)
    )
    var idx by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        // Show three phases, ~0.9 s each, then advance.
        for (i in 1 until phrases.size) {
            delay(900)
            idx = i
        }
        delay(900)
        viewModel.onbNext()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spinner()
            Spacer(Modifier.height(18.dp))
            AnimatedContent(
                targetState = phrases[idx],
                transitionSpec = { fadeIn(tween(220)).togetherWith(fadeOut(tween(180))) },
                label = "proc"
            ) { p ->
                Text(
                    text = p,
                    color = AppTheme.colors.textSecondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun Spinner() {
    val transition = rememberInfiniteTransition(label = "spin")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing)
        ),
        label = "angle"
    )
    val trackColor = AppTheme.colors.insetControlBackground
    val accentColor = AppTheme.colors.accentPrimary
    Canvas(modifier = Modifier.size(36.dp)) {
        val stroke = 3f
        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = stroke)
        )
        rotate(angle) {
            drawArc(
                color = accentColor,
                startAngle = 0f,
                sweepAngle = 90f,
                useCenter = false,
                style = Stroke(width = stroke)
            )
        }
    }
}

// ==================== Screen 14 — Observations ====================

@Composable
private fun ObservationsStep(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 60.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        StepTitle(Str.obObservationsTitle(lang))
        Spacer(Modifier.height(2.dp))
        ObservationCard(text = Str.obObservation1(lang))
        ObservationCard(text = Str.obObservation2(lang))
        ObservationCard(text = Str.obObservation3(lang))
        Spacer(Modifier.height(6.dp))
        PrimaryButton(
            text = Str.obContinue(lang),
            enabled = true,
            onClick = { viewModel.onbNext() }
        )
    }
}

@Composable
private fun ObservationCard(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Text(
            text = text,
            color = AppTheme.colors.textTertiary,
            fontSize = 13.sp,
            lineHeight = 20.sp
        )
    }
}

// ==================== Screens 15–17 — Mini-session ====================

@Composable
private fun MiniIntroStep(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 68.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepTitle(Str.obMiniIntroTitle(lang), align = TextAlign.Center)
        Spacer(Modifier.height(10.dp))
        Text(
            text = Str.obMiniIntroSub(lang),
            color = AppTheme.colors.textSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(28.dp))
        PrimaryButton(text = Str.obMiniStart(lang), enabled = true, onClick = { viewModel.onbNext() })
    }
}

@Composable
private fun MiniQuestionStep(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    val canProceed = state.onbMiniAnswer.isNotBlank()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 68.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = Str.obMiniQuestion(lang),
            color = AppTheme.colors.textPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 25.sp
        )
        LabeledField(
            label = "",
            placeholder = Str.obMiniPlaceholder(lang),
            value = state.onbMiniAnswer,
            onValueChange = { viewModel.updateOnbMiniAnswer(it) },
            minLines = 4
        )
        Spacer(Modifier.height(4.dp))
        PrimaryButton(
            text = Str.onbNext(lang),
            enabled = canProceed,
            onClick = { viewModel.onbNext() }
        )
    }
}

@Composable
private fun MiniDoneStep(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 68.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(AppTheme.colors.activeSegmentBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = AppTheme.colors.accentText,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(Modifier.height(18.dp))
        StepTitle(Str.obMiniDoneTitle(lang), align = TextAlign.Center)
        Spacer(Modifier.height(10.dp))
        Text(
            text = Str.obMiniDoneSub(lang),
            color = AppTheme.colors.textSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(28.dp))
        PrimaryButton(text = Str.obContinue(lang), enabled = true, onClick = { viewModel.onbNext() })
    }
}

// ==================== Screen 18 — Done ====================

@Composable
private fun DoneStep(state: AppState, viewModel: AppViewModel) {
    val lang = state.appLanguage
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp)
            .padding(top = 80.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SapfitLogo(isDark = state.isDarkTheme, modifier = Modifier.size(72.dp))
        Spacer(Modifier.height(20.dp))
        StepTitle(Str.obDoneTitle(lang), align = TextAlign.Center)
        Spacer(Modifier.height(10.dp))
        Text(
            text = Str.obDoneSub(lang),
            color = AppTheme.colors.textSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(36.dp))
        PrimaryButton(text = Str.obDoneCta(lang), enabled = true, onClick = { viewModel.onbFinish() })
    }
}

// ==================== Shared building blocks ====================

@Composable
private fun StepTitle(text: String, align: TextAlign = TextAlign.Start) {
    Text(
        text = text,
        color = AppTheme.colors.textPrimary,
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 30.sp,
        textAlign = align,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun StepSubtitle(text: String) {
    Text(
        text = text,
        color = AppTheme.colors.textSecondary,
        fontSize = 13.sp,
        lineHeight = 19.sp
    )
}

@Composable
private fun CheckboxRow(checked: Boolean, label: String, onToggle: () -> Unit) {
    val boxColor by animateColorAsState(
        targetValue = if (checked) AppTheme.colors.accentPrimary else AppTheme.colors.cardBackground,
        animationSpec = tween(180),
        label = "cb_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (checked) AppTheme.colors.accentPrimary else AppTheme.colors.borderDefault,
        animationSpec = tween(180),
        label = "cb_border"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onToggle() }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(boxColor)
                .border(1.dp, borderColor, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = AppTheme.colors.onAccent,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text = label,
            color = AppTheme.colors.textSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
        )
    }
}

@Composable
private fun SelectableRow(selected: Boolean, label: String, onClick: () -> Unit) {
    val bg by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.activeSegmentBackground else AppTheme.colors.cardBackground,
        animationSpec = tween(180),
        label = "row_bg"
    )
    val border by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.activeSegmentBorder else AppTheme.colors.borderDefault,
        animationSpec = tween(180),
        label = "row_border"
    )
    val text by animateColorAsState(
        targetValue = if (selected) AppTheme.colors.accentText else AppTheme.colors.textPrimary,
        animationSpec = tween(180),
        label = "row_text"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(0.5.dp, border, RoundedCornerShape(14.dp))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(
            text = label,
            color = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun LabeledField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    minLines: Int = 1
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
                Text(
                    text = placeholder,
                    color = AppTheme.colors.textMuted,
                    fontSize = 14.sp
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    color = AppTheme.colors.textPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                cursorBrush = SolidColor(AppTheme.colors.accentPrimary),
                minLines = minLines,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PrimaryButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    val bg by animateColorAsState(
        targetValue = if (enabled) AppTheme.colors.accentPrimary else AppTheme.colors.insetControlBackground,
        animationSpec = tween(200),
        label = "pb_bg"
    )
    val fg by animateColorAsState(
        targetValue = if (enabled) AppTheme.colors.onAccent else AppTheme.colors.textMuted,
        animationSpec = tween(200),
        label = "pb_fg"
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
private fun SecondaryButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    val fg by animateColorAsState(
        targetValue = if (enabled) AppTheme.colors.textPrimary else AppTheme.colors.textMuted,
        animationSpec = tween(200),
        label = "sb_fg"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AppTheme.colors.cardBackground)
            .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(20.dp))
            .clickable(enabled = enabled) { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = fg, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
