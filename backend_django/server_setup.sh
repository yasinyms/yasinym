#!/usr/bin/env bash
# ==============================================================================
# اسکریپت نصب و راه‌اندازی کامل بک‌اند تندیس (Django + PostgreSQL + PostGIS)
# برای اجرا روی سرور اوبونتو/دبیان (62.3.42.195)
# ==============================================================================

set -e

echo "🚀 در حال آماده‌سازی و نصب پیش‌نیازهای سرور..."

# ۱. به‌روزرسانی مخازن و نصب پکیج‌های سیستمی و دیتابیس جغرافیایی
export DEBIAN_FRONTEND=noninteractive
apt-get update -y
apt-get install -y \
    python3 \
    python3-pip \
    python3-venv \
    python3-dev \
    libpq-dev \
    postgresql \
    postgresql-contrib \
    postgis \
    postgresql-*-postgis-* \
    gdal-bin \
    libgdal-dev \
    git \
    curl \
    nginx \
    ufw

echo "✅ پکیج‌های سیستمی و PostGIS نصب شدند."

# ۲. راه‌اندازی و پیکربندی پایگاه داده PostgreSQL + PostGIS
echo "🗄️ در حال ساخت پایگاه داده 'tandis_db' و فعال‌سازی PostGIS..."
systemctl start postgresql
systemctl enable postgresql

sudo -u postgres psql -tc "SELECT 1 FROM pg_database WHERE datname = 'tandis_db'" | grep -q 1 || \
    sudo -u postgres psql -c "CREATE DATABASE tandis_db;"

sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'tandis_secure_db_pass';"
sudo -u postgres psql -d tandis_db -c "CREATE EXTENSION IF NOT EXISTS postgis;"
sudo -u postgres psql -d tandis_db -c "CREATE EXTENSION IF NOT EXISTS postgis_topology;"

echo "✅ پایگاه داده با افزونه‌های مکانی PostGIS با موفقیت فعال شد."

# ۳. ایجاد پوشه پروژه
PROJECT_DIR="/opt/tandis_backend"
mkdir -p "$PROJECT_DIR"
cd "$PROJECT_DIR"

# ۴. ساخت محیط مجازی پایتون و نصب وابستگی‌ها
echo "🐍 در حال راه‌اندازی محیط مجازی پایتون..."
python3 -m venv venv
source venv/bin/activate
pip install --upgrade pip

# پکیج‌های اختصاصی پروژه شما (Django NinjaExtra, JWT, PostGIS, Gunicorn, Uvicorn)
pip install \
    django \
    django-ninja \
    ninja-extra \
    ninja-jwt \
    django-cors-headers \
    psycopg2-binary \
    pydantic \
    gunicorn \
    uvicorn \
    pillow

# ۵. باز کردن پورت‌های لازم در فایروال
ufw allow 80/tcp || true
ufw allow 8000/tcp || true
ufw allow 3031/tcp || true

echo "=================================================================="
echo "🎉 سرور شما با موفقیت آماده شد!"
echo "محیط پروژه: $PROJECT_DIR"
echo "پایگاه داده: tandis_db (PostgreSQL + PostGIS)"
echo "پورت بک‌اند برای اتصال اپلیکیشن‌ها: 8000 (http://62.3.42.195:8000/api/v1)"
echo "=================================================================="
