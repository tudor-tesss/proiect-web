function createUserGate() {
    const endpoint = 'http://localhost:7101/users/gates';

    const emailAddress = document.getElementById('email').value;

    const createUserGateCommand = {
        emailAddress: emailAddress
    };

    fetch(endpoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(createUserGateCommand)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(error => {
                throw new Error(error);
            });
        }

        return response.json();
    })
    .then(data => {
        console.log('Success:', data);
        localStorage.setItem('userUuid', data);
        window.location.href = "sign-in-code-gate.html";
    })
    .catch(error => {
        console.error('Error:', error);

        var errorMessage = "";
        if (error.message == "UserGate.Create.UserDoesNotExist") {
            errorMessage = errorMessages["UserGate.Create.UserDoesNotExist"];
        } 
        else {
            errorMessage = "An error occurred while creating the user gate.";
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
    "UserGate.Create.UserDoesNotExist": "No user found matching the given email address." 
}