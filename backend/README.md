# PreCare Standalone Backend (Node.js + TypeScript + Express + PostgreSQL + Prisma)

This is the independent REST API backend for **PreCare**, serving both the native Android client and future web application.

## Core Capabilities
- **Anonymous Profiles**: No authentication required. First launch generates a persistent device profile.
- **Pregnancy Milestones**: Real calculations for LMP, EDD, current week, days remaining, and trimester tracking.
- **Medical Report Pipeline**: Multi-phase OCR validation, medical classification, structured lab extraction, and Gemini clinical interpretation.
- **Maya Assistant**: AI pregnancy and first-aid companion with obstetric emergency detection.
- **First Aid & Hospitals**: Caching endpoints and geo-location for maternal care centers.

## Prerequisites
- Node.js 20+
- PostgreSQL 16+ or Docker

## Setup & Run Locally

```bash
# 1. Install dependencies
cd backend
npm install

# 2. Configure environment
cp .env.example .env

# 3. Setup PostgreSQL & Prisma
npx prisma generate
npx prisma migrate dev --name init

# 4. Start Development Server
npm run dev
```

The server starts at `http://localhost:5000`.

## Docker Compose Quickstart
```bash
docker-compose up --build -d
```

## API Documentation
- Health Check: `GET /api/health`
- OpenAPI Specification: `GET /api/docs`
