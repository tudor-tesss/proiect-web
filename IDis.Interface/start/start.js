import { AuthenticationService, UsersService, DarkmodeLook } from "../@shared/index.js";

window.AuthenticationService = AuthenticationService;
await AuthenticationService.resumeSession();


export class StartComponent {
    static errorMessages = {
        "User.AlreadyExists": "Email address is already in use.",
        "User.Name.NullOrEmpty": "Name is required.",
        "User.FirstName.NullOrEmpty": "First name is required.",
        "User.EmailAddress.NullOrEmpty": "Email address is required.",
        "UserGate.Create.UserDoesNotExist": "No user found matching the given email address.",
        "UserGate.Pass.UserGateDoesNotExist": "No user gate found matching the given code.",
        "UserGate.Pass.UserGateExpired": "The code has expired.",
        "UserGate.Pass.InvalidCode": "The code is invalid.",
        "UserGate.Pass.UserGateAlreadyPassed": "The code has already been used."
    };

    static displayLoginForm() {
        const formWrapper = document.querySelector('.form-wrapper');
        document.addEventListener("keydown", function(event) {
            if (event.key === "Enter") {
                event.preventDefault();
                StartComponent.createUserGate();
            }
        });
        formWrapper.innerHTML = `
            <div class="login-box">
                <h2>Log In</h2>
                <form>
                    <div class="user-box">
                        <input type="text" name="email" id="email" required>
                        <label>Email</label>
                    </div>
                    <div id="error-container" class="error-container"></div>
                    <div class="submit-wrapper">
                        <button type="button" class="submit-b" onClick="StartComponent.createUserGate()">Log In</button>
                    </div>
                </form>
            </div>
        `;
    }

    static async createUserGate() {
        const emailAddress = document.getElementById('email').value;

        return await AuthenticationService
            .createUserGate(emailAddress)
            .then(data => {
                localStorage.setItem('userUuid', data);
                this.displayUserGateForm();
            })
            .catch(error => {
                console.error('Error:', error);

                let errorMessage = "";
                if (error.message === "UserGate.Create.UserDoesNotExist") {
                    errorMessage = this.errorMessages["UserGate.Create.UserDoesNotExist"];
                } else {
                    errorMessage = "An error occurred while creating the user gate.";
                }

                this.displayError(errorMessage);
            });
    }

    static displayUserGateForm() {
        const formWrapper = document.querySelector('.form-wrapper');
        document.addEventListener("keydown", function(event) {
            if (event.key === "Enter") {
                event.preventDefault();
                StartComponent.passUserGate();
            }
        });
        formWrapper.innerHTML = `
            <div class="login-box">
                <h2>Enter Code</h2>
                <form>
                    <div class="user-box">
                        <input type="text" name="code" id="code" required>
                        <label>Code</label>
                    </div>
                    <p class="center-text">Check your email for the code.</p>
                    <div id="error-container" class="error-container"></div>
                    <div class="submit-wrapper">
                        <button type="button" class="submit-b" onClick="StartComponent.passUserGate()">Submit</button>
                    </div>
                </form>
            </div>
        `;
    }

    static async passUserGate() {
        const userUuid = localStorage.getItem('userUuid');
        const code = document.getElementById('code').value;

        return await AuthenticationService
            .passUserGate(userUuid, code)
            .then(d => {
                AuthenticationService.setCookie('sessionId', d.sessionId, 1);
                AuthenticationService.setCookie('userId', userUuid, 1);
                window.location.href = '/main';
            })
            .catch(error => {
                let errorMessage = error.message;
                if (this.errorMessages[errorMessage] === undefined) {
                    errorMessage = "An error occurred while passing the user gate.";

                    window.location.href = '/start';
                }

                this.displayError(this.errorMessages[errorMessage]);
            });
    }

    static displayError(errorMessage) {
        const errorContainer = document.getElementById('error-container');
        errorContainer.textContent = errorMessage;
        errorContainer.style.display = 'block';

        setTimeout(() => {
            errorContainer.style.display = 'none';
        }, 5000);
    }

    static async createUser() {
        const name = document.getElementById('name').value;
        const firstName = document.getElementById('first-name').value;
        const email = document.getElementById('email').value;

        return await UsersService
            .createUser(name, firstName, email)
            .then(() => {
                this.displayLoginForm();
            })
            .catch(error => {
                let errorMessage = error.message;
                if (this.errorMessages[errorMessage] === undefined) {
                    errorMessage = "An error occurred while creating your account.";
                }

                this.displayError(this.errorMessages[errorMessage]);
            });
    }

    static displaySignUpForm() {
        const formWrapper = document.querySelector('.form-wrapper');
        document.addEventListener("keydown", function(event) {
            if (event.key === "Enter") {
                event.preventDefault();
                StartComponent.createUser();
            }
        });
        formWrapper.innerHTML = `
            <div class="login-box">
                <script src="start.js"></script>
                <h2>Sign Up</h2>
                <form>
                    <div class="user-box">
                        <input type="text" name="first-name" id="first-name" required>
                        <label>First Name</label>
                    </div>
                    <div class="user-box">
                        <input type="text" name="name" id="name" required>
                        <label>Name</label>
                    </div>
                    <div class="user-box">
                        <input type="text" name="email" id="email" required>
                        <label>Email</label>
                    </div>
                    <div id="error-container" class="error-container"></div>
                    <div class="submit-wrapper">
                        <button type="button" class="submit-b" onClick="StartComponent.createUser()">Submit</button>
                    </div>
                </form>
            </div>
        `;
    }

    static displayDarkMode(){
        const toggleButton = document.getElementById('toggle-button');
        toggleButton.addEventListener('click', DarkmodeLook.handleToggleDarkMode);

        const storedDarkMode = localStorage.getItem('darkMode');
        const isDarkMode = storedDarkMode === 'true' ? true : storedDarkMode === 'false' ? false : DarkmodeLook.isDarkMode();
        DarkmodeLook.updateImageSource(isDarkMode);
    
        DarkmodeLook.setTheme(isDarkMode ? 'dark' : 'light');
    }

}

window.StartComponent = StartComponent;
StartComponent.displayDarkMode();
StartComponent.displaySignUpForm();
