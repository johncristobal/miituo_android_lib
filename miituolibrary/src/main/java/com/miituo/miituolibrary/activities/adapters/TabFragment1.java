package com.miituo.miituolibrary.activities.adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miituo.miituolibrary.R;
import com.miituo.miituolibrary.activities.PDFViewer;
import com.miituo.miituolibrary.activities.WebActivity;
import com.miituo.miituolibrary.activities.data.IinfoClient;
import com.miituo.miituolibrary.activities.data.Report;
import com.miituo.miituolibrary.activities.threats.GetPDFSync;
import com.miituo.miituolibrary.activities.utils.SimpleCallBack;

import java.text.DecimalFormat;
import java.util.List;

public class TabFragment1 extends Fragment {

    ExpandableListView expListView;
    TextView tDriver, tContractor, tAuto, tPlacas, tModelo, tLastReport, lbEdoCta, lbCambioPago;
    TextView lbNameDriver, lbCelDriver, lbNameContractor, lbCelContractor;
    TextView lbAuto2, lbPlacas2, lbModelo2, lbLastReport2;
    LinearLayout lbLastReport3, btnCambioPago;

    public static AlertDialog alertaPago;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tab1, container, false);
        expListView = new ExpandableListView(getContext());
        tDriver = v.findViewById(R.id.lbDriver);
        tContractor = v.findViewById(R.id.lbContractor);
        tAuto = v.findViewById(R.id.lbAuto);
        tPlacas = v.findViewById(R.id.lbPlacas);
        tModelo = v.findViewById(R.id.lbModelo);
        tLastReport = v.findViewById(R.id.lbLastReport);
        lbEdoCta = v.findViewById(R.id.lbEdoCta);
        lbCambioPago = v.findViewById(R.id.lbCambioPago);
        lbNameDriver = v.findViewById(R.id.lbNameDriver);
        lbNameContractor = v.findViewById(R.id.lbNameContractor);
        lbCelDriver = v.findViewById(R.id.lbCelDriver);
        lbCelContractor = v.findViewById(R.id.lbCelContractor);
        lbAuto2 = v.findViewById(R.id.lbAuto2);
        lbPlacas2 = v.findViewById(R.id.lbPlacas2);
        lbModelo2 = v.findViewById(R.id.lbModelo2);
        lbLastReport2 = v.findViewById(R.id.lbLastReport2);
        lbLastReport3 =(LinearLayout)  v.findViewById(R.id.lbLastReport3);
        btnCambioPago = (LinearLayout) v.findViewById(R.id.btnCambioPago);
        GetData();
        btnCambioPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WebActivity.class).putExtra("isPoliza",false));
            }
        });
        List<Report> list = IinfoClient.getInfoClientObject().getPolicies().getReports();
//        if (list != null && list.size() > 1) {
        if (IinfoClient.getInfoClientObject().getPolicies().getMensualidad() > 0) {
            lbLastReport3.setEnabled(true);
            lbLastReport3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ver ticket de cobro anterior
//                    startActivity(new Intent(getContext(), TicketActivity.class));
                    new GetPDFSync(getActivity(),false, IinfoClient.InfoClientObject.getPolicies().getId(),IinfoClient.InfoClientObject.getPolicies().getNoPolicy(), new SimpleCallBack() {
                        @Override
                        public void run(boolean status, String res) {
                            if(!status){
//            Toast.makeText(c,"descarga fallida",Toast.LENGTH_LONG).show();
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Descarga Fallida");
                                builder.setMessage("Por favor intentalo más tarde.");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertaPago.dismiss();
                                    }
                                });
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        alertaPago = builder.create();
                                        alertaPago.show();
                                    }
                                });
                            }else{
                                // Toast.makeText(c,"descarga exitosa",Toast.LENGTH_LONG).show();
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Descarga completa");
                                builder.setMessage("Ahora tu estado de cuenta se encuentra en tus descargas.");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertaPago.dismiss();
//                    c.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                                        startActivity(new Intent(getActivity(), PDFViewer.class).putExtra("isPoliza",false));
                                    }
                                });
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        alertaPago = builder.create();
                                        alertaPago.show();
                                    }
                                });
                            }
                        }
                    }).execute();
                }
            });
        } else {
            lbLastReport3.setEnabled(false);
            lbLastReport3.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.height=lbLastReport3.getLayoutParams().height;
            layoutParams.setMargins(0, 0, 0, 0);
            btnCambioPago.setLayoutParams(layoutParams);

        }
        return v;
    }

    private void GetData() {
        lbNameDriver.setText(IinfoClient.InfoClientObject.getClient().getName() + " " + IinfoClient.InfoClientObject.getClient().getLastName() + " " + IinfoClient.InfoClientObject.getClient().getMotherName());
        lbCelDriver.setText(IinfoClient.InfoClientObject.getClient().getCelphone());
        lbNameContractor.setText(IinfoClient.InfoClientObject.getClient().getNameContract());
        lbCelContractor.setText(IinfoClient.InfoClientObject.getClient().getCelphoneContract());

        lbAuto2.setText(IinfoClient.InfoClientObject.getPolicies().getVehicle().getDescription().getDescription());
        lbPlacas2.setText(IinfoClient.InfoClientObject.getPolicies().getVehicle().getPlates());
        lbModelo2.setText(String.valueOf(IinfoClient.InfoClientObject.getPolicies().getVehicle().getModel().getModel()));

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        Double dos = Double.parseDouble(IinfoClient.InfoClientObject.getPolicies().getLastOdometer() + "");
        String yourFormattedString = formatter.format(dos);
        lbLastReport2.setText(yourFormattedString + " kms");
    }

}
