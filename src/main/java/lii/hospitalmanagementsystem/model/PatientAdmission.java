package lii.hospitalmanagementsystem.model;

import java.time.LocalDate;

public class PatientAdmission {
    private Long id;
    private Long patientId;
    private Long wardId;
    private Integer bedNumber;
    private String diagnosis;
    private LocalDate dateAdmitted;
    private LocalDate dateDischarged;

    public PatientAdmission(Long id, Long patientId, Long wardId, Integer bedNumber, String diagnosis, LocalDate dateAdmitted, LocalDate dateDischarged) {
        this.id = id;
        this.patientId = patientId;
        this.wardId = wardId;
        this.bedNumber = bedNumber;
        this.diagnosis = diagnosis;
        this.dateAdmitted = dateAdmitted;
        this.dateDischarged = dateDischarged;
    }


    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public Long getWardId() { return wardId; }
    public Integer getBedNumber() { return bedNumber; }
    public String getDiagnosis() { return diagnosis; }
    public LocalDate getDateAdmitted() { return dateAdmitted; }
    public LocalDate getDateDischarged() { return dateDischarged; }
    public void setDateDischarged(LocalDate dateDischarged) { this.dateDischarged = dateDischarged; }
    public void setWardId(Long wardId) { this.wardId = wardId; }
    public void setBedNumber(Integer bedNumber) { this.bedNumber = bedNumber; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
}