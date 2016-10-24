SET SCHEMA 'test_schema';

DELETE FROM person;

INSERT INTO person (person_id, first_name, last_name, age)
VALUES
  (nextval('person_seq'), 'John', 'Doe', 33),
  (nextval('person_seq'), 'Jane', 'Doe', 31);

DELETE FROM employer;

INSERT INTO employer (employer_code, name, description)
VALUES ('epam', 'EPAM', 'epam.com');
INSERT INTO employer (employer_code, name, description)
VALUES ('google', 'Google', 'google.com');
INSERT INTO employer (employer_code, name, description)
VALUES ('ya', 'Yandex', 'yandex.ru');
INSERT INTO employer (employer_code, name, description)
VALUES ('abc', 'ABC', 'abc description');

DELETE FROM position;

INSERT INTO position (position_code, name, description)
  SELECT
    'dev',
    'Developer',
    'Application developer'
  UNION ALL SELECT
              'QA',
              'QA',
              'Quality assurance'
  UNION ALL SELECT
              'BA',
              'BA',
              'Business analyst';

DELETE FROM employee;

INSERT INTO employee (employee_id, person_id)
  SELECT
    1, (SELECT person.person_id FROM person WHERE first_name = 'John')
  UNION ALL SELECT
              2, (SELECT person.person_id FROM person WHERE first_name = 'Jane');