CREATE TABLE user (
  id BIGINT AUTO_INCREMENT NOT NULL,
  username VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  `role` ENUM('ADMIN', 'USER') NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE user ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE user ADD CONSTRAINT uc_user_username UNIQUE (username);

CREATE TABLE refresh_token (
  id BIGINT AUTO_INCREMENT NOT NULL,
  user_id BIGINT NOT NULL,
  token VARCHAR(255) NOT NULL,
  expiry_date datetime NOT NULL,
  CONSTRAINT pk_refreshtoken PRIMARY KEY (id)
);

ALTER TABLE refresh_token ADD CONSTRAINT uc_refreshtoken_token UNIQUE (token);

ALTER TABLE refresh_token ADD CONSTRAINT FK_REFRESHTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);