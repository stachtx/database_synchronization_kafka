--liquibase formatted sql
--changeset stachtx:insert-department-data

INSERT INTO department (id, deleted, name, version) VALUES ('5cbfeed2-1f26-11ea-978f-2e728ce88125', false, 'Łódź | Unit Office department', 0);
INSERT INTO department (id, deleted, name, version) VALUES ('9b102026-3641-11ea-978f-2e728ce88125', false, 'Łódź |  Central office department', 0);
