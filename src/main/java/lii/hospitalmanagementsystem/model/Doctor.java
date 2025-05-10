package lii.hospitalmanagementsystem.model;

public class Doctor {
    private long employeeId;
    private long specialityId;


    public Doctor() {}

    public Doctor(long employeeId, long specialityId) {
        this.employeeId = employeeId;
        this.specialityId = specialityId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(long specialityId) {
        this.specialityId = specialityId;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "employeeId=" + employeeId +
                ", specialityId=" + specialityId +
                '}';
    }
}