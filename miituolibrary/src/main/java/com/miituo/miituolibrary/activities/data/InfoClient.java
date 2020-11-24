package com.miituo.miituolibrary.activities.data;

public class InfoClient
{
    /// <summary>
    /// Lista de polizas asociadas al Número Telefonico
    /// </summary>
    private InsurancePolicyDetail Policies;

    private ClientMovil Client;

    public InfoClient(InsurancePolicyDetail policies,ClientMovil client) {
        Policies = policies;
        Client=client;
    }

    public InsurancePolicyDetail getPolicies() {
        return Policies;
    }

    public void setPolicies(InsurancePolicyDetail policies) {
        Policies = policies;
    }

    public ClientMovil getClient() {
        return Client;
    }

    public void setClient(ClientMovil client) {
        Client = client;
    }
}