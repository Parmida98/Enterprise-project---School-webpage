package com.parmida98.school_webpage.user.authority;


/* Handles the ROLE_ concatenation
 *   This ENUM is essentially just a value holder
 * */

public enum UserRoleName {

    STUDENT("ROLE_STUDENT"),
    ADMIN("ROLE_ADMIN");

    private final String roleName;

    UserRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}

