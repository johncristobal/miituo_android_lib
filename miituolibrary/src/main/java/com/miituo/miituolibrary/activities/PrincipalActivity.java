package com.miituo.miituolibrary.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.miituo.miituolibrary.R;
import com.miituo.miituolibrary.activities.adapters.VehicleModelAdapter;
import com.miituo.miituolibrary.activities.data.IinfoClient;
import com.miituo.miituolibrary.activities.data.InfoClient;
import com.miituo.miituolibrary.activities.threats.GetPoliciesData;
import com.miituo.miituolibrary.activities.utils.CallBack;
import com.miituo.miituolibrary.activities.utils.SimpleCallBack;

import java.util.List;

public class PrincipalActivity extends AppCompatActivity implements CallBack {

    String telefono;
    SharedPreferences app_preferences;
    public int idpoliza;

    private ListView vList;
    private VehicleModelAdapter vadapter;

    public String tok_basic, tokencliente;
    public static List<InfoClient> result;
    static AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        telefono = getIntent().getStringExtra("telefono");

        app_preferences = getSharedPreferences(getString(R.string.shared_name_prefs), Context.MODE_PRIVATE);
        getPolizasData(telefono);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getPolizasData(telefono);
    }

    public void getPolizasData(final String telefono){
        String url = "InfoClientMobil/Celphone/"+telefono;
        new GetPoliciesData(url, PrincipalActivity.this, new SimpleCallBack() {
            @Override
            public void run(boolean status, String res) {
                if (!status){
                    String data[] = res.split("@");
                    //launchAlert(data[1]);
                }else{
                    //tenemos polizas, recuperamos list y mandamos a sms...
                    SharedPreferences.Editor editor = app_preferences.edit();
                    editor.putString("polizas", res);
                    editor.putString("Celphone", telefono);
                    editor.apply();

                    Gson parseJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                    List<InfoClient> InfoList = parseJson.fromJson(res, new TypeToken<List<InfoClient>>() {
                    }.getType());

                    //final GlobalActivity globalVariable = (GlobalActivity) getApplicationContext();
                    //globalVariable.setPolizas(InfoList);

                    result = InfoList;
                    if (result.size() < 1) {
                        //showViews(true);
                    }else{
                        //showViews(false);
                        String na = result.get(0).getClient().getName();
                        app_preferences.edit().putString("nombre", na).apply();
                        long starttime = app_preferences.getLong("time", 0);
                        vList = (ListView) findViewById(R.id.listviewinfoclient);
                        //removeInvalidPolicies();
                        vadapter = new VehicleModelAdapter(getApplicationContext(), result, starttime, PrincipalActivity.this);

                        vadapter.notifyDataSetChanged();
                        vList.setAdapter(vadapter);
                        if(result.size() > 0) {
                            tokencliente = result.get(0).getClient().getToken();
                        }else{
                            tokencliente = "";
                        }

                        //vList.setAdapter(vadapter);
                        //vadapter.notifyDataSetChanged();
                        //swipeContainer.setRefreshing(false);
                    }
                }
            }
        }).execute();
    }

    @Override
    public void runInt(int value) {
        int velocidad = 0;
        if (velocidad >= 5) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalActivity.this);
            builder.setTitle("¡Vas manejando!");
            builder.setMessage("Reporta más tarde…");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Intent i = new Intent(PrincipalActivity.this, SyncActivity.class);
                    //startActivity(i);
                }
            });
            AlertDialog alerta = builder.create();
            alerta.show();
        } else {
            //Clic into row to launch activity
            InfoClient item = result.get(value);
            //static class -- set client in this part
            //LogHelper.log(this,LogHelper.user_interaction,"PrincipalActivity.insgurancesList","clik en la poliza: "+item.getPolicies().getId(), "","",  "", "");
            IinfoClient.setInfoClientObject(item);
            IinfoClient.getInfoClientObject().getClient().setCelphone(app_preferences.getString("Celphone", "0"));

            tok_basic = item.getClient().getToken();
            idpoliza = IinfoClient.getInfoClientObject().getPolicies().getId();

            //firebase to get tokne....temp for now
            //String token = IinfoClient.getInfoClientObject().getClient().getToken();

            //set token...
            //IinfoClient.getInfoClientObject().getClient().setToken(token);
            Intent i;
//            item.getPolicies().setReportState(13);
            if (!item.getPolicies().isHasVehiclePictures() && !item.getPolicies().isHasOdometerPicture()) {
                i = new Intent(PrincipalActivity.this, VehiclePictures.class);
                app_preferences.edit().putString("odometro", "first").apply();
                startActivity(i);
            } else if (!item.getPolicies().isHasVehiclePictures() && item.getPolicies().isHasOdometerPicture()) {
                i = new Intent(PrincipalActivity.this, VehiclePictures.class);
                app_preferences.edit().putString("odometro", "first").apply();
                app_preferences.edit().putString("solofotos", "1").apply();
                startActivity(i);
            } else if (item.getPolicies().isHasVehiclePictures() && !item.getPolicies().isHasOdometerPicture()) {
                i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                app_preferences.edit().putString("odometro", "first").apply();
                startActivity(i);
            } else if (item.getPolicies().getReportState() == 13) {
                i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                app_preferences.edit().putString("odometro", "mensual").apply();
                startActivity(i);
            } else if (item.getPolicies().getReportState() == 14) {
                i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                app_preferences.edit().putString("odometro", "cancela").apply();
                i.putExtra("isCancelada", true);
                startActivity(i);
            } else if (item.getPolicies().getReportState() == 15) {
                i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                app_preferences.edit().putString("odometro", "ajuste").apply();
                startActivity(i);
            }
//          else if (item.getPolicies().getReportState() == 21) {
//                app_preferences.edit().putString("solofotos", "1").apply();
//                getfotosFaltantes fotos = new getfotosFaltantes();
//                fotos.execute();
//            }
            else {
                if (item.getPolicies().getState().getId() == 15) {
                    i = new Intent(PrincipalActivity.this, InfoCancelActivity.class);
                } else {
                    i = new Intent(PrincipalActivity.this, DetallesActivity.class);
                }
                startActivity(i);
            }
        }
    }

    @Override
    public void runInt2(int value) {

        //Clic into row to launch activity
        InfoClient item = result.get(value);
        //static class -- set client in this part
        IinfoClient.setInfoClientObject(item);
        IinfoClient.getInfoClientObject().getClient().setCelphone(app_preferences.getString("Celphone", "0"));

        tok_basic = item.getClient().getToken();
        idpoliza = IinfoClient.getInfoClientObject().getPolicies().getId();
        //getNewQuotation(item.getPolicies().getId());
    }

    //TODO - call Atlas action -------------------------------------------------------------------------
    public void llamarAtlas(final InfoClient v) {

        //startActivity(new Intent(this, MapsActivity.class));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(v.getPolicies().getInsuranceCarrier().getName());
        builder.setMessage("¡Reportar siniestro!");
        builder.setPositiveButton("Llamar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alerta.dismiss();
                try {
                    String noTel = "8008493917";
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", noTel, null));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alerta.dismiss();
            }
        });
        alerta = builder.create();
        alerta.show();
    }
}