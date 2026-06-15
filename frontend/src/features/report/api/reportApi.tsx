import type {
    Filter,
    GeneralReportResponseDTO
} from "../types";

// API
import api from '../../../shared/api';

const urlEndpoint: string = "/reports";

const fromDateFormatted = (date: string): string => {
  return `${date}T00:00:00Z`;
};

const toDateFormatted = (date: string): string => {
  return `${date}T23:59:59Z`;
};

export const getGeneralReport = async (
    filter: Partial<Filter>
): Promise<GeneralReportResponseDTO> => {
    const params: Record<string, string | number> = {};

    if (filter.fromDate) {
        params.fromDate = fromDateFormatted(filter.fromDate);
    }

    if (filter.toDate) {
        params.toDate = toDateFormatted(filter.toDate);
    }

    if (filter.userId) {
        params.userId = filter.userId;
    }

    if (filter.assetId) {
        params.assetId = filter.assetId;
    }

    const res = await api.get<GeneralReportResponseDTO>(urlEndpoint, {
        params
    });

    return res.data;
}