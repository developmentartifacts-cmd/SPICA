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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ===== DESIGN CONSTANTS =====
private val NavBarHeight = 72.dp
private val NavBarCorner = 36.dp
private val NavBarHorizontalMargin = 10.dp
private val UnselectedTabSize = 60.dp
private val UnselectedIconSize = 24.dp
private val SelectedPillHeight = 60.dp
private val SelectedPillWidth = 140.dp
private val SelectedPillCorner = 30.dp
private val SelectedIconCircle = 50.dp
private val SelectedIconSize = 24.dp
private val SelectedIconTextGap = 6.dp
private val SelectedTextSize = 14.sp
private val MaxNavBarWidth = 390.dp

// ===== MAIN BOTTOM NAV ITEMS =====
data class NavItem(val route: String, val label: String)

val MainNavItems = listOf(
    NavItem(route = "home", label = "Home"),
    NavItem(route = "exchange", label = "Exchange"),  // Marketplace → Exchange
    NavItem(route = "portfolio", label = "Portfolio"),
    NavItem(route = "watchlist", label = "Watchlist")
)

@Composable
fun AppNavBar(
    currentRoute: String?,
    onItemClick: (route: String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    val navBackground = colorScheme.surface
    val selectedPillBackground = colorScheme.onSurface.copy(alpha = 0.1f)
    val selectedCircle = colorScheme.primary
    val selectedIconTint = colorScheme.onPrimary
    val selectedTextColor = colorScheme.onSurface

    val unselectedCircle = colorScheme.onSurface.copy(alpha = 0.1f)
    val unselectedIconTint = colorScheme.onSurface.copy(alpha = 0.6f)

    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Public,     // Exchange
        Icons.Default.Wallet,     // Portfolio
        Icons.Default.PieChart    // Watchlist
    )

    val routes = MainNavItems.map { it.route }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = NavBarHorizontalMargin),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = MaxNavBarWidth)
                .fillMaxWidth()
                .height(NavBarHeight)
                .clip(RoundedCornerShape(NavBarCorner))
                .background(navBackground),
            color = navBackground
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
                                    .background(selectedPillBackground)
                                    .padding(horizontal = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(SelectedIconCircle)
                                        .clip(CircleShape)
                                        .background(selectedCircle),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icons[index],
                                        contentDescription = null,
                                        tint = selectedIconTint,
                                        modifier = Modifier.size(SelectedIconSize)
                                    )
                                }

                                Spacer(modifier = Modifier.width(SelectedIconTextGap))

                                Text(
                                    text = MainNavItems[index].label,
                                    color = selectedTextColor,
                                    fontSize = SelectedTextSize
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(UnselectedTabSize)
                                    .clip(CircleShape)
                                    .background(unselectedCircle),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icons[index],
                                    contentDescription = null,
                                    tint = unselectedIconTint,
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
