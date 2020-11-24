package com.miituo.miituolibrary.activities.data;

import java.util.Date;
import java.util.List;

public class InsurancePolicyDetail
{
    /// <summary>
    /// Propiedad para determinar si ya ha subido las fotos del vehiculo
    /// </summary>
    private boolean HasVehiclePictures;
    /// <summary>
    /// Propiedad para determinar si ya tiene la foto del odometro
    /// </summary>
    private boolean HasOdometerPicture;
    /// <summary>
    /// Id de la poliza en nuestra tabla local
    /// </summary>
    private int Id;
    /// <summary>
    /// mensualidad de la poliza a reportar
    /// </summary>
    private int Mensualidad;
    /// <summary>
    /// Id generado por la aseguradora
    /// </summary>
    private String NoPolicy;
    /// <summary>
    /// Fecha de Inicio de Cobertura de la póliza
    /// </summary>
    private String PaymentType;

    private Date StartDate;
    /// <summary>
    /// Fecha de Vigencia de la póliza
    /// </summary>
    private Date VigencyDate;
    /// <summary>
    /// Tarifa por Kilometro aplicada a esta Poliza
    /// </summary>
    private float Rate;
    /// <summary>
    /// Ultima Lectura del Odometro
    /// </summary>
    private int LastOdometer;
    /// <summary>
    /// Fecha limite para reportar el odometro
    /// </summary>
    private Date LimitReportDate;
    /// <summary>
    /// Aseguradora de la Póliza
    /// </summary>
    private InsuranceCarrier InsuranceCarrier;
    /// <summary>
    /// Estado de la Póliza
    /// </summary>
    private InsurancePolicyState State;
    /// <summary>
    /// Tipo de Cobertura de la póliza
    /// </summary>
    private Coverage Coverage;
    /// <summary>
    /// Vehículo Asegurado
    /// </summary>
    public VehicleMovil Vehicle;


    List<Report> Reports=null;

    /*private Tickets Tickets;

    public Tickets getTickets() {
        return Tickets;
    }

    public void setTickets(Tickets tickets) {
        Tickets = tickets;
    }*/

    public List<Report> getReports() {
        return Reports;
    }

    public void setReports(List<Report> reports) {
        Reports = reports;
    }

    public int getRegOdometer() {
        return regOdometer;
    }

    public void setRegOdometer(int regOdometer) {
        this.regOdometer = regOdometer;
    }

    public int regOdometer;

    public int ReportState;

    public InsurancePolicyDetail(){}

    public InsurancePolicyDetail(boolean hasVehiclePictures,boolean hasOdometerPicture,int id, String noPolicy, Date startDate, Date vigencyDate, float rate, int lastOdometer, Date limitReportDate, InsuranceCarrier insuranceCarrier, InsurancePolicyState state, Coverage coverage, VehicleMovil vehicle,int ReportState, String PaymentType) {
        HasVehiclePictures=hasVehiclePictures;
        HasOdometerPicture=hasOdometerPicture;
        Id = id;
        NoPolicy = noPolicy;
        StartDate = startDate;
        VigencyDate = vigencyDate;
        Rate = rate;
        LastOdometer = lastOdometer;
        LimitReportDate = limitReportDate;
        InsuranceCarrier = insuranceCarrier;
        State = state;
        Coverage = coverage;
        Vehicle = vehicle;

        this.ReportState = ReportState;
        this.PaymentType = PaymentType;

        //Save regOdometer...

    }

    public int getReportState() {
        return ReportState;
    }

    public void setReportState(int reportState) {
        ReportState = reportState;
    }

    public int getId() {
        return Id;
    }

    public void setMensualidad(int mensualidad) {
        Mensualidad = mensualidad;
    }

    public int getMensualidad() {
        return Mensualidad;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNoPolicy() {
        return NoPolicy;
    }

    public void setNoPolicy(String noPolicy) {
        NoPolicy = noPolicy;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String noPolicy) {
        PaymentType = noPolicy;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getVigencyDate() {
        return VigencyDate;
    }

    public void setVigencyDate(Date vigencyDate) {
        VigencyDate = vigencyDate;
    }

    public float getRate() {
        return Rate;
    }

    public void setRate(float rate) {
        Rate = rate;
    }

    public int getLastOdometer() {
        return LastOdometer;
    }

    public void setLastOdometer(int lastOdometer) {
        LastOdometer = lastOdometer;
    }

    public Date getLimitReportDate() {
        return LimitReportDate;
    }

    public void setLimitReportDate(Date limitReportDate) {
        LimitReportDate = limitReportDate;
    }

    public InsuranceCarrier getInsuranceCarrier() {
        return InsuranceCarrier;
    }

    public void setInsuranceCarrier(InsuranceCarrier insuranceCarrier) {
        InsuranceCarrier = insuranceCarrier;
    }

    public InsurancePolicyState getState() {
        return State;
    }

    public void setState(InsurancePolicyState state) {
        State = state;
    }

    public Coverage getCoverage() {
        return Coverage;
    }

    public void setCoverage(Coverage coverage) {
        Coverage = coverage;
    }

    public VehicleMovil getVehicle() {
        return Vehicle;
    }

    public void setVehicle(VehicleMovil vehicle) {
        Vehicle = vehicle;
    }

    public boolean isHasOdometerPicture() {
        return HasOdometerPicture;
    }

    public void setHasOdometerPicture(boolean hasOdometerPicture) {
        HasOdometerPicture = hasOdometerPicture;
    }

    public boolean isHasVehiclePictures() {
        return HasVehiclePictures;
    }

    public void setHasVehiclePictures(boolean hasVehiclePictures) {
        HasVehiclePictures = hasVehiclePictures;
    }
}