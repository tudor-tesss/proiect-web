export class PdfService{
    static async getPdfForPostStats(postId) {

        const endpoint = `http://localhost:7101/posts/${postId}/statistics/pdf`;

        return await fetch(endpoint, {
            method: "GET",
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
}