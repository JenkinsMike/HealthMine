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
      to_date(administration_date, 'YYYY-MM-DD') as administration_date
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
  )
--  Replace the following with your query, which can define any number of additional CTEs and should ultimately
--  select the following columns in the terminal query:
--
--  patient_id
--  regimen (null, 'Pfizer', 'Moderna', or 'Janssen')
--  vaccination_date (null, date string in a format of your choosing)

drop table if exists cte_vaccine_groups;
drop table if exists cte_vaccination_administration;
drop table if exists PatientFirstDose;
drop table if exists PatientSecondDose;
drop table if exists PatientFirstPfizerDosePlusDelta;
drop table if exists PatientFirstModernaDosePlusDelta;
drop table if exists PatientFirstPfizerDosePlusDelta2;
drop table if exists PatientFirstModernaDosePlusDelta2;
drop table if exists PatientFirstJnJDosePlusDelta2;
drop table if exists PatientSecondPfizerDoseMinusDelta;
drop table if exists PatientSecondModernaDoseMinusDelta;
-- Replace the following with your query, which can define any number of additional CTEs and should ultimately
-- select the following columns in the terminal query:
-- --
-- patient_id
-- regimen (null, 'Pfizer', 'Moderna', or 'Janssen')
-- vaccination_date (null, date string in a format of your choosing)

