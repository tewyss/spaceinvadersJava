# 👾 Space Invaders

A simple remake of the classic **Space Invaders** arcade game, built in **Java** with **custom sprites** and a **Spring-based backend** for database integration. The project demonstrates basic game logic, graphical rendering, and data persistence.

## Features

- Game is made for 2 languages (Czech and English)
- 2D game with custom-designed alien, player, and bullet sprites
- Keyboard controls for movement and shooting
- Game logic includes score tracking, player lives, and collision detection
- Score saving to a database via a Spring Boot REST API
- "Hall of Fame" feature showing top scores

## How to Install
### 1. Clone the Repository
### 2. Backend (Spring boot)
- Open the backend project in your IDE (e.g. IntelliJ IDEA or Eclipse)
- Make sure **Java 21+**
- Run `Application.java` in storage modul or use command
```bash
mvn spring-boot:run
```
- Backend will start on `http://localhost:8080`

### 3. Frontend (JavaFX)
- Open the `spaceinvaders` module in your IDE
- Run the main game class

## Gameplay Preview
![mainMenu](https://github.com/user-attachments/assets/09d724ff-41d2-41dc-a059-c33484b0a7a4)
![gameplay](https://github.com/user-attachments/assets/e05e29b6-554b-432d-b59d-e34be35c0d33)
