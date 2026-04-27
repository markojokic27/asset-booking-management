import 'i18next';
import enLayout from '../config/locales/en/layout.json';

declare module 'i18next' {
  interface CustomTypeOptions {
    defaultNS: 'translation';
    resources: {
      translation: {
        layout: typeof enLayout;
      };
    };
  }
}
