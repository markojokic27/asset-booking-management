// external dependencies
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import CalendarTodaySharpIcon from '@mui/icons-material/CalendarTodaySharp';
import { useTranslation } from 'react-i18next';

// components
import { Button } from '../../../components/ui/Button';
import { IconButton } from '../../../components/ui/IconButton';
import { Table, type TableColumn } from '../../../components/ui/Table';

// types
import type { UserDto } from '../types';

// utils
import { getDisplayName } from '../utils/users';

type Props = {
  data: UserDto[];
  emptyMessage?: React.ReactNode;
  nameSortDir: 'asc' | 'desc';
  onToggleNameSort: () => void;
  onView: (user: UserDto) => void;
  onEdit: (user: UserDto) => void;
  onBookings: (user: UserDto) => void;
  onDelete: (user: UserDto) => void;
  onReport: (user: UserDto) => void;
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
  onReport: _onReport,
}: Props) => {
  
  const { t } = useTranslation();
  const nextSortDirKey = nameSortDir === 'asc' ? 'descending' : 'ascending';

  const columns: TableColumn<UserDto>[] = [
    {
      key: 'name',
      header: (
        <button
          type="button"
          onClick={onToggleNameSort}
          className="inline-flex cursor-pointer items-center gap-2 select-none hover:text-(--color-primaryblue)"
          aria-label={t('users.table.sort.byLastNameAria', {
            direction: t(`users.table.sort.direction.${nextSortDirKey}`),
          })}
        >
          <span className="uppercase">{t('users.table.columns.name')}</span>
          <span
            className="inline-flex flex-col leading-none"
            aria-hidden="true"
          >
            <span
              className={nameSortDir === 'asc' ? 'opacity-100' : 'opacity-30'}
            >
              ▲
            </span>
            <span
              className={nameSortDir === 'desc' ? 'opacity-100' : 'opacity-30'}
            >
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
      header: t('users.table.columns.email'),
      accessor: 'email',
    },
    {
      key: 'bookings',
      header: (
        <span className="sr-only">{t('users.table.columns.bookings')}</span>
      ),
      headerClassName: 'w-px whitespace-nowrap',
      cellClassName: 'w-px whitespace-nowrap',
      render: (user) => (
        <Button data-testid="user-bookings-button"
          size="sm"
          variant="solid"
          iconLeft={<CalendarTodaySharpIcon fontSize="small" />}
          className="shadow-none"
          onClick={() => onBookings(user)}
        >
          {t('users.table.bookingsCta')}
        </Button>
      ),
    },
    {
      key: 'actions',
      header: (
        <span className="sr-only">{t('users.table.columns.actions')}</span>
      ),
      cellClassName: 'w-px whitespace-nowrap',
      render: (user) => (
        <div className="flex items-center gap-1">
          <IconButton data-testid="view-user-button"
            type="button"
            aria-label={t('users.table.rowActions.viewAria')}
            onClick={() => onView(user)}
          >
            <VisibilityOutlinedIcon
              fontSize="small"
              className="pointer-events-none"
            />
          </IconButton>

          {/* <IconButton
            type="button"
            aria-label={t('users.table.rowActions.reportAria')}
            onClick={() => onReport(user)}
          >
            <BarChartIcon fontSize="small" className="pointer-events-none" />
          </IconButton> */}
          <IconButton
            data-testid="edit-user-button"
            type="button"
            aria-label={t('users.table.rowActions.editAria')}
            disabled={user.status === 'DELETED'}
            onClick={() => onEdit(user)}
          >
            <EditOutlinedIcon
              fontSize="small"
              className="pointer-events-none"
            />
          </IconButton>
          <IconButton
            data-testid="delete-user-button"
            type="button"
            variant="danger"
            aria-label={t('users.table.rowActions.deleteAria')}
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
      rowClassName={(user) =>
        user.status === 'DELETED'
          ? 'bg-slate-100 opacity-60 hover:bg-slate-200 dark:text-black dark:[&_td]:!text-black dark:[&_button]:!text-black dark:[&_button:hover]:text-(--color-primaryblue) dark:hover:bg-slate-200'
          : undefined
      }
      className="w-full"
      emptyMessage={emptyMessage}
    />
  );
};
