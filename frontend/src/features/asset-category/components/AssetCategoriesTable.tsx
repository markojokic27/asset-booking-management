import { Table, type TableColumn } from '../../../components/ui/Table'
import type { AssetCategoryDto } from '../types'
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined'
import EditOutlinedIcon from '@mui/icons-material/EditOutlined'
import { useTranslation } from 'react-i18next'

type Props = {
    data: AssetCategoryDto[]
    onView: (category: AssetCategoryDto) => void
    onEdit?: (category: AssetCategoryDto) => void
    onDelete?: (category: AssetCategoryDto) => void
}

export const AssetCategoriesTable = ({
    data,
    onView,
    onEdit,
}: Props) => {
    const { t } = useTranslation()

    const columns: TableColumn<AssetCategoryDto>[] = [
        {
            key: 'name',
            header: t('assetCategories.table.columns.name'),
            accessor: 'name',
            cellClassName: 'font-medium'
        },
        {
            key: 'description',
            header: t('assetCategories.table.columns.description'),
            accessor: 'description'
        },
        {
            key: 'bookingPeriod',
            header: t('assetCategories.table.columns.bookingPeriod'),
            accessor: 'bookingPeriod'
            ,
            render: (category) => {
                const label =
                    category.bookingPeriod === 'HOUR'
                        ? t('assetCategories.bookingPeriod.hour')
                        : category.bookingPeriod === 'DAY'
                            ? t('assetCategories.bookingPeriod.day')
                            : category.bookingPeriod === 'WEEK'
                                ? t('assetCategories.bookingPeriod.week')
                                : t('assetCategories.bookingPeriod.month')

                return <span>{label}</span>
            }
        },
        {
            key: 'actions',
            header: <span className="sr-only">{t('assetCategories.table.columns.actions')}</span>,
            cellClassName: 'w-px whitespace-nowrap',
            render: (category) => (
                <div className="flex items-center gap-1">

                    <button
                        className="p-1.5 hover:text-(--color-primaryblue)"
                        onClick={() => onView(category)}
                        aria-label={t('assetCategories.table.rowActions.viewAria')}
                    >
                        <VisibilityOutlinedIcon fontSize="small" />
                    </button>

                    <button
                        className="p-1.5 hover:text-(--color-primaryblue)"
                        onClick={() => onEdit?.(category)}
                        aria-label={t('assetCategories.table.rowActions.editAria')}
                    >
                        <EditOutlinedIcon fontSize="small" />
                    </button>

                </div>
            )
        }
    ]

    return (
        <Table
            data={data}
            columns={columns}
            getRowKey={(c) => c.id}
            className="w-full"
        />
    )
}