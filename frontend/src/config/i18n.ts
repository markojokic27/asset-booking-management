import i18next from 'i18next';
import { initReactI18next } from 'react-i18next';

// English translations
import enLayout from './locales/en/layout.json';
import enUi from './locales/en/ui.json';
import enUsers from './locales/en/users.json';
import enAccount from './locales/en/account.json';
import enAssetCategories from './locales/en/assetCategories.json';
import enAssets from './locales/en/assets.json';
import enBookings from './locales/en/bookings.json';
import enDepartments from './locales/en/departments.json';
import enReport from './locales/en/report.json';
import enApprovals from './locales/en/approvals.json';
import enMyBookings from './locales/en/myBookings.json';

// Croatian translations
import hrLayout from './locales/hr/layout.json';
import hrUi from './locales/hr/ui.json';
import hrUsers from './locales/hr/users.json';
import hrAccount from './locales/hr/account.json';
import hrAssetCategories from './locales/hr/assetCategories.json';
import hrAssets from './locales/hr/assets.json';
import hrBookings from './locales/hr/bookings.json';
import hrDepartments from './locales/hr/departments.json';
import hrReport from './locales/hr/report.json';
import hrApprovals from './locales/hr/approvals.json';
import hrMyBookings from './locales/hr/myBookings.json';

// German translations
import deLayout from './locales/de/layout.json';
import deUi from './locales/de/ui.json';
import deUsers from './locales/de/users.json';
import deAccount from './locales/de/account.json';
import deAssetCategories from './locales/de/assetCategories.json';
import deAssets from './locales/de/assets.json';
import deBookings from './locales/de/bookings.json';
import deDepartments from './locales/de/departments.json';
import deReport from './locales/de/report.json';
import deApprovals from './locales/de/approvals.json';
import deMyBookings from './locales/de/myBookings.json';

export const LANGUAGE_STORAGE_KEY = 'language';
const SUPPORTED_LANGUAGES = ['en', 'hr', 'de'] as const;

const stored = localStorage.getItem(LANGUAGE_STORAGE_KEY);
const isStoredSupported =
  stored !== null &&
  (SUPPORTED_LANGUAGES as readonly string[]).includes(stored);
const initialLng = isStoredSupported
  ? (stored as (typeof SUPPORTED_LANGUAGES)[number])
  : 'en';

if (!isStoredSupported) {
  localStorage.setItem(LANGUAGE_STORAGE_KEY, initialLng);
}

i18next.use(initReactI18next).init({
  lng: initialLng,
  fallbackLng: 'en',
  resources: {
    en: {
      translation: {
        layout: enLayout,
        ui: enUi,
        users: enUsers,
        account: enAccount,
        assetCategories: enAssetCategories,
        assets: enAssets,
        bookings: enBookings,
        report: enReport,
        approvals: enApprovals,
        myBookings: enMyBookings,
        departments: enDepartments,
      },
    },
    hr: {
      translation: {
        layout: hrLayout,
        ui: hrUi,
        users: hrUsers,
        account: hrAccount,
        assetCategories: hrAssetCategories,
        assets: hrAssets,
        bookings: hrBookings,
        report: hrReport,
        approvals: hrApprovals,
        myBookings: hrMyBookings,
        departments: hrDepartments,
      },
    },
    de: {
      translation: {
        layout: deLayout,
        ui: deUi,
        users: deUsers,
        account: deAccount,
        assetCategories: deAssetCategories,
        assets: deAssets,
        bookings: deBookings,
        report: deReport,
        approvals: deApprovals,
        myBookings: deMyBookings,
        departments: deDepartments,
      },
    },
  },
  interpolation: {
    escapeValue: false,
  },
});

export default i18next;
