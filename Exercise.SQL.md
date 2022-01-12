# Query Exercise

Essentially, we want to do the same exact thing as the Basic CLI Program Exercise, but as a SQL query. Rather than files, we'll use CTEs. Rather than printing output to the console, we'll select it as a result set. The net output should be effectively identical, however.

If you don't have convenient access to a database engine, [https://dbfiddle.uk/?rdbms=oracle_11.2](https://dbfiddle.uk/?rdbms=oracle_11.2) is a good paste-and-execute SQL evaluator. [http://sqlfiddle.com](http://sqlfiddle.com) is also a good option, but requires building up the schema to query against separately.

While the basis query was written with Oracle in mind, it can be easily refactored to run on any database engine that supports CTEs and whatever capabilities that you want to use in the exercise (e. g., window/analytic functions). For example, to run on Postgres, simply remove the `from dual` statements. The statements to convert strings to dates might also need to be adjusted based on the engine of your selection.

The intent of these exercises is to demonstrate a working knowledge of SQL in general rather than familiarity with one RDBMS engine over another, so please feel free to use the engine of your choice.

# Dataset

Here is the basis for your query. Each of the files that were used in the Basic CLI Program Exercise have been defined as distinct CTEs. You can define as many additional CTEs as you wish, but we ask that you leave the first 2 untouched beyond what is necessary to execute using your database engine of choice.

