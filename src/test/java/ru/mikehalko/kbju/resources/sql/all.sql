-- kbju sql

-- users
DROP TABLE IF EXISTS "meal";
DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS "user_credential";

CREATE TABLE "user" (
                        user_id SERIAL PRIMARY KEY,
                        name VARCHAR(30) NOT NULL,
                        calories_min INT,
                        calories_max INT
);

-- meals
CREATE TABLE "meal" (
                        meal_id SERIAL PRIMARY KEY,
                        user_id INT NOT NULL,
                        date_time TIMESTAMP,
                        description VARCHAR(50),
                        mass INT,
                        proteins INT,
                        fats INT,
                        carbohydrates INT,
                        calories INT,
                        FOREIGN KEY (user_id) REFERENCES "user" (user_id) ON DELETE CASCADE
);

CREATE TABLE "user_credential" (
                                   user_id INT NOT NULL,
                                   name VARCHAR(30) NOT NULL,
                                   password VARCHAR(100) NOT NULL,
                                   FOREIGN KEY (user_id) REFERENCES "user" (user_id) ON DELETE CASCADE,
);

INSERT INTO "user" (name, calories_min, calories_max)
VALUES ('user1', 800, 1300),
       ('user2', 1000, 1500),
       ('user3', 1800, 2000);

INSERT INTO "meal" (user_id, date_time,
                    description, mass, proteins, fats,
                    carbohydrates, calories)
VALUES
    (1, '2023-09-02 09:23:54', 'Яичница (2 яйца) с сыром и зеленью', 174, 28, 44, 0, 512),
    (1, '2023-09-02 13:10:20', 'Блины из кабачков', 297, 15, 21, 50, 472),
    (1, '2023-09-02 20:43:42', 'Фаршированные перцы с рисом и овощами в духовке', 369, 11, 15, 66, 417),

    (1, '2023-09-03 09:15:20', 'Запеканка из творога с изюмом', 318, 35, 32, 51, 645),
    (1, '2023-09-03 14:00:00', 'Суп Том ям', 848, 25, 17, 34, 288),
    (1, '2023-09-03 19:50:32', 'Чахохбили из курицы по-грузински', 218, 22, 24, 7, 341);

INSERT INTO "user_credential" (user_id, name, password)
VALUES
    (1, 'USER_1', '1'),
    (2, 'USER_2', '2'),
    (3, 'USER_3', '3');

select * from "user";
select * from "user_credential";
select * from "meal";

select * from "user_credential" INNER JOIN "user" u on u.user_id = user_credential.user_id
                                INNER JOIN meal m on user_credential.user_id = m.user_id;

