TRUNCATE TABLE PRODUCT CASCADE;
TRUNCATE TABLE PRODUCT_TYPE CASCADE;
TRUNCATE TABLE DEPARTMENT CASCADE;
TRUNCATE TABLE USERDATA CASCADE;
TRUNCATE TABLE ADDRESS CASCADE;
TRUNCATE TABLE AUTHORITY CASCADE;
TRUNCATE TABLE ADDRESS CASCADE;
TRUNCATE TABLE USER_ CASCADE;
TRUNCATE TABLE USER_ROLE CASCADE;
TRUNCATE TABLE USERS_ROLES_AUTHORITIES CASCADE;
TRUNCATE TABLE OAUTH_CLIENT_DETAILS CASCADE;

--//---- ADDRESSES ----//--
INSERT INTO public.address (id, building_number, city, deleted, flat_number, street, version) VALUES ('ee7396e0-1f20-11ea-978f-2e728ce88125', '60', 'Kraków', false, null, 'Podole', 0);
INSERT INTO public.address (id, building_number, city, deleted, flat_number, street, version) VALUES ('f6095afc-1f20-11ea-978f-2e728ce88125', '2', 'Warszawa', false, null, 'żelazna', 0);
INSERT INTO public.address (id, building_number, city, deleted, flat_number, street, version) VALUES ('0059864e-1f21-11ea-978f-2e728ce88125', '3', 'Łódź', false, '2', 'Piotrkowaska', 0);
INSERT INTO public.address (id, building_number, city, deleted, flat_number, street, version) VALUES ('0c3da4a4-1f21-11ea-978f-2e728ce88125', '4', 'Łódź', false, null, 'Kościuszki', 0);
INSERT INTO public.address (id, building_number, city, deleted, flat_number, street, version) VALUES ('1152922e-1f21-11ea-a5e8-2e728ce88125', '5', 'Lublin', false, null, 'Lipowa', 0);
INSERT INTO public.address (id, building_number, city, deleted, flat_number, street, version) VALUES ('15e6067c-1f21-11ea-978f-2e728ce88125', '6', 'Sopot', false, null, 'Grunwaldzka', 0);
INSERT INTO public.address (id, building_number, city, deleted, flat_number, street, version) VALUES ('1b708a40-1f21-11ea-978f-2e728ce88125', '7', 'Szczecin', false, null, 'Kolumba', 0);
INSERT INTO public.address (id, building_number, city, deleted, flat_number, street, version) VALUES ('20d40a48-1f21-11ea-a5e8-2e728ce88125', '1', 'Zakopane', false, null, 'Krupówki', 0);
INSERT INTO public.address (id, building_number, city, deleted, flat_number, street, version) VALUES ('30c1de58-1f21-11ea-978f-2e728ce88125', '1', 'Zakopane', false, null, 'Krupówki', 0);
INSERT INTO public.address (id, building_number, city, deleted, flat_number, street, version) VALUES ('3501205a-1f21-11ea-a5e8-2e728ce88125', '1', 'Zakopane', false, null, 'Krupówki', 0);
INSERT INTO public.address (id, building_number, city, deleted, flat_number, street, version) VALUES ('3a2bbee6-1f21-11ea-978f-2e728ce88125', '1', 'Zakopane', false, null, 'Krupówki', 0);
INSERT INTO public.address (id, building_number, city, deleted, flat_number, street, version) VALUES ('3fa4b224-1f21-11ea-978f-2e728ce88125', '5', 'Kraków', false, '1', 'Dworcowa', 0);

--//---- USERDATA ----//--
INSERT INTO public.userdata (id, join_date, email, name, position, surname, version, workplace, address_id) VALUES ('65efc27a-1f21-11ea-978f-2e728ce88125', '2019-01-12 02:28:56.848000', 'email1@email.com', 'John', null, 'William', 0, null, 'ee7396e0-1f20-11ea-978f-2e728ce88125');
INSERT INTO public.userdata (id, join_date, email, name, position, surname, version, workplace, address_id) VALUES ('d9700e86-2d94-11ea-978f-2e728ce88125', '2011-02-12 12:00:00.000000', 'email2@email.com', 'Tomasz', null, 'Stachura', 0, null, 'f6095afc-1f20-11ea-978f-2e728ce88125');


