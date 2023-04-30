### Filmorate<br/>
Проект (аналог Кинопоиска) - социальная сеть, которая предлагает рекомендации кинопроката, основанные на том, какие фильмы смотрит пользователь и его друзья, а также на оценках, которые они ставят им.
____
### Возможности проекта
- создание, обновление фильмов;
- создание и обновление пользователей;
- добавление пользователей в друзья;
- удаление пользователей из друзей;
- возможность отметки понравившихся фильмов;
- вывод лучших фильмов (в зависимости от количества лайков).
____
### Освоенные технологии и навыки
RESTful приложение с использованием Spring Boot, Maven, Lombok, а также JDBC-запросов для взаимодействия с базой данных H2.
____
### Database scheme

![Database scheme](Filmorate.png)
**Схема состоит из 7 таблиц:**
- films
- users
- genres
- film_genre
- ratings
- likes
- friendship

**Связи между таблицами обозначены линиями:**
- *-1 связь many to one

**Примеры основных запросов для Film:**

1) метод getAll():
   ``` sql
   SELECT *
   FROM films;
   ``` 
2) метод getById(int id):
   ``` sql
   SELECT *
   FROM films
   WHERE film_id=id;
   ``` 
3) метод getTopFilmCount(int count):
   ``` sql
   SELECT *
   FROM films AS f
   RIGHT JOIN (SELECT film_id
               FROM likes
               GROUP BY film_id
               ORDER BY COUNT(user_id) DESC) AS l ON (l.film_id = f.film_id)
   LIMIT count;

**Примеры основных запросов для User:**

1) метод getAll():
   ``` sql
   SELECT *
   FROM users;
   ``` 

2) метод getById(int id):
   ``` sql
   SELECT *
   FROM users
   WHERE user_id=id;
   ``` 
3) метод getUserFriends(int id):

``` sql
   SELECT user_id2
   FROM friendship
   WHERE user_id1 = id
     AND status = true
   UNION
   SELECT user_id1
   FROM friendship
   WHERE user_id2 = id
     AND status = true;
   ``` 

4) метод getCommonFriends(int id, int otherId):

``` sql
   (SELECT user_id2
   FROM friendship
   WHERE user_id1 = id
     AND status = true
   UNION
   SELECT user_id1
   FROM friendship
   WHERE user_id2 = id
     AND status = true)
   INTERSECT
   (SELECT user_id2
   FROM friendship
   WHERE user_id1 = otherId
     AND status = true
   UNION
   SELECT user_id1
   FROM friendship
   WHERE user_id2 = otherId
     AND status = true);
   ```
   
