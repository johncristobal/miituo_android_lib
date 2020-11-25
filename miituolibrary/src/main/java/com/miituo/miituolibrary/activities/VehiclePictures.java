package com.miituo.miituolibrary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.miituo.miituolibrary.R;
import com.miituo.miituolibrary.activities.api.ApiClient;
import com.miituo.miituolibrary.activities.data.IinfoClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VehiclePictures extends AppCompatActivity {

    private ImageView Img1,Img2,Img3,Img4,Img5;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int PERMISSION_CODE = 1000;
    public int PERMISO;
    public String NOMBRETEMP;
    final int FRONT_VEHICLE=1;
    final String UrlApi="ImageSendProcess/Array/";
    final String UrlAjustelast="Policy/UpdatePolicyStatusReport/";
    final int SIDE_RIGHT_VEHICLE=2;
    final int REAR_VEHICLE=3;
    final int SIDE_LEFT_VEHICLE=4;
    public File photoFile = null;
    String currentPhotoPath;
    boolean IsTaken =false;
    public boolean flag1,flag2,flag3,flag4;
    public boolean flag1valida,flag2valida,flag3valida,flag4valida;
    Uri image_uri,image_uri1,image_uri2,image_uri3,image_uri4;
    private ApiClient api;

    public sendVehiclePicture hilofotos;
    public boolean breakThread;
    public String polizaFolio,tok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_pictures);

        String data = getIntent().getStringExtra("poliza");
        if(data != null)
            Log.w("data", data);

        try {
            polizaFolio = IinfoClient.getInfoClientObject().getPolicies().getNoPolicy();
            tok = IinfoClient.getInfoClientObject().getClient().getToken();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Atencion. Contacte al equipo de desarrollo para posible error.", Toast.LENGTH_SHORT).show();
            finish();
        }

        flag1 = false;
        flag2 = false;
        flag3 = false;
        flag4 = false;

        flag1valida = false;
        flag2valida = false;
        flag3valida = false;
        flag4valida = false;

        breakThread = false;
        hilofotos = new sendVehiclePicture();
        api=new ApiClient(VehiclePictures.this);

        Img1 = findViewById(R.id.Img1);
        Img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NOMBRETEMP = "frontal";
                PERMISO = FRONT_VEHICLE;
                checkpermission(FRONT_VEHICLE);
            }
        });

        Img2 = findViewById(R.id.Img2);
        Img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NOMBRETEMP = "derecho";
                PERMISO = SIDE_RIGHT_VEHICLE;
                checkpermission(SIDE_RIGHT_VEHICLE);
            }
        });

        Img3 = findViewById(R.id.Img3);
        Img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NOMBRETEMP = "back";
                PERMISO = REAR_VEHICLE;
                checkpermission(REAR_VEHICLE);
            }
        });

        Img4 = findViewById(R.id.Img4);
        Img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NOMBRETEMP = "izquierdo";
                PERMISO = SIDE_LEFT_VEHICLE;
                checkpermission(SIDE_LEFT_VEHICLE);
            }
        });

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.mensaje_fotos);

        TextView okbutton = (TextView)dialog.findViewById(R.id.textView70);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void checkpermission(int type){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (
                    checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
            } else {
                openCamera(type);//tomarFotografia();
            }
        }else{
            openCamera(type);//tomarFotografia();
        }
    }

    public void openCamera(int type){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, type);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                openCamera(PERMISO);
            } else {
                Toast.makeText(this, "No se pueden tomar fotos. Acceso denegado.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showAlertaFoto(){
        AlertDialog.Builder builder = new AlertDialog.Builder(VehiclePictures.this);
        builder.setTitle("Atención");
        builder.setMessage("No podemos abrir tu cámara. Revisa el dispositivo.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i=new Intent(VehiclePictures.this,PrincipalActivity.class);
                startActivity(i);
            }
        });

        AlertDialog alerta = builder.create();
        alerta.show();
    }

    public void tomarfoto(int p,String name, boolean isupper23){
        if (Build.VERSION.SDK_INT < 23) {
            Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takepic.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile(name,p);
                } catch (IOException ex) {
                    // Error occurred while creating the File...
                    showAlertaFoto();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    //Uri photoURI = FileProvider.getUriForFile(VehicleOdometer.this, "miituo.com.miituo.provider", photoFile);
                    Uri photoURI = Uri.fromFile(photoFile);
                    takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takepic, p);
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
                        photoFile = createImageFile(name,p);
                    } catch (IOException ex) {
                        // Error occurred while creating the File...
                        showAlertaFoto();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(VehiclePictures.this, "com.miituo.miituolibrary.provider", photoFile);
                        //Uri photoURI = Uri.fromFile(photoFile);
                        takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takepic, p);
