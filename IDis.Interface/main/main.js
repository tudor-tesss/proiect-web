import { AuthenticationService, CategoriesService, DarkmodeService } from "../@shared/index.js";

window.AuthenticationService = AuthenticationService;
await AuthenticationService.checkSession();

export class CategoriesOverviewComponent {
    static async getAllCategories() {
        let names = [];
        let categoryIds = [];

        await CategoriesService
            .getAll()
            .then((data) => {
                names = data.map((item) => item.name);
                categoryIds = data.map((item) => item.id);
            })
            .catch((error) => {
                const buttonsWrapper = document.querySelector(".buttons-wrapper");

                buttonsWrapper.innerHTML = `
                    <div class="buttons-box">
                        <div class="error-container" id="error-container"></div>
                    </div>
                `;

                let errorMessage = error.message;

                if (errorMessages[errorMessage] === undefined) {
                    errorMessage = "An error occurred getting the categories.Please try again later!";
                    displayError(errorMessage);

                } else {
                    displayError(errorMessages[errorMessage]);
                }
            });

        return [names, categoryIds];
    }

    static async displayError(errorMessage) {
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

    static async displayCategories() {
        const buttonsWrapper = document.querySelector(".buttons-wrapper");
        const buttonsBox = document.querySelector(".buttons-box");
        // Clear any existing buttons
        buttonsBox.innerHTML = "";

        try {
            const [names, categoryIds] = await this.getAllCategories();

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
                    window.location.href = `/categories/?categoryId=${categoryId}`;
                });

                buttonsBox.appendChild(button);
            }
        } catch (error) {
            console.error("Error:", error);
        }
    }
}

const toggleButton = document.getElementById('toggle-button');
const toggleImage = document.getElementById('toggle-image');

function updateImageSource(isDarkMode) {
    const imageSource = isDarkMode
        ? '../resources/icons/dark.png'
        : '../resources/icons/light.png';
    toggleImage.src = imageSource;
}

function handleToggleDarkMode() {
    const isDarkMode = DarkmodeService.toggleDarkMode();
    updateImageSource(isDarkMode);

    // Save the selected mode to local storage
    localStorage.setItem('darkMode', isDarkMode.toString());
}

toggleButton.addEventListener('click', handleToggleDarkMode);

// Check if user preference is stored in local storage
const storedDarkMode = localStorage.getItem('darkMode');
const isDarkMode = storedDarkMode === 'true' ? true : storedDarkMode === 'false' ? false : DarkmodeService.isDarkMode();
updateImageSource(isDarkMode);
// Set the initial mode based on stored value
DarkmodeService.setTheme(isDarkMode ? 'dark' : 'light');

window.CategoriesOverviewComponent = CategoriesOverviewComponent;
await CategoriesOverviewComponent.displayCategories();