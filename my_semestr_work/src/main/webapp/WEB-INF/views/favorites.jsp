<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="scoutx" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html><html lang="ru"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Избранное — ScoutX</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head><body>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>
<main class="page-wrap">
    <div class="section-heading">
        <div>
            <div class="eyebrow">YOUR COLLECTION</div>
            <h1>Избранные места</h1>
            <p class="muted">Места, которые ты сохранил.</p></div>
    </div>
    <c:if test="${empty places}">
        <div class="empty-state"><h3>Пока ничего нет</h3>
            <p>Сохраняй интересные места, чтобы быстро находить их снова.</p><a class="button button-primary"
                                                                                href="${pageContext.request.contextPath}/">Перейти
                в ленту</a></div>
    </c:if>
    <div class="place-grid"><c:forEach var="place" items="${places}"><scoutx:placeCard place="${place}"
                                                                                       context="${pageContext.request.contextPath}"/></c:forEach></div>
</main>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
<script>window.SCOUTX = {context: '${pageContext.request.contextPath}', csrf: '${requestScope.csrfToken}'};</script>
<script src="${pageContext.request.contextPath}/assets/js/app.js" defer></script>
</body></html>
