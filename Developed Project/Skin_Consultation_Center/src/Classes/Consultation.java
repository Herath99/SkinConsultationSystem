package Classes;

import java.time.LocalDate;

public class Consultation {
    private LocalDate date;
    private String time;
    private String notes;
    private Doctor doctor;
    private Patient patient;

    // Constructor
    public Consultation(LocalDate date, String time, String notes, Doctor doctor, Patient patient) {
        this.date = date;
        this.time = time;
        this.notes = notes;
        this.doctor = doctor;
        this.patient = patient;
    }

    // Getters and setters for the instance variables
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
