package com.parmida98.school_webpage.user.authority;


/* Handles the ROLE_ concatenation
 *   This ENUM is essentially just a value holder
 * */

/*
Denna enum:
✔ Centraliserar rollnamn
✔ Säkerställer korrekt ROLE_-prefix
✔ Undviker magiska strängar
✔ Gör kod mer konsekvent och lättare att ändra
 */

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

