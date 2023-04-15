function checkSession() {
    const cookies = document.cookie.split('; ').reduce((acc, cookie) => {
        const [name, value] = cookie.split('=');
        acc[name] = value;
        return acc;
    }, {});

    const sessionId = cookies['sessionToken'];

    // If the session ID is not set, redirect the user to the sign-in page.
    if (!sessionId) {
        window.location.href = '/start/start.html';
    }
}

function resumeSession() {
    const cookies = document.cookie.split('; ').reduce((acc, cookie) => {
        const [name, value] = cookie.split('=');
        acc[name] = value;
        return acc;
    }, {});

    const sessionId = cookies['sessionToken'];
    if (sessionId) {
        window.location.href = '/main/main.html';
    }
}