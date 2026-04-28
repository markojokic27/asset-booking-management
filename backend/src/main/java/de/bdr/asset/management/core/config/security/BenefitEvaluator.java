package de.bdr.asset.management.core.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Pattern;

@Component("benefitEvaluator")
@RequiredArgsConstructor

public class BenefitEvaluator {

    private final BenefitConfig benefitConfig;

    public boolean canBook(Authentication auth, String assetCategory) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        // Check authorities for ROLE_ADMIN
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) return true;

        // Check against YAML 'default-assets'
        if (benefitConfig.getDefaultBenefits().stream()
                .anyMatch(asset -> asset.equalsIgnoreCase(assetCategory))) {
            return true;
        }

        // Parse the user's benefit string from CustomUserDetails
        String benefitClaim = user.getBenefit();
        if (benefitClaim == null || benefitClaim.isEmpty()) {
            return false;
        }

        // Split by the delimiter (e.g., ";") and check for a match
        String[] userBenefits = benefitClaim.split(Pattern.quote(benefitConfig.getBenefitDelimiter()));

        return Arrays.stream(userBenefits)
                .anyMatch(b -> b.trim().equalsIgnoreCase(assetCategory));
    }
}
