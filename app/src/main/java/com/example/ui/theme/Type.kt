package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

// YekanNewFace FontFamily mapped with delicate & refined weights
// Mapping Light/Regular/Medium/Bold carefully so the typography looks sleek, airy, and refined
val YekanNewFaceFontFamily = FontFamily(
    Font(R.font.yekan_new_face_thin, FontWeight.Thin),
    Font(R.font.yekan_new_face_light, FontWeight.ExtraLight),
    Font(R.font.yekan_new_face_light, FontWeight.Light),
    Font(R.font.yekan_new_face_regular, FontWeight.Normal),
    Font(R.font.yekan_new_face_regular, FontWeight.Medium),
    Font(R.font.yekan_new_face_medium, FontWeight.SemiBold),
    Font(R.font.yekan_new_face_medium, FontWeight.Bold),
    Font(R.font.yekan_new_face_bold, FontWeight.ExtraBold),
    Font(R.font.yekan_new_face_black, FontWeight.Black)
)

val TandisTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = YekanNewFaceFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.2).sp,
        color = TandisTextPrimary
    ),
    displayMedium = TextStyle(
        fontFamily = YekanNewFaceFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.15).sp,
        color = TandisTextPrimary
    ),
    headlineMedium = TextStyle(
        fontFamily = YekanNewFaceFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        color = TandisTextPrimary
    ),
    titleLarge = TextStyle(
        fontFamily = YekanNewFaceFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = TandisTextPrimary
    ),
    titleMedium = TextStyle(
        fontFamily = YekanNewFaceFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.5.sp,
        lineHeight = 22.sp,
        color = TandisTextPrimary
    ),
    titleSmall = TextStyle(
        fontFamily = YekanNewFaceFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.5.sp,
        lineHeight = 20.sp,
        color = TandisTextPrimary
    ),
    bodyLarge = TextStyle(
        fontFamily = YekanNewFaceFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        color = TandisTextPrimary
    ),
    bodyMedium = TextStyle(
        fontFamily = YekanNewFaceFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 12.5.sp,
        lineHeight = 19.sp,
        color = TandisTextSecondary
    ),
    bodySmall = TextStyle(
        fontFamily = YekanNewFaceFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 11.5.sp,
        lineHeight = 17.sp,
        color = TandisTextSecondary
    ),
    labelLarge = TextStyle(
        fontFamily = YekanNewFaceFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 18.sp
    ),
    labelMedium = TextStyle(
        fontFamily = YekanNewFaceFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.5.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = YekanNewFaceFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 10.5.sp,
        lineHeight = 15.sp
    )
)
