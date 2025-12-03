package com.parmida98.school_webpage.user.authority;

/* UserPermission - is a simple String that will be handled by Spring Security
 *   If you want to add a new Permission, simply follow domain logic e.g:
 *       TODO_READ, TODO_DELETE, USER_READ, USER_DELETE etc...
 * */

public enum UserPermission {

    SEE_SCHEDULE("SEE_SCHEDULE"),
    SUBMIT_ASSIGNMENT("SUBMIT_ASSIGNMENT"),
    GRADE_ASSIGNMENT("GRADE_ASSIGNMENT"),
    REGISTER_STUDENT("REGISTER_STUDENT"),
    DELETE_USER("DELETE_USER");

    private final String userPermission;

    UserPermission(String userPermission) {
        this.userPermission = userPermission;
    }

    public String getUserPermission() {
        return userPermission;
    }
}

