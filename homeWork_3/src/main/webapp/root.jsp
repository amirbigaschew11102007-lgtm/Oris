<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Главная страница</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f5f5f5;
        }
        .buttons-container {
            display: flex;
            gap: 20px;
        }
        .btn {
            display: inline-block;
            padding: 15px 40px;
            font-size: 18px;
            text-decoration: none;
            color: white;
            border-radius: 8px;
            transition: background-color 0.3s, transform 0.2s;
            text-align: center;
        }
        .btn-save {
            background-color: #4CAF50;
        }
        .btn-save:hover {
            background-color: #45a049;
            transform: translateY(-2px);
        }
        .btn-search {
            background-color: #2196F3;
        }
        .btn-search:hover {
            background-color: #1976D2;
            transform: translateY(-2px);
        }
    </style>
</head>
<body>

<div class="buttons-container">
    <a href="/homeWork_3_war/save" class="btn btn-save">Save</a>
    <a href="/homeWork_3_war/search?field=name&value=Amir" class="btn btn-search">Search</a>
</div>

</body>
</html>