package com.example.ui.components

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ListingItem
import com.example.ui.theme.TandisBorderLight
import com.example.ui.theme.TandisPrimary
import com.example.ui.theme.TandisPrimaryDark
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
fun ListingDetailSheet(
    listing: ListingItem?,
    onDismiss: () -> Unit,
    onBookmarkToggle: (ListingItem) -> Unit,
    onRequestClick: (ListingItem) -> Unit,
    modifier: Modifier = Modifier
) {
    if (listing == null) return

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = TandisSurface,
        modifier = modifier.testTag("listing_detail_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Image & Close Actions
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(22.dp))
            ) {
                Image(
                    painter = painterResource(id = listing.imageRes),
                    contentDescription = listing.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )

                // Close Button
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .testTag("detail_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "بستن",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Badge top left
                listing.badgeText?.let { badge ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(listing.badgeBgColor)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            // Title & Verified
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (listing.verified) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "آگهی تایید شده",
                            tint = TandisPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = listing.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TandisTextPrimary,
                            fontSize = 16.sp,
                            lineHeight = 24.sp
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
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = TandisPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = listing.location,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TandisTextSecondary
                            )
                        )
                    }

                    Text(
                        text = listing.timeAgo,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TandisTextMuted
                        )
                    )
                }
            }

            // Price Highlight Banner
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = TandisPrimaryLight,
                border = androidx.compose.foundation.BorderStroke(1.dp, TandisPrimary.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = listing.priceLabel,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TandisTextSecondary
                        )
                    )
                    Text(
                        text = listing.price,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TandisPrimary,
                            fontSize = 15.5.sp
                        )
                    )
                }
            }

            // Post-Request Status Banner (if already requested)
            if (listing.isRequested) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = TandisSuccess.copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TandisSuccess.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = TandisSuccess,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "درخواست شما برای این آگهی ثبت شده است",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = TandisPrimaryDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            listing.requestTrackingCode?.let { code ->
                                Text(
                                    text = "کد پیگیری: $code — در انتظار تایید و هماهنگی",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TandisTextSecondary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Specs Table (if available)
            if (listing.specs.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "مشخصات فنی و ویژگی‌ها",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TandisTextPrimary
                        )
                    )

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = TandisSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listing.specs.forEachIndexed { index, spec ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = spec.label,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TandisTextSecondary
                                        )
                                    )
                                    Text(
                                        text = spec.value,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Medium,
                                            color = TandisTextPrimary
                                        )
                                    )
                                }
                                if (index < listing.specs.lastIndex) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(TandisBorderLight)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Full Description
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "توضیحات تکمیلی آگهی",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TandisTextPrimary
                    )
                )
                Text(
                    text = listing.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TandisTextSecondary,
                        lineHeight = 22.sp
                    )
                )
            }

            // Action Buttons: Request, Bookmark, Share (No Phone Number)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onRequestClick(listing) },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("detail_request_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (listing.isRequested) TandisSuccess else TandisPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (listing.isRequested) Icons.Default.CheckCircle else Icons.Default.AssignmentTurnedIn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (listing.isRequested) "مشاهده وضعیت درخواست" else "ثبت درخواست هماهنگی / سفارش",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                // Bookmark
                IconButton(
                    onClick = { onBookmarkToggle(listing) },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(TandisSurfaceVariant)
                        .testTag("detail_bookmark_toggle")
                ) {
                    Icon(
                        imageVector = if (listing.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "نشان کردن",
                        tint = if (listing.isBookmarked) TandisSecondary else TandisTextSecondary
                    )
                }

                // Share
                IconButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "${listing.title}\n${listing.price}\nمشاهده در تندیس: https://tandis.ir"
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "اشتراک‌گذاری آگهی"))
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(TandisSurfaceVariant)
                        .testTag("detail_share_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "اشتراک گذاری",
                        tint = TandisTextSecondary
                    )
                }
            }
        }
    }
}
