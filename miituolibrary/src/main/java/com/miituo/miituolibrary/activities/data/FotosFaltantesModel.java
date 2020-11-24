package com.miituo.miituolibrary.activities.data;

public class FotosFaltantesModel {

    public int Id;
    public String Descripcion;
    public String Id_Estatus;


    public FotosFaltantesModel(int id, String descripcion, String id_Estatus) {
        Id = id;
        Descripcion = descripcion;
        Id_Estatus = id_Estatus;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getId_Estatus() {
        return Id_Estatus;
    }

    public void setId_Estatus(String id_Estatus) {
        Id_Estatus = id_Estatus;
    }
}
