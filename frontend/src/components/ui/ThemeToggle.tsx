import { twMerge } from 'tailwind-merge';
import { useTheme } from '../../app/ThemeProvider';
import { Moon } from '../icons/Moon';
import { Sun } from '../icons/Sun';

type ThemeToggleProps = {
  className?: string;
};

export default function ThemeToggle({ className }: ThemeToggleProps) {
  const { theme, toggleTheme } = useTheme();
  const isDark = theme === 'dark';

  return (
    <button
      type="button"
      onClick={toggleTheme}
      aria-label="Toggle dark mode"
      aria-pressed={isDark}
      className={twMerge(
        'relative inline-flex h-10 w-26 items-center rounded-full bg-gray-200 p-1 text-gray-700 shadow-sm outline-none ring-1 ring-black/5 transition-colors hover:bg-gray-300 focus-visible:ring-2 focus-visible:ring-blue-500',
        'dark:bg-gray-800 dark:text-gray-200 dark:ring-white/10 dark:hover:bg-gray-700',
        className
      )}
    >
      <span className="relative z-10 flex w-full items-center justify-between px-2">
        <Sun className="h-6 w-6" />
        <Moon className="h-6 w-6" />
      </span>

      <span
        aria-hidden="true"
        className={twMerge(
          'absolute left-1 top-1 h-8 w-12 rounded-full bg-gray-100 shadow transition-transform duration-200 ease-in-out',
          'dark:bg-gray-900',
          isDark && 'translate-x-12'
        )}
      />
    </button>
  );
}
