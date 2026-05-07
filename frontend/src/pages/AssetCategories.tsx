// External packages
import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';

// Components
import { LayoutColumn } from '../components/layout/Layout';
import { SearchInput } from '../components/ui/SearchBar';
import { Button } from '../components/ui/Button';
import { AddCategoryModal } from '../features/asset-category/components/AddCategoryModal';
import { EditCategoryModal } from '../features/asset-category/components/EditCategoryModal';
import { CategoryModal } from '../features/asset-category/components/CategoryModal';
import { AssetCategoriesTable } from '../features/asset-category/components/AssetCategoriesTable';

// Types
import type { AssetCategoryDto } from '../features/asset-category/types';

// API
import { getAllCategories, getCategoryById, updateCategory } from '../features/asset-category/api/categoryApi';

// Assets
import AddSharpIcon from '@mui/icons-material/AddSharp';

export default function AssetCategories() {
  const { t } = useTranslation();

  const [search, setSearch] = useState('');
  const [openAddModal, setOpenAddModal] = useState(false);
  const [openViewModal, setOpenViewModal] = useState(false);
  const [openEditModal, setOpenEditModal] = useState(false);
  const [categories, setCategories] = useState<AssetCategoryDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [serverError, setServerError] = useState('');
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
        setServerError(t('assetCategories.errors.loadFailed'));
      } finally {
        setLoading(false);
      }
    };

    load();
  }, [t]);

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
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      <div className="w-full">
        <div className="flex w-full flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <h1 className="text-3xl leading-11 font-black tracking-[0.2em] text-black dark:text-white">
            {t('assetCategories.title')}
          </h1>

          <Button
            type="button"
            size="sm"
            onClick={() => setOpenAddModal(true)}
            iconLeft={<AddSharpIcon fontSize="small" />}
          >
            {t('assetCategories.actions.new')}
          </Button>
        </div>

        <div className="mt-6 h-px w-full bg-(--color-table-border)" />

        <div className="mt-6 flex w-full flex-wrap items-end gap-3">
          <SearchInput
            value={search}
            onChange={setSearch}
            placeholder={t('assetCategories.search.placeholder')}
            className="mb-0 w-full sm:ml-auto sm:w-70"
          />
        </div>

        <div className="mt-6 w-full">
          {loading ? (
            <p className="text-sm text-gray-500">
              {t('assetCategories.empty.loading')}
            </p>
          ) : serverError ? (
            <p className="bottom-24 self-center text-center font-semibold text-red-500 p-5">
              {serverError}
            </p>
          ) : categories.length === 0 ? (
            <p className="text-sm text-gray-500">
              {t('assetCategories.empty.none')}
            </p>
          ) : (
            <AssetCategoriesTable
              data={filteredCategories}
              onView={handleView}
              onEdit={handleEdit}
            />
          )}
        </div>
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

        <AddCategoryModal
          open={openAddModal}
          onClose={() => setOpenAddModal(false)}
        />
      </div>
    </LayoutColumn>
  );
}
