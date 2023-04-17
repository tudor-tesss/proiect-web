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

    if (!sessionId || !userId || !userIp) {
        return;
    }

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
    })
    .catch(error => {
    });
}

async function getUserIP() {
    const cookies = document.cookie.split('; ').reduce((acc, cookie) => {
        const [name, value] = cookie.split('=');
        acc[name] = value;
        return acc;
    }, {});

    var userIp = cookies['userIpAddress'];

    if (userIp) {
        return userIp;
    }

    try {
      const response = await fetch('https://ipapi.co/json/');
      const data = await response.json();

        setCookie('userIpAddress', data.ip, 1);

      return data.ip;
        // return "test-ip";
    } catch (error) {
      console.error('Error fetching IP address:', error);
    }
} 

function setCookie(name, value, days) {
    let expires = '';
    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = '; expires=' + date.toUTCString();
    }
    document.cookie = name + '=' + (value || '') + expires + '; path=/';
}

async function deleteSessionTokenCookie() {
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

    await fetch(endpoint, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(deleteUserSessionCommand)
    })
    .then(async response => {
        if (!response.ok) {
            const error = response.json();
            throw new Error(error);
        }

        return await response.json();
    })
    .then(d => {
    })
    .catch(error => {
        console.error('Error:', error);
    });


    document.cookie = "sessionId=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    document.cookie = "userId=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

    localStorage.removeItem('userUuid');

    window.location.href = '/start/start.html';
}