async function createUser() {
    const endpoint = 'http://localhost:7101/users';

    const name = document.getElementById('name').value;
    const firstName = document.getElementById('first-name').value;
    const email = document.getElementById('email').value;

    const createUserCommand = {
        name: name,
        firstName: firstName,
        emailAddress: email
    };

    await fetch(endpoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(createUserCommand)
    })
    .then(async response => {
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error);
        }

        return response.json();
    })
    .then(() => {
        window.location.href = "sign-in.html";
    })
    .catch(error => {
        console.error('Error:', error);

        var errorMessage = "";
        if (error.message == "User.AlreadyExists") {
            errorMessage = errorMessages["User.AlreadyExists"];
        } 
        else if (error.message == "User.Name.NullOrEmpty") {
            errorMessage = errorMessages["User.Name.NullOrEmpty"];
        } 
        else if (error.message == "User.FirstName.NullOrEmpty") {
            errorMessage = errorMessages["User.FirstName.NullOrEmpty"];
        } 
        else if (error.message == "User.EmailAddress.NullOrEmpty") {
            errorMessage = errorMessages["User.EmailAddress.NullOrEmpty"];
        } 
        else {
            errorMessage = "An error occurred while creating the user.";
        }

        displayError(errorMessage);
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
    "User.AlreadyExists": "Email address is already in use.",
    "User.Name.NullOrEmpty": "Name is required.",
    "User.FirstName.NullOrEmpty": "First name is required.",
    "User.EmailAddress.NullOrEmpty": "Email address is required."
}
