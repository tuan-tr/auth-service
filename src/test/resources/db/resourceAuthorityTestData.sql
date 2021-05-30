-- GRANT_PERMISSION(1)
-- CREATE(2)
-- READ(4)
-- UPDATE(8)
-- DELETE(16)
-- ENABLE(32)
-- ADD_ELEMENT(64)

-- user1Id: d1c30a05-910a-40bb-94d4-ade4337221d1
-- user2Id: cb438448-838d-4a86-a66b-980ca5696638

INSERT INTO auth.resource_authority (id, target_type, target_id, resource_type, resource_id, permissions, modified_at, modified_by)
  VALUES
  (uuid_generate_v4(), 'USER', 'd1c30a05-910a-40bb-94d4-ade4337221d1', 'USER', 'd1c30a05-910a-40bb-94d4-ade4337221d1', 4+8, now(), '00000000-0000-0000-0000-000000000000'),
  (uuid_generate_v4(), 'USER', 'd1c30a05-910a-40bb-94d4-ade4337221d1', 'USER', null, 2+4+8+16, now(), '00000000-0000-0000-0000-000000000000'),
  (uuid_generate_v4(), 'USER', 'cb438448-838d-4a86-a66b-980ca5696638', 'USER', 'cb438448-838d-4a86-a66b-980ca5696638', 4+8, now(), '00000000-0000-0000-0000-000000000000')
  ;
