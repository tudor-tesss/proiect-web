import {StatisticsService} from "../@shared/services/statistics.service.js";

async function getPost(postId) {
    return await fetch(`http://localhost:7101/posts/${postId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
    .then(async response => {
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error);
        }

        return await response.json();
    });
}
async function savePdf() {

    const urlParams = new URLSearchParams(window.location.search);
    const postId = urlParams.get('postId');

    const base64String = await StatisticsService
        .getPdfForPostStats(postId)
        .catch((error) => {
            console.log(error);
        });

    const byteCharacters = atob(base64String);
    const byteArrays = [];
    for (let offset = 0; offset < byteCharacters.length; offset += 512) {
        const slice = byteCharacters.slice(offset, offset + 512);
        const byteNumbers = new Array(slice.length);
        for (let i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
    }
    const blob = new Blob(byteArrays, {type: 'application/pdf'});

    const downloadLink = document.createElement('a');
    downloadLink.href = URL.createObjectURL(blob);
    downloadLink.download = 'poststats.pdf';
    downloadLink.click();
}

savePdf();