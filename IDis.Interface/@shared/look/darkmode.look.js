export class DarkmodeLook {
    static setTheme(theme) {
        const body = document.querySelector('body');
        body.setAttribute('data-theme', theme);
        localStorage.setItem('theme', theme);
    }

    static isDarkMode() {
        const currentTheme = document.querySelector('body').getAttribute('data-theme');
        return currentTheme === 'dark';
    }

    static toggleDarkMode() {
        const body = document.querySelector('body');
        const currentTheme = body.getAttribute('data-theme');
        const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
        DarkmodeLook.setTheme(newTheme);
        return newTheme === 'dark';
    }

    static updateImageSource(isDarkMode) {
        const toggleImage = document.getElementById('toggle-image');
        const imageSource = isDarkMode
            ? '../../resources/icons/dark.png'
            : '../../resources/icons/light.png';
        toggleImage.src = imageSource;
        return imageSource;
    }
    
    static handleToggleDarkMode() {
        const isDarkMode = DarkmodeLook.toggleDarkMode();
        DarkmodeLook.updateImageSource(isDarkMode);
    
        // Save the selected mode to local storage
        localStorage.setItem('darkMode', isDarkMode.toString());
    }
}