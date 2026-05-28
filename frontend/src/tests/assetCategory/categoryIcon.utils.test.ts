import { describe, it, expect } from 'vitest';
import { categoryNameToIconSlug, getCategoryIconSrc, CATEGORY_ICON_DEFAULT_SRC } from '../../features/asset-category/utils/categoryIcon';

describe('categoryNameToIconSlug', () => {
  it.each([
    ['Laptop',        'laptop'],
    ['Book',          'book'],
    ['Desk',          'desk'],
    ['Meeting room',  'meeting-room'],
    ['IT equipment',  'it-equipment'],
    ['Parking',       'parking'],
    ['LAPTOPS',       'laptops'],
    ['Mice & Keyboards', 'mice-and-keyboards'],
    ['Meeting Room!', 'meeting-room'],
    ['  -Desk- ',     'desk'],
    ['',              ''],
  ])('slugifies "%s" → "%s"', (input, expected) => {
    expect(categoryNameToIconSlug(input)).toBe(expected);
  });

  it('throws for input longer than 200 characters', () => {
    expect(() => categoryNameToIconSlug('a'.repeat(201))).toThrow('Input too long');
  });

  it('handles exactly 200 characters without throwing', () => {
    expect(() => categoryNameToIconSlug('a'.repeat(200))).not.toThrow();
  });
});

describe('getCategoryIconSrc', () => {
  it('returns the correct icon path', () => {
    expect(getCategoryIconSrc('Laptop')).toBe('/category-icons/laptop.png');
  });

  it('returns the default icon when slug is empty', () => {
    expect(getCategoryIconSrc('   ')).toBe(CATEGORY_ICON_DEFAULT_SRC);
  });
});