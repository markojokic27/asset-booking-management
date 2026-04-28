package de.bdr.asset.management.asset;

import lombok.Data;

@Data
public class AssetFilter {
    private String name;
    private Long categoryId;
    private AssetStatusEnum status;
    private String location;
}
