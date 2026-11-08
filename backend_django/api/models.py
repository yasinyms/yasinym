from django.contrib.gis.db import models as gis_models
from django.contrib.gis.geos import Point
from django.db import models
import random


class TandisUser(models.Model):
    ROLE_CHOICES = (
        ("customer", "مشتری"),
        ("specialist", "متخصص"),
    )

    phone = models.CharField(max_length=15, unique=True, verbose_name="شماره موبایل")
    full_name = models.CharField(max_length=120, verbose_name="نام و نام خانوادگی")
    role = models.CharField(max_length=20, choices=ROLE_CHOICES, default="customer")
    specialty_category = models.CharField(max_length=50, blank=True, null=True, verbose_name="رسته تخصصی")
    is_online = models.BooleanField(default=False, verbose_name="آنلاین / آماده به کار")
    is_verified = models.BooleanField(default=False, verbose_name="تایید هویت شده")
    current_location = gis_models.PointField(
        geography=True, srid=4326, blank=True, null=True, verbose_name="موقعیت مکانی زنده"
    )
    service_radius_meters = models.IntegerField(default=10000, verbose_name="شعاع پوشش به متر")
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.full_name} ({self.get_role_display()})"


class ServiceRequest(models.Model):
    STATUS_CHOICES = (
        ("pending", "در انتظار متخصص"),
        ("notified", "ارسال شده به متخصصین نزدیک"),
        ("accepted", "پذیرفته شده توسط متخصص"),
        ("in_progress", "در حال انجام"),
        ("completed", "انجام شده"),
        ("canceled", "لغو شده"),
    )

    tracking_code = models.CharField(max_length=25, unique=True, editable=False)
    customer = models.ForeignKey(
        TandisUser, on_delete=models.CASCADE, related_name="customer_requests"
    )
    specialist = models.ForeignKey(
        TandisUser,
        on_delete=models.SET_NULL,
        null=True,
        blank=True,
        related_name="assigned_requests",
    )
    category_id = models.CharField(max_length=50, verbose_name="دسته خدمات")
    title = models.CharField(max_length=200, verbose_name="عنوان درخواست")
    description = models.TextField(blank=True, verbose_name="توضیحات")
    address_text = models.TextField(verbose_name="آدرس دقیق مشتری")
    preferred_time = models.CharField(max_length=100, verbose_name="زمان ترجیحی")
    latitude = models.FloatField(verbose_name="عرض جغرافیایی")
    longitude = models.FloatField(verbose_name="طول جغرافیایی")
    location = gis_models.PointField(geography=True, srid=4326, verbose_name="نقطه جغرافیایی PostGIS")
    status = models.CharField(max_length=30, choices=STATUS_CHOICES, default="pending")
    created_at = models.DateTimeField(auto_now_add=True)
    accepted_at = models.DateTimeField(null=True, blank=True)

    def save(self, *args, **kwargs):
        if not self.tracking_code:
            self.tracking_code = f"TND-{random.randint(10000, 99999)}"
        if self.longitude is not None and self.latitude is not None:
            self.location = Point(float(self.longitude), float(self.latitude), srid=4326)
        super().save(*args, **kwargs)
