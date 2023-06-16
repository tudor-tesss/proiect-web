import { AuthenticationService, PostsService, CategoriesService } from "../../@shared/index.js";

window.AuthenticationService = AuthenticationService;
await AuthenticationService.checkSession();

export class AddPostComponent {
    static category = null;

    static async displayPostForm() {
        const params = new URLSearchParams(window.location.search);
        const categoryId = params.get("categoryId");

        const categoryForm = document.querySelector(".category-form");
        document.addEventListener("keydown", function(event) {
            if (event.key === "Enter") {
                event.preventDefault(); // Prevent default behavior (e.g., form submission)
                AddPostComponent.createPost(); // Call the createCategory function
            }
        });

        if (!categoryId) {
            window.location.href = "/main";
        }

        this.category = await CategoriesService
            .getCategory(categoryId)
            .catch((error) => {
                console.log(error);
            });

        const postForm = document.querySelector(".post-form");
        let innerHtml = ``;

        innerHtml += `
            <div class="post-box">
                <h2>Add Post</h2>
                <div class="name-label info-box">
                    <p>Title:</p>
                    <input type="text" class="input" id="post-name" required>
                </div>

                <div class="name-label info-box">
                    <p>Description:</p>
                    <textarea class="input" id="post-description" required oninput="AddPostComponent.autoResize(this)"></textarea>
                </div>
            `;

        for (let i = 0; i < this.category.ratingFields.length; i++) {
            innerHtml += `
                <div class="name-label info-box">
                    <p>${this.category.ratingFields[i]}:</p>
                `;
            
            for (let j = 1; j <= 5; j++) {
                innerHtml += `
                    <label for="post-rating-${i}-${j}">${j}</label>
                    <input type="radio" id="post-rating-${i}-${j}" name="post-rating-${i}" value="${j}" required>
                `;
            }
            
            innerHtml += `</div>`;
        }

        innerHtml += `
            <div class="error-container"></div>
        `;

        innerHtml += `
                <div class="btn-wrapper">
                    <button type="button" class="submit-b" onClick="AddPostComponent.createPost()">Add</button>
                </div>
                <div id="error-container" class="error-container"></div>
            </div>
        `;

        innerHtml += `</div>`;
        innerHtml += `</div>`;
        postForm.innerHTML = innerHtml;
    }

    static async createPost() {
        const name = document.getElementById("post-name").value;
        const body = document.getElementById("post-description").value;
        console.log(body);
        const ratingFields = this.category.ratingFields;
        //get rating values. its a Map<String, Integer> in java
        const ratingValues = new Map();
        for (let i = 0; i < ratingFields.length; i++) {
            const ratingField = ratingFields[i];
            const ratingValue = document.querySelector(`input[name="post-rating-${i}"]:checked`).value;
            ratingValues.set(ratingField, parseInt(ratingValue));
        }

        await PostsService
            .createPost(this.category.id, name, body, ratingValues)
            .then(() => {
                window.location.href = "/main";
            })
            .catch((error) => {
                this.displayError(this.errorMessages[error.message]);
            });
    }

    static displayError(errorMessage) {
        const errorContainer = document.getElementById('error-container');
        errorContainer.textContent = errorMessage;
        errorContainer.style.display = 'block';

        setTimeout(() => {
            errorContainer.style.display = 'none';
        }, 5000);
    }

    static autoResize(element) {
        element.style.height = "auto";
        element.style.height = (element.scrollHeight) + "px";
    }

    static errorMessages = {
        "Post.Title.NullOrEmpty": "Title cannot be empty.",
        "Post.Body.NullOrEmpty": "Description cannot be empty.",
        "Post.Rating.OutOfInterval": "Rating must be between 1 and 5.",
        "Post.User.DoesNotExist": "User does not exist.",
        "Post.Category.DoesNotExist": "Category does not exist.",
        "Post.Ratings.DoesNotMatch": "Rating fields do not match."
    };
}

window.AddPostComponent = AddPostComponent;
AddPostComponent.displayPostForm();