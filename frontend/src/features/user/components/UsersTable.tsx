import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import CalendarTodaySharpIcon from '@mui/icons-material/CalendarTodaySharp';
import { Button } from '../../../components/ui/Button';
import { IconButton } from '../../../components/ui/IconButton';
import { Table, type TableColumn } from '../../../components/ui/Table';
import type { UserDto } from '../types';
import { getDisplayName } from '../hooks/useUsers';

type Props = {
  data: UserDto[];
  emptyMessage?: React.ReactNode;
  nameSortDir: 'asc' | 'desc';
  onToggleNameSort: () => void;
  onView: (user: UserDto) => void;
  onEdit: (user: UserDto) => void;
  onBookings: (user: UserDto) => void;
  onDelete: (user: UserDto) => void;
};

export const UsersTable = ({
  data,
  emptyMessage,
  nameSortDir,
  onToggleNameSort,
  onView,
  onEdit,
  onBookings,
  onDelete,
}: Props) => {
  const columns: TableColumn<UserDto>[] = [
    {
      key: 'name',
      header: (
        <button
          type="button"
          onClick={onToggleNameSort}
          className="inline-flex cursor-pointer items-center gap-2 select-none hover:text-(--color-primaryblue)"
          aria-label={`Sort by last name ${nameSortDir === 'asc' ? 'descending' : 'ascending'
            }`}
        >
          <span>NAME</span>
          <span className="inline-flex flex-col leading-none" aria-hidden="true">
            <span className={nameSortDir === 'asc' ? 'opacity-100' : 'opacity-30'}>
              ▲
            </span>
            <span className={nameSortDir === 'desc' ? 'opacity-100' : 'opacity-30'}>
              ▼
            </span>
          </span>
        </button>
      ),
      cellClassName: 'font-medium',
      render: (user) => getDisplayName(user),
    },
    {
      key: 'email',
      header: 'Email',
      accessor: 'email',
    },
    {
      key: 'bookings',
      header: <span className="sr-only">Bookings</span>,
      headerClassName: 'w-px whitespace-nowrap',
      cellClassName: 'w-px whitespace-nowrap',
      render: (user) => (
        <Button
          size="sm"
          variant="solid"
          iconLeft={<CalendarTodaySharpIcon fontSize="small" />}
          className="shadow-none"
          onClick={() => onBookings(user)}
        >
          Bookings
        </Button>
      ),
    },
    {
      key: 'actions',
      header: <span className="sr-only">Actions</span>,
      cellClassName: 'w-px whitespace-nowrap',
      render: (user) => (
        <div className="flex items-center gap-1">
          <IconButton
            type="button"
            aria-label="View user"
            onClick={() => onView(user)}
          >
            <VisibilityOutlinedIcon
              fontSize="small"
              className="pointer-events-none"
            />
          </IconButton>
          <IconButton
            type="button"
            aria-label="Edit user"
            disabled={user.status === 'DELETED'}
            onClick={() => onEdit(user)}
          >
            <EditOutlinedIcon
              fontSize="small"
              className="pointer-events-none"
            />
          </IconButton>
          <IconButton
            type="button"
            variant="danger"
            aria-label="Delete user"
            disabled={user.status === 'DELETED'}
            onClick={() => onDelete(user)}
          >
            <DeleteOutlineIcon
              fontSize="small"
              className="pointer-events-none"
            />
          </IconButton>
        </div>
      ),
    },
  ];

  return (
    <Table
      data={data}
      columns={columns}
      getRowKey={(user) => String(user.id)}
      className="w-full"
      emptyMessage={emptyMessage}
    />
  );
};
