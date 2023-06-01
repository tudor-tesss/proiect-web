import { User } from "../models/index.js";

export class UsersService {
    static async getUserById(userId) {
        const endpoint = `http://localhost:7101/users/${userId}`;

        return await fetch(endpoint, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        })
        .then(async response => {
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error);
            }

            const result = await response.json();
            return new User(result.id, result.name, result.firstName, result.emailAddress);
        });
    }

    static async createUser(name, firstName, email) {
        const endpoint = 'http://localhost:7101/users';

        return await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(new User(null, name, firstName, email))
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