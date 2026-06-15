package de.bdr.asset.management.report;

import java.time.Instant;

import lombok.Data;

@Data
public class ReportFilter {
    private Instant fromDate;
    private Instant toDate;
    
    private Long userId;
    private Long assetId;
}
