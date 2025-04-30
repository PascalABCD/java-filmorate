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
SELECT films.name, COUNT(film_likes.user_id) as likes
FROM films
LEFT JOIN film_likes ON films.film_id = film_likes.film_id
GROUP BY films.id
ORDER BY likes DESC
LIMIT 5;
```

Получить все жанры фильма:
```sql
SELECT genres.name
FROM film_genre
JOIN genres ON film_genre.genre_id = genres.id
WHERE film_genre.film_id = 10;
```