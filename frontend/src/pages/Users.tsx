import { LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';
import FileDownloadOutlinedIcon from '@mui/icons-material/FileDownloadOutlined';
import AddIcon from '@mui/icons-material/Add';

export default function Users() {
  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      <div className="flex w-full items-center justify-between gap-6">
        <h1 className="text-3xl font-black leading-11 tracking-[0.2em] text-black dark:text-white">
          Users
        </h1>

        <div className="flex items-center gap-4">
          <Button
            size="sm"
            iconLeft={<FileDownloadOutlinedIcon fontSize="small" />}
            className="border-grayscale-200 bg-white font-bold uppercase text-black shadow-none hover:border-grayscale-300 hover:bg-grayscale-50 active:bg-grayscale-100 dark:bg-transparent dark:text-white dark:hover:border-grayscale-500 dark:hover:bg-bg-dark"
          >
            Export
          </Button>
          <Button
            size="sm"
            iconLeft={<AddIcon fontSize="small" />}
            className="border-(--color-primaryblue) bg-(--color-primaryblue) font-bold uppercase text-white shadow-none hover:brightness-95 active:brightness-90"
          >
            New
          </Button>
        </div>
      </div>

      <div className="mt-6 h-px w-full bg-[var(--color-table-border)]" />
    </LayoutColumn>
  );
}

