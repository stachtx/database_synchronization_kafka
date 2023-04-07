--liquibase formatted sql
--changeset stachtx:insert-user_roles-data

INSERT INTO users_roles (user_id, user_role_id) VALUES ('14e2df64-2d6a-11ea-978f-2e728ce88125', '75e20624-1f22-11ea-a32c-2e728ce88125');
INSERT INTO users_roles (user_id, user_role_id) VALUES ('14e2df64-2d6a-11ea-978f-2e728ce88125', '9325a2c2-1f22-11ea-978f-2e728ce88125');
INSERT INTO users_roles (user_id, user_role_id) VALUES ('26592f5a-2d6a-11ea-978f-2e728ce88125', '9325a2c2-1f22-11ea-978f-2e728ce88125');
