import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import AddIcon from '@mui/icons-material/Add';
import FileDownloadOutlinedIcon from '@mui/icons-material/FileDownloadOutlined';
import { LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';
import { Table, type TableColumn } from '../components/ui/Table';

type UserRow = {
  id: string;
  name: string;
  email: string;
};

const users: UserRow[] = [
  {
    id: '1',
    name: 'Ana Horvat',
    email: 'ana.horvat@example.com',
  },
];

export default function Users() {
  const columns: TableColumn<UserRow>[] = [
    {
      key: 'name',
      header: 'Name',
      accessor: 'name',
      cellClassName: 'font-medium',
    },
    {
      key: 'email',
      header: 'Email',
      accessor: 'email',
    },
    {
      key: 'actions',
      header: <span className="sr-only">Actions</span>,
      cellClassName: 'w-px whitespace-nowrap',
      render: () => (
        <div className="flex items-center gap-1">
          <button
            type="button"
            className="inline-flex cursor-pointer items-center justify-center rounded p-1.5 text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover) hover:text-(--color-primaryblue) active:scale-95"
            aria-label="View user"
          >
            <VisibilityOutlinedIcon
              fontSize="small"
              className="pointer-events-none"
            />
          </button>
          <button
            type="button"
            className="inline-flex cursor-pointer items-center justify-center rounded p-1.5 text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover) hover:text-(--color-primaryblue) active:scale-95"
            aria-label="Edit user"
          >
            <EditOutlinedIcon
              fontSize="small"
              className="pointer-events-none"
            />
          </button>
          <button
            type="button"
            className="inline-flex cursor-pointer items-center justify-center rounded p-1.5 text-red-600 transition-colors hover:bg-(--color-table-row-hover) hover:text-red-700 active:scale-95 dark:text-red-400 dark:hover:text-red-300"
            aria-label="Delete user"
          >
            <DeleteOutlineIcon
              fontSize="small"
              className="pointer-events-none"
            />
          </button>
        </div>
      ),
    },
  ];

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
            className="shadow-none"
          >
            Export
          </Button>
          <Button
            size="sm"
            iconLeft={<AddIcon fontSize="small" />}
            className="shadow-none"
          >
            New
          </Button>
        </div>
      </div>

      <div className="mt-6 h-px w-full bg-(--color-table-border)" />
      <div className="mt-6 w-full">
        <Table
          data={users}
          columns={columns}
          getRowKey={(user) => user.id}
          className="w-full"
          emptyMessage="No users yet."
        />
      </div>
    </LayoutColumn>
  );
}
