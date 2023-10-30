ALTER TABLE adventure ADD xp_for_adventure INT NOT NULL;
ALTER TABLE adventure ADD gold_for_adventure INT NOT NULL;

ALTER TABLE adventure
RENAME COLUMN adventure_time to adventure_time_in_minutes;

ALTER TABLE adventure MODIFY adventure_time_in_minutes INT NOT NULL;
