import express, { Request, Response, NextFunction } from 'express';
import helmet from 'helmet';
import cors from 'cors';
import rateLimit from 'express-rate-limit';
import { config } from './config';
import routes from './routes';

const app = express();

// Security middleware
app.use(helmet());
app.use(
  cors({
    origin: config.corsOrigins,
    methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS'],
    allowedHeaders: ['Content-Type', 'Authorization']
  })
);

// Rate limiting (100 requests per 15 minutes)
const limiter = rateLimit({
  windowMs: 15 * 60 * 1000,
  max: 100,
  standardHeaders: true,
  legacyHeaders: false,
  message: {
    success: false,
    error: {
      code: 'RATE_LIMIT_EXCEEDED',
      message: 'Too many requests, please try again later.'
    }
  }
});
app.use(limiter);

app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: true, limit: '10mb' }));

// Health Check API
app.get('/api/health', (req: Request, res: Response) => {
  res.status(200).json({
    success: true,
    status: 'healthy',
    database: 'connected',
    timestamp: new Date().toISOString()
  });
});

// Swagger / OpenAPI specification endpoint
app.get('/api/docs', (req: Request, res: Response) => {
  res.status(200).json({
    openapi: '3.0.0',
    info: {
      title: 'PreCare Maternal Healthcare API',
      version: '1.0.0',
      description: 'Shared REST API for PreCare Android client and Web platform.'
    },
    paths: {
      '/api/profiles': {
        post: { summary: 'Create anonymous profile' }
      },
      '/api/reports/analyze': {
        post: { summary: 'Validate and extract medical report values via OCR and AI' }
      },
      '/api/maya/chat': {
        post: { summary: 'Pregnancy assistant with emergency symptom safeguards' }
      }
    }
  });
});

// API Routes
app.use('/api', routes);

// Global Error Handler
app.use((err: any, req: Request, res: Response, next: NextFunction) => {
  const statusCode = err.statusCode || 500;
  const errorCode = err.code || 'SERVER_ERROR';
  const message = err.message || 'An unexpected error occurred.';

  res.status(statusCode).json({
    success: false,
    error: {
      code: errorCode,
      message
    }
  });
});

export default app;
