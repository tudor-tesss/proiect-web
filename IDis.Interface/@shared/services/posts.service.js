import { Post, PostReply } from "../models/index.js";
import { Routes } from "../routes/index.js";

export class PostsService {
    static async createPost(categoryId, title, body, ratings) {
        const endpoint = Routes.posts.createPost;
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
        const endpoint = Routes.posts.createPostReply.replace("{id}", parentPostId);

        const post = {
            authorId,
            parentPostId,
            title,
            body,
            ratings: Object.fromEntries(ratings)
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
        const endpoint = Routes.posts.getAll;

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
        const endpoint = Routes.posts.getAllPostsInCategory.replace("{id}", categoryId);

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
        const endpoint = Routes.posts.getAllPostsByCreatorId.replace("{id}", creatorId);

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
        const endpoint = Routes.posts.getPostById.replace("{id}", postId);

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
        const endpoint = Routes.posts.getPostReplies.replace("{id}", postId);

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