# CRM Realm - Technical Assessment Submission
**Role:** Junior Software Engineer Intern  
**Company:** CRM Realm  
**Status:** Complete (All unit tests passing)

---

## Repository Overview

```text
CRM Realm/
├── java-lead-tracker/
│   ├── src/
│   │   ├── Lead.java             # Lead domain model with validation & dynamic priority
│   │   ├── Main.java             # Interactive console interface with accumulative error display
│   │   └── LeadTest.java         # Standalone unit test suite (23 tests, zero dependencies)
│   └── bin/                      # Compiled Java class files
├── javascript/
│   └── solutions.js              # Self-contained JavaScript file with 3 functions & test suite
├── sql/
│   └── queries.sql               # PostgreSQL / ANSI SQL solutions for Task 1 & Task 2
└── README.md                     # Assessment documentation & review notes
```

---

## 1. How to Run the Application & Tests

### Prerequisites
- **Java JDK:** OpenJDK 17+ (tested on OpenJDK 25)
- **Node.js:** v18+ (tested on v24.19)
- **Zero Third-Party Dependencies:** No `npm install` or external JARs required.

### Part A: Java Lead Tracker
Open your terminal and navigate to the Java project directory:
```powershell
cd "java-lead-tracker"
```

1. **Compile all Java source files:**
   ```powershell
   javac -d bin src/Lead.java src/Main.java src/LeadTest.java
   ```

2. **Run the interactive console application:**
   ```powershell
   java -cp bin Main
   ```

3. **Run the automated unit tests:**
   ```powershell
   java -cp bin LeadTest
   ```
   *(Runs 23 automated tests covering valid lead creation, priority boundaries, invalid inputs, and multi-error validation).*

---

### Part B: JavaScript Problem-Solving
From the project root, run the single self-contained solutions file:
```powershell
node javascript/solutions.js
```
*(Executes and verifies all test cases covering edge cases, case-insensitivity, duplicates, negative numbers, and status aggregation).*

---

### Part C: SQL Basics
The queries in [`sql/queries.sql`](sql/queries.sql) are written in standard ANSI SQL (targeting PostgreSQL 15+, fully compatible with MySQL 8.0+ and SQLite). They can be executed directly in any SQL workbench or CLI.

---

## 2. Approach & Main Ideas for JavaScript Problems

### Problem 1: `firstNonRepeatingChar(str)`
- **Core Concept:** Two-pass frequency map.
- **Algorithm:**
  1. **Pass 1:** Build a frequency map of lowercase characters using a `Map` in $O(n)$ time.
  2. **Pass 2:** Iterate through the original string from left to right. The first character whose lowercased key has a count of exactly `1` in the map is returned in its **original casing**.
  3. Returns `null` if no unique character exists or the string is empty.
- **Complexity:** $O(n)$ time, $O(1)$ auxiliary space (bounded by character set).
- **Whitespace & Punctuation Handling:** We deliberately filter non-alphanumeric characters (`/[a-zA-Z0-9]/`). In sentences like `"Hello world!"`, spaces or punctuation marks are not treated as candidates for the first unique character.

### Problem 2: `findTwoSum(numbers, target)`
- **Core Concept:** One-Pass Hash Map.
- **Algorithm:**
  1. Traverse the array with index `i` and value `currentNum`.
  2. Compute `complement = target - currentNum`.
  3. If `complement` already exists in the `Map`, immediately return `[map.get(complement), i]`.
  4. Otherwise, store `currentNum` and its index in the `Map`.
- **Why this approach is superior:** 
  - Achieves optimal **$O(n)$ time** and $O(n)$ space.
  - Brute force nested loops take $O(n^2)$ time.
  - Sorting with two pointers takes $O(n \log n)$ time and scrambles the original array indices.

### Problem 3: `countLeadsByStatus(leads)`
- **Core Concept:** Declarative aggregation using `Array.prototype.reduce()`.
- **Algorithm:**
  - Aggregates status counts into a fresh object accumulator in $O(n)$ time.
  - Pure function: does **not** mutate the original input array.
  - Defensively ignores malformed lead items or missing status properties.
- **Handling Statuses with Count of Zero:**  
  *To include predefined statuses with a count of zero, initialize the accumulator object with all known business statuses mapped to 0 (e.g., `Object.fromEntries(knownStatuses.map(s => [s, 0]))`) before reducing over the leads array. Any status with no leads will retain its initial count of 0.*

---

## 3. Test Cases & Boundary Value Coverage

