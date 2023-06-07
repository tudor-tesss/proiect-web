export class Post {
    constructor(id, authorId, categoryId, title, body, ratings, createdAt) {
        this.id = id;
        this.authorId = authorId;
        this.categoryId = categoryId;
        this.title = title;
        this.body = body;
        this.ratings = ratings;
        this.createdAt = createdAt;
    }

    static fromJson(json) {
        return Object.assign(new Post(), json);
    }
}