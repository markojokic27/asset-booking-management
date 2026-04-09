import { LayoutColumn } from '../components/layout/Layout';
import { Table, type TableColumn } from '../components/ui/Table';
import type { AssetCategoryDto } from '../features/asset-category/types';
import { SearchInput } from '../components/ui/SearchBar';
import { useState } from 'react';
import { Button } from '../components/ui/Button';
import AddSharpIcon from '@mui/icons-material/AddSharp';

const assetcategories: AssetCategoryDto[] = [];

export default function AssetCategories() {
  const [search, setSearch] = useState('');
  const columns: TableColumn<AssetCategoryDto>[] = [
    {
      key: 'id',
      header: 'ID',
      accessor: 'id',
      cellClassName: 'font-medium',
    },
    {
      key: 'name',
      header: ' Name',
      accessor: 'name',
    },
    {
      key: 'status',
      header: 'Status',
      //accessor: 'status',
    },
    {
      key: 'edit',
      header: 'Edit',
      render: () => 'Edit',
    },
    {
      key: 'delete',
      header: 'Delete',
      render: () => 'Delete',
    },
  ];
  return (
    <LayoutColumn span={12} mdSpan={9} mdOffset={3} className="flex pt-35">
      <div className="w-full">
        <div className="flex w-full items-start justify-between gap-4">
          <SearchInput
            value={search}
            onChange={setSearch}
            placeholder="Search category by name"
            className="w-70 flex-none"
          />
          <Button
            type="submit"
            className="h-10 w-70 flex-none font-bold"
            iconLeft={<AddSharpIcon />}
          >
            Add new category
          </Button>
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
