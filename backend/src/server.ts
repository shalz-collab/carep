import app from './app';
import { config } from './config';

const server = app.listen(config.port, '0.0.0.0', () => {
  console.log(`PreCare shared REST backend running on http://0.0.0.0:${config.port}`);
  console.log(`Health Check: http://0.0.0.0:${config.port}/api/health`);
  console.log(`Documentation: http://0.0.0.0:${config.port}/api/docs`);
});

process.on('SIGTERM', () => {
  console.log('SIGTERM signal received: closing HTTP server');
  server.close(() => {
    console.log('HTTP server closed');
  });
});
