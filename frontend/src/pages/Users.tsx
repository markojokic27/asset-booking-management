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
        <h1 className="text-3xl leading-11 font-black tracking-[0.2em] text-black dark:text-white">
          Users
        </h1>

        <div className="flex items-center gap-4">
          <Button
            size="sm"
            variant="outline"
            iconLeft={<FileDownloadOutlinedIcon fontSize="small" />}
            className="uppercase shadow-none"
          >
            Export
          </Button>
          <Button 
            size="sm" 
            iconLeft={<AddIcon fontSize="small" />}
            className="uppercase shadow-none"
          >
            New
          </Button>
        </div>
      </div>

      <div className="mt-6 h-px w-full bg-(--color-table-border)" />
    </LayoutColumn>
  );
}