### Java Unit Tests (`LeadTest.java` - 23 Tests)
- **Priority Boundary Values:**
  - `0` (COLD lower boundary)
  - `49` (COLD upper boundary)
  - `50` (WARM lower boundary)
  - `79` (WARM upper boundary)
  - `80` (HOT lower boundary)
  - `100` (HOT upper boundary)
- **Invalid Inputs:**
  - Negative score (`-1`) and score $> 100$ (`101`).
  - Empty name (`""`), whitespace name (`"   "`), literal quote strings (`"\"\""`), and `null`.
  - Empty email, whitespace email, and malformed email lacking `@` or `.`.
  - Non-numeric input in console (e.g., `"abc"`).
- **Accumulative Multi-Error Validation:**
  - Submitting invalid name, invalid email, and invalid score simultaneously asserts that all 3 error messages are returned together.

### JavaScript Tests (`solutions.js` - 18 Tests)
- **Problem 1:**
  - Assessment samples: `"aabbcdd"` $\rightarrow$ `"c"`, `"swiss"` $\rightarrow$ `"w"`, `"aabb"` $\rightarrow$ `null`, `""` $\rightarrow$ `null`.
  - Preserves original case: `"sWiSs"` $\rightarrow$ `"W"`.
  - Case-insensitive duplicates: `"AaBbCc"` $\rightarrow$ `null`.
  - Ignores punctuation and spaces: `"a b a"` $\rightarrow$ `"b"`, `"!a!b!a!"` $\rightarrow$ `"b"`.
  - Defensive type checks: `null`, numbers $\rightarrow$ `null`.
- **Problem 2:**
  - Assessment samples: `[2, 7, 11, 15]`, target `9` $\rightarrow$ `[0, 1]`; `[3, 2, 4]`, target `6` $\rightarrow$ `[1, 2]`.
  - Duplicate target halves: `[3, 3]`, target `6` $\rightarrow$ `[0, 1]`.
  - Negative numbers: `[-1, -2, -3, -4, -5]`, target `-8` $\rightarrow$ `[2, 4]`.
  - Target zero: `[0, 4, 3, 0]`, target `0` $\rightarrow$ `[0, 3]`.
  - Edge cases: Single element arrays (`[5]`), empty arrays (`[]`), invalid types $\rightarrow$ `null`.
- **Problem 3:**
  - Standard sample with multiple statuses.
  - Empty array returns `{}`.
  - Dynamically detects and counts unseen/custom statuses.
  - Immutability assertion: verifies original array reference and items remain unchanged.

---

## 4. Design Decision & Assumption

### Key Design Decision: Accumulative Validation vs. Fail-Fast
- **What we chose:** In `Lead.java` and `Main.java`, we implemented **Accumulative Validation** rather than Fail-Fast validation.
- **Why:** In real-world software and CRM form inputs, failing on the first error creates a frustrating "guess-and-check" user experience where users fix one field only to encounter another error on the next attempt. By collecting all validation violations across name, email, and score, the user receives a complete list of corrections at once.

### Key Assumption: Punctuation & Whitespace in String Analysis
- **Assumption:** In `firstNonRepeatingChar`, we assumed punctuation and spaces should be excluded from candidate evaluation. In natural language processing and business text analysis, users want the first unique *alphanumeric character*, not an accidental space or punctuation symbol.

---

## 5. What I Would Improve With More Time

1. **Persistent Storage:** Currently, leads are stored in-memory using an `ArrayList<Lead>` (resetting upon app exit). With more time, I would integrate a lightweight persistent datastore (such as SQLite via JDBC or JSON file serialization) so lead data persists across sessions.
2. **Search and Filtering:** Add CLI options to filter leads by priority (e.g., view all HOT leads) or search leads by email.
3. **Interactive Menu Loop for Validation:** Instead of returning to the main menu upon validation failure, prompt the user specifically to re-enter only the invalid field(s) inline.

---

## 6. AI Tools & Learning Resources Disclosure

In accordance with Section 6 of the assessment guidelines:
- **AI Tool Used:** Google Antigravity (powered by Gemini).
- **Purpose of Use:** 
  - Explored design alternatives (comparing Java `record` vs standard OOP classes with encapsulation).
  - Analyzed time and space complexity tradeoffs ($O(n^2)$ brute force vs $O(n)$ hash map for Two Sum).
  - Formulated edge cases for boundary testing (specifically testing 0-count statuses and priority score boundaries: 0, 49, 50, 79, 80, 100).
- **Candidate Ownership:** All architectural decisions (accumulative validation, Single Source of Truth for dynamic priority, modular folder structure) were reviewed, selected, verified, and tested interactively.
