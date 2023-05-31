import { AuthenticationService, PostsService, CategoriesService, UsersService } from "../@shared/index.js";

window.AuthenticationService = AuthenticationService;
await AuthenticationService.checkSession();

export class AccountOverviewComponent {
//////////////////////////////      Get Categories       /////////////////////
    static async getCategoryByCreatorId() {
        let names = [];
        let categoryIds = [];
        const userUuid = localStorage.getItem('userUuid');
        await CategoriesService.getCategoriesByCreatorId(userUuid)
            .then((data) => {
                names = data.map((item) => item.name);
                categoryIds = data.map((item) => item.id);
            })
            .catch((error) => {
                console.error("Error:", error);

                const userCategoriesBox = document.querySelector(".user-categories-box");

                throw error;

            });

        return [names, categoryIds];
    }

//////////////////////////////      Get Posts      /////////////////////

    static async getPostsByCreatorId() {
        let names = [];
        let postsIds = [];
        const userUuid = localStorage.getItem('userUuid');
        await PostsService.getPostsByCreatorId(userUuid)
            .then((data) => {
                names = data.map((item) => item.title);
                postsIds = data.map((item) => item.id);
            })
            .catch((error) => {
                console.error("Error:", error);

                const userPostsBox = document.querySelector(".user-posts-box");

                throw error;

            });

        return [names, postsIds];
    }

//////////////////////////////      Get User     /////////////////////

    static async getUserById() {
        let name;
        let firstName;
        let email;
        const userUuid = localStorage.getItem('userUuid');
        await UsersService.getUserById(userUuid)
            .then((data) => {

                console.log(data);
                name = data.name || "N/A";
                firstName = data.firstName || "N/A";
                email = data.emailAddress || "N/A";

            })
            .catch((error) => {
                console.error("Error:", error);

                const userPostsBox = document.querySelector(".user-info-box");

                throw error;

            });

        return [name, firstName, email];
    }

    static async displayError(errorMessage, container) {
        const errorContainer = document.createElement("div");
        errorContainer.classList.add("error-container");
        errorContainer.textContent = errorMessage;
        if (errorMessage === "An error occurred getting the categories.Please try again later!") {
            // Show the first SVG for undefined error
            errorContainer.innerHTML = `
            <div class="cloud">
                <img src="../resources/icons/cloud.svg" alt="cloud" width="30%" height="auto" ">
                <p>${errorMessage}</p>
            </div>
        `;
        } else {
            // Show the second SVG for custom error message
            errorContainer.innerHTML = `
            <div class="empty">
                <img src="../resources/icons/sad.svg" alt="empty" width="13%" height="auto">
                <p>${errorMessage}</p>
            </div>
        `;
        }
        container.appendChild(errorContainer);
    }

    errorMessages = {
        "Category.User.DoesntHaveCategories": "Sorry, you do not have any categories yet. Please add one.",
        "Post.CreatorHasNoPosts": "Sorry, you do not have any posts yet. Please add one."
    };

////////////////////////////////////    Display categories  //////////////////
    static async displayCategories() {
        const infoWrapper = document.querySelector(".info-wrapper");
        const categoriesBox = document.querySelector(".user-categories-box");
        // Clear any existing buttons
        categoriesBox.innerHTML = '<h2>Your Categories</h2>';

        try {
            const [names, categoryIds] = await this.getCategoryByCreatorId();

            // Iterate over the names array and generate buttons
            for (let i = 0; i < names.length; i++) {

                const name = names[i];
                const categoryId = categoryIds[i];

                const anch = document.createElement("a");
                anch.classList.add("info-text");
                anch.textContent = name;
                anch.addEventListener("click", () => {
                    // Handle button click event
                    console.log(`Button "${name}" clicked`);
                    window.location.href = `../categories/category.html?categoryId=${categoryId}`;
                });

                categoriesBox.appendChild(anch);
            }

        } catch (error) {

            console.error("Error:", error);
            displayError(errorMessages["Category.User.DoesntHaveCategories"], categoriesBox);

            throw error;
        }

    }

///////////////////////////////     Display Posts   ///////////////////////////////

    static async displayPosts() {
        const infoWrapper = document.querySelector(".info-wrapper");
        const postsBox = document.querySelector(".user-posts-box");
        // Clear any existing buttons
        postsBox.innerHTML = '<h2>Your Posts</h2>';

        try {
            const [names, postsIds] = await this.getPostsByCreatorId();

            // Iterate over the names array and generate buttons
            for (let i = 0; i < names.length; i++) {

                const name = names[i];
                const postId = postsIds[i];

                const anch = document.createElement("a");
                anch.classList.add("info-text");
                anch.textContent = name;
                anch.addEventListener("click", () => {
                    // Handle button click event
                    console.log(`Button "${name}" clicked`);
                    window.location.href = `../posts/post.html?postId=${postId}`;
                });

                postsBox.appendChild(anch);
            }

        } catch (error) {
            console.error("Error:", error);
            displayError(errorMessages["Post.CreatorHasNoPosts"], postsBox);

        }

    }

///////////////////////////////     Display UserInfo  ///////////////////////////////

    static async displayUser() {
        const infoWrapper = document.querySelector(".info-wrapper");
        const userBox = document.querySelector(".user-info-box");
        // Clear any existing buttons
        userBox.innerHTML = '<h2>General Info</h2>';

        try {
            const [nameG, firstNameG, emailG] = await this.getUserById();

            const name = nameG;
            const firstName = firstNameG;
            const email = emailG;

            const pargN = document.createElement("p");
            pargN.classList.add("info-text-user");
            pargN.textContent = "Name:\t\t" + name;

            const pargF = document.createElement("p");
            pargF.classList.add("info-text-user");
            pargF.textContent = "First Name:\t" + firstName;

            const pargE = document.createElement("p");
            pargE.classList.add("info-text-user");
            pargE.textContent = "Email:\t" + email;

            userBox.appendChild(pargN);
            userBox.appendChild(pargF);
            userBox.appendChild(pargE);


        } catch (error) {
            console.error("Error:", error);

        }
    }
}

await AccountOverviewComponent.displayPosts();
await AccountOverviewComponent.displayCategories();
await AccountOverviewComponent.displayUser();
