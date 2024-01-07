# Project Name

## NoteShare: Collaborative Note-Taking and Sharing Platform

### Brief description of the project.

#### Technical Requirements : Framework and Database

This project is implemented using the Spring Boot framework for building RESTful APIs. We have chosen PostgreSQL as the database to store data.

#### Authentication and Rate Limiting

Authentication is implemented using Spring boot JWT Authentication. A token is generated as soon as a user execute login api which can be used to access other apis. The token gets expired after a particular time.

## Rate Limiter and Request throttling

### Implementation Overview
The RateLimiter class utilizes a ConcurrentHashMap to store request counts for each client. It ensures thread safety during concurrent access.

### Configuration Parameters
MAX_REQUESTS: Defines the maximum allowed requests per time window. 
#### Set to 10 Request for this project.

TIME_WINDOW_SECONDS: Specifies the duration of the time window in seconds.
#### Set to 60 seconds for this project.

### Initialization:

The requestCounts map stores the last request time for each client and their corresponding request count.
Request Handling:

On each incoming request, the system checks the elapsed time since the last request from the client.
If the time elapsed is within the current time window, it increments the request count.
If the request count exceeds the maximum allowed requests, the system rejects the request with a false response.
Time Window Reset:

If the elapsed time exceeds the defined time window, the system resets the request count and updates the last request time.

### Client Identification
The getClientKey method generates a unique key for each client based on their identifier.

### Usage
The allowRequest method is the entry point for handling incoming requests. It returns true if the request is allowed, and false if the client has exceeded the maximum allowed requests within the specified time window.

## Search Functionality
For high-performance search functionality, text indexing has been implemented using ts_vector function of Postgre SQL to ensure fast and efficient notes search as per keyword/query provided.

# API Endpoints
## Authentication Endpoints
POST /api/auth/signup: Create a new user account.

POST /api/auth/login: Log in to an existing user account and receive an access token.

## Note Endpoints

GET /api/notes: Get a list of all notes for the authenticated user.

GET /api/notes/{id}: Get a note by ID for the authenticated user.

POST /api/notes: Create a new note for the authenticated user.

PUT /api/notes/{id}: Update an existing note by ID for the 
authenticated user.

DELETE /api/notes/{id}: Delete a note by ID for the authenticated user.

POST /api/notes/{id}/share: Share a note with another user for the authenticated user.

GET /api/search?q={query}: Search for notes based on keywords for the authenticated user.

# Instructions on how to run the code and execute tests.

## Requirements
   - Java 17 or higher
   - Maven
   - Postgre SQL
   - Postman

### application.properties 

spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=root
spring.datasource.driver-class-name =org.postgresql.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true

logging.level.org.springframework.security=DEBUG

jwt.secret=
afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf


2. **Steps to Run:**
   - Clone the repository: `git clone <Your GitHub Repository URL>`
   - Navigate to the project directory: `cd <project-directory>`
   - Run the application: `./mvnw spring-boot:run`
   - Access the API at [http://localhost:8080](http://localhost:8080)

# API Reference

## create a new user account.

```http
  POST /api/auth/signup
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `name` | `string` | **Required**. username minimum character 4 |
| `email` | `string` | **Required**. user email address |
| `password` | `string` | **Required**. password character between 3 and 10 |
| `about` | `string` | **Required**. description about user |

### Sample Request :
{
    "name":"sample_name",
    "email":"sample@gmail.com",
    "password":"abcdefg",
    "about":"Software Test Engineer"
}

### Sample Response : 
{
    "id": 11,
    "name": "sample_name",
    "email": "sample@gmail.com",
    "password": "$2a$10$zkzMRMhehddqlqA6UXkeouY/BseaIuYjTr1MgwXH9cDwDW8L0SUsS",
    "about": "Software Test Engineer",
    "roles": [
        {
            "id": 502,
            "name": "NORMAL_USER"
        }
    ]
}

## log in to an existing user account and receive an access token.

```http
  POST /api/auth/login
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `userId`      | `String` | **Required**. Id of user |
| `password`      | `String` | **Required**. password of user |

### Sample Request :
{
    "userId" : "3",
    "password" : "abcde"
}

### Sample Response : 
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiaWF0IjoxNzA0NjI2ODIyLCJleHAiOjE3MDQ2NDQ4MjJ9.VcHxvu-NV4oyEyrJkxrqf4yP-Zt8Mss6_6PWqmxPsCA",
    "userDto": {
        "id": 3,
        "name": "SampleUser",
        "email": "sample@gmail.com",
        "password": "$2a$10$fXkzPZLA9DVPnYhyJkvom.ZzLKklWn4pAU.HMaW21/GIYrktRCkTq",
        "about": "Sample about content",
        "roles": [
            {
                "id": 502,
                "name": "NORMAL_USER"
            }
        ]
    }
}

### By default all users will be signed up as normal users in the system.
### To create admin user Request Param "admin" can be passed as mentioned below. Admin user can perform User CRUD operations.
```http
  POST /api/auth/login?role=admin
```

