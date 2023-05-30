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