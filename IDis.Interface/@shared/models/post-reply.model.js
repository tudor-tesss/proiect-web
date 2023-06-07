export class PostReply {
    constructor(id, authorID, parentPostId, title, body, ratings, createdAt) {
        this.id = id;
        this.authorID = authorID;
        this.parentPostId = parentPostId;
        this.title = title;
        this.body = body;
        this.ratings = ratings;
        this.createdAt = createdAt;
    }

    static fromJson(json) {
        return Object.assign(new PostReply(), json);
    }
}