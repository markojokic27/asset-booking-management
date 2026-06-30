import { twMerge } from 'tailwind-merge';

export type TabItem<T extends string> = {
  label: string;
  value: T;
};

type TabsProps<T extends string> = {
  tabs: TabItem<T>[];
  value: T;
  onChange: (val: T) => void;
  className?: string;
};

export function Tab<T extends string>({
  tabs,
  value,
  onChange,
  className,
}: Readonly<TabsProps<T>>) {
  return (
    <div className={twMerge('flex border-b border-gray-200', className)}>
      {tabs.map((tab) => {
        const isActive = tab.value === value;

        return (
          <button
            key={tab.value}
            type="button"
            onClick={() => onChange(tab.value)}
            className={twMerge(
              'cursor-pointer px-4 py-3 text-sm font-semibold transition-colors',
              'rounded-none border-b-2',
              isActive
                ? 'border-blue-600 text-blue-600'
                : 'border-transparent text-black hover:text-blue-600'
            )}
          >
            {tab.label}
          </button>
        );
      })}
    </div>
  );
}