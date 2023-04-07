--liquibase formatted sql
--changeset stachtx:create-tables

CREATE TABLE IF NOT EXISTS address (
    id uuid NOT NULL,
    building_number character varying(255) COLLATE pg_catalog."default" NOT NULL,
    city character varying(255) COLLATE pg_catalog."default" NOT NULL,
    deleted boolean NOT NULL,
    flat_number character varying(255) COLLATE pg_catalog."default",
    street character varying(255) COLLATE pg_catalog."default" NOT NULL,
    version bigint,
    CONSTRAINT address_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS authority
(
    id uuid NOT NULL,
    active boolean NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT authority_pkey PRIMARY KEY (id),
    CONSTRAINT uk_jdeu5vgpb8k5ptsqhrvamuad2 UNIQUE (name),
    CONSTRAINT ukj9vkma9pkho8of1bwxnt5rvgb UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS department
(
    id uuid NOT NULL,
    deleted boolean NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    version bigint,
    CONSTRAINT department_pkey PRIMARY KEY (id),
    CONSTRAINT uk_1t68827l97cwyxo9r1u6t4p7d UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS product_type
(
    id uuid NOT NULL,
    cost bigint NOT NULL,
    deleted boolean NOT NULL,
    manufacture character varying(255) COLLATE pg_catalog."default" NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    version bigint,
    CONSTRAINT product_type_pkey PRIMARY KEY (id),
    CONSTRAINT uk_bnu2aqss00w6he2vs4bmmy609 UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS product
(
    id uuid NOT NULL,
    create_date timestamp(6) without time zone NOT NULL,
    deleted boolean NOT NULL,
    last_update timestamp(6) without time zone,
    serial_number character varying(255) COLLATE pg_catalog."default" NOT NULL,
    status character varying(255) COLLATE pg_catalog."default" NOT NULL,
    version bigint,
    department_id uuid NOT NULL,
    product_type_id uuid NOT NULL,
    CONSTRAINT product_pkey PRIMARY KEY (id),
    CONSTRAINT uk_i0l0yp44bh32fs5hwiah4cn8j UNIQUE (serial_number),
    CONSTRAINT fk8vqjj32x97p0xtnbe504aku1c FOREIGN KEY (department_id)
        REFERENCES public.department (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fklabq3c2e90ybbxk58rc48byqo FOREIGN KEY (product_type_id)
        REFERENCES product_type (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS userdata
(
    id uuid NOT NULL,
    join_date timestamp(6) without time zone NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    "position" character varying(255) COLLATE pg_catalog."default",
    surname character varying(255) COLLATE pg_catalog."default" NOT NULL,
    version bigint,
    workplace character varying(255) COLLATE pg_catalog."default",
    address_id uuid NOT NULL,
    CONSTRAINT userdata_pkey PRIMARY KEY (id),
    CONSTRAINT uk_6cu7esnbt8hc25cg1lou021ci UNIQUE (address_id),
    CONSTRAINT uk_nk1nqff43pmuotrhi0vb0da17 UNIQUE (email),
    CONSTRAINT fkiglsxa9f32i7wjr37w4ptaisg FOREIGN KEY (address_id)
        REFERENCES public.address (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS user_
(
    id uuid NOT NULL,
    account_expired boolean NOT NULL,
    account_locked boolean NOT NULL,
    credentials_expired boolean NOT NULL,
    enabled boolean NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    user_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    version bigint,
    userdata_id uuid NOT NULL,
    CONSTRAINT user__pkey PRIMARY KEY (id),
    CONSTRAINT uk_ikkoov9wmrqhm4v2m4036kidi UNIQUE (userdata_id),
    CONSTRAINT uk_papwupiy57cysnx6x9ct1qxel UNIQUE (user_name),
    CONSTRAINT fk9v0s5j2u6d3mllk1cwd81vqns FOREIGN KEY (userdata_id)
        REFERENCES public.userdata (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS user_role
(
    id uuid NOT NULL,
    active boolean NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT user_role_pkey PRIMARY KEY (id),
    CONSTRAINT uk_lnth8w122wgy7grrjlu8hjmuu UNIQUE (name)
);


CREATE TABLE IF NOT EXISTS users_roles
(
    user_id uuid NOT NULL,
    user_role_id uuid NOT NULL,
    CONSTRAINT fk3e6cmho7fxbpjkngyxrsot02m FOREIGN KEY (user_id)
        REFERENCES public.user_ (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkrguegu40u9brqup2o8pk0l7yh FOREIGN KEY (user_role_id)
        REFERENCES public.user_role (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS users_roles_authorities
(
    user_role_id uuid NOT NULL,
    authority_id uuid NOT NULL,
    CONSTRAINT fk9ec8covwrth3eba2p1j5ni6bf FOREIGN KEY (user_role_id)
        REFERENCES public.user_role (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkk6mooh0v3tip0ocmei2cdscnb FOREIGN KEY (authority_id)
        REFERENCES public.authority (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);