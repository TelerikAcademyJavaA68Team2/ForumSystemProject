# Spring Boot Project (MotoForum - Automobile Forum Management System) - Team 2

## Overview

The forum is structured around topics, related to **automobiles**, where users can interact with posts and comments.

---

<br>

## Features

- **Create posts** related to automobiles.
- **Add comments** on posts.
- **Add tags** on posts for better filtering.
- **Search filtering** on posts based on different attributes.
- **React** to posts with likes and dislikes.
- **Register and authenticate** using JWT authentication.
- **Admins** moderate content to maintain quality.

<br />

## Technologies Used

- **JDK 17**
- **Spring Boot framework**
- **MariaDB**
- **Hibernate**
- **JWT Authentication**
- **Cloudinary API for Picture Upload**
- **Swagger API Documentation**

---

<br>

## Installation

Follow these steps to set up and run the application:

1. **Clone the Repository**: Download the project folder.
2. **Set Up the Database**: Create and configure a MariaDB database. Use the provided scripts to populate it.
3. **Configure Properties**: Edit `application.properties` in `src/main/resources/` to match your database settings.
4. **Run the Application**: Execute `ForumProjectApplication.class` to start the project.

---

<br>

## **IMPORTANT NOTE**

⚠️ **Every user/admin in the dummy data script has the same password of**  
**"12345678" – encrypted to**  
`$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m`  
⚠️ **The JWT token is set to expire after 100 days!**  
⚠️ **This is done to make testing functionalities easier!**

---

<br>

## Database Diagram

![database_relations_diagram](https://github.com/user-attachments/assets/e0692b79-b92f-4d2a-83c0-96db44d3d428)

<br>

---

## Solution Structure

- **Controllers**: Handle HTTP requests and responses for endpoints like posts, comments, and user profiles.
- **Services**: Implement business logic for managing posts, comments, and user accounts.
- **Repositories**: Interface with the database for CRUD operations.
- **Models**: Represent entities such as Users, Posts, and Comments.
- **Utilities**: Provide helper methods for validation and additional logic.

---

<br>

## Contributors

For further information, please feel free to contact us:

| Authors               | Emails                       | GitHub                                           |
|-----------------------|------------------------------|--------------------------------------------------|
| **Georgi Benchev**    | gega4321@gmail.com           | [GitHub Link](https://github.com/Georgi-Benchev) |
| **Ivan Ivanov**       | ivanovivanbusiness@gmail.com | [GitHub Link](https://github.com/ivanoffcode)    |

---

<br>

## Swagger Documentation

We provide a **Swagger API Documentation** to facilitate testing and exploration of the forum's endpoints.

- **Access it at**: [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)
