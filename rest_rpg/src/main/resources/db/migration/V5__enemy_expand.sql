CREATE TABLE enemy_strategy (
  id BIGINT AUTO_INCREMENT NOT NULL,
  CONSTRAINT pk_enemystrategy PRIMARY KEY (id)
);

CREATE TABLE strategy_element (
  id BIGINT AUTO_INCREMENT NOT NULL,
  element_event VARCHAR(255) NOT NULL,
  element_action VARCHAR(255) NOT NULL,
  priority INT NOT NULL,
  CONSTRAINT pk_strategyelement PRIMARY KEY (id)
);

CREATE TABLE strategy_element_enemy_strategy (
  enemy_strategy_id BIGINT NOT NULL,
  strategy_element_id BIGINT NOT NULL,
  CONSTRAINT pk_strategy_element_enemy_strategy PRIMARY KEY (enemy_strategy_id, strategy_element_id)
);

ALTER TABLE enemy ADD name VARCHAR(255) NOT NULL;
ALTER TABLE enemy ADD enemy_strategy_id BIGINT NOT NULL;
ALTER TABLE enemy ADD number_of_potions INT NOT NULL;
ALTER TABLE enemy ADD hp INT NOT NULL;
ALTER TABLE enemy ADD damage INT NOT NULL;

ALTER TABLE fight ADD enemy_current_hp INT NULL;

ALTER TABLE strategy_element_enemy_strategy ADD CONSTRAINT fk_streleenestr_on_enemy_strategy FOREIGN KEY (enemy_strategy_id) REFERENCES enemy_strategy (id);

ALTER TABLE strategy_element_enemy_strategy ADD CONSTRAINT fk_streleenestr_on_strategy_element FOREIGN KEY (strategy_element_id) REFERENCES strategy_element (id);