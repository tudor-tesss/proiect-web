import { AuthenticationService, PostsService, CategoriesService, StatisticsService } from "../@shared/index.js";

window.AuthenticationService = AuthenticationService;
await AuthenticationService.checkSession();

export class StatisticsOverviewComponent {
    static allPosts;
    static allStatistics;

    static async displayStatistics() {
        this.allPosts = await PostsService.getAllPosts();

        const urlParams = new URLSearchParams(window.location.search);
        const targetId = urlParams.get('targetId');
        const isPost = urlParams.get('isPost');

        if (isPost === "true") {
            if (targetId != null && targetId !== "" && targetId !== undefined && targetId !== "null") {
                this.allStatistics = await StatisticsService.getPostStatistics(targetId);
            }
            else {
                this.allStatistics = await StatisticsService.getPostsStatistics();
            }

            await this.displayPostStatistics(targetId);
        } else {
            if (targetId != null && targetId !== "" && targetId !== undefined && targetId !== "null") {
                this.allStatistics = await StatisticsService.getCategoryStatistics(targetId);
            }
            else {
                this.allStatistics = await StatisticsService.getCategoriesStatistics();
            }

            await this.displayCategoryStatistics(targetId);
        }
    }

    static async displayPostStatistics() {
        if (!Array.isArray(this.allStatistics.statistics)) {
            this.allStatistics.statistics = [this.allStatistics.statistics];
        }

        let statisticsBox = document.querySelector(".statistics-container");
        let innerHtml = ``;

        for (const s of this.allStatistics.statistics) {
            if (s.repliesCount === 0) {
                continue;
            }

            innerHtml += `<div class="statistics-wrapper">`;
            if (s.length === 0 || s === "" || s === [] || s === {} || s.title == null || s.title === "") {
                statisticsBox.innerHTML = "No statistics available.";
                innerHtml += '</div>';

                return;
            }

            innerHtml += `
                <a class="info-box title animated" href="../posts/?postId=${s.postId}">
                    <h2>${s.title} - ${s.repliesCount} replies</h2>
                </a>
            `;

            const score = String(s.averageScore).substring(0, 4);
            innerHtml += `
                <div class="info-box with-button">
                    <p class="info-box link">Average rating: ${score}</p>
                </div>
            `;

            const keys = Object.keys(s.averageScoreByRating);
            for (const key of keys) {
                const value = s.averageScoreByRating[key];

                innerHtml += `
                    <div class="info-box">
                        <p class="info-box link">Average rating for ${key}: ${value}</p>
                    </div>
                `;
            }

            innerHtml += '</div>';

            statisticsBox.innerHTML = innerHtml;
        }
    }

    static async displayCategoryStatistics(categoryId) {
        if (!Array.isArray(this.allStatistics.statistics)) {
            this.allStatistics.statistics = [this.allStatistics.statistics];
        }

        let statisticsBox = document.querySelector(".statistics-container");
        let innerHtml = ``;

        for (const s of this.allStatistics.statistics) {
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
                <a class="info-box title animated" href="../categories/?categoryId=${s.categoryId}">
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
            let p = this.allPosts.find(p => p.id === bestRatedPost);
            innerHtml += `
                <div class="info-box with-button">
                    <a class="info-box link animated" href="../posts/?postId=${bestRatedPost}">
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
                const first = this.allPosts.find(p => p.id === Object.keys(s.postsByRatings[key])[0]);
                innerHtml += `
                    <div class="info-box with-button">
                        <a class="info-box link animated" href="../posts/?postId=${first.id}">Best in ${key}: ${first.title}</a>
        
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
        let statisticsBox = document.querySelector(".statistics-container");

        const category = this.allStatistics.statistics.find(c => c.categoryId === id);
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
            const post = this.allPosts.find(p => p.id === postKey);
            innerHtml += `
                <div class="info-box">
                    <a class="info-box link animated" href="../posts/?postId=post.id">
                        ${post.title} - ${category.postsByRatings[key][p]}
                    </a>
                </div>
            `;

            count++;
        }

        innerHtml += `</div>`;
        statisticsBox.innerHTML = innerHtml;
    }

    static async viewPostsByAverageScore(originalCategoryId, categoryId) {
        let statisticsBox = document.querySelector(".statistics-container");

        const posts = this.allStatistics.statistics.find(c => c.categoryId === categoryId);

        if (posts == null || posts.length === 0 || posts === "" || posts === [] || posts === {} || posts.categoryName == null || posts.categoryName === "") {
            statisticsBox.innerHTML = "No statistics available.";
            return;
        }

        let innerHtml = `<div class="statistics-wrapper">`;

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
            const post = this.allPosts.find(p => p.id === key);
            const score = String(posts.postsByAverageScore[key]).substring(0, 4);
            innerHtml += `
                <div class="info-box">
                    <a class="info-box link animated" href="../posts/?postId=${post.id}">
                        ${post.title} - ${score}
                    </a>
                </div>
            `;
        }

        innerHtml += `</div>`;
        statisticsBox.innerHTML = innerHtml;
    }

    static addButtons() {
        const buttons = document.querySelector(".h-buttons-wrapper");

        const urlParams = new URLSearchParams(window.location.search);
        const isPost = urlParams.get("isPost");

        if (isPost === "true") {
            buttons.innerHTML = `
				 <a class ="add-category-button" href="../statistics/?isPost=false">View Categories Statistics</a>
				 <a class ="add-category-button" href="../account">Account</a>
            `;
        } else {
            buttons.innerHTML = `
                <a class ="add-category-button" href="../statistics/?isPost=true">View Posts Statistics</a>
                <a class ="add-category-button" href="../account">Account</a>
            `;
        }
    }
}

window.StatisticsOverviewComponent = StatisticsOverviewComponent;
await StatisticsOverviewComponent.displayStatistics();
StatisticsOverviewComponent.addButtons();