DELETE FROM meal_test;
DELETE FROM user_credential_test;
DELETE FROM user_test;

INSERT INTO "user_test" (name, calories_min, calories_max)
VALUES ('USER_1', 1000, 1800),
       ('USER_2', 1500, 2500),
       ('USER_3', 1900, 2800),
       ('USER_4', 1200, 2200),
       ('USER_5', 1300, 2300);

INSERT INTO "meal_test" (user_test_id, date_time,
                         description, mass, proteins, fats,
                         carbohydrates, calories)
VALUES
    (1, '2023-09-02 09:23:54', 'test_1_day-1', 174, 28, 44, 0, 512),
    (1, '2023-09-02 13:10:20', 'test_2_day-1', 297, 15, 21, 50, 472),
    (1, '2023-09-02 20:43:42', 'test_3_day-1', 369, 11, 15, 66, 417), -- sum cal 1401

    (1, '2023-09-03 09:15:20', 'test_4_day-2', 318, 35, 32, 51, 445),
    (1, '2023-09-03 14:00:00', 'test_5_day-2', 848, 25, 17, 34, 288),
    (1, '2023-09-03 19:50:32', 'test_6_day-2', 218, 22, 24, 7, 241), -- sum cal 974

    (1, '2023-09-03 09:15:20', 'test_7_day-3', 318, 35, 32, 51, 945),
    (1, '2023-09-03 14:00:00', 'test_8_day-3', 848, 25, 17, 34, 588),
    (1, '2023-09-03 19:50:32', 'test_9_day-3', 218, 22, 24, 7, 641); -- sum cal 2174

INSERT INTO "user_credential_test" (user_test_id, name, password)
VALUES (1, 'USER_1', '1'),
       (2, 'USER_2', '2'),
       (3, 'USER_3', '3'),
       (4, 'USER_4', '4'),
       (5, 'USER_5', '5');