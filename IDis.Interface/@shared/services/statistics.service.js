export class StatisticsService {
    static async getCategoryStatistics(categoryId) {
        const endpoint = `http://localhost:7101/categories/${categoryId}/statistics`;

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
        const endpoint = `http://localhost:7101/categories/statistics`;

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
        const endpoint = `http://localhost:7101/posts/${postId}/statistics`;

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
        const endpoint = `http://localhost:7101/posts/statistics`;

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
        const endpoint = `http://localhost:7101/posts/${postId}/statistics/pdf`;

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
        const endpoint = `http://localhost:7101/posts/${postId}/statistics/docbook`;

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
}