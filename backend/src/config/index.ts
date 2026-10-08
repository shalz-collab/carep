import dotenv from 'dotenv';
dotenv.config();

export const config = {
  port: process.env.PORT ? parseInt(process.env.PORT, 10) : 5000,
  databaseUrl: process.env.DATABASE_URL || '',
  geminiApiKey: process.env.GEMINI_API_KEY || '',
  corsOrigins: process.env.CORS_ORIGINS ? process.env.CORS_ORIGINS.split(',') : ['*'],
  googleCloudProjectId: process.env.GOOGLE_CLOUD_PROJECT_ID || '',
  googlePlacesApiKey: process.env.GOOGLE_PLACES_API_KEY || ''
};
