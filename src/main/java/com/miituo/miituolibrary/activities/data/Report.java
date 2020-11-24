package com.miituo.miituolibrary.activities.data;

import java.util.Date;

public class Report implements Comparable<Report>
{
    private int Id;
    private int odometro;
    private int recorridos;
    private double total;
    private Date fecha;
    private int condonacion;
    private String cupones;

    public Report() {}

    public Report(int id,int odometro,int recorridos,double total,Date fecha,int condonacion,String cupones) {
        Id = id;
        this.odometro=odometro;
        this.recorridos=recorridos;
        this.total=total;
        this.fecha=fecha;
        this.condonacion=condonacion;
        this.cupones=cupones;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getOdometro() {
        return odometro;
    }

    public void setOdometro(int odometro) {
        this.odometro = odometro;
    }

    public int getRecorridos() {
        return recorridos;
    }

    public void setRecorridos(int recorridos) {
        this.recorridos = recorridos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCondonacion() {
        return condonacion;
    }

    public void setCondonacion(int condonacion) {
        this.condonacion = condonacion;
    }

    public String getCupones() {
        return cupones;
    }

    public void setCupones(String cupones) {
        this.cupones = cupones;
    }

    @Override
    public int compareTo(Report f) {

        if (odometro < f.odometro) {
            return 1;
        }
        else if (odometro >  f.odometro) {
            return -1;
        }
        else {
            return 0;
        }
    }
    @Override
    public String toString(){
        return "odometro: "+this.odometro;
    }
}