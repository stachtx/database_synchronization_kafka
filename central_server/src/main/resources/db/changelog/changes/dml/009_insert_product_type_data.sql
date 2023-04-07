--liquibase formatted sql
--changeset stachtx:insert-product_type-data

INSERT INTO product_type (id, cost, deleted, manufacture, name, version) VALUES ('dc183220-1f26-11ea-978f-2e728ce88125', 279, false, 'ASUS', 'Mysz Razer Naga Trinity (RZ01-02410100-R2M1)', 0);
INSERT INTO product_type (id, cost, deleted, manufacture, name, version) VALUES ('e9942b16-1f26-11ea-978f-2e728ce88125', 2099, false, 'Lenovo', 'Laptop Lenovo V110-15IKB (80TH002CPB)', 0);
