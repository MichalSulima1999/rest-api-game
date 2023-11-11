ALTER TABLE fight_effect DROP FOREIGN KEY FK_FIGHTEFFECT_ON_FIGHT_ENEMY;
alter table fight_effect drop COLUMN fight_enemy_id;
ALTER TABLE fight_effect DROP FOREIGN KEY FK_FIGHTEFFECT_ON_FIGHT_PLAYER;
ALTER TABLE fight_effect CHANGE fight_player_id fight_id BIGINT NOT NULL;
ALTER TABLE fight_effect MODIFY fight_id BIGINT NOT NULL;
ALTER TABLE fight_effect ADD CONSTRAINT FK_FIGHTEFFECT_ON_FIGHT FOREIGN KEY (fight_id) REFERENCES fight (id);
ALTER TABLE fight_effect ADD is_player_effect BIT(1) NOT NULL;
ALTER TABLE fight_effect MODIFY skill_effect VARCHAR(255) NOT NULL;
ALTER TABLE fight_effect MODIFY duration INT NOT NULL;
ALTER TABLE fight_effect ADD effect_multiplier FLOAT NOT NULL;

ALTER TABLE fight DROP COLUMN player_turn;

ALTER TABLE skill ADD magic_damage BIT(1) NOT NULL;
ALTER TABLE skill MODIFY multiplier_per_level FLOAT NOT NULL;
ALTER TABLE skill MODIFY effect_duration INT NOT NULL;
ALTER TABLE skill MODIFY effect_duration_per_level INT NOT NULL;
ALTER TABLE skill MODIFY effect_multiplier FLOAT NOT NULL;
ALTER TABLE skill MODIFY effect_multiplier_per_level FLOAT NOT NULL;

ALTER TABLE statistics DROP COLUMN damage;
ALTER TABLE statistics DROP COLUMN magic_damage;
ALTER TABLE statistics DROP COLUMN armor;
ALTER TABLE statistics DROP COLUMN dodge_chance;
ALTER TABLE statistics DROP COLUMN critical_chance;
ALTER TABLE statistics DROP COLUMN xp_to_next_level;

ALTER TABLE item DROP FOREIGN KEY FK_ITEM_ON_ADVENTURE;
ALTER TABLE item DROP COLUMN adventure_id;
ALTER TABLE item DROP COLUMN amount;
ALTER TABLE item ADD price INT NOT NULL;
ALTER TABLE item ADD power INT NOT NULL;

ALTER TABLE equipment ADD armor_id BIGINT NULL;
ALTER TABLE equipment ADD CONSTRAINT FK_EQUIPMENT_ON_ARMOR FOREIGN KEY (armor_id) REFERENCES item (id);
ALTER TABLE equipment ADD weapon_id BIGINT NULL;
ALTER TABLE equipment ADD CONSTRAINT FK_EQUIPMENT_ON_WEAPON FOREIGN KEY (weapon_id) REFERENCES item (id);
ALTER TABLE equipment ADD health_potions INT NOT NULL;

ALTER TABLE item_equipment DROP FOREIGN KEY FK_ITEMEQUIPMENT_ON_EQUIPMENT;
ALTER TABLE item_equipment DROP FOREIGN KEY FK_ITEMEQUIPMENT_ON_ITEM;
DROP TABLE item_equipment;

UPDATE
  fight
SET
  enemy_current_hp = '0'
WHERE
  enemy_current_hp IS NULL;
ALTER TABLE fight MODIFY enemy_current_hp INT NOT NULL;

UPDATE
  fight
SET
  enemy_current_mana = '0'
WHERE
  enemy_current_mana IS NULL;
ALTER TABLE fight MODIFY enemy_current_mana INT NOT NULL;
UPDATE
  skill
SET
  effect_duration = '0'
WHERE
  effect_duration IS NULL;
