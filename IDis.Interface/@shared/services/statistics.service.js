import { Routes } from "../routes/routes.js";

export class StatisticsService {
    static async getCategoryStatistics(categoryId) {
        const endpoint = Routes.categories.getCategoryStatistics.replace("{id}", categoryId);

        return await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
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

    static async getCategoriesStatistics() {
        const endpoint = Routes.categories.getCategoriesStatistics;

        return await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
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

    static async getPostStatistics(postId) {
        const endpoint = Routes.posts.getPostStatistics.replace("{id}", postId);

        return await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
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

    static async getPostsStatistics() {
        const endpoint = Routes.posts.getPostsStatistics;

        return await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
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

    static async getPdfForPostStats(postId) {
        const endpoint = Routes.getPdfForPostStatistics.replace("{id}", postId);

        return await fetch(endpoint, {
            method: "POST",
            headers: {
                "Content-Type": "application/pdf",
            }
        })
            .then(async (response) => {
                if (!response.ok) {
                    const error = await response.json();
                    throw new Error(error);
                }

                return response.text()
            });
    }

    static async getPdfForCategoryStats(categoryId) {
        const endpoint = Routes.getPdfForCategoryStatistics.replace("{id}", categoryId);

        return await fetch(endpoint, {
            method: "POST",
            headers: {
                "Content-Type": "application/pdf",
            }
        })
            .then(async (response) => {
                if (!response.ok) {
                    const error = await response.json();
                    throw new Error(error);
                }

                return response.text()
            });
    }

    static async getDocbookForPostStats(postId) {
        const endpoint = Routes.getDocbookForPostStatistics.replace("{id}", postId);

        return await fetch(endpoint, {
            method: "POST",
            headers: {
                "Content-Type": "application/xml",
            }
        })
        .then(async (response) => {
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error);
            }

            return response.text()
        });
    }

    static async getDocbookForCategoryStats(categoryId) {
        const endpoint = Routes.getDocbookForCategoryStatistics.replace("{id}", categoryId);

        return await fetch(endpoint, {
            method: "POST",
            headers: {
                "Content-Type": "application/xml",
            }
        })
        .then(async (response) => {
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error);
            }

            return response.text()
        });
    }

    static async getCsvForPostStats(postId) {
        const endpoint = Routes.getCsvForPostStatistics.replace("{id}", postId);

        return await fetch(endpoint, {
            method: "POST",
            headers: {
                "Content-Type": "text/csv",
            }
        })
        .then(async (response) => {
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error);
            }

            return response.text()
        });
    }

    static async getCsvForCategoryStats(categoryId) {
        const endpoint = Routes.getCsvForCategoryStatistics.replace("{id}", categoryId);

        return await fetch(endpoint, {
            method: "POST",
            headers: {
                "Content-Type": "text/csv",
            }
        })
        .then(async (response) => {
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error);
            }

            return response.text()
        });
    }
}