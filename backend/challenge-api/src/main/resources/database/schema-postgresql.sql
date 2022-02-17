DROP TABLE IF EXISTS public.nt_user_authority CASCADE;
DROP TABLE IF EXISTS public.nt_authority CASCADE;
DROP TABLE IF EXISTS public.nt_user CASCADE;
DROP TABLE IF EXISTS public.nt_camera CASCADE;

CREATE TABLE public.nt_authority
(
    authority_name character varying(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT nt_authority_pkey PRIMARY KEY (authority_name)
);

CREATE TABLE public.nt_user
(
    user_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    activated boolean,
    email character varying(128) COLLATE pg_catalog."default",
    password character varying(128) COLLATE pg_catalog."default",
    phone character varying(20) COLLATE pg_catalog."default",
    username character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT nt_user_pkey PRIMARY KEY (user_id)
);

CREATE TABLE public.nt_user_authority
(
    user_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    authority_name character varying(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT nt_user_authority_pkey PRIMARY KEY (user_id, authority_name),
    CONSTRAINT fko7qe720jmjmwso354pd23oy85 FOREIGN KEY (user_id)
        REFERENCES public.nt_user (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkpo95nhivvgijenj2f12uqcb62 FOREIGN KEY (authority_name)
        REFERENCES public.nt_authority (authority_name) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE public.nt_camera
(
    camera_id character varying(10) COLLATE pg_catalog."default" NOT NULL,
    intersection_id character varying(10) COLLATE pg_catalog."default",
    password character varying(128) COLLATE pg_catalog."default",
    settings_updated boolean,
    CONSTRAINT nt_camera_pkey PRIMARY KEY (camera_id)
);
