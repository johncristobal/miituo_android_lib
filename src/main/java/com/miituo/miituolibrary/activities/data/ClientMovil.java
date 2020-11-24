package com.miituo.miituolibrary.activities.data;

public class ClientMovil
{
    private int Id;
    private String Name;
    private String LastName;
    private String MotherName;
    private String Celphone;
    private String Token;
    private String Datacelphone;
    private String NameContract;
    private String CelphoneContract;
    private String Cupon;

    private String Email;

    public ClientMovil(){}

    public ClientMovil(int id, String lastName, String motherName, String name) {
        Id = id;
        LastName = lastName;
        MotherName = motherName;
        Name = name;
        Celphone="";
        Token="";
        Email = "";
    }

    public String getDatacelphone() {
        return Datacelphone;
    }

    public String getNameContract() {
        return NameContract;
    }

    public void setNameContract(String nameContract) {
        NameContract = nameContract;
    }

    public String getCelphoneContract() {
        return CelphoneContract;
    }

    public void setCelphoneContract(String celphoneContract) {
        CelphoneContract = celphoneContract;
    }

    public String getCupon() {
        return Cupon;
    }

    public void setCupon(String cupon) {
        Cupon = cupon;
    }

    public void setDatacelphone(String datacelphone) {
        Datacelphone = datacelphone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String correo) {
        this.Email = correo;
    }

    public String getCelphone() {
        return Celphone;
    }

    public void setCelphone(String celphone) {
        Celphone = celphone;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getMotherName() {
        return MotherName;
    }

    public void setMotherName(String motherName) {
        MotherName = motherName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}