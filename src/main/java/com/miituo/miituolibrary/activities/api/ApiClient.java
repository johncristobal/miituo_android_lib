package com.miituo.miituolibrary.activities.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.miituo.miituolibrary.activities.VehicleOdometer;
import com.miituo.miituolibrary.activities.data.ClientMovil;
import com.miituo.miituolibrary.activities.data.FotosFaltantesModel;
import com.miituo.miituolibrary.activities.data.IinfoClient;
import com.miituo.miituolibrary.activities.data.InfoClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ApiClient {

    protected String UrlApi;

    public String UrlCotiza;
    public String pathPhotos;
    private Context c;
    public static int ambiente = 0;
    //  0->DEV
    //  1->QA
    //  2->Multiasegs
    //  3->MaquinaLocal
    //  4->PROD

    protected String miituoKey="Basic +e9Kte71youZLjbIGSlfU5FKO7bL40hgVzpuRzi8EKpDbyXpedQa6DKW2bp7lvi2G+Gy3v11UJOp/ReETiLpTJgkpFyzwSe9fTZUTO/XUBg=";

    public ApiClient(Context c){
        this.c=c;
        if(ambiente==0){
            UrlApi="https://apidev2019.miituo.com/api/"; //desarrollo
            UrlCotiza= "https://websitedev2019.miituo.com/Cotizar";
            miituoKey="Basic yTZ/1bWv6Mpi85ZDkwK4AvJVZH2zA6hcdk0BecEoP4upNOLi2hLM4fzzcPmmSe8UUI5EcJoT4dySqIStZaqvCnlo4dLCASmhjZInXhRlcdc=";
            pathPhotos = "https://filesdev.miituo.com/";
        }
        if(ambiente==1){
            UrlApi="https://apiqas2019.miituo.com/api/";
            UrlCotiza = "https://websiteqas2019.miituo.com/Cotizar";//control de calidad
            miituoKey="Basic yTZ/1bWv6Mpi85ZDkwK4AvJVZH2zA6hcdk0BecEoP4upNOLi2hLM4fzzcPmmSe8UUI5EcJoT4dySqIStZaqvCnlo4dLCASmhjZInXhRlcdc=";
            pathPhotos = "https://filesqa.miituo.com/";
        }
        if(ambiente==2){
            UrlApi="http://api.netdev.miituo.com/api/";
            pathPhotos="https://filesdev.miituo.com/";
        } //desarrollo version 2.0
        if(ambiente==3){ UrlApi="http://192.168.1.28:8080/api/"; } //Servidor local, aun no funciona en linea
        if(ambiente==4){
            UrlApi="https://api.miituo.com/api/";
            UrlCotiza = "https://miituo.com/Cotizar";
            pathPhotos="https://files.miituo.com/"; //https://files.miituo.com/6528/FROM_VEHICLE.png
        } //esta en linea...
    }


    public String getDatos(String Url) throws IOException {

        final String TAG = "JsonParser.java";
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url+"");
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else
                in = urlConnection.getInputStream();
            String jsonstring="";
            try {
                jsonstring=getStringFromInputStream(in);
                if(jsonstring!=null || jsonstring!="") {
                    return jsonstring;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public String getUrl(String Url) throws IOException
    {
        final String TAG = "JsonParser.java";
        InputStream in = null;

        try {
            URL url = new URL( UrlApi+Url+"");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization", miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();

            //Se verifica que la conexion sea exitosa...
            if (statusCode != 200) {
                String jsonstring = "";
                //Log.d(TAG, "The response is: " + statusCode);
            }
            //Si la conexion es exitosa se obtiene la cadena del server...
            else
                in = urlConnection.getInputStream();
            String jsonstring="";

            //Se obtiene la cadena del bufer de entrada para retornarla y trabajar en el call back
            //Mientras no sea vacia o nula...
            try {
                jsonstring=getStringFromInputStream(in);
                if(jsonstring!=null || jsonstring!="") {
                    return jsonstring;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        //Cerramos el buffer de entrada ...
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Método para obtener kilometraje cupon...
    public String getCupon(String Url) throws IOException
    {

        //Crear el buffer de entrada y apuntarlo a null
        final String TAG = "JsonParser.java";
        InputStream in=null;

        //Se crea la conexion y se realiza la peticion mediante el metodo getResponse()
        try {
            URL url = new URL(UrlApi+Url+"");
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();

            //Se verifica que la conexion sea exitosa...
            if (statusCode != 200) {
                String jsonstring = "";
                //Log.d(TAG, "The response is: " + statusCode);
            }
            //Si la conexion es exitosa se obtiene la cadena del server...
            else
                in = urlConnection.getInputStream();
            String jsonstring="";

            //Se obtiene la cadena del bufer de entrada para retornarla y trabajar en el call back
            //Mientras no sea vacia o nula...
            try {
                jsonstring=getStringFromInputStream(in);
                if(jsonstring!=null || jsonstring!="") {
                    return jsonstring;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        //Cerramos el buffer de entrada ...
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }
    //Enviamos odometro por pirmera vez-----------------------------------------------------------------
    public String postDatos(String Url,String body) throws IOException
    {
        InputStream in=null;
        String jsonstring="";
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.connect();

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(body);
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                jsonstring=getStringFromInputStream(in);
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
        return  jsonstring;
    }

    public String getPDF(Context context,String url,String noPoliza){
        String res=null;
        try {
            URL u = new URL(UrlApi+url);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-Type", "application/pdf");
            c.setDoOutput(false);
            c.connect();
//            File f1=new File(context.getExternalFilesDir(null),"poliza.pdf");
            File f1=new File("/sdcard/Download/",noPoliza+".pdf");
//            File f1=new File(context.getFilesDir(),"poliza.pdf");
            FileOutputStream f = new FileOutputStream(f1);
            System.out.println("El archivo en: "+f1.getAbsolutePath());
            InputStream in;// = c.getInputStream();
            int status = c.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                in = c.getErrorStream();
                String jsonstring=getStringFromInputStream(in);
                return null;
            }
            else
                in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer,0, len1);
            }
            //DetallesActivity.pdf=f1;
            f.close();
            res="OK";
        }
        catch (Exception e){
            e.printStackTrace();
            res=null;
        }
        return res;
    }

    //Metodo para recuperar datos del cliente a partir del celular----------------------------------------------------------------
    public List<InfoClient> getInfoClient(String Url, Context context) throws IOException
    {
        final String TAG = "JsonParser.java";
        List<InfoClient> InfoList=null;
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    ////LogHelper.log(this.c,//LogHelper.request,Url,"error obteniendo polizas","",jsonstring, "","responseCode: "+statusCode);
                    String msg=error.getString("Message");
                    throw new IOException(msg);
                }
                catch (JSONException ex)
                {
                    ////LogHelper.log(this.c,//LogHelper.request,Url,"","", jsonstring, "",//LogHelper.getException(ex));
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else
                in = urlConnection.getInputStream();
            JSONArray resultJson=null;
            String jsonstring="";
            try {
                jsonstring=getStringFromInputStream(in);
                //Log.w("Data",jsonstring);
                if(jsonstring!=null || jsonstring!="") {
                    Gson parseJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                    InfoList = parseJson.fromJson(jsonstring, new TypeToken<List<InfoClient>>() {
                    }.getType());


                    String test = "";
                    for(int i=0;i<InfoList.size();i++) {
                        try {
                            JSONArray json = new JSONArray(jsonstring);
                            JSONObject object = (JSONObject) new JSONTokener(json.getString(i)).nextValue();
                            JSONObject json2 = object.getJSONObject("Policies");
                            String report = json2.getString("ReportState");

                            if (report.equals("15") || report.equals("14")) {
                                JSONArray objeto = json2.getJSONArray("Tickets");
                                JSONObject lastobj = objeto.getJSONObject(0);
                                SharedPreferences pre = context.getSharedPreferences("miituo", Context.MODE_PRIVATE);
                                pre.edit().putString("idticket", lastobj.getString("Id")).apply();
                                break;
                            }

                        } catch (JSONException e) {
                            test = "";
                            //LogHelper.log(this.c,//LogHelper.request,Url,"error en lectura de polizas (tickets)","",jsonstring, "",//LogHelper.getException(e));
                            e.printStackTrace();
                            continue;
                        }
                    }
                    //idTicket = lastobj.getString("Id");
                }
            } catch (Exception e) {
                //LogHelper.log(this.c,//LogHelper.request,Url,"","",jsonstring, "",//LogHelper.getException(e));
                e.printStackTrace();
            }
            //LogHelper.log(this.c,//LogHelper.request,Url,"","",jsonstring, "","");
            return InfoList;

            //InputStream in=new BufferedInputStream(urlConnection.getInputStream());
        }
        catch (IOException e1){
            //LogHelper.log(this.c,//LogHelper.request,Url,"", "","", "",//LogHelper.getException(e1));
            throw new IOException(e1.getMessage());
        }
        catch (Exception e){
            //LogHelper.log(this.c,//LogHelper.request,Url,"", "","", "",//LogHelper.getException(e));
            e.printStackTrace();
//                throw new IOException(e.getMessage());
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
        return InfoList;
    }

    //recuperamos solo string de polizas
    public String getInfoClientData(String Url,Context context)
    {
        final String TAG = "JsonParser.java";
        List<InfoClient> InfoList=null;
        InputStream in=null;
        try {
            URL url = new URL(UrlApi + Url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring = "";
                //Log.d(TAG, "The response is: " + statusCode);
                in = urlConnection.getErrorStream();
                jsonstring = getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    //LogHelper.log(this.c, //LogHelper.request, Url, "error obteniendo polizas", "", jsonstring, "", "responseCode: " + statusCode);
                    String msg = error.getString("Message");
                    return "error@"+msg;
                } catch (JSONException ex) {
                    //LogHelper.log(this.c, //LogHelper.request, Url, "", "", jsonstring, "", //LogHelper.getException(ex));
                    return "error@Error al Convertir Respuesta de Error";
                }finally {
                    in.close();
                }
            } else {
                in = urlConnection.getInputStream();
                String jsonstring = "";
                try {
                    jsonstring = getStringFromInputStream(in);
                    //LogHelper.log(this.c, //LogHelper.request, Url, "", "", jsonstring, "", "");
                    Gson parseJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                    InfoList = parseJson.fromJson(jsonstring, new TypeToken<List<InfoClient>>() {
                    }.getType());

                    for(int i=0;i<InfoList.size();i++) {
                        try {
                            JSONArray json = new JSONArray(jsonstring);
                            JSONObject object = (JSONObject) new JSONTokener(json.getString(i)).nextValue();
                            JSONObject json2 = object.getJSONObject("Policies");
                            String report = json2.getString("ReportState");

                            if (report.equals("15") || report.equals("14")) {
                                JSONArray objeto = json2.getJSONArray("Tickets");
                                JSONObject lastobj = objeto.getJSONObject(0);
                                SharedPreferences pre = context.getSharedPreferences("miituo", Context.MODE_PRIVATE);
                                pre.edit().putString("idticket", lastobj.getString("Id")).apply();
                                break;
                            }

                        } catch (JSONException e) {
                            //LogHelper.log(this.c,//LogHelper.request,Url,"error en lectura de polizas (tickets)","",jsonstring, "",//LogHelper.getException(e));
                            e.printStackTrace();
                            return "error@"+e.getMessage();
                        }
                    }

                    return jsonstring;

                } catch (Exception e) {
                    //LogHelper.log(this.c, //LogHelper.request, Url, "", "", jsonstring, "", //LogHelper.getException(e));
                    e.printStackTrace();
                    return "error@"+e.getMessage();
                }
                finally {
                    in.close();
                }
            }
        }
        catch (IOException e1){
            //LogHelper.log(this.c,//LogHelper.request,Url,"", "","", "",//LogHelper.getException(e1));
            return "error@"+e1.getMessage();
        }
        catch (Exception e){
            //LogHelper.log(this.c,//LogHelper.request,Url,"", "","", "",//LogHelper.getException(e));
            e.printStackTrace();
            return "error@"+e.getMessage();
        }
    }

    //Metodo para recuperar datos del cliente a partir del celular----------------------------------------------------------------
    public String getQuotation(String Url,Context context) throws IOException
    {
        String resp=null;
        InputStream in=null;
        try {
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Authorization",miituoKey);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else
                in = urlConnection.getInputStream();
            try {
                resp=getStringFromInputStream(in);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("Error al Convertir Respuesta de Error");
            }
            return resp;
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public static String getStringFromInputStream(InputStream stream) throws IOException
    {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, "UTF8");
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }

    //Subir fotografia----------------------------------------------------------------
    public int UploadPhoto(int Type, Bitmap img, String Url, String tok, String appOdo, String lat, String lon, String cp) throws IOException
    {
        InputStream in=null;
        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";
        DataOutputStream outputStream = null;

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        JSONObject o=new JSONObject();
        try {

            //LogHelper.request,Url,"inicio subir foto","","",  "id: "+ IinfoClient.getInfoClientObject().getPolicies().getId()+" token: "+tok+" Type: "+Type +" AppOdo: "+appOdo+" lat: "+lat+" lng: "+lon+" cp: "+cp ,"");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            //urlConnection.setRequestProperty("Content-Type", "application/json");
            //urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",tok);

//            outputStream = new DataOutputStream(urlConnection.getOutputStream());
//            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
//            outputStream.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"file"+Type+".jpg\"" + lineEnd);
//            outputStream.writeBytes("Content-Type: image/jpg" + lineEnd);
//            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
//            outputStream.writeBytes(lineEnd);
//            outputStream.write(byteArray);
//            outputStream.writeBytes(lineEnd);

            String filename = Type+"";
            String filename2 = IinfoClient.getInfoClientObject().getPolicies().getId()+"";
            String filename3 = IinfoClient.getInfoClientObject().getPolicies().getNoPolicy()+"";
            String parmas [][]= {
                    {"Type", filename},
                    {"PolicyId", filename2},
                    {"PolicyFolio", filename3},
                    {"AppOdometer", appOdo},
                    {"Lat", lat},
                    {"Lng", lon},
                    {"AppZipCode", cp},
                    {"InsuranceId", String.valueOf(IinfoClient.getInfoClientObject().getPolicies().getInsuranceCarrier().getId())}
            };
            o.put("Type",Type).put("PolicyId",filename2).put("PolicyFolio",filename3)
                    .put("AppOdometer",appOdo).put("Lat",lat).put("Lng",lon).put("AppZipCode",cp)
                    .put("InsuranceId",String.valueOf(IinfoClient.getInfoClientObject().getPolicies().getInsuranceCarrier().getId()));
            //LogHelper.log(this.c,//LogHelper.request,Url,"subiendo foto",o.toString(),"",  "id: "+ IinfoClient.getInfoClientObject().getPolicies().getId(),"");

            // Upload POST Data

            outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"file"+Type+".jpg\"" + lineEnd);
            outputStream.writeBytes("Content-Type: image/jpg" + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            outputStream.writeBytes(lineEnd);
            outputStream.write(byteArray);
            outputStream.writeBytes(lineEnd);

            for (int i=0;i<8;i++) {
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + parmas[i][0] + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(parmas[i][1]);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                //LogHelper.log(this.c,//LogHelper.request,Url,"error subir foto", o.toString(),jsonstring,"id: "+IinfoClient.getInfoClientObject().getPolicies().getId(),"responseCode: "+statusCode);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                String jsonstring = "";
                jsonstring = getStringFromInputStream(in);

                int entero = 0;
                try {
                    //LogHelper.log(this.c,//LogHelper.request,Url, "subir foto auto OK", o.toString(),jsonstring, "","");
                    entero = Integer.parseInt(jsonstring);
                }catch (Exception e){
                    if(Url.contains("AutoRead")){
                        entero=0;
                        try {
                            VehicleOdometer.autoRead = new JSONArray(jsonstring);
                            //LogHelper.log(this.c,//LogHelper.request,Url, "subir foto odometro OK", o.toString(),jsonstring, "","");
                        }catch(Exception ex){
                            //LogHelper.log(this.c,//LogHelper.request,Url, "error subir foto odometro", o.toString(),jsonstring, "",//LogHelper.getException(ex));
                            ex.printStackTrace();
                        }
                    }
                    else {
                        //LogHelper.log(this.c,//LogHelper.request,Url, "error subir foto auto", o.toString(),jsonstring, "",//LogHelper.getException(e));
                        throw new IOException("Error al subir la fotografía. Intente más tarde.");
                    }
                }
                return entero;
                //IinfoClient.InfoClientObject.getPolicies().setRegOdometer(Integer.parseInt(jsonstring));
            }
        }
        catch (Exception e){
            e.printStackTrace();
            //LogHelper.log(this.c,//LogHelper.request,Url, "error subir foto", o.toString(),"", "",//LogHelper.getException(e));
            throw new IOException("Error al subir la fotografía. Intente más tarde.");
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    /*public RequestBody toRequestBody(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }*/

    //Subir fotografia----------------------------------------------------------------
    public Bitmap DownloadPhoto(String Url,Context context,String to) throws IOException
    {
        InputStream in=null;
        try {

            //LogHelper.log(this.c,//LogHelper.request,Url, "inicio bajar foto auto", "","", "tok: "+to, "");
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Authorization",to);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();

            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    //LogHelper.log(this.c,//LogHelper.request,Url, "error bajar foto auto", "","responseCode: "+statusCode, "", "");
                    JSONObject error = new JSONObject(jsonstring);
                    //throw new IOException(error.getString(("Message")));
                    return null;
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            } else {
                try {
                    in = urlConnection.getInputStream();
                    String jsonstring = "";
                    jsonstring = getStringFromInputStream(in);

                    byte[] decodedString = Base64.decode(jsonstring, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    //LogHelper.log(this.c,//LogHelper.request,Url, "fin bajar foto auto ok", "","","tok:"+ to, "");
                    return decodedByte;
                }
                catch(Exception e){
                    //LogHelper.log(this.c,//LogHelper.request,Url, "fin bajar foto auto error", "","", "tok:"+to, //LogHelper.getException(e));
                    throw new IOException("Error al recuperar imagen del servidor.");
                }
                //ahora convetir string to imagen base64
            /*try {
                JSONObject success = new JSONObject(jsonstring);
            } catch (JSONException ex) {
                throw new IOException("Error al Convertir Respuesta de Error");
            }
            */
                //IinfoClient.InfoClientObject.getPolicies().setRegOdometer(Integer.parseInt(jsonstring));
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Subir fotografia----------------------------------------------------------------
    public Bitmap DownloadPhotoMultiaseguradora(String Url,Context context,String to) throws IOException
    {
        InputStream in=null;
        try {
            /*ByteArrayOutputStream ArrayBytes=new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG,50,ArrayBytes);
            byte[] mapByte=ArrayBytes.toByteArray();*/

            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            //urlConnection.setReadTimeout(0);
            //urlConnection.setConnectTimeout(0);
            //urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Authorization",to);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();

            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    JSONObject error = new JSONObject(jsonstring);
                    //throw new IOException(error.getString(("Message")));
                    return null;
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            } else {
                try {
                    in = urlConnection.getInputStream();
                    String jsonstring = "";
                    jsonstring = getStringFromInputStream(in);

                    byte[] decodedString = Base64.decode(jsonstring, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    return decodedByte;
                }
                catch(Exception e){
                    throw new IOException("Error al recuperar imagen del servidor.");
                }
                //ahora convetir string to imagen base64
            /*try {
                JSONObject success = new JSONObject(jsonstring);
            } catch (JSONException ex) {
                throw new IOException("Error al Convertir Respuesta de Error");
            }
            */
                //IinfoClient.InfoClientObject.getPolicies().setRegOdometer(Integer.parseInt(jsonstring));
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Enviamos odometro por pirmera vez-----------------------------------------------------------------
    public Boolean AjusteOdometerCasification(int Type,String Url,Context context,String tt) throws IOException
    {
        InputStream in=null;
        try {
            //LogHelper.log(this.c,//LogHelper.request,Url, "AjusteOdometerCasification", "","", "tt: "+tt, "");
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",tt);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            try {
                SharedPreferences pre = context.getSharedPreferences("miituo", Context.MODE_PRIVATE);
                String idtick = pre.getString("idticket","null");

                sendObject.put("Id", idtick);
                sendObject.put("sTiket",7);
                sendObject.put("GodinName","App");
                sendObject.put("GodynSolution","");
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    //LogHelper.log(this.c,//LogHelper.request,Url, "AjusteOdometerCasification error", sendObject.toString(),jsonstring, "responseCode: "+statusCode+"\ntoken: "+tt, "");
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                Boolean jsonstring;
                jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
                //LogHelper.log(this.c,//LogHelper.request,Url, "AjusteOdometerCasification OK", "",jsonstring.toString(), "responseCode: "+statusCode+"\ntoken: "+tt, "");
                return  jsonstring;
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Enviamos odometro por pirmera vez-----------------------------------------------------------------
    public Boolean AjusteOdometerTicket(int Type,String Url,Context context,String t) throws IOException
    {
        InputStream in=null;
        try {
            //LogHelper.log(this.c,//LogHelper.request,Url, "AjusteOdometerTicket", "","", "t"+t, "");
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",t);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            try {
                SharedPreferences pre = context.getSharedPreferences("miituo", Context.MODE_PRIVATE);
                String idtick = pre.getString("idticket","null");

                sendObject.put("OdomCorrect", 0);
                sendObject.put("OdomMoment",IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
                sendObject.put("idTicket",idtick);
                sendObject.put("idPolicy",IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    //LogHelper.log(this.c,//LogHelper.request,Url, "AjusteOdometerTicket error", sendObject.toString(),jsonstring, "responseCode: "+statusCode+"\ntoken: "+t, "");
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                Boolean jsonstring;
                jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
                //LogHelper.log(this.c,//LogHelper.request,Url, "AjusteOdometerTicket OK", "",jsonstring.toString(), "responseCode: "+statusCode+"\ntoken: "+t, "");
                return  jsonstring;
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Enviamos odometro por pirmera vez-----------------------------------------------------------------
    public Boolean AjusteOdometerLast(int Type,String Url,int id,String t) throws IOException
    {
        InputStream in=null;
        try {
            //LogHelper.log(this.c,//LogHelper.request,Url, "AjusteOdometerLast", "","", "type: "+Type +"id: "+id+"tok: "+t, "");
            URL url = new URL(UrlApi+Url+id+"/"+12);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",t);
            urlConnection.connect();
            /*JSONObject sendObject=new JSONObject();
            try {
                sendObject.put("Id", 0);
                sendObject.put("sTiket",7);
                sendObject.put("GodinName","App");
                sendObject.put("GodynSolution","");
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();*/

            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    //LogHelper.log(this.c,//LogHelper.request,Url, "AjusteOdometerLast error", "",jsonstring, "type: "+Type +"id: "+id+"tok: "+t, "");
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                Boolean jsonstring;
                jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
                //LogHelper.log(this.c,//LogHelper.request,Url, "AjusteOdometerLast error", "",jsonstring.toString(), "type: "+Type +"id: "+id+"tok: "+t, "");
                return  jsonstring;
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Enviamos odometro por pirmera vez-----------------------------------------------------------------
    public Boolean ConfirmOdometer(int Type,String Url,String tik) throws IOException
    {
        InputStream in=null;
        JSONObject sendObject=new JSONObject();
        try {
            //LogHelper.log(this.c,//LogHelper.request,Url, "ConfirmOdometer inicio", "","", "type: "+Type +" tik: "+tik, "");
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",tik);
            try {
                sendObject.put("Type", Type);
                sendObject.put("InsuranceId",String.valueOf(IinfoClient.getInfoClientObject().getPolicies().getInsuranceCarrier().getId()));
                sendObject.put("PolicyId",IinfoClient.getInfoClientObject().getPolicies().getId());
                sendObject.put("PolicyFolio",IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());
                sendObject.put("Odometer",IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            urlConnection.connect();
            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    //LogHelper.log(this.c,//LogHelper.request,Url, "ConfirmOdometer error", sendObject.toString(), jsonstring, "type: "+Type +"tik: "+tik, "");
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                Boolean jsonstring;
                jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
                //LogHelper.log(this.c,//LogHelper.request,Url, "ConfirmOdometer OK", sendObject.toString(),jsonstring.toString(), "type: "+Type +"tik: "+tik, "");
                return  jsonstring;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            //LogHelper.log(this.c,//LogHelper.request,"ConfirmOdometer error","error al subir 1er odometro",sendObject.toString(),"","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),//LogHelper.getException(ex));
            throw ex;
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    public String ConfirmReportLast(String Url,String tok) throws IOException
    {
        InputStream in=null;
        try {
            //LogHelper.log(this.c,//LogHelper.request,Url, "ConfirmReportLast inicio", "","",  "tok: "+tok, "");
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",tok);
            urlConnection.connect();
            JSONObject sendObject=new JSONObject();
            //JSONObject subItem=new JSONObject();
            try {
                //subItem.put("PolicyId",IinfoClient.getInfoClientObject().getPolicies().getId());
                //subItem.put("PolicyFolio",IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());
                //subItem.put("Odometer",IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
                //subItem.put("ClientId",IinfoClient.getInfoClientObject().getClient().getId());
                sendObject.put("Type",1);
                sendObject.put("PolicyId",IinfoClient.getInfoClientObject().getPolicies().getId());
                sendObject.put("PolicyFolio",IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());
                sendObject.put("Odometer",IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
                sendObject.put("ClientId",IinfoClient.getInfoClientObject().getClient().getId());
                sendObject.put("AppOdometer", VehicleOdometer.autoRead);
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            String cad = sendObject.toString();
            writter.write(sendObject.toString());

            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                //in = urlConnection.getInputStream();
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    //LogHelper.log(this.c,//LogHelper.request,Url, "ConfirmReportLast error", sendObject.toString(), jsonstring, "tok: "+tok, "");
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                    //return jsonstring;
                }
                catch (JSONException ex)
                {
                    throw new IOException("Problema al reportar odómetro, contáctenos para más información.");
                    //return jsonstring;
                }
            }
            else {
                in = urlConnection.getInputStream();
                //Boolean jsonstring;
                //jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
                String jsonstring=getStringFromInputStream(in);
                //throw new IOException("Error al Convertir Respuesta de Error");
                //LogHelper.log(this.c,//LogHelper.request,Url, "ConfirmReportLast OK", sendObject.toString(), jsonstring, "tok: "+tok, "");
                return jsonstring;
            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Recuperamos recibo con datos y formularas----------------------------------------------------------------
    public String ConfirmReport(String Url,String tok,boolean isCancelacion, boolean isPagar) throws IOException
    {
        InputStream in=null;
        JSONObject sendObject=new JSONObject();
        try {
            //LogHelper.log(this.c,//LogHelper.request,Url, "ConfirmReport inicio", "","",  "tok: "+tok, "");
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",tok);
            JSONObject subItem=new JSONObject();
            try {
                subItem.put("PolicyId",IinfoClient.getInfoClientObject().getPolicies().getId());
                subItem.put("PolicyFolio",IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());
                subItem.put("ClientId",IinfoClient.getInfoClientObject().getClient().getId());
                if (isCancelacion) {
                    subItem.put("Odometer", 0);
                    subItem.put("CancelacionVoluntaria",isPagar);
                    sendObject.put("Type", 6);   //Tipo 6 = cancelacion voluntaria...
                }
                else {
                    subItem.put("Odometer",IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
                    subItem.put("CancelacionVoluntaria",false);
                    sendObject.put("Type",1);   //Tipo 1 = pago mensual...
                }
                sendObject.put("ImageItem",subItem);
            }
            catch (JSONException ex)
            {
                throw new IOException(ex.getMessage());
            }

            urlConnection.connect();
            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            String cad = sendObject.toString();
            writter.write(sendObject.toString());
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    //LogHelper.log(this.c,//LogHelper.request,Url, "ConfirmReport error", sendObject.toString(), jsonstring, "tok: "+tok+" isCancelacion: "+isCancelacion, "");
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                //Boolean jsonstring;
                //jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
                String jsonstring=getStringFromInputStream(in);
                //LogHelper.log(this.c,//LogHelper.request,Url, "ConfirmReport OK", sendObject.toString(), jsonstring, "tok: "+tok+" isCancelacion: "+isCancelacion, "");
                return jsonstring;
            }
        }
        catch (Exception e){
            //LogHelper.log(this.c,//LogHelper.request,Url,"ConfirmReport Error 2",sendObject.toString(), "","tok: "+tok+" isCancelacion: "+isCancelacion,//LogHelper.getException(e));
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Actualiza token ----------------------------------------------------------------
    public Boolean updateToken(String Url, ClientMovil client, String tok) throws IOException
    {
        InputStream in=null;
        try {
            //LogHelper.log(this.c,//LogHelper.request,Url, "updateToken inicio", "","",  "tok: "+tok, "");
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(0);
            urlConnection.setConnectTimeout(0);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Authorization",tok);
            urlConnection.connect();

            OutputStreamWriter writter=new OutputStreamWriter(urlConnection.getOutputStream());
            Gson parset=new Gson();
            String result=parset.toJson(client);
            writter.write(result);
            writter.flush();
            writter.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    //LogHelper.log(this.c,//LogHelper.request,Url, "updateToken error", result, jsonstring, "tok: "+tok, "");
                    JSONObject error = new JSONObject(jsonstring);
                    throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else {
                in = urlConnection.getInputStream();
                Boolean jsonstring;
                jsonstring = Boolean.parseBoolean(getStringFromInputStream(in));
                //LogHelper.log(this.c,//LogHelper.request,Url, "updateToken OK", result, jsonstring.toString(), "tok: "+tok, "");
                return  jsonstring;

            }
        }
        finally {
            if(in!=null)
            {
                in.close();
            }
        }
    }

    //Metodo para recuperar datos del cliente----------------------------------------------------------------
    public String getToken(String Url)
    {
        final String TAG = "JsonParser.java";
        List<InfoClient> InfoList=null;
        InputStream in=null;
        try {
            //LogHelper.log(this.c,//LogHelper.request,Url, "getToken inicio", "","",  "", "");
            URL url = new URL(UrlApi+Url);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            //urlConnection.setReadTimeout(10000);
            //urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    //LogHelper.log(this.c,//LogHelper.request,Url, "getToken error", "",jsonstring, "", "");
                    JSONObject error = new JSONObject(jsonstring);
                    return "error@"+error.getString(("Message"));
                    //throw new IOException(error.getString(("Message")));
                }
                catch (JSONException ex)
                {
                    return "error@Error al Convertir Respuesta de Error";
                    //throw new IOException("Error al Convertir Respuesta de Error");
                }
            }
            else
                in = urlConnection.getInputStream();

            JSONArray resultJson=null;
            String jsonstring="";
            try {
                jsonstring=getStringFromInputStream(in);
                Log.e("TOKEN",jsonstring);
                //LogHelper.log(this.c,//LogHelper.request,Url, "getToken OK", "", jsonstring, "", "");
                return jsonstring;
            } catch (Exception e) {
                //LogHelper.log(this.c,//LogHelper.request,Url, "getToken error", "", jsonstring, "", //LogHelper.getException(e));
                e.printStackTrace();
                return "error@"+e.getMessage();
            }finally {
                in.close();
            }

            //InputStream in=new BufferedInputStream(urlConnection.getInputStream());
        }catch (Exception e) {
            //LogHelper.log(this.c,//LogHelper.request,Url, "getToken error", "", "", "", //LogHelper.getException(e));
            e.printStackTrace();
            return "error@"+e.getMessage();
        }
        /*finally {
            if(in!=null)
            {
                in.close();
            }
        }*/
    }

    public List<FotosFaltantesModel> getFotosFaltantes(String Url, int idpoliza, String tok_basic) {

        List<FotosFaltantesModel> InfoList=null;
        InputStream in=null;
        try {

            //LogHelper.log(this.c,//LogHelper.request,Url, "getFotosFaltantes inicio", "","","id: "+idpoliza+" tok_basic: "+tok_basic, "");
            URL url = new URL(UrlApi+Url+"/"+idpoliza);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Authorization",tok_basic);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();

            if (statusCode != 200) {
                String jsonstring="";
                //Log.d(TAG, "The response is: " + statusCode);
                in=urlConnection.getErrorStream();
                jsonstring=getStringFromInputStream(in);
                try {
                    //LogHelper.log(this.c,//LogHelper.request,Url, "getFotosFaltantes error", "", jsonstring, "", "");
                    JSONObject error = new JSONObject(jsonstring);
                    //throw new IOException(error.getString(("Message")));
                    return null;
                }
                catch (JSONException ex)
                {
                    throw new IOException("Error al Convertir Respuesta de Error");
                }
            } else {
                in = urlConnection.getInputStream();
                JSONArray resultJson=null;
                String jsonstring="";
                try {
                    jsonstring = getStringFromInputStream(in);
                    //Log.w("Data",jsonstring);
                    //LogHelper.log(this.c,//LogHelper.request,Url, "getFotosFaltantes OK", "",jsonstring, "", "");
                    if (jsonstring != null || jsonstring != "") {
                        Gson parseJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                        InfoList = parseJson.fromJson(jsonstring, new TypeToken<List<FotosFaltantesModel>>() {
                        }.getType());
                    }
                }catch(Exception e){
                    //LogHelper.log(this.c,//LogHelper.request,Url, "getFotosFaltantes error", "",jsonstring, "", //LogHelper.getException(e));
                    e.printStackTrace();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return InfoList;

    }
}

