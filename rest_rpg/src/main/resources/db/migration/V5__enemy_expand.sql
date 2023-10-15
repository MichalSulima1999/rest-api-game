CREATE TABLE strategy_element (
  id BIGINT AUTO_INCREMENT NOT NULL,
  element_event VARCHAR(255) NOT NULL,
  element_action VARCHAR(255) NOT NULL,
  priority INT NOT NULL,
  CONSTRAINT pk_strategyelement PRIMARY KEY (id)
);

CREATE TABLE strategy_element_enemy (
  enemy_id BIGINT NOT NULL,
  strategy_element_id BIGINT NOT NULL,
  CONSTRAINT pk_strategy_element PRIMARY KEY (enemy_id, strategy_element_id)
);

ALTER TABLE enemy ADD name VARCHAR(255) NOT NULL;
ALTER TABLE enemy ADD number_of_potions INT NOT NULL;
ALTER TABLE enemy ADD hp INT NOT NULL;
ALTER TABLE enemy ADD damage INT NOT NULL;
ALTER TABLE enemy ADD mana INT NOT NULL;
ALTER TABLE enemy ADD skill_level INT NOT NULL;
ALTER TABLE enemy ADD skill_id BIGINT NULL;

CREATE TABLE fight_effect (
   id BIGINT AUTO_INCREMENT NOT NULL,
   fight_player_id BIGINT NULL,
   fight_enemy_id BIGINT NULL,
   skill_effect VARCHAR(255) NULL,
   duration INT NULL,
   CONSTRAINT pk_fighteffect PRIMARY KEY (id)
);

ALTER TABLE fight ADD enemy_current_hp INT NULL;
ALTER TABLE fight ADD enemy_current_mana INT NULL;

ALTER TABLE skill DROP COLUMN race;
ALTER TABLE skill ADD character_class NULL;

ALTER TABLE strategy_element_enemy ADD CONSTRAINT fk_streleenestr_on_enemy FOREIGN KEY (enemy_id) REFERENCES enemy (id);

ALTER TABLE strategy_element_enemy ADD CONSTRAINT fk_streleenestr_on_strategy_element FOREIGN KEY (strategy_element_id) REFERENCES strategy_element (id);

ALTER TABLE fight_effect ADD CONSTRAINT FK_FIGHTEFFECT_ON_FIGHT_ENEMY FOREIGN KEY (fight_enemy_id) REFERENCES fight (id);

ALTER TABLE fight_effect ADD CONSTRAINT FK_FIGHTEFFECT_ON_FIGHT_PLAYER FOREIGN KEY (fight_player_id) REFERENCES fight (id);

ALTER TABLE enemy ADD CONSTRAINT FK_ENEMY_ON_SKILL FOREIGN KEY (skill_id) REFERENCES skill (id);