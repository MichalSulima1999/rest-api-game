ALTER TABLE user ADD enabled BIT(1) NOT NULL;
ALTER TABLE user ADD verification_code VARCHAR(64) NOT NULL;
