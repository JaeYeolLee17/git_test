DROP TABLE IF EXISTS public.nt_user_authority CASCADE;
DROP TABLE IF EXISTS public.nt_authority CASCADE;
DROP TABLE IF EXISTS public.nt_user CASCADE;
DROP TABLE IF EXISTS public.nt_region CASCADE;
DROP TABLE IF EXISTS public.nt_camera CASCADE;
DROP TABLE IF EXISTS public.nt_intersection CASCADE;
DROP TABLE IF EXISTS public.nt_link CASCADE;


CREATE TABLE public.nt_authority
(
    authority_name character varying(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT nt_authority_pkey PRIMARY KEY (authority_name)
);

CREATE TABLE public.nt_user
(
    user_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    password character varying(128) COLLATE pg_catalog."default" NOT NULL,
    username character varying(20) COLLATE pg_catalog."default" NOT NULL,
    email character varying(128) COLLATE pg_catalog."default",
    phone character varying(20) COLLATE pg_catalog."default",
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
    intersection_id character varying(10) COLLATE pg_catalog."default" NOT NULL,
    lat double precision NOT NULL,
    lng double precision NOT NULL,
    direction character varying(10) COLLATE pg_catalog."default" NOT NULL,
    rtsp_url character varying(128) COLLATE pg_catalog."default",
    server_url character varying(128) COLLATE pg_catalog."default",
    collect_cycle integer,
    password character varying(256) COLLATE pg_catalog."default",
    rtsp_id character varying(10) COLLATE pg_catalog."default",
    rtsp_password character varying(256) COLLATE pg_catalog."default",
    send_cycle integer,
    distance integer,
    settings_updated boolean,
    last_data_time timestamp without time zone,
    s_width integer,
    s_height integer,
    l_width integer,
    l_height integer,
    degree integer,
    CONSTRAINT nt_camera_pkey PRIMARY KEY (camera_id),
    CONSTRAINT u_camera_intersection_id UNIQUE (camera_id, intersection_id)
);

CREATE TABLE public.nt_region
(
    region_id character varying(10) COLLATE pg_catalog."default" NOT NULL,
    region_name character varying(20) COLLATE pg_catalog."default" NOT NULL,
    gps point[],
    CONSTRAINT nt_region_pkey PRIMARY KEY (region_id)
);

CREATE TABLE public.nt_intersection
(
    intersection_id character varying(10) COLLATE pg_catalog."default" NOT NULL,
    intersection_name character varying(20) COLLATE pg_catalog."default" NOT NULL,
    lat double precision NOT NULL,
    lng double precision NOT NULL,
    region_id character varying(10) COLLATE pg_catalog."default" NOT NULL,
    national_id integer,
    CONSTRAINT nt_intersection_pkey PRIMARY KEY (intersection_id)
);

CREATE TABLE public.nt_link
(
    start_id character varying(10) COLLATE pg_catalog."default" NOT NULL,
    end_id character varying(10) COLLATE pg_catalog."default" NOT NULL,
    start_name character varying(30) COLLATE pg_catalog."default" NOT NULL,
    end_name character varying(30) COLLATE pg_catalog."default" NOT NULL,
    gps point[] NOT NULL,
    link_id character varying(10) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT nt_link_pkey PRIMARY KEY (link_id),
    CONSTRAINT "u_startId_endId" UNIQUE (start_id, end_id)
);
