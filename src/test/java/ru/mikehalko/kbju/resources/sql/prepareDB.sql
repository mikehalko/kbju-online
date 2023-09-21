-- users
DROP TABLE IF EXISTS "meal_test";
DROP TABLE IF EXISTS "user_credential_test";
DROP TABLE IF EXISTS "user_test";

CREATE TABLE "user_test" (
                        user_test_id SERIAL PRIMARY KEY,
                        name VARCHAR(30) NOT NULL,
                        calories_min INT,
                        calories_max INT
);

-- meals
CREATE TABLE "meal_test" (
                        meal_test_id SERIAL PRIMARY KEY,
                        user_test_id INT NOT NULL,
                        date_time TIMESTAMP,
                        description VARCHAR(50),
                        mass INT,
                        proteins INT,
                        fats INT,
                        carbohydrates INT,
                        calories INT,
                        FOREIGN KEY (user_test_id) REFERENCES "user_test" (user_test_id) ON DELETE CASCADE
);

CREATE TABLE "user_credential_test" (
                             user_test_id INT NOT NULL,
                             name VARCHAR(30) NOT NULL,
                             password VARCHAR(100) NOT NULL,
                             FOREIGN KEY (user_test_id) REFERENCES "user_test" (user_test_id) ON DELETE CASCADE
);