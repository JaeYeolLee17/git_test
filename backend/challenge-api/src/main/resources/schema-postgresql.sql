DROP TABLE IF EXISTS public.nt_user_authority CASCADE;
DROP TABLE IF EXISTS public.nt_authority CASCADE;
DROP TABLE IF EXISTS public.nt_user CASCADE;
DROP TABLE IF EXISTS public.nt_camera_road CASCADE;
DROP TABLE IF EXISTS public.nt_camera CASCADE;
DROP TABLE IF EXISTS public.nt_link_gps CASCADE;
DROP TABLE IF EXISTS public.nt_link CASCADE;
DROP TABLE IF EXISTS public.nt_intersection CASCADE;
DROP TABLE IF EXISTS public.nt_region_gps CASCADE;
DROP TABLE IF EXISTS public.nt_region CASCADE;
DROP TABLE IF EXISTS public.nt_tsi_node CASCADE;
DROP TABLE IF EXISTS public.nt_tsi_signal CASCADE;
DROP TABLE IF EXISTS public.nt_tsi CASCADE;
DROP TABLE IF EXISTS public.lt_traffic_data_m15 CASCADE;
DROP SEQUENCE IF EXISTS public.nt_user_user_id_seq CASCADE;
DROP SEQUENCE IF EXISTS public.nt_camera_camera_id_seq CASCADE;
DROP SEQUENCE IF EXISTS public.nt_camera_road_camera_road_id_seq CASCADE;
DROP SEQUENCE IF EXISTS public.nt_link_link_id_seq CASCADE;
DROP SEQUENCE IF EXISTS public.nt_link_gps_link_gps_id_seq CASCADE;
DROP SEQUENCE IF EXISTS public.nt_intersection_intersection_id_seq CASCADE;
DROP SEQUENCE IF EXISTS public.nt_region_region_id_seq CASCADE;
DROP SEQUENCE IF EXISTS public.nt_region_gps_region_gps_id_seq CASCADE;
DROP SEQUENCE IF EXISTS public.nt_tsi_node_tsi_node_id_seq CASCADE;
DROP SEQUENCE IF EXISTS public.nt_tsi_tsi_id_seq CASCADE;
DROP SEQUENCE IF EXISTS public.nt_tsi_signal_tsi_signal_id_seq CASCADE;
DROP SEQUENCE IF EXISTS public.lt_traffic_data_m15_id_seq CASCADE;

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
    username character varying(32) COLLATE pg_catalog."default" NOT NULL,
    password character varying(128) COLLATE pg_catalog."default" NOT NULL,
    nickname character varying(32) COLLATE pg_catalog."default",
    email character varying(128) COLLATE pg_catalog."default",
    phone character varying(20) COLLATE pg_catalog."default",
    disabled boolean,
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

CREATE SEQUENCE public.nt_region_region_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE public.nt_region
(
    region_id bigint NOT NULL DEFAULT nextval('nt_region_region_id_seq'::regclass),
    region_no character varying(10) COLLATE pg_catalog."default" NOT NULL,
    region_name character varying(32) COLLATE pg_catalog."default",
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    CONSTRAINT nt_region_pkey PRIMARY KEY (region_id),
    CONSTRAINT uk_l8ugcmpbp73hp8atv429r25f8 UNIQUE (region_no)
);

