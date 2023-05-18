

async function createCategory(){
    const endpoint = 'http://localhost:7101/categories';

    const name = document.getElementById('category-name').value;
    var inputs = document.querySelectorAll(".rating-field")
    var ratingFields = Array.from(inputs).map(input => input.value);

    console.log(ratingFields);

    var creatorId = localStorage.getItem('userUuid');


    const createCategoryCommand={
        name: name,
        ratingFields: ratingFields,
        creatorId: creatorId
    };

    await fetch(endpoint,{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(createCategoryCommand)
    })
        .then(async response => {
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error);
            }

            return response.json();
        })
        .then(() => {

            window.location.href = "../main/add-post.html";

        })
        .catch(error => {
            console.error('Error:', error);

            var errorMessage = error.message;

            if (errorMessages[errorMessage] == undefined) {
                errorMessage = "An error occurred while creating your account.";
            }

            displayError(errorMessages[errorMessage]);
        });
}

function displayError(errorMessage) {
    const errorContainer = document.getElementById('error-container');
    errorContainer.textContent = errorMessage;
    errorContainer.style.display = 'block';

    setTimeout(() => {
        errorContainer.style.display = 'none';
    }, 5000);
}

errorMessages = {
    "Category.AlreadyExists": "There is already a category with the same name.",
    "Category.Name.NullOrEmpty": "Name is required.",
    "Category.RatingField.NullOrEmpty": "Rating field cannot be empty.",
}


function updateTextInputFields() {
    var textInputFields = document.getElementById("input-field");

    // Clear the div where the text input fields will be inserted
    textInputFields.innerHTML = "";

    // Generate the initial set of text input fields
    for (var i = 1; i <= 1; i++) {
        var inputContainer = document.createElement("div");
        inputContainer.classList.add("input-field");

        var input = document.createElement("input");
        input.type = "text";
        input.placeholder = "New Rating Field ";
        input.name = "rating-" + i;
        input.required = true;
        input.classList.add("rating-field");

        inputContainer.appendChild(input);
        textInputFields.appendChild(inputContainer);
    }
}


function displayCategoryForm() {
    const categoryForm = document.querySelector('.category-form');
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
        <button type="button" class="remove-field-btn" onClick="removeField()">Remove</button>
      </div>
      <div id="error-container" class="error-container"></div>
      <div class="submit-wrapper">
        <button type="button" class="submit-b" onClick="createCategory()">Submit</button>
      </div>
    </div>
  `;

    // Generate the initial set of input fields after adding form content to the DOM
    updateTextInputFields();
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

    inputContainer.appendChild(input);

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

