<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../html/header.html" %>

<!DOCTYPE html>
<html>
<head>
    <title>Все пользователи</title>
    <style>
        /* Немного базовых стилей, чтобы таблица выглядела нормально */
        body { font-family: Arial, sans-serif; }
        table { border-collapse: collapse; width: 60%; margin: 20px auto; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }
        th { background-color: #f2f2f2; }
        h2 { text-align: center; }
    </style>
</head>
<body>

<h2>Список зарегистрированных пользователей</h2>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Имя</th>
        <th>Фамилия</th>
    </tr>
    </thead>
    <tbody>
    <%-- Перебираем список из атрибута userForJsp --%>
    <c:forEach var="user" items="${userForJsp}">
        <tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.email}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<%-- Выводим сообщение, если список пуст --%>
<c:if test="${empty userForJsp}">
    <p style="text-align: center; color: red;">Пользователи не найдены.</p>
</c:if>

</body>
</html>