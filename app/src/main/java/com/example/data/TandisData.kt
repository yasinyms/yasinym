package com.example.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.ui.graphics.Color
import com.example.R
import com.example.model.CategoryItem
import com.example.model.CategoryTabItem
import com.example.model.HeroSlideItem
import com.example.model.ListingItem
import com.example.model.SpecItem

object TandisData {
    val CITIES = listOf(
        "تهران و حومه",
        "مشهد",
        "اصفهان",
        "شیراز",
        "تبریز",
        "کرج",
        "اهواز",
        "قم",
        "کرمانشاه",
        "رشت",
        "ارومیه",
        "ساری"
    )

    val SEARCH_TRENDS = listOf(
        "پژو ۲۰۷ سقف شیشه‌ای",
        "آپارتمان وام‌دار پردیس",
        "استخدام برنامه‌نویس دورکار",
        "آیفون ۱۵ پرومکس",
        "مشاوره حقوقی فوری"
    )

    val CATEGORY_TABS = listOf(
        CategoryTabItem("all", "همه آگهی‌ها"),
        CategoryTabItem("auto", "خودرو"),
        CategoryTabItem("real_estate", "املاک"),
        CategoryTabItem("digital", "موبایل و دیجیتال"),
        CategoryTabItem("jobs", "استخدام"),
        CategoryTabItem("legal", "خدمات و حقوقی"),
        CategoryTabItem("home", "خانه و آشپزخانه")
    )

    val CATEGORIES = listOf(
        CategoryItem(
            id = "ride",
            name = "درخواست خودرو",
            description = "سرویس هوشمند سفر درون‌شهری و بین‌شهری تندیس",
            icon = Icons.Default.LocalTaxi,
            iconColor = Color(0xFFF2A900),
            bgColor = Color(0xFF0A8387),
            isProminent = true,
            badge = "جدید"
        ),
        CategoryItem(
            id = "services",
            name = "فنی و تعمیرات",
            description = "تعمیر کولر، یخچال، لباسشویی، پکیج، برق‌کاری و لوله‌کشی",
            icon = Icons.Default.Build,
            iconColor = Color(0xFFEA580C),
            bgColor = Color(0xFFFFF7ED)
        ),
        CategoryItem(
            id = "auto",
            name = "امداد و خودرو",
            description = "مکانیک سیار، یدک‌کش، خودروبر، باطری‌ساز و تعویض روغن",
            icon = Icons.Default.DirectionsCar,
            iconColor = Color(0xFF0284C7),
            bgColor = Color(0xFFF0F9FF)
        ),
        CategoryItem(
            id = "home",
            name = "نظافت و باربری",
            description = "نظافت منزل، شرکت، قالیشویی، مبل‌شویی و اتوبار تخصصی",
            icon = Icons.Default.CleaningServices,
            iconColor = Color(0xFF059669),
            bgColor = Color(0xFFECFDF5)
        ),
        CategoryItem(
            id = "events",
            name = "پزشکی و پرستاری",
            description = "ویزیت پزشک در منزل، پرستاری سالمند و کودک، تزریقات",
            icon = Icons.Default.MedicalServices,
            iconColor = Color(0xFFE11D48),
            bgColor = Color(0xFFFFF1F2)
        ),
        CategoryItem(
            id = "legal",
            name = "حقوقی و اداری",
            description = "وکیل پایه‌یک، داوری، تنظیم قرارداد و عریضه‌نویسی",
            icon = Icons.Default.Balance,
            iconColor = Color(0xFF7C3AED),
            bgColor = Color(0xFFF5F3FF)
        ),
        CategoryItem(
            id = "apparel",
            name = "زیبایی و سلامت",
            description = "آرایشگاه، اصلاح، کراتین مو، خدمات ناخن و پاکسازی پوست",
            icon = Icons.Default.ContentCut,
            iconColor = Color(0xFFDB2777),
            bgColor = Color(0xFFFDF2F8)
        ),
        CategoryItem(
            id = "industrial",
            name = "صنعت و دیجیتال",
            description = "برق و موتور صنعتی، طراحی سایت، سئو، دکوراسیون و چاپ",
            icon = Icons.Default.PrecisionManufacturing,
            iconColor = Color(0xFF2563EB),
            bgColor = Color(0xFFEFF6FF)
        )
    )

