import { AuthenticationService, Errors, PostsService, UsersService, StatisticsService } from "../@shared/index.js";

window.AuthenticationService = AuthenticationService;
await AuthenticationService.checkSession();

export class PostOverviewComponent {
    static replyBoxOpen = false;
    static post = null;

    static async displayPost() {
        const urlParams = new URLSearchParams(window.location.search);
        const postId = urlParams.get('postId');

        let postBox = document.querySelector(".post-container");
        let innerHtml = ``;

        this.post = await PostsService
            .getPost(postId)
            .catch((error) => {
                console.log(error);
            });
        
        innerHtml += `<div class="post-wrapper">`;

        if (postId == null || postId === "" || postId === undefined || postId === "null" || this.post == null) {
            postBox.innerHTML = `
                <div class="info-box on-column">
                    No post available for the given id.
                </div>
            `;

            return;
        }

        const userInfo = await UsersService.getUserById(this.post.authorId);
        innerHtml += `
            <div class="info-box on-column">
                <h1 class="info-box on-column title link small animated">${this.post.title}</h1>
                <h3 class="info-box on-column title link small animated">By: ${userInfo.firstName} ${userInfo.name}</h3>
                <p class="word-wrap">${this.post.body}</p>
            </div>
        `;

        innerHtml += `
            <div class="ratings-wrapper info-box">
        `;
        
        const ratingKeys = Object.keys(this.post.ratings);
        ratingKeys.forEach(r => {
            innerHtml += `
                <div class="info-box on-column link">
                    <h3 class="small thin">${r}: ${this.post.ratings[r]}</h3>
                </div>
            `;
        });
        innerHtml += `</div>`;

        innerHtml += `
                <button class="submit-b" onclick="PostOverviewComponent.toggleReplyBox()">Reply</button>
        `;        

        innerHtml += `</div>`;

        innerHtml += `
            <div class="reply-box-wrapper"></div>
        `;

        const replies = await PostsService
            .getPostReplies(postId)
            .catch((error) => {
                console.log(error);
            });

            if (replies) {
                const replyPromises = replies.map((r) => {
                    return UsersService
                        .getUserById(r.authorID)
                        .then((replyAuthor) => {
                            let replyInnerHtml = `
                                <div class="post-wrapper on-column">
                                    <div class="info-box on-column">
                                        <h3 class="info-box on-column title link small animated">${r.title}</h3>
                                        <h4 class="info-box on-column title link small animated">By: ${replyAuthor.firstName} ${replyAuthor.name}</h4>
                                        <p class="word-wrap">${r.body}</p>
                                    </div>
        
                                    <div class="ratings-wrapper info-box on-column">
                            `;
        
                            const ratingKeys = Object.keys(r.ratings);
                            ratingKeys.forEach(k => {
                                replyInnerHtml += `
                                    <div class="info-box on-column thin link">
                                        <h3 class="small thin">${k}: ${r.ratings[k]}</h3>
                                    </div>
                                `;
                            });
                            replyInnerHtml += `</div></div>`;
        
                            return replyInnerHtml;
                        })
                        .catch((error) => {
                            console.log(error);
                            return '';  // return an empty string on error
                        });
                });
        
                const replyInnerHtmls = await Promise.all(replyPromises);
        
                for (let replyInnerHtml of replyInnerHtmls) {
                    innerHtml += replyInnerHtml;
                }
            }

        innerHtml += `</div>`;        

        postBox.innerHTML = innerHtml;
    }

    static toggleReplyBox() {
        if (PostOverviewComponent.replyBoxOpen) {
            PostOverviewComponent.closeReplyBox();
        } else {
            PostOverviewComponent.openReplyBox();
        }
    }

    static openReplyBox() {
        PostOverviewComponent.replyBoxOpen = true;
        const replyBox = document.querySelector(".reply-box-wrapper");

        let innerHtml = `
                <div class="reply-box">
                    <div class="name-label info-box">
                        <p>Title:</p>
                        <input type="text" id="reply-name" class="input" required></input>
                    </div>

                    <div class="name-label info-box">
                        <p>Description:</p>
                        <textarea class="input" id="reply-description" required oninput="PostOverviewComponent.autoResize(this)"></textarea>
                    </div>
        `;

        const ratingKeys = Object.keys(this.post.ratings);
        for (let i = 0; i < ratingKeys.length; i++) {
            innerHtml += `
                <div class="name-label thin info-box">
                    <p class="no-word-wrap">${ratingKeys[i]}:</p>
                `;
            
            for (let j = 1; j <= 5; j++) {
                innerHtml += `
                    <div class="radio-item">
                        <input type="radio" id="post-rating-${i}-${j}" name="post-rating-${i}" value="${j}" required></input>
                        <label for="post-rating-${i}-${j}">${j}</label>
                    </div>
                `;
            }
                
            
            innerHtml += `</div>`;
        }

        
        innerHtml += `
            <button class="submit-b" onclick="PostOverviewComponent.submitReply()">Submit</button>
            <div id="error-container" class="error-container"></div>
        `;
        innerHtml += `</div>`;
        innerHtml += `</div>`;
        replyBox.innerHTML = innerHtml;
    }

