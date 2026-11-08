package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CategoryTabItem
import com.example.model.ListingItem
import com.example.ui.theme.TandisBorderLight
import com.example.ui.theme.TandisPrimary
import com.example.ui.theme.TandisPrimaryLight
import com.example.ui.theme.TandisSecondary
import com.example.ui.theme.TandisSuccess
import com.example.ui.theme.TandisSurface
import com.example.ui.theme.TandisTextMuted
import com.example.ui.theme.TandisTextPrimary
import com.example.ui.theme.TandisTextSecondary

@Composable
fun TandisListingsSection(
    listings: List<ListingItem>,
    tabs: List<CategoryTabItem>,
    selectedTabId: String,
    onTabSelected: (String) -> Unit,
    onListingClick: (ListingItem) -> Unit,
    onBookmarkToggle: (ListingItem) -> Unit,
    onRequestClick: (ListingItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("listings_section"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 4.dp, height = 22.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(TandisPrimary)
                )
                Text(
                    text = "جدیدترین آگهی‌های منتخب",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TandisTextPrimary
                    )
                )
            }
            Text(
                text = "${listings.size} آگهی فعال",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TandisTextMuted
                )
            )
        }

        // Filter Tabs
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("listings_tabs_row")
        ) {
            items(tabs) { tab ->
                val isSelected = tab.id == selectedTabId
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) TandisPrimary else TandisSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) TandisPrimary else TandisBorderLight
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onTabSelected(tab.id) }
                        .testTag("tab_item_${tab.id}")
                ) {
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Light,
                            color = if (isSelected) Color.White else TandisTextSecondary
                        ),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        // Listings List
        if (listings.isEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(20.dp),
                color = TandisSurface
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "هیچ آگهی در این دسته یافت نشد",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TandisTextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = "می‌توانید سایر دسته‌ها یا جستجوی کلی را بررسی کنید",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TandisTextMuted
                        )
                    )
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listings.forEach { listing ->
                    ListingCard(
                        listing = listing,
                        onClick = { onListingClick(listing) },
                        onBookmarkToggle = { onBookmarkToggle(listing) },
                        onRequestClick = { onRequestClick(listing) }
                    )
                }
            }
        }
    }
}

@Composable
fun ListingCard(
    listing: ListingItem,
    onClick: () -> Unit,
    onBookmarkToggle: () -> Unit,
    onRequestClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag("listing_card_${listing.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = TandisSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, TandisBorderLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image thumbnail with badge
            Box(
                modifier = Modifier
                    .size(width = 110.dp, height = 110.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = listing.imageRes),
                    contentDescription = listing.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )

                // Badge top
                listing.badgeText?.let { badge ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(listing.badgeBgColor)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            // Info details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (listing.verified) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "تایید شده",
                            tint = TandisPrimary,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Text(
                        text = listing.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = TandisTextPrimary,
                            fontSize = 13.5.sp,
                            lineHeight = 20.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Price
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = listing.priceLabel,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TandisTextMuted,
                            fontWeight = FontWeight.Light,
                            fontSize = 11.sp
                        )
                    )
                    Text(
                        text = listing.price,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TandisPrimary,
                            fontSize = 13.sp
                        )
                    )
                }

                // Location & Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = TandisTextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = listing.location,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TandisTextSecondary,
                                fontWeight = FontWeight.Light,
                                fontSize = 11.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Text(
                        text = listing.timeAgo,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TandisTextMuted,
                            fontWeight = FontWeight.Light,
                            fontSize = 10.sp
                        )
                    )
                }

                // Action buttons (Bookmark & Request instead of Phone Call)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBookmarkToggle,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("bookmark_button_${listing.id}")
                    ) {
                        Icon(
                            imageVector = if (listing.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "نشان کردن",
                            tint = if (listing.isBookmarked) TandisSecondary else TandisTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    val isRequested = listing.isRequested
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isRequested) TandisSuccess.copy(alpha = 0.14f) else TandisPrimaryLight,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(onClick = onRequestClick)
                            .testTag("request_button_${listing.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isRequested) Icons.Default.CheckCircle else Icons.Default.AssignmentTurnedIn,
                                contentDescription = if (isRequested) "درخواست ثبت شده" else "ثبت درخواست",
                                tint = if (isRequested) TandisSuccess else TandisPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (isRequested) "درخواست ثبت شد" else "ثبت درخواست",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isRequested) TandisSuccess else TandisPrimary,
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
