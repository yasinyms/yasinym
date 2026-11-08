package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.network.TandisServerClient
import com.example.ui.theme.TandisBackground
import com.example.ui.theme.TandisBorderLight
import com.example.ui.theme.TandisPrimary
import com.example.ui.theme.TandisPrimaryDark
import com.example.ui.theme.TandisPrimaryLight
import com.example.ui.theme.TandisSurface
import com.example.ui.theme.TandisSurfaceVariant
import com.example.ui.theme.TandisTextMuted
import com.example.ui.theme.TandisTextPrimary
import com.example.ui.theme.TandisTextSecondary
import kotlinx.coroutines.launch

/**
 * فرم ورود و ثبت‌نام اختصاصی مشتری (اپلیکیشن اول: تندیس)
 * بدون هیچ‌گونه انتخاب نقش متخصص؛ منطبق با API بک‌اند شما:
 * - POST /api/v1/auth/customer/ & /api/v1/auth/customer/finally/
 * - POST /api/v1/profiles/customer/signup/information/
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthModal(
    onDismiss: () -> Unit,
    onLoginSuccess: (UserProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    // true = ورود مشتری, false = ثبت‌نام مشتری جدید
    var isLoginTab by remember { mutableStateOf(true) }
    var otpStep by remember { mutableStateOf(false) }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var nationalId by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = TandisSurface,
        modifier = modifier.testTag("auth_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
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
                            .background(TandisPrimaryLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = TandisPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = if (isLoginTab) "ورود مشتری به تندیس" else "ثبت‌نام مشتری جدید در تندیس",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TandisTextPrimary
                            )
                        )
                        Text(
                            text = "ویژه مشتریان (اپلیکیشن اول تندیس)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TandisTextMuted,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "بستن",
                        tint = TandisTextMuted
                    )
                }
            }

            // Tab Switcher: ورود مشتری | ثبت‌نام مشتری
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    Pair(true, "ورود با شماره همراه"),
                    Pair(false, "ثبت‌نام مشتری جدید")
                ).forEach { (isLogin, label) ->
                    val isSelected = isLoginTab == isLogin
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) TandisPrimary else TandisSurfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                isLoginTab = isLogin
                                otpStep = false
                                errorMessage = null
                            }
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Light,
                                color = if (isSelected) Color.White else TandisTextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )
                    }
                }
            }

            if (!isLoginTab && !otpStep) {
                // فیلدهای ثبت‌نام مشتری طبق SignupInformationForCustomerInSchema در API شما
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = {
                            firstName = it
                            errorMessage = null
                        },
                        label = { Text("نام", style = MaterialTheme.typography.labelSmall) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = TandisBackground,
                            unfocusedContainerColor = TandisSurfaceVariant,
                            focusedBorderColor = TandisPrimary,
                            unfocusedBorderColor = TandisBorderLight
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = {
                            lastName = it
                            errorMessage = null
                        },
                        label = { Text("نام خانوادگی", style = MaterialTheme.typography.labelSmall) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = TandisBackground,
                            unfocusedContainerColor = TandisSurfaceVariant,
                            focusedBorderColor = TandisPrimary,
                            unfocusedBorderColor = TandisBorderLight
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = nationalId,
                    onValueChange = {
                        if (it.length <= 10) nationalId = it
                        errorMessage = null
                    },
                    label = { Text("کد ملی ۱۰ رقمی", style = MaterialTheme.typography.labelSmall) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Badge,
                            contentDescription = null,
                            tint = TandisPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TandisBackground,
                        unfocusedContainerColor = TandisSurfaceVariant,
                        focusedBorderColor = TandisPrimary,
                        unfocusedBorderColor = TandisBorderLight
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Phone Input
            OutlinedTextField(
                value = phone,
                onValueChange = {
                    if (it.length <= 11) phone = it
                    errorMessage = null
                },
                enabled = !otpStep,
                label = { Text("شماره همراه ۱۱ رقمی (مثال: 09123456789)", style = MaterialTheme.typography.labelSmall) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = TandisPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = TandisBackground,
                    unfocusedContainerColor = TandisSurfaceVariant,
                    focusedBorderColor = TandisPrimary,
                    unfocusedBorderColor = TandisBorderLight
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_phone_input")
            )

            if (otpStep) {
                OutlinedTextField(
                    value = otpCode,
                    onValueChange = {
                        otpCode = it
                        errorMessage = null
                    },
                    label = { Text("کد تایید پیامکی (OTP)", style = MaterialTheme.typography.labelSmall) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = TandisPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TandisBackground,
                        unfocusedContainerColor = TandisSurfaceVariant,
                        focusedBorderColor = TandisPrimary,
                        unfocusedBorderColor = TandisBorderLight
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            errorMessage?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 11.5.sp
                    )
                )
            }

            // Submit Button
            Button(
                onClick = {
                    if (!otpStep) {
                        if (!isLoginTab && (firstName.isBlank() || lastName.isBlank() || nationalId.length != 10)) {
                            errorMessage = "لطفاً نام، نام خانوادگی و کد ملی ۱۰ رقمی را کامل وارد نمایید."
                            return@Button
                        }
                        if (phone.length != 11 || !phone.startsWith("0")) {
                            errorMessage = "لطفاً شماره همراه ۱۱ رقمی معتبر با صفر اول وارد نمایید."
                            return@Button
                        }
                        scope.launch {
                            if (!isLoginTab) {
                                TandisServerClient.signupCustomerInfo(firstName, lastName, nationalId, phone)
                            }
                            TandisServerClient.requestCustomerOtp(phone)
                            otpStep = true
                        }
                    } else {
                        if (otpCode.length < 4) {
                            errorMessage = "لطفاً کد تایید پیامکی را وارد نمایید."
                            return@Button
                        }
                        scope.launch {
                            TandisServerClient.verifyCustomerOtp(phone, otpCode)
                            val displayName = if (firstName.isNotBlank()) "$firstName $lastName" else "مشتری تندیس"
                            onLoginSuccess(
                                UserProfile(
                                    isLoggedIn = true,
                                    name = displayName,
                                    phone = phone,
                                    role = "مشتری رسمی تندیس"
                                )
                            )
                            onDismiss()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("auth_submit_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TandisPrimary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = when {
                        otpStep -> "تایید کد و ورود به حساب مشتری"
                        isLoginTab -> "دریافت کد تایید ورود"
                        else -> "ثبت‌نام مشتری و دریافت کد تایید"
                    },
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            if (otpStep) {
                TextButton(
                    onClick = { otpStep = false },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "ویرایش شماره همراه",
                        style = MaterialTheme.typography.labelSmall.copy(color = TandisPrimaryDark)
                    )
                }
            }
        }
    }
}
