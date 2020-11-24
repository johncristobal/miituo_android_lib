package com.miituo.miituolibrary.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miituo.miituolibrary.R;
import com.miituo.miituolibrary.activities.data.IinfoClient;

public class ConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        EditText editlast = (EditText)findViewById(R.id.editTextLast);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_name_prefs), Context.MODE_PRIVATE);
        String mCurrentPhotoPath = preferences.getString("nombrefotoodometro", "null");
        String filePath = mCurrentPhotoPath;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bmp = BitmapFactory.decodeFile(filePath,options);

        ImageView vistaodo = findViewById(R.id.imageView17);
        vistaodo.setImageBitmap(bmp);

        try {
            int a = IinfoClient.InfoClientObject.getPolicies().getLastOdometer();

            if (a == 0) {
                RelativeLayout ultimo = (RelativeLayout) findViewById(R.id.relativeLayout5);
                ultimo.setVisibility(View.GONE);

                //RelativeLayout ahora = (RelativeLayout)findViewById(R.id.relativeLayout4);
            } else {
                editlast.setText(a + "");
                editlast.setEnabled(false);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void validar(View v){
        //get odometer value
        //validar no sea ""
        //lanzar nuevo actiivty con valor para confimar del otro lado...

        EditText odometro = (EditText)findViewById(R.id.editTextConfirma);
        String odo = odometro.getText().toString();

        if(odo.equals("")){
            Toast.makeText(this,"Es necesario capturar los kms. que marca el odómetro.",Toast.LENGTH_SHORT).show();
        }else{
            //launch activity and save odometro
            Intent i = new Intent(this,LastOdometerActivity.class);
            i.putExtra("valor",odo);
            startActivity(i);
        }
    }

}