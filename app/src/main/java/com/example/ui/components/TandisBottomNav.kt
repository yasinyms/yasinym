package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TandisBorderLight
import com.example.ui.theme.TandisPrimary
import com.example.ui.theme.TandisPrimaryDark
import com.example.ui.theme.TandisSurface
import com.example.ui.theme.TandisTextPrimary
import com.example.ui.theme.TandisTextSecondary

sealed class BottomNavItem(
    val id: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "خانه", Icons.Default.Home)
    object Categories : BottomNavItem("categories", "دسته‌بندی‌ها", Icons.Default.GridView)
    object Support : BottomNavItem("support", "پشتیبانی", Icons.Default.HeadsetMic)
    object Profile : BottomNavItem("profile", "حساب من", Icons.Default.Person)
}

@Composable
fun TandisBottomNav(
    activeTabId: String,
    isLoggedIn: Boolean,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Categories,
        BottomNavItem.Support,
        BottomNavItem.Profile
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("mobile_bottom_navigation"),
        color = TandisSurface.copy(alpha = 0.98f),
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, TandisBorderLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEach { item ->
                val isActive = activeTabId == item.id
                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onTabSelected(item.id) }
                        .testTag("bottom_nav_${item.id}")
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (isActive) TandisPrimaryDark else TandisTextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = if (item.id == "profile" && !isLoggedIn) "ورود" else item.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isActive) FontWeight.Medium else FontWeight.Light,
                            color = if (isActive) TandisPrimaryDark else TandisTextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    if (isActive) {
                        Box(
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(TandisPrimary)
                        )
                    }
                }
            }
        }
    }
}
