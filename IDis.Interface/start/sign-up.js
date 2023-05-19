async function createUser() {
    const endpoint = 'http://localhost:7101/users';

    const name = document.getElementById('name').value;
    const firstName = document.getElementById('first-name').value;
    const email = document.getElementById('email').value;

    const createUserCommand = {
        name: name,
        firstName: firstName,
        emailAddress: email
    };

    await fetch(endpoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(createUserCommand)
    })
    .then(async response => {
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error);
        }

        return response.json();
    })
    .then(() => {
        displayLoginForm();
    })
    .catch(error => {
        console.error('Error:', error);

        var errorMessage = error.message;

        if (errorMessages[errorMessage] == undefined) {
            errorMessage = "An error occurred while creating your account.";
        }

        displayError(errorMessages[errorMessage]);
    });
}

function displayLoginForm() {
    const formWrapper = document.querySelector('.form-wrapper');
    formWrapper.innerHTML = `
        <div class="login-box">
            <h2>Log In</h2>
            <form>
                <div class="user-box">
                    <input type="text" name="email" id="email" required>
                    <label>Email</label>
                </div>
                <div id="error-container" class="error-container"></div>
                <div class="submit-wrapper">
                    <button type="button" class="submit-b" onClick="createUserGate()">Log In</button>
                </div>
            </form>
        </div>
    `;
}

async function createUserGate() {
    const endpoint = 'http://localhost:7101/users/gates';

    const emailAddress = document.getElementById('email').value;

    const createUserGateCommand = {
        emailAddress: emailAddress
    };

    await fetch(endpoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(createUserGateCommand)
    })
    .then(async response => {
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error);
        }

        return response.json();
    })
    .then(data => {
        localStorage.setItem('userUuid', data);
        displayUserGateForm();
    })
    .catch(error => {
        console.error('Error:', error);

        var errorMessage = "";
        if (error.message == "UserGate.Create.UserDoesNotExist") {
            errorMessage = errorMessages["UserGate.Create.UserDoesNotExist"];
        } 
        else {
            errorMessage = "An error occurred while creating the user gate.";
        }

        displayError(errorMessage);
    });
}

function displayUserGateForm() {
    const formWrapper = document.querySelector('.form-wrapper');
    formWrapper.innerHTML = `
        <div class="login-box">
            <h2>Enter Code</h2>
            <form>
                <div class="user-box">
                    <input type="text" name="code" id="code" required>
                    <label>Code</label>
                </div>
                <div id="error-container" class="error-container"></div>
                <div class="submit-wrapper">
                    <button type="button" class="submit-b" onClick="passUserGate()">Submit</button>
                </div>
            </form>
        </div>
    `;
}

async function passUserGate() {
    const userUuid = localStorage.getItem('userUuid');

    const endpoint = 'http://localhost:7101/users/' + userUuid + '/gates';

    const passUserGateCommand = {
        code: document.getElementById('code').value
    };

    await fetch(endpoint, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(passUserGateCommand)
    })
    .then(async response => {
        if (!response.ok) {
            const error = await response.json();

            throw new Error(error);
        }

        return response.json();
    })
    .then(data => {
        createSession(data);
    })
    .catch(error => {
        console.error('Error:', error.message);

        var errorMessage = error.message;

        if (errorMessages[errorMessage] == undefined) {
            errorMessage = "An error occurred while passing the user gate.";
        }

        displayError(errorMessages[errorMessage]);
    });
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
        window.location.href = '../main/main.html';
    })
    .catch(error => {
        deleteSessionTokenCookie();
        console.error('Error:', error);
        displayError("An error occurred while creating the session.");
        window.location.href = '/start/start.html';
    })
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
    } catch (error) {
      console.error('Error fetching IP address:', error);
    }
} 

function displayError(errorMessage) {
    const errorContainer = document.getElementById('error-container');
    errorContainer.textContent = errorMessage;
    errorContainer.style.display = 'block';

    setTimeout(() => {
        errorContainer.style.display = 'none';
    }, 5000);
}

errorMessages = {
    "User.AlreadyExists": "Email address is already in use.",
    "User.Name.NullOrEmpty": "Name is required.",
    "User.FirstName.NullOrEmpty": "First name is required.",
    "User.EmailAddress.NullOrEmpty": "Email address is required.",
    "UserGate.Create.UserDoesNotExist": "No user found matching the given email address.",
    "UserGate.Pass.UserGateDoesNotExist": "No user gate found matching the given code.",
    "UserGate.Pass.UserGateExpired": "The code has expired.",
    "UserGate.Pass.InvalidCode": "The code is invalid.",
    "UserGate.Pass.UserGateAlreadyPassed": "The code has already been used."
}

function displaySignUpForm() {
    const formWrapper = document.querySelector('.form-wrapper');
    formWrapper.innerHTML = `
        <div class="login-box">
            <script src="sign-up.js"></script>
            <h2>Sign Up</h2>
            <form>
                <div class="user-box">
                    <input type="text" name="first-name" id="first-name" required>
                    <label>First Name</label>
                </div>
                <div class="user-box">
                    <input type="text" name="name" id="name" required>
                    <label>Name</label>
                </div>
                <div class="user-box">
                    <input type="text" name="email" id="email" required>
                    <label>Email</label>
                </div>
                <div id="error-container" class="error-container"></div>
                <div class="submit-wrapper">
                    <button type="button" class="submit-b" onClick="createUser()">Submit</button>
                </div>
            </form>
        </div>
    `;
}