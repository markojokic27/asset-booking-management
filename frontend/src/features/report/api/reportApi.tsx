import type {
    Filter,
    GeneralReportResponseDTO
} from "../types";

// API
import api from '../../../shared/api';

const urlEndpoint: string = "/reports";

export const getGeneralReport = async (
    filter: Partial<Filter>
): Promise<GeneralReportResponseDTO> => {
    const params: Record<string, string | number> = {};

    if (filter.fromDate) {
        params.fromDate = filter.fromDate;
    }

    if (filter.toDate) {
        params.toDate = filter.toDate;
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