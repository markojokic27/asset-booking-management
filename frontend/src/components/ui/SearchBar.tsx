import { twMerge } from 'tailwind-merge';
import SearchSharpIcon from '@mui/icons-material/SearchSharp';

type SearchInputProps = {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
  className?: string;
};

export const SearchInput: React.FC<SearchInputProps> = ({
  value,
  onChange,
  placeholder = 'Search by name...',
  className,
}) => {
  return (
    <div className="relative mb-3 w-full">
      <div className="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
        <SearchSharpIcon className="h-5 w-5 text-(--color-table-border)" />
      </div>
      <input
        type="text"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder}
        className={twMerge(
          'border-(--color-table-border focus:ouline-none w-full border-2 py-2 pl-10 outline-none',
          className
        )}
      />
    </div>
  );
};
