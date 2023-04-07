--liquibase formatted sql
--changeset stachtx:insert-userdata-data

INSERT INTO userdata (id, join_date, email, name, position, surname, version, workplace, address_id) VALUES ('65efc27a-1f21-11ea-978f-2e728ce88125', '2019-01-12 02:28:56.848000', 'email1@email.com', 'John', null, 'William', 0, null, 'ee7396e0-1f20-11ea-978f-2e728ce88125');
INSERT INTO userdata (id, join_date, email, name, position, surname, version, workplace, address_id) VALUES ('d9700e86-2d94-11ea-978f-2e728ce88125', '2011-02-12 12:00:00.000000', 'email2@email.com', 'Tomasz', null, 'Stachura', 0, null, 'f6095afc-1f20-11ea-978f-2e728ce88125');
