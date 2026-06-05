import 'i18next';

// English translations
import enLayout from '../config/locales/en/layout.json';
import enUi from '../config/locales/en/ui.json';
import enUsers from '../config/locales/en/users.json';
import enAccount from '../config/locales/en/account.json';
import enAssetCategories from '../config/locales/en/assetCategories.json';
import enAssets from '../config/locales/en/assets.json';
import enBookings from '../config/locales/en/bookings.json';
import enDepartments from '../config/locales/en/departments.json';
import enReport from '../config/locales/en/report.json';
import enApprovals from '../config/locales/en/approvals.json';
import enMyBookings from '../config/locales/en/myBookings.json';

declare module 'i18next' {
  interface CustomTypeOptions {
    defaultNS: 'translation';
    resources: {
      translation: {
        layout: typeof enLayout;
        ui: typeof enUi;
        users: typeof enUsers;
        account: typeof enAccount;
        assetCategories: typeof enAssetCategories;
        assets: typeof enAssets;
        bookings: typeof enBookings;
        departments: typeof enDepartments;
        report: typeof enReport;
        approvals: typeof enApprovals;
        myBookings: typeof enMyBookings;
      };
    };
  }
}
