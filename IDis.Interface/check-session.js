async function checkSession() {
    const cookies = document.cookie.split('; ').reduce((acc, cookie) => {
        const [name, value] = cookie.split('=');
        acc[name] = value;
        return acc;
    }, {});

    var userIp = await getUserIP();
    var userId = localStorage.getItem('userUuid');
    const sessionId = cookies['sessionId'];

    // If the session ID is not set, redirect the user to the sign-in page.
    if (!sessionId) {
        window.location.href = '/start/start.html';
    }

    if (!userId) {
        window.location.href = '/start/start.html';
    }

    var endpoint = 'http://localhost:7101/users/' + userId + '/sessions';

    var checkUserSessionCommand = {
        userId: userId,
        sessionId: sessionId,
        ipAddress: userIp
    }

    await fetch(endpoint, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(checkUserSessionCommand)
    })
    .then(async response => {
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error);
        }

        return response.json();
    })
    .then(d => {
        console.log(d);
    })
    .catch(error => {
        console.error('Error:', error);
        deleteSessionTokenCookie();
        window.location.href = '/start/start.html';
    });
}

async function resumeSession() {
    const cookies = document.cookie.split('; ').reduce((acc, cookie) => {
        const [name, value] = cookie.split('=');
        acc[name] = value;
        return acc;
    }, {});

    var userIp = await getUserIP();
    var userId = localStorage.getItem('userUuid');
    const sessionId = cookies['sessionId'];

    var endpoint = 'http://localhost:7101/users/' + userId + '/sessions';

    var checkUserSessionCommand = {
        userId: userId,
        sessionId: sessionId,
        ipAddress: userIp
    };

    await fetch(endpoint, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(checkUserSessionCommand)
    })
    .then(async response => {
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error);
        }

        return response.json();
    })
    .then(d => {
        window.location.href = '/main/main.html';
        console.log(d);
    })
    .catch(error => {
        console.error('Error:', error);
        deleteSessionTokenCookie();
    });
}

async function getUserIP() {
    try {
      const response = await fetch('https://ipapi.co/json/');
      const data = await response.json();
      return data.ip;
    } catch (error) {
      console.error('Error fetching IP address:', error);
    }
} 
