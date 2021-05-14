package unipi.protal.vaccineappointment.entities;

public class Claims {
    private String display_name;
    private Boolean doctor;
    private String email;
    private boolean patient;

    public Claims() {
    }

    public Claims(String display_name, Boolean doctor, String email, boolean patient) {
        this.display_name = display_name;
        this.doctor = doctor;
        this.email = email;
        this.patient = patient;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public Boolean getDoctor() {
        return doctor;
    }

    public void setDoctor(Boolean doctor) {
        this.doctor = doctor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPatient() {
        return patient;
    }

    public void setPatient(boolean patient) {
        this.patient = patient;
    }
}
