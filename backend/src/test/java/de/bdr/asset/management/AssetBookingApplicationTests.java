package de.bdr.asset.management;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
    "JWT_SECRET=1111111111111111111111111111111111111111",
    "JWT_EXPIRY_SECONDS=3600",
    "JWT_REFRESH_SECONDS=86400"
})
@ActiveProfiles("test")
class AssetBookingApplicationTests {

    @Test
    void contextLoads() {
    }
}