package lii.hospitalmanagementsystem.model;

public class PatientTransfer {
    private Long id;
    private Long patientId;
    private Long fromWard;
    private Long toWard;
    private String reason;
    private Long patientAdmissionId;

    public PatientTransfer(Long id, Long patientId, Long fromWard, Long toWard, String reason, Long patientAdmissionId) {
        this.id = id;
        this.patientId = patientId;
        this.fromWard = fromWard;
        this.toWard = toWard;
        this.reason = reason;
        this.patientAdmissionId = patientAdmissionId;
    }

    public Long getTransferId() {
        return id;
    }

    public void setTransferId(long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public Long getFromWardId() {
        return fromWard;
    }

    public void setFromWard(long fromWard) {
        this.fromWard = fromWard;
    }

    public Long getToWardId() {
        return toWard;
    }

    public void setToWard(long toWard) {
        this.toWard = toWard;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getPatientAdmissionId() {
        return patientAdmissionId;
    }

    public void setPatientAdmissionId(long patientAdmissionId) {
        this.patientAdmissionId = patientAdmissionId;
    }
}
