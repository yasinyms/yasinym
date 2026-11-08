package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.theme.TandisBorderLight
import com.example.ui.theme.TandisDanger
import com.example.ui.theme.TandisPrimary
import com.example.ui.theme.TandisPrimaryDark
import com.example.ui.theme.TandisPrimaryLight
import com.example.ui.theme.TandisSecondary
import com.example.ui.theme.TandisSurface
import com.example.ui.theme.TandisSurfaceVariant
import com.example.ui.theme.TandisTextMuted
import com.example.ui.theme.TandisTextPrimary
import com.example.ui.theme.TandisTextSecondary

@Composable
fun TandisSideDrawer(
    userProfile: UserProfile,
    currentCity: String,
    onClose: () -> Unit,
    onCityClick: () -> Unit,
    onAuthClick: () -> Unit,
    onPostAdClick: () -> Unit,
    onSupportClick: () -> Unit,
    onRulesClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier
            .width(310.dp)
            .fillMaxHeight()
            .testTag("side_drawer_sheet"),
        drawerContainerColor = TandisSurface,
        drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Header with Brand and Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(TandisPrimaryDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "ت",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TandisSecondary,
                                    fontSize = 18.sp
                                )
                            )
                        }
                        Column {
                            Text(
                                text = "تندیس",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TandisTextPrimary
                                )
                            )
                            Text(
                                text = "سوپراپلیکیشن نیازمندی‌ها",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TandisTextMuted,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "بستن",
                            tint = TandisTextMuted
                        )
                    }
                }

                HorizontalDivider(color = TandisBorderLight)

                // User Profile Box
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = TandisPrimaryLight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(onClick = onAuthClick)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(TandisPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "سامانه ابری تندیس",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TandisPrimaryDark
                                )
                            )
                            Text(
                                text = "متصل به پایگاه داده سرور Firebase",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TandisTextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // City Selector Item
                DrawerMenuItem(
                    icon = Icons.Default.LocationOn,
                    title = "موقعیت مکانی: $currentCity",
                    subtitle = "تغییر شهر یا استان",
                    onClick = onCityClick
                )

                // Navigation Items
                DrawerMenuItem(
                    icon = Icons.Default.AddCircle,
                    title = "ثبت رایگان آگهی جدید",
                    subtitle = "خودرو، املاک، کالا، استخدام",
                    iconColor = TandisSecondary,
                    onClick = onPostAdClick
                )

                DrawerMenuItem(
                    icon = Icons.Default.GridView,
                    title = "بازار آگهی‌ها و نیازمندی‌ها",
                    subtitle = "مرور دسته‌بندی‌های کالا و خدمات",
                    onClick = onClose
                )

                DrawerMenuItem(
                    icon = Icons.Default.Build,
                    title = "بانک اصناف و متخصصین",
                    subtitle = "ثبت‌نام و عضویت ویژه کسب‌وکارها",
                    onClick = onClose
                )

                DrawerMenuItem(
                    icon = Icons.Default.HeadsetMic,
                    title = "پشتیبانی و پاسخگویی ۲۴ ساعته",
                    subtitle = "ارتباط با کارشناسان پشتیبانی تندیس",
                    onClick = onSupportClick
                )

                DrawerMenuItem(
                    icon = Icons.Default.Security,
                    title = "راهنمای معامله امن و قوانین",
                    subtitle = "نکات حقوقی و حریم خصوصی",
                    onClick = onRulesClick
                )
            }

            // Bottom Actions: Logout & Version
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (userProfile.isLoggedIn) {
                    DrawerMenuItem(
                        icon = Icons.Default.Logout,
                        title = "خروج از حساب کاربری",
                        subtitle = "",
                        iconColor = TandisDanger,
                        onClick = onLogoutClick
                    )
                }

                HorizontalDivider(color = TandisBorderLight)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "نسخه ۱.۰.۰ تندیس",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TandisTextMuted,
                            fontSize = 10.sp
                        )
                    )
                    Text(
                        text = "طراحی شده برای اندروید",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TandisTextMuted,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color = TandisPrimary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TandisTextPrimary
                )
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TandisTextMuted,
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}
