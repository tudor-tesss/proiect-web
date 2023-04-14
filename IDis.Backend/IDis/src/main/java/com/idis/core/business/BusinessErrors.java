package com.idis.core.business;

public class BusinessErrors {
    public static class User {
        public static final String UserAlreadyExists = "User.AlreadyExists";
    }

    public static class UserGate {
        public static class Create {
            public static final String UserDoesNotExist = "UserGate.Create.UserDoesNotExist";
        }

        public static class Pass {
            public static final String UserGateDoesNotExist = "UserGate.Pass.UserGateDoesNotExist";
        }
    }
}
