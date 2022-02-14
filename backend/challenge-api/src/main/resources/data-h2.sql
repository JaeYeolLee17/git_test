INSERT INTO NT_AUTHORITY (AUTHORITY_NAME) values ('ROLE_ADMIN');
INSERT INTO NT_AUTHORITY (AUTHORITY_NAME) values ('ROLE_MANAGER');
INSERT INTO NT_AUTHORITY (AUTHORITY_NAME) values ('ROLE_USER');

INSERT INTO NT_USER (USER_ID, USERNAME, PASSWORD, EMAIL, PHONE, ACTIVATED) VALUES ('admin', 'admin_name', '$2a$10$ZopyHUrwUiE1n2Y0ck.JkuHPB34PQ8U6qTHPRRg6nPYinAeOBirs.', 'admin@email...', '010-...', true);
INSERT INTO NT_USER (USER_ID, USERNAME, PASSWORD, EMAIL, PHONE, ACTIVATED) VALUES ('manager', 'manager_name', '$2a$10$ZopyHUrwUiE1n2Y0ck.JkuHPB34PQ8U6qTHPRRg6nPYinAeOBirs.', 'manager@email...', '010-...', true);

INSERT INTO NT_USER_AUTHORITY (USER_ID, AUTHORITY_NAME) values ('admin', 'ROLE_ADMIN');
INSERT INTO NT_USER_AUTHORITY (USER_ID, AUTHORITY_NAME) values ('manager', 'ROLE_MANAGER');