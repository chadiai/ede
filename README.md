# Enterprise Development Experience project
## Thema Beschrijving
Het gekozen thema van deze microservices-architectuur is een eenvoudig berichten- en afsprakensysteem. 
Het systeem maakt gebruik van verschillende microservices om de functionaliteit te bieden aan de gebruikers.
In mijn Spring Boot-gebaseerde microservice-applicatie heb ik gebruik gemaakt van de krachtige mogelijkheden van Spring Cloud om een schaalbare, veerkrachtige en veilige architectuur te creëren. Elke microservice is ontworpen om één specifieke functionaliteit te bieden, wat resulteert in een modulaire, onderhoudbare en veilige codebase. Door middel van de Service Registry (Eureka) kunnen microservices zich dynamisch registreren en ontdekken, wat een flexibele en gedistribueerde systeemintegratie mogelijk maakt. Ik maak ook gebruik van een API Gateway die inkomende verzoeken routeren naar de juiste microservice, wat een uniforme, gecentraliseerde en beveiligde toegang tot de applicatie biedt. Met de geïntegreerde beveiliging en geavanceerde route filtering van Spring Security kunnen alleen geautoriseerde gebruikers toegang krijgen tot specifieke endpoints, waarbij strikte controlemechanismen zorgen dat de identiteit van de ingelogde gebruiker overeenkomt met de verzender van een bericht. Deze gelaagde beveiliging en toegangscontrole, samen met de asynchrone communicatie via Kafka voor het verwerken van events en notificaties, waarborgen de integriteit, veiligheid en efficiëntie van datastromen binnen de applicatie. Kortom, mijn microservice-applicatie in Spring Boot combineert de flexibiliteit, modulariteit en robuustheid van microservices met geavanceerde beveiligingsmechanismen en systeemintegratie, waardoor een moderne, veilige en toekomstbestendige applicatie-architectuur wordt gerealiseerd die voldoet aan de moderne eisen van privacy en gegevensbescherming.

![Systeemoverzicht](./images/schema.png)


#### Checklist

- ALGEMENE EISEN & DOCUMENTATIE✅ 
- Maak en gebruik je eigen Auth service i.p.v. GCP OAuth2✅ 
- Maak de interactie met minstens 1 service event-driven✅ 
- Gebruik Kafka i.p.v. ActiveMQ (dit heeft twee pods nodig)✅

## Microservices en Componenten
- **API Gateway**: Fungeert als de toegangspoort voor alle inkomende verzoeken en routeert deze naar de juiste microservice met route-filtering (authenticatie uitbreiding).
- **Appointment Service**: Beheert alle functies met betrekking tot afspraken.
- **Message Service**: Beheert alle berichten gerelateerde functionaliteiten.
- **User Service (Custom Auth)**: Beheert gebruikersgegevens en authenticatie.
- **Notification Service (Kafka Async Event Listener)**: Luistert naar events om asynchrone notificaties te verzenden.
- **Service Registry (Eureka Spring)**: Registreert alle beschikbare microservices.

## API Endpoints
### Gebruikersgerelateerde Endpoints (CUSTOM AUTH)
- **POST** `/register`: Registreer een nieuwe gebruiker.
- **POST** `/auth/login`: Authenticeer en log in een gebruiker.

### Berichten Endpoints
- **GET** `/messages/m/{messageId}`: Haal een specifiek bericht op (alleen deelnemers).
- **POST** `/messages/{userId}/send`: Verzend een bericht (alleen eigen chats).
- **GET** `/messages/all`: Haal alle berichten op (alleen voor beheerders).

### Afspraken Endpoints
- **POST** `/appointment/create`: Maak een nieuwe afspraak.
- **GET** `/appointment/{userId}/all`: Haal alle afspraken van een gebruiker op.
- **PUT** `/appointment/{appointmentId}/edit`: Bewerk een specifieke afspraak.
- **DEL** `/appointment/{appointmentId}/delete`: Verwijder een specifieke afspraak.
- **GET** `/appointment/all`: Haal alle afspraken op (alleen voor beheerders).

## Event-driven met Kafka (notification-service)
- **KafkaListener** `/messages/{userId}/send`: De notification-service logt het userId met een nieuwe bericht.
  ![](./images/notification_service.png)

## API Endpoints POSTMAN
### Gebruikersgerelateerde Endpoints
- **POST** `/register`: Registreer een nieuwe gebruiker.

  ![](./images/register.png)
- **POST** `/auth/login`: Authenticeer en log in een gebruiker.

  ![](./images/login.png)

### Berichten Endpoints
- **GET** `/messages/m/{messageId}`: Haal een specifiek bericht op (alleen deelnemers).

  <span style="color: red;">**UNAUTHORIZED**</span>

  ![](./images/message_unauth.png)

  <span style="color: green;">**AUTHORIZED**</span>

  ![](./images/message_auth.png)
- **POST** `/messages/{userId}/send`: Verzend een bericht (alleen eigen chats).

  <span style="color: red;">**UNAUTHORIZED**</span>

  ![](./images/send_message_unauth.png)

  <span style="color: green;">**AUTHORIZED**</span>

  ![](./images/send_message_auth.png)
- **GET** `/messages/all`: Haal alle berichten op (alleen voor admins).

   **ADMIN**

   ![](./images/all_messages.png)
### Afspraken Endpoints
- **POST** `/appointment/create`: Maak een nieuwe afspraak.

  ![](./images/create_appointment.png)
- **GET** `/appointment/{userId}/all`: Haal alle afspraken van een gebruiker op.

  <span style="color: red;">**UNAUTHORIZED </span> - wrong user logged in**

  ![](./images/all_userapp.png)

  <span style="color: green;">**AUTHORIZED**</span>

  ![](./images/all_usersapp_auth.png)
- **PUT** `/appointment/{appointmentId}/edit`: Bewerk een specifieke afspraak.

  ![](./images/edit_appointment.png)
- **DEL** `/appointment/{appointmentId}/delete`: Verwijder een specifieke afspraak.

  <span style="color: red;">**UNAUTHORIZED**</span>

  ![](./images/delete_unauth.png)

  <span style="color: green;">**AUTHORIZED**</span>

  ![](./images/delete_auth.png)
- **GET** `/appointment/all`: Haal alle afspraken op (alleen voor admins).

  ![](./images/all_appointments_after.png)

### Beheerders Endpoints
- **PUT** `/user/{userId}`: Bewerk een specifieke gebruiker (alleen voor admins).

  <span style="color: red;">**UNAUTHORIZED**</span>

  ![](./images/edit_user_unauth.png)

  <span style="color: green;">**AUTHORIZED**</span>

  ![](./images/edit_user_auth.png)

- **GET** `/user/all`: Haal alle gebruikers op (alleen voor admins).

  ![](./images/all_users.png)
