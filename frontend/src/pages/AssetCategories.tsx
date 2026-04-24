import { LayoutColumn } from '../components/layout/Layout';
import type { AssetCategoryDto } from '../features/asset-category/types';
import { SearchInput } from '../components/ui/SearchBar';
import { useState, useEffect } from 'react';
import { Button } from '../components/ui/Button';
import AddSharpIcon from '@mui/icons-material/AddSharp';
import { AddCategoryModal } from '../features/asset-category/components/AddCategoryModal';
import { AssetCategoriesTable } from '../features/asset-category/components/AssetCategoriesTable';
import { getAllCategories, getCategoryById, updateCategory } from '../features/asset-category/api/categoryApi';
import { CategoryModal } from '../features/asset-category/components/CategoryModal';
import { EditCategoryModal } from '../features/asset-category/components/EditCategoryModal'

export default function AssetCategories() {
  const [search, setSearch] = useState('');
  const [openAddModal, setOpenAddModal] = useState(false);
  const [openViewModal, setOpenViewModal] = useState(false);
  const [openEditModal, setOpenEditModal] = useState(false);
  const [categories, setCategories] = useState<AssetCategoryDto[]>([])
  const [loading, setLoading] = useState(true)
  const [serverError, setServerError] = useState('')
  const [activeCategory, setActiveCategory] =
    useState<AssetCategoryDto | null>(null);


  useEffect(() => {
    const load = async () => {
      try {
        setLoading(true);
        setServerError('');

        const data = await getAllCategories();
        setCategories(data.content);
      } catch (err) {
        setServerError('Failed to load categories');
      } finally {
        setLoading(false);
      }
    };

    load();
  }, []);

  const filteredCategories = categories.filter((category) =>
    category.name.toLowerCase().includes(search.toLowerCase())
  );

  const handleView = async (category: AssetCategoryDto) => {
    setOpenViewModal(true);
    setActiveCategory(null);

    try {
      const fullCategory = await getCategoryById(category.id);
      setActiveCategory(fullCategory);
    } catch (err) {
      console.error(err);
    }

  };
  const handleEdit = async (category: AssetCategoryDto) => {
    setActiveCategory(category);
    setOpenEditModal(true);
    try {
      const fullCategory = await getCategoryById(category.id);
      setActiveCategory(fullCategory);
    } catch (err) {
      console.error(err);
    }
  };

  const handleSaveCategory = async (updatedCategory: AssetCategoryDto) => {
  try {
    await updateCategory(updatedCategory.id, updatedCategory);

    setCategories((prev) =>
      prev.map((category) =>
        category.id === updatedCategory.id ? updatedCategory : category
      )
    );

    setActiveCategory(updatedCategory);
    setOpenEditModal(false);
  } catch (err) {
    console.error('Failed to update category:', err);
  }
};

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
            onClick={() => setOpenAddModal(true)}
            className="mb-3 h-10 w-full font-bold sm:w-70"
            iconLeft={<AddSharpIcon />}
          >
            Add new category
          </Button>
          <AddCategoryModal
            open={openAddModal}
            onClose={() => setOpenAddModal(false)}
          />
        </div>

        {loading ? (
          <p className="text-sm text-gray-500">Loading categories...</p>
        ) : serverError ? (
          <p className="bottom-24 self-center text-center font-semibold text-red-500 p-5">
            {serverError}
          </p>
        ) : categories.length === 0 ? (
          <p className="text-sm text-gray-500">No categories found</p>
        ) : (
          <AssetCategoriesTable
            data={filteredCategories}
            onView={handleView}
            onEdit={handleEdit}
          />
        )}
        <CategoryModal
          isOpen={openViewModal}
          onClose={() => {
            setOpenViewModal(false);
            setActiveCategory(null);
          }}
          category={activeCategory}
        />
        <EditCategoryModal
          isOpen={openEditModal}
          onClose={() => {
            setOpenEditModal(false);
            setActiveCategory(null);
          }}
          category={activeCategory}
          onSave={handleSaveCategory}
        />
      </div>
    </LayoutColumn>
  );
}
