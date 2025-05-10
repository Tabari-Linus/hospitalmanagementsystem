package lii.hospitalmanagementsystem.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patient {
    private final LongProperty patientId = new SimpleLongProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty surname = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
    private final LongProperty telephoneNo = new SimpleLongProperty();

    public Patient() {}

    public Patient(Long patientId, String firstName, String surname, String address, long telephoneNo) {
        if (patientId != null) {
            setPatientId(patientId);
        }
        setFirstName(firstName);
        setSurname(surname);
        setAddress(address);
        setTelephoneNo(telephoneNo);
    }

    public LongProperty patientIdProperty() {
        return patientId;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public LongProperty telephoneNoProperty() {
        return telephoneNo;
    }

    public Long getPatientId() {
        return patientId.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getSurname() {
        return surname.get();
    }

    public String getAddress() {
        return address.get();
    }

    public long getTelephoneNo() {
        return telephoneNo.get();
    }

    public void setPatientId(Long patientId) {
        this.patientId.set(patientId != null ? patientId : 0L);
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setTelephoneNo(long telephoneNo) {
        this.telephoneNo.set(telephoneNo);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + getPatientId() +
                ", firstName='" + getFirstName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", telephoneNo=" + getTelephoneNo() +
                '}';
    }
}
