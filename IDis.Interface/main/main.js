
async function getAllCategories() {
    let names = [];
    const endpoint = "http://localhost:7101/categories";

    const getAllCategoriesCommand={}


    await fetch(endpoint, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        }
    })
        .then(async (response) => {
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error);
            }

            return response.json();
        })
        .then((data) => {
            names = data.map((item) => item.name);
            console.log(data);
        })
        .catch((error) => {
            console.error("Error:", error);

            var errorMessage = error.message;

            if (errorMessages[errorMessage] == undefined) {
                errorMessage = "An error occurred getting the categories.";
            }
            displayError(errorMessages[errorMessage]);
        });

    return names;
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
    "Category.NoCategoriesInDatabase": "Sorry, we do not have categories yet, please add one."
};

function displayCategories() {
    const buttonsBox = document.querySelector('.buttons-box');

    // Clear any existing buttons
    buttonsBox.innerHTML = '';

    getAllCategories().then((names) => {
        // Iterate over the names array and generate buttons
        for (let i = 0; i < names.length; i++) {
            const name = names[i];

            const button = document.createElement('button');
            button.type = 'button';
            button.classList.add('category-button');
            button.textContent = name;
            button.addEventListener('click', () => {
                // Handle button click event
                console.log(`Button "${name}" clicked`);
                // Add your desired logic here
            });

            buttonsBox.appendChild(button);
        }
    });
}

// Call the displayCategories function
displayCategories();

