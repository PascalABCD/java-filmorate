# java-filmorate
Template repository for Filmorate project.

![Data Base scheme](filmorate.png)

Получить всех друзей пользователя:
``` sql
SELECT followed_user_id 
FROM friends
WHERE user_id = 1;
```

Получить топ-5 самых популярных фильмов (по количеству лайков):
```sql
SELECT f.name, COUNT(fl.user_id) as likes
FROM films f
LEFT JOIN film_likes fl ON f.film_id = fl.film_id
GROUP BY f.id
ORDER BY likes DESC
LIMIT 5;
```

Получить все жанры фильма:
```sql
SELECT g.name
FROM film_genres f
JOIN genres g ON f.genre_id = g.id
WHERE f.film_id = 10;
```