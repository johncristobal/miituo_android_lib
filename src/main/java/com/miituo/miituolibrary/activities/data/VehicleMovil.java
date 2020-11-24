package com.miituo.miituolibrary.activities.data;

public class VehicleMovil
{
    private int Id;
    private String Color;
    private String Plates;
    private String NoMotor;
    private int Capacity;
    private VehicleBrand Brand;
    private VehicleModel Model;
    private VehicleSubtype Subtype;
    private VehicleType Type;
    private VehicleDescription Description;

    public VehicleMovil() {}

    public VehicleMovil(int id, String color, String plates, String noMotor, int capacity, VehicleBrand brand, VehicleModel model, VehicleSubtype subtype, VehicleType type, VehicleDescription description) {
        Id = id;
        Color = color;
        Plates = plates;
        NoMotor = noMotor;
        Capacity = capacity;
        Brand = brand;
        Model = model;
        Subtype = subtype;
        Type = type;
        Description = description;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getPlates() {
        return Plates;
    }

    public void setPlates(String plates) {
        Plates = plates;
    }

    public String getNoMotor() {
        return NoMotor;
    }

    public void setNoMotor(String noMotor) {
        NoMotor = noMotor;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    public VehicleBrand getBrand() {
        return Brand;
    }

    public void setBrand(VehicleBrand brand) {
        Brand = brand;
    }

    public VehicleModel getModel() {
        return Model;
    }

    public void setModel(VehicleModel model) {
        Model = model;
    }

    public VehicleSubtype getSubtype() {
        return Subtype;
    }

    public void setSubtype(VehicleSubtype subtype) {
        Subtype = subtype;
    }

    public VehicleType getType() {
        return Type;
    }

    public void setType(VehicleType type) {
        Type = type;
    }

    public VehicleDescription getDescription() {
        return Description;
    }

    public void setDescription(VehicleDescription description) {
        Description = description;
    }
}