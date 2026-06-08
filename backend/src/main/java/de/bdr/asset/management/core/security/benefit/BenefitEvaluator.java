package de.bdr.asset.management.core.security.benefit;

import de.bdr.asset.management.asset.Asset;
import de.bdr.asset.management.asset.AssetRepository;
import de.bdr.asset.management.core.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component("benefitEvaluator")
@RequiredArgsConstructor
public class BenefitEvaluator {

    private final BenefitConfig benefitConfig;
    private final AssetRepository assetRepository;

    public boolean canBook(Authentication auth, Long assetId) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        // Admin Bypass
        if (auth.getAuthorities().stream().anyMatch(a -> ("ROLE_ADMIN").equals(a.getAuthority()))) {
            return true;
        }

        // Find the asset category
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
        String category = asset.getCategory().getName().toUpperCase().trim();

        // If it's a default asset, everyone can book it
        if (benefitConfig.getDefaultBenefits().stream().anyMatch(d -> d.equalsIgnoreCase(category))) {
            return true;
        }

        // If it's NOT a default (e.g., RECURRING_PARKING), check user string
        String benefitClaim = user.getBenefit();
        if (benefitClaim == null || benefitClaim.isBlank()) {
            return false;
        }

        // Split by ";" and check for exact match OR the "ALL" wildcard
        List<String> userBenefits = Arrays.stream(benefitClaim.split(Pattern.quote(benefitConfig.getBenefitDelimiter())))
                .map(String::trim)
                .map(String::toUpperCase)
                .toList();

        return userBenefits.contains(category);
    }
}
