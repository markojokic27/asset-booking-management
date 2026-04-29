import 'i18next';
import enLayout from '../config/locales/en/layout.json';
import enUi from '../config/locales/en/ui.json';
import enUsers from '../config/locales/en/users.json';
import enAccount from '../config/locales/en/account.json';

declare module 'i18next' {
  interface CustomTypeOptions {
    defaultNS: 'translation';
    resources: {
      translation: {
        layout: typeof enLayout;
        ui: typeof enUi;
        users: typeof enUsers;
        account: typeof enAccount;
      };
    };
  }
}
