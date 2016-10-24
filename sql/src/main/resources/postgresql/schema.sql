DROP SCHEMA test_schema CASCADE;

CREATE SCHEMA IF NOT EXISTS test_schema;

SET SCHEMA 'test_schema';

CREATE TABLE person (
  person_id  INTEGER PRIMARY KEY,
  first_name VARCHAR(255),
  last_name  VARCHAR(255),
  age        INTEGER
);

CREATE TABLE employee (
  employee_id INTEGER PRIMARY KEY,
  person_id   INTEGER,
  FOREIGN KEY (person_id) REFERENCES person (person_id)
);

CREATE TABLE position (
  position_code VARCHAR(64) PRIMARY KEY,
  name          VARCHAR(255),
  description   VARCHAR(255)
);

CREATE TABLE employer (
  employer_code VARCHAR(64) PRIMARY KEY,
  name          VARCHAR(255),
  description   VARCHAR(255)
);

CREATE TABLE job_history (
  employee_id   INTEGER,
  position_code VARCHAR(64),
  employer_code VARCHAR(64),
  duration      INTEGER,
  FOREIGN KEY (employee_id) REFERENCES employee (employee_id),
  FOREIGN KEY (employer_code) REFERENCES employer (employer_code),
  FOREIGN KEY (position_code) REFERENCES position (position_code)
);

CREATE SEQUENCE person_seq;

ALTER TABLE person
  ADD COLUMN email VARCHAR(255);

CREATE UNIQUE INDEX /*email*/
  ON person (email);

CREATE TABLE login (
  person_id INTEGER PRIMARY KEY,
  login     VARCHAR(255) NOT NULL,
  email     VARCHAR(255) NOT NULL
);

ALTER TABLE login
  ADD /*CONSTRAINT login_person_id_fkey*/ FOREIGN KEY (person_id) REFERENCES person (person_id)