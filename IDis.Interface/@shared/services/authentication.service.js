export class AuthenticationService {
    static getCookie(name) {
        const cookies = document.cookie.split('; ').reduce((acc, cookie) => {
            const [name, value] = cookie.split('=');
            acc[name] = value;
            return acc;
        }, {});

        return cookies[name];
    }

    static async checkSession() {
        const userIp = await this.getUserIP();
        const userId = localStorage.getItem('userUuid');
        const sessionId = this.getCookie('sessionId');
    
        if (!sessionId) {
            window.location.href = '/start/start.html';
        }
    
        if (!userId) {
            window.location.href = '/start/start.html';
        }
    
        const endpoint = 'http://localhost:7101/users/' + userId + '/sessions';
        const checkUserSessionCommand = {
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
            this.deleteSessionTokenCookie();
            window.location.href = '/start/start.html';
        });
    }
    
    static async resumeSession() {
        const userIp = await this.getUserIP();
        const userId = localStorage.getItem('userUuid');
        const sessionId = this.getCookie('sessionId');
    
        if (!sessionId || !userId || !userIp) {
            return;
        }

        const endpoint = 'http://localhost:7101/users/' + userId + '/sessions';
        const checkUserSessionCommand = {
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
    
    static async getUserIP() {
        const userIp = this.getCookie('userIpAddress');
    
        if (userIp) {
            return userIp;
        }
    
        try {
            const response = await fetch('https://ipapi.co/json/');
            const data = await response.json();

            this.setCookie('userIpAddress', data.ip, 1);

            return data.ip;
        } catch (error) {
          console.error('Error fetching IP address:', error);
        }
    } 
    
    static setCookie(name, value, days) {
        let expires = '';
        if (days) {
            const date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            expires = '; expires=' + date.toUTCString();
        }
        document.cookie = name + '=' + (value || '') + expires + '; path=/';
    }
    
    static async deleteSessionTokenCookie() {
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

    static async createUser() {
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

                if (errorMessages[errorMessage] === undefined) {
                    errorMessage = "An error occurred while creating your account.";
                }

                displayError(errorMessages[errorMessage]);
            });
    }
}