async function createCategory() {
    const endpoint = "http://localhost:7101/categories";

    const name = document.getElementById("category-name").value;
    var inputs = document.querySelectorAll(".rating-field");
    var ratingFields = Array.from(inputs).map((input) => input.value);

    console.log(ratingFields);

    var creatorId = localStorage.getItem("userUuid");

    const createCategoryCommand = {
        name: name,
        ratingFields: ratingFields,
        creatorId: creatorId,
    };

    await fetch(endpoint, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(createCategoryCommand),
    })
    .then(async (response) => {
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error);
        }

        return response.json();
    })
    .then(() => {
        window.location.href = "../main/add-post.html";
    })
    .catch((error) => {
        console.error("Error:", error);

        var errorMessage = error.message;

        if (errorMessages[errorMessage] == undefined) {
            errorMessage = "An error occurred while creating your account.";
        }
        displayError(errorMessages[errorMessage]);
    });
}

function displayError(errorMessage) {
    const errorContainer = document.getElementById("error-container");
    errorContainer.textContent = errorMessage;
    errorContainer.style.display = "block";

    setTimeout(() => {
        errorContainer.style.display = "none";
    }, 5000);
}

errorMessages = {
    "Category.AlreadyExists": "There is already a category with the same name.",
    "Category.Name.NullOrEmpty": "Name is required.",
    "Category.RatingField.NullOrEmpty": "Rating field cannot be empty.",
};

function updateTextInputFields() {
    var textInputFields = document.getElementById("input-field");

    textInputFields.innerHTML = "";

    for (var i = 1; i <= 1; i++) {
        var inputContainer = document.createElement("div");
        inputContainer.classList.add("input-field");

        var input = document.createElement("input");
        input.type = "text";
        input.placeholder = "New Rating Field ";
        input.name = "rating-" + i;
        input.required = true;
        input.classList.add("rating-field");

        var deleteButton = createDeleteButton();

        inputContainer.appendChild(input);
        inputContainer.appendChild(deleteButton);
        textInputFields.appendChild(inputContainer);
    }
}

function displayCategoryForm() {
    const categoryForm = document.querySelector(".category-form");
    categoryForm.innerHTML = `
    <div class="category-box">
      <h2>Add Category</h2>
      <div class="name-label">
        <input type="text" class="input" id="category-name" required>
        <label>Name</label>
      </div>
      <div id="input-field"></div>
      <div class="btn-wrapper">
        <button type="button" class="add-field-btn" onClick="addField()">Add</button>
      </div>
      <div id="error-container" class="error-container"></div>
      <div class="submit-wrapper">
        <button type="button" class="submit-b" onClick="createCategory()">Submit</button>
      </div>
    </div>
  `;

    updateTextInputFields();
}

function createDeleteButton() {
    var deleteButton = document.createElement("button");
    deleteButton.type = "button";
    deleteButton.classList.add("remove-field-btn");

    deleteButton.innerHTML = `
        <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 96 960 960" width="24" fill="currentColor">
            <path d="M261 936q-24.75 0-42.375-17.625T201 876V306h-41v-60h188v-30h264v30h188v60h-41v570q0 24-18 42t-42 18H261Zm438-630H261v570h438V306ZM367 790h60V391h-60v399Zm166 0h60V391h-60v399ZM261 306v570-570Z"/>
        </svg>  
    `;

    deleteButton.onclick = function () {
        this.parentNode.remove();
    };

    return deleteButton;
}

function addField() {
    var inputContainer = document.createElement("div");
    inputContainer.classList.add("input-field");

    var input = document.createElement("input");
    input.type = "text";
    input.placeholder = "New Rating Field";
    input.name = "rating";
    input.required = true;
    input.classList.add("rating-field");

    var deleteButton = createDeleteButton();

    inputContainer.appendChild(input);
    inputContainer.appendChild(deleteButton);

    var inputField = document.getElementById("input-field");
    inputField.appendChild(inputContainer);
}

function removeField() {
    var inputField = document.getElementById("input-field");
    var inputContainers = inputField.getElementsByClassName("input-field");

    if (inputContainers.length > 1) {
        inputField.removeChild(inputContainers[inputContainers.length - 1]);
    }
}
