DELETE FROM user_roles;
DELETE FROM visits;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password, expert) VALUES
  ('Терапевт', 'user@yandex.ru', '{noop}password', 'Терапевт'),
  ('Администратор', 'admin@gmail.com', '{noop}admin', 'Админ'),
  ('Невролог', 'user@gmail.com', '{noop}admin', 'Невролог');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001),
  ('ROLE_USER', 100001),
  ('ROLE_USER', 100002);

INSERT INTO visits (date_time, description, expert, user_id)
VALUES ('2015-05-30 10:00:00', 'Ванюков А.М.', 'Терапевт', 100000),
       ('2015-05-30 13:00:00', 'Петров Ю.А.', 'Терапевт', 100000),
       ('2015-05-30 20:00:00', 'Галыгин П.Р.', 'Невролог', 100002),
       ('2015-05-31 10:00:00', 'Незлобин Г.Б.', 'Невролог', 100002),
       ('2015-05-31 13:00:00', 'Незлобин Г.Б.', 'Невролог', 100002),
       ('2015-05-31 20:00:00', 'Пушкин А.С.', 'Невролог', 100002),
       ('2015-06-01 14:00:00', 'Рыжов Н.А.', 'Терапевт', 100000),
       ('2015-06-01 21:00:00', 'Петров Ю.А.', 'Терапевт', 100000);
