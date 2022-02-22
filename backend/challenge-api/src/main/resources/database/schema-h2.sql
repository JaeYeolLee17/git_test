create table nt_authority (authority_name varchar(20) not null, primary key (authority_name));

create table nt_camera (camera_id varchar(10) not null, intersection_id varchar(10), password varchar(128), settings_updated boolean, primary key (camera_id));

create table nt_user (user_id varchar(20) not null, activated boolean, email varchar(128), password varchar(128), phone varchar(20), username varchar(20), primary key (user_id));

create table nt_user_authority (user_id varchar(20) not null, authority_name varchar(20) not null, primary key (user_id, authority_name));

alter table nt_user_authority add constraint FKpo95nhivvgijenj2f12uqcb62 foreign key (authority_name) references nt_authority;

alter table nt_user_authority add constraint FKo7qe720jmjmwso354pd23oy85 foreign key (user_id) references nt_user;
