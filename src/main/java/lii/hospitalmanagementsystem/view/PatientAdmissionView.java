package lii.hospitalmanagementsystem.view;


public class PatientAdmissionView {
    private final Long patientId;
    private final String patientName;
    private final Integer wardNumber;
    private final String doctorName;
    private final String diagnosis;
    private final String admissionDate;

    public PatientAdmissionView(Long patientId, String patientName, Integer wardNumber,
                                String doctorName, String diagnosis, String admissionDate) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.wardNumber = wardNumber;
        this.doctorName = doctorName;
        this.diagnosis = diagnosis;
        this.admissionDate = admissionDate;
    }


    public Long getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public Integer getWardNumber() { return wardNumber; }
    public String getDoctorName() { return doctorName; }
    public String getDiagnosis() { return diagnosis; }
    public String getAdmissionDate() { return admissionDate; }
}