```sql
with
  cte_vaccine_groups as (
    select 'Pfizer First Dose' as vaccine_group_name, '0001A' as vaccine_code from dual union all
    select 'Pfizer First Dose' as vaccine_group_name, '91300' as vaccine_code from dual union all
    select 'Pfizer First Dose' as vaccine_group_name, '5926710001' as vaccine_code from dual union all
    select 'Pfizer First Dose' as vaccine_group_name, '59267100001' as vaccine_code from dual union all
    select 'Pfizer First Dose' as vaccine_group_name, '59267100002' as vaccine_code from dual union all
    select 'Pfizer First Dose' as vaccine_group_name, '59267100003' as vaccine_code from dual union all
    select 'Pfizer Second Dose' as vaccine_group_name, '0002A' as vaccine_code from dual union all
    select 'Pfizer Second Dose' as vaccine_group_name, '91300' as vaccine_code from dual union all
    select 'Pfizer Second Dose' as vaccine_group_name, '5926710001' as vaccine_code from dual union all
    select 'Pfizer Second Dose' as vaccine_group_name, '59267100001' as vaccine_code from dual union all
    select 'Pfizer Second Dose' as vaccine_group_name, '59267100002' as vaccine_code from dual union all
    select 'Pfizer Second Dose' as vaccine_group_name, '59267100003' as vaccine_code from dual union all
    select 'Moderna First Dose' as vaccine_group_name, '0011A' as vaccine_code from dual union all
    select 'Moderna First Dose' as vaccine_group_name, '91301' as vaccine_code from dual union all
    select 'Moderna First Dose' as vaccine_group_name, '8077727310' as vaccine_code from dual union all
    select 'Moderna First Dose' as vaccine_group_name, '80777027310' as vaccine_code from dual union all
    select 'Moderna First Dose' as vaccine_group_name, '80777027399' as vaccine_code from dual union all
    select 'Moderna Second Dose' as vaccine_group_name, '0012A' as vaccine_code from dual union all
    select 'Moderna Second Dose' as vaccine_group_name, '91301' as vaccine_code from dual union all
    select 'Moderna Second Dose' as vaccine_group_name, '8077727310' as vaccine_code from dual union all
    select 'Moderna Second Dose' as vaccine_group_name, '80777027310' as vaccine_code from dual union all
    select 'Moderna Second Dose' as vaccine_group_name, '80777027399' as vaccine_code from dual union all
    select 'Janssen' as vaccine_group_name, '91303' as vaccine_code from dual union all
    select 'Janssen' as vaccine_group_name, '0031A' as vaccine_code from dual union all
    select 'Janssen' as vaccine_group_name, '59676058005' as vaccine_code from dual union all
    select 'Janssen' as vaccine_group_name, '59676058015' as vaccine_code from dual
  ),
  cte_vaccination_administration as (
    select
      patient_id,
      vaccine_code,
      administration_date
    from (
      select 1 as patient_id, '0001A' as vaccine_code, '2021-01-01' as administration_date from dual union all
      select 2 as patient_id, '0002A' as vaccine_code, '2021-01-01' as administration_date from dual union all
      select 3 as patient_id, '0001A' as vaccine_code, '2021-01-01' as administration_date from dual union all
      select 3 as patient_id, '0002A' as vaccine_code, '2021-01-17' as administration_date from dual union all
      select 4 as patient_id, '91300' as vaccine_code, '2021-01-01' as administration_date from dual union all
      select 4 as patient_id, '5926710001' as vaccine_code, '2021-01-18' as administration_date from dual union all
      select 5 as patient_id, '0002A' as vaccine_code, '2021-01-01' as administration_date from dual union all
      select 5 as patient_id, '91300' as vaccine_code, '2021-03-01' as administration_date from dual union all
      select 6 as patient_id, '59267100003' as vaccine_code, '2021-04-01' as administration_date from dual union all
      select 6 as patient_id, '0001A' as vaccine_code, '2021-06-01' as administration_date from dual union all
      select 7 as patient_id, '0002A' as vaccine_code, '2021-07-01' as administration_date from dual union all
      select 7 as patient_id, '0002A' as vaccine_code, '2021-08-01' as administration_date from dual union all
      select 8 as patient_id, '0011A' as vaccine_code, '2021-01-01' as administration_date from dual union all
      select 9 as patient_id, '0012A' as vaccine_code, '2021-01-01' as administration_date from dual union all
      select 10 as patient_id, '0011A' as vaccine_code, '2021-01-01' as administration_date from dual union all
      select 10 as patient_id, '0012A' as vaccine_code, '2021-01-24' as administration_date from dual union all
      select 11 as patient_id, '91301' as vaccine_code, '2021-01-01' as administration_date from dual union all
      select 11 as patient_id, '8077727310' as vaccine_code, '2021-01-25' as administration_date from dual union all
      select 12 as patient_id, '0012A' as vaccine_code, '2021-01-01' as administration_date from dual union all
      select 12 as patient_id, '91301' as vaccine_code, '2021-03-01' as administration_date from dual union all
      select 13 as patient_id, '80777027399' as vaccine_code, '2021-04-01' as administration_date from dual union all
      select 13 as patient_id, '0011A' as vaccine_code, '2021-06-01' as administration_date from dual union all
      select 14 as patient_id, '0012A' as vaccine_code, '2021-07-01' as administration_date from dual union all
      select 14 as patient_id, '0012A' as vaccine_code, '2021-08-01' as administration_date from dual union all
      select 15 as patient_id, '59676058015' as vaccine_code, '2021-01-01' as administration_date from dual
    )
  ),
  cte_group_code_checker as (
  -- Grab important things like patient_id, administration_date, vaccince_group,
  --  and create a new column for Vaccing Regiment
  -- JOIN cte_vaccination_administration to cte_vaccine_groups on vaccine_code
      select 
        patient_id
      , case when vg.vaccine_group_name = 'Janssen' 
             then 'Johson and Johnson' 
             else SubStr(vaccine_group_name, 1, INSTR(vg.vaccine_group_name,' ')) 
        end as regiment
      , administration_date
      , vaccine_group_name
      from cte_vaccination_administration va
      join cte_vaccine_groups vg 
      on va.vaccine_code = vg.vaccine_code
  ),
  cte_self_merge as (
  -- Again, grab important things, but unite them.  Grab patient_id, regiment,
  --  an administration_date as first_admin_date, a group_name as first_group_name,
  --  and the same for the second administration, and do some date-math to find
  --  out how many days apart first_ and second_ administration dates are.
  -- LOJ cte_group_code_checker to itself on patient_id, regiment, and 
  --  vaccine_group_name on "First" grouping, "Second" grouping, or single-dose
  --  grouping
  -- FILTER where the second administration_date is greater than the first
  --  AND first administration_date is not equal to the second administration_date
  --  while also not being the single-dose regiment
  --  OR the single-does regiment (such that we get a phantom second-dose date)
      select 
        a.patient_id
      , a.regiment
      , a.administration_date as first_admin_date
      , a.vaccine_group_name  as first_group_name
      , b.administration_date as second_admin_date
      , b.vaccine_group_name  as second_group_name
      , TO_DATE(b.administration_date, 'YYYY-MM-DD') - TO_DATE(a.administration_date, 'YYYY-MM-DD') as date_diff
      from cte_group_code_checker a
      left outer join cte_group_code_checker b 
      on a.patient_id = b.patient_id 
        and a.regiment = b.regiment
        and (a.vaccine_group_name like '%First%'  or a.vaccine_group_name like '%Janssen%')
        and (b.vaccine_group_name like '%Second%' or b.vaccine_group_name like '%Janssen%')
      where b.administration_date > a.administration_date
        and (a.administration_date != b.administration_date and a.regiment != 'Johson and Johnson')
        or a.regiment = 'Johson and Johnson'
  )
  -- SELECT patient_id, regiment, and second_admin_date
  -- FROM merged result of two base ctes
  -- WHERE date-difference business rules apply.
  -- ORDER BY oldest fully vaccinated date.
  select
    patient_id        as "Patient ID"
  , regiment          as "Vaccine Regiment"
  , second_admin_date as "Vaccincation Date"
  from cte_self_merge
  where (TRIM(regiment) = 'Johson and Johnson'
     or (TRIM(regiment) = 'Pfizer'  and date_diff >= 17)
     or (TRIM(regiment) = 'Moderna' and date_diff >= 24))
  order by second_admin_date
```

# Exercise

Using the above basis query, replace the terminal query (`select * from ...`) with additional CTEs that you deem necessary and a terminal query that selects the documented columns.

In your submission back to HealthMine, please include the entirety of your working query.

https://dbfiddle.uk/?rdbms=oracle_18&fiddle=8e2a19a8b7b8f4a98405baea165fd271
