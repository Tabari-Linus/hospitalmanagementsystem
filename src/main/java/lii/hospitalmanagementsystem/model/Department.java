package lii.hospitalmanagementsystem.model;

public class Department {
    private Long departmentCode;
    private String departmentName;
    private String building;
    private long directorId;

    public Department() {}

    public Department(Long departmentCode, String departmentName, String building, long directorId) {
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.building = building;
        this.directorId = directorId;
    }

    public long getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(long departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(long directorId) {
        this.directorId = directorId;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentCode=" + departmentCode +
                ", departmentName='" + departmentName + '\'' +
                ", building='" + building + '\'' +
                ", directorId=" + directorId +
                '}';
    }
}
