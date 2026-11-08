package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TandisData
import com.example.model.ServiceRequestItem
import com.example.network.TandisServerClient
import com.example.ui.theme.TandisBackground
import com.example.ui.theme.TandisBorderLight
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
import kotlinx.coroutines.launch

/**
 * نرم‌افزار دوم به صورت کامل و مجزا: «همکار تندیس» (ویژه متخصصین و اصناف)
 *
 * دارای دو بخش مستقل:
 * ۱. صفحه لاگین متخصص و ثبت‌نام متخصص (بدون هیچ گزینه‌ای برای مشتری)
 *    - متصل به POST /api/v1/auth/provider/ و /api/v1/auth/provider/finally/
 *    - متصل به POST /api/v1/profile/provider/signup/information/
 * ۲. داشبورد و رادار مکانی متخصص (دریافت نوتیفیکیشن درخواست مشتری، مشاهده آدرس و لوکیشن روی نقشه PostGIS و پذیرش سفارش)
 */
@Composable
fun HamkarTandisAppScreen(
    incomingRequests: List<ServiceRequestItem>,
    onAcceptRequest: (ServiceRequestItem) -> Unit,
    onRefreshFromServer: () -> Unit,
    onSwitchToCustomerApp: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Specialist Authentication State (مخصوص همکار تندیس)
    var isProviderLoggedIn by remember { mutableStateOf(false) }
    var providerName by remember { mutableStateOf("") }
    var providerBusiness by remember { mutableStateOf("") }
    var providerSpecialty by remember { mutableStateOf("فنی و تعمیرات") }

    if (onSwitchToCustomerApp != null) {
        BackHandler { onSwitchToCustomerApp() }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(TandisBackground)
            .testTag("hamkar_tandis_screen"),
        containerColor = TandisBackground,
        topBar = {
            Surface(
                color = TandisPrimaryDeep,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(TandisSecondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Engineering,
                                contentDescription = null,
                                tint = TandisPrimaryDeep,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "همکار تندیس (ویژه متخصصین)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Text(
                                text = "متصل به سرور 62.3.42.195 | سامانه نوتیفیکیشن و لوکیشن",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TandisSecondary,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isProviderLoggedIn) {
                            IconButton(onClick = { isProviderLoggedIn = false }) {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    contentDescription = "خروج متخصص",
                                    tint = Color.White
                                )
                            }
                        }
                        if (onSwitchToCustomerApp != null) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.15f),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onSwitchToCustomerApp() }
                            ) {
                                Text(
                                    text = "اپ مشتری",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontSize = 10.5.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.navigationBarsPadding()
            )
        }
    ) { paddingValues ->
        if (!isProviderLoggedIn) {
            // ۱. صفحه اختصاصی لاگین و ثبت‌نام متخصص (بدون هیچ‌گونه گزینه مشتری)
            ProviderExclusiveAuthView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                onProviderAuthenticated = { name, business, category ->
                    providerName = name
                    providerBusiness = business
                    providerSpecialty = category
                    isProviderLoggedIn = true
                    scope.launch {
                        snackbarHostState.showSnackbar("خوش آمدید همکار گرامی: $name")
                    }
                }
            )
        } else {
            // ۲. داشبورد و رادار زنده دریافت درخواست‌های مشتری در اپلیکیشن همکار تندیس
            ProviderLiveDashboardView(
                providerName = providerName,
                businessName = providerBusiness,
                specialty = providerSpecialty,
                requests = incomingRequests,
                onAcceptRequest = onAcceptRequest,
                onRefreshFromServer = onRefreshFromServer,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    }
}

