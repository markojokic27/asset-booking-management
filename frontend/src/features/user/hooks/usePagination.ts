// External packages
import { useEffect, useMemo, useState } from 'react';

export function usePagination<T>(data: T[], pageSize = 10) {
  const [page, setPage] = useState(1);

  const totalPages = Math.max(1, Math.ceil(data.length / pageSize));

  useEffect(() => {
    if (page > totalPages) setPage(totalPages);
  }, [page, totalPages]);

  const paged = useMemo(() => {
    const start = (page - 1) * pageSize;
    return data.slice(start, start + pageSize);
  }, [data, page, pageSize]);

  const items: Array<number | 'ellipsis'> = useMemo(() => {
    if (totalPages <= 7)
      return Array.from({ length: totalPages }, (_, i) => i + 1);

    const result: Array<number | 'ellipsis'> = [1];
    const left = Math.max(2, page - 1);
    const right = Math.min(totalPages - 1, page + 1);

    if (left > 2) result.push('ellipsis');
    for (let i = left; i <= right; i++) result.push(i);
    if (right < totalPages - 1) result.push('ellipsis');

    result.push(totalPages);
    return result;
  }, [page, totalPages]);

  return {
    page,
    setPage,
    totalPages,
    pageSize,
    paged,
    items,
  };
}
