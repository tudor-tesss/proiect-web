export class PostsService {
    static async createPost(authorId, categoryId, title, body, ratings) {
        const endpoint = "http://localhost:7101/posts";

        const post = {
            authorId,
            categoryId,
            title,
            body,
            ratings
        };

        return await fetch(endpoint, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(post)
        })
        .then(async (response) => {
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error);
            }

            return response.json();
        });
    }

    static async createPostReply(authorId, parentPostId, title, body, ratings) {
        const endpoint = `http://localhost:7101/posts/${parentPostId}/replies`;

        const post = {
            authorId,
            parentPostId,
            title,
            body,
            ratings
        };

        return await fetch(endpoint, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(post)
        })
        .then(async (response) => {
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error);
            }

            return response.json();
        });
    }

    static async getAllPosts() {
        const endpoint = "http://localhost:7101/posts";

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

    static async getPostsByCreatorId(creatorId) {
        const endpoint = `http://localhost:7101/users/${creatorId}/posts`;

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