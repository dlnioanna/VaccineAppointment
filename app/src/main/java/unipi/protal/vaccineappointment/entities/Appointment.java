package unipi.protal.vaccineappointment.entities;

public class Appointment {
    private String name;
    private String lastName;
    private String telephone;
    private String firstDose;
    private String time;
    private String secondDose;

    public Appointment(){}

    public Appointment(String name, String lastName, String telephone, String firstDose, String time, String secondDose) {
        this.name = name;
        this.lastName = lastName;
        this.telephone = telephone;
        this.firstDose = firstDose;
        this.time = time;
        this.secondDose = secondDose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFirstDose() {
        return firstDose;
    }

    public void setFirstDose(String firstDose) {
        this.firstDose = firstDose;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSecondDose() {
        return secondDose;
    }

    public void setSecondDose(String secondDose) {
        this.secondDose = secondDose;
    }
}

