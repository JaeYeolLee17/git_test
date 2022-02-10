INSERT INTO USER (USER_ID, USERNAME, PASSWORD, EMAIL, ACTIVATED) VALUES ('admin', 'admin_name', '$2a$10$ZopyHUrwUiE1n2Y0ck.JkuHPB34PQ8U6qTHPRRg6nPYinAeOBirs.', 'admin@email...', 1);
INSERT INTO USER (USER_ID, USERNAME, PASSWORD, EMAIL, ACTIVATED) VALUES ('user1', 'user_name1', '$2a$10$ZopyHUrwUiE1n2Y0ck.JkuHPB34PQ8U6qTHPRRg6nPYinAeOBirs.', 'user1@email...', 1);
INSERT INTO USER (USER_ID, USERNAME, PASSWORD, EMAIL, ACTIVATED) VALUES ('user2', 'user_name2', '$2a$10$ZopyHUrwUiE1n2Y0ck.JkuHPB34PQ8U6qTHPRRg6nPYinAeOBirs.', 'user2@email...', 1);

INSERT INTO AUTHORITY (AUTHORITY_NAME) values ('ROLE_USER');
INSERT INTO AUTHORITY (AUTHORITY_NAME) values ('ROLE_ADMIN');

INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_NAME) values ('admin', 'ROLE_ADMIN');
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_NAME) values ('user1', 'ROLE_USER');
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_NAME) values ('user2', 'ROLE_USER');