package de.bdr.asset.management.feature;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "feature")
public class FeatureConfig {

    private boolean assetNameValidationEnabled;

}
