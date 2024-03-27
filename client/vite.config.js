import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react'
import basicSsl from '@vitejs/plugin-basic-ssl'

// https://vitejs.dev/config/
export default ({ mode }) => {
  process.env = {...process.env, ...loadEnv(mode, process.cwd())};
  
  return defineConfig({
    plugins: [
      react(),
      basicSsl(),
    ],

    server: {
      proxy: {
        '/api': {
          target: 'https://localhost:8443/',
          changeOrigin: true,
          secure: false,
        },
      },
    },

    build: {
      outDir: 'build',
    },
    
    base: process.env.VITE_PUBLIC_URL || '/'
  });
};
