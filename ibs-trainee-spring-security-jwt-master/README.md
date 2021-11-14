Для успешной аутентификации в сервисе необходимо выполнить следующий запрос

POST http://localhost:8080/login 
{"username" : "oliver","password" : "password"}

Из заголовков ответа взять значение заголовка Authorization

Все последующие обращение к ресурсам сервиса делать с добавлением в заголовки
Authorization  Bearer {token}

////

Refresh token выдается при авторизации по логину и паролю в headers ответа. 
Запрос обновления на
localhost:8080/login/token/{username}
в Header KEY RFTOKEN.
{username} должен соответствовать имени пользователя,
который обновляет токен
... 
scrum password123