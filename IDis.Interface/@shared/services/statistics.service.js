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

        return await fetch(endpoint)
            .then(async (response) => {
                if (!response.ok) {
                    const error = await response.json();
                    throw new Error(error);
                }

                return response.json();
            });
    }
}