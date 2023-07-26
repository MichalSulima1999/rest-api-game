ALTER TABLE `character` ADD sex VARCHAR(255) NOT NULL;

ALTER TABLE `character` MODIFY occupation_id BIGINT NOT NULL;
ALTER TABLE `character` MODIFY fight_id BIGINT NOT NULL;
ALTER TABLE `character` MODIFY name VARCHAR(255) NOT NULL;
ALTER TABLE `character` MODIFY artwork VARCHAR(255) NOT NULL;

ALTER TABLE statistics ADD free_statistic_points INT NOT NULL;