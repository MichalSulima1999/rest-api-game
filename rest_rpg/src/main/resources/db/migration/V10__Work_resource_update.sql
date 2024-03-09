ALTER TABLE work ADD resource_type VARCHAR(255) NOT NULL;

UPDATE
  work
SET
  resource_type = 'GOLD'
WHERE resource_type IS NULL;

ALTER TABLE work RENAME COLUMN wage TO resource_amount;

ALTER TABLE equipment ADD iron INT NOT NULL;
ALTER TABLE equipment ADD wood INT NOT NULL;
