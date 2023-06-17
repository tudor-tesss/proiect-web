import { Routes } from "../routes/index.js";

export class CategoriesService {
    static async createCategory(name, ratingFields, creatorId) {
        const endpoint = Routes.categories.createCategory;
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

    static async getAll() {
        const endpoint = Routes.categories.getAll;

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

    static async getCategoriesByCreatorId(creatorId) {
        const endpoint = Routes.categories.getCategoriesByCreatorId.replace("{id}", creatorId);

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

    static async getCategory(categoryId) {
        const url = Routes.categories.getAll;

        const categories = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        })
            .then(async response => {
                return await response.json();
            });

        return categories.find(c => c.id === categoryId);
    }
}