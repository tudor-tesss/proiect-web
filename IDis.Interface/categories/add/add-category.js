import { AuthenticationService, CategoriesService } from "../../@shared/index.js";

window.AuthenticationService = AuthenticationService;
await AuthenticationService.checkSession();

export class AddCategoryComponent {
    static async createCategory() {
        const name = document.getElementById("category-name").value;
        const inputs = document.querySelectorAll(".rating-field");
        const ratingFields = Array.from(inputs).map((input) => input.value);
        const creatorId = localStorage.getItem("userUuid");

        await CategoriesService
            .createCategory(name, ratingFields, creatorId)
            .then(() => {
                window.location.href = "/main/main.html";
            })
            .catch((error) => {
                this.displayError(this.errorMessages[error.message]);
            });
    }

    static updateTextInputFields() {
        let textInputFields = document.getElementById("input-field");

        textInputFields.innerHTML = "";

        for (let i = 1; i <= 1; i++) {
            const inputContainer = document.createElement("div");
            inputContainer.classList.add("input-field");

            const input = document.createElement("input");
            input.type = "text";
            input.placeholder = "New Rating Field ";
            input.name = "rating-" + i;
            input.required = true;
            input.classList.add("rating-field");

            const deleteButton = this.createDeleteButton();

            inputContainer.appendChild(input);
            inputContainer.appendChild(deleteButton);
            textInputFields.appendChild(inputContainer);
        }
    }

    static displayCategoryForm() {
        const categoryForm = document.querySelector(".category-form");
        document.addEventListener("keydown", function(event) {
            if (event.key === "Enter") {
                event.preventDefault(); // Prevent default behavior (e.g., form submission)
                AddCategoryComponent.createCategory(); // Call the createCategory function
            }
        });
        categoryForm.innerHTML = `
            <div class="category-box">
              <h2>Add Category</h2>
              <div class="name-label">
                <input type="text" class="input" id="category-name" required>
                <label>Name</label>
              </div>
              <div id="input-field"></div>
              <div class="btn-wrapper">
                <button type="button" class="add-field-btn" onClick="AddCategoryComponent.addField()">Add</button>
              </div>
              <div id="error-container" class="error-container"></div>
              <div class="submit-wrapper">
                <button type="button" class="submit-b" onClick="AddCategoryComponent.createCategory()">Submit</button>
              </div>
            </div>
        `;

        this.updateTextInputFields();
    }

    static createDeleteButton() {
        let deleteButton = document.createElement("button");
        deleteButton.type = "button";
        deleteButton.classList.add("remove-field-btn");

        deleteButton.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 96 960 960" width="24" fill="currentColor">
                <path d="M261 936q-24.75 0-42.375-17.625T201 876V306h-41v-60h188v-30h264v30h188v60h-41v570q0 24-18 42t-42 18H261Zm438-630H261v570h438V306ZM367 790h60V391h-60v399Zm166 0h60V391h-60v399ZM261 306v570-570Z"/>
            </svg>  
        `;

        deleteButton.onclick = function()
        {
            this.parentNode.remove();
        };

        return deleteButton;
    }

    static addField() {
        const inputContainer = document.createElement("div");
        inputContainer.classList.add("input-field");

        const input = document.createElement("input");
        input.type = "text";
        input.placeholder = "New Rating Field";
        input.name = "rating";
        input.required = true;
        input.classList.add("rating-field");

        const deleteButton = this.createDeleteButton();

        inputContainer.appendChild(input);
        inputContainer.appendChild(deleteButton);

        const inputField = document.getElementById("input-field");
        inputField.appendChild(inputContainer);
    }

    static errorMessages = {
        "Category.AlreadyExists": "There is already a category with the same name.",
        "Category.Name.NullOrEmpty": "Name is required.",
        "Category.RatingField.NullOrEmpty": "Rating field cannot be empty.",
    };

    static displayError(errorMessage) {
        const errorContainer = document.getElementById("error-container");
        errorContainer.textContent = errorMessage;
        errorContainer.style.display = "block";

        setTimeout(() => {
            errorContainer.style.display = "none";
        }, 5000);
    }
}

window.AddCategoryComponent = AddCategoryComponent;
AddCategoryComponent.displayCategoryForm();