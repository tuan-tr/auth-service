-- GRANT_PERMISSION(1)
-- CREATE(2)
-- READ(4)
-- UPDATE(8)
-- DELETE(16)
-- ENABLE(32)
-- ADD_ELEMENT(64)

INSERT INTO auth.resource_authority(id, target_type, target_id, resource_type, resource_id, permissions, modified_at, modified_by) VALUES
(nanoid(), 'USER', 'user1Id', 'USER', 'user1Id', 4+8, now(), 'testUserId'),
(nanoid(), 'USER', 'user1Id', 'USER', null, 2+4+8+16, now(), 'testUserId'),
(nanoid(), 'USER', 'user2Id', 'USER', 'user2Id', 4+8, now(), 'testUserId')
;
