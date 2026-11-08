package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CategoryItem
import com.example.model.ListingItem
import com.example.model.ServiceRequestItem
import com.example.ui.theme.TandisBackground
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
fun ServiceRequestModal(
    category: CategoryItem? = null,
    listing: ListingItem? = null,
    currentCity: String,
    defaultPhone: String = "",
    existingRequest: ServiceRequestItem? = null,
    onDismiss: () -> Unit,
    onSubmitRequest: (ServiceRequestItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val targetTitle = listing?.title ?: category?.name ?: "درخواست خدمات تندیس"
    val targetSubtitle = listing?.price ?: category?.description ?: "ثبت سفارش در سامانه هوشمند تندیس"

    var address by remember { mutableStateOf(listing?.location ?: currentCity) }
    var preferredTime by remember { mutableStateOf("فوری (امروز)") }
    var description by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf(defaultPhone) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var submittedRequest by remember { mutableStateOf(existingRequest) }

    val timeOptions = listOf(
        "فوری (امروز)",
        "فردا صبح (۸ الی ۱۲)",
        "فردا عصر (۱۴ الی ۱۹)",
        "هماهنگی زمان دلخواه"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = TandisSurface,
        modifier = modifier.testTag("service_request_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .padding(bottom = 28.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                            .background(if (submittedRequest != null) TandisSuccess.copy(alpha = 0.14f) else TandisPrimaryLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (submittedRequest != null) Icons.Default.CheckCircle else (category?.icon ?: Icons.Default.AssignmentTurnedIn),
                            contentDescription = null,
                            tint = if (submittedRequest != null) TandisSuccess else TandisPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = if (submittedRequest != null) "درخواست شما ثبت شد" else "ثبت درخواست آنلاین",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TandisTextPrimary
                            )
                        )
                        Text(
                            text = targetTitle,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TandisPrimary,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.5.sp
                            ),
                            maxLines = 1
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

            if (submittedRequest != null) {
                // Step 2: Post-Request Tracking & Status View (بعد از ثبت درخواست)
                val req = submittedRequest!!
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = TandisPrimaryLight,
                    border = androidx.compose.foundation.BorderStroke(1.dp, TandisPrimary.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TandisPrimary
                        ) {
                            Text(
                                text = "کد پیگیری درخواست: ${req.trackingCode}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }

                        Text(
                            text = req.status,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TandisPrimaryDark,
                                textAlign = TextAlign.Center
                            )
                        )

                        Text(
                            text = "درخواست شما بدون نمایش عمومی شماره تماس، در سامانه امن تندیس ثبت گردید و کارشناسان / طرف مقابل جهت هماهنگی نهایی از طریق پنل تندیس اقدام خواهند کرد.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TandisTextSecondary,
                                textAlign = TextAlign.Center,
                                lineHeight = 19.sp
                            )
                        )
                    }
                }

                // Summary details of the submitted request
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = TandisSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RequestDetailRow("عنوان درخواست:", req.title)
                        RequestDetailRow("محدوده / آدرس:", req.address)
                        RequestDetailRow("زمان ترجیحی:", req.preferredTime)
                        if (req.description.isNotBlank()) {
                            RequestDetailRow("توضیحات شما:", req.description)
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("request_done_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TandisPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "متوجه شدم و بازگشت",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            } else {
                // Step 1: Request Submission Form (بدون نمایش شماره تماس مستقیم)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = TandisSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = targetTitle,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = TandisTextPrimary
                            )
                        )
                        Text(
                            text = targetSubtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TandisTextSecondary
                            )
                        )
                    }
                }

                // Time Selection Chips
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "زمان مورد نظر برای هماهنگی یا مراجعه:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = TandisTextPrimary
                        )
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        timeOptions.chunked(2).forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowItems.forEach { option ->
                                    val isSelected = preferredTime == option
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) TandisPrimaryLight else TandisBackground,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) TandisPrimary else TandisBorderLight
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { preferredTime = option }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Schedule,
                                                contentDescription = null,
                                                tint = if (isSelected) TandisPrimary else TandisTextMuted,
                                                modifier = Modifier.size(15.dp)
                                            )
                                            Text(
                                                text = option,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = if (isSelected) TandisPrimaryDark else TandisTextSecondary,
                                                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Light,
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

                // Location / Address Field
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "محدوده یا آدرس:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = TandisTextPrimary
                        )
                    )
                    OutlinedTextField(
                        value = address,
                        onValueChange = {
                            address = it
                            errorMessage = null
                        },
                        placeholder = {
                            Text(
                                text = "نام محله یا آدرس دقیق را وارد کنید",
                                style = MaterialTheme.typography.bodySmall.copy(color = TandisTextMuted)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = TandisPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = TandisBackground,
                            unfocusedContainerColor = TandisSurfaceVariant,
                            focusedBorderColor = TandisPrimary,
                            unfocusedBorderColor = TandisBorderLight
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("request_address_input")
                    )
                }

                // Description / Notes Field
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "توضیحات درخواست (شرایط، پیشنهاد یا جزئیات):",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = TandisTextPrimary
                        )
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = {
                            Text(
                                text = "توضیحات تکمیلی درخواست خود را بنویسید...",
                                style = MaterialTheme.typography.bodySmall.copy(color = TandisTextMuted)
                            )
                        },
                        minLines = 2,
                        maxLines = 3,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = TandisBackground,
                            unfocusedContainerColor = TandisSurfaceVariant,
                            focusedBorderColor = TandisPrimary,
                            unfocusedBorderColor = TandisBorderLight
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("request_description_input")
                    )
                }

                errorMessage?.let { err ->
                    Text(
                        text = err,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.error
                        )
                    )
                }

                // Submit Request Button
                Button(
                    onClick = {
                        if (address.isBlank()) {
                            errorMessage = "لطفاً محدوده یا آدرس را مشخص فرمایید."
                        } else {
                            val code = "TND-${(10000..99999).random()}"
                            val newReq = ServiceRequestItem(
                                id = listing?.id ?: category?.id ?: code,
                                title = targetTitle,
                                categoryName = category?.name ?: "آگهی تندیس",
                                address = address.trim(),
                                preferredTime = preferredTime,
                                description = description.trim(),
                                customerPhone = customerPhone,
                                trackingCode = code
                            )
                            submittedRequest = newReq
                            onSubmitRequest(newReq)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_request_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TandisPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AssignmentTurnedIn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "ثبت نهایی درخواست",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RequestDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TandisTextMuted
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TandisTextPrimary,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}
