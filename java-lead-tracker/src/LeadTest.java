/**
 * Standalone unit test suite for Lead and priority calculation.
 * Runs directly on standard Java without requiring JUnit or third-party frameworks.
 */
public class LeadTest {
    private static int testsPassed = 0;
    private static int testsTotal = 0;

    public static void main(String[] args) {
        System.out.println("Running Lead Tracker Unit Tests...\n");

        testPriorityBoundaries();
        testValidLeadCreation();
        testInvalidInputs();
        testAccumulativeValidation();

        System.out.println("\n-------------------------------------------");
        System.out.printf("Test Summary: %d / %d tests passed.%n", testsPassed, testsTotal);
        System.out.println("-------------------------------------------");

        if (testsPassed != testsTotal) {
            System.exit(1);
        }
    }

    private static void assertEquals(String expected, String actual, String testName) {
        testsTotal++;
        if (expected.equals(actual)) {
            testsPassed++;
            System.out.printf("[PASS] %s%n", testName);
        } else {
            System.out.printf("[FAIL] %s - Expected: %s, Actual: %s%n", testName, expected, actual);
        }
    }

    private static void testPriorityBoundaries() {
        System.out.println("== Priority Boundary Tests ==");
        // COLD range: 0 - 49
        assertEquals("COLD", Lead.calculatePriority(0), "Score 0 should be COLD");
        assertEquals("COLD", Lead.calculatePriority(25), "Score 25 should be COLD");
        assertEquals("COLD", Lead.calculatePriority(49), "Score 49 (upper boundary) should be COLD");

        // WARM range: 50 - 79
        assertEquals("WARM", Lead.calculatePriority(50), "Score 50 (lower boundary) should be WARM");
        assertEquals("WARM", Lead.calculatePriority(65), "Score 65 should be WARM");
        assertEquals("WARM", Lead.calculatePriority(79), "Score 79 (upper boundary) should be WARM");

        // HOT range: 80 - 100
        assertEquals("HOT", Lead.calculatePriority(80), "Score 80 (lower boundary) should be HOT");
        assertEquals("HOT", Lead.calculatePriority(95), "Score 95 should be HOT");
        assertEquals("HOT", Lead.calculatePriority(100), "Score 100 (upper boundary) should be HOT");
    }

    private static void testValidLeadCreation() {
        System.out.println("\n== Valid Lead Creation Tests ==");
        Lead lead = new Lead("Alice Walker", "alice@example.com", 85);
        assertEquals("Alice Walker", lead.getName(), "Lead name should match");
        assertEquals("alice@example.com", lead.getEmail(), "Lead email should match");
        assertEquals("HOT", lead.getPriority(), "Lead priority should be HOT");
    }

    private static void testInvalidInputs() {
        System.out.println("\n== Invalid Input Handling Tests ==");

        // Blank/Empty name
        assertThrows(() -> new Lead("", "valid@example.com", 50), "Empty name should throw exception");
        assertThrows(() -> new Lead("   ", "valid@example.com", 50), "Whitespace name should throw exception");
        assertThrows(() -> new Lead("\"\"", "valid@example.com", 50), "Literal quote name should throw exception");
        assertThrows(() -> new Lead(null, "valid@example.com", 50), "Null name should throw exception");

        // Blank/Empty email
        assertThrows(() -> new Lead("Bob", "", 50), "Empty email should throw exception");
        assertThrows(() -> new Lead("Bob", "   ", 50), "Whitespace email should throw exception");
        assertThrows(() -> new Lead("Bob", null, 50), "Null email should throw exception");
        assertThrows(() -> new Lead("Bob", "invalidemail", 50), "Malformed email should throw exception");

        // Out of bound scores
        assertThrows(() -> new Lead("Bob", "bob@example.com", -1), "Score -1 should throw exception");
        assertThrows(() -> new Lead("Bob", "bob@example.com", 101), "Score 101 should throw exception");
    }

    private static void testAccumulativeValidation() {
        System.out.println("\n== Accumulative Multi-Error Validation Test ==");
        testsTotal++;
        try {
            // All 3 inputs are invalid at once
            new Lead("", "invalid-email", 150);
            System.out.println("[FAIL] Should have thrown exception for all 3 invalid fields");
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            boolean hasNameError = msg.contains("Lead name cannot be empty");
            boolean hasEmailError = msg.contains("Lead email must be a valid email format");
            boolean hasScoreError = msg.contains("Lead score must be an integer between 0 and 100");

            if (hasNameError && hasEmailError && hasScoreError) {
                testsPassed++;
                System.out.println("[PASS] Accumulative validation captured all 3 errors at once");
            } else {
                System.out.printf("[FAIL] Expected all 3 errors, but got message:%n%s%n", msg);
            }
        }
    }

    private static void assertThrows(Runnable runnable, String testName) {
        testsTotal++;
        try {
            runnable.run();
            System.out.printf("[FAIL] %s - Expected IllegalArgumentException but none was thrown%n", testName);
        } catch (IllegalArgumentException e) {
            testsPassed++;
            System.out.printf("[PASS] %s%n", testName);
        } catch (Exception e) {
            System.out.printf("[FAIL] %s - Expected IllegalArgumentException but caught %s%n", testName, e.getClass().getSimpleName());
        }
    }
}
