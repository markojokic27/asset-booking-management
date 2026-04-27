import i18next from 'i18next';
import { initReactI18next } from 'react-i18next';
import enLayout from './locales/en/layout.json';
import hrLayout from './locales/hr/layout.json';
import deLayout from './locales/de/layout.json';

const savedLanguage = localStorage.getItem('language');

i18next.use(initReactI18next).init({
  lng: savedLanguage || 'hr',
  fallbackLng: 'en',
  resources: {
    en: { translation: { layout: enLayout } },
    hr: { translation: { layout: hrLayout } },
    de: { translation: { layout: deLayout } },
  },
  interpolation: {
    escapeValue: false,
  },
});

export default i18next;