INSERT INTO OAUTH_CLIENT_DETAILS(CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY)
	VALUES ('spring-security-oauth2-read-client', 'resource-server-rest-api',
	/*spring-security-oauth2-read-client-password1234*/'$2a$04$WGq2P9egiOYoOFemBRfsiO9qTcyJtNRnPKNBl5tokP7IP.eZn93km',
	'read', 'password,authorization_code,refresh_token,implicit', 'USER', 10800, 2592000);

INSERT INTO OAUTH_CLIENT_DETAILS(CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY)
	VALUES ('spring-security-oauth2-read-write-client', 'resource-server-rest-api',
	/*spring-security-oauth2-read-write-client-password1234*/'$2a$04$soeOR.QFmClXeFIrhJVLWOQxfHjsJLSpWrU1iGxcMGdu.a5hvfY4W',
	'read,write', 'password,authorization_code,refresh_token,implicit', 'USER', 10800, 2592000);

--//---- AUTHORITIES ----//--

---- Products ----
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('9e73257e-1f21-11ea-978f-2e728ce88125',TRUE, 'PRODUCT_READ');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('a5934aa0-1f21-11ea-a5e8-2e728ce88125',TRUE, 'PRODUCT_LIST_READ');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('ac11997c-1f21-11ea-978f-2e728ce88125',TRUE, 'PRODUCT_LIST_FOR_DEPARTMENT_READ');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('b3e56700-1f21-11ea-a5e8-2e728ce88125',TRUE, 'PRODUCT_CREATE');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('bc66fb28-1f21-11ea-978f-2e728ce88125',TRUE, 'PRODUCT_UPDATE');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('c4b76fce-1f21-11ea-978f-2e728ce88125',TRUE, 'PRODUCT_DELETE');

---- Product Types ----
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('dc8fa59e-1f21-11ea-978f-2e728ce88125',TRUE, 'PRODUCT_TYPE_READ');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('e3423bea-1f21-11ea-978f-2e728ce88125',TRUE, 'PRODUCT_TYPE_LIST_READ');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('e924bfa6-1f21-11ea-a5e8-2e728ce88125',TRUE, 'PRODUCT_TYPE_CREATE');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('ef7c2434-1f21-11ea-978f-2e728ce88125',TRUE, 'PRODUCT_TYPE_UPDATE');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('f5da6e6c-1f21-11ea-978f-2e728ce88125',TRUE, 'PRODUCT_TYPE_DELETE');

---- Department ----
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('84e99766-1f24-11ea-978f-2e728ce88125',TRUE, 'DEPARTMENT_READ');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('f0d0b46e-1f24-11ea-a5e8-2e728ce88125',TRUE, 'DEPARTMENT_LIST_READ');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('fa59e56e-1f24-11ea-a32c-2e728ce88125',TRUE, 'DEPARTMENT_CREATE');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('08c46d72-1f25-11ea-978f-2e728ce88125',TRUE, 'DEPARTMENT_UPDATE');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('1195d15c-1f25-11ea-a5e8-2e728ce88125',TRUE, 'DEPARTMENT_DELETE');

---- User ----
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('1e015bf0-1f25-11ea-a5e8-2e728ce88125',TRUE, 'USER_DELETE');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('285b6528-1f25-11ea-978f-2e728ce88125',TRUE, 'ACCOUNT_UPDATE_ADMIN');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('dac0fb54-24c2-11ea-978f-2e728ce88125',TRUE,'ACCOUNT_UPDATE_SELF');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('4049814c-1f25-11ea-978f-2e728ce88125',TRUE, 'USER_READ');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('488c9bf0-1f25-11ea-978f-2e728ce88125',TRUE, 'USER_ROLES_READ');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('55667008-1f25-11ea-978f-2e728ce88125',TRUE, 'PASSWORD_UPDATE');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('5e0e2354-1f25-11ea-978f-2e728ce88125',TRUE, 'PASSWORD_READ');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('6d9162dc-1f25-11ea-a5e8-2e728ce88125',TRUE, 'PASSWORD_ADMIN_UPDATE');
INSERT INTO AUTHORITY(ID,ACTIVE, NAME) VALUES ('7685c356-1f25-11ea-978f-2e728ce88125',TRUE, 'PASSWORD_ADMIN_READ');
INSERT INTO AUTHORITy (ID,ACTIVE, NAME) VALUES ('c5b95930-2d6a-11ea-978f-2e728ce88125',TRUE, 'USER_CREATE');

INSERT INTO public.department (id, deleted, name, version) VALUES ('5cbfeed2-1f26-11ea-978f-2e728ce88125', false, 'Łódź | Unit Office department', 0);
INSERT INTO public.department (id, deleted, name, version) VALUES ('9b102026-3641-11ea-978f-2e728ce88125', false, 'Łódź |  Central office department', 0);

