<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Добавление пользователей</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f8;
            margin: 0;
            padding: 30px;
        }

        .container {
            max-width: 700px;
            margin: 0 auto;
            background: white;
            padding: 25px 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        h1 {
            text-align: center;
            color: #333;
        }

        .user-block {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 15px 20px;
            margin-bottom: 20px;
            background: #fafafa;
        }

        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #555;
        }

        input {
            width: 100%;
            padding: 8px;
            margin-bottom: 12px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
        }

        button {
            width: 100%;
            padding: 12px;
            background: #2f81f7;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
        }

        .error {
            color: #c62828;
            text-align: center;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>Добавление пользователей</h1>

    <%
        Integer count = (Integer) request.getAttribute("count");

        if (count == null || count <= 0) {
    %>

        <p class="error">
            Не удалось получить количество пользователей из базы данных.
        </p>

    <%
        } else {
    %>

        <form method="post" action="${pageContext.request.contextPath}/save">

            <%
                for (int i = 1; i <= count; i++) {
            %>

                <div class="user-block">
                    <h3>Пользователь <%= i %></h3>

                    <label for="name_<%= i %>">Имя</label>
                    <input type="text"
                           id="name_<%= i %>"
                           name="name"
                           required>

                    <label for="email_<%= i %>">Email</label>
                    <input type="email"
                           id="email_<%= i %>"
                           name="email"
                           required>

                    <label for="age_<%= i %>">Возраст</label>
                    <input type="number"
                           id="age_<%= i %>"
                           name="age"
                           min="1"
                           max="120"
                           required>

                    <label for="city_<%= i %>">Город</label>
                    <input type="text"
                           id="city_<%= i %>"
                           name="city"
                           required>
                </div>

            <%
                }
            %>

            <button type="submit">Сохранить пользователей</button>

        </form>

    <%
        }
    %>
</div>

</body>
</html>