package lii.hospitalmanagementsystem.model;


public class DoctorDTO {
    private long employeeId;
    private String firstName;
    private String surname;
    private String address;
    private long telephoneNo;
    private String specialityName;

    public DoctorDTO(long employeeId, String firstName, String surname, String address,
                     long telephoneNo, String specialityName) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.telephoneNo = telephoneNo;
        this.specialityName = specialityName;
    }

    public long getEmployeeId() { return employeeId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public long getTelephoneNo() { return telephoneNo; }
    public String getSpecialityName() { return specialityName; }
}
