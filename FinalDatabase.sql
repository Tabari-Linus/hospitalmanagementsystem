-- Database: hospital_db

-- DROP DATABASE IF EXISTS hospital_db;

CREATE DATABASE hospital_db
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en-US'
    LC_CTYPE = 'en-US'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
	



CREATE TABLE "employee" (
    "employee_id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "first_name" VARCHAR(255) NOT NULL,
    "surname" VARCHAR(255) NOT NULL,
    "address" VARCHAR(255) NOT NULL,
    "telephone_no" BIGINT NOT NULL
);

CREATE TABLE "speciality" (
    "speciality_id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL
);

CREATE TABLE "doctor" (
    "employee_id" BIGINT PRIMARY KEY,
    "speciality_id" BIGINT NOT NULL,
    FOREIGN KEY ("employee_id") REFERENCES "employee"("employee_id"),
    FOREIGN KEY ("speciality_id") REFERENCES "speciality"("speciality_id")
);

CREATE TABLE "department" (
    "department_code" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "department_name" VARCHAR(255) NOT NULL,
    "building" VARCHAR(255) NOT NULL,
    "director_id" BIGINT NOT NULL,
    FOREIGN KEY ("director_id") REFERENCES "doctor"("employee_id")
);

CREATE TABLE "nurse" (
    "employee_id" BIGINT PRIMARY KEY,
    "rotation" VARCHAR(255) NOT NULL,
    "salary" DECIMAL(10, 2) NOT NULL,
    "department_id" BIGINT NOT NULL,
    FOREIGN KEY ("employee_id") REFERENCES "employee"("employee_id"),
    FOREIGN KEY ("department_id") REFERENCES "department"("department_code")
);

CREATE TABLE "ward" (
    "ward_id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "department_id" BIGINT NOT NULL,
    "ward_number" INTEGER NOT NULL,
    "supervisor_id" BIGINT NOT NULL,
    "bed_count" INTEGER NOT NULL,
    FOREIGN KEY ("department_id") REFERENCES "department"("department_code"),
    FOREIGN KEY ("supervisor_id") REFERENCES "nurse"("employee_id"),
    UNIQUE ("department_id", "ward_number")
);

CREATE TABLE "patient" (
    "patient_id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "first_name" VARCHAR(255) NOT NULL,
    "surname" VARCHAR(255) NOT NULL,
    "address" VARCHAR(255) NOT NULL,
    "telephone_no" BIGINT NOT NULL
);

CREATE TABLE "patientadmission" (
    "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "patient_id" BIGINT NOT NULL,
    "ward_id" BIGINT NOT NULL,
    "bed_number" INTEGER NOT NULL,
    "diagnosis" TEXT NOT NULL,
    "date_admitted" DATE NOT NULL,
    "date_discharged" DATE,
    FOREIGN KEY ("patient_id") REFERENCES "patient"("patient_id"),
    FOREIGN KEY ("ward_id") REFERENCES "ward"("ward_id")
);

CREATE TABLE "patienttreatment" (
    "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "patient_id" BIGINT NOT NULL,
    "doctor_id" BIGINT NOT NULL,
    "treatment_date" DATE NOT NULL,
    "remarks" TEXT NOT NULL,
    "patientadmission_id" BIGINT NOT NULL,
    FOREIGN KEY ("patient_id") REFERENCES "patient"("patient_id"),
    FOREIGN KEY ("doctor_id") REFERENCES "doctor"("employee_id"),
    FOREIGN KEY ("patientadmission_id") REFERENCES "patientadmission"("id")
);

CREATE TABLE "patienttransfer" (
    "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "patient_id" BIGINT NOT NULL,
    "from_ward_id" BIGINT NOT NULL,
    "to_ward_id" BIGINT NOT NULL,
    "reason" TEXT NOT NULL,
    "patient_admission_id" BIGINT NOT NULL,
    FOREIGN KEY ("patient_id") REFERENCES "patient"("patient_id"),
    FOREIGN KEY ("from_ward_id") REFERENCES "ward"("ward_id"),
    FOREIGN KEY ("to_ward_id") REFERENCES "ward"("ward_id"),
    FOREIGN KEY ("patient_admission_id") REFERENCES "patientadmission"("id")
);

CREATE TABLE "useraccount" (
    "user_id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "username" VARCHAR(255) NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    "is_admin" BOOLEAN NOT NULL DEFAULT FALSE,
    "employee_id" BIGINT,
    FOREIGN KEY ("employee_id") REFERENCES "employee"("employee_id")
);


select * from employee;

