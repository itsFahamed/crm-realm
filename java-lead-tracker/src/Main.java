import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console application for managing leads in the Lead Tracker.
 * 
 * Responsibilities:
 * - Provides a clean, interactive console interface.
 * - Handles user input safely, preventing scanner buffer issues.
 * - Implements accumulative error reporting so users see all issues at once.
 */
public class Main {
    private static final List<Lead> leads = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=========================================");
        System.out.println("   CRM Realm - Simple Lead Tracker");
        System.out.println("=========================================");

        boolean running = true;
        while (running) {
            displayMenu();
            System.out.print("Enter your choice (1-3): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleAddLead(scanner);
                    break;
                case "2":
                    handleViewLeads();
                    break;
                case "3":
                    System.out.println("\nThank you for using Lead Tracker. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\n[!] Invalid option. Please select 1, 2, or 3.\n");
                    break;
            }
        }

        scanner.close();
    }

    /**
     * Displays the main menu choices to the user.
     */
    private static void displayMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Add a lead");
        System.out.println("2. View all leads");
        System.out.println("3. Exit");
    }

    /**
     * Prompts the user for lead details, validates all inputs together,
     * and displays all error messages at once if validation fails.
     *
     * @param scanner Scanner instance for reading input
     */
    private static void handleAddLead(Scanner scanner) {
        System.out.println("\n--- Add a New Lead ---");

        System.out.print("Enter lead full name: ");
        String name = scanner.nextLine();

        System.out.print("Enter lead email: ");
        String email = scanner.nextLine();

        System.out.print("Enter lead score (0 - 100): ");
        String scoreInput = scanner.nextLine().trim();

        // Check if score is an integer
        int parsedScore = -1;
        boolean scoreIsInteger = true;
        try {
            parsedScore = Integer.parseInt(scoreInput);
        } catch (NumberFormatException e) {
            scoreIsInteger = false;
        }

        // If score is not an integer, we construct a descriptive error list
        if (!scoreIsInteger) {
            List<String> inputErrors = new ArrayList<>();
            if (name == null || name.trim().isEmpty() || name.trim().equals("\"\"") || name.trim().equals("''")) {
                inputErrors.add("Lead name cannot be empty.");
            }
            if (email == null || email.trim().isEmpty() || email.trim().equals("\"\"") || email.trim().equals("''")) {
                inputErrors.add("Lead email cannot be empty.");
            } else if (!email.contains("@") || !email.contains(".")) {
                inputErrors.add("Lead email must be a valid email format (e.g., user@example.com).");
            }
            inputErrors.add("Lead score must be a whole number between 0 and 100.");

            System.out.println("\n[Validation Error(s)]");
            for (String err : inputErrors) {
                System.out.println(" - " + err);
            }
            return;
        }

        // Score is an integer; delegate full creation and accumulative validation to Lead model
        try {
            Lead newLead = new Lead(name, email, parsedScore);
            leads.add(newLead);
            System.out.printf("\n[Success] Lead added successfully! Priority assigned: %s%n", newLead.getPriority());
        } catch (IllegalArgumentException e) {
            System.out.println("\n[Validation Error(s)]");
            System.out.println(" - " + e.getMessage());
        }
    }

    /**
     * Displays all stored leads with their name, email, score, and priority.
     * If no leads have been added yet, displays a helpful message.
     */
    private static void handleViewLeads() {
        System.out.println("\n--- Stored Leads ---");

        if (leads.isEmpty()) {
            System.out.println("No leads found in the system. Use option 1 to add a lead.");
            return;
        }

        System.out.printf("%-5s | %-20s | %-28s | %-6s | %-8s%n", "#", "Name", "Email", "Score", "Priority");
        System.out.println("-----------------------------------------------------------------------------");

        for (int i = 0; i < leads.size(); i++) {
            Lead lead = leads.get(i);
            System.out.printf("%-5d | %-20s | %-28s | %-6d | %-8s%n",
                    (i + 1),
                    lead.getName(),
                    lead.getEmail(),
                    lead.getScore(),
                    lead.getPriority());
        }
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("Total Leads: %d%n", leads.size());
    }
}
