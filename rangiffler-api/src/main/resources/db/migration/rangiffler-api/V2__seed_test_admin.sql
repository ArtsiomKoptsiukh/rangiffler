-- V2__seed_test_admin.sql
-- Create test user "testAdmin" linked to existing country with code = 'by'

-- 1) Get country id (must already exist)
SELECT id INTO @country_id
FROM `country`
WHERE code = 'by'
    LIMIT 1;

-- 2) Fail fast if country not found (prevents inserting broken FK)
SELECT IF(@country_id IS NULL, 1/0, 1);

-- 3) Insert user if not exists (idempotent)
-- Use deterministic UUID so the user id is stable across environments
SET @admin_uuid_text = '00000000-0000-0000-0000-000000000002';
SET @admin_user_id = UUID_TO_BIN(@admin_uuid_text, true);

INSERT INTO `user` (id, username, first_name, last_name, avatar, country_id)
SELECT @admin_user_id, 'testAdmin', 'Test', 'Admin', NULL, @country_id
    WHERE NOT EXISTS (
  SELECT 1 FROM `user` WHERE username = 'testAdmin'
);
