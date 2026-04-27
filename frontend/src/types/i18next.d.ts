import 'i18next';
import enCommon from '../config/locales/en/common.json';
import enLayout from '../config/locales/en/layout.json';

declare module 'i18next' {
  interface CustomTypeOptions {
    defaultNS: 'translation';
    resources: {
      translation: {
        common: typeof enCommon;
        layout: typeof enLayout;
      };
    };
  }
}
