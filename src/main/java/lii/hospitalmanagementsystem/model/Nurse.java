package lii.hospitalmanagementsystem.model;

import java.math.BigDecimal;

public class Nurse {
    private long employeeId;
    private String rotation;
    private double salary;
    private long departmentId;

    public Nurse() {}

    public Nurse(long employeeId, String rotation, double salary, long departmentId) {
        this.employeeId = employeeId;
        this.rotation = rotation;
        this.salary = salary;
        this.departmentId = departmentId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Nurse{" +
                "employeeId=" + employeeId +
                ", rotation='" + rotation + '\'' +
                ", salary=" + salary +
                ", departmentId=" + departmentId +
                '}';
    }
}