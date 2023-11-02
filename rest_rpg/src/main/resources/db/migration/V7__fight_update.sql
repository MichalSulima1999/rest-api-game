ALTER TABLE character_table
DROP COLUMN fight_id;

ALTER TABLE occupation ADD fight_id BIGINT NOT NULL;
ALTER TABLE occupation ADD CONSTRAINT FK__occupation_fight FOREIGN KEY (fight_id) REFERENCES fight (id);

ALTER TABLE fight ADD is_active BIT(1) NOT NULL;