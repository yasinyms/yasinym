package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TandisBackground
import com.example.ui.theme.TandisBorderLight
import com.example.ui.theme.TandisPrimary
import com.example.ui.theme.TandisPrimaryDark
import com.example.ui.theme.TandisPrimaryLight
import com.example.ui.theme.TandisSecondary
import com.example.ui.theme.TandisSurface
import com.example.ui.theme.TandisSurfaceVariant
import com.example.ui.theme.TandisTextMuted
import com.example.ui.theme.TandisTextPrimary
import com.example.ui.theme.TandisTextSecondary

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean,
    val time: String = "هم‌اکنون"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportChatModal(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("سلام! به مرکز پشتیبانی و راهنمایی هوشمند تندیس خوش آمدید. چگونه می‌توانیم به شما کمک کنیم؟", false),
            ChatMessage("می‌توانید از پرسش‌های متداول زیر انتخاب نمایید یا پیام خود را تایپ فرمایید.", false)
        )
    }

    val quickQuestions = listOf(
        "نحوه ثبت آگهی رایگان در تندیس",
        "درخواست کارشناسی رسمی خودرو و ملک",
        "عضویت در بانک اصناف و خدمات‌دهندگان",
        "گزارش تخلف یا آگهی نامعتبر"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = TandisSurface,
        modifier = modifier.testTag("support_chat_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp),
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
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(TandisPrimaryLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HeadsetMic,
                            contentDescription = null,
                            tint = TandisPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "پشتیبانی ۲۴ ساعته تندیس",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TandisTextPrimary
                            )
                        )
                        Text(
                            text = "پاسخگویی سریع و برخط کارشناسان",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TandisTextMuted,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                // Call direct support
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = TandisPrimaryLight,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:02188009900"))
                            context.startActivity(intent)
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = null,
                            tint = TandisPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "تماس تلفنی",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TandisPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            // Quick Question Chips
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "پرسش‌های پرتکرار:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TandisTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickQuestions.take(2).forEach { q ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = TandisSurfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    messages.add(ChatMessage(q, true))
                                    messages.add(
                                        ChatMessage(
                                            "درخواست شما برای «$q» ثبت گردید. کارشناس راهنما در کمتر از ۵ دقیقه پاسخ خواهد داد.",
                                            false
                                        )
                                    )
                                }
                        ) {
                            Text(
                                text = q,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TandisTextSecondary,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(8.dp),
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // Chat Messages Box
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(TandisBackground)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { msg ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (msg.isFromUser) Arrangement.End else Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 14.dp,
                                        topEnd = 14.dp,
                                        bottomStart = if (msg.isFromUser) 14.dp else 2.dp,
                                        bottomEnd = if (msg.isFromUser) 2.dp else 14.dp
                                    )
                                )
                                .background(if (msg.isFromUser) TandisPrimary else TandisSurface)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .fillMaxWidth(0.85f)
                        ) {
                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (msg.isFromUser) Color.White else TandisTextPrimary,
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }
            }

            // Input Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = "پیام خود را بنویسید...",
                            style = MaterialTheme.typography.bodySmall.copy(color = TandisTextMuted)
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TandisSurfaceVariant,
                        unfocusedContainerColor = TandisSurfaceVariant,
                        focusedBorderColor = TandisPrimary,
                        unfocusedBorderColor = TandisBorderLight
                    ),
                    singleLine = true
                )

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            messages.add(ChatMessage(inputText.trim(), true))
                            val reply = "پیام شما دریافت شد. همکاران ما در بخش پشتیبانی تندیس در اسرع وقت پاسخ خواهند داد."
                            inputText = ""
                            messages.add(ChatMessage(reply, false))
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(TandisPrimary)
                        .testTag("send_support_message_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "ارسال",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
