# School Webpage – Spring Security

Detta README beskriver alla viktiga delar av projektet: endpoints, roller, autentisering, registrering, JWT, cookies samt RabbitMQ-flödet.

---

## **API Endpoints**

### **1. POST /register**

**Syfte:** Registrerar en ny användare av själva studenten.

**Body (JSON):**

* `username`
* `password`
* `email`

**Roller som krävs:**

* Öppen endpoint (ingen autentisering krävs).

**Vad den gör:**

* Validerar input via DTO.
* Skapar en ny student med rollen **STUDENT**.
* Hashar lösenord.
* Sparar i databasen.
* Skickar RabbitMQ-meddelande → EmailConsumer.
* Returnerar ett DTO-objekt med användarnamn.

---

### **2. POST /login**

**Syfte:** Autentiserar användare.

**Body (JSON):**

* `username`
* `password`

**Roller som krävs:**

* Öppen endpoint.

**Vad den gör:**

* Autentiserar via Spring Security AuthenticationManager.
* Skickar MQ-meddelande om login.
* Genererar JWT baserat på CustomUser.
* Lägger JWT i en HTTP-only cookie.
* Returnerar token + roller + användarnamn.


---

## **Rollsystem (Roles & Permissions)**

Projektet använder **UserRole** Enum:

* `ADMIN`
* `STUDENT`

Rollerna används i SecurityConfig för att styra åtkomst till skyddade endpoints.
JWT-token innehåller rollerna så att Spring Security kan filtrera requests.

---

## **Hur Login fungerar (Autentisering)**

1. Klienten skickar `{ username, password }` till `/login`.
2. Spring Security försöker autentisera via:

    * UsernamePasswordAuthenticationToken
    * CustomUserDetailsService
3. Vid lyckad inloggning:

    * JWT genereras
    * HTTP-only cookie skapas: `authToken=`
    * Response innehåller: username, token, authorities.
4. RabbitMQ-meddelande skickas: "LOGIN|email".

Misslyckad inloggning → 401 Unauthorized.

---

## **Hur Registrering fungerar**

1. Klienten skickar valid JSON med username, password, email.
2. DTO valideras med:

    * @ValidUsername
    * @ValidPassword
    * @Email
3. Mapper skapar en CustomUser.
4. Lösenord hashas.
5. Rollen **STUDENT** sätts automatiskt.
6. Användaren sparas.
7. MQ-meddelande skickas: "REGISTER|email".
8. Response innehåller ett UserResponseDTO med endast användarnamn.

---

## **RabbitMQ – Microservice Funktionalitet**

Projektet implementerar en microtjänst via RabbitMQ.

### 🔹 EmailProducer (skickar meddelanden)

* Anropas vid registrering: `sendAccountCreatedEmail(email)`
* Anropas vid login: `sendLoginEmail(email)`
* Skickar format: `TYPE|email`
* TYPE är exempelvis `REGISTER` eller `LOGIN`.

### 🔹 EmailConsumer (tar emot meddelanden)

* Lyssnar på RabbitConfig.QUEUE_NAME.
* Tar emot strängmeddelanden.
* Delar upp dem i `TYPE` och `email`.
* Använder JavaMailSender för att skicka mail.
* Vid test i dev körs via MailHog.

### 🔹 RabbitConfig

* Skapar Queue, Exchange och Binding:

    * Queue: `email-queue`
    * Exchange: `email-exchange`
    * Routing key: `email.routing`

RabbitMQ används för att:

* Skicka login-meddelande
* Skicka registrerings-meddelande

---

## 🍪 **JWT & Cookies – Säkerhet**

* JWT lagras i en **HTTP-only cookie** (skyddar mot XSS).
* SameSite=Lax för CSRF-skydd.
* Token innehåller:

    * username
    * roller
    * issuedAt
    * expiration
* Token parseras och valideras av JwtUtils.
* AuthenticationFilter sätter SecurityContext.

---

## 🛡️ **Validering & Input-säkerhet**

Projektet använder:

* DTO-validering
* Egna annoteringar (@ValidUsername, @ValidPassword)
* GlobalExceptionHandler för fel
* Inga entiteter exponeras
* Ingen HTML-rendering → ingen XSS-risk

---