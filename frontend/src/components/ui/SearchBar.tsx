// External packages
import { twMerge } from 'tailwind-merge';
import SearchSharpIcon from '@mui/icons-material/SearchSharp';
import { useTranslation } from 'react-i18next';

type SearchInputProps = {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
  className?: string;
};

export const SearchInput: React.FC<SearchInputProps> = ({
  value,
  onChange,
  placeholder,
  className,
}) => {
  const { t } = useTranslation();
  return (
    <div className={twMerge('relative w-full pt-1', className)}>
      <div className="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
        <SearchSharpIcon className="h-5 w-5 text-(--color-table-border)" />
      </div>
      <input data-testid="search-input" 
        type="text"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder ?? t('ui.search.byNamePlaceholder')}
        className="w-full rounded-lg border-2 border-(--color-table-border) bg-(--color-table-surface) py-2 pl-10 text-(--color-table-text) outline-none placeholder:text-(--color-table-text)/60 focus:outline-none"
      />
    </div>
  );
};
