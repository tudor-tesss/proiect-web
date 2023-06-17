export class Routes {
    static isLocalhost = false;
    static urlPrefix = this.isLocalhost ? "http://localhost:7101" : "https://p01--backend--ylsyc29qft6l.code.run";

    static authentication = {
        checkSession: this.urlPrefix + "/users/{id}/sessions",
        deleteSession: this.urlPrefix + "/users/{id}/sessions",
        createUserGate: this.urlPrefix + "/users/gates",
        passUserGate: this.urlPrefix + "/users/{id}/gates",
        createSession: this.urlPrefix + "/users/{id}/sessions"
    };

    static categories = {
        createCategory: this.urlPrefix + "/categories",
        getAll: this.urlPrefix + "/categories",
        getCategoriesByCreatorId: this.urlPrefix + "/users/{id}/categories"
    };

    static posts = {
        createPost: this.urlPrefix + "/posts",
        createPostReply: this.urlPrefix + "/posts/{id}/replies",
        getAll: this.urlPrefix + "/posts",
        getAllPostsInCategory: this.urlPrefix + "/categories/{id}/posts",
        getAllPostsByCreatorId: this.urlPrefix + "/users/{id}/posts",
        getPostById: this.urlPrefix + "/posts/{id}",
        getPostReplies: this.urlPrefix + "/posts/{id}/replies"
    };

    static statistics = {
        getCategoryStatistics: this.urlPrefix + "/categories/{id}/statistics",
        getCategoriesStatistics: this.urlPrefix + "/categories/statistics",
        getPostStatistics: this.urlPrefix + "/posts/{id}/statistics",
        getPostsStatistics: this.urlPrefix + "/posts/statistics",
        getPdfForPostStatistics: this.urlPrefix + "/posts/{id}/statistics/pdf",
        getPdfForCategoryStatistics: this.urlPrefix + "/categories/{id}/statistics/pdf",
        getDocbookForPostStatistics: this.urlPrefix + "/posts/{id}/statistics/docbook",
        getDocbookForCategoryStatistics: this.urlPrefix + "/categories/{id}/statistics/docbook",
        getCsvForPostStatistics: this.urlPrefix + "/posts/{id}/statistics/csv",
        getCsvForCategoryStatistics: this.urlPrefix + "/categories/{id}/statistics/csv",
    };

    static users = {
        getUserById: this.urlPrefix + "/users/{id}",
        createUser: this.urlPrefix + "/users",
        getAllUserNames: this.urlPrefix + "/users/all/names",
    };
}