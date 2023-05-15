package com.idis.core.domain;

public class DomainErrors {
    public static class User {
        public static final String NameNullOrEmpty = "User.Name.NullOrEmpty";
        public static final String FirstNameNullOrEmpty = "User.FirstName.NullOrEmpty";
        public static final String EmailAddressNullOrEmpty = "User.EmailAddress.NullOrEmpty";
    }

    public static class UserGate {
        public static class Pass {
            public static final String UserGateExpired = "UserGate.Pass.UserGateExpired";
            public static final String InvalidCode = "UserGate.Pass.InvalidCode";
            public static final String UserGateAlreadyPassed = "UserGate.Pass.UserGateAlreadyPassed";
        }
    }

    public static class UserSession {
        public static final String IpAddressNullOrEmpty = "UserSession.IpAddress.NullOrEmpty";
    }

    public static class Category{
        public static final String NameNullOrEmpty = "Category.Name.NullOrEmpty";
        public static final String RatingFieldNullOrEmpty = "Category.RatingField.NullOrEmpty";
    }

}
