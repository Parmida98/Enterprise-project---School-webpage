package com.parmida98.school_webpage.user.authority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import static com.parmida98.school_webpage.user.authority.UserPermission.*;
import java.util.*;

/* UserRole: Handles Authorities
 *   Should contain both ROLE + PERMISSIONS
 *   Should contain a way to return SimpleGrantedAuthority (Spring Class)
 *   import static - removes the class requirement for Variables (no more dots)
 *       NOTE: The class still exists, but is not necessary to call
 * */

public enum UserRole {

    STUDENT(
            UserRoleName.STUDENT.getRoleName(),
            Set.of(
                    SEE_SCHEDULE,
                    SUBMIT_ASSIGNMENT
            )
    ),

    ADMIN(
            UserRoleName.ADMIN.getRoleName(),
            Set.of(
                    REGISTER_STUDENT,
                    GRADE_ASSIGNMENT,
                    DELETE_USER
            )
    );

    private final String roleName;
    private final Set<UserPermission> userPermissions;

    UserRole(String roleName, Set<UserPermission> userPermissions) {
        this.roleName = roleName;
        this.userPermissions = userPermissions;
    }

    public String getRoleName() {
        return roleName;
    }

    public Set<UserPermission> getUserPermissions() {
        return userPermissions;
    }

    // Get a LIST that Spring understands - containing both ROLE + PERMISSION
    public List<SimpleGrantedAuthority> getUserAuthorities() {

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        // this == the choice made after UserRole. (e.g: UserRole.ADMIN)
        authorityList.add(new SimpleGrantedAuthority(this.roleName));
        authorityList.addAll(
                this.userPermissions.stream().map(
                        userPermission -> new SimpleGrantedAuthority(userPermission.getUserPermission())
                ).toList()
        );

        return authorityList;
    }

}
