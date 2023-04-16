function deleteSessionTokenCookie() {
    const endpoint = 'http://localhost:7101/users/' + localStorage.getItem('userUuid') + '/sessions';

    const cookies = document.cookie.split(';').reduce((acc, c) => {
        const [key, v] = c.trim().split('=').map(decodeURIComponent);
        try {
            return Object.assign(acc, { [key]: JSON.parse(v) });
        } catch (e) {
            return Object.assign(acc, { [key]: v });
        }
    }, {});

    const deleteUserSessionCommand = {
        sessionId: cookies['sessionId']
    };

    fetch(endpoint, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(deleteUserSessionCommand)
    })
    .then(response => {
        if (!response.ok) {
            const error = response.json();
            throw new Error(error);
        }

        return response.json();
    })
    .then(d => {
        console.log(d);
    })
    .catch(error => {
        console.error('Error:', error);
    });


    document.cookie = "sessionId=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    document.cookie = "userId=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

    localStorage.removeItem('userUuid');

    window.location.href = '/start/start.html';
}