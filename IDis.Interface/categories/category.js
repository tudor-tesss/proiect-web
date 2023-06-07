import { AuthenticationService, PostsService, StatisticsService } from "../@shared/index.js";

window.AuthenticationService = AuthenticationService;
await AuthenticationService.checkSession();

export class CategoryOverviewComponent {
    static async displayPosts() {
        const urlParams = new URLSearchParams(window.location.search);
        const categoryId = urlParams.get('categoryId');

        let categoryBox = document.querySelector(".category-container");
        let innerHtml = ``;

        const posts = await PostsService
            .getAllPostsInCategory(categoryId)
            .catch((error) => {
                console.log(error);
            });

        if (categoryId == null || categoryId === "" || categoryId === undefined || categoryId === "null" || posts == null || posts === "Post.Category.HasNoPosts") {
            categoryBox.innerHTML = `
                <div class="info-box">
                    No posts available for the given category.
                </div>
            `;

            return;
        }

        for (const p of posts) {
            innerHtml += `
                <div class="category-wrapper">
                    <div class="info-box">
                        <a class="info-box title link small animated" href="../posts/post.html?postId=${p.id}">
                            <h1 class="small">${p.title}</h1>
                        </a>
    
                        <p>${p.body}</p>
                    </div>
            `;

            innerHtml += `
                <div class="ratings-wrapper info-box">
            `;
            const ratingKeys = Object.keys(p.ratings);
            ratingKeys.forEach(r => {
                innerHtml += `
                    <div class="info-box thin link">
                        <h3 class="small thin">${r}: ${p.ratings[r]}</h3>
                    </div>
                `;
            });

            innerHtml += `</div>`;
            innerHtml += `</div>`;
        }

        innerHtml += `</div>`;

        categoryBox.innerHTML = innerHtml;
    }

    static addButton() {
        const urlParams = new URLSearchParams(window.location.search);
        const categoryId = urlParams.get('categoryId');

        let div = document.querySelector(".add-wrapper");
        div.innerHTML = `
		    <nav>
                <a class="add-post-button" href="/statistics/statistics.html?isPost=true&targetId=${categoryId}">View Statistics</a>
                <a class="add-category-button" href="/account/account.html">Account</a>
                <div class="dropdown">
                    <button class="dropbtn">Export</button>
                    <div class="dropdown-content">
                        <button onclick="CategoryOverviewComponent.exportPdf()">PDF</button>
                        <button onclick="CategoryOverviewComponent.exportDocbook()">Docbook</button>
                        <button onclick="CategoryOverviewComponent.exportCsv()">CSV</button>
                    </div>
                </div>
                <button class="help-button">Help</button>
            </nav>
        `;
    }

    static async exportDocbook() {
        const urlParams = new URLSearchParams(window.location.search);
        const postId = urlParams.get('categoryId');
    
        const result = await StatisticsService
            .getDocbookForCategoryStats(postId)
            .catch((error) => {
                console.log(error);
            });
    
        const blob = new Blob([result], {type: 'application/docbook+xml'});
    
        const downloadLink = document.createElement('a');
        downloadLink.href = URL.createObjectURL(blob);
        downloadLink.download = 'catstats.xml';
        downloadLink.click();
    }
}

window.CategoryOverviewComponent = CategoryOverviewComponent;
await CategoryOverviewComponent.displayPosts();
CategoryOverviewComponent.addButton();