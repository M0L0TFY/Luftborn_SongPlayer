# Song Player 🎵

A Spring Boot REST API backend application that connects music listeners to a vast library of songs.

## 📚 Tech Stack
- **Language:** Java 21
- **Frameworks:** Spring Boot 4.1, Spring Data JPA, Spring Security
- **Database/Cache:** PostgreSQL 18, Redis 8.2
- **Infrastructure:** Docker & Docker Compose

## ✨ Main Features
- **User Authentication:** Secure Login/Register using stateless JWT access token and opaque refresh token.
- **Songs & Albums Management:** Create your songs and albums with ease.
- **Playlist Customization:** Customize your playlists to your liking while having the ability to make it public for others to enjoy.

## 🚀 How to Run

### Prerequisites
- [Git](https://git-scm.com/install/)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

### 1. Clone the repository
```bash
git clone https://github.com/M0L0TFY/Luftborn_SongPlayer.git
cd Luftborn_SongPlayer/Luftborn_Backend
```
### 2. Setup Environment Variables
Create a ``.env`` file in the root folder based on ``.env.template``.
```bash
# On Linux/macOS/Git Bash:
cp .env.template .env

# On Windows:
copy .env.template .env
```

### 3. Build and Run
Run the following command in the terminal.
```bash
docker compose up --build
```
Please wait about 5 seconds after running the command. Since the Docker container and the backend service need a moment to initialize.

Once the application is running, you can explore the API endpoints via Swagger UI:

👉 http://localhost:8080/swagger-ui/index.html

Make sure that you register an account from the Authentication endpoint, then copy the accessToken string from the response body and paste it into the green Authorize button at the top right of the page (with the padlock icon) to login.

### 4. Clean up
Once you're done with the application, you can clean up your system by running the following command in the terminal.
```bash
docker compose down -v
```