package lii.hospitalmanagementsystem.model;


public class Ward {
    private Long wardId;
    private long departmentId;
    private int wardNumber;
    private long supervisorId;
    private int bedCount;

    public Ward() {}

    public Ward(Long wardId, long departmentId, int wardNumber, long supervisorId, int bedCount) {
        this.wardId = wardId;
        this.departmentId = departmentId;
        this.wardNumber = wardNumber;
        this.supervisorId = supervisorId;
        this.bedCount = bedCount;
    }

    public long getWardId() {
        return wardId;
    }

    public void setWardId(long wardId) {
        this.wardId = wardId;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public int getWardNumber() {
        return wardNumber;
    }

    public void setWardNumber(int wardNumber) {
        this.wardNumber = wardNumber;
    }

    public long getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(long supervisorId) {
        this.supervisorId = supervisorId;
    }

    public int getBedCount() {
        return bedCount;
    }

    public void setBedCount(int bedCount) {
        this.bedCount = bedCount;
    }

    @Override
    public String toString() {
        return "Ward{" +
                "wardId=" + wardId +
                ", departmentId=" + departmentId +
                ", wardNumber=" + wardNumber +
                ", supervisorId=" + supervisorId +
                ", bedCount=" + bedCount +
                '}';
    }
}
