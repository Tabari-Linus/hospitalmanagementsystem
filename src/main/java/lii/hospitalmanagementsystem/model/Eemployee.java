package lii.hospitalmanagementsystem.model;

public class Employee {
    private Long employeeId;
    private String firstName;
    private String surname;
    private String address;
    private long telephoneNo;

    public Employee() {}

    public Employee(Long employeeId, String firstName, String surname, String address, long telephoneNo) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.telephoneNo = telephoneNo;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(long telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", telephoneNo=" + telephoneNo +
                '}';
    }
}