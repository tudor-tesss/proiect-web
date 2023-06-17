import { User } from "../models/index.js";
import { Routes } from "../routes/index.js";

export class UsersService {
    static async getUserById(userId) {
        const endpoint = Routes.users.getUserById.replace("{id}", userId);

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
        const endpoint = Routes.users.createUser;

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

    static async getAllUserNames() {
        const endpoint = Routes.users.getAllUserNames;

        return await fetch(endpoint, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        })
        .then(async response => {
            if(!response.ok) {
                const error = await response.json();
                throw new Error(error);
            }

            const res = await response.json();
            const map = new Map(Object.entries(res)); 
            
            return map;
        });
    }
}