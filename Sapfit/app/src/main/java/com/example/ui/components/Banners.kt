package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExplainerBanner
import com.example.model.ExplainerSection
import com.example.model.ReminderBanner
import com.example.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlin.math.abs

private const val REMINDER_AUTO_DISMISS_MS = 6000L

/**
 * Home-screen reminder banners. Behaviour (spec §13):
 * - Auto-dismiss after ~6 seconds (per banner, independently).
 * - Swipe-up gesture dismisses immediately.
 * - Stacked visually (top bright, ones below dim + scaled — iOS lockscreen style).
 * - Tap opens the linked project.
 * - No X button.
 */
@Composable
fun ReminderBannerStack(
    banners: List<ReminderBanner>,
    onBannerClick: (ReminderBanner) -> Unit,
    onDismiss: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (banners.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        banners.take(2).forEachIndexed { index, banner ->
            key(banner.id) {
                ReminderBannerItem(
                    banner = banner,
                    isStacked = index >= 1,
                    onClick = { onBannerClick(banner) },
                    onDismiss = { onDismiss(banner.id) }
                )
            }
        }
    }
}

@Composable
private fun ReminderBannerItem(
    banner: ReminderBanner,
    isStacked: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    var visible by remember(banner.id) { mutableStateOf(true) }
    var dragOffset by remember(banner.id) { mutableFloatStateOf(0f) }

    // Auto-dismiss: after 6s ask the exit animation to play.
    LaunchedEffect(banner.id) {
        delay(REMINDER_AUTO_DISMISS_MS)
        visible = false
    }
    // When visible flips to false (auto or swipe), wait for the exit animation, then dismiss.
    LaunchedEffect(visible) {
        if (!visible) {
            delay(220)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(220)) + slideInVertically(tween(220)) { -it / 3 },
        exit = fadeOut(tween(180)) + slideOutVertically(tween(180)) { -it }
    ) {
        val stackedAlpha = if (isStacked) 0.7f else 1f
        val stackedScale = if (isStacked) 0.97f else 1f
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = dragOffset
                    alpha = stackedAlpha * (1f - (abs(dragOffset) / 200f).coerceIn(0f, 1f))
                    scaleX = stackedScale
                    scaleY = stackedScale
                }
                .clip(RoundedCornerShape(22.dp))
                .background(AppTheme.colors.cardBackground)
                .border(0.5.dp, AppTheme.colors.borderDefault, RoundedCornerShape(22.dp))
                .pointerInput(banner.id) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            if (dragOffset < -60f) visible = false else dragOffset = 0f
                        },
                        onVerticalDrag = { _, dy ->
                            dragOffset = (dragOffset + dy).coerceAtMost(0f)
                        }
                    )
                }
                .clickable { onClick() }
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Column {
                Text(
                    text = banner.title,
                    color = AppTheme.colors.textPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = banner.text,
                    color = AppTheme.colors.textSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

/**
 * Explainer banner (spec §13): shown once on a tab's first visit,
 * stays until manually dismissed. Left indigo accent bar, swipe-up to hide,
 * X icon as a fallback.
 */
@Composable
fun ExplainerBannerView(
    banner: ExplainerBanner,
    onDismiss: (ExplainerSection) -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember(banner.section) { mutableStateOf(true) }
    var dragOffset by remember(banner.section) { mutableFloatStateOf(0f) }

    LaunchedEffect(visible) {
        if (!visible) {
            delay(210)
            onDismiss(banner.section)
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(240)) + slideInVertically(tween(240)) { -it / 4 },
        exit = fadeOut(tween(200)) + slideOutVertically(tween(200)) { -it }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = dragOffset
                    alpha = 1f - (abs(dragOffset) / 200f).coerceIn(0f, 1f)
                }
                .clip(RoundedCornerShape(18.dp))
                .background(AppTheme.colors.activeSegmentBackground)
                .border(0.5.dp, AppTheme.colors.activeSegmentBorder, RoundedCornerShape(18.dp))
                .pointerInput(banner.section) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            if (dragOffset < -60f) visible = false else dragOffset = 0f
                        },
                        onVerticalDrag = { _, dy ->
                            dragOffset = (dragOffset + dy).coerceAtMost(0f)
                        }
                    )
                }
                .padding(start = 14.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(38.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(AppTheme.colors.accentPrimary)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = banner.title,
                    color = AppTheme.colors.accentText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = banner.text,
                    color = AppTheme.colors.textSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable { visible = false },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = AppTheme.colors.textMuted,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
