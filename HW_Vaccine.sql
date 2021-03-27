DROP SCHEMA IF EXISTS HW_Vaccine;

CREATE SCHEMA HW_Vaccine;

USE HW_Vaccine;

CREATE TABLE COMPANY (
  Name VARCHAR(50) PRIMARY KEY,
  Website VARCHAR(255) CHECK (Website LIKE "https://%")
);

CREATE TABLE DISEASE (
  Name VARCHAR(50) PRIMARY KEY,
  Communicable BOOL,
  -- Whether the disease can be transmitted from a human to
  --    another.
  TYPE ENUM ("infectious", "deficiency", "hereditary")
);

CREATE TABLE VACCINE (
  Name VARCHAR(50) PRIMARY KEY,
  Manufacturer VARCHAR(50) NOT NULL,
  FOREIGN KEY (Manufacturer) REFERENCES COMPANY (NAME) ON
    UPDATE CASCADE
);

CREATE TABLE EFFICACY (
  DiseaseName VARCHAR(50),
  VaccineName VARCHAR(50),
  Efficacy DECIMAl(5, 2),
  PRIMARY KEY (DiseaseName, VaccineName),
  FOREIGN KEY (DiseaseName) REFERENCES DISEASE (NAME),
  FOREIGN KEY (VaccineName) REFERENCES VACCINE (NAME)
);

INSERT INTO COMPANY
VALUES (
  "Moderna",
  "https://www.modernatx.com/");

INSERT INTO DISEASE
VALUES (
  "Coronavirus disease 2019",
  TRUE,
  "infectious");

INSERT INTO VACCINE
VALUES (
  "mRNA-1273",
  "Moderna");

INSERT INTO EFFICACY
VALUES (
  "Coronavirus disease 2019",
  "mRNA-1273",
  94.1);


/*
 START EDITING
 */
-- start snippet solution
/* code/sql/HW_VaccineSol.sql */
/*

I. Short Questions (3 pts.)

Answer the following short questions. In our implementation…

1. … can two companies have exactly the same name?

No, as COMPANY.Name is the only attribute in the primary key of COMPANY.

2. … can two companies have the same website?

Yes, nothing prevents it.

3. … can a company not have a website?

Yes, the domain of COMPANY.Website is "VARCHAR(255)", without a constraint preventing it from being "NULL".

4. … can the same vaccine be manufactured by multiple companies?

No, as VACCINE.Manufacturer is an attribute in VACCINE that accepts only one value.

5. … can a vaccine not have a manufacturer?

No, as VACCINE.Manufacturer bears the "NOT NULL" constraint.

6. … can a disease being neither communicable nor not communicable?

Yes, as DISEASE.Communicable is of type "BOOL", it accepts the "NULL" value.

7. … can the same vaccine have different efficacies for different diseases?

Yes, the EFFICACY table has for primary key VaccineName and DiseaseName, which implies that the same vaccine can occur repeatedly as long as it is associated with different diseases.
 */
/*

II. Longer Questions (6 pts.)

Answer the following questions:

1. What does `CHECK (Website LIKE "https://*")` do?

It refrains any value not starting with  "https://" to be inserted as a value for the COMPANY.Website attribute.
Note that in particular it forbids a website from not being secured (that is, http:// is not a valid protocol).

2. Why did we picked the `DECIMAl(5,2)` datatype?

It is the appropriate datatype to represent percentage values represented as ranging from 100.00 to 0.00.
The discussion at https://stackoverflow.com/a/2762376/ also highlights that percent can be represented as decimal(5,4) with a check to insure that the value will range between 1.0000 and 0.0000.

3. What is the benefit / are the benefits of having a separate EFFICACY table over having something like

CREATE TABLE VACCINE(
 Name VARCHAR(50) PRIMARY KEY,
 Manufacturer VARCHAR(50),
 Disease VARCHAR(50),
 Efficacy DECIMAl(5,2),
 FOREIGN KEY (Manufacturer) REFERENCES COMPANY (Name)
);

?

This implementation does not allow to record that the same vaccine can have different efficacies for different diseases.
Stated differently, it forbids to represent vaccines efficient against multiple diseases faitfully.
 */
/*

III. Relational Model (6 pts.)

Draw the relational model corresponding to this code.
You can hand-draw it and join a scan or a picture, or simply hand me back a sheet.
 */
/*

IV. Simple Commands (5 pts.)

Below, you are asked to write commands that perform various actions.
Please, leave them uncommented, unless
 - you can not write them correctly, but want to share your attempt,
 - it is specified that it should return an error.

The first question is answered as an example.
 */
-- 0. Write a command that list the names of
--       all the diseases.
SELECT Name
FROM DISEASE;

-- 1. Write a command that insert "Pfizer" in the
--       COMPANY table (you can make up the website or look
--  it)
INSERT INTO COMPANY
VALUES (
  "Pfizer",
  "https://www.pfizer.com/");

--  2. Write a command that insert the "Pfizer-BioNTech
--	COVID-19 Vaccine" in the VACCINE table, and a
--  command
--	that store the efficacy of that vaccine against
--	the "Coronavirus disease 2019" disease
--       ( you can make up the values or look them up).
INSERT INTO VACCINE
VALUES (
  "Pfizer-BioNTech COVID-19 Vaccine",
  "Pfizer");

INSERT INTO EFFICACY
VALUES (
  "Coronavirus disease 2019",
  "Pfizer-BioNTech COVID-19 Vaccine",
  89);

--  3. Write a command that updates the name of the
--	company "Moderna" to "Moderna, Inc." everywhere.
UPDATE
  COMPANY
SET Name = "Moderna, Inc."
WHERE Name = "Moderna";

--  4. Write a command that lists the name of all the
--	companies.
SELECT Name
FROM COMPANY;

--  5. Write a command that deletes the "Coronavirus disease
--	2019" entry from the DISEASE table (if only!).
/*
DELETE FROM DISEASE
WHERE Name = "Coronavirus disease 2019";
 */
--  This command should return an error. Explain it and leave
--     the command commented.
--   The "Coronavirus disease 2019" value in DISEASE.Name is
--  refereed to by two entries in the EFFICACY table.
--   As the foreign key from EFFICACY.DiseaseName to
--  DISEASE.Name does not specify its policy "ON DELETE", its
--  default behavior is to restrict deletion, causing the
--  error.
--     6. Write two commands: one that adds "physiological"
-- to
--	the possible types of diseases, and one that
-- inserts
--	a physiological disease in the DISEASE table.
ALTER TABLE DISEASE MODIFY TYPE ENUM ("infectious",
  "deficiency", "hereditary", "physiological");

INSERT INTO DISEASE
VALUES (
  "Asthma",
  FALSE,
  "physiological");
  

  
INSERT INTO COMPANY VALUES (
  "MehulDrInc",
  "https://MehulDrInc.com");

INSERT INTO VACCINE VALUES (
  "Asthma",
  "MehulDrInc");
  
--  7 (difficult). Write a command that return the list of
--		   all the companies that manufacture a
--		   vaccine against "Coronavirus disease
--  2019".
SELECT VACCINE.Manufacturer
FROM VACCINE,
  EFFICACY
WHERE VACCINE.Name = EFFICACY.VaccineName
  AND EFFICACY.DiseaseName = "Coronavirus disease 2019";


SELECT COMPANY.Website FROM VACCINE, COMPANY WHERE VACCINE.Name = "Asthma" AND VACCINE.Manufacturer = COMPANY.Name;
-- end snippet solution
