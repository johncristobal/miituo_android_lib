package com.miituo.miituolibrary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.miituo.miituolibrary.R;
import com.miituo.miituolibrary.activities.api.ApiClient;
import com.miituo.miituolibrary.activities.data.IinfoClient;
import com.miituo.miituolibrary.activities.data.InfoClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.miituo.miituolibrary.activities.PrincipalActivity.result;

public class VehicleOdometer extends AppCompatActivity {

    public static JSONArray autoRead=new JSONArray();
    public String mCurrentPhotoPath,tok;
    final String  UrlApi="ImageSendProcess/AutoRead";
    final String ApiSendReportCancelation ="ReportOdometer/PreviewSaveReport/";
    private ApiClient api;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int PERMISSION_CODE = 1000;
    private Button btn6;
    public boolean flagodo = false;
    Uri image_uri;

    public File photoFile = null;
    final int ODOMETER=5,CANCEL=9;
    public sendOdometro sendodo;

    ImageView Img5;
    SharedPreferences app_preferences;
    public String tipoodometro;
    boolean IsTaken =false;
    Bitmap bmp;
    public static String odo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_odometer);

        try {
            tok = IinfoClient.getInfoClientObject().getClient().getToken();
        }catch (Exception e){
            List<InfoClient> polizas = result;
            if(polizas.size() > 0) {
                tok = polizas.get(0).getClient().getToken();
            }else{
                Toast.makeText(this, "Sin autorización, intente mas tarde.", Toast.LENGTH_SHORT).show();
            }
        }

        TextView btnSinAuto = (TextView)findViewById(R.id.btnSinAuto);
        if(getIntent().getBooleanExtra("isCancelada",false)){
            btnSinAuto.setVisibility(View.VISIBLE);
            btnSinAuto.setEnabled(true);
        }
        else{
            btnSinAuto.setVisibility(View.INVISIBLE);
            btnSinAuto.setEnabled(false);
        }

        Init5();
        api=new ApiClient(VehicleOdometer.this);
    }

    void Init5(){
        //edittext with odometer
        //odometeredit = (EditText)findViewById(R.id.editText);
        //edit2 = (EditText)findViewById(R.id.editText2);

        Img5=(ImageView)findViewById(R.id.img5);
        btn6=(Button)findViewById(R.id.btn6);
        /*btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/

        //lanzar picturoe para capturar foto
        Img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (
                            checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    ) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
                    } else {
                        openCamera();//tomarFotografia();
                    }
                }else{
                    openCamera();//tomarFotografia();
                }
            }
        });
    }

    public void openCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, ODOMETER);
    }

    public void tomarFotografia(){


        if (Build.VERSION.SDK_INT < 23) {
            Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takepic.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File...
                    showAlertaFoto();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    //Uri photoURI = FileProvider.getUriForFile(VehicleOdometer.this, "miituo.com.miituo.provider", photoFile);
                    Uri photoURI = Uri.fromFile(photoFile);
                    takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takepic, ODOMETER);
//                    Intent i= new Intent(this,CamActivity.class);
//                    i.putExtra("img",photoFile);
//                    startActivityForResult(i,ODOMETER);
                }
            }
        }else{
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //PERMISO = FRONT_VEHICLE;
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }else{
                Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takepic.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File...
                        showAlertaFoto();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(VehicleOdometer.this, "com.miituo.miituolibrary.provider", photoFile);
                        //Uri photoURI = Uri.fromFile(photoFile);
                        takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takepic, ODOMETER);
//                        Intent i= new Intent(this,CamActivity.class);
//                        i.putExtra("img",photoFile);
//                        startActivityForResult(i,ODOMETER);
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();//tomarFotografia();
            } else {
                Toast.makeText(this, "No se pueden tomar fotos. Acceso denegado.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void subirFoto(View v){
        if (btn6.getText().toString().contains("omar")){
            openCamera();//tomarFotografia();
        }else {
            if (flagodo) {
                sendodo = new sendOdometro();
                sendodo.execute("OK");
            } else {
                Toast msg = Toast.makeText(getApplicationContext(), "Tome la foto del odómetro para finalizar", Toast.LENGTH_LONG);
                msg.show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode== Activity.RESULT_OK)
            {
                //String filePath = mCurrentPhotoPath;
                flagodo = true;
                //Img5.setImageBitmap(bmp);
                Glide.with(VehicleOdometer.this)
                        .load(image_uri)
                        .apply(new RequestOptions().override(150, 200).centerCrop())
                        .into(Img5);
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                Glide.with(VehicleOdometer.this)
//                        .load(imageBitmap)
//                        .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
//                        //.override(150,200)
//                        //.centerCrop()
//                        .into(Img1);
                //Img1.setImageBitmap(imageBitmap);
//                String user = "";
//                String picName="";
//                switch (requestCode){
//                    case 1:
//                        user = "frontal";
//                        picName="Frontal";
//                        break;
//                    case 2:
//                        user = "derecho";
//                        picName="Lateral Derecho";
//                        break;
//                    case 3:
//                        user = "back";
//                        picName="Trasera";
//                        break;
//                    case 4:
//                        user = "izquierdo";
//                        picName="Lateral Izquierdo";
//                        break;
//                    default:
//                        break;
//                }
//
//                String filePath = currentPhotoPath;
//
//                if (requestCode == 1) {
//                    //flag1 = true;
//                    //comprimer imagen antes de lanzarla al imageview
//                    //bmp.compress(Bitmap.CompressFormat.JPEG,15);
//
//                    //Img1.setImageBitmap(resizedtoshow);
//                    Glide.with(VehiclePictures.this)
//                            .load(filePath)
//                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
//                            //.override(150,200)
//                            //.centerCrop()
//                            .into(Img1);
//                    //Img1.setImageBitmap(bmp);
//                }
//                if (requestCode == 2) {
//                    flag2 = true;
//                    //Bundle ext=data.getExtras();
//                    //bmp=(Bitmap)ext.get("data");
//                    //Img2.setImageBitmap(resizedtoshow);
//                    //Img2.setImageBitmap(bmp);
//                    Glide.with(VehiclePictures.this)
//                            .load(filePath)
//                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
//                            //.override(150,200)
//                            //.centerCrop()
//                            .into(Img2);
//                }
//                if (requestCode == 3) {
//                    flag3 = true;
//                    //Bundle ext=data.getExtras();
//                    //bmp=(Bitmap)ext.get("data");
//                    //Img3.setImageBitmap(resizedtoshow);
//                    //Img3.setImageBitmap(bmp);
//                    Glide.with(VehiclePictures.this)
//                            .load(filePath)
//                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
//                            //.override(150,200)
//                            //.centerCrop()
//                            .into(Img3);
//                }
//                if (requestCode == 4) {
//                    flag4 = true;
//                    //Bundle ext=data.getExtras();
//                    //bmp=(Bitmap)ext.get("data");
//                    //Img4.setImageBitmap(resizedtoshow);
//                    //Img4.setImageBitmap(bmp);
//                    Glide.with(VehiclePictures.this)
//                            .load(filePath)
//                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
//                            // .override(150,200)
//                            //.centerCrop()
//                            .into(Img4);
//                }
//                if (requestCode == 5) {
//                    //Bundle ext=data.getExtras();
//                    //bmp=(Bitmap)ext.get("data");
//                    //Img5.setImageBitmap(resizedtoshow);
//                    //Img5.setImageBitmap(bmp);
//                    Glide.with(VehiclePictures.this)
//                            .load(filePath)
//                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
//                            //.override(150,200)
//                            //.centerCrop()
//                            .into(Img5);
//                }
//                IsTaken = true;
                IsTaken = true;
                btn6.setText("Continuar");

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void showAlertaFoto(){
        AlertDialog.Builder builder = new AlertDialog.Builder(VehicleOdometer.this);
        builder.setTitle("Atención");
        builder.setMessage("No podemos abrir tu cámara. Revisa el dispositivo.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i=new Intent(VehicleOdometer.this,PrincipalActivity.class);
                startActivity(i);
            }
        });

        AlertDialog alerta = builder.create();
        alerta.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "PNG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            //File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            File image = File.createTempFile(
                    imageFileName,  // prefix
                    ".jpeg",         // suffix
                    storageDir      // directory
            );

            //File image = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+"odometro_"+polizaFolio+".png");

            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = image.getAbsolutePath();

//            SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("nombrefotoodometro", mCurrentPhotoPath);
//            editor.apply();
            return image;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void sinAuto(View v){
        new getCobroTask().execute(false);
    }
//*******************************************not attribute =! class*********************************
    public class sendOdometro extends AsyncTask<String, Void, Void> {
        ProgressDialog progress = new ProgressDialog(VehicleOdometer.this);
        String ErrorCode = "";
        String isFoto = "OK";

        public void showAlerta() {
            AlertDialog.Builder builder = new AlertDialog.Builder(VehicleOdometer.this);
            builder.setTitle("Atención");
            builder.setMessage("Hubo un problema. Lo intentaremos más tarde.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(VehicleOdometer.this, ConfirmActivity.class);
                    startActivity(i);
                }
            });

            AlertDialog alerta = builder.create();
            alerta.show();
        }

        @Override
        protected void onPreExecute() {
            try {
                progress.setTitle("Registro de odómetro");
                progress.setMessage("Subiendo imagen...");
                //progress.setIcon(R.drawable.miituo);
                progress.setCancelable(false);
                progress.setIndeterminate(true);
                progress.show();
            } catch (Exception e) {
                showAlerta();
            }
        }

        private String getFilePath(Uri uri) {
            String[] projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String picturePath = cursor.getString(columnIndex); // returns null
                cursor.close();
                return picturePath;
            }
            return null;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                app_preferences = getSharedPreferences(getString(R.string.shared_name_prefs), Context.MODE_PRIVATE);
                tipoodometro = app_preferences.getString("odometro", "null");
                isFoto = params[0];
                if (isFoto.equals("NO")) {
                    bmp = BitmapFactory.decodeResource(VehicleOdometer.this.getResources(), R.drawable.ayuda);
                    //api.UploadPhoto(6, ImageList.get(0).getImage(), UrlApi);
                    api.UploadPhoto(6, bmp, UrlApi, tok, "", "0", "0", "");
                } else {
                    //Subimos foto de odometro.....
                    //mCurrentPhotoPath = app_preferences.getString("nombrefotoodometro", "null");
                    InputStream input = getContentResolver().openInputStream(image_uri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    bmp = BitmapFactory.decodeStream(input,null, options);
                    input.close();
                    //bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri);
                    File fdelete = new File(getFilePath(image_uri));

                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                            System.out.println("file Deleted :" );
                        } else {
                            System.out.println("file not Deleted :");
                        }
                    }

                    String lat = null, lon = null, cp = null;
                    lat="0";
                    lon="0";
                    cp="";
                    api.UploadPhoto((tipoodometro.equals("cancela") ? 6 : 5), bmp, UrlApi, tok, odo, lat, lon, cp);
                }
                IinfoClient.InfoClientObject.getPolicies().setRegOdometer(Integer.parseInt("1000"));
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            try {
                progress.dismiss();
                if(isFoto.equals("NO")){
                    new getCobroTask().execute(true);
                }
                else {
                    Intent i = new Intent(VehicleOdometer.this, ConfirmActivity.class);
                    startActivity(i);
                }
            }
            catch (Exception e){
                showAlerta();
            }
        }

        @Override
        protected void onCancelled() {
            try {
                progress.dismiss();
                if (ErrorCode.equals("1000")) {
                    Intent i = new Intent(VehicleOdometer.this, ConfirmActivity.class);
                    startActivity(i);
                } else {
                    Toast msg = Toast.makeText(getApplicationContext(), "Tuvimos un problema:" + ErrorCode, Toast.LENGTH_LONG);
                    msg.show();
                }
            }catch (Exception e){
                showAlerta();
            }
        }
    }
//*******************************************not attribute =! class*********************************
    public class getCobroTask extends AsyncTask<Boolean, Void, Void> {
        ProgressDialog progress = new ProgressDialog(VehicleOdometer.this);
        String respuesta="",ErrorCode = "";
        AlertDialog alert;
        boolean isPagar;

    @Override
        protected void onPreExecute() {
            progress.setTitle("Odómetro");
            progress.setMessage("Enviando información...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            //progress.setIcon(R.drawable.miituo);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            progress.dismiss();
            Toast msg = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
            msg.show();

            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Boolean... params) {
            try {
                isPagar = params[0];
                respuesta = api.ConfirmReport(ApiSendReportCancelation,tok,true, params[0]);
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            try {
                progress.dismiss();
                if(!isPagar) {
    //                                Intent i = new Intent(VehicleOdometer.this, NoReporteOdometroActivity.class);
    //                                i.putExtra("json", respuesta);
    //                                startActivityForResult(i, CANCEL);
                    showPago(respuesta);
                }
                else{
                    JSONObject json=new JSONObject(respuesta);
                    JSONObject datos=json.getJSONArray("ResulList").getJSONObject(0);
                    boolean isOK=datos.isNull("ConfirmReport")?false:datos.getBoolean("ConfirmReport");

                    if (isOK) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(VehicleOdometer.this);
                        builder.setTitle("Error");
                        builder.setMessage("Error al subir información. Intente más tarde");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(VehicleOdometer.this, PrincipalActivity.class);
                                finish();
                                startActivity(i);
                            }
                        });
                        AlertDialog alerta = builder.create();
                        alerta.show();
                    } else {
                        new android.app.AlertDialog.Builder(VehicleOdometer.this)
                            .setTitle("Gracias.")
                            .setMessage("Tu información ha sido actualizada.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    IinfoClient.getInfoClientObject().getPolicies().setLastOdometer(IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
                                    IinfoClient.getInfoClientObject().getPolicies().setReportState(12);

                                    //String rs = UpdateDataBase(modelBase.FeedEntryPoliza.TABLE_NAME,IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());

                                    Intent i = new Intent(VehicleOdometer.this, PrincipalActivity.class);
                                    i.putExtra("actualizar","1");
                                    finish();
                                    startActivity(i);
                                }
                            })
                            .show();
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        public void showPago(String resp){
            String noDias="";
            String saldo="";
            try{
                JSONObject json = new JSONObject(resp);
                JSONObject datos = json.getJSONArray("ResulList").getJSONObject(0);
                noDias=datos.getString("Dias_sin_reportar");
                saldo=datos.getString("amount");
            }
            catch (Exception e){
                e.printStackTrace();
            }
            final AlertDialog.Builder sort = new AlertDialog.Builder(VehicleOdometer.this);
            // Get the layout inflater
            LayoutInflater inflater = getLayoutInflater();
            View sortView = inflater.inflate(R.layout.activity_no_reporte_odometro, null);
            sort.setView(sortView);
            sort.setCancelable(false);
            TextView back=(TextView) sortView.findViewById(R.id.btnRegresar);
            TextView pagar=(TextView) sortView.findViewById(R.id.btnPagar);
            TextView dias=(TextView) sortView.findViewById(R.id.lbGral);
            dias.setText(Html.fromHtml("<p>Han transcurrido <b>"+noDias+" días" +
                    "</b> desde tu<br> último reporte de odómetro,<br>tu <b>cargo es de $"+saldo+"</b></p>"));
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
            pagar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendodo = new sendOdometro();
                    sendodo.execute("NO");
                }
            });
            alert=sort.create();
            alert.show();
        }
    }
}