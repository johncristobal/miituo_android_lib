package com.miituo.miituolibrary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VehiclePictures extends AppCompatActivity {

    private ImageView Img1,Img2,Img3,Img4,Img5;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public int PERMISO;
    public String NOMBRETEMP;
    final int FRONT_VEHICLE=1;
    final int SIDE_RIGHT_VEHICLE=2;
    final int REAR_VEHICLE=3;
    final int SIDE_LEFT_VEHICLE=4;
    public File photoFile = null;
    String currentPhotoPath;
    boolean IsTaken =false;
    public boolean flag1,flag2,flag3,flag4;
    public boolean flag1valida,flag2valida,flag3valida,flag4valida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_pictures);

        String data = getIntent().getStringExtra("poliza");
        if(data != null)
            Log.w("data", data);

        Img1 = findViewById(R.id.Img1);
        Img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NOMBRETEMP = "frontal";
                PERMISO = FRONT_VEHICLE;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    } else {
                        tomarfoto(FRONT_VEHICLE, NOMBRETEMP,true);
                    }
                }else{
                    tomarfoto(FRONT_VEHICLE, NOMBRETEMP,true);
                }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                tomarfoto(FRONT_VEHICLE, NOMBRETEMP,true);
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

//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                Glide.with(VehiclePictures.this)
//                            .load(imageBitmap)
//                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
//                            //.override(150,200)
//                            //.centerCrop()
//                            .into(Img1);
                //Img1.setImageBitmap(imageBitmap);
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

                String filePath = currentPhotoPath;

                if (requestCode == 1) {
                    //flag1 = true;
                    //comprimer imagen antes de lanzarla al imageview
                    //bmp.compress(Bitmap.CompressFormat.JPEG,15);
                    //Img5.setImageBitmap(bmp);
                    //Img1.setImageBitmap(resizedtoshow);
                    Glide.with(VehiclePictures.this)
                            .load(filePath)
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
                    Glide.with(VehiclePictures.this)
                            .load(filePath)
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
                    Glide.with(VehiclePictures.this)
                            .load(filePath)
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
                    Glide.with(VehiclePictures.this)
                            .load(filePath)
                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
                            // .override(150,200)
                            //.centerCrop()
                            .into(Img4);
                }
                if (requestCode == 5) {
                    //Bundle ext=data.getExtras();
                    //bmp=(Bitmap)ext.get("data");
                    //Img5.setImageBitmap(resizedtoshow);
                    //Img5.setImageBitmap(bmp);
                    Glide.with(VehiclePictures.this)
                            .load(filePath)
                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
                            //.override(150,200)
                            //.centerCrop()
                            .into(Img5);
                }
                IsTaken = true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void subirFotos(View v){
        Toast.makeText(this,"Subiendo fotos...",Toast.LENGTH_SHORT).show();
    }
}