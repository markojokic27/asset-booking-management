export const CATEGORY_ICON_DEFAULT_SRC = '/category-icons/default.png';

export function categoryNameToIconSlug(name: string): string {
  if (name.length > 200) throw new Error('Input too long');
  return name
    .trim()
    .toLowerCase()
    .normalize('NFD')
    // strip diacritics (č ć ž š đ …) -> (c c z s d …)
    .replace(/\p{Diacritic}/gu, '')
    .replace(/&/g, ' and ')
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/^-+|-+$/g, '');
}

export function getCategoryIconSrc(name: string): string {
  const slug = categoryNameToIconSlug(name);
  // public/ is served at /
  return slug ? `/category-icons/${slug}.png` : CATEGORY_ICON_DEFAULT_SRC;
}
