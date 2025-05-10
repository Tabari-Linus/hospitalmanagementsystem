package lii.hospitalmanagementsystem.model;


import java.time.LocalDate;

public class PatientTreatment {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private LocalDate treatmentDate;
    private String remarks;
    private Long patientAdmissionId;

    public PatientTreatment(Long id, Long patientId, Long doctorId, LocalDate treatmentDate,
                            String remarks, Long patientAdmissionId) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.treatmentDate = treatmentDate;
        this.remarks = remarks;
        this.patientAdmissionId = patientAdmissionId;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public Long getDoctorId() { return doctorId; }
    public LocalDate getTreatmentDate() { return treatmentDate; }
    public String getRemarks() { return remarks; }
    public Long getPatientAdmissionId() { return patientAdmissionId; }
}