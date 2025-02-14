📌 MoviesAPI - Movie Backend Project
1️⃣ Project Information
Application Name: movie-backend
Server Port: 8089
Database: MySQL
JPA Provider: Hibernate
External API: OMDB API
2️⃣ Configuration
🔹 Database Configuration

spring.datasource.url=jdbc:mysql://localhost:3306/movieapp
spring.datasource.username=springstudent
spring.datasource.password=springstudent
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
🔹 JPA Configuration

spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
🔹 OMDB API Configuration

omdb.api.url=https://www.omdbapi.com/
omdb.api.key=bd72aea5
🔹 Encoding Configuration
spring.http.encoding.force=true
spring.http.encoding.charset=UTF-8
spring.http.encoding.enabled=true
3️⃣ How to Run the Project
Clone the repository.
Ensure MySQL is running and a database named movieapp exists.
Update application.properties with your database credentials if needed.
Build and run the application using:
mvn spring-boot:run
The application will start on port 8089.

4️⃣ API Endpoints
🌍 Public Endpoints
🔎 Search Movies: GET /movies/search?title={title}&actor={actor}&director={director}
📃 List All Movies: GET /movies/list

🔐 Admin Endpoints
➕ Add Movie: POST /movies/add
❌ Remove Movie: DELETE /movies/remove/{id}

👥 User Endpoints
❤️ Add to Favorites: POST /movies/favorite/{movieId}
⭐ Get Favorite Movies: GET /movies/favorites
🎬 Rate Movie: POST /movies/{movieId}/rate?rating={value}

📌 Ensure you have the required role (ADMIN or USER) for restricted endpoints.












