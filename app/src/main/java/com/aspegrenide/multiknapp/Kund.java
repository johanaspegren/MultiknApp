package com.aspegrenide.multiknapp;

public class Kund {
    private String adress;
    private String enhetDV;
    private String enhetTP;
    private String epostadress;
    private String epostadressExtra;
    private String fullName;
    private String gdpr;
    private String language;
    private String personalNumber;
    private String phoneNumberSMS;
    private String phoneNumberSMSExtra;
    private String subscribe;
    private String wantedFormat;
    private String wantedMedia;
    private String zip;


    // for Firebase connection, empty constructor
    public Kund() {
    }

    @Override
    public String toString() {
        return "Kund{" +
                "adress='" + adress + '\'' +
                ", enhetDV='" + enhetDV + '\'' +
                ", enhetTP='" + enhetTP + '\'' +
                ", epostadress='" + epostadress + '\'' +
                ", epostadressExtra='" + epostadressExtra + '\'' +
                ", fullName='" + fullName + '\'' +
                ", gdpr='" + gdpr + '\'' +
                ", language='" + language + '\'' +
                ", personalNumber='" + personalNumber + '\'' +
                ", phoneNumberSMS='" + phoneNumberSMS + '\'' +
                ", phoneNumberSMSExtra='" + phoneNumberSMSExtra + '\'' +
                ", subscribe='" + subscribe + '\'' +
                ", wantedFormat='" + wantedFormat + '\'' +
                ", wantedMedia='" + wantedMedia + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEnhetDV() {
        return enhetDV;
    }

    public void setEnhetDV(String enhetDV) {
        this.enhetDV = enhetDV;
    }

    public String getEnhetTP() {
        return enhetTP;
    }

    public void setEnhetTP(String enhetTP) {
        this.enhetTP = enhetTP;
    }

    public String getEpostadress() {
        return epostadress;
    }

    public void setEpostadress(String epostadress) {
        this.epostadress = epostadress;
    }

    public String getEpostadressExtra() {
        return epostadressExtra;
    }

    public void setEpostadressExtra(String epostadressExtra) {
        this.epostadressExtra = epostadressExtra;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGdpr() {
        return gdpr;
    }

    public void setGdpr(String gdpr) {
        this.gdpr = gdpr;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getPhoneNumberSMS() {
        return phoneNumberSMS;
    }

    public void setPhoneNumberSMS(String phoneNumberSMS) {
        this.phoneNumberSMS = phoneNumberSMS;
    }

    public String getPhoneNumberSMSExtra() {
        return phoneNumberSMSExtra;
    }

    public void setPhoneNumberSMSExtra(String phoneNumberSMSExtra) {
        this.phoneNumberSMSExtra = phoneNumberSMSExtra;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public String getWantedFormat() {
        return wantedFormat;
    }

    public void setWantedFormat(String wantedFormat) {
        this.wantedFormat = wantedFormat;
    }

    public String getWantedMedia() {
        return wantedMedia;
    }

    public void setWantedMedia(String wantedMedia) {
        this.wantedMedia = wantedMedia;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}