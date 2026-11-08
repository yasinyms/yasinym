from ninja_extra import api_controller, route, ControllerBase
from ninja_jwt.authentication import JWTAuth
from django.shortcuts import get_object_or_404
from django.utils import timezone
from uuid import UUID
from apps.profiles.models import ProviderProfile
from apps.order.models import ServiceRequest
from api.v1.profiles.permissions import IsActiveUser, IsCustomer, IsProvider
from api.v1.order.services import ServiceRequestService
from api.v1.order.schemas import (
    ServiceRequestOutGeneralSchema,
    ServiceRequestInSchema,
    ServiceRequestOutSmallGeneral,
)


@api_controller(
    "customer/service-requests/",
    auth=JWTAuth(),
    tags=["Service Requests Operations For Customer"],
    permissions=[IsActiveUser, IsCustomer],
)
class ServiceRequestController(ControllerBase):
    """عملیات درخواست برای مشتری"""

    def __init__(self):
        self.service = ServiceRequestService()

    @route.post("send/", response={200: ServiceRequestOutGeneralSchema})
    def send_service_request(self, request, data: ServiceRequestInSchema):
        return self.service.send_service_reqeust(request, data)

    @route.get("my/", response={200: list[ServiceRequestOutSmallGeneral]})
    def list_my_service_request(self, request):
        return self.service.list_my_service_request(request)

    @route.get("my/{service_id}/", response={200: ServiceRequestOutGeneralSchema})
    def retrieve_my_service_request(self, request, service_id: UUID):
        return self.service.retrieve_my_service_request(request, service_id)


@api_controller(
    "provider/service-requests/",
    auth=JWTAuth(),
    tags=["Service Requests Operations For Provider"],
    permissions=[IsActiveUser, IsProvider],
)
class ServiceRequestControllerForProvider(ControllerBase):
    """عملیات درخواست مشتری برای پنل و اپلیکیشن همکار تندیس"""

    @route.get("assigned/", response={200: list[ServiceRequestOutGeneralSchema]})
    def list_assigned_to_me(self, request):
        """درخواست‌هایی که به این همکار ارجاع داده شده یا پذیرفته است"""
        provider = get_object_or_404(ProviderProfile, user=request.user)
        return ServiceRequest.objects.filter(
            accepted_provider=provider
        ).order_by("-created_at")

    @route.get("open-nearby/", response={200: list[ServiceRequestOutGeneralSchema]})
    def list_open_in_my_area(self, request):
        """درخواست‌های باز (open) هم‌صنف در شهرهای تحت پوشش همکار"""
        provider = get_object_or_404(ProviderProfile, user=request.user)
        cities = list(provider.areas.values_list("city", flat=True))
        qs = ServiceRequest.objects.filter(status=ServiceRequest.Status.OPEN)
        if provider.category_id:
            qs = qs.filter(category_id=provider.category_id)
        if cities:
            qs = qs.filter(city__in=cities)
        return qs.order_by("-created_at")

    @route.post("accept/{service_id}/", response={200: ServiceRequestOutGeneralSchema})
    def accept_open_request(self, request, service_id: UUID):
        """قبول کردن درخواست باز توسط همکار تندیس"""
        provider = get_object_or_404(ProviderProfile, user=request.user)
        sr = get_object_or_404(ServiceRequest, pk=service_id)
        if sr.status == ServiceRequest.Status.OPEN:
            sr.accepted_provider = provider
            sr.accepted_at = timezone.now()
            sr.status = ServiceRequest.Status.ACCEPTED
            sr.save(update_fields=["accepted_provider", "accepted_at", "status", "updated_at"])
        return sr

    @route.post("done/{service_id}/", response={200: ServiceRequestOutGeneralSchema})
    def complete_request(self, request, service_id: UUID):
        """اعلام پایان کار توسط همکار تندیس"""
        provider = get_object_or_404(ProviderProfile, user=request.user)
        sr = get_object_or_404(ServiceRequest, pk=service_id, accepted_provider=provider)
        sr.status = ServiceRequest.Status.DONE
        sr.done_at = timezone.now()
        sr.save(update_fields=["status", "done_at", "updated_at"])
        return sr
