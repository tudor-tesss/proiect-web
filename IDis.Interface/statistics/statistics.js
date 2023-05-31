import { AuthenticationService, PostsService, CategoriesService, StatisticsService } from "../../@shared/index.js";

window.AuthenticationService = AuthenticationService;
await AuthenticationService.checkSession();

const allPosts = await PostsService.getAllPosts();

export class StatisticsOverviewComponent {
    static async displayStatistics() {
        const urlParams = new URLSearchParams(window.location.search);
        const targetId = urlParams.get('targetId');
        const isPost = urlParams.get('isPost');

        console.log("Target ID: " + targetId);
        console.log("Is Post: " + isPost);

        if (isPost === "true") {
            await this.displayPostStatistics(targetId);
        } else {
            await this.displayCategoryStatistics(targetId);
        }
    }

    static async displayPostStatistics(postId) {
        let url = "http://localhost:7101/posts";
        if (postId != null && postId !== "" && postId !== undefined) {
            url = url + "/" + postId + "/statistics";
        } else {
            url = url + "/statistics";
        }

        console.log("URL: " + url);
    }

    static async displayCategoryStatistics(categoryId) {
        let d = null;
        if (categoryId != null && categoryId !== "" && categoryId !== undefined && categoryId !== "null") {
            d = await StatisticsService.getCategoryStatistics(categoryId);
        }
        else {
            d = await StatisticsService.getCategoriesStatistics();
        }

        if (!Array.isArray(d.statistics)) {
            d.statistics = [d.statistics];
        }

        let statisticsBox = document.querySelector(".statistics-container");
        let innerHtml = ``;

        for (const s of d.statistics) {
            if (s.postCount === 0) {
                continue;
            }

            innerHtml += `<div class="statistics-wrapper">`;
            if (s.length === 0 || s === "" || s === [] || s === {} || s.categoryName == null || s.categoryName === "") {
                statisticsBox.innerHTML = "No statistics available.";
                innerHtml += '</div>';

                return;
            }

            innerHtml += `
                <a class="info-box title animated" href="../categories/category.html?categoryId=${s.categoryId}">
                            <h2>${s.categoryName} - ${s.postCount} posts</h2>
                </a>
            `;

            const score = String(s.averageScore).substring(0, 4);
            innerHtml += `
                <div class="info-box with-button">
                    <p class="info-box link">Average rating: ${score}</p>
        
                    <div class="submit-wrapper">
                        <button type="button" class="submit-b" onClick="StatisticsOverviewComponent.viewPostsByAverageScore('${categoryId}', '${s.categoryId}')">View all</button>
                    </div>
                </div>
            `;

            const bestRatedPost = Object.keys(s.postsByAverageScore)[0];
            let p = allPosts.find(p => p.id === bestRatedPost);
            innerHtml += `
                <div class="info-box with-button">
                    <a class="info-box link animated" href="../posts/post.html?postId=${bestRatedPost}">
                        Top rated post: ${p.title}
                    </a>
        
                    <div class="submit-wrapper">
                        <button type="button" class="submit-b" onClick="StatisticsOverviewComponent.viewPostsByAverageScore('${categoryId}', '${s.categoryId}')">View all</button>
                    </div>
                </div>
            `;

            let count = 0;
            const keys = Object.keys(s.postsByRatings);
            for (const key of keys) {
                const first = allPosts.find(p => p.id === Object.keys(s.postsByRatings[key])[0]);
                innerHtml += `
                    <div class="info-box with-button">
                        <a class="info-box link animated" href="../posts/post.html?postId=${first.id}">Best in ${key}: ${first.title}</a>
        
                        <div class="submit-wrapper">
                            <button type="button" class="submit-b" onClick="StatisticsOverviewComponent.viewPostsByRatings('${categoryId}', '${s.categoryId}', ${count})">View all</button>
                        </div>
                    </div>
                `;

                count++;
            }

            innerHtml += '</div>';
        }

        statisticsBox.innerHTML = innerHtml;
    }

    static async viewPostsByRatings(originalCategoryId, id, index) {
        const url = "http://localhost:7101/categories/statistics";

        await StatisticsService.getCategoryStatistics(id)
            .then(async d => {
                const category = d.statistics;

                let statisticsBox = document.querySelector(".statistics-container");

                if (category == null || category.length === 0 || category === "" || category === [] || category === {} || category.categoryName == null || category.categoryName === "") {
                    statisticsBox.innerHTML = "No statistics available.";

                    return;
                }

                let innerHtml = `
                    <div class="statistics-wrapper">
                `;

                innerHtml += `
                    <button type="button" class="submit-b small" onClick="StatisticsOverviewComponent.displayCategoryStatistics('${originalCategoryId}')">
                        Back
                    </button>
                `;

                const key = Object.keys(category.postsByRatings)[index];
                innerHtml += `
                    <h2 class="info-box title">${key}</h2>
                `;

                const posts = Object.keys(category.postsByRatings[key]);
                let count = 0;
                for (const p of posts) {
                    const postKey = Object.keys(category.postsByRatings[key])[count];
                    const post = allPosts.find(p => p.id === postKey);
                    innerHtml += `
                        <div class="info-box">
                            <a class="info-box link animated" href="../posts/post.html?postId=post.id">
                                ${post.title} - ${category.postsByRatings[key][p]}
                            </a>
                        </div>
                    `;

                    count++;
                }

                innerHtml += `</div>`;
                statisticsBox.innerHTML = innerHtml;
            });
    }

    static async viewPostsByAverageScore(originalCategoryId, categoryId) {
        let statisticsBox = document.querySelector(".statistics-container");

        await StatisticsService.getCategoryStatistics(categoryId)
            .then(async d => {
                const posts = d.statistics;
                const allPosts = await PostsService.getAllPosts();

                if (posts == null || posts.length === 0 || posts === "" || posts === [] || posts === {} || posts.categoryName == null || posts.categoryName === "") {
                    statisticsBox.innerHTML = "No statistics available.";
                    return;
                }

                let innerHtml = `
                    <div class="statistics-wrapper">
                `;

                innerHtml += `
                    <button type="button" class="submit-b small" onClick="StatisticsOverviewComponent.displayCategoryStatistics('${originalCategoryId}')">
                        Back
                    </button>
                `;

                innerHtml += `
                    <h2 class="info-box title">Posts by average score</h2>
                `;

                const averageKeys = Object.keys(posts.postsByAverageScore);
                for (const key of averageKeys) {
                    const post = allPosts.find(p => p.id === key);
                    const score = String(posts.postsByAverageScore[key]).substring(0, 4);
                    innerHtml += `
                        <div class="info-box">
                            <a class="info-box link animated" href="../posts/post.html?postId=${post.id}">
                                ${post.title} - ${score}
                            </a>
                        </div>
                    `;
                }

                innerHtml += `</div>`;
                statisticsBox.innerHTML = innerHtml;
            });
    }
}

window.StatisticsOverviewComponent = StatisticsOverviewComponent;
await StatisticsOverviewComponent.displayStatistics();