<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="ru"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title><c:choose><c:when
        test="${not empty place}">Редактировать место</c:when><c:otherwise>Новое место</c:otherwise></c:choose> — ScoutX</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head><body>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>
<main class="page-wrap narrow-wrap">
    <div class="form-page-head">
        <div class="eyebrow">ТВОЁ ОТКРЫТИЕ</div>
        <h1><c:choose><c:when
                test="${not empty place}">Редактировать место</c:when><c:otherwise>Добавить интересное место</c:otherwise></c:choose></h1>
        <p class="muted">Расскажи другим, что интересного ты нашёл в городе.</p></div>
    <c:if test="${not empty error}">
        <div class="notice notice-error"><c:out value="${error}"/></div>
    </c:if>
    <c:set var="safeTitle"><c:out value="${place.title}"/></c:set>
    <c:set var="safeAddress"><c:out value="${place.streetAddress}"/></c:set>
    <form class="panel form-stack" method="post" enctype="multipart/form-data"
          action="${pageContext.request.contextPath}/places/${not empty place ? 'update' : 'create'}">
        <input type="hidden" name="_csrf" value="${requestScope.csrfToken}"><c:if test="${not empty place}"><input
            type="hidden" name="id" value="${place.id}"></c:if>
        <label>Название места<input name="title" required maxlength="150" value="${safeTitle}"
                                    placeholder="Например, граффити на старой фабрике"></label>
        <label>Описание<textarea name="description" rows="5" placeholder="Чем интересно это место?"><c:out
                value="${place.description}"/></textarea></label>
        <label>Улица и адрес<input name="streetAddress" maxlength="255" value="${safeAddress}"
                                   placeholder="Улица, номер дома"></label>
        <div class="form-row"><label>Город<select name="cityId" required>
            <option value="">Выбери город места</option>
            <c:forEach var="city" items="${cities}">
                <option value="${city.id}"
                        <c:if test="${(not empty place and place.cityId == city.id) or (empty place and sessionScope.user.cityId == city.id)}">selected</c:if>>
                    <c:out value="${city.name}"/>, <c:out value="${city.country}"/></option>
            </c:forEach></select></label>
            <label>Категория<select name="categoryId" required><c:forEach var="category" items="${categories}">
                <option value="${category.id}"
                        <c:if test="${not empty place and place.categoryId == category.id}">selected</c:if>><c:out
                        value="${category.nameCategory}"/></option>
            </c:forEach></select></label></div>
        <c:if test="${empty place}"><label>Первая фотография <span
                class="muted">(обязательно, JPG/PNG/GIF/WEBP до 5 МБ)</span><input type="file" name="photo"
                                                                                   accept="image/jpeg,image/png,image/gif,image/webp"
                                                                                   required></label></c:if>
        <div class="form-actions"><a class="button button-ghost" href="${pageContext.request.contextPath}/">Отмена</a>
            <button class="button button-primary" type="submit"><c:choose><c:when
                    test="${not empty place}">Сохранить изменения</c:when><c:otherwise>Опубликовать место</c:otherwise></c:choose></button>
        </div>
        <c:if test="${empty place}"><p class="small muted">После сохранения место сразу появится в городской
            ленте.</p></c:if>
    </form>
</main>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
<script>window.SCOUTX = {context: '${pageContext.request.contextPath}', csrf: '${requestScope.csrfToken}'};</script>
<script src="${pageContext.request.contextPath}/assets/js/app.js" defer></script>
</body></html>
