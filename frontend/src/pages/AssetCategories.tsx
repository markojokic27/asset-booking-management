import { LayoutColumn } from '../components/layout/Layout';
import { Table, type TableColumn } from '../components/ui/Table';
import type { AssetCategoryDto } from '../features/asset-category/types';
import { SearchInput } from '../components/ui/SearchBar';
import { useState } from 'react';
import { Button } from '../components/ui/Button';
import AddSharpIcon from '@mui/icons-material/AddSharp';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import { AddCategoryModal } from '../features/asset-category/components/AddCategoryModal';

type assetcategories = {
  id: string;
  name: string;
  description: string;
  approval: boolean;
};

// TODO: Fetch all existing categories
const assetcategories: AssetCategoryDto[] = [
  {
    id: '1',
    name: 'Laptops',
    description: 'Test',
    approval: false,
    bookingPeriod: 'WEEK',
    createdAt: new Date(),
    lastModifiedAt: new Date(),
  },
];

export default function AssetCategories() {
  const [search, setSearch] = useState('');
  const [openModal, setOpenModal] = useState(false);
  const [_activeCategory, setActiveCategory] =
    useState<AssetCategoryDto | null>(null);
  const columns: TableColumn<AssetCategoryDto>[] = [
    {
      key: 'name',
      header: 'Name',
      accessor: 'name',
      cellClassName: 'font-medium',
    },
    {
      key: 'description',
      header: 'Description',
      accessor: 'description',
    },
    {
      key: 'bookingPeriod',
      header: 'Booking Period',
      accessor: 'bookingPeriod',
    },
    {
      key: 'actions',
      header: <span className="sr-only">Actions</span>,
      cellClassName: 'w-px whitespace-nowrap',
      render: (category) => (
        <div className="flex items-center gap-1">
          <button
            type="button"
            className="inline-flex cursor-pointer items-center justify-center rounded p-1.5 text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover) hover:text-(--color-primaryblue) active:scale-95"
            aria-label="View user"
            onClick={() => {
              setActiveCategory(category);
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
    <LayoutColumn span={12} mdSpan={9} mdOffset={3} className="flex pt-35">
      <div className="w-full">
        <h1 className="text-3xl leading-11 font-black tracking-[0.2em] text-black dark:text-white">
          Asset Categories
        </h1>
        <div className="mt-4 mb-8 h-px w-full bg-(--color-table-border)" />
        <div className="flex w-full flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <SearchInput
            value={search}
            onChange={setSearch}
            placeholder="Search category by name"
            className="w-full sm:w-70"
          />
          <Button
            type="submit"
            onClick={() => setOpenModal(true)}
            className="mb-3 h-10 w-full font-bold sm:w-70"
            iconLeft={<AddSharpIcon />}
          >
            Add new category
          </Button>
          <AddCategoryModal
            open={openModal}
            onClose={() => setOpenModal(false)}
          />
        </div>

        <Table
          data={assetcategories}
          columns={columns}
          getRowKey={(category) => category.id}
          className="w-full"
        />
      </div>
    </LayoutColumn>
  );
}
