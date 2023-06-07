import { AuthenticationService, PostsService, UsersService } from "../@shared/index.js";

window.AuthenticationService = AuthenticationService;
await AuthenticationService.checkSession();

export class PostOverviewComponent {
    static async displayPost() {
        const urlParams = new URLSearchParams(window.location.search);
        const postId = urlParams.get('postId');

        let postBox = document.querySelector(".post-container");
        let innerHtml = ``;

        const post = await PostsService
            .getPost(postId)
            .catch((error) => {
                console.log(error);
            });
        
        innerHtml += `<div class="post-wrapper">`;

        if (postId == null || postId === "" || postId === undefined || postId === "null" || post == null) {
            postBox.innerHTML = `
                <div class="info-box">
                    No post available for the given id.
                </div>
            `;

            return;
        }

        const userInfo = await UsersService.getUserById(post.authorId);
        innerHtml += `
            <div class="info-box">
                <h1 class="info-box title link small animated">${post.title}</h1>
                <h3 class="info-box title link small animated">By: ${userInfo.firstName} ${userInfo.name}</h3>
                <p>${post.body}</p>
            </div>
        `;

        innerHtml += `
            <div class="ratings-wrapper info-box">
        `;
        const ratingKeys = Object.keys(post.ratings);
        ratingKeys.forEach(r => {
            innerHtml += `
                <div class="info-box thin link">
                    <h3 class="small">${r}: ${post.ratings[r]}</h3>
                </div>
            `;
        });
        innerHtml += `</div>`;
        innerHtml += `</div>`;

        const replies = await PostsService
            .getPostReplies(postId)
            .catch((error) => {
                console.log(error);
            });

        if (replies) {
            innerHtml += `
                <div class="post-wrapper small title">
                    <h2 class="info-box title link small animated">Replies</h2>
                </div>
            `;

            for (let r of replies) {
                const replyAuthor = await UsersService
                    .getUserById(r.authorID)
                    .catch((error) => {
                        console.log(error);
                    });
                innerHtml += `
                    <div class="post-wrapper">
                        <div class="info-box">
                            <h3 class="info-box title link small animated">${r.title}</h3>
                            <h4 class="info-box title link small animated">By: ${replyAuthor.firstName} ${replyAuthor.name}</h4>
                            <p>${r.body}</p>
                        </div>

                        <div class="ratings-wrapper info-box">
                `;
                const ratingKeys = Object.keys(r.ratings);
                ratingKeys.forEach(k => {
                    innerHtml += `
                        <div class="info-box thin link">
                            <h3 class="small">${k}: ${r.ratings[k]}</h3>
                        </div>
                    `;
                });
                innerHtml += `</div>`;
                innerHtml += `</div>`;
            }
        }

        innerHtml += `</div>`;        

        postBox.innerHTML = innerHtml;
    }
}

window.PostOverviewComponent = PostOverviewComponent;
await PostOverviewComponent.displayPost();

// async function savePdf() {
//     const urlParams = new URLSearchParams(window.location.search);
//     const postId = urlParams.get('postId');

//     const base64String = await StatisticsService
//         .getPdfForPostStats(postId)
//         .catch((error) => {
//             console.log(error);
//         });

//     const byteCharacters = atob(base64String);
//     const byteArrays = [];
//     for (let offset = 0; offset < byteCharacters.length; offset += 512) {
//         const slice = byteCharacters.slice(offset, offset + 512);
//         const byteNumbers = new Array(slice.length);
//         for (let i = 0; i < slice.length; i++) {
//             byteNumbers[i] = slice.charCodeAt(i);
//         }
//         const byteArray = new Uint8Array(byteNumbers);
//         byteArrays.push(byteArray);
//     }
//     const blob = new Blob(byteArrays, {type: 'application/pdf'});

//     const downloadLink = document.createElement('a');
//     downloadLink.href = URL.createObjectURL(blob);
//     downloadLink.download = 'poststats.pdf';
//     // downloadLink.click();
// }

// savePdf();