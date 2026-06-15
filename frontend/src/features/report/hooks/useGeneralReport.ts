import { useEffect, useState } from 'react';
import type { Filter, GeneralReportResponseDTO } from '../types';
import { getGeneralReport } from '../api/reportApi';

export function useGeneralReport(filters: Filter) {
  const [report, setReport] =
    useState<GeneralReportResponseDTO | null>(null);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<unknown>(null);

  const [debouncedFilters, setDebouncedFilters] =
    useState(filters);

    useEffect(() => {
    const timeout = setTimeout(() => {
        setDebouncedFilters(filters);
    }, 300);

    return () => clearTimeout(timeout);
    }, [filters]);

  useEffect(() => {
  let ignore = false;

  const loadReport = async () => {
    try {
      setLoading(true);
      setError(null);

      const data = await getGeneralReport(debouncedFilters);

      if (!ignore) {
        setReport(data);
      }
    } catch (err) {
      if (!ignore) {
        setError(err);
      }
    } finally {
      if (!ignore) {
        setLoading(false);
      }
    }
  };

  loadReport();

  return () => {
    ignore = true;
  };
}, [debouncedFilters]);

  return { report, loading, error };
}