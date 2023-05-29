
async function getAllCategories() {
    let names = [];
    let categoryIds = [];
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
            categoryIds = data.map((item) => item.id);
            console.log(data);
        })
        .catch((error) => {
            console.error("Error:", error);

            const buttonsWrapper = document.querySelector(".buttons-wrapper");

            buttonsWrapper.innerHTML=`
            <div class="buttons-box">
                <div class="error-container", id="error-container"></div>
            </div>
            `;

            var errorMessage = error.message;

            if (errorMessages[errorMessage] == undefined) {
                errorMessage = "An error occurred getting the categories.Please try again later!";
                displayError(errorMessage);

            }
            else{
                displayError(errorMessages[errorMessage]);
            }

        });

    return [names, categoryIds];
}

 async function displayError(errorMessage) {
    const errorContainer = document.getElementById("error-container");

     if (errorMessage === "An error occurred getting the categories.Please try again later!") {
         // Show the first SVG for undefined error
         errorContainer.innerHTML = `
            <div class="cloud">
                <img src="../resources/icons/cloud-reload.svg" alt="cloud" width="80%" height="auto">
                <p>${errorMessage}</p>
            </div>
        `;
     } else {
         // Show the second SVG for custom error message
         errorContainer.innerHTML = `
            <div class="empty">
                <img src="../resources/icons/empty.svg" alt="empty" width="80%" height="auto">
                <p>${errorMessage}</p>
            </div>
        `;
     }

    errorContainer.style.display = "block";

}

errorMessages = {
    "Category.NoCategoriesInDatabase": "Sorry, we do not have any categories yet. Please add one."
};

async function displayCategories() {
    const buttonsWrapper = document.querySelector(".buttons-wrapper");
    const buttonsBox = document.querySelector(".buttons-box");
    // Clear any existing buttons
    buttonsBox.innerHTML = "";

    try {
        const [names, categoryIds] = await getAllCategories();

        // Iterate over the names array and generate buttons
        for (let i = 0; i < names.length; i++) {

            const name = names[i];
            const categoryId = categoryIds[i];

            const button = document.createElement("button");
            button.type = "button";
            button.classList.add("category-button");
            button.textContent = name;
            button.addEventListener("click", () => {
                // Handle button click event
                console.log(`Button "${name}" clicked`);
                window.location.href = `categories/category.html?categoryId=${categoryId}`;
            });

            buttonsBox.appendChild(button);
        }

    } catch(error) {
        console.error("Error:", error);
    }
}
document.addEventListener("DOMContentLoaded", () => {
    displayCategories();
});


