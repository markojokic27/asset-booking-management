package de.bdr.asset.management.booking.dto;

import de.bdr.asset.management.asset.AssetStatusEnum;

public record AssetSummaryDTO(
    String name,
    String category,
    AssetStatusEnum status
) {}
