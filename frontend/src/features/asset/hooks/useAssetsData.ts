// External packages
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';

// API
import {
  getAllAssets,
  createAsset,
  updateAsset,
  deleteAsset,
} from '../api/assetApi';

// Types
import type { AssetDto } from '../types';

export function useAssetsData() {
  const { t } = useTranslation();
  const [assets, setAssets] = useState<AssetDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [deletingAssetId, setDeletingAssetId] = useState<number | null>(null);

  useEffect(() => {
    let mounted = true;

    (async () => {
      try {
        setLoading(true);

        const data = await getAllAssets();

        if (mounted) {
          setAssets(data.content);
        }
      } catch {
        if (mounted) {
          setError(t('assets.errors.loadAssets'));
        }
      } finally {
        if (mounted) {
          setLoading(false);
        }
      }
    })();

    return () => {
      mounted = false;
    };
  }, [t]);

  const update = async (asset: AssetDto) => {
    const dto = await updateAsset(asset.id, {
      name: asset.name,
      categoryId: asset.categoryId,
      status: asset.status,
      location: asset.location,
      description: asset.description,
    });

    setAssets((prev) =>
      prev.map((a) => (a.id === dto.id ? dto : a))
    );

    return dto;
  };

  const create = async (
    input: Parameters<typeof createAsset>[0]
  ) => {
    const dto = await createAsset(input);

    setAssets((prev) => [dto, ...prev]);

    return dto;
  };

  const remove = async (id: number) => {
    try {
      setDeletingAssetId(id);

      await deleteAsset(id);

      // Soft delete in UI
      setAssets((prev) =>
        prev.map((a) =>
          a.id === id
            ? { ...a, status: 'DELETED' as const }
            : a
        )
      );
    } finally {
      setDeletingAssetId(null);
    }
  };

  return {
    assets,
    loading,
    error,
    deletingAssetId,
    actions: {
      update,
      create,
      remove,
    },
  };
}