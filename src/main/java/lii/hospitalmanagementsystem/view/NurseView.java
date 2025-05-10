package lii.hospitalmanagementsystem.view;

import lii.hospitalmanagementsystem.model.Employee;
import lii.hospitalmanagementsystem.model.Nurse;

public class NurseView {
    private long employeeId;
    private String firstName;
    private String surname;
    private String address;
    private long telephoneNo;
    private String department;
    private String rotation;
    private double salary;
    private long departmentId;

    public NurseView(Employee employee, Nurse nurse, String department) {
        this.employeeId = employee.getEmployeeId();
        this.firstName = employee.getFirstName();
        this.surname = employee.getSurname();
        this.address = employee.getAddress();
        this.telephoneNo = employee.getTelephoneNo();
        this.department = department;
        this.rotation = nurse.getRotation();
        this.salary = nurse.getSalary();
        this.departmentId = nurse.getDepartmentId();
    }


    public long getEmployeeId() { return employeeId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getFullName() { return firstName + " " + surname; }
    public String getAddress() { return address; }
    public long getTelephoneNo() { return telephoneNo; }
    public String getDepartmentName() { return department; }


    public String getRotation() { return rotation; }
    public double getSalary() { return salary; }
    public long getDepartmentId() { return departmentId; }

}