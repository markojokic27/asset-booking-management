// External packages
import { useState, useEffect, useMemo } from 'react';
import { Navigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

// Components
import { LayoutColumn } from '../components/layout/Layout';
import { SearchInput } from '../components/ui/SearchBar';
import { Pagination } from '../components/ui/Pagination';
import { Button } from '../components/ui/Button';
import { CategoryFormModal } from '../features/asset-category/components/CategoryFormModal';
import { CategoryModal } from '../features/asset-category/components/CategoryModal';
import { AssetCategoriesTable } from '../features/asset-category/components/AssetCategoriesTable';

// Types
import type { AssetCategoryDto } from '../features/asset-category/types';

// API
import {
  createCategory,
  getAllCategories,
  getCategoryById,
  updateCategory,
} from '../features/asset-category/api/categoryApi';

// Hooks
import { useAuth } from '../features/auth/context/AuthContext';
import { usePagination } from '../features/user/hooks/usePagination';
import { isAdmin, isEmployee } from '../features/user/utils/users';

// Assets
import AddSharpIcon from '@mui/icons-material/AddSharp';

export default function AssetCategories() {
  const { user, isLoading } = useAuth();

  if (!isLoading && isEmployee(user)) {
    return <Navigate to="/bookings" replace />;
  }

  return <AssetCategoriesPage />;
}

function AssetCategoriesPage() {
  const { t } = useTranslation();
  const { user } = useAuth();

  const [search, setSearch] = useState('');
  const [nameSortDir, setNameSortDir] = useState<'asc' | 'desc'>('asc');
  const [formModalMode, setFormModalMode] = useState<'none' | 'create' | 'edit'>('none');
  const [openViewModal, setOpenViewModal] = useState(false);
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

  const collator = useMemo(
    () => new Intl.Collator('hr', { sensitivity: 'base' }),
    []
  );

  const filteredCategories = useMemo(
    () =>
      categories.filter((category) =>
        category.name.toLowerCase().includes(search.toLowerCase())
      ),
    [categories, search]
  );

  const sortedCategories = useMemo(() => {
    const dir = nameSortDir === 'asc' ? 1 : -1;

    return [...filteredCategories].sort(
      (a, b) => collator.compare(a.name, b.name) * dir
    );
  }, [filteredCategories, nameSortDir, collator]);

  const pagination = usePagination(sortedCategories, 10);

  useEffect(() => {
    pagination.setPage(1);
  }, [search, nameSortDir]);

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
    setFormModalMode('edit');
    try {
      const fullCategory = await getCategoryById(category.id);
      setActiveCategory(fullCategory);
    } catch (err) {
      console.error(err);
    }
  };

  const closeFormModal = () => {
    setFormModalMode('none');
    setActiveCategory(null);
  };

  const handleCreateCategory = async (
    data: Parameters<typeof createCategory>[0]
  ) => {
    const created = await createCategory(data);
    setCategories((prev) => [created, ...prev]);
  };

  const handleSaveCategory = async (updatedCategory: AssetCategoryDto) => {
    await updateCategory(updatedCategory.id, updatedCategory);
    setCategories((prev) =>
      prev.map((category) =>
        category.id === updatedCategory.id ? updatedCategory : category
      )
    );
  };

  const editProps = isAdmin(user) ? { onEdit: handleEdit } : {};

  let categoriesContent;
  if (loading) {
    categoriesContent = (
      <p className="text-sm text-gray-500">
        {t('assetCategories.empty.loading')}
      </p>
    );
  } else if (serverError) {
    categoriesContent = (
      <p className="bottom-24 self-center text-center font-semibold text-red-500 p-5">
        {serverError}
      </p>
    );
  } else if (categories.length === 0) {
    categoriesContent = (
      <p className="text-sm text-gray-500">
        {t('assetCategories.empty.none')}
      </p>
    );
  } else {
    categoriesContent = (
      <AssetCategoriesTable
        data={pagination.paged}
        nameSortDir={nameSortDir}
        onToggleNameSort={() =>
          setNameSortDir((d) => (d === 'asc' ? 'desc' : 'asc'))
        }
        onView={handleView}
        {...editProps}
      />
    );
  }

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

          {isAdmin(user) && (
            <Button
              data-testid="add-category-button"
              type="button"
              size="sm"
              onClick={() => setFormModalMode('create')}
              iconLeft={<AddSharpIcon fontSize="small" />}
            >
              {t('assetCategories.actions.new')}
            </Button>
          )}
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

        <div className="mt-6 w-full">{categoriesContent}</div>

        {sortedCategories.length > 0 && !loading && !serverError && (
          <Pagination
            page={pagination.page}
            totalPages={pagination.totalPages}
            items={pagination.items}
            onPageChange={pagination.setPage}
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
        {isAdmin(user) && (
          <CategoryFormModal
            isOpen={formModalMode !== 'none'}
            mode={formModalMode === 'create' ? 'create' : 'edit'}
            category={activeCategory}
            onClose={closeFormModal}
            onCreate={handleCreateCategory}
            onSave={handleSaveCategory}
          />
        )}
      </div>
    </LayoutColumn>
  );
}
