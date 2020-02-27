DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2015-05-30 10:00:00', 'Завтрак', 500),
       (100000, '2015-05-30 13:00:00', 'Обед', 1000),
       (100000, '2015-05-30 20:00:00', 'Ужин', 500),
       (100000, '2015-05-31 10:00:00', 'Завтрак', 500),
       (100000, '2015-05-31 13:00:00', 'Обед', 1000),
       (100000, '2015-05-31 20:00:00', 'Ужин', 503),
       (100001, '2015-05-30 10:00:00', 'Завтрак', 500),
       (100001, '2015-05-30 13:00:00', 'Обед', 1000),
       (100001, '2015-05-30 20:00:00', 'Ужин', 500),
       (100001, '2015-05-31 10:00:00', 'Завтрак', 500),
       (100001, '2015-05-31 13:00:00', 'Обед', 1000),
       (100001, '2015-05-31 20:00:00', 'Ужин', 503)