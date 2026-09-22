package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MainTab
import com.example.model.ProjectProgress
import com.example.ui.theme.*

// 1. Capsule Navigation Bar (Liquid Glass style)
@Composable
fun CapsuleNavigationBar(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    appLanguage: com.example.data.AppLanguage = com.example.data.AppLanguage.RU,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(32.dp), spotColor = Color.Black)
                .clip(RoundedCornerShape(32.dp))
                .background(AppTheme.colors.navBackground)
                .border(
                    width = 0.5.dp,
                    color = AppTheme.colors.navBorder,
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val tabs = listOf(
                Triple(MainTab.HOME, com.example.i18n.Str.tabHome(appLanguage), Icons.Filled.Home),
                Triple(MainTab.PROJECTS, com.example.i18n.Str.tabProjects(appLanguage), Icons.Filled.Folder),
                Triple(MainTab.ABOUT, com.example.i18n.Str.tabAbout(appLanguage), Icons.Filled.BarChart),
                Triple(MainTab.PROFILE, com.example.i18n.Str.tabProfile(appLanguage), Icons.Filled.Person)
            )

            tabs.forEach { (tab, label, icon) ->
                val isSelected = currentTab == tab
                val weight by animateFloatAsState(
                    targetValue = if (isSelected) 1.6f else 1.0f,
                    animationSpec = tween(300, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                    label = "tab_weight_${tab.name}"
                )
                NavTabItem(
                    label = label,
                    icon = icon,
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    testTag = "nav_tab_${tab.name.lowercase()}",
                    modifier = Modifier.weight(weight)
                )
            }
        }
    }
}

@Composable
private fun NavTabItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (selected) AppTheme.colors.accentPrimary else Color.Transparent,
        animationSpec = tween(durationMillis = 220),
        label = "nav_bg_color"
    )
    val contentColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (selected) AppTheme.colors.onAccent else AppTheme.colors.textFaint,
        animationSpec = tween(durationMillis = 220),
        label = "nav_content_color"
    )
    
    Box(
        modifier = modifier
            .testTag(testTag)
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
            AnimatedVisibility(
                visible = selected,
                enter = fadeIn(animationSpec = tween(200)) + expandHorizontally(animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(200)) + shrinkHorizontally(animationSpec = tween(200))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = label,
                        color = contentColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// 2. 3-Color Progress Bar
@Composable
fun ProgressBar3Color(
    progress: ProjectProgress,
    modifier: Modifier = Modifier,
    barHeight: Int = 8
) {
    val total = (progress.locked + progress.learning + progress.ahead).coerceAtLeast(1)
    val lockedWeight = progress.locked.toFloat() / total
    val learningWeight = progress.learning.toFloat() / total
    val aheadWeight = progress.ahead.toFloat() / total

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight.dp)
                .clip(RoundedCornerShape((barHeight / 2).dp))
                .background(AppTheme.colors.insetControlBackground)
        ) {
            if (lockedWeight > 0) {
                Box(
                    modifier = Modifier
                        .weight(lockedWeight)
                        .fillMaxHeight()
                        .background(AppTheme.colors.progressLocked)
                )
            }
            if (learningWeight > 0) {
                Box(
                    modifier = Modifier
                        .weight(learningWeight)
                        .fillMaxHeight()
                        .background(AppTheme.colors.progressLearning)
                )
            }
            if (aheadWeight > 0) {
                Box(
                    modifier = Modifier
                        .weight(aheadWeight)
                        .fillMaxHeight()
                        .background(AppTheme.colors.progressAhead)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(color = AppTheme.colors.progressLocked, label = "Закреплено ${progress.locked}%")
            LegendItem(color = AppTheme.colors.progressLearning, label = "Изучаем ${progress.learning}%")
            LegendItem(color = AppTheme.colors.progressAhead, label = "Впереди ${progress.ahead}%")
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = AppTheme.colors.textMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun SapfitLogo(
    isDark: Boolean,
    modifier: Modifier = Modifier,
    showBackground: Boolean = true
) {
    val bgColor = if (isDark) Color(0xFF12131A) else Color(0xFFFFFFFF)
    val ring1Color = if (isDark) Color(0xFFFFFFFF) else Color(0xFF000000) // Inner arc & dot (Black in light, White in dark)
    val ring2Color = if (isDark) Color(0xFF8A8F9B) else Color(0xFF595B60) // Middle arc (Slate Grey)
    val ring3Color = if (isDark) Color(0xFF383A43) else Color(0xFFB8BABF) // Outer arc (Light Silver / Dark Grey)

    Box(
        modifier = modifier
            .then(
                if (showBackground) {
                    Modifier
                        .clip(RoundedCornerShape(22.dp))
                        .background(bgColor)
                        .border(
                            width = 0.5.dp,
                            color = if (isDark) Color(0xFF282B36) else Color(0xFFE5E7EB),
                            shape = RoundedCornerShape(22.dp)
                        )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            val canvasSize = size.minDimension
            val cx = size.width / 2f
            val cy = size.height / 2f
            val strokeWidth = canvasSize * 0.075f

            // Center Dot
            drawCircle(
                color = ring1Color,
                radius = canvasSize * 0.085f,
                center = Offset(cx, cy)
            )

            // Inner Ring Arc (Ring 1)
            val r1 = canvasSize * 0.22f
            drawArc(
                color = ring1Color,
                startAngle = 45f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(cx - r1, cy - r1),
                size = Size(r1 * 2f, r1 * 2f),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Middle Ring Arc (Ring 2)
            val r2 = canvasSize * 0.33f
            drawArc(
                color = ring2Color,
                startAngle = 45f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(cx - r2, cy - r2),
                size = Size(r2 * 2f, r2 * 2f),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Outer Ring Arc (Ring 3)
            val r3 = canvasSize * 0.44f
            drawArc(
                color = ring3Color,
                startAngle = 45f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(cx - r3, cy - r3),
                size = Size(r3 * 2f, r3 * 2f),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}

