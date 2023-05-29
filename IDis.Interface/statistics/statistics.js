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
        url = url + "/" + categoryId + "/statistics";
    }
    else {
        url = url + "/statistics";
    }

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
    .then(d => {
        var statisticsBox = document.querySelector(".statistics-container");
        var innerHtml = ``;

        d.statistics.forEach(s => {
            innerHtml += `<div class="statistics-wrapper">`;
            if (s == null || s == undefined || s.length == 0 || s == "" || s == [] || s == {} || s.categoryName == null || s.categoryName == undefined || s.categoryName == "") {
                statisticsBox.innerHTML = "No statistics available.";
        
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
                        <button type="button" class="submit-b" onClick="viewPostsByAverageScore()">View all</button>
                    </div>
                </div>
            `;

            const bestRatedPost = Object.values(s.postsByAverageScore)[0];
            innerHtml += `
                <div class="info-box with-button">
                    <a class="info-box link animated" href="../posts/post.html?postId=${bestRatedPost.id}">
                        <p>Top rated post: ${bestRatedPost.title}</p>
                    </a>

                    <div class="submit-wrapper">
                        <button type="button" class="submit-b" onClick="viewPostsByAverageScore()">View all</button>
                    </div>
                </div>
            `;
            
            let count = 0;
            Object.keys(s.postsByRatings).forEach(key => {
                const first = Object.keys(s.postsByRatings[key])[0];
                innerHtml += `
                    <div class="info-box with-button">
                        <p class="info-box link">Best in ${key}: ${first}</p>

                        <div class="submit-wrapper">
                            <button type="button" class="submit-b" onClick="viewPostsByRatings('${categoryId}', '${s.categoryId}', ${count})">View all</button>
                        </div>
                    </div>
                `;

                count++;
            });

            innerHtml += '</div>';
        });

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
    .then(d => {
        const category = d.statistics.find(s => s.categoryId == id);

        var statisticsBox = document.querySelector(".content");
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
        posts.forEach(p => {
            const postKey = Object.keys(category.postsByRatings[key])[count];
            innerHtml += `
                <div class="info-box">
                    <p class="info-box link">
                        ${postKey} - ${category.postsByRatings[key][p]}
                    </p>
                </div>
            `;

            count++;
        });

        innerHtml += `</div>`;
        statisticsBox.innerHTML = innerHtml;
    });
}