## get a list of all notes for the authenticated user.

```http
  GET /api/notes
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `Authorization` | `string` | **Required**. Request Header |

### Sample Request :

#### Header "Authorization" is passed as RequestHeader for Authentication

### Sample Response : 
{
    "content": [
        {
            "notesId": 1,
            "userId": 3,
            "title": "Notes1-Java",
            "content": "Java is a programming language used to write code which can be understood by machine.",
            "addedDate": "2024-01-07T11:39:11.614+00:00"
        },
        {
            "notesId": 2,
            "userId": 3,
            "title": "Notes2-Python",
            "content": "Python is a programming language used to write code which can be understood by machine.",
            "addedDate": "2024-01-07T11:39:56.254+00:00"
        }
    ]
}

## get a note by ID for the authenticated user.

```http
  GET /api/notes/{id} 
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `Authorization` | `string` | **Required**. Request Header |
| `notesId` | `Integer` | **Required**. Request Param |

### Sample Request :

#### notesId "id" is passed as RequestParam in URL.
#### Header "Authorization" is passed as RequestHeader for Authentication.

### Sample Response : 
{
    "notesId": 1,
    "userId": 3,
    "title": "Notes1-Java",
    "content": "java is a programming language used to write code which can be understood by machine.",
    "addedDate": 1704293862752
}

## create a new note for the authenticated user.

```http
  POST /api/notes
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `userId` | `Integer` | **Required**. userId to create note for that user |
| `title` | `string` | **Required**. title of note minimum 4 characters|
| `content` | `string` | **Required**. content of note minimum 10 characters |
| `Authorization` | `string` | **Required**. Request Header |

### Sample Request :

#### Header "Authorization" is passed as RequestHeader for Authentication.

{
    "userId":2,
    "title":"Notes6-C#",
    "content":"C# is a programming language used to write code which can be understood by machine."
}

### Sample Response : 
{
    "notesId": 7,
    "userId": 2,
    "title": "Notes6-C#",
    "content": "C# is a programming language used to write code which can be understood by machine.",
    "addedDate": 1704445889013
}

## update an existing note by ID for the authenticated user.

```http
  PUT /api/notes
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `notesId` | `Integer` | **Required**. notesId to update that note |
| `title` | `string` | **Required**. to update title of note |
| `content` | `string` | **Required**. to update content of note |
| `Authorization` | `string` | **Required**. Request Header |

### Sample Request :

#### Header "Authorization" is passed as RequestHeader for Authentication.
{
    "notesId":7,
    "title":"Notes6-C#-updated",
    "content":"C# is a programming language used to write code which can be understood by machine."
}

### Sample Response : 
{
    "notesId": 7,
    "userId": 2,
    "title": "Notes6-C#-updated",
    "content": "C# is a programming language used to write code which can be understood by machine.",
    "addedDate": 1704445889013
}

## delete a note by ID for the authenticated user.

```http
  DELETE /api/notes/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `Authorization` | `string` | **Required**. Request Header |
| `notesId` | `Integer` | **Required**. Request Param |

### Sample Request :

#### notesId "id" is passed as RequestParam in URL.
#### Header "Authorization" is passed as RequestHeader for Authentication.

### Sample Response : 
{
    "message": "Notes deleted Successfully",
    "statusCode": true
}

## share a note with another user for the authenticated user.

```http
  POST /api/notes/{id}/share
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `Authorization` | `string` | **Required**. Request Header |
| `userId` | `Integer` | **Required**. Request Param userId of user to share notes |
| `userId` | `string` | **Required**. userId of sharing user |
| `title` | `string` | **Required**. title of note |
| `content` | `string` | **Required**. content of note |

### Sample Request :

#### userId "id" is passed as RequestParam in URL.
#### Header "Authorization" is passed as RequestHeader for Authentication.

{
    "userId":11,
    "title":"Notes5-C-share",
    "content":"C is a programming language used to write code which can be understood by machine."
}

### Sample Response : 
{
    "message": "Notes shared successfully with the user : 1",
    "status": "0",
    "notesDto": {
        "notesId": 8,
        "userId": 1,
        "title": "Notes5-C-share",
        "content": "C is a programming language used to write code which can be understood by machine.",
        "addedDate": 1704447655040
    }
}

## search for notes based on keywords for the authenticated user.


```http
  GET /api/search?q=:query
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `Authorization` | `string` | **Required**. Request Header |
| `q` | `String` | **Required**. Params to pass search keyword |

### Sample Request :

#### keyword "q" is passed as RequestParam in URL. -> 'java'
#### Header "Authorization" is passed as RequestHeader for Authentication.

### Sample Response : 
[
    {
        "notesId": 1,
        "userId": 1,
        "title": "Notes1-Java",
        "content": "java is a programming language used to write code which can be understood by machine.",
        "addedDate": 1704293862752
    }
]


