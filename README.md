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
- [API Reference](#api-reference)
- [Running Tests](#running-tests)
- [Docker](#docker)
- [CI/CD](#cicd)
- [Deployment](#deployment)
- [License](#license)

---

## Overview

Blackjack API is a fully reactive backend service that allows players to:

- Register and authenticate via JWT
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
│  Controllers · Repositories · Security · Config     │
└─────────────────────────────────────────────────────┘
```

**Core rule:** dependency arrows always point inward. The domain never knows about Spring, MongoDB, or any external library.

**Bounded Contexts:**

| Context | Responsibility | Database |
|---------|---------------|----------|
| `game` | Game lifecycle, Blackjack rules | MongoDB |
| `player` | Player profiles, authentication | MySQL |
| `ranking` | Leaderboard queries | MySQL |

**Inbound adapters (driving):** REST controllers, JWT filter  
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
| Security | Spring Security + JWT (JJWT 0.12) |
| Cache | Caffeine |
| Documentation | SpringDoc OpenAPI (Swagger UI) |
| Testing | JUnit 5 · Mockito · WebTestClient · StepVerifier |
| Containerisation | Docker (multi-stage build) |
| CI/CD | GitHub Actions |
| Deployment | Render |

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
        ├── security/              # JwtService, JwtAuthFilter
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
| `JWT_SECRET` | Yes | HS256 signing key (min 256 bits) |

---

## API Reference

Full interactive documentation available at `/swagger-ui.html`.

### Authentication

```
POST /auth/register    Register a new player account
POST /auth/login       Login and receive a JWT token
```

All other endpoints require a valid JWT in the `Authorization` header:

```
Authorization: Bearer <token>
```

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

### Example: create a game

**Request**
```http
POST /game/new
Authorization: Bearer eyJhbGci...
Content-Type: application/json

{
  "playerId": 1
}
```

**Response `201 Created`**
```json
{
  "gameId": "665f1a2b3c4d5e6f7a8b9c0d",
  "playerId": 1,
  "status": "IN_PROGRESS",
  "playerHand": {
    "cards": [
      { "suit": "HEARTS",   "rank": "A", "value": 11 },
      { "suit": "SPADES",   "rank": "K", "value": 10 }
    ],
    "score": 21,
    "bust": false
  },
  "dealerHand": {
    "cards": [
      { "suit": "DIAMONDS", "rank": "7", "value": 7 },
      { "suit": "HIDDEN",   "rank": "?", "value": 0 }
    ],
    "score": 7,
    "bust": false
  },
  "message": "Blackjack! You win!"
}
```

### Example: play a turn

**Request**
```http
POST /game/665f1a2b3c4d5e6f7a8b9c0d/play
Authorization: Bearer eyJhbGci...
Content-Type: application/json

{
  "action": "HIT"
}
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

## Running Tests

```bash
# All tests
./mvnw verify

# Unit tests only (no Spring context)
./mvnw test

# Integration tests only
./mvnw failsafe:integration-test

# With coverage report
./mvnw verify jacoco:report
# Report at: target/site/jacoco/index.html
```

**Test strategy:**

| Test type | Scope | Tools |
|-----------|-------|-------|
| Unit | Domain logic (no Spring) | JUnit 5 |
| Slice | Controller layer only | WebTestClient + @WebFluxTest |
| Integration | Full stack with real DBs | @SpringBootTest + StepVerifier |

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

---

## CI/CD

GitHub Actions pipeline defined in `.github/workflows/ci.yml`:

```
Push to any branch
    │
    ▼
Job: ci
  └─ Checkout → Java 21 → mvn verify (all tests must pass)
        │
        │ (only on main)
        ▼
Job: docker
  └─ Build image → Push to Docker Hub
        │
        │ (only if docker succeeds)
        ▼
Job: deploy
  └─ Trigger Render deploy hook
```

**Required GitHub Secrets:**

| Secret | Description |
|--------|-------------|
| `DOCKER_USERNAME` | Docker Hub username |
| `DOCKER_PASSWORD` | Docker Hub access token |
| `RENDER_DEPLOY_HOOK` | Render deploy webhook URL |
| `JWT_SECRET` | JWT signing key |
| `MONGODB_URI` | MongoDB Atlas URI |
| `R2DBC_URL` | Cloud MySQL R2DBC URL |
| `DB_USER` | Cloud MySQL username |
| `DB_PASS` | Cloud MySQL password |

---

## Deployment

The application is deployed on **Render** using a Docker image hosted on Docker Hub.

**Cloud databases:**
- **MongoDB** → MongoDB Atlas (free M0 cluster)
- **MySQL** → Aiven for MySQL (free tier)

### Manual deploy steps

1. Push a Docker image to Docker Hub
2. Create a **Web Service** on Render pointing to the image
3. Set all environment variables in Render dashboard
4. Deploy

All subsequent deploys are automatic via the GitHub Actions pipeline.

> **Note:** Render free tier services spin down after 15 minutes of inactivity. The first request after a cold start may take 30–60 seconds.

**Health check endpoint:**  
`GET /actuator/health` → `{ "status": "UP" }`

---
