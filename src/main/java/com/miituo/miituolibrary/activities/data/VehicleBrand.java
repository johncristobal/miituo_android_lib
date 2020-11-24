package com.miituo.miituolibrary.activities.data;

public class VehicleBrand
{
    private int Id;
    private String Description;

    public VehicleBrand(int id, String description) {
        Id = id;
        Description = description;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
