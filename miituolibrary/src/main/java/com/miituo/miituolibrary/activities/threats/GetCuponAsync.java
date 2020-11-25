package com.miituo.miituolibrary.activities.threats;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.miituo.miituolibrary.activities.api.ApiClient;
import com.miituo.miituolibrary.activities.utils.SimpleCallBack;

public class GetCuponAsync extends AsyncTask<String, Void, Void> {


    Activity c;
    SimpleCallBack cb;
    String resp=null, url="";
    boolean status = false;
    public ProgressDialog progress;

    //Constructor
    public GetCuponAsync (Activity c, String url, SimpleCallBack cb) {
        this.c = c;
        this.url = url;
        this.cb = cb;
        progress = new ProgressDialog(c);
    }

    //preExecute: metodo de la familia AsyncTask
    // funciona para inicializar datos antes de ejecutar el hilo...
    /*protected void onPreExecute() {
        progress.setMessage("Consultando información");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }*/

    //DoInBackGround: en este metodo se hace la peticion  a la api
    //y hace el llamado al servidor
    @Override
    protected Void doInBackground(String... voids) {
        ApiClient ac = new ApiClient(c);
        try{
            status = false;
            resp = ac.getCupon(url);
            status = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            resp = e.getMessage();
            status = false;
        }
        return null;
    }

    //PostExecute: Metodo que llama al callBack para mandar a llamar al hilo mprincipal y realizar
    //la funcion...
    @Override
    protected void onPostExecute(Void aVoid) {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            cb.run(status, resp);
        }catch(Exception e){
            e.printStackTrace();
            cb.run(status, resp);
        }
    }
}
