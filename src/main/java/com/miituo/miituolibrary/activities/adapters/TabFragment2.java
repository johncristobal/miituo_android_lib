package com.miituo.miituolibrary.activities.adapters;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.miituo.miituolibrary.R;
import com.miituo.miituolibrary.activities.data.IinfoClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.miituo.miituolibrary.activities.adapters.PagerAdapter.pager;

public class TabFragment2 extends Fragment {

    Typeface tipo;

    /*public TabFragment2(Typeface t){
        tipo = t;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab2, container, false);

        TextView tex = (TextView)v.findViewById(R.id.textView);
        tex = (TextView)v.findViewById(R.id.textView78);

        TextView fecha = (TextView)v.findViewById(R.id.texdate);
        TextView fechaabajo = (TextView)v.findViewById(R.id.textView80);

        Button b1 = v.findViewById(R.id.button13);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(0);
            }
        });
        Calendar c = Calendar.getInstance();

        //fecha limite
        Date fechacadena = IinfoClient.InfoClientObject.getPolicies().getLimitReportDate();
        c.setTime(fechacadena);
        c.add(Calendar.DATE, -1);
        fechacadena = c.getTime();

        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(c.getTime());
        String real = getName(month_name);
        SimpleDateFormat year_data = new SimpleDateFormat("yyyy");
        String year = year_data.format(c.getTime());

        //fecha menos 5 dias
        Date fechalimite = IinfoClient.InfoClientObject.getPolicies().getLimitReportDate();
        c.setTime(fechalimite);
        SimpleDateFormat month_date_before = new SimpleDateFormat("MMM");
        c.add(Calendar.DATE, -4);
        fechalimite = c.getTime();
        String month_name_before = month_date_before.format(c.getTime());
        String real_before = getName(month_name_before);
        SimpleDateFormat year_data_bef = new SimpleDateFormat("yyyy");
        String year_bef = year_data_bef.format(c.getTime());

        fecha.setText("del "+new SimpleDateFormat("dd").format(fechalimite)+" de "+real_before);
        fechaabajo.setText("al "+new SimpleDateFormat("dd").format(fechacadena)+" de "+real);

        return v;
    }

    public String getName(String nombre){

        if(nombre.equals("ene") || nombre.equals("Jan") || nombre.equals("jan") || nombre.equals("Ene")){
            return "enero";
        }else if(nombre.equals("feb") || nombre.equals("Feb")){
            return "febrero";
        }else if (nombre.equals("mar") || nombre.equals("Mar")){
            return "marzo";
        }else if (nombre.equals("abr") || nombre.equals("apr") || nombre.equals("Apr")){
            return "abril";
        }else if (nombre.equals("may") || nombre.equals("May")){
            return "mayo";
        }else if (nombre.equals("jun") || nombre.equals("Jun")){
            return "junio";
        }else if (nombre.equals("jul") || nombre.equals("Jul")){
            return "julio";
        }else if (nombre.equals("ago") || nombre.equals("aug") || nombre.equals("Aug") || nombre.equals("Ago")){
            return "agosto";
        }else if (nombre.equals("sep") || nombre.equals("Sep")){
            return "septiembre";
        }else if (nombre.equals("oct") || nombre.equals("Oct")){
            return "octubre";
        }else if (nombre.equals("nov") || nombre.equals("Nov")){
            return "noviembre";
        }else if (nombre.equals("dic") || nombre.equals("dec") || nombre.equals("Dic") || nombre.equals("Dec")){
            return "diciembre";
        }else{
            return nombre;
        }
    }
}
