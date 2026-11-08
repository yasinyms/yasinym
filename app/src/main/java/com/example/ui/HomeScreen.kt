package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.TandisData
import com.example.model.CategoryItem
import com.example.model.ListingItem
import com.example.model.ServiceRequestItem
import com.example.model.UserProfile
import com.example.ui.components.AuthModal
import com.example.ui.components.CitySelectionDialog
import com.example.ui.components.ListingDetailSheet
import com.example.ui.components.ServiceRequestModal
import com.example.ui.components.SpecialistRadarModal
import com.example.ui.components.SupportChatModal
import com.example.ui.components.TandisBottomNav
import com.example.ui.components.TandisCategoriesGrid
import com.example.ui.components.TandisCtaBanner
import com.example.ui.components.TandisHeroCarousel
import com.example.ui.components.TandisListingsSection
import com.example.ui.components.TandisSearchBar
import com.example.ui.components.TandisSideDrawer
import com.example.ui.components.TandisTopBar
import com.example.ui.theme.TandisBackground
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // User State
    var userProfile by remember { mutableStateOf(UserProfile()) }
    var currentCity by remember { mutableStateOf("تهران و حومه") }

    // Navigation and Tab States
    var activeBottomTab by remember { mutableStateOf("home") }
    var selectedCategoryTabId by remember { mutableStateOf("all") }
    var searchQuery by remember { mutableStateOf("") }

    // Modals
    var selectedListingForDetail by remember { mutableStateOf<ListingItem?>(null) }
    var selectedListingForRequest by remember { mutableStateOf<ListingItem?>(null) }
    var selectedCategoryForRequest by remember { mutableStateOf<CategoryItem?>(null) }
    var showCityDialog by remember { mutableStateOf(false) }
    var showAuthModal by remember { mutableStateOf(false) }
    var showSupportModal by remember { mutableStateOf(false) }
    var showSpecialistRadarModal by remember { mutableStateOf(false) }

    // Store submitted requests by item ID
    val submittedRequests = remember { mutableStateMapOf<String, ServiceRequestItem>() }

    // Listings Data with Bookmark & Request Status Support
    val listings = remember { mutableStateListOf<ListingItem>().apply { addAll(TandisData.INITIAL_LISTINGS) } }

    // Filtered Listings
    val filteredListings by remember(searchQuery, selectedCategoryTabId) {
        derivedStateOf {
            listings.filter { item ->
                val matchesCategory = if (selectedCategoryTabId == "all") {
                    true
                } else {
                    item.categoryId == selectedCategoryTabId
                }

                val matchesSearch = if (searchQuery.isBlank()) {
                    true
                } else {
                    item.title.contains(searchQuery.trim(), ignoreCase = true) ||
                        item.description.contains(searchQuery.trim(), ignoreCase = true) ||
                        item.location.contains(searchQuery.trim(), ignoreCase = true)
                }

                matchesCategory && matchesSearch
            }
        }
    }

    // Handle Back Press
    BackHandler(
        enabled = drawerState.isOpen ||
            selectedListingForRequest != null ||
            selectedCategoryForRequest != null ||
            selectedListingForDetail != null ||
            showCityDialog ||
            showAuthModal ||
            showSupportModal ||
            showSpecialistRadarModal
    ) {
        when {
            drawerState.isOpen -> coroutineScope.launch { drawerState.close() }
            selectedListingForRequest != null -> selectedListingForRequest = null
            selectedCategoryForRequest != null -> selectedCategoryForRequest = null
            selectedListingForDetail != null -> selectedListingForDetail = null
            showCityDialog -> showCityDialog = false
            showAuthModal -> showAuthModal = false
            showSupportModal -> showSupportModal = false
            showSpecialistRadarModal -> showSpecialistRadarModal = false
        }
    }

    // اگر کاربر وارد نرم‌افزار دوم («همکار تندیس») شده باشد، کل محیط نرم‌افزار دوم را نمایش بده
    if (showSpecialistRadarModal) {
        HamkarTandisAppScreen(
            incomingRequests = submittedRequests.values.toList(),
            onAcceptRequest = { req ->
                val updatedReq = req.copy(status = "پذیرفته شد توسط متخصص (در مسیر اعزام)")
                submittedRequests[req.id] = updatedReq
            },
            onRefreshFromServer = {
                coroutineScope.launch {
                    val res = com.example.network.TandisServerClient.fetchNearbyRequestsForSpecialist(
                        latitude = 35.7219,
                        longitude = 51.3347
                    )
                    res.onSuccess { remoteList ->
                        remoteList.forEach { dto ->
                            submittedRequests[dto.trackingCode] = ServiceRequestItem(
                                id = dto.trackingCode,
                                title = dto.title,
                                categoryName = dto.categoryId,
                                address = dto.addressText,
                                preferredTime = dto.preferredTime,
                                description = dto.description,
                                customerPhone = "",
                                trackingCode = dto.trackingCode,
                                status = dto.status
                            )
                        }
                    }
                }
            },
            onSwitchToCustomerApp = { showSpecialistRadarModal = false },
            modifier = modifier
        )
        return
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            TandisSideDrawer(
                userProfile = userProfile,
                currentCity = currentCity,
                onClose = { coroutineScope.launch { drawerState.close() } },
                onCityClick = {
                    coroutineScope.launch { drawerState.close() }
                    showCityDialog = true
                },
                onAuthClick = {
                    coroutineScope.launch { drawerState.close() }
                    showAuthModal = true
                },
                onPostAdClick = {
                    coroutineScope.launch {
                        drawerState.close()
                        snackbarHostState.showSnackbar("جهت ثبت آگهی رایگان ابتدا وارد حساب کاربری خود شوید.")
                        showAuthModal = true
                    }
                },
                onSupportClick = {
                    coroutineScope.launch { drawerState.close() }
                    showSupportModal = true
                },
                onRulesClick = {
                    coroutineScope.launch {
                        drawerState.close()
                        snackbarHostState.showSnackbar("قوانین و راهنمای معامله امن تندیس به روزرسانی شد.")
                    }
                },
                onLogoutClick = {
                    userProfile = UserProfile()
                    coroutineScope.launch {
                        drawerState.close()
                        snackbarHostState.showSnackbar("با موفقیت از حساب کاربری خارج شدید.")
                    }
                }
            )
        }
    ) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .background(TandisBackground)
                .testTag("home_screen_scaffold"),
            containerColor = TandisBackground,
            topBar = {
                TandisTopBar(
                    currentCity = currentCity,
                    userProfile = userProfile,
                    onMenuClick = { coroutineScope.launch { drawerState.open() } },
                    onCityClick = { showCityDialog = true },
                    onProfileClick = { showAuthModal = true }
                )
            },
            bottomBar = {
                TandisBottomNav(
                    activeTabId = activeBottomTab,
                    isLoggedIn = userProfile.isLoggedIn,
                    onTabSelected = { tabId ->
                        activeBottomTab = tabId
                        when (tabId) {
                            "home" -> {
                                coroutineScope.launch {
                                    listState.animateScrollToItem(0)
                                }
                            }
                            "categories" -> {
                                coroutineScope.launch {
                                    listState.animateScrollToItem(2)
                                }
                            }
                            "support" -> {
                                showSupportModal = true
                            }
                            "profile" -> {
                                showAuthModal = true
                            }
                        }
                    }
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        ) { paddingValues ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .testTag("home_lazy_column"),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Item 0: Search Bar & Trending Queries
                item {
                    TandisSearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(3)
                            }
                        },
                        trends = TandisData.SEARCH_TRENDS,
                        onTrendSelected = { trend ->
                            searchQuery = trend
                            coroutineScope.launch {
                                listState.animateScrollToItem(3)
                            }
                        }
                    )
                }

                // Item 1: Hero Carousel
                item {
                    TandisHeroCarousel(
                        slides = TandisData.HERO_SLIDES,
                        onActionClick = { actionText ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("انتخاب: $actionText")
                            }
                        }
                    )
                }

                // Item 2: Categories Grid (Clicking opens ServiceRequestModal + filters)
                item {
                    TandisCategoriesGrid(
                        categories = TandisData.CATEGORIES,
                        selectedCategoryId = selectedCategoryTabId,
                        onCategoryClick = { cat ->
                            selectedCategoryTabId = cat.id
                            selectedCategoryForRequest = cat
                        },
                        onRideHailingClick = {
                            val rideCat = TandisData.CATEGORIES.firstOrNull { it.id == "ride" }
                            selectedCategoryForRequest = rideCat
                        }
                    )
                }

                // Item 3: Listings Section (Filtered Tabs & Listing Cards with Request Flow)
                item {
                    TandisListingsSection(
                        listings = filteredListings,
                        tabs = TandisData.CATEGORY_TABS,
                        selectedTabId = selectedCategoryTabId,
                        onTabSelected = { tabId ->
                            selectedCategoryTabId = tabId
                        },
                        onListingClick = { listing ->
                            selectedListingForDetail = listing
                        },
                        onBookmarkToggle = { listing ->
                            val index = listings.indexOfFirst { it.id == listing.id }
                            if (index != -1) {
                                val current = listings[index]
                                val updated = current.copy(isBookmarked = !current.isBookmarked)
                                listings[index] = updated
                                val msg = if (updated.isBookmarked) "به آگهی‌های نشان‌شده اضافه شد" else "از نشان‌شده‌ها حذف شد"
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(msg)
                                }
                            }
                        },
                        onRequestClick = { listing ->
                            selectedListingForRequest = listing
                        }
                    )
                }

                // Item 4: Provider / Expert CTA Banner (Opens Specialist App Radar)
                item {
                    TandisCtaBanner(
                        onProviderSignupClick = {
                            showSpecialistRadarModal = true
                        },
                        onMoreInfoClick = {
                            showSpecialistRadarModal = true
                        }
                    )
                }

                // Bottom Spacing
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // Modals
    if (showCityDialog) {
        CitySelectionDialog(
            cities = TandisData.CITIES,
            currentCity = currentCity,
            onCitySelected = { city ->
                currentCity = city
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("شهر انتخاب شده: $city")
                }
            },
            onDismiss = { showCityDialog = false }
        )
    }

    if (showAuthModal) {
        AuthModal(
            onDismiss = { showAuthModal = false },
            onLoginSuccess = { profile ->
                userProfile = profile
                if (profile.role.contains("متخصص")) {
                    showSpecialistRadarModal = true
                }
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("خوش آمدید ${profile.name}!")
                }
            }
        )
    }

    if (showSupportModal) {
        SupportChatModal(
            onDismiss = { showSupportModal = false }
        )
    }

    if (showSpecialistRadarModal) {
        SpecialistRadarModal(
            requests = submittedRequests.values.toList(),
            onAcceptRequest = { req ->
                val updatedReq = req.copy(status = "پذیرفته شد توسط متخصص (در مسیر اعزام)")
                submittedRequests[req.id] = updatedReq
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("درخواست «${req.title}» توسط متخصص پذیرفته شد.")
                }
            },
            onRefreshFromServer = {
                coroutineScope.launch {
                    val res = com.example.network.TandisServerClient.fetchNearbyRequestsForSpecialist(
                        latitude = 35.7219,
                        longitude = 51.3347
                    )
                    res.onSuccess { remoteList ->
                        remoteList.forEach { dto ->
                            submittedRequests[dto.trackingCode] = ServiceRequestItem(
                                id = dto.trackingCode,
                                title = dto.title,
                                categoryName = dto.categoryId,
                                address = dto.addressText,
                                preferredTime = dto.preferredTime,
                                description = dto.description,
                                customerPhone = "",
                                trackingCode = dto.trackingCode,
                                status = dto.status
                            )
                        }
                        snackbarHostState.showSnackbar("لیست درخواست‌های نزدیک از سرور 62.3.42.195 بروزرسانی شد.")
                    }.onFailure {
                        snackbarHostState.showSnackbar("در حال انتظار برای راه‌اندازی سرویس جنگو روی 62.3.42.195:8000")
                    }
                }
            },
            onDismiss = { showSpecialistRadarModal = false }
        )
    }

    // Detail Sheet for Listing
    selectedListingForDetail?.let { listing ->
        ListingDetailSheet(
            listing = listing,
            onDismiss = { selectedListingForDetail = null },
            onBookmarkToggle = { item ->
                val index = listings.indexOfFirst { it.id == item.id }
                if (index != -1) {
                    val current = listings[index]
                    val updated = current.copy(isBookmarked = !current.isBookmarked)
                    listings[index] = updated
                    selectedListingForDetail = updated
                }
            },
            onRequestClick = { item ->
                selectedListingForDetail = null
                selectedListingForRequest = item
            }
        )
    }

    // Request Modal for Listing or Category
    if (selectedListingForRequest != null || selectedCategoryForRequest != null) {
        val targetId = selectedListingForRequest?.id ?: selectedCategoryForRequest?.id ?: ""
        ServiceRequestModal(
            category = selectedCategoryForRequest,
            listing = selectedListingForRequest,
            currentCity = currentCity,
            defaultPhone = userProfile.phone,
            existingRequest = submittedRequests[targetId],
            onDismiss = {
                selectedListingForRequest = null
                selectedCategoryForRequest = null
            },
            onSubmitRequest = { req ->
                submittedRequests[targetId] = req
                selectedListingForRequest?.let { item ->
                    val index = listings.indexOfFirst { it.id == item.id }
                    if (index != -1) {
                        val updated = listings[index].copy(
                            isRequested = true,
                            requestTrackingCode = req.trackingCode
                        )
                        listings[index] = updated
                    }
                }
                coroutineScope.launch {
                    // Send asynchronously to Django REST + PostGIS server at 62.3.42.195
                    com.example.network.TandisServerClient.submitCustomerRequest(
                        title = req.title,
                        categoryId = targetId,
                        description = req.description,
                        addressText = req.address,
                        preferredTime = req.preferredTime,
                        latitude = 35.7219,
                        longitude = 51.3347
                    )
                    snackbarHostState.showSnackbar("درخواست ثبت و نوتیفیکیشن به پنل متخصصین نزدیک ارسال شد (${req.trackingCode}).")
                }
            }
        )
    }
}
