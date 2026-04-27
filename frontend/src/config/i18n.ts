import i18next from 'i18next';
import { initReactI18next } from 'react-i18next';
import enLayout from './locales/en/layout.json';
import enUi from './locales/en/ui.json';
import hrLayout from './locales/hr/layout.json';
import hrUi from './locales/hr/ui.json';
import deLayout from './locales/de/layout.json';
import deUi from './locales/de/ui.json';

const savedLanguage = localStorage.getItem('language');

i18next.use(initReactI18next).init({
  lng: savedLanguage || 'hr',
  fallbackLng: 'en',
  resources: {
    en: { translation: { layout: enLayout, ui: enUi } },
    hr: { translation: { layout: hrLayout, ui: hrUi } },
    de: { translation: { layout: deLayout, ui: deUi } },
  },
  interpolation: {
    escapeValue: false,
  },
});

export default i18next;
