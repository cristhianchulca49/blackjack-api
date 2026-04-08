# Blackjack API

![CI/CD](https://github.com/YOUR_USERNAME/blackjack-api/actions/workflows/ci.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-brightgreen?logo=springboot)
![License](https://img.shields.io/badge/license-MIT-blue)

> Reactive REST API for a Blackjack game — built with Java 21, Spring WebFlux, MongoDB and MySQL, following DDD and Hexagonal Architecture.

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Environment Variables](#environment-variables)
- [Docker](#docker)


---

## Overview

Blackjack API is a fully reactive backend service that allows players to:

- Create and play Blackjack games (HIT / STAND)
- Track their win/loss statistics
- Compete on a global leaderboard

All endpoints are non-blocking, built on top of Spring WebFlux and Project Reactor. Game state is stored in **MongoDB** (flexible document model), while player profiles and ranking data live in **MySQL** accessed via **R2DBC** (reactive driver — zero blocking threads).

---

## Architecture

The project follows **Domain-Driven Design (DDD)** with a **Hexagonal Architecture** (also known as Ports & Adapters), organised into three independent Bounded Contexts:

```
┌─────────────────────────────────────────────────────┐
│                  Infrastructure                     │
│  ┌───────────────────────────────────────────────┐  │
│  │               Application                    │  │
│  │  ┌─────────────────────────────────────────┐ │  │
│  │  │              Domain                     │ │  │
│  │  │  Entities · Value Objects · Ports       │ │  │
│  │  │  No external dependencies               │ │  │
│  │  └─────────────────────────────────────────┘ │  │
│  │  Use Cases · Reactive orchestration           │  │
│  └───────────────────────────────────────────────┘  │
│  Controllers · Repositories · · Config           │
└─────────────────────────────────────────────────────┘
```

**Core rule:** dependency arrows always point inward. The domain never knows about Spring, MongoDB, or any external library.

**Bounded Contexts:**

| Context | Responsibility | Database |
|---------|---------------|----------|
| `game` | Game lifecycle, Blackjack rules | MongoDB |
| `player` | Player profiles | MySQL |
| `ranking` | Leaderboard queries | MySQL |

**Inbound adapters (driving):** REST controllers,  
**Outbound adapters (driven):** MongoDB adapter, R2DBC MySQL adapter

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.3 |
| Reactive engine | Spring WebFlux + Project Reactor |
| Game persistence | MongoDB (Spring Data Reactive) |
| Player persistence | MySQL via R2DBC |
| Cache | Caffeine |
| Testing | JUnit 5 · Mockito · WebTestClient · StepVerifier |
| Containerisation | Docker (multi-stage build) |

---

## Project Structure

```
com.blackjack/
│
├── game/                          # Bounded Context: Game
│   ├── domain/
│   │   ├── model/                 # Game, Hand, Card, Deck, GameStatus
│   │   ├── port/
│   │   │   ├── in/                # CreateGameUseCase, PlayGameUseCase ...
│   │   │   └── out/               # GameRepositoryPort
│   │   └── service/               # BlackjackDomainService (pure rules)
│   ├── application/
│   │   └── usecase/               # Use case implementations
│   └── infrastructure/
│       ├── adapter/
│       │   ├── in/rest/           # GameController + DTOs
│       │   └── out/persistence/   # GameMongoAdapter + GameDocument
│       └── mapper/                # Game ↔ GameDocument ↔ GameResponse
│
├── player/                        # Bounded Context: Player
│   ├── domain/
│   ├── application/
│   └── infrastructure/
│       ├── adapter/
│       │   ├── in/rest/           # PlayerController + AuthController
│       │   └── out/persistence/   # PlayerR2dbcAdapter + PlayerEntity
│       └── mapper/
│
├── ranking/                       # Bounded Context: Ranking
│   ├── domain/
│   ├── application/
│   └── infrastructure/
│       ├── adapter/
│       │   ├── in/rest/           # RankingController
│       │   └── out/persistence/   # RankingR2dbcAdapter
│       └── mapper/
│
└── shared/                        # Shared Kernel
    ├── domain/exception/          # GameNotFoundException, InvalidMoveException ...
    └── infrastructure/
        ├── config/                # Mongo, R2DBC, Security, Cache, Swagger
        └── exception/             # GlobalExceptionHandler
```

---

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- Docker + Docker Compose

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/blackjack-api.git
cd blackjack-api
```

### 2. Set up environment variables

```bash
cp .env.example .env
# Edit .env with your local values
```

### 3. Start databases

```bash
docker-compose up -d
```

This starts:
- MongoDB on `localhost:27017`
- MySQL on `localhost:3306`

### 4. Run the application

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`  
Swagger UI at `http://localhost:8080/swagger-ui.html`

---

## Environment Variables

Copy `.env.example` to `.env` and fill in your values. **Never commit `.env`.**

```env
# Server
PORT=8080

# MongoDB
MONGODB_URI=mongodb://localhost:27017/blackjack

# MySQL R2DBC
R2DBC_URL=r2dbc:mysql://localhost:3306/blackjack_players
DB_USER=root
DB_PASS=secret

# JWT
JWT_SECRET=your-256-bit-secret-key-here

# (Production only)
RENDER_DEPLOY_HOOK=https://api.render.com/deploy/...
DOCKER_USERNAME=your-dockerhub-username
DOCKER_PASSWORD=your-dockerhub-token
```

| Variable | Required | Description |
|----------|----------|-------------|
| `PORT` | No | Server port (default: 8080) |
| `MONGODB_URI` | Yes | Full MongoDB connection URI |
| `R2DBC_URL` | Yes | R2DBC MySQL connection URL |
| `DB_USER` | Yes | MySQL username |
| `DB_PASS` | Yes | MySQL password |

---

### Game

```
POST   /game/new              Create a new game
GET    /game/{id}             Get current game state
POST   /game/{id}/play        Play: HIT or STAND
DELETE /game/{id}/delete      Delete a game
```

### Player

```
PUT    /player/{playerId}     Update player profile
```

### Ranking

```
GET    /ranking?top=10        Get top N players by wins
```



### Error response format

All errors follow a consistent structure:

```json
{
  "timestamp": "2025-06-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Game not found with id: 665f1a2b...",
  "path": "/game/665f1a2b..."
}
```

| HTTP Status | Scenario |
|-------------|----------|
| `400` | Validation error (missing field, wrong format) |
| `401` | Missing or invalid JWT |
| `404` | Game or player not found |
| `409` | Username already taken |
| `422` | Invalid move (e.g. HIT on a finished game) |
| `500` | Unexpected server error |

---

## Docker

### Build image

```bash
docker build -t blackjack-api .
```

### Run container

```bash
docker run -p 8080:8080 --env-file .env blackjack-api
```

### Full local stack

```bash
# Start databases + app
docker-compose up --build

# Stop everything
docker-compose down

# Stop and remove volumes (clears all data)
docker-compose down -v
```

### Dockerfile overview

The image uses a **multi-stage build** to keep the final size minimal:

```
Stage 1 (build)    →  eclipse-temurin:21-jdk-alpine + Maven
                       runs: mvn package -DskipTests
                       output: target/blackjack-api.jar

Stage 2 (runtime)  →  eclipse-temurin:21-jre-alpine
                       copies: only the fat JAR
                       final image size: ~180MB
```
### DockerHub link
``` bash
https://hub.docker.com/r/cristhianchulca/blackjack-api
```
---
