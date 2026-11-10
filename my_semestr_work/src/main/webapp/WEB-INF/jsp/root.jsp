<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../html/header.html" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Главная</title>
</head>
<body>

<h1>Последние посты</h1>

<c:if test="${empty AllPlacesOnYourCity}">
    <p>Постов пока нет.</p>
</c:if>

<c:forEach var="post" items="${AllPlacesOnYourCity}">
    <article>
        <h2><c:out value="${post.title}"/></h2>
        <p><c:out value="${post.description}"/></p>
        <small><c:out value="${post.createdAt}"/></small>
    </article>
    <hr>
</c:forEach>

</body>
</html>