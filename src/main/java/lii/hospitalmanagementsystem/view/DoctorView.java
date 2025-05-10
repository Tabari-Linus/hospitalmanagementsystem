package lii.hospitalmanagementsystem.view;


import lii.hospitalmanagementsystem.model.Doctor;
import lii.hospitalmanagementsystem.model.Employee;

public class DoctorView {
    private long employeeId;
    private String firstName;
    private String surname;
    private String address;
    private long telephoneNo;
    private long specialityId;
    private String specialityName;

    public DoctorView(Employee employee, Doctor doctor, String specialityName) {
        this.employeeId = employee.getEmployeeId();
        this.firstName = employee.getFirstName();
        this.surname = employee.getSurname();
        this.address = employee.getAddress();
        this.telephoneNo = employee.getTelephoneNo();
        this.specialityId = doctor.getSpecialityId();
        this.specialityName = specialityName;
    }


    public long getEmployeeId() { return employeeId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getFullName() { return firstName + " " + surname; }
    public String getAddress() { return address; }
    public long getTelephoneNo() { return telephoneNo; }
    public long getSpecialityId() { return specialityId; }
    public String getSpecialityName() {
        return specialityName;
    }
}