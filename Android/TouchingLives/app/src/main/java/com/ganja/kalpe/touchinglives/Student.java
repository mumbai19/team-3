package com.ganja.kalpe.touchinglives;

public class Student {
    int age, phone, savings;
    String name, standard, address, medium, oldNew, mode, batch, centre_id, program;

    public Student(int age, int phone, int savings, String name, String standard, String address, String medium, String oldNew, String mode,
                   String batch, String centre_id, String program) {
        this.age = age;
        this.phone = phone;
        this.savings = savings;
        this.name = name;
        this.standard = standard;
        this.address = address;
        this.medium = medium;
        this.oldNew = oldNew;
        this.mode = mode;
        this.batch = batch;
        this.centre_id = centre_id;
        this.program = program;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setSavings(int savings) {
        this.savings = savings;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public void setOldNew(String oldNew) {
        this.oldNew = oldNew;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setCentre(String centre_id) {
        this.centre_id = centre_id;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public int getAge() {
        return age;
    }

    public int getPhone() {
        return phone;
    }

    public int getSavings() {
        return savings;
    }

    public String getName() {
        return name;
    }

    public String getStandard() {
        return standard;
    }

    public String getAddress() {
        return address;
    }

    public String getMedium() {
        return medium;
    }

    public String getOldNew() {
        return oldNew;
    }

    public String getMode() {
        return mode;
    }

    public String getBatch() {
        return batch;
    }

    public String getCentre_id() {
        return centre_id;
    }

    public String getProgram() {
        return program;
    }
}
