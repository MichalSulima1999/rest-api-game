CREATE TABLE user (
  id BIGINT AUTO_INCREMENT NOT NULL,
   username VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   `role` VARCHAR(255) NOT NULL,
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

CREATE TABLE enemy (
  id BIGINT AUTO_INCREMENT NOT NULL,
   deleted BIT(1) NOT NULL,
   CONSTRAINT pk_enemy PRIMARY KEY (id)
);

CREATE TABLE equipment (
  id BIGINT AUTO_INCREMENT NOT NULL,
   gold INT NOT NULL,
   deleted BIT(1) NOT NULL,
   CONSTRAINT pk_equipment PRIMARY KEY (id)
);

CREATE TABLE skill (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NULL,
   type VARCHAR(255) NOT NULL,
   multiplier FLOAT NOT NULL,
   effect VARCHAR(255) NULL,
   effect_duration INT NULL,
   effect_multiplier FLOAT NULL,
   race VARCHAR(255) NOT NULL,
   deleted BIT(1) NOT NULL,
   CONSTRAINT pk_skill PRIMARY KEY (id)
);

CREATE TABLE statistics (
  id BIGINT AUTO_INCREMENT NOT NULL,
   max_hp INT NOT NULL,
   current_hp INT NOT NULL,
   max_mana INT NOT NULL,
   current_mana INT NOT NULL,
   damage INT NOT NULL,
   magic_damage INT NOT NULL,
   armor INT NOT NULL,
   dodge_chance FLOAT NOT NULL,
   critical_chance FLOAT NOT NULL,
   current_xp INT NOT NULL,
   xp_to_next_level INT NOT NULL,
   current_level INT NOT NULL,
   strength INT NOT NULL,
   dexterity INT NOT NULL,
   constitution INT NOT NULL,
   intelligence INT NOT NULL,
   deleted BIT(1) NOT NULL,
   CONSTRAINT pk_statistics PRIMARY KEY (id)
);

CREATE TABLE training (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NOT NULL,
   stat VARCHAR(255) NULL,
   amount INT NOT NULL,
   training_minutes INT NOT NULL,
   deleted BIT(1) NOT NULL,
   CONSTRAINT pk_training PRIMARY KEY (id)
);

CREATE TABLE work (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NOT NULL,
   wage INT NOT NULL,
   work_minutes INT NOT NULL,
   deleted BIT(1) NOT NULL,
   CONSTRAINT pk_work PRIMARY KEY (id)
);

CREATE TABLE fight (
  id BIGINT AUTO_INCREMENT NOT NULL,
   enemy_id BIGINT NULL,
   player_turn BIT(1) NULL,
   deleted BIT(1) NOT NULL,
   CONSTRAINT pk_fight PRIMARY KEY (id)
);

CREATE TABLE adventure (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NOT NULL,
   adventure_time datetime NULL,
   enemy_id BIGINT NULL,
   deleted BIT(1) NOT NULL,
   CONSTRAINT pk_adventure PRIMARY KEY (id)
);

CREATE TABLE occupation (
  id BIGINT AUTO_INCREMENT NOT NULL,
   finish_time datetime NULL,
   adventure_id BIGINT NULL,
   training_id BIGINT NULL,
   work_id BIGINT NULL,
   deleted BIT(1) NOT NULL,
   CONSTRAINT pk_occupation PRIMARY KEY (id)
);

CREATE TABLE item (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NOT NULL,
   type VARCHAR(255) NOT NULL,
   amount INT NOT NULL,
   adventure_id BIGINT NULL,
   deleted BIT(1) NOT NULL,
   CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE TABLE `character` (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NULL,
   race VARCHAR(255) NOT NULL,
   character_class VARCHAR(255) NOT NULL,
   status VARCHAR(255) NOT NULL,
   artwork VARCHAR(255) NULL,
   user_id BIGINT NOT NULL,
   statistics_id BIGINT NOT NULL,
   occupation_id BIGINT NULL,
   fight_id BIGINT NULL,
   equipment_id BIGINT NOT NULL,
   deleted BIT(1) NOT NULL,
   CONSTRAINT pk_character PRIMARY KEY (id)
);

CREATE TABLE character_skill (
  level INT NOT NULL,
   deleted BIT(1) NOT NULL,
   character_id BIGINT NOT NULL,
   skill_id BIGINT NOT NULL,
   CONSTRAINT pk_characterskill PRIMARY KEY (character_id, skill_id)
);

CREATE TABLE item_equipment (
  amount INT NOT NULL,
   deleted BIT(1) NOT NULL,
   item_id BIGINT NOT NULL,
   equipment_id BIGINT NOT NULL,
   CONSTRAINT pk_itemequipment PRIMARY KEY (item_id, equipment_id)
);

ALTER TABLE item_equipment ADD CONSTRAINT FK_ITEMEQUIPMENT_ON_EQUIPMENT FOREIGN KEY (equipment_id) REFERENCES equipment (id);

ALTER TABLE item_equipment ADD CONSTRAINT FK_ITEMEQUIPMENT_ON_ITEM FOREIGN KEY (item_id) REFERENCES item (id);

ALTER TABLE fight ADD CONSTRAINT FK_FIGHT_ON_ENEMY FOREIGN KEY (enemy_id) REFERENCES enemy (id);

ALTER TABLE adventure ADD CONSTRAINT FK_ADVENTURE_ON_ENEMY FOREIGN KEY (enemy_id) REFERENCES enemy (id);

ALTER TABLE occupation ADD CONSTRAINT FK_OCCUPATION_ON_ADVENTURE FOREIGN KEY (adventure_id) REFERENCES adventure (id);

ALTER TABLE occupation ADD CONSTRAINT FK_OCCUPATION_ON_TRAINING FOREIGN KEY (training_id) REFERENCES training (id);

ALTER TABLE occupation ADD CONSTRAINT FK_OCCUPATION_ON_WORK FOREIGN KEY (work_id) REFERENCES work (id);

ALTER TABLE item ADD CONSTRAINT FK_ITEM_ON_ADVENTURE FOREIGN KEY (adventure_id) REFERENCES adventure (id);

ALTER TABLE `character` ADD CONSTRAINT uc_character_name UNIQUE (name);

ALTER TABLE `character` ADD CONSTRAINT FK_CHARACTER_ON_EQUIPMENT FOREIGN KEY (equipment_id) REFERENCES equipment (id);

ALTER TABLE `character` ADD CONSTRAINT FK_CHARACTER_ON_FIGHT FOREIGN KEY (fight_id) REFERENCES fight (id);

ALTER TABLE `character` ADD CONSTRAINT FK_CHARACTER_ON_OCCUPATION FOREIGN KEY (occupation_id) REFERENCES occupation (id);

ALTER TABLE `character` ADD CONSTRAINT FK_CHARACTER_ON_STATISTICS FOREIGN KEY (statistics_id) REFERENCES statistics (id);

ALTER TABLE `character` ADD CONSTRAINT FK_CHARACTER_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE character_skill ADD CONSTRAINT FK_CHARACTERSKILL_ON_CHARACTER FOREIGN KEY (character_id) REFERENCES `character` (id);

ALTER TABLE character_skill ADD CONSTRAINT FK_CHARACTERSKILL_ON_SKILL FOREIGN KEY (skill_id) REFERENCES skill (id);