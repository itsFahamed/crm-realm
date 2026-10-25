import java.util.ArrayList;
import java.util.List;

/**
 * Represents a business lead in the Lead Tracker application.
 * 
 * Design decisions:
 * 1. Encapsulation: Fields are private to prevent direct external manipulation.
 * 2. Single Source of Truth: Priority is calculated dynamically from the score via calculatePriority()
 *    rather than stored as a mutable field, preventing data inconsistency.
 * 3. Accumulative Validation: Instead of failing on the first error, constructor checks all fields
 *    and reports all validation errors at once for a better user experience.
 */
public class Lead {
    private final String name;
    private final String email;
    private final int score;

    /**
     * Constructs and validates a new Lead instance using accumulative validation.
     *
     * @param name  Full name of the lead (must not be empty, blank, or quotes)
     * @param email Email address of the lead (must not be empty or malformed)
     * @param score Qualification score from 0 to 100 inclusive
     * @throws IllegalArgumentException containing all identified validation errors
     */
    public Lead(String name, String email, int score) {
        List<String> errors = new ArrayList<>();

        // Validate Name
        if (name == null || name.trim().isEmpty() || name.trim().equals("\"\"") || name.trim().equals("''")) {
            errors.add("Lead name cannot be empty.");
        }

        // Validate Email
        if (email == null || email.trim().isEmpty() || email.trim().equals("\"\"") || email.trim().equals("''")) {
            errors.add("Lead email cannot be empty.");
        } else if (!email.contains("@") || !email.contains(".")) {
            errors.add("Lead email must be a valid email format (e.g., user@example.com).");
        }

        // Validate Score
        if (score < 0 || score > 100) {
            errors.add("Lead score must be an integer between 0 and 100 inclusive.");
        }

        // If any errors were detected, throw them all together
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n - ", errors));
        }

        this.name = name.trim();
        this.email = email.trim();
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getScore() {
        return score;
    }

    /**
     * Calculates priority dynamically from a given score.
     * 
     * Priority Rules:
     * - 80 to 100: HOT
     * - 50 to 79:  WARM
     * - 0 to 49:   COLD
     *
     * @param score Qualification score (0-100)
     * @return The priority category ("HOT", "WARM", or "COLD")
     */
    public static String calculatePriority(int score) {
        if (score >= 80) {
            return "HOT";
        } else if (score >= 50) {
            return "WARM";
        } else {
            return "COLD";
        }
    }

    /**
     * Instance method to retrieve the lead's calculated priority.
     * Delegates directly to calculatePriority(int) to guarantee consistency.
     *
     * @return The priority category ("HOT", "WARM", or "COLD")
     */
    public String getPriority() {
        return calculatePriority(this.score);
    }

    @Override
    public String toString() {
        return String.format("Name: %-20s | Email: %-25s | Score: %-3d | Priority: %s",
                name, email, score, getPriority());
    }
}
