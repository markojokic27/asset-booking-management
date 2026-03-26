import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import '../config/i18n.ts'
import App from './App.tsx'
import '../styles/index.css'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
