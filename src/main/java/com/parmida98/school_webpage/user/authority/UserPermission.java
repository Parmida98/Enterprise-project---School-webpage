package com.parmida98.school_webpage.user.authority;

/* UserPermission - is a simple String that will be handled by Spring Security
 *   If you want to add a new Permission, simply follow domain logic e.g:
 *       TODO_READ, TODO_DELETE, USER_READ, USER_DELETE etc...
 * */

// Den skapar en kontrollerad lista över tillåtna rättigheter som kan användas i Spring Security, t.ex.:
//Istället för att använda hårdkodade strängar överallt, använder du enums -> säkrare och mer självdokumenterande kod.

public enum UserPermission {

    SEE_SCHEDULE("SEE_SCHEDULE"),
    SUBMIT_ASSIGMENT("SUBMIT_ASSIGNMENT"),
    GRADE_ASSIGMENT("GRADE_ASSIGNMENT"),
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

