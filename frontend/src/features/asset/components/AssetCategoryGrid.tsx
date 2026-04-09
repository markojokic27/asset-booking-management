import * as React from 'react';
import { AssetCategoryCard } from './AssetCategoryCard';

export type AssetCategoryGridProps = {
  categories: readonly string[];
  selectedCategory: string;
  onSelectCategory: (category: string) => void;
};

export const AssetCategoryGrid: React.FC<AssetCategoryGridProps> = ({
  categories,
  selectedCategory,
  onSelectCategory,
}) => {
  return (
    <div className="grid w-full grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
      {categories.map((category) => (
        <AssetCategoryCard
          key={category}
          title={category}
          isSelected={selectedCategory === category}
          onClick={() => onSelectCategory(category)}
        />
      ))}
    </div>
  );
};
