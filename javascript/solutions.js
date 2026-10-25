/**
 * CRM Realm - Technical Assessment: Part B (JavaScript Problem-Solving)
 * 
 * Contains solutions and test cases for:
 * 1. firstNonRepeatingChar(str)
 * 2. findTwoSum(numbers, target)
 * 3. countLeadsByStatus(leads)
 */

// =====================================================================
// Problem 1: First Non-Repeating Character
// =====================================================================
/**
 * Finds the first character in a string that appears exactly once.
 * - Case-insensitive counting ('S' and 's' count as the same character).
 * - Preserves and returns the character in its original casing.
 * - Returns null if no character appears exactly once, or if string is empty.
 * - Punctuation & spaces: Non-alphanumeric characters are excluded so that
 *   meaningful alphanumeric characters are evaluated.
 * 
 * Approach: Two-Pass Frequency Map
 * Time Complexity:  O(n) where n is string length.
 * Space Complexity: O(1) auxiliary (bounded by alphabet size).
 *
 * @param {string} str - Input string
 * @returns {string|null} - First non-repeating character or null
 */
function firstNonRepeatingChar(str) {
  if (typeof str !== "string" || str.length === 0) {
    return null;
  }

  const isAlphaNumeric = (char) => /[a-zA-Z0-9]/.test(char);

  // Pass 1: Build frequency map of lowercased characters
  const charCounts = new Map();
  for (const char of str) {
    if (isAlphaNumeric(char)) {
      const lower = char.toLowerCase();
      charCounts.set(lower, (charCounts.get(lower) || 0) + 1);
    }
  }

  // Pass 2: Find the first character with a count of 1 in original order
  for (const char of str) {
    if (isAlphaNumeric(char)) {
      const lower = char.toLowerCase();
      if (charCounts.get(lower) === 1) {
        return char; // Returns original casing
      }
    }
  }

  return null;
}


// =====================================================================
// Problem 2: Two Sum
// =====================================================================
/**
 * Finds the indices of two distinct elements in an array that add up to a target number.
 * - Does not use the same element twice.
 * - Returns null if no pair exists.
 * 
 * Approach: One-Pass Hash Map
 * - As we iterate, we compute complement = (target - currentNum).
 * - If complement exists in map, return [map.get(complement), currentIndex].
 * - Otherwise, store currentNum and its index in map.
 * 
 * Why this approach:
 * - Optimal O(n) time complexity (vs O(n^2) nested loops).
 * - Preserves original array indices (unlike sorting + two pointers).
 * 
 * Time Complexity:  O(n)
 * Space Complexity: O(n)
 *
 * @param {number[]} numbers - Array of integers
 * @param {number} target - Target sum
 * @returns {number[]|null} - [index1, index2] or null
 */
function findTwoSum(numbers, target) {
  if (!Array.isArray(numbers) || numbers.length < 2 || typeof target !== "number") {
    return null;
  }

  const seenNumbers = new Map(); // stores { value: index }

  for (let i = 0; i < numbers.length; i++) {
    const currentNum = numbers[i];
    const complement = target - currentNum;

    if (seenNumbers.has(complement)) {
      return [seenNumbers.get(complement), i];
    }

    seenNumbers.set(currentNum, i);
  }

  return null;
}


// =====================================================================
// Problem 3: Count Leads by Status
// =====================================================================
/**
 * Counts the occurrences of each status in an array of lead objects.
 * - Does not mutate the original array.
 * - Dynamically supports any new status values.
 * - Returns an empty object {} for an empty array.
 * 
 * Approach: Array.prototype.reduce()
 * - Pure functional approach, immutability, zero external side-effects.
 * 
 * How to include statuses with a count of zero:
 * To include predefined statuses with 0 counts, initialize the accumulator object
 * with all known business statuses mapped to 0 (e.g., Object.fromEntries(knownStatuses.map(s => [s, 0])))
 * before reducing over the leads array. Any status without leads will retain its initial count of 0.
 * 
 * Time Complexity:  O(n) where n is the number of leads.
 * Space Complexity: O(s) where s is the number of distinct statuses.
 *
 * @param {Array<{name: string, status: string}>} leads - Array of lead objects
 * @returns {Record<string, number>} - Object mapping each status to its count
 */
function countLeadsByStatus(leads) {
  if (!Array.isArray(leads) || leads.length === 0) {
    return {};
  }

  return leads.reduce((statusCounts, lead) => {
    if (lead && typeof lead.status === "string" && lead.status.trim() !== "") {
      const statusKey = lead.status.trim();
      statusCounts[statusKey] = (statusCounts[statusKey] || 0) + 1;
    }
    return statusCounts;
  }, {});
}


