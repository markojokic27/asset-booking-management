import i18next from 'i18next';
import { initReactI18next } from 'react-i18next';
import enTranslation from './locales/en.json';
import hrTranslation from './locales/hr.json';
import deTranslation from './locales/de.json';

const savedLanguage = localStorage.getItem('language');

i18next
  .use(initReactI18next)
  .init({
    lng: savedLanguage || 'hr',
    fallbackLng: 'en',
    resources: {
      en: { translation: enTranslation },
      hr: { translation: hrTranslation },
      de: { translation: deTranslation },
    },
    interpolation: {
      escapeValue: false,
    },
  });

export default i18next;