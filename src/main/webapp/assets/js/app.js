(() => {
    const config = window.SCOUTX || { context: '', csrf: '' };

    // Избранное меняется через AJAX: страница не перезагружается.
    document.addEventListener('click', async (event) => {
        const button = event.target.closest('.favorite-button');
        if (!button) return;
        button.disabled = true;
        try {
            const body = new URLSearchParams();
            body.set('placeId', button.dataset.placeId);
            const response = await fetch(`${config.context}/favorites`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
                    'X-CSRF-Token': config.csrf,
                    'X-Requested-With': 'XMLHttpRequest',
                    'Accept': 'application/json'
                }, body: new FormData(form)
            });
            const data = await response.json();
            if (!response.ok || !data.ok) throw new Error(data.error || 'Не удалось изменить избранное.');
            const saved = data.data.saved;
            button.dataset.saved = String(saved);
            button.classList.toggle('is-saved', saved);
            button.textContent = saved ? '♥' : '♡';
        } catch (error) {
            button.title = error.message;
        } finally {
            button.disabled = false;
        }
    });

    // Комментарий с необязательной фотографией отправляется через fetch + FormData.
    // Новый комментарий добавляется в список без перезагрузки страницы.
    document.addEventListener('submit', async (event) => {
        const form = event.target.closest('.comment-form');
        if (!form) return;
        event.preventDefault();
        const message = form.querySelector('.inline-message');
        const submit = form.querySelector('button[type="submit"]');
        message.textContent = '';
        submit.disabled = true;
        try {
            const response = await fetch(`${config.context}/comments`, {
                method: 'POST',
                headers: { 'X-CSRF-Token': config.csrf,
                    'X-Requested-With': 'XMLHttpRequest',
                    'Accept': 'application/json'
                },
                body: new FormData(form)
            });
            const data = await response.json();
            if (!response.ok || !data.ok) throw new Error(data.error || 'Не удалось отправить комментарий.');

            const commentData = data.data;
            const item = document.createElement('article');
            item.className = 'comment-item';
            const head = document.createElement('div');
            head.className = 'comment-head';
            const author = document.createElement('strong');
            author.textContent = commentData.username;
            const date = document.createElement('span');
            date.className = 'muted small';
            date.textContent = 'только что';
            head.append(author, date);
            const text = document.createElement('p');
            text.textContent = commentData.content;
            item.append(head, text);
            if (commentData.attachmentUrl) {
                const link = document.createElement('a');
                link.href = commentData.attachmentUrl;
                link.target = '_blank';
                const image = document.createElement('img');
                image.className = 'comment-attachment';
                image.src = commentData.attachmentUrl;
                image.alt = 'Фото из комментария';
                link.append(image);
                item.append(link);
            }
            const list = document.getElementById(`comments-${form.dataset.photoId}`);
            list.append(item);
            form.reset();
        } catch (error) {
            message.textContent = error.message;
        } finally {
            submit.disabled = false;
        }
    });
})();
