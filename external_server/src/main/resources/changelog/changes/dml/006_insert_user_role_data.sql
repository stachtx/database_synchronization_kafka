--liquibase formatted sql
--changeset stachtx:insert-user_role-data

INSERT INTO USER_ROLE(ID,NAME,ACTIVE) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125','ADMIN',TRUE );
INSERT INTO USER_ROLE(ID,NAME,ACTIVE) VALUES ('9325a2c2-1f22-11ea-978f-2e728ce88125','USER',TRUE );