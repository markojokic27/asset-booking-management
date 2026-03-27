import * as DropdownMenu from '@radix-ui/react-dropdown-menu';
import { useTranslation } from 'react-i18next';
import { ChevronDown } from '../icons/ChevronDown';

const languages = [
  { code: 'hr', label: 'HRV' },
  { code: 'en', label: 'ENG' },
  { code: 'de', label: 'DEU' },
];

function LanguageSwitcher() {
  const { i18n } = useTranslation();

  const currentLanguage = languages.find((lang) => lang.code === i18n.language);

  const handleChange = (code: string) => {
    i18n.changeLanguage(code);
    localStorage.setItem('language', code);
  };

  return (
    <DropdownMenu.Root>
      <DropdownMenu.Trigger asChild>
        <button className="group flex items-center focus:outline-none">
          {currentLanguage?.label || 'Select Language'}
          <ChevronDown className="h-6 w-6 transition-transform duration-300 ease-in-out group-data-[state=open]:rotate-180" />{' '}
        </button>
      </DropdownMenu.Trigger>

      <DropdownMenu.Content
        align="end"
        className="mt-1 rounded border bg-white shadow"
      >
        {languages.map((lang) => (
          <DropdownMenu.Item
            key={lang.code}
            onSelect={() => handleChange(lang.code)}
            className="cursor-pointer px-6 py-2 hover:bg-gray-100 hover:outline-none"
          >
            {lang.label}
          </DropdownMenu.Item>
        ))}
      </DropdownMenu.Content>
    </DropdownMenu.Root>
  );
}

export default LanguageSwitcher;
