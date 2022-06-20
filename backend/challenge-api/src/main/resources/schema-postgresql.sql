DROP TABLE IF EXISTS public.nt_user_authority CASCADE;
DROP TABLE IF EXISTS public.nt_authority CASCADE;
DROP TABLE IF EXISTS public.nt_user CASCADE;
DROP TABLE IF EXISTS public.nt_region CASCADE;
DROP TABLE IF EXISTS public.nt_region_gps CASCADE;
DROP TABLE IF EXISTS public.nt_intersection CASCADE;
DROP TABLE IF EXISTS public.nt_link CASCADE;
DROP TABLE IF EXISTS public.nt_link_gps CASCADE;
DROP TABLE IF EXISTS public.nt_camera CASCADE;
DROP TABLE IF EXISTS public.nt_camera_road CASCADE;
DROP TABLE IF EXISTS public.nt_camera_road_direction CASCADE;
DROP TABLE IF EXISTS public.nt_camera_road_lane CASCADE;

CREATE TABLE public.nt_authority
(
    authority_name character varying(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT nt_authority_pkey PRIMARY KEY (authority_name)
);

CREATE SEQUENCE public.nt_user_user_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE public.nt_user
(
    user_id bigint NOT NULL DEFAULT nextval('nt_user_user_id_seq'::regclass),
    username character varying(128) COLLATE pg_catalog."default" NOT NULL,
    password character varying(128) COLLATE pg_catalog."default" NOT NULL,
    nickname character varying(20) COLLATE pg_catalog."default",
    email character varying(128) COLLATE pg_catalog."default",
    phone character varying(20) COLLATE pg_catalog."default",
    enabled boolean NOT NULL,
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    CONSTRAINT nt_user_pkey PRIMARY KEY (user_id),
    CONSTRAINT uk_nx42foogoor2lh45qp0vanfwy UNIQUE (username)
);

CREATE TABLE public.nt_user_authority
(
    user_id bigint NOT NULL,
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

CREATE TABLE public.nt_region
(
    region_id character varying(10) COLLATE pg_catalog."default" NOT NULL,
    region_name character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT nt_region_pkey PRIMARY KEY (region_id)
);

CREATE TABLE public.nt_region_gps
(
    latitude double precision NOT NULL,
    longitude double precision,
    region_id character varying(10) COLLATE pg_catalog."default",
    CONSTRAINT nt_region_gps_pkey PRIMARY KEY (latitude),
    CONSTRAINT fkqanwdwnfx1ijtedyux6xjd84a FOREIGN KEY (region_id)
        REFERENCES public.nt_region (region_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE public.nt_intersection
(
    intersection_id character varying(10) COLLATE pg_catalog."default" NOT NULL,
    intersection_name character varying(20) COLLATE pg_catalog."default",
    latitude double precision,
    longitude double precision,
    national_id integer,
    region_id character varying(10) COLLATE pg_catalog."default",
    CONSTRAINT nt_intersection_pkey PRIMARY KEY (intersection_id),
    CONSTRAINT fko183g0oa94so37xvycapa4f8p FOREIGN KEY (region_id)
        REFERENCES public.nt_region (region_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE public.nt_link
(
    link_id character varying(10) COLLATE pg_catalog."default" NOT NULL,
    end_id character varying(10) COLLATE pg_catalog."default",
    end_name character varying(30) COLLATE pg_catalog."default",
    start_id character varying(10) COLLATE pg_catalog."default",
    start_name character varying(30) COLLATE pg_catalog."default",
    CONSTRAINT nt_link_pkey PRIMARY KEY (link_id)
);

CREATE TABLE public.nt_link_gps
(
    latitude double precision NOT NULL,
    longitude double precision,
    link_id character varying(10) COLLATE pg_catalog."default",
    CONSTRAINT nt_link_gps_pkey PRIMARY KEY (latitude),
    CONSTRAINT fk141vf5mygaaj07tjbyurbohu5 FOREIGN KEY (link_id)
        REFERENCES public.nt_link (link_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE public.nt_camera
(
    camera_id character varying(10) COLLATE pg_catalog."default" NOT NULL,
    collect_cycle integer,
    degree integer,
    distance integer,
    l_height integer,
    l_width integer,
    last_data_time timestamp without time zone,
    latitude double precision,
    longitude double precision,
    password character varying(256) COLLATE pg_catalog."default" NOT NULL,
    rtsp_id character varying(10) COLLATE pg_catalog."default",
    rtsp_password character varying(256) COLLATE pg_catalog."default" NOT NULL,
    rtsp_url character varying(128) COLLATE pg_catalog."default",
    send_cycle integer,
    server_url character varying(128) COLLATE pg_catalog."default",
    settings_updated boolean,
    s_height integer,
    s_width integer,
    direction_id character varying(10) COLLATE pg_catalog."default",
    intersection_id character varying(10) COLLATE pg_catalog."default",
    CONSTRAINT nt_camera_pkey PRIMARY KEY (camera_id),
    CONSTRAINT fk2d5cbcly0o41qwb2q6ryp23uo FOREIGN KEY (intersection_id)
        REFERENCES public.nt_intersection (intersection_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkkyl4shx8srdcs77m0qdit2s8y FOREIGN KEY (direction_id)
        REFERENCES public.nt_intersection (intersection_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE public.nt_camera_road
(
    road_id character varying(10) COLLATE pg_catalog."default" NOT NULL,
    camera_id character varying(10) COLLATE pg_catalog."default",
    crosswalk character varying(128) COLLATE pg_catalog."default",
    start_line character varying(128) COLLATE pg_catalog."default",
    uturn character varying(128) COLLATE pg_catalog."default",
    CONSTRAINT nt_camera_road_pkey PRIMARY KEY (road_id)
);

CREATE TABLE public.nt_camera_road_direction
(
    road_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    direction boolean,
    CONSTRAINT nt_camera_road_direction_pkey PRIMARY KEY (road_id),
    CONSTRAINT fkpua1lfdkreen0gtvvg1pdnjmd FOREIGN KEY (road_id)
        REFERENCES public.nt_camera_road (road_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE public.nt_camera_road_lane
(
    road_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    lane character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT nt_camera_road_lane_pkey PRIMARY KEY (road_id),
    CONSTRAINT fk872ya75vajbnsnvecglom0ynt FOREIGN KEY (road_id)
        REFERENCES public.nt_camera_road (road_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);