--//---- USER ----//--
INSERT INTO public.user_ (id, account_expired, account_locked, credentials_expired, enabled, password, user_name, version, userdata_id) VALUES ('14e2df64-2d6a-11ea-978f-2e728ce88125', false, false, false, true, '$2a$08$qvrzQZ7jJ7oy2p/msL4M0.l83Cd0jNsX6AJUitbgRXGzge4j035ha', 'admin', 0, '65efc27a-1f21-11ea-978f-2e728ce88125');
INSERT INTO public.user_ (id, account_expired, account_locked, credentials_expired, enabled, password, user_name, version, userdata_id) VALUES ('26592f5a-2d6a-11ea-978f-2e728ce88125', false, false, false, true, '$2a$08$EBkM9ioW5YS0izjrngkLTOcGJARIU2X5D3Tu8UstUWGQyQ4HGV5fa', 'user', 0, 'd9700e86-2d94-11ea-978f-2e728ce88125');

--//---- USER ROLE ----//--
INSERT INTO USER_ROLE(ID,NAME,ACTIVE) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125','ADMIN',TRUE );
INSERT INTO USER_ROLE(ID,NAME,ACTIVE) VALUES ('9325a2c2-1f22-11ea-978f-2e728ce88125','USER',TRUE );

--//---- USERS ROLES ----//--
INSERT INTO public.users_roles (user_id, user_role_id) VALUES ('14e2df64-2d6a-11ea-978f-2e728ce88125', '75e20624-1f22-11ea-a32c-2e728ce88125');
INSERT INTO public.users_roles (user_id, user_role_id) VALUES ('14e2df64-2d6a-11ea-978f-2e728ce88125', '9325a2c2-1f22-11ea-978f-2e728ce88125');
INSERT INTO public.users_roles (user_id, user_role_id) VALUES ('26592f5a-2d6a-11ea-978f-2e728ce88125', '9325a2c2-1f22-11ea-978f-2e728ce88125');

--//---- USER ROLES AUTHORITIES ----//--
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', '9e73257e-1f21-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', 'a5934aa0-1f21-11ea-a5e8-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', 'ac11997c-1f21-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', 'b3e56700-1f21-11ea-a5e8-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', 'bc66fb28-1f21-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', 'c4b76fce-1f21-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', 'dc8fa59e-1f21-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', 'e3423bea-1f21-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', 'e924bfa6-1f21-11ea-a5e8-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', 'ef7c2434-1f21-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', 'f5da6e6c-1f21-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', '84e99766-1f24-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', 'f0d0b46e-1f24-11ea-a5e8-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', 'fa59e56e-1f24-11ea-a32c-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', '08c46d72-1f25-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', '1195d15c-1f25-11ea-a5e8-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', '1e015bf0-1f25-11ea-a5e8-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', '285b6528-1f25-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', '6d9162dc-1f25-11ea-a5e8-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125', '7685c356-1f25-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID,AUTHORITY_ID) VALUES ('75e20624-1f22-11ea-a32c-2e728ce88125','c5b95930-2d6a-11ea-978f-2e728ce88125');

