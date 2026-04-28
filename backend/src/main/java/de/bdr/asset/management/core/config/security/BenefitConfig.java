package de.bdr.asset.management.core.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "benefit")
public class BenefitConfig {
    private List<String> defaultBenefits;
    private String benefitDelimiter;
}
