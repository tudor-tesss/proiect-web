export class CategoriesService {
    static async createCategory(name, ratingFields, creatorId) {
        const endpoint = "http://localhost:7101/categories";

        await fetch(endpoint, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                name: name,
                ratingFields: ratingFields,
                creatorId: creatorId
            })
        })
        .then(async (response) => {
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error);
            }

            return response.json();
        });
    }
}