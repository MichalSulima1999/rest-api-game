ALTER TABLE skill ADD gold_cost INT NOT NULL;
ALTER TABLE skill ADD statistic_points_cost INT NOT NULL;

UPDATE
  skill
SET
  gold_cost = '0'
WHERE
  gold_cost IS NULL;

UPDATE
  skill
SET
  statistic_points_cost = '0'
WHERE
  statistic_points_cost IS NULL;

ALTER TABLE occupation MODIFY training_id BIGINT NOT NULL;

ALTER TABLE occupation DROP FOREIGN KEY FK_OCCUPATION_ON_TRAINING;
ALTER TABLE occupation DROP COLUMN training_id;
DROP TABLE training;
