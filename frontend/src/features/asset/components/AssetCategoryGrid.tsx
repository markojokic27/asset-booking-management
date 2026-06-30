// External packages
import * as React from 'react';
// Components
import { AssetCategoryCard } from './AssetCategoryCard';

export type AssetCategoryGridProps = {
  categories: string[];
  selectedCategory: string;
  onSelectCategory: (category: string) => void;
  allCategory?: {
    label: string;
    value: string;
  };
};

export const AssetCategoryGrid: React.FC<AssetCategoryGridProps> = ({
  categories,
  selectedCategory,
  onSelectCategory,
  allCategory,
}) => {
  return (
    <div className="grid w-full grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
      {allCategory && (
        <AssetCategoryCard
          title={allCategory.label}
          isSelected={selectedCategory === allCategory.value}
          onClick={() => onSelectCategory(allCategory.value)}
          showBackgroundImage={false}
          data-testid="asset-category-card-all"
        />
      )}
      {categories.map((category) => (
  <AssetCategoryCard
    key={category}
    title={category}
    isSelected={selectedCategory === category}
    onClick={() => onSelectCategory(category)}
    data-testid={`category-card-${category.toLowerCase()}`}
  />
  ))}
    </div>
  );
};
