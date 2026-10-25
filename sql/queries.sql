-- =====================================================================
-- CRM Realm - Technical Assessment: Part C (SQL Basics)
-- =====================================================================

-- Database: PostgreSQL (queries are ANSI SQL compliant and also run on MySQL)
-- Assumption: The 'leads' table contains (id, name, email, score), where score is an integer from 0 to 100.


-- ---------------------------------------------------------------------
-- Task 1: Retrieve all leads whose priority is HOT (score >= 80)
-- ---------------------------------------------------------------------
SELECT 
    id, 
    name, 
    email, 
    score
FROM 
    leads
WHERE 
    score >= 80
ORDER BY 
    score DESC;


-- ---------------------------------------------------------------------
-- Task 2: Report the number of leads in each priority group: HOT, WARM, COLD
-- Priority rules:
--   - HOT:  80 to 100
--   - WARM: 50 to 79
--   - COLD: 0 to 49
-- ---------------------------------------------------------------------
SELECT 
    CASE 
        WHEN score >= 80 THEN 'HOT'
        WHEN score >= 50 THEN 'WARM'
        ELSE 'COLD'
    END AS priority,
    COUNT(*) AS lead_count
FROM 
    leads
GROUP BY 
    CASE 
        WHEN score >= 80 THEN 'HOT'
        WHEN score >= 50 THEN 'WARM'
        ELSE 'COLD'
    END
ORDER BY 
    MIN(score) DESC;
