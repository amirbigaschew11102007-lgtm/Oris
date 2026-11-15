<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="ru"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Регистрация — ScoutX</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head><body class="auth-page">
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>
<main class="auth-card auth-card-wide">
    <div class="eyebrow">СТАНЬ ЧАСТЬЮ СООБЩЕСТВА</div>
    <h1>Создать аккаунт</h1>
    <p class="muted">Укажи город, чтобы видеть открытия поблизости. Город можно не выбирать.</p>
    <c:if test="${not empty error}">
        <div class="notice notice-error"><c:out value="${error}"/></div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/register" class="form-stack">
        <input type="hidden" name="_csrf" value="${requestScope.csrfToken}">
        <label>Имя пользователя<input name="username" required minlength="3" maxlength="50"
                                      autocomplete="username"></label>
        <label>Email<input type="email" name="email" required maxlength="255" autocomplete="email"></label>
        <label>Город<select name="cityId">
            <option value="">Не указывать город</option>
            <c:forEach var="city" items="${cities}">
                <option value="${city.id}"><c:out value="${city.name}"/>, <c:out value="${city.country}"/></option>
            </c:forEach></select></label>
        <label>Пароль<input type="password" name="password" required minlength="8" autocomplete="new-password"></label>
        <label>Повтори пароль<input type="password" name="confirmPassword" required minlength="8"
                                    autocomplete="new-password"></label>
        <button class="button button-primary button-wide" type="submit">Зарегистрироваться</button>
    </form>
    <p class="form-foot">Уже есть аккаунт? <a href="${pageContext.request.contextPath}/login">Войти</a></p></main>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
<script>window.SCOUTX = {context: '${pageContext.request.contextPath}', csrf: '${requestScope.csrfToken}'};</script>
<script src="${pageContext.request.contextPath}/assets/js/app.js" defer></script>
</body></html>
