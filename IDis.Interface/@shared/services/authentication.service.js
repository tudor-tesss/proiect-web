import { Routes } from "../routes/index.js";

export class AuthenticationService {
    static getCookie(name) {
        const cookies = document.cookie.split('; ').reduce((acc, cookie) => {
            const [name, value] = cookie.split('=');
            acc[name] = value;
            return acc;
        }, {});

        return cookies[name];
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

    static async checkSession() {
        const userIp = await this.getUserIP();
        const userId = localStorage.getItem('userUuid');
        const sessionId = this.getCookie('sessionId');
    
        if (!sessionId) {
            window.location.href = '/start';
        }
    
        if (!userId) {
            window.location.href = '/start';
        }
    
        const endpoint = Routes.authentication.checkSession.replace('{id}', userId);
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
        .catch(error => {
            console.error('Error:', error);
            this.deleteSessionTokenCookie();
            window.location.href = '/start';
        })
        .then(d => {
            AuthenticationService.setCookie('sessionId', sessionId, 1);
            AuthenticationService.setCookie('userId', userId, 1);
        });
    }
    
    static async resumeSession() {
        const userIp = await this.getUserIP();
        const userId = localStorage.getItem('userUuid');
        const sessionId = this.getCookie('sessionId');
    
        if (!sessionId || !userId || !userIp) {
            return;
        }

        const endpoint = Routes.authentication.checkSession.replace('{id}', userId);
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
            window.location.href = '/main';
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
    
    static async deleteSessionTokenCookie() {
        const endpoint = Routes.authentication.deleteSessionTokenCookie.replace('{id}', localStorage.getItem('userUuid'));
    
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
    
        window.location.href = '/start';
    }

    static async createUserGate(emailAddress) {
        const endpoint = Routes.authentication.createUserGate;

        const createUserGateCommand = {
            emailAddress: emailAddress
        };

        return await fetch(endpoint, {
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
        });
    }

    static async passUserGate(userUuid, code) {
        const endpoint = Routes.authentication.passUserGate.replace('{id}', userUuid);

        const passUserGateCommand = {
            code: code
        };

        return await fetch(endpoint, {
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
        .then(async data => {
            return await this.createSession(data.userId);
        });
    }

    static async createSession(userId) {
        const endpoint = Routes.authentication.createSession.replace('{id}', userId);

        const createSessionCommand = {
            userId: userId,
            userIpAddress: await this.getUserIP()
        };

        return await fetch(endpoint, {
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
        });
    }
}