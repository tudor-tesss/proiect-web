export class Errors {
    static errorMessages = {
        "User.Name.NullOrEmpty": "Name is required.",
        "User.FirstName.NullOrEmpty": "First name is required.",
        "User.EmailAddress.NullOrEmpty": "Email address is required.",
        "UserGate.Pass.UserGateExpired": "The code has expired.",
        "UserGate.Pass.InvalidCode": "The code is invalid.",
        "UserGate.Pass.UserGateAlreadyPassed": "The code has already been used.",
        "UserSession.IpAddress.NullOrEmpty": "Something went wrong. Please try again.",
        "Category.Name.NullOrEmpty": "Name is required.",
        "Category.RatingField.NullOrEmpty": "Rating field is required.",
        "Post.Title.NullOrEmpty": "Title is required.",
        "Post.Body.NullOrEmpty": "Post description is required.",
        "Post.Rating.OutOfInterval": "The rating must be between 1 and 5.",
        "PostReply.Title.NullOrEmpty": "Title is required.",
        "PostReply.Body.NullOrEmpty": "Post description is required.",
        "PostReply.Rating.OutOfInterval": "The rating must be between 1 and 5.",

        "User.AlreadyExists": "Email address is already in use.",
        "User.UserDoesNotExist": "No user found matching the given email address.",
        "UserGate.Create.UserDoesNotExist": "No user found matching the given email address.",
        "UserGate.Pass.UserGateDoesNotExist": "No user gate found for user.",
        "UserSession.Create.UserDoesNotExist": "User does not exist.",
        "UserSession.Check.UserSessionDoesNotExist": "User session does not exist.",
        "UserSession.Check.UserSessionExpired": "User session has expired.",
        "UserSession.Check.UserSessionIpAddressDoesNotMatch": "User session IP address does not match.",
        "UserSession.Check.UserSessionUserIdDoesNotMatch": "User session user ID does not match.",
        "Category.AlreadyExists": "Category already exists.",
        "Category.NoCategoriesInDatabase": "No categories found in database.",
        "Category.User.DoesntHaveCategories": "User doesn't have any categories.",
        "Post.User.DoesNotExist": "User does not exist.",
        "Post.Category.DoesNotExist": "Category does not exist.",
        "Post.Ratings.DoesNotMatch": "Post ratings do not match.",
        "Post.Category.HasNoPosts": "Category has no posts.",
        "Post.NotFound": "Post not found.",
        "Post.CreatorHasNoPosts": "Creator has no posts.",
        "Post.NoPostsAvailable": "No posts available.",
        "CategoryStatistics.Category.DoesNotExist": "Category does not exist.",
        "PostStatistics.Post.DoesNotExist": "Post does not exist.",
        "PostReply.User.DoesNotExist": "User does not exist.",
        "PostReply.Post.DoesNotExist": "Post does not exist.",
        "PostReply.Ratings.DoNotMatch": "Post reply ratings do not match.",
        "PostReply.NoRepliesFound": "No replies found.",
        "Category.NotFound": "Category not found."
    };

    static displayError(errorMessage, bypass = false) {
        let message = "";

        if (!bypass) {
            if (!this.errorMessages[errorMessage]) {
                message = "An error occurred.";
            } else {
                message = this.errorMessages[errorMessage];
            }
        } else {
            message = errorMessage;
        }

        const errorContainer = document.getElementById('error-container');
        errorContainer.textContent = message;
        errorContainer.style.display = 'block';

        setTimeout(() => {
            errorContainer.style.display = 'none';
        }, 5000);
    } 
}