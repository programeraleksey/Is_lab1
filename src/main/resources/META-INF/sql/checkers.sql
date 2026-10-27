ALTER TABLE worker ADD CONSTRAINT worker_id_positive CHECK (id > 0);
ALTER TABLE worker ADD CONSTRAINT worker_name_not_empty CHECK (length(trim(name)) > 0);
ALTER TABLE worker ADD CONSTRAINT worker_salary_valid CHECK (salary > 0);
ALTER TABLE worker ADD CONSTRAINT worker_rating_valid CHECK (rating > 0);
ALTER TABLE worker ADD CONSTRAINT worker_position_valid CHECK (position in ('HUMAN_RESOURCES', 'HEAD_OF_DIVISION', 'HEAD_OF_DEPARTMENT', 'DEVELOPER'));

ALTER TABLE coordinates ADD CONSTRAINT coordinates_y_valid CHECK (y > -612);


ALTER TABLE organization ADD CONSTRAINT organization_annual_turnover_positive CHECK (annual_turnover > 0);
ALTER TABLE organization ADD CONSTRAINT organization_employees_count_positive CHECK (employees_count > 0);
ALTER TABLE organization ADD CONSTRAINT organization_full_name_not_empty CHECK (length(trim(full_name)) > 0);
ALTER TABLE organization ADD CONSTRAINT organization_rating_positive CHECK (rating > 0);


ALTER TABLE person ADD CONSTRAINT person_eye_color_valid CHECK (eye_color in ('RED', 'YELLOW', 'BROWN'));
ALTER TABLE person ADD CONSTRAINT person_hair_color_valid CHECK (hair_color in ('RED', 'YELLOW', 'BROWN'));
ALTER TABLE person ADD CONSTRAINT person_nationality_valid CHECK (nationality in ('UNITED_KINGDOM', 'USA', 'GERMANY', 'CHINA', 'THAILAND'));

ALTER TABLE address ADD CONSTRAINT address_street_not_empty CHECK (length(trim(street)) > 0);