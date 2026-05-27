import { describe, it, expect } from 'vitest';
import { renderHook, act } from '@testing-library/react';
import { usePagination } from '../../features/user/hooks/usePagination';

const makeData = (length: number) => Array.from({ length }, (_, i) => i + 1);

// Runs the hook with given data and pageSize
const setup = (length: number, pageSize?: number) =>
  renderHook(({ data }) => usePagination(data, pageSize), {
    initialProps: { data: makeData(length) },
  });

describe('usePagination', () => {


  it('starts on page 1 with correct pageSize and totalPages', () => {
    const { result } = setup(20, 5);
    expect(result.current.page).toBe(1);
    expect(result.current.pageSize).toBe(5);
    expect(result.current.totalPages).toBe(4);
  });

  it('totalPages is at least 1 for empty data', () => {
    const { result } = renderHook(() => usePagination([]));
    expect(result.current.totalPages).toBe(1);
  });


  it('returns correct slice per page', () => {
    const { result } = setup(20, 5);
    expect(result.current.paged).toEqual([1, 2, 3, 4, 5]);

    act(() => result.current.setPage(2));
    expect(result.current.paged).toEqual([6, 7, 8, 9, 10]);

    act(() => result.current.setPage(3));
    expect(result.current.paged).toEqual([11, 12, 13, 14, 15]);
  });

  it('returns partial last page', () => {
    const { result } = setup(11, 5);
    act(() => result.current.setPage(3));
    expect(result.current.paged).toEqual([11]);
  });

  it('returns all data if less than pageSize', () => {
    const { result } = setup(3, 10);
    expect(result.current.paged).toEqual([1, 2, 3]);
  });

  it('returns empty array for empty data', () => {
    const { result } = renderHook(() => usePagination([]));
    expect(result.current.paged).toEqual([]);
  });


  it('setPage updates current page', () => {
    const { result } = setup(20, 5);
    act(() => result.current.setPage(3));
    expect(result.current.page).toBe(3);
  });

  it('resets to totalPages if page exceeds it after data shrinks', () => {
    const { result, rerender } = renderHook(
      ({ data }) => usePagination(data, 5),
      { initialProps: { data: makeData(20) } }
    );

    act(() => result.current.setPage(4));
    rerender({ data: makeData(10) });
    expect(result.current.page).toBe(2);
  });


  it('returns all page numbers when totalPages <= 7', () => {
    const { result } = setup(35, 5);
    expect(result.current.items).toEqual([1, 2, 3, 4, 5, 6, 7]);
  });

  it('always includes first and last page in items', () => {
    const { result } = setup(100, 10);
    act(() => result.current.setPage(5));
    expect(result.current.items[0]).toBe(1);
    expect(result.current.items[result.current.items.length - 1]).toBe(10);
  });

  it('adds ellipsis after first page when far from start', () => {
    const { result } = setup(100, 10);
    act(() => result.current.setPage(8));
    expect(result.current.items[0]).toBe(1);
    expect(result.current.items[1]).toBe('ellipsis');
  });

  it('adds ellipsis before last page when far from end', () => {
    const { result } = setup(100, 10);
    act(() => result.current.setPage(3));
    const items = result.current.items;
    expect(items[items.length - 1]).toBe(10);
    expect(items[items.length - 2]).toBe('ellipsis');
  });

  it('no ellipsis when on last page', () => {
    const { result } = setup(100, 10);
    act(() => result.current.setPage(10));
    expect(result.current.items).toEqual([1, 'ellipsis', 9, 10]);
  });

});