import { Routes } from "../routes/routes.js";

export class StatisticsService {
    static async getCategoryStatistics(categoryId) {
        const endpoint = Routes.statistics.getCategoryStatistics.replace("{id}", categoryId);

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
        const endpoint = Routes.statistics.getCategoriesStatistics;

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
        const endpoint = Routes.statistics.getPostStatistics.replace("{id}", postId);

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
        const endpoint = Routes.statistics.getPostsStatistics;

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
        const endpoint = Routes.statistics.getPdfForPostStatistics.replace("{id}", postId);

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
        const endpoint = Routes.statistics.getPdfForCategoryStatistics.replace("{id}", categoryId);

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
        const endpoint = Routes.statistics.getDocbookForPostStatistics.replace("{id}", postId);

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
        const endpoint = Routes.statistics.getDocbookForCategoryStatistics.replace("{id}", categoryId);

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
        const endpoint = Routes.statistics.getCsvForPostStatistics.replace("{id}", postId);

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
        const endpoint = Routes.statistics.getCsvForCategoryStatistics.replace("{id}", categoryId);

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