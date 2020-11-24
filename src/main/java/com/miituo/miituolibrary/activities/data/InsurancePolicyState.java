package com.miituo.miituolibrary.activities.data;

public class InsurancePolicyState
{
    private int Id;
    private String Description;

    public InsurancePolicyState(int id, String description) {
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