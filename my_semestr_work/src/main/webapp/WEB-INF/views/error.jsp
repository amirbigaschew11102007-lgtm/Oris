<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="ru"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Ошибка — ScoutX</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head><body>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>
<main class="page-wrap">
    <div class="empty-state"><h1>Не получилось выполнить действие</h1>
        <p><c:out value="${error}"/></p><a class="button button-primary" href="${pageContext.request.contextPath}/">Вернуться
            на главную</a></div>
</main>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body></html>
