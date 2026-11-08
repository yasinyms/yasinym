# معماری بک‌اند و نقشه پایگاه داده جغرافیایی تندیس (PostgreSQL + PostGIS)
-- سرور هدف: 62.3.42.195

-- ۱. فعال‌سازی افزونه مکانی و جغرافیایی PostGIS
CREATE EXTENSION IF NOT EXISTS postgis;

-- ۲. جدول کاربران (مشتریان و متخصصین)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    phone VARCHAR(15) UNIQUE NOT NULL,
    full_name VARCHAR(120) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('customer', 'specialist', 'admin')),
    specialty_category VARCHAR(50), -- مثلا: services, auto, home, legal, ride
    is_online BOOLEAN DEFAULT FALSE,
    is_verified BOOLEAN DEFAULT FALSE,
    -- موقعیت زنده متخصص روی نقشه (Longitude, Latitude - SRID 4326)
    current_location GEOGRAPHY(Point, 4326),
    service_radius_meters INTEGER DEFAULT 10000, -- شعاع پوشش پیش‌فرض: ۱۰ کیلومتر
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- ایندکس مکانی (GIST) برای جستجوی فوق‌سریع نزدیک‌ترین متخصصین
CREATE INDEX idx_users_current_location ON users USING GIST (current_location);
CREATE INDEX idx_users_role_online ON users (role, is_online, specialty_category);

-- ۳. جدول درخواست‌های خدمات و تعمیرات (Service Requests)
CREATE TABLE service_requests (
    id BIGSERIAL PRIMARY KEY,
    tracking_code VARCHAR(25) UNIQUE NOT NULL,
    customer_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    specialist_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    category_id VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    address_text TEXT NOT NULL,
    preferred_time VARCHAR(100) NOT NULL,
    -- مختصات دقیق محل درخواست مشتری روی نقشه (Longitude, Latitude)
    location GEOGRAPHY(Point, 4326) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'pending'
        CHECK (status IN ('pending', 'notified', 'accepted', 'in_progress', 'completed', 'canceled')),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    accepted_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ
);

-- ایندکس مکانی روی موقعیت درخواست‌ها برای محاسبه فاصله با ST_DWithin و ST_Distance
CREATE INDEX idx_requests_location ON service_requests USING GIST (location);
CREATE INDEX idx_requests_status_category ON service_requests (status, category_id);

-- ۴. جدول لاگ ارسال نوتیفیکیشن به متخصصین نزدیک
CREATE TABLE request_notifications (
    id BIGSERIAL PRIMARY KEY,
    request_id BIGINT NOT NULL REFERENCES service_requests(id) ON DELETE CASCADE,
    specialist_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    distance_meters DOUBLE PRECISION NOT NULL,
    is_seen BOOLEAN DEFAULT FALSE,
    sent_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(request_id, specialist_id)
);

-- نمونه کوئری PostGIS برای یافتن متخصصین آنلاین در شعاع ۱۰ کیلومتری مشتری:
-- SELECT id, full_name, phone,
--        ST_Distance(current_location, ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography) AS distance_meters
-- FROM users
-- WHERE role = 'specialist'
--   AND is_online = TRUE
--   AND specialty_category = :category_id
--   AND ST_DWithin(current_location, ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography, service_radius_meters)
-- ORDER BY distance_meters ASC;
