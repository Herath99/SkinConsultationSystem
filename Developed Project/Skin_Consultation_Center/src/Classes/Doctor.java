package Classes;

import java.io.Serializable;
import java.time.LocalDate;

public class Doctor extends Person implements Serializable {
    private String medicalLicenseNumber;
    private String speciality;

    // Constructor
    public Doctor(String name, String surname, LocalDate dob, String mobileNumber, String medicalLicenseNumber, String speciality) {
        super(name, surname, dob, mobileNumber);
        this.medicalLicenseNumber = medicalLicenseNumber;
        this.speciality = speciality;
    }

    // Getters and setters for the medicalLicenseNumber and speciality instance variables
    public String getMedicalLicenseNumber() {
        return medicalLicenseNumber;
    }

    public void setMedicalLicenseNumber(String medicalLicenseNumber) {
        this.medicalLicenseNumber = medicalLicenseNumber;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