//                        Intent i= new Intent(this,CamActivity.class);
//                        i.putExtra("img",photoFile);
//                        startActivityForResult(i,ODOMETER);
                    }
                }
            }
        }
//        Intent takepic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////        try {
////            startActivityForResult(takepic, MY_CAMERA_REQUEST_CODE);
////        }catch (Exception ex) {
////            ex.printStackTrace();
////            // Error occurred while creating the File...
////        }
////        startActivityForResult(i, FRONT_VEHICLE);
//        if (takepic.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            try {
//                photoFile = createImageFile(name,p);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//                // Error occurred while creating the File...
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI;
//                photoURI = FileProvider.getUriForFile(VehiclePictures.this, "com.miituo.miituolibrary.provider", photoFile);
////                if(isupper23) {
////                    photoURI = FileProvider.getUriForFile(VehiclePictures.this, "com.miituo.miituolibrary.provider", photoFile);
////                }
////                else{
////                    photoURI = Uri.fromFile(photoFile);
////                }
//                takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takepic, p);
//            }else{
//                Toast.makeText(this,"Tuvimos un problema al tomar la imagen. Intente mas tarde.",Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    private File createImageFile(String username, int tag) throws IOException {
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
            currentPhotoPath = image.getAbsolutePath();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode== Activity.RESULT_OK)
            {
                String user = "";
                String picName="";
                switch (requestCode){
                    case 1:
                        user = "frontal";
                        picName="Frontal";
                        break;
                    case 2:
                        user = "derecho";
                        picName="Lateral Derecho";
                        break;
                    case 3:
                        user = "back";
                        picName="Trasera";
                        break;
                    case 4:
                        user = "izquierdo";
                        picName="Lateral Izquierdo";
                        break;
                    default:
                        break;
                }

                if (requestCode == 1) {
                    flag1 = true;
                    //comprimer imagen antes de lanzarla al imageview
                    //bmp.compress(Bitmap.CompressFormat.JPEG,15);
                    //Img5.setImageBitmap(bmp);
                    //Img1.setImageBitmap(resizedtoshow);
                    image_uri1 = image_uri;
                    Glide.with(VehiclePictures.this)
                            .load(image_uri)
                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
                            //.override(150,200)
                            //.centerCrop()
                            .into(Img1);
                    //Img1.setImageBitmap(bmp);
                }
                if (requestCode == 2) {
                    flag2 = true;
                    //Bundle ext=data.getExtras();
                    //bmp=(Bitmap)ext.get("data");
                    //Img2.setImageBitmap(resizedtoshow);
                    //Img2.setImageBitmap(bmp);
                    image_uri2 = image_uri;
                    Glide.with(VehiclePictures.this)
                            .load(image_uri)
                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
                            //.override(150,200)
                            //.centerCrop()
                            .into(Img2);
                }
                if (requestCode == 3) {
                    flag3 = true;
                    //Bundle ext=data.getExtras();
                    //bmp=(Bitmap)ext.get("data");
                    //Img3.setImageBitmap(resizedtoshow);
                    //Img3.setImageBitmap(bmp);
                    image_uri3 = image_uri;
                    Glide.with(VehiclePictures.this)
                            .load(image_uri)
                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
                            //.override(150,200)
                            //.centerCrop()
                            .into(Img3);
                }
                if (requestCode == 4) {
                    flag4 = true;
                    //Bundle ext=data.getExtras();
                    //bmp=(Bitmap)ext.get("data");
                    //Img4.setImageBitmap(resizedtoshow);
                    //Img4.setImageBitmap(bmp);
                    image_uri4 = image_uri;
                    Glide.with(VehiclePictures.this)
                            .load(image_uri)
                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
                            // .override(150,200)
                            //.centerCrop()
                            .into(Img4);
                }

                IsTaken = true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void subirFotos(View v){
        //valida si ya subio todas las fotosv
        //if (ImageList.size() > 3 && !IinfoClient.InfoClientObject.getPolicies().isHasVehiclePictures()) {
        if (flag1 && flag2 && flag3 && flag4) {

            try {
                hilofotos.execute();

                //sendVehiclePicture.execute();//.get(10000, TimeUnit.MILLISECONDS);
            }
            catch(Exception e){
                //so much time to upload photos...so break thread and continue...
                Log.e("Error","Se acabo el tiempo...");
                hilofotos.cancel(true);
            }
        } else {
            //si no ha subido las fotos...err
            Toast msg = Toast.makeText(getApplicationContext(), "Falta tomar las fotos obligatorias.", Toast.LENGTH_LONG);
            msg.show();
        }
    }

//*******************************************not attribute =! class*********************************
    public class sendVehiclePicture extends AsyncTask<Void, Void, Void> {
        public ProgressDialog progress=new ProgressDialog(VehiclePictures.this);
        String ErrorCode="";
        public boolean flag = true;
        public int cont = 0;

        public void showAlerta(){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VehiclePictures.this);
            builder.setTitle("Atención.");
            builder.setMessage("Hubo un problema al subir las fotos. Lo intentaremos más tarde.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //change this...now launch odometer if error...
                    IinfoClient.InfoClientObject.getPolicies().setHasVehiclePictures(false);

                    Intent i=new Intent(VehiclePictures.this,PrincipalActivity.class);
                    startActivity(i);
                }
            });

            android.app.AlertDialog alerta = builder.create();
            alerta.show();
        }

        @Override
        protected void onCancelled() {
            try {
                progress.dismiss();
                showAlerta();
            }catch(Exception e){
                showAlerta();
            }
        }

        @Override
        protected void onPreExecute() {
            try {
                progress.setTitle("Subiendo Fotos del Vehículo...");
                progress.setMessage("Espere un momento en lo que subimos las imágenes de su vehiculo...");
                //progress.setIcon(R.drawable.miituo);
                progress.setCancelable(false);
                progress.setIndeterminate(false);
                progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progress.setMax(4);
                progress.setProgress(0);
                progress.show();
            }catch(Exception e){
                showAlerta();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
//                String sf=app_preferences.getString("solofotos","null");
//                if(sf.equals("1")) {
//
//                    String username = "";
//                    if(flag1valida){
//                        username = "frontal";
//                        launchImagen(username,1);
//                    }
//                    if(flag2valida){
//                        username = "derecho";
//                        launchImagen(username,2);
//                    }
//                    if(flag3valida){
//                        username = "back";
//                        launchImagen(username,3);
//                    }
//                    if(flag4valida){
//                        username = "izquierdo";
//                        launchImagen(username,4);
//                    }
//                    //lo mismo con los demas
//
//                }else{
                    //while(flag) {
                    //for(int i=0;i<ImageList.size();i++) {
                    for (int i = 1; i <= 4; i++) {
                        Uri username = null;
                        if (i == 1)
                            username = image_uri1;
                        if (i == 2)
                            username = image_uri2;
                        if (i == 3)
                            username = image_uri3;
                        if (i == 4)
                            username = image_uri4;

                        //File image = new File();

                        //BitmapFactory.Options options = new BitmapFactory.Options();
                        //options.inSampleSize = 4;
                        //Bitmap bit = BitmapFactory.decodeFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + username + startTime + polizaFolio + ".png", options);
                        InputStream input = getContentResolver().openInputStream(username);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;
                        Bitmap bit = BitmapFactory.decodeStream(input,null, options);
                        input.close();
                        //bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri);
                        File fdelete = new File(getFilePath(username));

                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                System.out.println("file Deleted :" );
                            } else {
                                System.out.println("file not Deleted :");
                            }
                        }

                        int a = api.UploadPhoto(i, bit, UrlApi, tok,"","0","0","");
                        if(a <= 4 && a != 0){
                            //subio correcto
                            progress.incrementProgressBy(1);
                            cont++;
                        }
                    }
                    //}

                    if(!flag){
                        this.cancel(true);
                    }
                //}
            }
            catch (Exception ex)
            {
                ErrorCode=ex.getMessage();
                this.cancel(true);
            }
            return null;
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
//        private void launchImagen(String username,int tipo) {
//
//            try {
//                SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
//                mCurrentPhotoPath = preferences.getString("nombrefoto" + username + polizaFolio, "null");
//
//                String filePath = mCurrentPhotoPath;
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 4;
//                Bitmap bit = BitmapFactory.decodeFile(filePath, options);
//
//                int a = api.UploadPhoto(tipo, bit, UrlApi, tok,"","0","0","");
//                if (a <= 4 && a != 0) {
//                    progress.incrementProgressBy(1);
//                    cont++;
//                }
//            }catch(Exception e){
//                ErrorCode = e.toString();
//                LogHelper.log(VehiclePictures.this,LogHelper.backTask,"VehiclePictures.sendVehiclePictures.launchImage","",
//                        "", "","",LogHelper.getException(e));
//                this.cancel(true);
//            }
//        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                new android.app.AlertDialog.Builder(VehiclePictures.this)
                        .setTitle("Fotos de Vehículo")
                        .setMessage("Las fotos se han subido correctamente. !Gracias¡")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progress.dismiss();
                                IinfoClient.InfoClientObject.getPolicies().setHasVehiclePictures(true);

                                //valido foto de odometro tmb antes de lanzar a sendodometer***
                                Intent odo = new Intent(VehiclePictures.this, VehicleOdometer.class);
                                startActivity(odo);

                                //app_preferences= getSharedPreferences("miituo", Context.MODE_PRIVATE);
                                //app_preferences.edit().putLong("time",startTime).apply();
                                //String fotos = app_preferences.getString("solofotos","null");
//                                if(fotos.equals("1")){
//                                    //aqu ivalido siguiente foto odometro
//                                    //entonces hay mas de una foto que solicitaron
//                                    //busco si hay de odometro, si no, entonces updateStauts
//                                    boolean miniodo = false;
//                                    for(int k=0;k<fotosfaltantesList.size();k++){
//                                        if(fotosfaltantesList.get(k).getId() == 5){
//                                            miniodo = true;
//                                            break;
//                                        }
//                                    }
//                                    if(miniodo){
//                                        Intent i = new Intent(VehiclePictures.this,VehicleOdometer.class);
//                                        startActivity(i);
//
//                                    }else{
//                                        app_preferences.edit().putString("solofotos","0").apply();
//                                        updateStatus odometro = new updateStatus();
//                                        odometro.execute();
//
//                                    }
//                                }else{
//                                    Intent odo = new Intent(VehiclePictures.this, VehicleOdometer.class);
//                                    startActivity(odo);
//                                }
                            }
                        })
                        .show();
            }catch(Exception e){
                showAlerta();
            }
        }
    }
}