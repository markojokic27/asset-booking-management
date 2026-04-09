import { useEffect, useMemo, useState } from 'react';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import AddIcon from '@mui/icons-material/Add';
import FileDownloadOutlinedIcon from '@mui/icons-material/FileDownloadOutlined';
import CalendarTodaySharpIcon from '@mui/icons-material/CalendarTodaySharp';
import { useNavigate } from 'react-router-dom';
import { LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';
import { Table, type TableColumn } from '../components/ui/Table';
import { SearchInput } from '../components/ui/SearchBar';
import { UserModal } from '../features/user/components/UserModal';

type UserRow = {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
};

const users: UserRow[] = [
  {
    id: '1',
    firstName: 'Ana',
    lastName: 'Horvat',
    email: 'ana.horvat@example.com',
  },
  {
    id: '2',
    firstName: 'Ante',
    lastName: 'Anić',
    email: 'ante.anic@example.com',
  },
  {
    id: '3',
    firstName: 'Anica',
    lastName: 'Barišić',
    email: 'anica.barisic@example.com',
  },
];

function getFullName(user: Pick<UserRow, 'firstName' | 'lastName'>) {
  return `${user.firstName} ${user.lastName}`.trim();
}

function getDisplayName(user: Pick<UserRow, 'firstName' | 'lastName'>) {
  return `${user.lastName} ${user.firstName}`.trim();
}

export default function Users() {
  const [search, setSearch] = useState('');
  const [isUserModalOpen, setIsUserModalOpen] = useState(false);
  const [activeUser, setActiveUser] = useState<UserRow | null>(null);
  const [page, setPage] = useState(1);
  const [nameSortDir, setNameSortDir] = useState<'asc' | 'desc'>('asc');
  const navigate = useNavigate();

  const filteredUsers = useMemo(() => {
    const q = search.trim().toLowerCase();
    const base = !q
      ? users
      : users.filter(
      (u) =>
            u.firstName.toLowerCase().includes(q) ||
            u.lastName.toLowerCase().includes(q) ||
            getFullName(u).toLowerCase().includes(q) ||
            u.email.toLowerCase().includes(q)
        );

    const collator = new Intl.Collator('hr', { sensitivity: 'base' });
    const dir = nameSortDir === 'asc' ? 1 : -1;
    return [...base].sort((a, b) => {
      const lastCmp = collator.compare(a.lastName, b.lastName);
      if (lastCmp !== 0) return lastCmp * dir;
      const firstCmp = collator.compare(a.firstName, b.firstName);
      if (firstCmp !== 0) return firstCmp * dir;
      return collator.compare(a.email, b.email) * dir;
    });
  }, [search, nameSortDir]);

  useEffect(() => {
    setPage(1);
  }, [search]);

  const totalPages = 9;
  const safePage = Math.min(page, totalPages);
  const paginationItems: Array<number | 'ellipsis'> = [
    1,
    2,
    3,
    4,
    'ellipsis',
    8,
    9,
  ];

  const columns: TableColumn<UserRow>[] = [
    {
      key: 'name',
      header: (
        <button
          type="button"
          onClick={() => setNameSortDir((d) => (d === 'asc' ? 'desc' : 'asc'))}
          className="inline-flex cursor-pointer items-center gap-2 select-none hover:text-(--color-primaryblue)"
          aria-label={`Sort by last name ${
            nameSortDir === 'asc' ? 'descending' : 'ascending'
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
      render: () => (
        <Button
          size="sm"
          variant="solid"
          iconLeft={<CalendarTodaySharpIcon fontSize="small" />}
          className="shadow-none"
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
          <button
            type="button"
            className="inline-flex cursor-pointer items-center justify-center rounded p-1.5 text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover) hover:text-(--color-primaryblue) active:scale-95"
            aria-label="View user"
            onClick={() => {
              setActiveUser(user);
              setIsUserModalOpen(true);
            }}
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
      <div className="mt-6 flex w-full justify-end">
        <SearchInput
          value={search}
          onChange={setSearch}
          placeholder="Search users..."
          className="mb-0 w-70"
        />
      </div>
      <div className="mt-6 w-full">
        <Table
          data={filteredUsers}
          columns={columns}
          getRowKey={(user) => user.id}
          className="w-full"
          emptyMessage="No users yet."
        />
      </div>

      {filteredUsers.length > 0 && (
        <nav
          className="mt-5 flex w-full items-center justify-center"
          aria-label="Pagination"
        >
          <div className="flex items-center gap-2 text-sm text-(--color-table-text)">
            <button
              type="button"
              onClick={() => setPage((p) => Math.max(1, p - 1))}
              disabled={safePage <= 1}
              className="inline-flex cursor-pointer items-center gap-2 rounded px-2 py-1 transition-colors hover:bg-(--color-table-row-hover) disabled:cursor-not-allowed disabled:opacity-50"
            >
              <span aria-hidden="true">‹</span>
              <span>Previous</span>
            </button>

            <div className="flex items-center gap-2">
              {paginationItems.map((item, idx) => {
                if (item === 'ellipsis') {
                  return (
                    <span
                      key={`ellipsis-${idx}`}
                      className="text-(--color-table-text) select-none"
                      aria-hidden="true"
                    >
                      …
                    </span>
                  );
                }

                const isActive = item === safePage;
                return (
                  <button
                    key={item}
                    type="button"
                    onClick={() => setPage(item)}
                    aria-current={isActive ? 'page' : undefined}
                    className={[
                      'inline-flex h-6 w-6 cursor-pointer items-center justify-center rounded border text-xs transition-colors',
                      isActive
                        ? 'border-(--color-table-border) bg-(--color-table-row-hover)'
                        : 'border-transparent hover:bg-(--color-table-row-hover)',
                    ].join(' ')}
                  >
                    {item}
                  </button>
                );
              })}
            </div>

            <button
              type="button"
              onClick={() => setPage((p) => Math.min(totalPages, p + 1))}
              disabled={safePage >= totalPages}
              className="inline-flex cursor-pointer items-center gap-2 rounded px-2 py-1 transition-colors hover:bg-(--color-table-row-hover) disabled:cursor-not-allowed disabled:opacity-50"
            >
              <span>Next</span>
              <span aria-hidden="true">›</span>
            </button>
          </div>
        </nav>
      )}

      <UserModal
        isOpen={isUserModalOpen}
        onClose={() => {
          setIsUserModalOpen(false);
          setActiveUser(null);
        }}
        user={activeUser ? { ...activeUser, name: getFullName(activeUser) } : null}
      />
    </LayoutColumn>
  );
}
