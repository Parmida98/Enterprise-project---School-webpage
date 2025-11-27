package com.parmida98.school_webpage.user.dto;

/*
Denna record:
✔ Tar emot inloggningsuppgifter
✔ Används som input till Spring Security
✔ Är immutabel
✔ Innehåller ingen affärslogik
✔ Är ett rent överföringsobjekt
 */
// TODO validation & Injection protection
public record CustomUserLoginDTO(
        String username,
        String password
) {
}

