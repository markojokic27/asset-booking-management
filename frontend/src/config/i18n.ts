import i18next from 'i18next';
import { initReactI18next } from 'react-i18next';
import enCommon from './locales/en/common.json';
import enLayout from './locales/en/layout.json';
import hrCommon from './locales/hr/common.json';
import hrLayout from './locales/hr/layout.json';
import deCommon from './locales/de/common.json';
import deLayout from './locales/de/layout.json';

const savedLanguage = localStorage.getItem('language');

i18next.use(initReactI18next).init({
  lng: savedLanguage || 'hr',
  fallbackLng: 'en',
  resources: {
    en: { translation: { common: enCommon, layout: enLayout } },
    hr: { translation: { common: hrCommon, layout: hrLayout } },
    de: { translation: { common: deCommon, layout: deLayout } },
  },
  interpolation: {
    escapeValue: false,
  },
});

export default i18next;
