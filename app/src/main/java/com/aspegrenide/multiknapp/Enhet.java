package com.aspegrenide.multiknapp;

public class Enhet {
    private String namn;
    private String typ;
    private boolean checked;

    // for Firebase connection, empty constructor
    public Enhet() {
    }

    public Enhet(String namn, String typ) {
        this.namn = namn;
        this.typ = typ;
    }

    @Override
    public String toString() {
        return "Enhet{" +
                "namn='" + namn + '\'' +
                ", typ='" + typ + '\'' +
                '}';
    }

    public String getNamn() {
        return namn;
    }

    public void setNamn(String namn) {
        this.namn = namn;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

// checked is a temporary var to keep track if the correspeonding checkbox should be selected
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
