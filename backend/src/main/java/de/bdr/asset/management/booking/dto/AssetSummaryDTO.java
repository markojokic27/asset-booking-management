package de.bdr.asset.management.booking.dto;

import de.bdr.asset.management.asset.AssetStatusEnum;

public record AssetSummaryDTO(
    Long id,
    String name,
    CategorySummaryDTO category,
    AssetStatusEnum status,
    String description,
    String location
) {}