// =====================================================================
// Self-Executing Tests (Runs via: node javascript/solutions.js)
// =====================================================================
function runTests() {
  console.log("==================================================");
  console.log("   CRM Realm - JavaScript Solutions & Tests");
  console.log("==================================================\n");

  let passed = 0;
  let total = 0;

  function assert(actual, expected, testName) {
    total++;
    const actualStr = JSON.stringify(actual);
    const expectedStr = JSON.stringify(expected);
    if (actualStr === expectedStr) {
      passed++;
      console.log(`  [PASS] ${testName}`);
    } else {
      console.error(`  [FAIL] ${testName}`);
      console.error(`         Expected: ${expectedStr}, Got: ${actualStr}`);
    }
  }

  console.log("--- Problem 1: firstNonRepeatingChar ---");
  assert(firstNonRepeatingChar("aabbcdd"), "c", 'firstNonRepeatingChar("aabbcdd") -> "c"');
  assert(firstNonRepeatingChar("swiss"), "w", 'firstNonRepeatingChar("swiss") -> "w"');
  assert(firstNonRepeatingChar("sWiSs"), "W", 'firstNonRepeatingChar("sWiSs") -> "W" (case-insensitive, original case)');
  assert(firstNonRepeatingChar("aabb"), null, 'firstNonRepeatingChar("aabb") -> null');
  assert(firstNonRepeatingChar(""), null, 'firstNonRepeatingChar("") -> null');
  assert(firstNonRepeatingChar("a b a"), "b", 'firstNonRepeatingChar("a b a") -> "b" (ignores whitespace)');
  assert(firstNonRepeatingChar("!a!b!a!"), "b", 'firstNonRepeatingChar("!a!b!a!") -> "b" (ignores punctuation)');

  console.log("\n--- Problem 2: findTwoSum ---");
  assert(findTwoSum([2, 7, 11, 15], 9), [0, 1], "findTwoSum([2, 7, 11, 15], 9) -> [0, 1]");
  assert(findTwoSum([3, 2, 4], 6), [1, 2], "findTwoSum([3, 2, 4], 6) -> [1, 2]");
  assert(findTwoSum([3, 3], 6), [0, 1], "findTwoSum([3, 3], 6) -> [0, 1] (duplicates)");
  assert(findTwoSum([1, 2, 3], 10), null, "findTwoSum([1, 2, 3], 10) -> null");
  assert(findTwoSum([-1, -2, -3, -4, -5], -8), [2, 4], "findTwoSum negative numbers -> [2, 4]");
  assert(findTwoSum([0, 4, 3, 0], 0), [0, 3], "findTwoSum zeros -> [0, 3]");
  assert(findTwoSum([5], 5), null, "findTwoSum single element -> null");

  console.log("\n--- Problem 3: countLeadsByStatus ---");
  const sampleLeads = [
    { name: "Alice", status: "NEW" },
    { name: "Bob", status: "CONTACTED" },
    { name: "Charlie", status: "NEW" },
    { name: "David", status: "QUALIFIED" },
  ];
  assert(
    countLeadsByStatus(sampleLeads),
    { NEW: 2, CONTACTED: 1, QUALIFIED: 1 },
    "countLeadsByStatus(sampleLeads) -> { NEW: 2, CONTACTED: 1, QUALIFIED: 1 }"
  );
  assert(countLeadsByStatus([]), {}, "countLeadsByStatus([]) -> {}");
  assert(
    countLeadsByStatus([
      { name: "Eve", status: "ONBOARDING" },
      { name: "Frank", status: "LOST" },
      { name: "Grace", status: "ONBOARDING" },
    ]),
    { ONBOARDING: 2, LOST: 1 },
    "countLeadsByStatus dynamic new statuses"
  );

  // Immutability check
  const originalLeadsCopy = JSON.stringify(sampleLeads);
  countLeadsByStatus(sampleLeads);
  assert(JSON.stringify(sampleLeads), originalLeadsCopy, "Does not mutate input array");

  console.log("\n==================================================");
  console.log(`  Test Results: ${passed} / ${total} tests passed.`);
  console.log("==================================================");
}

// Execute tests when run directly via node
if (require.main === module) {
  runTests();
}

module.exports = {
  firstNonRepeatingChar,
  findTwoSum,
  countLeadsByStatus,
};
