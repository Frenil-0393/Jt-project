package com.library.lms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration smoke test – verifies the Spring application context loads.
 * Uses H2 in-memory database (configured in src/test/resources/application.properties).
 * The production application uses MySQL (src/main/resources/application.properties).
 */
@SpringBootTest
class LibraryManagementApplicationTests {

    @Test
    void contextLoads() {
        // Verifies that the Spring application context starts successfully
    }
}
