import { LayoutColumn } from '../components/layout/Layout';

export default function Bookings() {
  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      <h1 className="text-3xl leading-11 font-black tracking-[0.2em] text-black dark:text-white">
        Bookings
      </h1>
      <div className="mt-6 h-px w-full bg-(--color-table-border)" />
    </LayoutColumn>
  );
}
