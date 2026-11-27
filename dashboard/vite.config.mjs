import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  // Exposed to client code via import.meta.env (values end up in the public bundle)
  envPrefix: ['VITE_', 'REACT_APP_'],
  oxc: {
    jsx: true
  },
  test: {
    environment: 'jsdom',
    setupFiles: './src/setupTests.js'
  }
});