CREATE SEQUENCE public.nt_region_gps_region_gps_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE public.nt_region_gps
(
    region_gps_id bigint NOT NULL DEFAULT nextval('nt_region_gps_region_gps_id_seq'::regclass),
    region_id bigint,
    lat double precision,
    lng double precision,
    gps_order integer,
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    CONSTRAINT nt_region_gps_pkey PRIMARY KEY (region_gps_id),
    CONSTRAINT ukfi9a59s3sjqqwlywhw3mt8mdj UNIQUE (region_id, lat, lng),
    CONSTRAINT fkqanwdwnfx1ijtedyux6xjd84a FOREIGN KEY (region_id)
        REFERENCES public.nt_region (region_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE public.nt_intersection_intersection_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE public.nt_intersection
(
    intersection_id bigint NOT NULL DEFAULT nextval('nt_intersection_intersection_id_seq'::regclass),
    intersection_no character varying(10) COLLATE pg_catalog."default" NOT NULL,
    intersection_name character varying(32) COLLATE pg_catalog."default",
    lat double precision,
    lng double precision,
    region_id bigint,
    national_id bigint,
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    CONSTRAINT nt_intersection_pkey PRIMARY KEY (intersection_id),
    CONSTRAINT uk_sjj5pddc56ktd3to8fq925q94 UNIQUE (intersection_no),
    CONSTRAINT fko183g0oa94so37xvycapa4f8p FOREIGN KEY (region_id)
        REFERENCES public.nt_region (region_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE public.nt_camera_camera_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE public.nt_camera
(
    camera_id bigint NOT NULL DEFAULT nextval('nt_camera_camera_id_seq'::regclass),
    camera_no character varying(10) COLLATE pg_catalog."default" NOT NULL,
    password character varying(128) COLLATE pg_catalog."default" NOT NULL,
    intersection_id bigint,
    direction_id bigint,
    lat double precision,
    lng double precision,
    distance integer,
    rtsp_url character varying(128) COLLATE pg_catalog."default",
    rtsp_id character varying(32) COLLATE pg_catalog."default",
    rtsp_password character varying(128) COLLATE pg_catalog."default",
    server_url character varying(128) COLLATE pg_catalog."default",
    send_cycle integer,
    collect_cycle integer,
    small_width integer,
    small_height integer,
    large_width integer,
    large_height integer,
    degree integer,
    settings_updated boolean NOT NULL,
    last_data_time timestamp without time zone,
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    CONSTRAINT nt_camera_pkey PRIMARY KEY (camera_id),
    CONSTRAINT uk_y13pydn7ymn8kss496aj8db9 UNIQUE (camera_no),
    CONSTRAINT fk2d5cbcly0o41qwb2q6ryp23uo FOREIGN KEY (intersection_id)
        REFERENCES public.nt_intersection (intersection_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkkyl4shx8srdcs77m0qdit2s8y FOREIGN KEY (direction_id)
        REFERENCES public.nt_intersection (intersection_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE public.nt_camera_road_camera_road_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE public.nt_camera_road
(
    camera_road_id bigint NOT NULL DEFAULT nextval('nt_camera_road_camera_road_id_seq'::regclass),
    camera_id bigint,
    start_line character varying(255) COLLATE pg_catalog."default",
    lane character varying(255) COLLATE pg_catalog."default",
    uturn character varying(255) COLLATE pg_catalog."default",
    crosswalk character varying(255) COLLATE pg_catalog."default",
    direction character varying(255) COLLATE pg_catalog."default",
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    CONSTRAINT nt_camera_road_pkey PRIMARY KEY (camera_road_id),
    CONSTRAINT fk7fa1kh4fs21pw6ol52npu4wml FOREIGN KEY (camera_id)
        REFERENCES public.nt_camera (camera_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE public.nt_link_link_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE public.nt_link
(
    link_id bigint NOT NULL DEFAULT nextval('nt_link_link_id_seq'::regclass),
    start_id bigint,
    end_id bigint,
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    CONSTRAINT nt_link_pkey PRIMARY KEY (link_id),
    CONSTRAINT ukc1sdrv1f58r8jta7my772b7ss UNIQUE (start_id, end_id),
    CONSTRAINT fkgkkfk8o0t3sw1edm72axkprfa FOREIGN KEY (start_id)
        REFERENCES public.nt_intersection (intersection_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkolot7x2h0y5ivccvn2096xsgr FOREIGN KEY (end_id)
        REFERENCES public.nt_intersection (intersection_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE public.nt_link_gps_link_gps_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE public.nt_link_gps
(
    link_gps_id bigint NOT NULL DEFAULT nextval('nt_link_gps_link_gps_id_seq'::regclass),
    link_id bigint,
    lat double precision,
    lng double precision,
    gps_order integer,
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    CONSTRAINT nt_link_gps_pkey PRIMARY KEY (link_gps_id),
    CONSTRAINT ukj5asivtra90oxw4tc2hkhv4w0 UNIQUE (link_id, lat, lng),
    CONSTRAINT fk141vf5mygaaj07tjbyurbohu5 FOREIGN KEY (link_id)
        REFERENCES public.nt_link (link_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE public.nt_tsi_node_tsi_node_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE public.nt_tsi_node
(
    tsi_node_id bigint NOT NULL DEFAULT nextval('nt_tsi_node_tsi_node_id_seq'::regclass),
    node_id bigint NOT NULL,
    node_name character varying(32) COLLATE pg_catalog."default",
    lat double precision,
    lng double precision,
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    CONSTRAINT nt_tsi_node_pkey PRIMARY KEY (tsi_node_id),
    CONSTRAINT uk_o6bncwjyn91j0u331cvhlsnpq UNIQUE (node_id)
);

CREATE SEQUENCE public.nt_tsi_tsi_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE public.nt_tsi
(
    tsi_id bigint NOT NULL DEFAULT nextval('nt_tsi_tsi_id_seq'::regclass),
    node_id bigint NOT NULL,
    transition boolean,
    response boolean,
    lights_out boolean,
    flashing boolean,
    manual boolean,
    error_center boolean,
    error_scu boolean,
    error_contradiction boolean,
    cycle_counter integer,
    signal_count integer,
    "time" timestamp without time zone,
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    CONSTRAINT nt_tsi_pkey PRIMARY KEY (tsi_id),
    CONSTRAINT uk_iiowv6i7am1qrlyf4qpny0xip UNIQUE (node_id)
);

CREATE SEQUENCE public.nt_tsi_signal_tsi_signal_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE public.nt_tsi_signal
(
    tsi_signal_id bigint NOT NULL DEFAULT nextval('nt_tsi_signal_tsi_signal_id_seq'::regclass),
    tsi_id bigint,
    info character varying(20) COLLATE pg_catalog."default",
    time_reliability character varying(20) COLLATE pg_catalog."default",
    person boolean,
    status character varying(20) COLLATE pg_catalog."default",
    display_time integer,
    remain_time integer,
    direction integer,
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    CONSTRAINT nt_tsi_signal_pkey PRIMARY KEY (tsi_signal_id),
    CONSTRAINT uk6n7vl2bffxqj33jbgtyet8q0d UNIQUE (tsi_id, info, direction),
    CONSTRAINT fkm6epur2eyrk8unkcc6irrrb2v FOREIGN KEY (tsi_id)
        REFERENCES public.nt_tsi (tsi_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE public.lt_traffic_data_m15_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE public.lt_traffic_data_m15
(
    id bigint NOT NULL DEFAULT nextval('lt_traffic_data_m15_id_seq'::regclass),
    t timestamp without time zone NOT NULL,
    c character varying(10) COLLATE pg_catalog."default" NOT NULL,
    i character varying(10) COLLATE pg_catalog."default",
    r character varying(10) COLLATE pg_catalog."default",
    p integer,
    sr0 integer,
    sr1 integer,
    sr2 integer,
    sr3 integer,
    sr4 integer,
    qmsr_len integer,
    qmsr0 integer,
    qmsr1 integer,
    qmsr2 integer,
    qmsr3 integer,
    qmsr4 integer,
    qtsr0 integer,
    qtsr1 integer,
    qtsr2 integer,
    qtsr3 integer,
    qtsr4 integer,
    lu0 integer,
    lu1 integer,
    lu2 integer,
    lu3 integer,
    lu4 integer,
    qmlu_len integer,
    qmlu0 integer,
    qmlu1 integer,
    qmlu2 integer,
    qmlu3 integer,
    qmlu4 integer,
    qtlu0 integer,
    qtlu1 integer,
    qtlu2 integer,
    qtlu3 integer,
    qtlu4 integer,
    qt_t integer,
    CONSTRAINT lt_traffic_data_m15_pkey PRIMARY KEY (id),
    CONSTRAINT ukns9kd2c11ogafqkpklblcvh39 UNIQUE (t, c)
);