    static closeReplyBox() {
        PostOverviewComponent.replyBoxOpen = false;
        const replyBox = document.querySelector(".reply-box-wrapper");
        replyBox.innerHTML = "";
    }

    static async submitReply() {
        const urlParams = new URLSearchParams(window.location.search);
        const postId = urlParams.get('postId');
        const userId = localStorage.getItem("userUuid");

        const title = document.getElementById("reply-name").value;
        if (title.length < 1) {
            Errors.displayError("Title must be at least 1 character long.", true);
            return;
        }

        const body = document.getElementById("reply-description").value;
        if (body.length < 1) {
            Errors.displayError("Description must be at least 1 character long.", true);
            return;
        }

        const ratingFields = Object.keys(this.post.ratings);
        const ratingValues = new Map();
        for (let i = 0; i < ratingFields.length; i++) {
            const ratingField = ratingFields[i];
            const ratingValue = document.querySelector(`input[name="post-rating-${i}"]:checked`).value;

            if (ratingValue.length < 1) {
                Errors.displayError("All ratings must be filled in.", true);
                return;
            }

            ratingValues.set(ratingField, parseInt(ratingValue));
        }

        await PostsService
            .createPostReply(userId, postId, title, body, ratingValues)
            .then(() => {
                PostOverviewComponent.displayPost();
            })
            .catch((error) => {
                Errors.displayError(errorMessages[error]);
            });
    }

    static autoResize(element) {
        element.style.height = "auto";
        element.style.height = (element.scrollHeight) + "px";
    }

    static addButton() {
        const urlParams = new URLSearchParams(window.location.search);
        const postId = urlParams.get('postId');

        let div = document.querySelector(".add-wrapper");
        div.innerHTML = `
            <nav>
                <a class="add-post-button" href="/statistics/?isPost=true&targetId=${postId}">View Statistics</a>
                <a class="add-category-button" href="/account">Account</a>
                <div class="dropdown">
                    <button class="dropbtn">Export</button>
                    <div class="dropdown-content">
                        <button onclick="PostOverviewComponent.exportPdf()">PDF</button>
                        <button onclick="PostOverviewComponent.exportDocbook()">Docbook</button>
                        <button onclick="PostOverviewComponent.exportCsv()">CSV</button>
                    </div>
                </div>
                <button class="help-button">Help</button>
            </nav>
    `;
    }

    static async exportPdf() {
        const urlParams = new URLSearchParams(window.location.search);
        const postId = urlParams.get('postId');
    
        const base64String = await StatisticsService
            .getPdfForPostStats(postId)
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
        downloadLink.download = 'poststats.pdf';
        downloadLink.click();
    }

    static async exportDocbook() {
        const urlParams = new URLSearchParams(window.location.search);
        const postId = urlParams.get('postId');
    
        const result = await StatisticsService
            .getDocbookForPostStats(postId)
            .catch((error) => {
                console.log(error);
            });
    
        const blob = new Blob([result], {type: 'application/docbook+xml'});
    
        const downloadLink = document.createElement('a');
        downloadLink.href = URL.createObjectURL(blob);
        downloadLink.download = 'poststats.xml';
        downloadLink.click();
    }

    static async exportCsv() {
        const urlParams = new URLSearchParams(window.location.search);
        const postId = urlParams.get('postId');
    
        const result = await StatisticsService
            .getCsvForPostStats(postId)
            .catch((error) => {
                console.log(error);
            });
    
        const blob = new Blob([result], {type: 'text/csv'});
    
        const downloadLink = document.createElement('a');
        downloadLink.href = URL.createObjectURL(blob);
        downloadLink.download = 'poststats.csv';
        downloadLink.click();
    }
}

window.PostOverviewComponent = PostOverviewComponent;
await PostOverviewComponent.displayPost();
PostOverviewComponent.addButton();