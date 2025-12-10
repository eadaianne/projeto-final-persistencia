import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';

export default ({ mode }) => {
  // load env variables for the current mode (development by default)
  const env = loadEnv(mode, process.cwd(), '');
  const apiUrl = env.VITE_API_URL || 'http://localhost:8080';

  return defineConfig({
    plugins: [react()],
    server: {
      port: 5173,
      strictPort: false,
      proxy: {
        // Proxy /api requests to the backend configured by VITE_API_URL
        '/api': {
          target: apiUrl,
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, ''),
        },
      },
    },
    build: {
      outDir: 'dist',
      sourcemap: false,
    },
  });
};
