<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="ru"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Админ-панель — ScoutX</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"></head><body>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>
<main class="page-wrap">
    <div class="section-heading"><div><div class="eyebrow">CONTROL ROOM</div><h1>Панель администратора</h1><p class="muted">Просмотр пользователей, управление публикациями и категориями.</p></div></div>
    <c:if test="${not empty error}"><div class="notice notice-error"><c:out value="${error}"/></div></c:if>

    <section class="admin-section">
        <h2>Публикации</h2>
        <div class="table-wrap"><table><thead><tr><th>Место</th><th>Автор</th><th>Город</th><th>Категория</th><th>Действия</th></tr></thead><tbody>
        <c:forEach var="p" items="${places}"><tr>
            <td><a href="${pageContext.request.contextPath}/places/view?id=${p.id}"><c:out value="${p.title}"/></a></td>
            <td><c:out value="${p.authorName}"/></td>
            <td><c:out value="${p.cityName}"/></td>
            <td><c:out value="${p.categoryName}"/></td>
            <td><form method="post" action="${pageContext.request.contextPath}/admin" onsubmit="return confirm('Удалить публикацию?')">
                <input type="hidden" name="_csrf" value="${requestScope.csrfToken}">
                <input type="hidden" name="id" value="${p.id}">
                <input type="hidden" name="action" value="deletePlace">
                <button class="button button-danger button-small" type="submit">Удалить</button>
            </form></td>
        </tr></c:forEach>
        </tbody></table></div>
    </section>

    <section class="admin-section">
        <h2>Пользователи</h2>
        <div class="table-wrap"><table><thead><tr><th>Имя</th><th>Email</th><th>Город</th><th>Роль</th></tr></thead><tbody>
        <c:forEach var="u" items="${users}"><tr>
            <td><c:out value="${u.username}"/></td>
            <td><c:out value="${u.email}"/></td>
            <td><c:out value="${u.cityName}"/></td>
            <td><c:out value="${u.role}"/></td>
        </tr></c:forEach>
        </tbody></table></div>
    </section>

    <section class="admin-section">
        <h2>Категории</h2>
        <div class="category-list"><c:forEach var="cat" items="${categories}"><span class="category-pill static"><c:out value="${cat.name}"/></span></c:forEach></div>
        <form method="post" action="${pageContext.request.contextPath}/admin" class="inline-form">
            <input type="hidden" name="_csrf" value="${requestScope.csrfToken}">
            <input type="hidden" name="action" value="addCategory">
            <input name="name" maxlength="100" placeholder="Название новой категории" required>
            <button class="button button-primary button-small" type="submit">Добавить</button>
        </form>
    </section>
</main>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
<script>window.SCOUTX={context:'${pageContext.request.contextPath}',csrf:'${requestScope.csrfToken}'};</script><script src="${pageContext.request.contextPath}/assets/js/app.js" defer></script></body></html>
