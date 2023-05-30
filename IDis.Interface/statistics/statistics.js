async function displayStatistics() {
    const urlParams = new URLSearchParams(window.location.search);
    const targetId = urlParams.get('targetId');
    const isPost = urlParams.get('isPost');

    console.log("Target ID: " + targetId);
    console.log("Is Post: " + isPost);

    if (isPost == "true") {
        await displayPostStatistics(targetId);
    }
    else {
        await displayCategoryStatistics(targetId);
    }
}

async function displayPostStatistics(postId) {
    var url = "http://localhost:7101/posts";
    if (postId != null && postId != "" && postId != undefined) {
        url = url + "/" + postId + "/statistics";
    }
    else {
        url = url + "/statistics";
    }

    console.log("URL: " + url);
}

async function displayCategoryStatistics(categoryId) {
    var url = "http://localhost:7101/categories";
    if (categoryId != null && categoryId != "" && categoryId != undefined && categoryId != "null") {
        url = url + "/" + categoryId;
    }
    url = url + "/statistics";

    console.log("URL: " + url);

    await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
    })
    .then(async response => {
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error);
        }

        return await response.json();
    })
    .then(async d => {
        if (!Array.isArray(d.statistics)) {
            d.statistics = [d.statistics];
        }

        var statisticsBox = document.querySelector(".statistics-container");
        var innerHtml = ``;

        for (const s of d.statistics) {
            if (s.postCount == 0) {
                continue;
            }
            
            innerHtml += `<div class="statistics-wrapper">`;
            if (s == null || s == undefined || s.length == 0 || s == "" || s == [] || s == {} || s.categoryName == null || s.categoryName == undefined || s.categoryName == "") {
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
                        <button type="button" class="submit-b" onClick="viewPostsByAverageScore('${categoryId}', '${s.categoryId}')">View all</button>
                    </div>
                </div>
            `;

            const bestRatedPost = Object.keys(s.postsByAverageScore)[0];
            await getPost(bestRatedPost)
                .then(p => {
                    innerHtml += `
                        <div class="info-box with-button">
                            <a class="info-box link animated" href="../posts/post.html?postId=${bestRatedPost}">
                                Top rated post: ${p.title}
                            </a>

                            <div class="submit-wrapper">
                                <button type="button" class="submit-b" onClick="viewPostsByAverageScore('${categoryId}', '${s.categoryId}')">View all</button>
                            </div>
                        </div>
                    `;
                });
            
            let count = 0;
            const keys = Object.keys(s.postsByRatings);
            for (const key of keys) {
                const first = await getPost(Object.keys(s.postsByRatings[key])[0]);
                innerHtml += `
                    <div class="info-box with-button">
                        <a class="info-box link animated" href="../posts/post.html?postId=${first.id}">Best in ${key}: ${first.title}</a>

                        <div class="submit-wrapper">
                            <button type="button" class="submit-b" onClick="viewPostsByRatings('${categoryId}', '${s.categoryId}', ${count})">View all</button>
                        </div>
                    </div>
                `;

                count++;
            }

            innerHtml += '</div>';
        }

        statisticsBox.innerHTML = innerHtml;
    });    
}

async function viewPostsByRatings(originalCategoryId, id, index) {
    const url = "http://localhost:7101/categories/statistics";

    await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
    })
    .then(async response => {
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error);
        }

        return await response.json();
    })
    .then(async d => {
        const category = d.statistics.find(s => s.categoryId == id);

        var statisticsBox = document.querySelector(".statistics-container");

        if (category == null || category == undefined || category.length == 0 || category == "" || category == [] || category == {} || category.categoryName == null || category.categoryName == undefined || category.categoryName == "") {
            statisticsBox.innerHTML = "No statistics available.";
            
            return;
        }

        var innerHtml = `
            <div class="statistics-wrapper">
        `;

        innerHtml += `
            <button type="button" class="submit-b small" onClick="displayCategoryStatistics('${originalCategoryId}')">
                Back
            </button>
        `;

        const key = Object.keys(category.postsByRatings)[index];
        innerHtml += `
            <h2 class="info-box title">${key}</h2>
        `;

        const posts = Object.keys(category.postsByRatings[key]);
        var count = 0;
        for (const p of posts) {
            const postKey = Object.keys(category.postsByRatings[key])[count];
            const post = await getPost(postKey);
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

async function viewPostsByAverageScore(originalCategoryId, categoryId) {
    const url = "http://localhost:7101/categories/statistics";

    await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
    })
    .then(async response => {
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error);
        }

        return await response.json();
    })
    .then(async d => {
        const posts = d.statistics.find(s => s.categoryId == categoryId);

        if (posts == null || posts == undefined || posts.length == 0 || posts == "" || posts == [] || posts == {} || posts.categoryName == null || posts.categoryName == undefined || posts.categoryName == "") {
            statisticsBox.innerHTML = "No statistics available.";
            return;
        }

        var statisticsBox = document.querySelector(".statistics-container");
        var innerHtml = `
            <div class="statistics-wrapper">
        `;

        innerHtml += `
            <button type="button" class="submit-b small" onClick="displayCategoryStatistics('${originalCategoryId}')">
                Back
            </button>
        `;

        innerHtml += `
            <h2 class="info-box title">Posts by average score</h2>
        `;

        const averageKeys = Object.keys(posts.postsByAverageScore);
        for (const key of averageKeys) {
            const post = await getPost(key);
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

async function getPost(postId) {
    return await fetch(`http://localhost:7101/posts/${postId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
    .then(async response => {
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error);
        }

        return await response.json();
    })
    .catch((error) => {
        return "";
    })
}