package lii.hospitalmanagementsystem;

import lii.hospitalmanagementsystem.databasecrud.*;
import lii.hospitalmanagementsystem.model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class HospitalSystemEndToEndTest {

    private static EmployeeCRUD employeeCRUD = new EmployeeCRUD();
    private static SpecialityCRUD specialityCRUD = new SpecialityCRUD();
    private static DoctorCRUD doctorCRUD = new DoctorCRUD();
    private static DepartmentCRUD departmentCRUD = new DepartmentCRUD();
    private static NurseCRUD nurseCRUD = new NurseCRUD();
    private static WardCRUD wardCRUD = new WardCRUD();
    private static PatientCRUD patientCRUD = new PatientCRUD();
    private static PatientAdmissionCRUD admissionCRUD = new PatientAdmissionCRUD();
    private static PatientTreatmentCRUD treatmentCRUD = new PatientTreatmentCRUD();
    private static PatientTransferCRUD transferCRUD = new PatientTransferCRUD();

    public static void main(String[] args) throws Exception {
        runTestScenario();
    }

    private static void runTestScenario() throws Exception {
        System.out.println("=== Starting Hospital System End-to-End Test ===");

        createInitialData();
        patientAdmissionWorkflow();
        try {
            patientTreatmentProcess();
        } catch (Exception e) {
            System.out.println("Error during treatment process: " + e.getMessage());
            throw new RuntimeException(e);
        }
        patientTransferProcess();
        try {
            dischargePatient();
        } catch (Exception e) {
            System.out.println("Error during discharge process: " + e.getMessage());
            throw new RuntimeException(e);
        }
        printFinalReports();

        System.out.println("=== Test Completed Successfully ===");
    }

    private static void createInitialData() throws Exception {
        System.out.println("\n--- Creating Initial Data ---");


        Speciality cardiology = new Speciality( null,"Cardiology");
        Speciality neurology = new Speciality(null, "Neurology");
        Speciality surgery = new Speciality(null, "General Surgery");

        specialityCRUD.insertSpeciality(cardiology);
        specialityCRUD.insertSpeciality(neurology);
        specialityCRUD.insertSpeciality(surgery);


        Employee emp1 = new Employee(null, "John", "Smith", "123 Main St", 5551234567L);
        Employee emp2 = new Employee(null, "Emily", "Johnson", "456 Oak Ave", 5552345678L);
        Employee emp3 = new Employee(null, "Michael", "Williams", "789 Pine Rd", 5553456789L);
        Employee emp4 = new Employee(null, "Sarah", "Brown", "321 Elm St", 5554567890L);
        Employee emp5 = new Employee(null, "David", "Jones", "654 Maple Dr", 5555678901L);
        Employee emp6 = new Employee(null, "David", "Jones", "654 Maple Dr", 5555678901L);

        long i = employeeCRUD.insertEmployee(emp1);
        long l =employeeCRUD.insertEmployee(emp2);
        long j = employeeCRUD.insertEmployee(emp3);
        long k =employeeCRUD.insertEmployee(emp4);
        long p = employeeCRUD.insertEmployee(emp5);
        long q = employeeCRUD.insertEmployee(emp6);


        doctorCRUD.insertDoctor(new Doctor(i, cardiology.getSpecialityId()));
        doctorCRUD.insertDoctor(new Doctor(l, neurology.getSpecialityId()));
        doctorCRUD.insertDoctor(new Doctor(j, surgery.getSpecialityId()));


        Department dept1 = new Department(null, "Cardiology", "Building A", i);
        Department dept2 = new Department(null, "Neurology", "Building B", j);
        Department dept3 = new Department(null, "Surgery", "Building C", l);

        departmentCRUD.insertDepartment(dept1);
        departmentCRUD.insertDepartment(dept2);
        departmentCRUD.insertDepartment(dept3);


        nurseCRUD.insertNurse(new Nurse(k, "Day", 65000.00, dept1.getDepartmentCode()));
        nurseCRUD.insertNurse(new Nurse(p, "Night", 68000.00, dept2.getDepartmentCode()));
        nurseCRUD.insertNurse(new Nurse(q, "Day", 70000.00, dept3.getDepartmentCode()));


        Ward ward1 = new Ward(null, dept1.getDepartmentCode(), 101, k, 20);
        Ward ward2 = new Ward(null, dept2.getDepartmentCode(), 201, p, 15);
        Ward ward3 = new Ward(null, dept1.getDepartmentCode(), 102, q, 10);

        wardCRUD.insertWard(ward1);
        wardCRUD.insertWard(ward2);
        wardCRUD.insertWard(ward3);

        System.out.println("Initial data created successfully.");
    }

    private static void patientAdmissionWorkflow() throws Exception {
        System.out.println("\n--- Patient Admission Workflow ---");


        List<Ward> wards = wardCRUD.getAllWards();
        if (wards.isEmpty()) {
            throw new RuntimeException("No wards available for admission");
        }
        Ward firstWard = wards.get(0);


        Patient patient = new Patient(null, "Robert", "Wilson", "987 Cedar Ln", 5556789012L);
        Patient patient1 = new Patient(null, "Alice", "Davis", "123 Birch St", 5557890123L);
        Patient patient2 = new Patient(null, "James", "Garcia", "456 Spruce St", 5558901234L);
        Patient patient3 = new Patient(null, "Linda", "Martinez", "789 Fir St", 5559012345L);
        patientCRUD.insertPatient(patient);
        patientCRUD.insertPatient(patient1);
        patientCRUD.insertPatient(patient2);
        patientCRUD.insertPatient(patient3);
        System.out.println("Patient created: " + patient);


        PatientAdmission admission = new PatientAdmission(
                null,
                patient.getPatientId(),
                firstWard.getWardId(),
                5,
                "Severe chest pain, suspected myocardial infarction",
                LocalDate.of(2023, 11, 15),
                LocalDate.of(2023, 11, 30)
        );
        PatientAdmission admission1 = new PatientAdmission(
                null,
                patient1.getPatientId(),
                firstWard.getWardId(),
                6,
                "Headache and dizziness, suspected stroke",
                LocalDate.of(2023, 11, 16),
                LocalDate.of(2023, 11, 30)
        );
        PatientAdmission admission2 = new PatientAdmission(
                null,
                patient2.getPatientId(),
                firstWard.getWardId(),
                7,
                "Abdominal pain, suspected appendicitis",
                LocalDate.of(2023, 11, 17),
                LocalDate.of(2023, 11, 30)
        );
        PatientAdmission admission3 = new PatientAdmission(
                null,
                patient3.getPatientId(),
                firstWard.getWardId(),
                8,
                "Fever and cough, suspected pneumonia",
                LocalDate.of(2023, 11, 18),
                LocalDate.of(2023, 11, 30)
        );

        if (admissionCRUD.insertPatientAdmission(admission)) {
            System.out.println("Patient admitted successfully:");
            System.out.println(admission);
        } else {
            System.err.println("Failed to admit patient");
        }
        if (admissionCRUD.insertPatientAdmission(admission1)) {
            System.out.println("Patient admitted successfully:");
            System.out.println(admission1);
        } else {
            System.err.println("Failed to admit patient");
        }
        if (admissionCRUD.insertPatientAdmission(admission2)) {
            System.out.println("Patient admitted successfully:");
            System.out.println(admission2);
        } else {
            System.err.println("Failed to admit patient");
        }
        if (admissionCRUD.insertPatientAdmission(admission3)) {
            System.out.println("Patient admitted successfully:");
            System.out.println(admission3);
        } else {
            System.err.println("Failed to admit patient");
        }
    }

    private static void patientTreatmentProcess() throws Exception {
        System.out.println("\n--- Patient Treatment Process ---");


        List<Patient> patients = patientCRUD.getAllPatients();
        if (patients.isEmpty()) {
            throw new RuntimeException("No patients available for treatment");
        }
        Patient patient = patients.get(0);

        List<Doctor> doctors = doctorCRUD.getAllDoctors();
        if (doctors.isEmpty()) {
            throw new RuntimeException("No doctors available");
        }
        Doctor doctor = doctors.get(0);


        admissionCRUD.insertPatientAdmission( new PatientAdmission(
                null,
                patient.getPatientId(),
                1L,
                1,
                "Initial assessment",
                LocalDate.of(2023, 11, 15),
                null
        ));

        List<PatientAdmission> admissions = admissionCRUD.getAdmissionsByPatient(patient.getPatientId());
        if (admissions.isEmpty()) {
            throw new RuntimeException("No admissions found for patient");
        }
        PatientAdmission admission = admissions.get(0);


        PatientTreatment treatment1 = new PatientTreatment(
                null,
                patient.getPatientId(),
                doctor.getEmployeeId(),
                LocalDate.of(2023, 11, 15),
                "Initial assessment, ordered ECG and blood tests",
                admission.getId()
        );

        PatientTreatment treatment2 = new PatientTreatment(
                null,
                patient.getPatientId(),
                doctor.getEmployeeId(),
                LocalDate.of(2023, 12, 15),
                "Confirmed myocardial infarction, started medication",
                admission.getId()
        );

        PatientTreatment treatment3 = new PatientTreatment(
                null,
                patient.getPatientId(),
                doctor.getEmployeeId(),
                LocalDate.of(2022, 11, 15),
                "Performed angioplasty, patient responding well",
                admission.getId()
        );

        treatmentCRUD.insertPatientTreatment(treatment1);
        treatmentCRUD.insertPatientTreatment(treatment2);
        treatmentCRUD.insertPatientTreatment(treatment3);

        System.out.println("Treatments recorded for patient " + patient.getPatientId() + ":");
        List<PatientTreatment> treatments = treatmentCRUD.getTreatmentsByPatient(patient.getPatientId());
        treatments.forEach(System.out::println);
    }

    private static void patientTransferProcess() throws Exception {
        System.out.println("\n--- Patient Transfer Process ---");


        List<Patient> patients = patientCRUD.getAllPatients();
        if (patients.isEmpty()) {
            throw new RuntimeException("No patients available for transfer");
        }
        Patient patient = patients.get(0);

        List<PatientAdmission> admissions = admissionCRUD.getAdmissionsByPatient(patient.getPatientId());
        if (admissions.isEmpty()) {
            throw new RuntimeException("No admissions found for patient");
        }
        PatientAdmission admission = admissions.get(0);


        List<Ward> allWards = wardCRUD.getAllWards();
        if (allWards.size() < 2) {
            throw new RuntimeException("Not enough wards for transfer");
        }
        Ward fromWard = wardCRUD.getWardById(admission.getWardId());

        Ward toWard = allWards.stream()
                .filter(w -> !Objects.equals(w.getWardId(), fromWard.getWardId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No suitable ward found for transfer"));

        PatientTransfer transfer = new PatientTransfer(
                null,
                patient.getPatientId(),
                fromWard.getWardId(),
                toWard.getWardId(),
                "Neurological symptoms observed, consult with neurologist required",
                admission.getId()
        );

        if (transferCRUD.insertTransfer(transfer)) {
            System.out.println("Transfer recorded successfully:");
            System.out.println(transfer);


            admission.setWardId(toWard.getWardId());
            admission.setBedNumber(3);

            if (admissionCRUD.updateAdmission(admission)) {
                System.out.println("Admission updated to new ward:");
                System.out.println(admission);


                List<Doctor> neurologists = doctorCRUD.getDoctorsBySpeciality(2L); // Assuming 2 is neurology
                if (neurologists.isEmpty()) {
                    throw new RuntimeException("No neurologists available");
                }
                Doctor neurologist = neurologists.get(0);


                PatientTreatment treatment4 = new PatientTreatment(
                        null,
                        patient.getPatientId(),
                        neurologist.getEmployeeId(),
                        LocalDate.of(2023, 11, 20),
                        "Neurological consultation, ordered MRI",
                        admission.getId()
                );

                PatientTreatment treatment5 = new PatientTreatment(
                        null,
                        patient.getPatientId(),
                        neurologist.getEmployeeId(),
                        LocalDate.of(2023, 11, 21),
                        "MRI results reviewed, no neurological damage found",
                        admission.getId()
                );

                treatmentCRUD.insertPatientTreatment(treatment4);
                treatmentCRUD.insertPatientTreatment(treatment5);

                System.out.println("Additional treatments after transfer:");
                treatmentCRUD.getTreatmentsByPatient(patient.getPatientId()).forEach(System.out::println);
            }
        }
    }

    private static void dischargePatient() throws Exception {
        System.out.println("\n--- Patient Discharge Process ---");


        List<PatientAdmission> admissions = admissionCRUD.getAllAdmissions();
        if (admissions.isEmpty()) {
            throw new RuntimeException("No admissions found");
        }
        PatientAdmission admission = admissions.get(0);


        admission.setDateDischarged(LocalDate.of(2023, 11, 25));

        if (admissionCRUD.updateAdmission(admission)) {
            System.out.println("Patient discharged successfully. Final admission details:");
            System.out.println(admission);
        } else {
            System.err.println("Failed to update discharge date");
        }
    }

    private static void printFinalReports() throws Exception {
        System.out.println("\n--- Final Reports ---");

        System.out.println("\nAll Departments:");
        departmentCRUD.getAllDepartments().forEach(dept -> {
            try {
                System.out.println(dept);
                Employee director = employeeCRUD.getEmployeeById(dept.getDirectorId());
                System.out.println("  Director: " + director.getFirstName() + " " + director.getSurname());
            } catch (Exception e) {
                System.err.println("Error getting department info: " + e.getMessage());
            }
        });

        System.out.println("\nAll Patients:");
        patientCRUD.getAllPatients().forEach(System.out::println);

        System.out.println("\nPatient Admission History:");
        admissionCRUD.getAllAdmissions().forEach(admission -> {
            try {
                Patient patient = patientCRUD.getPatientById(admission.getPatientId());
                Ward ward = wardCRUD.getWardById(admission.getWardId());
                System.out.printf("Patient: %s %s, Ward: %d, Admitted: %s, Discharged: %s%n",
                        patient.getFirstName(), patient.getSurname(),
                        ward.getWardNumber(),
                        admission.getDateAdmitted(),
                        admission.getDateDischarged());
            } catch (Exception e) {
                System.err.println("Error getting admission info: " + e.getMessage());
            }
        });

        System.out.println("\nAll Treatments:");
        treatmentCRUD.getAllTreatments().forEach(treatment -> {
            try {
                Patient patient = patientCRUD.getPatientById(treatment.getPatientId());
                Employee doctor = employeeCRUD.getEmployeeById(treatment.getDoctorId());
                System.out.printf("%s - %s %s treated by Dr. %s: %s%n",
                        treatment.getTreatmentDate(),
                        patient.getFirstName(), patient.getSurname(),
                        doctor.getSurname(),
                        treatment.getRemarks());
            } catch (Exception e) {
                System.err.println("Error getting treatment info: " + e.getMessage());
            }
        });

        System.out.println("\nCurrent Ward Occupancy:");
        wardCRUD.getAllWards().forEach(ward -> {
            try {
                Department dept = departmentCRUD.getDepartmentById(ward.getDepartmentId());
                long occupiedBeds = admissionCRUD.getCurrentAdmissionsByWard(ward.getWardId()).size();
                System.out.printf("%s Ward %d: %d/%d beds occupied%n",
                        dept.getDepartmentName(),
                        ward.getWardNumber(),
                        occupiedBeds,
                        ward.getBedCount());
            } catch (Exception e) {
                System.err.println("Error getting ward info: " + e.getMessage());
            }
        });
    }
}