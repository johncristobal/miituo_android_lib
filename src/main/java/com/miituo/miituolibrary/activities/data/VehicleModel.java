package com.miituo.miituolibrary.activities.data;


public class VehicleModel
{
    private int Id;
    private int Model;

    public VehicleModel(int id, int model) {
        Id = id;
        Model = model;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getModel() {
        return Model;
    }

    public void setModel(int model) {
        Model = model;
    }
}
