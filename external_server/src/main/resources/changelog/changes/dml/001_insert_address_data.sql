--liquibase formatted sql
--changeset stachtx:insert-address-data

INSERT INTO address (id, building_number, city, deleted, flat_number, street, version) VALUES ('ee7396e0-1f20-11ea-978f-2e728ce88125', '60', 'Kraków', false, null, 'Podole', 0);
INSERT INTO address (id, building_number, city, deleted, flat_number, street, version) VALUES ('f6095afc-1f20-11ea-978f-2e728ce88125', '2', 'Warszawa', false, null, 'żelazna', 0);
INSERT INTO address (id, building_number, city, deleted, flat_number, street, version) VALUES ('0059864e-1f21-11ea-978f-2e728ce88125', '3', 'Łódź', false, '2', 'Piotrkowaska', 0);
INSERT INTO address (id, building_number, city, deleted, flat_number, street, version) VALUES ('0c3da4a4-1f21-11ea-978f-2e728ce88125', '4', 'Łódź', false, null, 'Kościuszki', 0);
INSERT INTO address (id, building_number, city, deleted, flat_number, street, version) VALUES ('1152922e-1f21-11ea-a5e8-2e728ce88125', '5', 'Lublin', false, null, 'Lipowa', 0);
INSERT INTO address (id, building_number, city, deleted, flat_number, street, version) VALUES ('15e6067c-1f21-11ea-978f-2e728ce88125', '6', 'Sopot', false, null, 'Grunwaldzka', 0);
INSERT INTO address (id, building_number, city, deleted, flat_number, street, version) VALUES ('1b708a40-1f21-11ea-978f-2e728ce88125', '7', 'Szczecin', false, null, 'Kolumba', 0);
INSERT INTO address (id, building_number, city, deleted, flat_number, street, version) VALUES ('20d40a48-1f21-11ea-a5e8-2e728ce88125', '1', 'Zakopane', false, null, 'Krupówki', 0);
INSERT INTO address (id, building_number, city, deleted, flat_number, street, version) VALUES ('30c1de58-1f21-11ea-978f-2e728ce88125', '1', 'Zakopane', false, null, 'Krupówki', 0);
INSERT INTO address (id, building_number, city, deleted, flat_number, street, version) VALUES ('3501205a-1f21-11ea-a5e8-2e728ce88125', '1', 'Zakopane', false, null, 'Krupówki', 0);
INSERT INTO address (id, building_number, city, deleted, flat_number, street, version) VALUES ('3a2bbee6-1f21-11ea-978f-2e728ce88125', '1', 'Zakopane', false, null, 'Krupówki', 0);
INSERT INTO address (id, building_number, city, deleted, flat_number, street, version) VALUES ('3fa4b224-1f21-11ea-978f-2e728ce88125', '5', 'Kraków', false, '1', 'Dworcowa', 0);