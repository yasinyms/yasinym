from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import ServiceRequestViewSet

router = DefaultRouter()
router.register(r"requests", ServiceRequestViewSet, basename="service-requests")

urlpatterns = [
    path("api/", include(router.urls)),
]