    val HERO_SLIDES = listOf(
        HeroSlideItem(
            id = 1,
            tag = "بازار املاک برتر کشور",
            title = "خرید و اجاره مستقیم ملک بدون واسطه مضاعف",
            description = "هزاران ملک کارشناسی‌شده با فایلینگ اختصاصی مالکان و دفاتر رسمی املاک سراسر ایران.",
            primaryAction = "جستجوی هوشمند ملک",
            secondaryAction = "سپردن رایگان ملک",
            imageRes = R.drawable.media_2,
            gradientStart = Color(0xFF0D686D),
            gradientEnd = Color(0xFF06383B)
        ),
        HeroSlideItem(
            id = 2,
            tag = "طرح ویژه کارشناسی تندیس",
            title = "تخفیف ویژه کارشناسی و خرید امن خودرو",
            description = "گواهی ۴۰۰ نقطه‌ای سلامت فنی و بدنه همراه با ضمانت بازگشت وجه و تنظیم مبایعه‌نامه رسمی در محل.",
            primaryAction = "درخواست فوری کارشناس",
            secondaryAction = "مشاهده خودروهای تاییدشده",
            imageRes = R.drawable.media_1,
            gradientStart = Color(0xFF0B5256),
            gradientEnd = Color(0xFF04282B)
        ),
        HeroSlideItem(
            id = 3,
            tag = "خدمات تخصصی حقوقی و قراردادی",
            title = "مشاوره تخصصی و قراردادهای امن حقوقی",
            description = "تنظیم تخصصی مبایعه‌نامه‌ها و قراردادهای تجاری و ملکی با نظارت مستقیم وکلای تایید صلاحیت شده.",
            primaryAction = "مشاوره با وکیل پایه یک",
            secondaryAction = "استعلام قراردادها",
            imageRes = R.drawable.media_3,
            gradientStart = Color(0xFF134E4A),
            gradientEnd = Color(0xFF042F2E)
        )
    )

