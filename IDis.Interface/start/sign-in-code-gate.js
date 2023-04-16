function passUserGate() {
    const userUuid = localStorage.getItem('userUuid');

    const endpoint = 'http://localhost:7101/users/' + userUuid + '/gates';

    const code = document.getElementById('code').value;

    const passUserGateCommand = {
        code: code
    };

    console.log('before fetch');
    fetch(endpoint, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(passUserGateCommand)
    })
    .then(response => {
        if (!response.ok) {
            const error = response.json();

            console.log(error);

            throw new Error(error);
        }

        return response.json();
    })
    .then(data => {
        console.log('Success:', data);
        createSession(data);
    })
    .catch(error => {
        console.error('Error:', error);

        var errorMessage = "";
        if (error.message == "UserGate.Pass.UserGateDoesNotExist") {
            errorMessage = errorMessages["UserGate.Pass.UserGateDoesNotExist"];
        } 
        else if (error.message == "UserGate.Pass.UserGateExpired") {
            errorMessage = errorMessages["UserGate.Pass.UserGateExpired"];
        } 
        else if (error.message == "UserGate.Pass.InvalidCode") {
            errorMessage = errorMessages["UserGate.Pass.InvalidCode"];
        } 
        else if (error.message == "UserGate.Pass.UserGateAlreadyPassed") {
            errorMessage = errorMessages["UserGate.Pass.UserGateAlreadyPassed"];
        } 
        else {
            errorMessage = "An error occurred while passing the user gate.";
        }

        displayError(errorMessage);
    });
}

function displayError(errorMessage) {
    const errorContainer = document.getElementById('error-container');
    errorContainer.textContent = errorMessage;
    errorContainer.style.display = 'block';

    setTimeout(() => {
        errorContainer.style.display = 'none';
    }, 5000);
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

errorMessages = {
    "UserGate.Pass.UserGateDoesNotExist": "No user gate found matching the given code.",
    "UserGate.Pass.UserGateExpired": "The code has expired.",
    "UserGate.Pass.InvalidCode": "The code is invalid.",
    "UserGate.Pass.UserGateAlreadyPassed": "The code has already been used.",
}

async function createSession(data) {
    const endpoint = 'http://localhost:7101/users/' + data.userId + '/sessions';

    const createSessionCommand = {
        userId: data.userId,
        userIpAddress: await getUserIP()
    };

    await fetch(endpoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(createSessionCommand)
    })
    .then(async response => {
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error);
        }

        return response.json();
    })
    .then(d => {
        setCookie('sessionId', d.sessionId, 1);
        setCookie('userId', data.userId, 1);
        window.location.href = '/main/main.html';
    })
    .catch(error => {
        deleteSessionTokenCookie();
        console.error('Error:', error);
        displayError("An error occurred while creating the session.");
        window.location.href = '/start/start.html';
    })
}

async function getUserIP() {
    try {
        const response = await fetch('https://ipapi.co/json/');
        const data = await response.json();
        // return data.ip;
        return "test-ip";
    } 
    catch (error) {
        console.error('Error fetching IP address:', error);
    }
  } 
  