# Basic CLI Program Exercise

Given two independent data sets in the form of CSV files, we want to know which patients have been fully vaccinated for COVID and, if fully vaccinated, the date on which they became fully vaccinated and by which regimen. Create a basic CLI program in any compilable language that reads data in from 2 files, calculate vaccination status on a per-patient basis, and output the results. The program must require compilation; do not use a scripting language (e. g., Python, JavaScript, PHP, etc.).

In this case, we are only concerned about 3 vaccination regimens, which adhere to the following rules:

1. Pfizer
    * 2 distinct doses
    * Each dose separated by at least 17 days
    * The first dose is identified by any vaccine code in the `Pfizer First Dose` vaccine group
    * The second dose is identified by any vaccine code in the `Pfizer Second Dose` vaccine group
    * The vaccination date is the date of the qualifying second dose
2. Moderna
    * 2 distinct doses
    * Each dose separated by at least 24 days
    * The first dose is identified by any vaccine code in the `Moderna First Dose` vaccine group
    * The second dose is identified by any vaccine code in the `Moderna Second Dose` vaccine group
    * The vaccination date is the date of the qualifying second dose
3. Janssen (Johson & Johnson)
    * Single dose
    * A dose is identified by any vaccine code in the `Janssen` vaccine group
    * The vaccination date is the date of the single dose

# Constraints

The only hard constraint that we are imposing on your implementation is that your code must not contain any string literals that have specific ***vaccine codes*** as values. It may, however, contain string literals that have ***vaccine group names*** as values.

Aside from the vaccination regimen rules described above, we don't want to concern this exercise with the innumerable corner cases that could possibly exist. Even so, we fully recognize that there might be considerations that we didn't take an explicit stand on. If you run into such a consideration, feel free to make whatever decision you feel is appropriate and call it out in your source code.

# Inputs

The 2 files that will be acting as inputs are:

1. `vaccine_groups.csv`: enumeration of the discrete vaccine codes and the vaccine group that each code belongs to. Fields include:
    * `vaccine_group_name`: Name of the vaccine group, of which there are 5 (2 for Pfizer, 2 for Moderna, and 1 for Janssen).
    * `vaccine_code`: Discrete code that belongs to the group.
2. `vaccination_administration.csv`: a history of which vaccine was administered to a given patient on which date. Fields include:
    * `patient_id`: Arbitrary number that uniquely idenfies the patient.
    * `vaccine_code`: Code of the vaccine that was administered to the patient.
    * `administration_date`: Date on which the identified vaccine was administered to the patient. All dates will be in the form of `YYYY-MM-DD`.

The files are small enough that you should be able to inspect them and logically determine what the output should be. Feel free to hard-code the names of the files in your code - we will not be using different files to evaluate your code.

# Output

The output (printed to the console or to a file - it doesn't matter to us) should be comprised of a single line per patient that communicates the following information in whatever format you see fit:

1. ID of the patient.
2. If (and only if) fully vaccinated per the above rules, the name of the vaccination regimen (Pfizer, Moderna, or Janssen) with which they were vaccinated. If the patient has not been fully vaccinated, you can output any value to indicate this.
3. If (and only if) fully vaccinated per the above rules, the vaccination date as calculated per the above rules. If the patient has not been fully vaccinated, you can output any value to indicate this.

The output should represent a given patient once and only once and there will definitely be patients who are not considered fully vaccinated.

In your submission back to HealthMine, please provide us with your source code along with any instructions for compiling and executing the program.
