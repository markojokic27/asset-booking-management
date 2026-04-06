import * as DropdownMenu from '@radix-ui/react-dropdown-menu';
import DE from 'country-flag-icons/react/3x2/DE';
import GB from 'country-flag-icons/react/3x2/GB';
import HR from 'country-flag-icons/react/3x2/HR';
import { useTranslation } from 'react-i18next';
import { ChevronDown } from '../icons/ChevronDown';

const languages = [
  { code: 'hr', label: 'Hrvatski', Flag: HR },
  { code: 'en', label: 'English', Flag: GB },
  { code: 'de', label: 'Deutsch', Flag: DE },
] as const;

const flagClass =
  'h-5 w-[1.875rem] shrink-0 overflow-hidden rounded-sm ring-1 ring-black/10 dark:ring-white/15';

function resolveLanguage(code: string) {
  return (
    languages.find((lang) => lang.code === code) ??
    languages.find((lang) => code.startsWith(`${lang.code}-`)) ??
    languages.find((lang) => code.startsWith(lang.code))
  );
}

function LanguageSwitcher() {
  const { i18n } = useTranslation();

  const currentLanguage = resolveLanguage(i18n.language);

  const handleChange = (code: string) => {
    i18n.changeLanguage(code);
    localStorage.setItem('language', code);
  };

  const CurrentFlag = currentLanguage?.Flag;

  return (
    <DropdownMenu.Root modal={false}>
      <DropdownMenu.Trigger asChild>
        <button
          type="button"
          aria-label={currentLanguage?.label ?? 'Select language'}
          className="group flex items-center gap-1.5 text-gray-900 hover:cursor-pointer focus:outline-none dark:text-gray-100"
        >
          {CurrentFlag ? (
            <CurrentFlag className={flagClass} title={currentLanguage.label} />
          ) : (
            <span className="text-sm">?</span>
          )}
          <ChevronDown className="h-5 w-5 shrink-0 transition-transform duration-300 ease-in-out group-data-[state=open]:rotate-180" />
        </button>
      </DropdownMenu.Trigger>

      <DropdownMenu.Content
        align="end"
        className="mt-1 rounded border border-gray-200 bg-white text-gray-900 shadow dark:border-gray-700 dark:bg-gray-900 dark:text-gray-100"
      >
        {languages.map((lang) => {
          const Flag = lang.Flag;
          return (
            <DropdownMenu.Item
              key={lang.code}
              onSelect={() => handleChange(lang.code)}
              className="cursor-pointer px-4 py-2 hover:bg-gray-100 hover:outline-none dark:hover:bg-gray-800"
            >
              <span className="flex items-center gap-3">
                <Flag className={`${flagClass} pointer-events-none`} />
                <span className="text-sm">{lang.label}</span>
              </span>
            </DropdownMenu.Item>
          );
        })}
      </DropdownMenu.Content>
    </DropdownMenu.Root>
  );
}

export default LanguageSwitcher;
