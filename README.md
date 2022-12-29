# java-filmorate

Template repository for Filmorate project.
![Database scheme](Filmorate.png)
Схема состоит из 7 таблиц:
- films
- users
- genres
- film_genre
- ratings
- likes
- friendship

  Связи между таблицами обозначены линиями:
- *-1 связь many to one

Примеры основных запросов для Film:

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

Примеры основных запросов для User:

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
   