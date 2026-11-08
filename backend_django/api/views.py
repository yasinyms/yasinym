from rest_framework import serializers, viewsets, status
from rest_framework.decorators import action
from rest_framework.response import Response
from django.contrib.gis.geos import Point
from django.contrib.gis.db.models.functions import Distance
from django.contrib.gis.measure import D
from django.utils import timezone
from .models import TandisUser, ServiceRequest


class TandisUserSerializer(serializers.ModelSerializer):
    class Meta:
        model = TandisUser
        fields = [
            "id",
            "phone",
            "full_name",
            "role",
            "specialty_category",
            "is_online",
            "is_verified",
            "service_radius_meters",
        ]


class ServiceRequestSerializer(serializers.ModelSerializer):
    customer_name = serializers.CharField(source="customer.full_name", read_only=True)
    specialist_name = serializers.CharField(source="specialist.full_name", read_only=True, default=None)
    distance_meters = serializers.SerializerMethodField()

    class Meta:
        model = ServiceRequest
        fields = [
            "id",
            "tracking_code",
            "customer",
            "customer_name",
            "specialist",
            "specialist_name",
            "category_id",
            "title",
            "description",
            "address_text",
            "preferred_time",
            "latitude",
            "longitude",
            "status",
            "distance_meters",
            "created_at",
            "accepted_at",
        ]
        read_only_fields = ["tracking_code", "status", "created_at", "accepted_at"]

    def get_distance_meters(self, obj):
        dist = getattr(obj, "distance", None)
        if dist is not None:
            return round(dist.m, 1)
        return None


class ServiceRequestViewSet(viewsets.ModelViewSet):
    """
    API جامع ارتباط بین اپلیکیشن مشتری و اپلیکیشن متخصص تندیس
    1. POST /api/requests/ -> ثبت درخواست توسط مشتری + یافتن متخصصین نزدیک با PostGIS
    2. GET  /api/requests/nearby_for_specialist/?lat=35.72&lng=51.41&category=services -> دریافت درخواست‌های نزدیک برای اپ متخصص
    3. POST /api/requests/{id}/accept/ -> قبول درخواست توسط متخصص و ارسال اعلان به مشتری
    """

    queryset = ServiceRequest.objects.all().order_by("-created_at")
    serializer_class = ServiceRequestSerializer

    def perform_create(self, serializer):
        instance = serializer.save(status="notified")
        return instance

    @action(detail=False, methods=["get"])
    def nearby_for_specialist(self, request):
        """
        نمایش درخواست‌های باز در اطراف موقعیت مکانی متخصص (بر اساس محاسبه دقیق فاصله PostGIS)
        """
        lat = float(request.query_params.get("lat", 35.7219))
        lng = float(request.query_params.get("lng", 51.3347))
        radius_km = float(request.query_params.get("radius_km", 15.0))
        category = request.query_params.get("category")

        specialist_point = Point(lng, lat, srid=4326)
        qs = ServiceRequest.objects.filter(
            status__in=["pending", "notified"],
            location__distance_lte=(specialist_point, D(km=radius_km)),
        ).annotate(distance=Distance("location", specialist_point)).order_by("distance", "-created_at")

        if category and category != "all":
            qs = qs.filter(category_id=category)

        serializer = self.get_serializer(qs, many=True)
        return Response(serializer.data)

    @action(detail=True, methods=["post"])
    def accept(self, request, pk=None):
        """
        قبول کردن سفارش توسط متخصص (از داخل اپلیکیشن متخصص)
        """
        service_request = self.get_object()
        if service_request.status not in ["pending", "notified"]:
            return Response(
                {"error": "این درخواست قبلاً توسط متخصص دیگری پذیرفته شده است."},
                status=status.HTTP_400_BAD_REQUEST,
            )

        specialist_id = request.data.get("specialist_id")
        if specialist_id:
            service_request.specialist_id = specialist_id

        service_request.status = "accepted"
        service_request.accepted_at = timezone.now()
        service_request.save()

        return Response(self.get_serializer(service_request).data)
