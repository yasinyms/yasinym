#!/bin/bash
# اسکریپت خودکار نصب و راه‌اندازی بک‌اند تندیس (Django REST + PostgreSQL + PostGIS)
# روی سرور: ssh -p 3031 root@62.3.42.195

set -e

echo "=== ۱. نصب PostgreSQL، افزونه PostGIS و پیش‌نیازهای پایتون ==="
apt-get update
apt-get install -y python3-pip python3-venv python3-dev libpq-dev \
    postgresql postgresql-contrib postgis gdal-bin libgdal-dev nginx

echo "=== ۲. ساخت دیتابیس PostgreSQL و فعال‌سازی PostGIS ==="
sudo -u postgres psql -tc "SELECT 1 FROM pg_database WHERE datname = 'tandis_db'" | grep -q 1 || \
    sudo -u postgres psql -c "CREATE DATABASE tandis_db;"

sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'tandis_secure_db_pass';"
sudo -u postgres psql -d tandis_db -c "CREATE EXTENSION IF NOT EXISTS postgis;"

echo "=== ۳. راه‌اندازی پروژه Django در مسیر /opt/tandis_backend ==="
mkdir -p /opt/tandis_backend
cd /opt/tandis_backend
python3 -m venv venv
source venv/bin/activate
pip install --upgrade pip
pip install django djangorestframework django-cors-headers psycopg2-binary gunicorn

echo "=== ۴. سرویس آماده اجرا روی پورت 8000 (62.3.42.195:8000) ==="
