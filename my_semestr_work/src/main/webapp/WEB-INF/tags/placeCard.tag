<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ attribute name="place" required="true" type="com.example.models.Places" %>
<%@ attribute name="context" required="true" type="java.lang.String" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<article class="place-card">
    <a class="place-image" href="${context}/places/view?id=${place.id}">
        <c:choose>
            <c:when test="${not empty place.previewPhotoPath}"><img src="${context}/media/${place.previewPhotoPath}" alt="Фотография места" loading="lazy"></c:when>
            <c:otherwise><div class="image-placeholder"><span>SCOUTX</span><b>⌖</b></div></c:otherwise>
        </c:choose>
        <span class="category-pill"><c:out value="${place.categoryName}"/></span>
    </a>
    <div class="place-card-body">
        <div class="place-meta"><span>⌖ <c:out value="${place.cityName}"/></span><span>Автор: <c:out value="${place.authorName}"/></span></div>
        <h3><a href="${context}/places/view?id=${place.id}"><c:out value="${place.title}"/></a></h3>
        <p><c:out value="${place.description}"/></p>
        <div class="card-bottom">
            <a class="text-link" href="${context}/places/view?id=${place.id}">Открыть место →</a>
            <c:if test="${not empty sessionScope.user}"><button class="favorite-button ${place.favorite ? 'is-saved' : ''}" data-place-id="${place.id}" data-saved="${place.favorite}" title="В избранное">${place.favorite ? '♥' : '♡'}</button></c:if>
        </div>
    </div>
</article>
