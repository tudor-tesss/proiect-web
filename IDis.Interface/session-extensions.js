function deleteSessionTokenCookie() {
    document.cookie = "sessionToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

    checkSession();
}