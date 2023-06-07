import { Post, PostReply } from "../models/index.js";

export class PostsService {
    static async createPost(categoryId, title, body, ratings) {
        const endpoint = "http://localhost:7101/posts";
        const authorId = localStorage.getItem("userUuid");

        const post = {
            authorId: authorId,
            categoryId,
            title,
            body,
            ratings: Object.fromEntries(ratings)
        };

        console.log(ratings);
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

            const postsJson = await response.json();
            return postsJson.map(postJson => Post.fromJson(postJson));
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

            const postsJson = await response.json();
            return postsJson.map(postJson => Post.fromJson(postJson));
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

            const postsJson = await response.json();
            return postsJson.map(postJson => Post.fromJson(postJson));
        });
    }

    static async getPost(postId) {
        const endpoint = `http://localhost:7101/posts/${postId}`;

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

            const json = await response.json();
            return Post.fromJson(json);
        });
    }

    static async getPostReplies(postId) {
        const endpoint = `http://localhost:7101/posts/${postId}/replies`;

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

            const postsJson = await response.json();
            return postsJson.map(postJson => PostReply.fromJson(postJson));
        });
    }
}