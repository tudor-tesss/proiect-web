export class PostsService {
    static async getAllPostsInCategory(categoryId) {
        const endpoint = `http://localhost:7101/categories/${categoryId}/posts`;

        return await fetch(endpoint, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            }
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