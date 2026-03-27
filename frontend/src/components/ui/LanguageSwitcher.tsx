import { useState } from 'react';
import { useTranslation } from 'react-i18next';

const languages = [
  { code: 'hr', label: 'HRV' },
  { code: 'en', label: 'ENG' },
  { code: 'de', label: 'DEU' },
];

function LanguageSwitcher() {
  const { i18n } = useTranslation();
  const [isOpen, setIsOpen] = useState(false);

  const currentLanguage = languages.find((lang) => lang.code === i18n.language);

  const handleChange = (code: string) => {
    i18n.changeLanguage(code);
    localStorage.setItem('language', code);
    setIsOpen(false);
  };

  return (
    <div className="relative">
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="flex items-center gap-1"
      >
        {currentLanguage?.label} ▾
      </button>

      {isOpen && (
        <ul className="absolute right-0 mt-1 rounded border bg-white shadow">
          {languages.map((lang) => (
            <li key={lang.code}>
              <button
                onClick={() => handleChange(lang.code)}
                className="w-full px-4 py-2 text-left hover:bg-gray-100"
              >
                {lang.label}
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default LanguageSwitcher;
