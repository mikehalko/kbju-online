  ## Содержание
1. [Основное](#link1)
    - [ссылка на сайт](#link1.1)
    - [описание](#link1.2)
    - [использование](#link1.3)
1. [Стек](#link2)
    - [работа приложения](#link2.1)
    - [база данных](#link2.2)
    - [о популярных фреймворках](#link2.3)
3. [Слои](#link3)
3. [Планируется](#link4)

---

<div id='link1'/>
# kbju-online
> An assistant for controlling consumption calories, proteins, fats, carbohydrates.

  
### ссылка на сайт
[KBJU](http://defezis.ru/kbju/)
> дизайн сайта, структура html обусловлены тем, что визуальная часть существует для демонстрации реальной работы приложения и только.

## Описание
> Приложение позволяет контроллировать потребление калорий.
Использует верхнюю и нижнюю границу суточного потребления калорий, чтобы отслеживать недоедание/переедание пищи.

## Использование
Пользователь заносит данные (*создание*) об употреблении пищи и сохраняет их. \
Он может *редактировать* существующую запись и *удалять* её, просматривать весь *список* записей, где критичные значения будут подсвечены особыми цветами. \
Помимо этого, пользователь имеет возможность менять пароль, имя (но не логин), границы потребления калорий.

<br>
<hr>

# Стек
### Сборка Maven
- java 11
- javax servlet, servlet-api, javaee-api
- logback 1.4.9
- junit 4.13
- postgresql 42.4.3

### Работа приложения - Tomcat, Docker, Linux
Осуществляется в контейнере сервлетов *Tomcat*, который размещен в *Docker* контейнере на выделенном *Linux* VDS сервере.

### PostgreSQL - база данных
БД размещена в отдельном *Docker* контейнере на той же *Linux* машине.

### Hibernate/Spring/SpringMVC - пока не используется
Приложение написано на языке *Java*, с помощью *Servlet API* и собственной реализацией *валидирования* данных, *аутентификации* пользователей, без использования популярных фреймворков.
> Мной уже использованы упомянутые технологии в других пет-проектах. \
> Разработка усложнена для углубленного обучения основам Java программирования на реальном проекте. Тем не менее, в будущих обновления, я внесу Spring в проект. Затем JPA.

<br>
<hr>

## Слои
  - **модель**: 2 основных сущности (пользователь (user), приём пищи (meal) и 3 вспомогательные (пищевая ценность(nutritionally), логин-пароль(userCredentials), transfer object (mealTo));
  - **репозиторий**: 2 реализации, первая - in-memory, позже добавилась вторая - SQL;
  - **service**, **controller**: пока не используются, нужны для будущего введения Spring;
<br>

  - **веб**: 3 сервлета (user, meal, login) и context для инициализации подключения и констант полей (для БД) из properties, а также HttpFilter фильтр аутентификации;
  - **фронтенд**: jsp, css, js - реализация простая, для демонстрации работы приложения и дополнительного более реального опыта разработки;
<br>

  - **подключение к БД**: потеря соединения и повторное подключение контроллируется "обёрткой" над Connection;
  - **валидация**: собственная реализация, использующая Enum классы, где перечисления (константы) соотвествуют полям моделей;
<br>

  - **тесты**: unit-тесты, написаны по принципу независимости друг от друга. Используют скрипты заполнения БД тестовыми данными.
  - **util**: множество утильных классов, например - request parameter парсер для параметров-полей в запросе, позволяющего определять пустые и несуществующие параметры, испозьзующий Enum класс, для удобства разработки и прозрачности работы кода

<br>
<hr>

<div id='link4'/>
  
## Планируется
  - Выделить общий класс сущностей (Meal и User)
  - Рефакторинг, рефакторинг, рефакторинг..
  - Больше тестов: пока покрытие небольшое, написано для SQL реализаций репозитория. 
  - Локализация: английский и русский языки. В коде уже используются константы, добавление такого функционала не должно быть сложным.
  - Spring (Web, MVC, Security, Validation...)
  - JPA (jdbc -> Hibernate)
  - Кэширование и Транзакционность
  - RESTful
  - Профили Spring и Maven для переключения между различными реализациями
  - Миграция на JUnit 5