@Composable
private fun ProviderExclusiveAuthView(
    onProviderAuthenticated: (name: String, business: String, category: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var isLoginMode by remember { mutableStateOf(true) }
    var otpStep by remember { mutableStateOf(false) }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var nationalId by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("فنی و تعمیرات") }
    var otpCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val categories = TandisData.CATEGORIES.map { it.name }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = TandisSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = if (isLoginMode) "ورود متخصصین به همکار تندیس" else "ثبت‌نام متخصص و صاحب صنف در همکار تندیس",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TandisTextPrimary
                    )
                )
                Text(
                    text = "این نرم‌افزار منحصراً ویژه متخصصین و همکاران تندیس جهت دریافت نوتیفیکیشن سفارش‌ها و مشاهده لوکیشن مشتریان است.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TandisTextSecondary
                    )
                )

                // Switcher: ورود متخصص | ثبت‌نام متخصص
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf(
                        Pair(true, "ورود متخصص"),
                        Pair(false, "ثبت‌نام متخصص جدید")
                    ).forEach { (loginTab, title) ->
                        val isSelected = isLoginMode == loginTab
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) TandisPrimaryDark else TandisSurfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    isLoginMode = loginTab
                                    otpStep = false
                                    errorMessage = null
                                }
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = if (isSelected) Color.White else TandisTextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Light,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)
                            )
                        }
                    }
                }

                if (!isLoginMode && !otpStep) {
                    // فیلدهای ثبت‌نام متخصص منطبق با SignupInformationSchema در API شما
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { firstName = it; errorMessage = null },
                            label = { Text("نام متخصص", style = MaterialTheme.typography.labelSmall) },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { lastName = it; errorMessage = null },
                            label = { Text("نام خانوادگی", style = MaterialTheme.typography.labelSmall) },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = nationalId,
                        onValueChange = { if (it.length <= 10) nationalId = it; errorMessage = null },
                        label = { Text("کد ملی ۱۰ رقمی متخصص", style = MaterialTheme.typography.labelSmall) },
                        leadingIcon = {
                            Icon(Icons.Default.Badge, contentDescription = null, tint = TandisPrimary)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = businessName,
                        onValueChange = { businessName = it; errorMessage = null },
                        label = { Text("نام کسب‌وکار یا واحد صنفی", style = MaterialTheme.typography.labelSmall) },
                        leadingIcon = {
                            Icon(Icons.Default.Business, contentDescription = null, tint = TandisPrimary)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // انتخاب صنف تخصصی
                    Text(
                        text = "انتخاب رسته و صنف تخصصی:",
                        style = MaterialTheme.typography.labelMedium.copy(color = TandisTextPrimary)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        categories.chunked(2).forEach { rowCats ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowCats.forEach { catName ->
                                    val selected = selectedCategory == catName
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (selected) TandisPrimaryLight else TandisBackground,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (selected) TandisPrimary else TandisBorderLight
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { selectedCategory = catName }
                                    ) {
                                        Text(
                                            text = catName,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (selected) TandisPrimaryDark else TandisTextSecondary,
                                                fontWeight = if (selected) FontWeight.Medium else FontWeight.Light
                                            ),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("سوابق کاری و مهارت‌ها (bio)", style = MaterialTheme.typography.labelSmall) },
                        minLines = 2,
                        maxLines = 3,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // شماره همراه متخصص
                OutlinedTextField(
                    value = phone,
                    onValueChange = { if (it.length <= 11) phone = it; errorMessage = null },
                    enabled = !otpStep,
                    label = { Text("شماره همراه متخصص (۱۱ رقمی)", style = MaterialTheme.typography.labelSmall) },
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = TandisPrimary)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                if (otpStep) {
                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { otpCode = it; errorMessage = null },
                        label = { Text("کد تایید پیامکی همکار (OTP)", style = MaterialTheme.typography.labelSmall) },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = TandisPrimary)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                errorMessage?.let { err ->
                    Text(
                        text = err,
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error)
                    )
                }

                Button(
                    onClick = {
                        if (!otpStep) {
                            if (!isLoginMode && (firstName.isBlank() || lastName.isBlank() || nationalId.length != 10 || businessName.isBlank())) {
                                errorMessage = "لطفاً مشخصات فردی، کد ملی ۱۰ رقمی و نام کسب‌وکار را کامل وارد کنید."
                                return@Button
                            }
                            if (phone.length != 11 || !phone.startsWith("0")) {
                                errorMessage = "لطفاً شماره موبایل ۱۱ رقمی معتبر با صفر اول وارد نمایید."
                                return@Button
                            }
                            scope.launch {
                                if (!isLoginMode) {
                                    TandisServerClient.signupProviderInfo(
                                        firstName = firstName,
                                        lastName = lastName,
                                        nationalId = nationalId,
                                        phone = phone,
                                        category = selectedCategory,
                                        businessName = businessName,
                                        bio = bio
                                    )
                                }
                                TandisServerClient.requestProviderOtp(phone)
                                otpStep = true
                            }
                        } else {
                            if (otpCode.length < 4) {
                                errorMessage = "لطفاً کد تایید پیامکی را وارد نمایید."
                                return@Button
                            }
                            scope.launch {
                                TandisServerClient.verifyProviderOtp(phone, otpCode)
                                val fullName = if (firstName.isNotBlank()) "$firstName $lastName" else "متخصص همکار تندیس"
                                val biz = if (businessName.isNotBlank()) businessName else "واحد تخصصی $selectedCategory"
                                onProviderAuthenticated(fullName, biz, selectedCategory)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TandisPrimaryDark,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = when {
                            otpStep -> "تایید کد و ورود به پنل همکار تندیس"
                            isLoginMode -> "دریافت کد ورود متخصص"
                            else -> "ثبت‌نام متخصص و دریافت کد تایید"
                        },
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProviderLiveDashboardView(
    providerName: String,
    businessName: String,
    specialty: String,
    requests: List<ServiceRequestItem>,
    onAcceptRequest: (ServiceRequestItem) -> Unit,
    onRefreshFromServer: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isAvailable by remember { mutableStateOf(true) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Specialist Profile & Availability Card
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = TandisSurface,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = "$providerName ($businessName)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TandisTextPrimary
                        )
                    )
                    Text(
                        text = "صنف: $specialty | دریافت خودکار نوتیفیکیشن از سرور",
                        style = MaterialTheme.typography.labelSmall.copy(color = TandisPrimary)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    IconButton(onClick = onRefreshFromServer) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "بروزرسانی از سرور",
                            tint = TandisPrimary
                        )
                    }
                    Switch(
                        checked = isAvailable,
                        onCheckedChange = { isAvailable = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = TandisPrimary
                        )
                    )
                }
            }
        }

        // Spatial Map Radar (PostGIS Location View)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(185.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = TandisPrimaryDeep)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val strokeEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                    listOf(42.dp.toPx(), 80.dp.toPx(), 120.dp.toPx()).forEach { r ->
                        drawCircle(
                            color = Color(0xFF0A8387).copy(alpha = 0.45f),
                            radius = r,
                            center = center,
                            style = Stroke(width = 1.5f, pathEffect = strokeEffect)
                        )
                    }

                    // Specialist Center
                    drawCircle(color = Color(0xFFF2A900).copy(alpha = 0.25f), radius = 18.dp.toPx(), center = center)
                    drawCircle(color = Color(0xFFF2A900), radius = 6.dp.toPx(), center = center)

                    // Customer Request Locations
                    val offsets = listOf(
                        Offset(center.x + 60.dp.toPx(), center.y - 34.dp.toPx()),
                        Offset(center.x - 70.dp.toPx(), center.y + 28.dp.toPx()),
                        Offset(center.x + 28.dp.toPx(), center.y + 46.dp.toPx())
                    )
                    requests.take(3).forEachIndexed { idx, _ ->
                        val p = offsets[idx % offsets.size]
                        drawLine(
                            color = Color.White.copy(alpha = 0.35f),
                            start = center,
                            end = p,
                            strokeWidth = 2f,
                            pathEffect = strokeEffect
                        )
                        drawCircle(color = Color(0xFF10B981).copy(alpha = 0.35f), radius = 14.dp.toPx(), center = p)
                        drawCircle(color = Color(0xFF10B981), radius = 5.5.dp.toPx(), center = p)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.Black.copy(alpha = 0.5f),
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
                            text = "نقشه لوکیشن مشتریان (PostGIS Spatial Map)",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 10.sp)
                        )
                    }
                }
            }
        }

        // Requests List
        Text(
            text = "درخواست‌های دریافتی مشتریان (${requests.size} مورد):",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = TandisTextPrimary
            )
        )

        if (requests.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = TandisSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "در حال گوش دادن به نوتیفیکیشن‌های سرور...",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TandisTextPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = "به محض اینکه مشتری در اپلیکیشن «تندیس» درخواست تعمیر ثبت کند، نوتیفیکیشن، آدرس دقیق و لوکیشن او در این لیست ظاهر می‌شود.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TandisTextSecondary)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(requests) { req ->
                    val accepted = req.status.contains("پذیرفته شد")
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = TandisSurface,
                        shadowElevation = 1.5.dp,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (accepted) TandisSuccess.copy(alpha = 0.4f) else TandisBorderLight
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = req.title,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = TandisTextPrimary
                                    )
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = TandisPrimaryLight
                                ) {
                                    Text(
                                        text = "کد: ${req.trackingCode}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TandisPrimaryDark,
                                            fontSize = 10.sp
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
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "آدرس و لوکیشن مشتری: ${req.address}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TandisTextSecondary)
                                )
                            }

                            if (req.description.isNotBlank()) {
                                Text(
                                    text = "توضیحات مشتری: ${req.description}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TandisTextMuted)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "زمان: ${req.preferredTime}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TandisTextMuted)
                                )

                                Button(
                                    onClick = { onAcceptRequest(req) },
                                    enabled = !accepted,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (accepted) TandisSuccess else TandisPrimary,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(
                                        text = if (accepted) "پذیرفته شد ✓" else "قبول درخواست و اعزام",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Medium
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
