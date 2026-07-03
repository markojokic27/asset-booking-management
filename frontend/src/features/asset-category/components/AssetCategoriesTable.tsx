import { Table, type TableColumn } from '../../../components/ui/Table';
import type { AssetCategoryDto, BookingPeriod } from '../types';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import { useTranslation } from 'react-i18next';

type Props = {
  data: AssetCategoryDto[];
  nameSortDir: 'asc' | 'desc';
  onToggleNameSort: () => void;
  onView: (category: AssetCategoryDto) => void;
  onEdit?: (category: AssetCategoryDto) => void;
};

export const AssetCategoriesTable = ({
  data,
  nameSortDir,
  onToggleNameSort,
  onView,
  onEdit,
}: Props) => {
  const { t } = useTranslation();
  const nextSortDirKey = nameSortDir === 'asc' ? 'descending' : 'ascending';

  const columns: TableColumn<AssetCategoryDto>[] = [
    {
      key: 'name',
      header: (
        <button
          type="button"
          onClick={onToggleNameSort}
          className="inline-flex cursor-pointer items-center gap-2 select-none hover:text-(--color-primaryblue)"
          aria-label={t('assetCategories.table.sort.byNameAria', {
            direction: t(
              `assetCategories.table.sort.direction.${nextSortDirKey}`
            ),
          })}
        >
          <span className="uppercase">
            {t('assetCategories.table.columns.name')}
          </span>
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
      accessor: 'name',
      cellClassName: 'font-medium',
    },
    {
      key: 'description',
      header: t('assetCategories.table.columns.description'),
      accessor: 'description',
    },
    {
      key: 'bookingPeriod',
      header: t('assetCategories.table.columns.bookingPeriod'),
      accessor: 'bookingPeriod',
      render: (category) => {
        const bookingPeriodLabelKeys = {
          HOUR: 'assetCategories.bookingPeriod.hour',
          DAY: 'assetCategories.bookingPeriod.day',
        } as const satisfies Record<BookingPeriod, string>;

        return <span>{t(bookingPeriodLabelKeys[category.bookingPeriod])}</span>;
      },
    },
    {
      key: 'actions',
      header: (
        <span className="sr-only">
          {t('assetCategories.table.columns.actions')}
        </span>
      ),
      cellClassName: 'w-px whitespace-nowrap',
      render: (category) => (
        <div className="flex items-center gap-1">
          <button
            data-testid="view-assetCategory-button"
            className="p-1.5 hover:cursor-pointer hover:text-(--color-primaryblue)"
            onClick={() => onView(category)}
            aria-label={t('assetCategories.table.rowActions.viewAria')}
          >
            <VisibilityOutlinedIcon fontSize="small" />
          </button>

          {/* if the user is an admin, show the edit button */}
          {onEdit && (
            <button
              data-testid="edit-assetCategory-button"
              className="p-1.5 hover:cursor-pointer hover:text-(--color-primaryblue)"
              onClick={() => onEdit(category)}
              aria-label={t('assetCategories.table.rowActions.editAria')}
            >
              <EditOutlinedIcon fontSize="small" />
            </button>
          )}
        </div>
      ),
    },
  ];

  return (
    <Table
      data={data}
      columns={columns}
      getRowKey={(c) => c.id}
      className="mt-6 w-full"
    />
  );
};
