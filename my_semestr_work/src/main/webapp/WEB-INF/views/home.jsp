<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="scoutx" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
    <title>ScoutX — интересные места рядом</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>
<main class="page-wrap">
    <section class="hero">
        <div class="hero-copy">
            <div class="eyebrow">ГОРОД — ЭТО БОЛЬШЕ, ЧЕМ КАРТА</div>
            <h1>Находи места.<br><span>Делись открытиями.</span></h1>
            <p>ScoutX — лента необычных мест, уличного искусства и городских историй, которые создают сами люди.</p>
            <c:if test="${not empty sessionScope.user}">
                <a class="button button-primary" href="${pageContext.request.contextPath}/places/new">＋ Добавить интересное место</a>
            </c:if>
        </div>
        <div class="hero-art"><div class="orbit orbit-one"></div><div class="orbit orbit-two"></div><div class="hero-pin">⌖</div><span class="hero-label">EXPLORE<br>YOUR CITY</span></div>
    </section>

    <section class="section-heading">
        <div><div class="eyebrow">DISCOVER</div><h2><c:choose><c:when test="${not empty sessionScope.user and not empty sessionScope.user.cityId}">Места твоего города</c:when><c:otherwise>Последние открытия</c:otherwise></c:choose></h2>
        <p><c:choose><c:when test="${not empty sessionScope.user and not empty sessionScope.user.cityId}">Публикации из города <strong><c:out value="${sessionScope.user.cityName}"/></strong></c:when><c:when test="${not empty sessionScope.user}">Ты не указал город в профиле — показываем общую ленту.</c:when><c:otherwise>Войди, чтобы видеть публикации своего города.</c:otherwise></c:choose></p></div>
    </section>

    <c:if test="${empty places}"><div class="empty-state"><div class="empty-icon">⌖</div><h3>Пока здесь тихо</h3><p>В этой ленте пока нет мест. Стань первым, кто поделится открытием!</p><c:if test="${not empty sessionScope.user}"><a class="button button-primary" href="${pageContext.request.contextPath}/places/new">Создать публикацию</a></c:if></div></c:if>

    <div class="place-grid">
        <c:forEach var="place" items="${places}">
            <scoutx:placeCard place="${place}" context="${pageContext.request.contextPath}"/>
        </c:forEach>
    </div>
</main>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
<script>window.SCOUTX={context:'${pageContext.request.contextPath}',csrf:'${requestScope.csrfToken}'};</script>
<script src="${pageContext.request.contextPath}/assets/js/app.js" defer></script>
</body></html>
