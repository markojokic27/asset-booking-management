export const CATEGORY_ICON_DEFAULT_SRC = '/category-icons/default.png';

const MAX_EXPANDED = 2048;

const RE_NON_ALNUM_RUN = new RegExp(`[^a-z0-9]{1,${MAX_EXPANDED}}`, 'g');
const RE_LEADING_HYPHENS = new RegExp(`^-{1,${MAX_EXPANDED}}`);
const RE_TRAILING_HYPHENS = new RegExp(`-{1,${MAX_EXPANDED}}$`);

export function categoryNameToIconSlug(name: string): string {
  if (name.length > 200) throw new Error('Input too long');
  return (
    name
      .trim()
      .toLowerCase()
      .normalize('NFD')
      // strip diacritics (č ć ž š đ …) -> (c c z s d …)
      .replaceAll(/\p{Diacritic}/gu, '')
      .replaceAll('&', ' and ')
      .replaceAll(RE_NON_ALNUM_RUN, '-')
      .replace(RE_LEADING_HYPHENS, '')
      .replace(RE_TRAILING_HYPHENS, '')
  );
}

export function getCategoryIconSrc(name: string): string {
  const slug = categoryNameToIconSlug(name);
  // public/ is served at /
  return slug ? `/category-icons/${slug}.png` : CATEGORY_ICON_DEFAULT_SRC;
}
