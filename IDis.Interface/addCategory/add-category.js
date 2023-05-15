var numberOfFieldsDropdown = document.getElementById('numberOfFields');
numberOfFieldsDropdown.addEventListener('change', generateTextInputFields);
generateTextInputFields();

function generateTextInputFields() {
    var numberOfFields = parseInt(numberOfFieldsDropdown.value);
    var textInputFields = document.getElementById("input-field");

    // Clear the div where the text input fields will be inserted
    textInputFields.innerHTML = "";

    // Generate a dynamic number of text input fields based on the selected value
    for (var i = 1; i <= numberOfFields; i++) {
        var inputContainer = document.createElement("div");
        inputContainer.classList.add("name-label", "input-field");

        var input = document.createElement("input");
        input.type = "text";
        input.placeholder = "Rating " + i;
        input.name = "rating-" + i;
        input.id = "ratingId";
        input.required = true;
        input.classList.add("input-field");


        inputContainer.appendChild(input);
        textInputFields.appendChild(inputContainer);
    }
}

function displayCategoryForm() {
    const categoryForm = document.querySelector('.category-form');
    categoryForm.innerHTML = `
    <div class="category-box">
      <h2>Add Category</h2>
      <form>
        <div class="name-label">
          <input type="text" class="input" id="category-name" required>
          <label>Name</label>
        </div>
        <div class="input-field">
        
          <label>Choose the number of rating fields:</label>
          
          <div class="selection-options" id="selection-options">
            <select id="numberOfFields" name="number">
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
              <option value="4">4</option>
              <option value="5">5</option>
            </select>
          </div>
        </div>
        <div id="input-field"></div>
        <div id="error-container" class="error-container"></div>
        <div class="submit-wrapper">
          <button type="button" class="submit-b" onClick="createCategory()">Submit</button>
        </div>
      </form>
    </div>
  `;
    // Generate the initial set of input fields after adding form content to the DOM
    generateTextInputFields();
}
