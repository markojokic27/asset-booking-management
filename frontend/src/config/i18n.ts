import i18next from 'i18next';
import { initReactI18next } from 'react-i18next';
import enLayout from './locales/en/layout.json';
import enUi from './locales/en/ui.json';
import enUsers from './locales/en/users.json';
import enAccount from './locales/en/account.json';
import enAssetCategories from './locales/en/assetCategories.json';
import hrLayout from './locales/hr/layout.json';
import hrUi from './locales/hr/ui.json';
import hrUsers from './locales/hr/users.json';
import hrAccount from './locales/hr/account.json';
import hrAssetCategories from './locales/hr/assetCategories.json';
import deLayout from './locales/de/layout.json';
import deUi from './locales/de/ui.json';
import deUsers from './locales/de/users.json';
import deAccount from './locales/de/account.json';
import deAssetCategories from './locales/de/assetCategories.json';

const savedLanguage = localStorage.getItem('language');

i18next.use(initReactI18next).init({
  lng: savedLanguage || 'hr',
  fallbackLng: 'en',
  resources: {
    en: {
      translation: {
        layout: enLayout,
        ui: enUi,
        users: enUsers,
        account: enAccount,
        assetCategories: enAssetCategories,
      },
    },
    hr: {
      translation: {
        layout: hrLayout,
        ui: hrUi,
        users: hrUsers,
        account: hrAccount,
        assetCategories: hrAssetCategories,
      },
    },
    de: {
      translation: {
        layout: deLayout,
        ui: deUi,
        users: deUsers,
        account: deAccount,
        assetCategories: deAssetCategories,
      },
    },
  },
  interpolation: {
    escapeValue: false,
  },
});

export default i18next;