---- USER ----
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('9325a2c2-1f22-11ea-978f-2e728ce88125','4049814c-1f25-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('9325a2c2-1f22-11ea-978f-2e728ce88125', '488c9bf0-1f25-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('9325a2c2-1f22-11ea-978f-2e728ce88125', '55667008-1f25-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('9325a2c2-1f22-11ea-978f-2e728ce88125', '5e0e2354-1f25-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('9325a2c2-1f22-11ea-978f-2e728ce88125','dac0fb54-24c2-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('9325a2c2-1f22-11ea-978f-2e728ce88125', 'e3423bea-1f21-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('9325a2c2-1f22-11ea-978f-2e728ce88125', 'dc8fa59e-1f21-11ea-978f-2e728ce88125');
INSERT INTO USERS_ROLES_AUTHORITIES(USER_ROLE_ID, AUTHORITY_ID) VALUES ('9325a2c2-1f22-11ea-978f-2e728ce88125', '9e73257e-1f21-11ea-978f-2e728ce88125');

INSERT INTO public.product_type (id, cost, deleted, manufacture, name, version) VALUES ('dc183220-1f26-11ea-978f-2e728ce88125', 279, false, 'ASUS', 'Mysz Razer Naga Trinity (RZ01-02410100-R2M1)', 0);
INSERT INTO public.product_type (id, cost, deleted, manufacture, name, version) VALUES ('e9942b16-1f26-11ea-978f-2e728ce88125', 2099, false, 'Lenovo', 'Laptop Lenovo V110-15IKB (80TH002CPB)', 0);

INSERT INTO public.product (id, create_date, deleted, last_update, serial_number, status, version, department_id, product_type_id) VALUES ('a176ee4e-1f27-11ea-978f-2e728ce88125', '2011-02-12 12:00:00.000000', false, '2011-02-12 12:00:00.000000', 'XYZ122LPZ', 'available', 0, '5cbfeed2-1f26-11ea-978f-2e728ce88125','dc183220-1f26-11ea-978f-2e728ce88125');
INSERT INTO public.product (id, create_date, deleted, last_update, serial_number, status, version, department_id, product_type_id) VALUES ('a7c95764-1f27-11ea-978f-2e728ce88125', '2011-02-12 12:00:00.000000', false, '2018-02-12 12:00:00.000000', 'XYZ152LPZ', 'available', 0, '5cbfeed2-1f26-11ea-978f-2e728ce88125','dc183220-1f26-11ea-978f-2e728ce88125');
INSERT INTO public.product (id, create_date, deleted, last_update, serial_number, status, version, department_id, product_type_id) VALUES ('00ec35b4-1f28-11ea-978f-2e728ce88125', '2011-02-12 12:00:00.000000', false, '2011-02-12 12:20:00.000000', 'XYZ122JPZ', 'available', 0, '5cbfeed2-1f26-11ea-978f-2e728ce88125','dc183220-1f26-11ea-978f-2e728ce88125');
INSERT INTO public.product (id, create_date, deleted, last_update, serial_number, status, version, department_id, product_type_id) VALUES ('b77af46a-1f27-11ea-a5e8-2e728ce88125', '2011-02-12 12:00:00.000000', false, '2011-02-12 12:00:00.000000', 'XYZ1H2LPZ', 'available', 0, '5cbfeed2-1f26-11ea-978f-2e728ce88125','dc183220-1f26-11ea-978f-2e728ce88125');
INSERT INTO public.product (id, create_date, deleted, last_update, serial_number, status, version, department_id, product_type_id) VALUES ('bb9e7bd4-1f27-11ea-978f-2e728ce88125', '2011-02-12 12:00:00.000000', false, '2011-02-12 12:00:00.000000', 'XY245GLPZ', 'available', 0, '5cbfeed2-1f26-11ea-978f-2e728ce88125','dc183220-1f26-11ea-978f-2e728ce88125');
INSERT INTO public.product (id, create_date, deleted, last_update, serial_number, status, version, department_id, product_type_id) VALUES ('c04105e4-1f27-11ea-978f-2e728ce88125', '2011-02-12 12:00:00.000000', false, '2011-02-12 12:20:00.000000', 'XYZ12DDPZ', 'available', 0, '5cbfeed2-1f26-11ea-978f-2e728ce88125','e9942b16-1f26-11ea-978f-2e728ce88125');
INSERT INTO public.product (id, create_date, deleted, last_update, serial_number, status, version, department_id, product_type_id) VALUES ('c4c8d510-1f27-11ea-978f-2e728ce88125', '2011-02-12 12:00:00.000000', false, '2018-02-12 12:00:00.000000', 'XYZ189LPZ', 'available', 0, '5cbfeed2-1f26-11ea-978f-2e728ce88125','e9942b16-1f26-11ea-978f-2e728ce88125');
INSERT INTO public.product (id, create_date, deleted, last_update, serial_number, status, version, department_id, product_type_id) VALUES ('c9dda076-1f27-11ea-978f-2e728ce88125', '2011-02-12 12:00:00.000000', false, '2018-02-12 12:00:00.000000', 'XYZ249LPZ', 'available', 0, '5cbfeed2-1f26-11ea-978f-2e728ce88125','e9942b16-1f26-11ea-978f-2e728ce88125');
INSERT INTO public.product (id, create_date, deleted, last_update, serial_number, status, version, department_id, product_type_id) VALUES ('cec814ae-1f27-11ea-978f-2e728ce88125', '2011-02-12 12:00:00.000000', false, '2018-02-12 12:00:00.000000', 'XYZ189LRT', 'available', 0, '5cbfeed2-1f26-11ea-978f-2e728ce88125','e9942b16-1f26-11ea-978f-2e728ce88125');