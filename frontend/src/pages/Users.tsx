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
            className="border-white bg-white text-black hover:bg-gray-100 hover:text-black dark:border-white dark:bg-white dark:text-black dark:hover:bg-gray-100 dark:hover:text-black"
          >
            Export
          </Button>
          <Button 
            size="sm" 
            iconLeft={<AddIcon fontSize="small" />}
          >
            New
          </Button>
        </div>
      </div>

      <div className="mt-6 h-px w-full bg-[var(--color-table-border)]" />
    </LayoutColumn>
  );
}
