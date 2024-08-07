import { AuthenticationService, PostsService, StatisticsService, DarkmodeLook } from "../@shared/index.js";

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
                        <a class="info-box title link small animated" href="../posts/?postId=${p.id}">
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
                <div class="dark-button">
                    <button class="toggle-button" id="toggle-button">
                        <img id="toggle-image" src="../resources/icons/light.png">
                    </button>
                </div>
                
                <div class="nav-buttons">
                    <a class="add-post-button" href="/statistics/?isPost=false&targetId=${categoryId}">View Statistics</a>
                    <a class="add-post-button" href="/posts/add/?categoryId=${categoryId}">Add Post</a>
                    <a class="add-category-button" href="/account">Account</a>
                    
                    <div class="dropdown">
                        <button class="dropbtn">Export</button>
                        <div class="dropdown-content">
                            <button onclick="CategoryOverviewComponent.exportPdf()">PDF</button>
                            <button onclick="CategoryOverviewComponent.exportDocbook()">Docbook</button>
                            <button onclick="CategoryOverviewComponent.exportCsv()">CSV</button>
                        </div>
                    </div>
                    <button class="add-post-button" onClick="AuthenticationService.deleteSessionTokenCookie()" type="button">Log Out</button>
                </div>
            </nav>
        `;
    }

    static async exportPdf() {
        const urlParams = new URLSearchParams(window.location.search);
        const categoryId = urlParams.get('categoryId');

        const base64String = await StatisticsService
            .getPdfForCategoryStats(categoryId)
            .catch((error) => {
                console.log(error);
            });

        const byteCharacters = atob(base64String);
        const byteArrays = [];
        for (let offset = 0; offset < byteCharacters.length; offset += 512) {
            const slice = byteCharacters.slice(offset, offset + 512);
            const byteNumbers = new Array(slice.length);
            for (let i = 0; i < slice.length; i++) {
                byteNumbers[i] = slice.charCodeAt(i);
            }
            const byteArray = new Uint8Array(byteNumbers);
            byteArrays.push(byteArray);
        }
        const blob = new Blob(byteArrays, {type: 'application/pdf'});

        const downloadLink = document.createElement('a');
        downloadLink.href = URL.createObjectURL(blob);
        downloadLink.download = 'category_stats.pdf';
        downloadLink.click();
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

    static async exportCsv() {
        const urlParams = new URLSearchParams(window.location.search);
        const postId = urlParams.get('categoryId');
    
        const result = await StatisticsService
            .getCsvForCategoryStats(postId)
            .catch((error) => {
                console.log(error);
            });
    
        const blob = new Blob([result], {type: 'application/csv'});
    
        const downloadLink = document.createElement('a');
        downloadLink.href = URL.createObjectURL(blob);
        downloadLink.download = 'catstats.csv';
        downloadLink.click();
    }
    
    static displayDarkMode(){
        const toggleButton = document.getElementById('toggle-button');
        toggleButton.addEventListener('click', DarkmodeLook.handleToggleDarkMode);

        const storedDarkMode = localStorage.getItem('darkMode');
        const isDarkMode = storedDarkMode === 'true' ? true : storedDarkMode === 'false' ? false : DarkmodeLook.isDarkMode();
        DarkmodeLook.updateImageSource(isDarkMode);
    
        DarkmodeLook.setTheme(isDarkMode ? 'dark' : 'light');
    }
}

window.CategoryOverviewComponent = CategoryOverviewComponent;
await CategoryOverviewComponent.displayPosts();
CategoryOverviewComponent.addButton();
CategoryOverviewComponent.displayDarkMode();