    val INITIAL_LISTINGS = listOf(
        ListingItem(
            id = "ad-1",
            title = "پژو ۲۰۷i اتوماتیک مدل ۱۴۰۲ بدون رنگ",
            categoryId = "auto",
            price = "۸۹۵,۰۰۰,۰۰۰ تومان",
            priceLabel = "قیمت:",
            location = "تهران، سعادت‌آباد",
            timeAgo = "فوری",
            imageRes = R.drawable.media_4,
            badgeText = "کارشناسی شده",
            badgeBgColor = Color(0xFF0A8387),
            verified = true,
            featured = true,
            description = "پژو ۲۰۷i اتوماتیک MC مدل بهمن ۱۴۰۲، کارکرد ۲۲ هزار کیلومتر. کاملاً بدون رنگ و خط و خش، دارای برگه رسمی کارشناسی تندیس. بیمه شخص ثالث ۱۰ ماه، روکش چرم فابریک، لاستیک‌ها ۹۰٪.",
            phone = "۰۹۱۲۳۴۵۶۷۸۹",
            specs = listOf(
                SpecItem("مدل", "۱۴۰۲"),
                SpecItem("کارکرد", "۲۲,۰۰۰ کیلومتر"),
                SpecItem("گیربکس", "اتوماتیک ۶ سرعته"),
                SpecItem("رنگ", "سفید صدفی")
            )
        ),
        ListingItem(
            id = "ad-2",
            title = "آپارتمان ۱۲۰ متری نیاوران کلیدنخورده ۲ خواب",
            categoryId = "real_estate",
            price = "۲۱,۵۰۰,۰۰۰,۰۰۰ تومان",
            priceLabel = "قیمت کل:",
            location = "تهران، نیاوران",
            timeAgo = "لحظاتی پیش",
            imageRes = R.drawable.media_5,
            badgeText = "فروش فوری",
            badgeBgColor = Color(0xFFDC2626),
            verified = true,
            featured = true,
            description = "آپارتمان نوساز کلیدنخورده در بهترین فرعی نیاوران. نورگیر عالی جنوبی، دو خواب کینگ سایز (یک مستر)، لابی مجلل مبله، ۲ پارکینگ سندی باکس، سند تک‌برگ آماده انتقال.",
            phone = "۰۹۱۲۹۸۷۶۵۴۳",
            specs = listOf(
                SpecItem("متراژ", "۱۲۰ متر"),
                SpecItem("تعداد خواب", "۲ خواب (۱ مستر)"),
                SpecItem("طبقه", "۴ از ۶"),
                SpecItem("پارکینگ", "۲ عدد سندی باکس")
            )
        ),
        ListingItem(
            id = "ad-3",
            title = "آیفون ۱۵ پرو مکس ۲۵۶ گیگ رنگ نچرال تیتانیوم",
            categoryId = "digital",
            price = "۷۸,۵۰۰,۰۰۰ تومان",
            priceLabel = "قیمت:",
            location = "تهران، پونک",
            timeAgo = "۲۰ دقیقه پیش",
            imageRes = R.drawable.media_6,
            badgeText = "ضمانت اصالت",
            badgeBgColor = Color(0xFF059669),
            verified = true,
            featured = true,
            description = "آیفون ۱۵ پرو مکس با حافظه ۲۵۶ گیگابایت، پارت‌نامبر ZAA دو سیم‌کارت، باتری ۱۰۰٪، بدون کوچک‌ترین خط و نقطه. دارای گارانتی ۱۸ ماهه شرکتی و کد رجیستری فعال در سامانه همتا.",
            phone = "۰۹۱۲۰۱۱۲۲۳۳",
            specs = listOf(
                SpecItem("حافظه", "۲۵۶ گیگابایت"),
                SpecItem("سلامت باتری", "۱۰۰٪"),
                SpecItem("رنگ", "Natural Titanium"),
                SpecItem("پارت نامبر", "ZAA (دوسیم)")
            )
        ),
        ListingItem(
            id = "ad-4",
            title = "استخدام مدیر ارشد مارکتینگ و رشد (فول‌تایم)",
            categoryId = "jobs",
            price = "توافقی + پاداش عملکردی",
            priceLabel = "حقوق پیشنهادی:",
            location = "تهران، ونک",
            timeAgo = "۱ ساعت پیش",
            imageRes = R.drawable.media_7,
            badgeText = "استخدام فوری",
            badgeBgColor = Color(0xFF0284C7),
            verified = true,
            featured = false,
            description = "یک هلدینگ معتبر فناوری جهت توسعه محصولات سوپراپلیکیشن خود از افراد متخصص با حداقل ۵ سال سابقه مدیریت رشد و پرفورمنس مارکتینگ دعوت به همکاری می‌نماید. بیمه تکمیلی، ناهار، پاداش فصلی.",
            phone = "۰۲۱۸۸۰۰۹۹۰۰",
            specs = listOf(
                SpecItem("نوع همکاری", "تمام‌وقت حضوری / هیبرید"),
                SpecItem("سابقه کار", "حداقل ۵ سال مرتبط"),
                SpecItem("تحصیلات", "کارشناسی ارشد مدیریت یا صنایع"),
                SpecItem("مزایا", "بیمه تکمیلی، وام، پاداش عملکرد")
            )
        ),
        ListingItem(
            id = "ad-5",
            title = "مشاوره حقوقی تخصصی قراردادهای تجاری و ملکی",
            categoryId = "legal",
            price = "بر اساس تعرفه کانون",
            priceLabel = "حق‌المشاوره:",
            location = "تهران، جردن",
            timeAgo = "امروز",
            imageRes = R.drawable.media_3,
            badgeText = "وکیل پایه یک",
            badgeBgColor = Color(0xFF7C3AED),
            verified = true,
            featured = true,
            description = "دفتر وکالت دکتر فرهمند، وکیل پایه یک دادگستری و مشاور رسمی دعاوی تجاری، بازرگانی، ملکی و داوری. بررسی تخصصی قراردادهای سرمایه‌گذاری و پیش‌فروش آپارتمان با ارائه گواهی استعلام حقوقی.",
            phone = "۰۲۱۲۲۰۰۱۱۰۰",
            specs = listOf(
                SpecItem("صلاحیت", "وکیل پایه یک دادگستری"),
                SpecItem("حوزه تخصصی", "املاک، قراردادها و داوری"),
                SpecItem("امکان مشاوره", "حضوری و تلفنی")
            )
        ),
        ListingItem(
            id = "ad-6",
            title = "ست میز ناهارخوری ۸ نفره چوب راش گرجستان",
            categoryId = "home",
            price = "۳۲,۰۰۰,۰۰۰ تومان",
            priceLabel = "قیمت:",
            location = "تهران، دلاوران",
            timeAgo = "۳ ساعت پیش",
            imageRes = R.drawable.media_8,
            badgeText = "در حد نو",
            badgeBgColor = Color(0xFFD97706),
            verified = false,
            featured = false,
            description = "میز ناهارخوری ۸ نفره سفارشی‌ساز، ساخته شده از چوب راش گرجستان درجه یک. صفحه پلی‌استر ضدخش و آب، صندلی‌های ارگونومیک با پارچه ترک نانو ضدلک. مناسب جهیزیه، فروش به علت مهاجرت.",
            phone = "۰۹۳۵۴۴۴۳۳۲۲",
            specs = listOf(
                SpecItem("تعداد نفرات", "۸ نفره"),
                SpecItem("جنس چوب", "راش سوپر گرجستان"),
                SpecItem("جنس پارچه", "مخمل ترک ضدلک"),
                SpecItem("وضعیت", "در حد نو")
            )
        ),
        ListingItem(
            id = "ad-7",
            title = "هیوندای سانتافه ۲۰۱۷ فول آپشن سفارش اروپا",
            categoryId = "auto",
            price = "۳,۷۵۰,۰۰۰,۰۰۰ تومان",
            priceLabel = "قیمت:",
            location = "تهران، شهرک غرب",
            timeAgo = "۵ ساعت پیش",
            imageRes = R.drawable.media_9,
            badgeText = "تخفیف ویژه",
            badgeBgColor = Color(0xFFDC2626),
            verified = true,
            featured = true,
            description = "سانتافه ۲۰۱۷ گرند فول کامل اروپا، ۴ رادار، اتوپارک، ۵ گرمکن ۲ سردکن، سقف پانوراما، درایو مد ۳ حالته، صندوق برقی با سنسور پا، ساب جلو و عقب اینفینیتی، کارکرد واقعی ۶۸ هزار به شرط دیاگ نمایندگی.",
            phone = "۰۹۱۲۷۷۷۸۸۹۹",
            specs = listOf(
                SpecItem("سال ساخت", "۲۰۱۷"),
                SpecItem("کارکرد", "۶۸,۰۰۰ کیلومتر"),
                SpecItem("رادارها", "۴ رادار فعال"),
                SpecItem("آپشن‌ها", "اتوپارک، سقف پانوراما")
            )
        ),
        ListingItem(
            id = "ad-8",
            title = "تعمیرات تخصصی آیفون و سامسونگ با قطعات اورجینال",
            categoryId = "services",
            price = "طبق نرخ اتحادیه",
            priceLabel = "اجرت کار:",
            location = "تهران، بازار موبایل ایران",
            timeAgo = "فوری در حضور مشتری",
            imageRes = R.drawable.media_6,
            badgeText = "ضمانت قطعه",
            badgeBgColor = Color(0xFF0A8387),
            verified = true,
            featured = false,
            description = "تعویض ال‌سی‌دی، گلس فنی و باتری انواع مدل‌های آیفون و پرچمداران سامسونگ در کمتر از ۴۵ دقیقه در حضور مشتری. استفاده انحصاری از قطعات روکاری و اورجینال همراه با ۱۲۰ روز گارانتی کتبی تعویض.",
            phone = "۰۲۱۶۶۷۰۲۰۰۰",
            specs = listOf(
                SpecItem("ضمانت", "۱۲۰ روز گارانتی تعویض کتبی"),
                SpecItem("مدت زمان", "۳۰ تا ۴۵ دقیقه"),
                SpecItem("تجهیزات", "کلین‌روم استاندارد ضد گرد و غبار")
            )
        )
    )
}
