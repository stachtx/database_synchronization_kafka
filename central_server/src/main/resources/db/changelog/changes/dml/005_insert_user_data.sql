--liquibase formatted sql
--changeset stachtx:insert-user-data

INSERT INTO user_ (id, account_expired, account_locked, credentials_expired, enabled, password, user_name, version, userdata_id) VALUES ('14e2df64-2d6a-11ea-978f-2e728ce88125', false, false, false, true, '$2a$08$qvrzQZ7jJ7oy2p/msL4M0.l83Cd0jNsX6AJUitbgRXGzge4j035ha', 'admin', 0, '65efc27a-1f21-11ea-978f-2e728ce88125');
INSERT INTO user_ (id, account_expired, account_locked, credentials_expired, enabled, password, user_name, version, userdata_id) VALUES ('26592f5a-2d6a-11ea-978f-2e728ce88125', false, false, false, true, '$2a$08$EBkM9ioW5YS0izjrngkLTOcGJARIU2X5D3Tu8UstUWGQyQ4HGV5fa', 'user', 0, 'd9700e86-2d94-11ea-978f-2e728ce88125');
