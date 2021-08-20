package com.yomna.salaries.util;


import com.yomna.salaries.model.User;

public class AuthorizationUtil {
    private static User currentAuthorizedUser;

    public static User getCurrentAuthorizedUser() {
        return currentAuthorizedUser;
    }

    public static void setCurrentAuthorizedUser(User currentAuthorizedUser) {
        AuthorizationUtil.currentAuthorizedUser = currentAuthorizedUser;
    }
}
