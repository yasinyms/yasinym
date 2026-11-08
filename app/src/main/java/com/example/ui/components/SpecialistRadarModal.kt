package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ServiceRequestItem
import com.example.network.TandisServerClient
import com.example.ui.theme.TandisBackground
import com.example.ui.theme.TandisBorderLight
import com.example.ui.theme.TandisDanger
import com.example.ui.theme.TandisPrimary
import com.example.ui.theme.TandisPrimaryDark
import com.example.ui.theme.TandisPrimaryDeep
import com.example.ui.theme.TandisPrimaryLight
import com.example.ui.theme.TandisSecondary
import com.example.ui.theme.TandisSuccess
import com.example.ui.theme.TandisSurface
import com.example.ui.theme.TandisSurfaceVariant
import com.example.ui.theme.TandisTextMuted
import com.example.ui.theme.TandisTextPrimary
import com.example.ui.theme.TandisTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialistRadarModal(
    requests: List<ServiceRequestItem>,
    onAcceptRequest: (ServiceRequestItem) -> Unit,
    onRefreshFromServer: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isOnline by remember { mutableStateOf(true) }
    var selectedPinRequest by remember { mutableStateOf<ServiceRequestItem?>(requests.firstOrNull()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = TandisSurface,
        modifier = modifier.testTag("specialist_radar_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 26.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top Header: Specialist App Mode & Server Connection Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(TandisPrimaryDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Radar,
                            contentDescription = null,
                            tint = TandisSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "پنل و رادار متخصصین تندیس (نرم‌افزار دوم)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TandisTextPrimary
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = null,
                                tint = TandisSuccess,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "متصل به سرور: 62.3.42.195 (PostGIS)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TandisTextSecondary,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onRefreshFromServer,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "همگام‌سازی با سرور",
                            tint = TandisPrimary
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "بستن",
                            tint = TandisTextMuted
                        )
                    }
                }
            }

            // Online Status Bar
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isOnline) TandisPrimaryLight else TandisSurfaceVariant,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isOnline) TandisPrimary.copy(alpha = 0.3f) else TandisBorderLight
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = if (isOnline) TandisPrimary else TandisTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = if (isOnline) "وضعیت متخصص: آنلاین (آماده دریافت نوتیفیکیشن)" else "وضعیت متخصص: آفلاین",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = TandisTextPrimary
                                )
                            )
                            Text(
                                text = "شعاع پوشش مکانی PostGIS: ۱۰ کیلومتر اطراف شما",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TandisTextSecondary,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Switch(
                        checked = isOnline,
                        onCheckedChange = { isOnline = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = TandisPrimary
                        )
                    )
                }
            }

            // Spatial Map Radar Preview (PostGIS Map Visualization)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = TandisPrimaryDeep)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Radar Canvas (Grid + Spatial Distance Rings + Pins)
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val strokeEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                        // Spatial Distance Rings (2km, 5km, 10km)
                        listOf(40.dp.toPx(), 75.dp.toPx(), 115.dp.toPx()).forEach { radius ->
                            drawCircle(
                                color = Color(0xFF0A8387).copy(alpha = 0.45f),
                                radius = radius,
                                center = center,
                                style = Stroke(width = 1.5f, pathEffect = strokeEffect)
                            )
                        }

                        // Specialist Center Point
                        drawCircle(
                            color = Color(0xFFF2A900).copy(alpha = 0.25f),
                            radius = 18.dp.toPx(),
                            center = center
                        )
                        drawCircle(
                            color = Color(0xFFF2A900),
                            radius = 6.dp.toPx(),
                            center = center
                        )

                        // Customer Request Pins around Specialist
                        val pinOffsets = listOf(
                            Offset(center.x + 55.dp.toPx(), center.y - 32.dp.toPx()),
                            Offset(center.x - 68.dp.toPx(), center.y + 24.dp.toPx()),
                            Offset(center.x + 25.dp.toPx(), center.y + 45.dp.toPx())
                        )

                        requests.take(3).forEachIndexed { idx, _ ->
                            val pos = pinOffsets[idx % pinOffsets.size]
                            drawLine(
                                color = Color.White.copy(alpha = 0.3f),
                                start = center,
                                end = pos,
                                strokeWidth = 2f,
                                pathEffect = strokeEffect
                            )
                            drawCircle(
                                color = Color(0xFF10B981).copy(alpha = 0.35f),
                                radius = 14.dp.toPx(),
                                center = pos
                            )
                            drawCircle(
                                color = Color(0xFF10B981),
                                radius = 5.5.dp.toPx(),
                                center = pos
                            )
                        }
                    }

                    // Overlay Badges on Map
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.Black.copy(alpha = 0.45f),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = null,
                                tint = TandisSecondary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "نقشه فضایی PostGIS (موقعیت زنده مشتری و متخصص)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = TandisPrimary,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "${requests.size} درخواست در شعاع شما",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Incoming Customer Requests List for Specialist
            Text(
                text = "نوتیفیکیشن‌های دریافتی از مشتریان نزدیک:",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TandisTextPrimary
                )
            )

            if (requests.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = TandisBackground,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "هنوز درخواستی در محدوده شما ثبت نشده است",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TandisTextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Text(
                            text = "به محض اینکه مشتری در اپلیکیشن اول دکمه «ثبت درخواست» را بزند، نوتیفیکیشن و لوکیشن او اینجا نمایش داده می‌شود.",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TandisTextMuted
                            )
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(requests) { req ->
                        val isAccepted = req.status.contains("پذیرفته شد")
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = TandisBackground,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isAccepted) TandisSuccess.copy(alpha = 0.4f) else TandisBorderLight
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPinRequest = req }
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = req.title,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Medium,
                                            color = TandisTextPrimary,
                                            fontSize = 13.sp
                                        )
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = TandisPrimaryLight
                                    ) {
                                        Text(
                                            text = "فاصله: ۱.۸ کیلومتر",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = TandisPrimaryDark,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Medium
                                            ),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = TandisPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "آدرس و لوکیشن مشتری: ${req.address}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TandisTextSecondary,
                                            fontSize = 11.5.sp
                                        )
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = TandisTextMuted,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = req.preferredTime,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = TandisTextMuted,
                                                fontSize = 10.5.sp
                                            )
                                        )
                                    }

                                    Button(
                                        onClick = { onAcceptRequest(req) },
                                        enabled = !isAccepted,
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isAccepted) TandisSuccess else TandisPrimary,
                                            contentColor = Color.White
                                        ),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Text(
                                            text = if (isAccepted) "پذیرفته شد ✓" else "قبول درخواست و اعزام",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