-- So, I tried to learn Oracle really quick and it felt like I was wasting a little bit too much time, so I pivoted.
-- And I made the tables in MySQL. See below.
create table cte_vaccine_groups (vaccine_group_name varchar(32) default null, vaccine_code varchar(32));
insert into cte_vaccine_groups (vaccine_group_name, vaccine_code) values ('Pfizer First Dose', '0001A'), ('Pfizer First Dose', '91300'), ('Pfizer First Dose', '5926710001'), ('Pfizer First Dose', '59267100001'), ('Pfizer First Dose', '59267100002'), ('Pfizer First Dose', '59267100003'), ('Pfizer Second Dose', '0002A'), ('Pfizer Second Dose', '91300'), ('Pfizer Second Dose', '5926710001'), ('Pfizer Second Dose', '59267100001'), ('Pfizer Second Dose', '59267100002'), ('Pfizer Second Dose', '59267100003'), ('Moderna First Dose', '0011A'), ('Moderna First Dose', '91301'), ('Moderna First Dose', '8077727310'), ('Moderna First Dose', '80777027310'), ('Moderna First Dose', '80777027399'), ('Moderna Second Dose', '0012A'), ('Moderna Second Dose', '91301'), ('Moderna Second Dose', '8077727310'), ('Moderna Second Dose', '80777027310'), ('Moderna Second Dose', '80777027399'), ('Janssen', '91303'), ('Janssen', '0031A'), ('Janssen', '59676058005'), ('Janssen', '59676058015');
create table cte_vaccination_administration (patient_id int(4) default null, vaccine_code varchar(32), administration_date date default null);
insert into cte_vaccination_administration (patient_id, vaccine_code, administration_date) values ('1', '0001A', '2021-01-01'), ('2', '0002A', '2021-01-01'), ('3', '0001A', '2021-01-01'), ('3', '0002A', '2021-01-17'), ('4', '91300', '2021-01-01'), ('4', '5926710001', '2021-01-18'), ('5', '0002A', '2021-01-01'), ('5', '91300', '2021-03-01'), ('6', '59267100003', '2021-04-01'), ('6', '0001A', '2021-06-01'), ('7', '0002A', '2021-07-01'), ('7', '0002A', '2021-08-01'), ('8', '0011A', '2021-01-01'), ('9', '0012A', '2021-01-01'), ('10', '0011A', '2021-01-01'), ('10', '0012A', '2021-01-24'), ('11', '91301', '2021-01-01'), ('11', '8077727310', '2021-01-25'), ('12', '0012A', '2021-01-01'), ('12', '91301', '2021-03-01'), ('13', '80777027399', '2021-04-01'), ('13', '0011A', '2021-06-01'), ('14', '0012A', '2021-07-01'), ('14', '0012A', '2021-08-01'), ('15', '59676058015', '2021-01-01');
-- I tried a few different attempts.
-- Create a table of (assumed) valid first doses of Pfizer, then add a col to represent the minimum future date that
-- full vaccination would be considered (to be used later with maths).create temporary table if not exists PatientFirstPfizerDosePlusDelta as (select distinct va.patient_id, vg.vaccine_code, vg.vaccine_group_name, va.administration_date, ADDDATE(va.administration_date, 17) '17 Days Later'from cte_vaccination_administration va , cte_vaccine_groups vgwhere va.vaccine_code = vg.vaccine_codeand vg.vaccine_code = (select va2.vaccine_code from cte_vaccine_groups va2, cte_vaccine_groups vg where va2.vaccine_group_name = 'pfizer First Dose' and vg.vaccine_code = va2.vaccine_code limit 1)order by va.patient_id, va.administration_date);
-- Create a table of (assumed) valid second doses of Pfizer, then add a col to represent the minimum paste date that
-- full vaccination would be considered (to be used later with maths).
-- this one didn't work correctly. cross-population was starting here.
create temporary table if not exists PatientSecondPfizerDoseMinusDelta as ( select distinct va.patient_id, vg.vaccine_code, vg.vaccine_group_name, va.administration_date, SUBDATE(va.administration_date, INTERVAL 17 DAY) '17 Days Earlier' from cte_vaccination_administration va , cte_vaccine_groups vg where va.vaccine_code = vg.vaccine_code and vg.vaccine_code = (select va2.vaccine_code from cte_vaccine_groups va2, cte_vaccine_groups vg where va2.vaccine_group_name = 'Pfizer Second Dose' and vg.vaccine_code = va2.vaccine_code limit 1) order by va.patient_id, va.administration_date);
-- Create a table of (assumed) valid first doses of Moderna, then add a col to represent the minimum future date that
-- full vaccination would be considered (to be used later with maths).
create
temporary table if not exists PatientFirstModernaDosePlusDelta as ( select distinct va.patient_id, vg.vaccine_code, vg.vaccine_group_name, va.administration_date, ADDDATE(va.administration_date, 24) '24 Days Later' from cte_vaccination_administration va , cte_vaccine_groups vg where va.vaccine_code = vg.vaccine_code and vg.vaccine_code = (select va2.vaccine_code from cte_vaccine_groups va2, cte_vaccine_groups vg where va2.vaccine_group_name = 'Moderna First Dose' and vg.vaccine_code = va2.vaccine_code limit 1) order by va.patient_id, va.administration_date);
-- Create a table of (assumed) valid second doses of Moderna, then add a col to represent the minimum paste date that
-- full vaccination would be considered (to be used later with maths).
-- this one didn't work correctly. cross-population was starting here.
create
temporary table if not exists PatientSecondModernaDoseMinusDelta as ( select distinct va.patient_id, vg.vaccine_code, vg.vaccine_group_name, va.administration_date, SUBDATE(va.administration_date, INTERVAL 24 DAY) '24 Days Earlier' from cte_vaccination_administration va , cte_vaccine_groups vg where va.vaccine_code = vg.vaccine_code and vg.vaccine_code = (select va2.vaccine_code from cte_vaccine_groups va2, cte_vaccine_groups vg where va2.vaccine_group_name = 'Moderna Second Dose' and vg.vaccine_code = va2.vaccine_code limit 1 ) order by va.patient_id, va.administration_date);
-- Create a table of (assumed) valid first doses of Janssen.
create
temporary table if not exists PatientFirstJnJDosePlusDelta as ( select distinct va.patient_id, vg.vaccine_code, vg.vaccine_group_name, va.administration_date from cte_vaccination_administration va , cte_vaccine_groups vg where va.vaccine_code = vg.vaccine_code and vg.vaccine_group_name = 'Janssen' order by va.patient_id, va.administration_date);
select *
from PatientFirstPfizerDosePlusDelta;
select *
from PatientFirstModernaDosePlusDelta;
select *
from PatientFirstJnJDosePlusDelta;
select *
from PatientSecondPfizerDoseMinusDelta;
select *
from PatientSecondModernaDoseMinusDelta;
-- It is at this point that I have to admit that I have forgotten more than I thought when it came to writing accurate
-- SQLs. I considered trying to create Keys. I considered trying to just do this all in an H2 in memory database.
-- I debated setting up a local docker container of MariaDBs, using Flyway to create two tables, create, basically, a
-- DAO and a Repo using SpringBoot, and doing it all in the code (which is how I would do it in a real-life scenario),
-- but that felt like the solution would be in opposition to the spirit of this ask. I did have some attempts before 
-- that used the from -> join -> on syntax, but I dropped that attempt when I started using intelliJ instead of that
-- online tool.
-- 
-- This bums me out pretty hard. I had some ideas to do some date-math. If I had another day, maybe I would get a bit
-- closer, but it is time to submit this to you all.  And now the formatting is all off...sigh.  I really do not like
-- that I am turning in something like this, but I gave myself a deadline and I have to give you all the respect 
-- deserved and turn this in.
-- 
-- Thank you for your consideration. Despite the result here, I am still confident in my abilities, especially in the 
-- CLI.
-- 
-- Thanks!

```

# Exercise

Using the above basis query, replace the terminal query (`select * from ...`) with additional CTEs that you deem necessary and a terminal query that selects the documented columns.

In your submission back to HealthMine, please include the entirety of your working query.
