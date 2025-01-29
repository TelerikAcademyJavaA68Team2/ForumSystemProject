# Forum Management System - Team 2, Java Alpha Program, Telerik Academy

## Georgi and Ivan's Forum Project

### **Database Schema:**
The forum is structured around topics related to **automobiles**, where users can interact with posts and comments.

### **Project Description:**
This is an **Automobile Forum Management System** developed using the **Spring Boot** framework. The forum allows users to:
- **Create posts** related to automobiles.
- **Add comments** on posts.
- **Add tags** on posts for better filtering.
- **Search filtering** on posts based on different attributes.
- **React** to posts with likes and dislikes.
- **Register and authenticate** using JWT authentication.
- **Admins** moderate content to maintain quality.

### **Technologies Used:**
- **JDK 17**
- **Spring Boot framework**
- **MariaDB**
- **Hibernate**
- **JWT Authentication**
- **Swagger API Documentation**

### **How to Build the Project:**
1. **Clone the Repository**: Download the project folder.
2. **Set Up the Database**: Create and configure a MariaDB database. Use the provided scripts to populate it.
3. **Configure Properties**: Edit `application.properties` in `src/main/resources/` to match your database settings.
4. **Run the Application**: Execute `ForumProjectApplication.class` to start the project.

### **Swagger Documentation**
We provide a **Swagger API Documentation** to facilitate testing and exploration of the forum's endpoints.

- **Access it at**: [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)
