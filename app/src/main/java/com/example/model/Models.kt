package com.example.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryItem(
    val id: String,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val iconColor: Color,
    val bgColor: Color,
    val isProminent: Boolean = false,
    val badge: String? = null
)

data class SpecItem(
    val label: String,
    val value: String
)

data class ListingItem(
    val id: String,
    val title: String,
    val categoryId: String,
    val price: String,
    val priceLabel: String = "قیمت:",
    val location: String,
    val timeAgo: String,
    @param:DrawableRes val imageRes: Int,
    val badgeText: String? = null,
    val badgeBgColor: Color = Color(0xFF0A8387),
    val verified: Boolean = false,
    val featured: Boolean = false,
    val description: String,
    val phone: String = "",
    val specs: List<SpecItem> = emptyList(),
    val isBookmarked: Boolean = false,
    val isRequested: Boolean = false,
    val requestTrackingCode: String? = null
)

data class ServiceRequestItem(
    val id: String,
    val title: String,
    val categoryName: String,
    val address: String,
    val preferredTime: String,
    val description: String,
    val customerPhone: String,
    val trackingCode: String,
    val status: String = "در انتظار تایید متخصص / آگهی‌دهنده"
)

data class HeroSlideItem(
    val id: Int,
    val tag: String,
    val title: String,
    val description: String,
    val primaryAction: String,
    val secondaryAction: String,
    @param:DrawableRes val imageRes: Int,
    val gradientStart: Color,
    val gradientEnd: Color
)

data class CityItem(
    val name: String,
    val isSelected: Boolean = false
)

data class CategoryTabItem(
    val id: String,
    val label: String
)

data class UserProfile(
    val isLoggedIn: Boolean = false,
    val name: String = "کاربر مهمان",
    val phone: String = "",
    val role: String = "عضو رسمی (مشتری)"
)
