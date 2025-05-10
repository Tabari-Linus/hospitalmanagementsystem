package lii.hospitalmanagementsystem.view;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lii.hospitalmanagementsystem.model.Patient;

public class PatientView {
    private final LongProperty patientId;
    private final StringProperty firstName;
    private final StringProperty surname;
    private final StringProperty address;
    private final LongProperty telephoneNo;

    public PatientView(Patient patient) {
        this.patientId = new SimpleLongProperty(patient.getPatientId());
        this.firstName = new SimpleStringProperty(patient.getFirstName());
        this.surname = new SimpleStringProperty(patient.getSurname());
        this.address = new SimpleStringProperty(patient.getAddress());
        this.telephoneNo = new SimpleLongProperty(patient.getTelephoneNo());
    }

    public long getPatientId() { return patientId.get(); }
    public String getFirstName() { return firstName.get(); }
    public String getSurname() { return surname.get(); }
    public String getAddress() { return address.get(); }
    public long getTelephoneNo() { return telephoneNo.get(); }

    public LongProperty patientIdProperty() { return patientId; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty surnameProperty() { return surname; }
    public StringProperty addressProperty() { return address; }
    public LongProperty telephoneNoProperty() { return telephoneNo; }
}
