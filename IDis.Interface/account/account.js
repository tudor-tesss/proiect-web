//////////////////////////////      Get Categories       /////////////////////
async function getCategoryByCreatorId() {
    let names = [];
    let categoryIds = [];
    const userUuid = localStorage.getItem('userUuid');
    const endpoint = `http://localhost:7101/users/${userUuid}/categories`;

    const getCategoryByCreatorId = {}


    await fetch(endpoint, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        }
    })
        .then(async (response) => {
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error);
            }

            return response.json();
        })
        .then((data) => {
            names = data.map((item) => item.name);
            categoryIds = data.map((item) => item.id);
            console.log(data);
        })
        .catch((error) => {
            console.error("Error:", error);

            const userCategoriesBox = document.querySelector(".user-categories-box");

            throw error;

        });

    return [names, categoryIds];
}


//////////////////////////////      Get Posts      /////////////////////

async function getPostsByCreatorId() {
    let names = [];
    let postsIds = [];
    const userUuid = localStorage.getItem('userUuid');
    const endpoint = `http://localhost:7101/users/${userUuid}/posts`;

    const getPostsByCreatorId = {}


    await fetch(endpoint, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        }
    })
        .then(async (response) => {
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error);
            }

            return response.json();
        })
        .then((data) => {
            names = data.map((item) => item.title);
            postIds = data.map((item) => item.id);
            console.log(data);
        })
        .catch((error) => {
            console.error("Error:", error);

            const userPostsBox = document.querySelector(".user-posts-box");

            throw error;

        });

    return [names, postsIds];
}


//////////////////////////////      Get User     /////////////////////

async function getUserById() {
    let name;
    let firstName;
    let email;
    const userUuid = localStorage.getItem('userUuid');
    const endpoint = `http://localhost:7101/users/${userUuid}`;

    const getUserById = {}


    await fetch(endpoint, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        }
    })
        .then(async (response) => {
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error);
            }

            return response.json();
        })
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

async function displayError(errorMessage, container) {
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
async function displayCategories() {
    const infoWrapper = document.querySelector(".info-wrapper");
    const categoriesBox = document.querySelector(".user-categories-box");
    // Clear any existing buttons
    categoriesBox.innerHTML = '<h2>Your Categories</h2>';

    try {
        const [names, categoryIds] = await getCategoryByCreatorId();

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

async function displayPosts() {
    const infoWrapper = document.querySelector(".info-wrapper");
    const postsBox = document.querySelector(".user-posts-box");
    // Clear any existing buttons
    postsBox.innerHTML = '<h2>Your Posts</h2>';

    try {
        const [names, postsIds] = await getPostsByCreatorId();

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

async function displayUser() {
    const infoWrapper = document.querySelector(".info-wrapper");
    const userBox = document.querySelector(".user-info-box");
    // Clear any existing buttons
    userBox.innerHTML = '<h2>General Info</h2>';

    try {
        const [nameG, firstNameG, emailG] = await getUserById();

        const name = nameG;
        const firstName = firstNameG;
        const email = emailG;

        const pargN = document.createElement("p");
        pargN.classList.add("info-text-user");
        pargN.textContent = "Name:\t\t" +name;

        const pargF = document.createElement("p");
        pargF.classList.add("info-text-user");
        pargF.textContent = "First Name:\t"+firstName;

        const pargE = document.createElement("p");
        pargE.classList.add("info-text-user");
        pargE.textContent = "Email:\t"+email;

        userBox.appendChild(pargN);
        userBox.appendChild(pargF);
        userBox.appendChild(pargE);


    } catch (error) {
        console.error("Error:", error);

    }
}

document.addEventListener("DOMContentLoaded", () => {
    displayPosts();
    displayCategories();
    displayUser();
});