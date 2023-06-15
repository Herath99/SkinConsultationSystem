package Classes;

import java.io.Serializable;
import java.time.LocalDate;

public class Patient extends Person implements Serializable {
    private String patientId;

    // Constructor
    public Patient(String name, String surname, LocalDate dob, String mobileNumber, String patientId) {
        super(name, surname, dob, mobileNumber);
        this.patientId = patientId;
    }

    // Getters and setters for the patientId and illness instance variables
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

}
