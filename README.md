# URL Shortener

Spring Boot application that creates short codes for URLs and redirects to original URLs.  
Uses MySQL as the persistence layer.

## Prerequisites
- JDK 21
- Docker Desktop (optional)
- Maven

### .env (project root)
- A default `.env` file exists in the project root. Update values as needed.
- If you already have MySQL running locally, and you want to use docker please change `DB_PORT` (e.g. `3307`) to avoid conflicts.

Suggested keys (examples):
```
SERVER_PORT=8080
DB_PORT=3306 (or 3307 for docker)
DB_SCHEMA=url_shortener
DB_USERNAME=user1
DB_PASSWORD=password
DB_ROOT_PASSWORD=root
PMA_PORT=8484
```
### MySQL (local installation)
- Create a database/schema named as per `DB_SCHEMA` in `.env` file.

### Docker (run MySQL + phpMyAdmin)
- Ensure Docker Desktop is installed and running.
- Docker will load environment variables from `.env` file.

PowerShell examples:
```powershell
# docker will use by default .env
docker compose up -d
```
- then phpMyAdmin will be available at http://localhost:8484/
- the database schema will be created automatically.

### Build & run the application (outside IDE)
```powershell
# build (Windows)
.\mvnw.cmd clean package -DskipTests

java -jar .\target\url-shortener-0.0.1-SNAPSHOT.jar
```

### Postman
- in the root of the project you will find: `url-shortener.postman_collection.json`
- Import `url-shortener.postman_collection.json` into Postman for API testing.
- Open the collection and click "View Complete Documentation" to request details.

