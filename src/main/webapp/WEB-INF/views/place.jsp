<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="ru"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title><c:out
        value="${place.title}"/> — ScoutX</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head><body>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>
<main class="page-wrap detail-wrap">
    <c:if test="${param.created == '1'}">
        <div class="notice notice-success">Публикация успешно создана и появилась в городской ленте.</div>
    </c:if>
    <div class="detail-top"><a class="back-link" href="${pageContext.request.contextPath}/">← Вернуться в ленту</a>
    </div>
    <div class="detail-heading">
        <div>
            <div class="eyebrow"><c:out value="${place.categoryName}"/> · <c:out value="${place.cityName}"/></div>
            <h1><c:out value="${place.title}"/></h1>
            <p class="detail-meta">Автор: <strong><c:out value="${place.authorName}"/></strong><c:if
                    test="${not empty place.streetAddress}"> · <c:out value="${place.streetAddress}"/></c:if></p></div>
        <c:if test="${not empty sessionScope.user and (sessionScope.user.id == place.authorId or sessionScope.user.admin)}">
            <div class="inline-actions"><a class="button button-ghost button-small"
                                           href="${pageContext.request.contextPath}/places/edit?id=${place.id}">Редактировать</a>
                <form method="post" action="${pageContext.request.contextPath}/places/delete"
                      onsubmit="return confirm('Удалить эту публикацию?')"><input type="hidden" name="_csrf"
                                                                                  value="${requestScope.csrfToken}"><input
                        type="hidden" name="id" value="${place.id}">
                    <button class="button button-danger button-small" type="submit">Удалить</button>
                </form>
            </div>
        </c:if></div>
    <p class="detail-description"><c:out value="${place.description}"/></p>
    <section class="section-heading">
        <div>
            <div class="eyebrow">GALLERY</div>
            <h2>Фотографии места</h2></div>
    </section>
    <c:if test="${empty place.photos}">
        <div class="empty-state compact"><p>Пока нет фотографий. Добавь первую!</p></div>
    </c:if>
    <div class="photo-list">
        <c:forEach var="photo" items="${place.photos}">
            <article class="photo-panel">
                <img class="detail-photo" src="${pageContext.request.contextPath}/media/${photo.storage_file_path}"
                     alt="Фотография места" loading="lazy">
                <div class="photo-info"><span>Добавил: <c:out
                        value="${photo.uploaderName}"/></span><span>Комментарии: ${photo.comments.size()}</span></div>
                <div class="comments-area"><h3>Обсуждение</h3>
                    <div class="comments-list" id="comments-${photo.id}">
                        <c:forEach var="comment" items="${photo.comments}">
                            <article class="comment-item">
                                <div class="comment-head"><strong><c:out value="${comment.username}"/></strong><span
                                        class="muted small"><c:out value="${comment.createdAt}"/></span></div>
                                <p><c:out value="${comment.content}"/></p><c:if
                                    test="${not empty comment.attachment_path}"><a
                                    href="${pageContext.request.contextPath}/media/${comment.attachmentPath}"
                                    target="_blank"><img class="comment-attachment"
                                                         src="${pageContext.request.contextPath}/media/${comment.attachmentPath}"
                                                         alt="Фото из комментария"></a></c:if></article>
                        </c:forEach>
                    </div>
                    <c:choose><c:when test="${not empty sessionScope.user}">
                        <form class="comment-form" action="${pageContext.request.contextPath}/comments" method="post"
                              enctype="multipart/form-data" data-photo-id="${photo.id}"><input type="hidden"
                                                                                               name="_csrf"
                                                                                               value="${requestScope.csrfToken}"><input
                                type="hidden" name="photoId" value="${photo.id}"><label class="sr-only"
                                                                                        for="comment-${photo.id}">Комментарий</label><textarea
                                id="comment-${photo.id}" name="content" rows="2" maxlength="2000"
                                placeholder="Напиши комментарий..." required></textarea>
                            <div class="comment-form-bottom"><label class="file-label">＋ Фото <input type="file"
                                                                                                     name="attachment"
                                                                                                     accept="image/jpeg,image/png,image/gif,image/webp"></label>
                                <button class="button button-primary button-small" type="submit">Отправить</button>
                            </div>
                            <div class="inline-message" aria-live="polite"></div>
                        </form>
                    </c:when><c:otherwise><p class="muted small">Войди, чтобы оставлять комментарии и
                        фотографии.</p></c:otherwise></c:choose>
                </div>
            </article>
        </c:forEach>
    </div>
    <c:if test="${not empty sessionScope.user}">
        <section class="panel upload-panel"><h3>Добавить фотографию к месту</h3>
            <form method="post" action="${pageContext.request.contextPath}/photos" enctype="multipart/form-data"><input
                    type="hidden" name="_csrf" value="${requestScope.csrfToken}"><input type="hidden" name="placeId"
                                                                                        value="${place.id}">
                <div class="form-row"><input type="file" name="photo" accept="image/jpeg,image/png,image/gif,image/webp"
                                             required>
                    <button class="button button-primary button-small" type="submit">Загрузить</button>
                </div>
                <p class="small muted">JPG, PNG, GIF или WEBP, максимум 5 МБ.</p></form>
        </section>
    </c:if>
</main>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
<script>window.SCOUTX = {context: '${pageContext.request.contextPath}', csrf: '${requestScope.csrfToken}'};</script>
<script src="${pageContext.request.contextPath}/assets/js/app.js" defer></script>
</body></html>
