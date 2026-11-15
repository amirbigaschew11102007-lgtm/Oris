<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="ru"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Вход — ScoutX</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head><body class="auth-page">
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>
<main class="auth-card"><div class="eyebrow">СНОВА РАДЫ ТЕБЯ ВИДЕТЬ</div><h1>Войти в ScoutX</h1><p class="muted">Продолжай исследовать свой город.</p>
<c:if test="${not empty error}"><div class="notice notice-error"><c:out value="${error}"/></div></c:if>
<form method="post" action="${pageContext.request.contextPath}/login" class="form-stack">
<input type="hidden" name="_csrf" value="${requestScope.csrfToken}">
<label>Email или имя пользователя<input name="login" required maxlength="255" autocomplete="username"></label>
<label>Пароль<input type="password" name="password" required autocomplete="current-password"></label>
<button class="button button-primary button-wide" type="submit">Войти</button>
</form><p class="form-foot">Нет аккаунта? <a href="${pageContext.request.contextPath}/register">Зарегистрироваться</a></p></main>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
<script>window.SCOUTX={context:'${pageContext.request.contextPath}',csrf:'${requestScope.csrfToken}'};</script><script src="${pageContext.request.contextPath}/assets/js/app.js" defer></script></body></html>
