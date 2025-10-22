package com.gibson.spica.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ===== DESIGN CONSTANTS =====
private val NavBarBackground = Color(0xFF1C1C1E)
private val SelectedGreen = Color(0xFFAEF359)
private val UnselectedGray = Color(0xFF333333)
private val SelectedIconTint = Color.Black
private val UnselectedIconTint = Color.White
private val TextColor = Color.White

private val NavBarHeight = 72.dp
private val NavBarCorner = 36.dp
private val NavBarHorizontalMargin = 10.dp // ✅ tighter side spacing
private val UnselectedTabSize = 60.dp
private val UnselectedIconSize = 24.dp
private val SelectedPillHeight = 60.dp
private val SelectedPillWidth = 140.dp
private val SelectedPillCorner = 30.dp
private val SelectedIconCircle = 50.dp
private val SelectedIconSize = 24.dp
private val SelectedIconTextGap = 6.dp
private val SelectedTextSize = 14.sp
private val MaxNavBarWidth = 390.dp // ✅ fixed max width

@Composable
actual fun AppNavBar(currentRoute: String?, onItemClick: (route: String) -> Unit) {
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Public,
        Icons.Default.Wallet,
        Icons.Default.PieChart
    )

    val routes = listOf(
        MainNavItems[0].route,
        MainNavItems[1].route,
        MainNavItems[2].route,
        MainNavItems[3].route
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = NavBarHorizontalMargin), // ✅ space from screen edges
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = MaxNavBarWidth) // ✅ keep width within 390.dp even on big screens
                .fillMaxWidth() // only fills up to 390dp center-aligned
                .height(NavBarHeight)
                .clip(RoundedCornerShape(NavBarCorner)) // full rounded pill
                .background(NavBarBackground),
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                routes.forEachIndexed { index, route ->
                    val selected = currentRoute == route

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(SelectedPillCorner))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onItemClick(route) }
                            .height(SelectedPillHeight),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selected) {
                            Row(
                                modifier = Modifier
                                    .width(SelectedPillWidth)
                                    .height(SelectedPillHeight)
                                    .clip(RoundedCornerShape(SelectedPillCorner))
                                    .background(UnselectedGray.copy(alpha = 0.45f))
                                    .padding(horizontal = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(SelectedIconCircle)
                                        .clip(CircleShape)
                                        .background(SelectedGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icons[index],
                                        contentDescription = null,
                                        tint = SelectedIconTint,
                                        modifier = Modifier.size(SelectedIconSize)
                                    )
                                }

                                Spacer(modifier = Modifier.width(SelectedIconTextGap))

                                Text(
                                    text = MainNavItems.getOrNull(index)?.label ?: "",
                                    color = TextColor,
                                    fontSize = SelectedTextSize
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(UnselectedTabSize)
                                    .clip(CircleShape)
                                    .background(UnselectedGray),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icons[index],
                                    contentDescription = null,
                                    tint = UnselectedIconTint,
                                    modifier = Modifier.size(UnselectedIconSize)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
