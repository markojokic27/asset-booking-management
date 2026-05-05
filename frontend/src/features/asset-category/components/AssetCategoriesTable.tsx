import { Table, type TableColumn } from '../../../components/ui/Table'
import type { AssetCategoryDto } from '../types'
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined'
import EditOutlinedIcon from '@mui/icons-material/EditOutlined'

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
    const columns: TableColumn<AssetCategoryDto>[] = [
        {
            key: 'name',
            header: 'Name',
            accessor: 'name',
            cellClassName: 'font-medium'
        },
        {
            key: 'description',
            header: 'Description',
            accessor: 'description'
        },
        {
            key: 'bookingPeriod',
            header: 'Booking Period',
            accessor: 'bookingPeriod'
        },
        {
            key: 'actions',
            header: <span className="sr-only">Actions</span>,
            cellClassName: 'w-px whitespace-nowrap',
            render: (category) => (
                <div className="flex items-center gap-1">

                    <button
                        className="p-1.5 hover:text-(--color-primaryblue)"
                        onClick={() => onView(category)}
                    >
                        <VisibilityOutlinedIcon fontSize="small" />
                    </button>

                    <button
                        className="p-1.5 hover:text-(--color-primaryblue)"
                        onClick={() => onEdit?.(category)}
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
            className="w-full mt-6"
        />
    )
}