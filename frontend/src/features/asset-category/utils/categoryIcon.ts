const FALLBACK_SVG = `<svg xmlns="http://www.w3.org/2000/svg" width="256" height="256" viewBox="0 0 256 256">
  <defs>
    <linearGradient id="g" x1="0" y1="0" x2="1" y2="1">
      <stop offset="0" stop-color="#e5e7eb"/>
      <stop offset="1" stop-color="#cbd5e1"/>
    </linearGradient>
  </defs>
  <rect x="0" y="0" width="256" height="256" rx="24" fill="url(#g)"/>
  <path d="M72 172c14-20 30-30 48-30s34 10 48 30" fill="none" stroke="#94a3b8" stroke-width="12" stroke-linecap="round"/>
  <circle cx="96" cy="104" r="10" fill="#94a3b8"/>
  <circle cx="160" cy="104" r="10" fill="#94a3b8"/>
</svg>`;

export const CATEGORY_ICON_DEFAULT_SRC = '/category-icons/default.png';

export const CATEGORY_ICON_FALLBACK_SRC =
  `data:image/svg+xml,${encodeURIComponent(FALLBACK_SVG)}`;

export function categoryNameToIconSlug(name: string): string {
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

