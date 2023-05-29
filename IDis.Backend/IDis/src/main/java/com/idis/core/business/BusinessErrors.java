package com.idis.core.business;

public class BusinessErrors {
    public static class User {
        public static final String UserAlreadyExists = "User.AlreadyExists";
        public static final String UserDoesNotExist = "User.UserDoesNotExist";
    }

    public static class UserGate {
        public static class Create {
            public static final String UserDoesNotExist = "UserGate.Create.UserDoesNotExist";
        }

        public static class Pass {
            public static final String UserGateDoesNotExist = "UserGate.Pass.UserGateDoesNotExist";
        }
    }

    public static class UserSession {
        public static class Create {
            public static final String UserDoesNotExist = "UserSession.Create.UserDoesNotExist";
        }

        public static class Check {
            public static final String UserSessionDoesNotExist = "UserSession.Check.UserSessionDoesNotExist";
            public static final String UserSessionExpired = "UserSession.Check.UserSessionExpired";
            public static final String UserSessionIpAddressDoesNotMatch = "UserSession.Check.UserSessionIpAddressDoesNotMatch";
            public static final String UserSessionUserIdDoesNotMatch = "UserSession.Check.UserSessionUserIdDoesNotMatch";
        }
    }

    public static class Category {
        public static final String CategoryAlreadyExists = "Category.AlreadyExists";
        public static final String NoCategoriesInDatabase = "Category.NoCategoriesInDatabase";
    }

    public static class Post {
        public static final String UserDoesNotExist = "Post.User.DoesNotExist";
        public static final String CategoryDoesNotExist = "Post.Category.DoesNotExist";
        public static final String RatingsDoNotMatch = "Post.Ratings.DoesNotMatch";
    }

    public static class CategoryStatistics {
        public static final String CategoryDoesNotExist = "CategoryStatistics.Category.DoesNotExist";
    }

    public static class PostReply {
        public static final String UserDoesNotExist = "PostReply.User.DoesNotExist";
        public static final String PostDoesNotExist = "PostReply.Post.DoesNotExist";
        public static final String RatingsDoNotMatch = "PostReply.Ratings.DoNotMatch";
    }
}
