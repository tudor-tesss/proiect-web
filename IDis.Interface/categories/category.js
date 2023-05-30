async function displayPosts() {
    const urlParams = new URLSearchParams(window.location.search);
    const categoryId = urlParams.get('categoryId');

    var categoryBox = document.querySelector(".category-container");
    var innerHtml = ``;

    const posts = await getAllPosts(categoryId);
    if (categoryId == null || categoryId == "" || categoryId == undefined || categoryId == "null" || posts == null || posts == undefined || posts == "Post.Category.HasNoPosts") {
        categoryBox.innerHTML = `
            <div class="info-box">
                No posts available for the given category.
            </div>
        `;

        return;
    }
    const category = await getCategory(categoryId);
    const postCount = posts.length;

    // innerHtml += `
    //     <div class="category-wrapper">
    //         <div class="info-box title">
    //             <h2>${category.name}  -  ${postCount} posts</h2>
    //         </div>
    //     </div>
    // `;

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
                <div class="info-box title thin link small">
                    <h3 class="small">${r}: ${p.ratings[r]}</h3>
                </div>
            `;
        });

        innerHtml += `</div>`;
        innerHtml += `</div>`;
    }

    innerHtml += `</div>`;

    categoryBox.innerHTML = innerHtml;
}

function addButton() {
    const urlParams = new URLSearchParams(window.location.search);
    const categoryId = urlParams.get('categoryId');

    var div = document.querySelector(".add-wrapper");
    div.innerHTML = `
		<nav>
            <a class="add-post-button" href="../posts/add/add-post.html?categoryId=${categoryId}">Add Post</a>
            <button class="help-button" onClick="displayLoginForm()">Help</button>
        </nav>
    `;
}

async function getCategory(categoryId) {
    const url = "http://localhost:7101/categories";

    const categories = await fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
    .then(async response => {
        return await response.json();
    });

    return categories.find(c => c.id == categoryId);
}

async function getAllPosts(categoryId) {
    return await fetch(`http://localhost:7101/categories/${categoryId}/posts`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
    .then(async response => {
        return await response.json();
    });
}