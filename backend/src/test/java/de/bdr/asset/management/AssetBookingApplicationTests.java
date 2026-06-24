package de.bdr.asset.management;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(properties = {
        "JWT_SECRET=1111111111111111111111111111111111111111",
        "JWT_EXPIRY_SECONDS=3600",
        "JWT_REFRESH_SECONDS=86400",
        "BASE_URL=http://localhost:5173"
})
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
class AssetBookingApplicationTests extends AbstractIntegrationTest {

    @Test
    void contextLoads() {
    }
}