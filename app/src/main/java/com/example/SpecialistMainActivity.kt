package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.model.ServiceRequestItem
import com.example.network.TandisServerClient
import com.example.ui.HamkarTandisAppScreen
import com.example.ui.theme.TandisTheme
import kotlinx.coroutines.launch

/**
 * اکتیویتی مستقل نرم‌افزار دوم: «همکار تندیس» (ویژه متخصصین و اصناف)
 * دارای آیکون لانچر اختصاصی در صفحه اصلی گوشی اندروید
 */
class SpecialistMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            val requests = remember {
                mutableStateListOf(
                    ServiceRequestItem(
                        id = "req-101",
                        title = "تعمیر تخصصی پکیج و کولر گازی",
                        categoryName = "فنی و تعمیرات",
                        address = "تهران، سعادت‌آباد، میدان کاج، خیابان سرو شرقی",
                        preferredTime = "فوری (امروز)",
                        description = "پکیج ارور E01 می‌دهد و فشار بار افت کرده است.",
                        customerPhone = "",
                        trackingCode = "TND-48219"
                    )
                )
            }

            TandisTheme {
                HamkarTandisAppScreen(
                    incomingRequests = requests,
                    onAcceptRequest = { req ->
                        val idx = requests.indexOfFirst { it.id == req.id }
                        if (idx != -1) {
                            requests[idx] = req.copy(status = "پذیرفته شد توسط متخصص")
                        }
                    },
                    onRefreshFromServer = {
                        scope.launch {
                            TandisServerClient.fetchNearbyRequestsForSpecialist(
                                latitude = 35.7219,
                                longitude = 51.3347
                            ).onSuccess { list ->
                                list.forEach { dto ->
                                    if (requests.none { it.trackingCode == dto.trackingCode }) {
                                        requests.add(
                                            0,
                                            ServiceRequestItem(
                                                id = dto.id,
                                                title = dto.title,
                                                categoryName = dto.categoryId,
                                                address = dto.addressText,
                                                preferredTime = dto.preferredTime,
                                                description = dto.description,
                                                customerPhone = "",
                                                trackingCode = dto.trackingCode,
                                                status = dto.status
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    },
                    onSwitchToCustomerApp = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
