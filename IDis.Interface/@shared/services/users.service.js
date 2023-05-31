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

            return await response.json();
        });
    }
}