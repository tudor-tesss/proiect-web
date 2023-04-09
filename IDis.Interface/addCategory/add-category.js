var numberOfFieldsDropdown = document.getElementById('numberOfFields');
numberOfFieldsDropdown.addEventListener('change', generateTextInputFields);
generateTextInputFields()

function generateTextInputFields() {
    var numberOfFields = document.getElementById("numberOfFields").value;
    var textInputFields = document.getElementById("rating-fields");
  
    // clear the div where the text input fields will be inserted
    textInputFields.innerHTML = "";
  
    // generate a dynamic number of text input fields based on the selected value
    for (var i = 1; i <= numberOfFields; i++) {
      var input = document.createElement("input");
      input.classList.add("input")
      input.type = "text";
      input.placeholder = "Rating " + i;
      input.name = "rating-" + i;
      textInputFields.appendChild(input);
    }
  }