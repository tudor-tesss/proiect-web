export class PopupService{
    static showPopup() {
        const popup = document.getElementById('popup');
        popup.style.display = 'block';
    }
      
    static hidePopup() {
        const popup = document.getElementById('popup');
        popup.style.display = 'none';
    }
}