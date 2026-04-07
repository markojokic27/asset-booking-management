import { twMerge } from 'tailwind-merge';
import SearchSharpIcon from '@mui/icons-material/SearchSharp';

type SearchInputProps = {
    value: string;
    onChange: (value: string) => void;
    placeholder?: string;
    className?: string
};

export const SearchInput: React.FC<SearchInputProps> = ({
    value,
    onChange,
    placeholder = 'Search by name...',
    className,
}) => {
    return (
        <div className='relative w-full mb-3'>
            <div className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none">
                <SearchSharpIcon className="h-5 w-5 text-[var(--color-table-border)]" />
            </div>
            <input
                type="text"
                value={value}
                onChange={(e) => onChange(e.target.value)}
                placeholder={placeholder}
                className={twMerge('w-full pl-10 py-2 border-2 border-[var(--color-table-border)] outline-none focus:ouline-none', className)} />
        </div>
    );
};