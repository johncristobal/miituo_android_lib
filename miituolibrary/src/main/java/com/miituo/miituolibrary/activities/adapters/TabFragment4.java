package com.miituo.miituolibrary.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miituo.miituolibrary.R;
import com.miituo.miituolibrary.activities.data.IinfoClient;

public class TabFragment4 extends Fragment {

    Context context;
    Typeface tipo;

    /*public TabFragment4(Context c,Typeface t){
        context = c;
        tipo = t;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab4, container, false);

        Button call = (Button)v.findViewById(R.id.button6);
        TextView res = (TextView)v.findViewById(R.id.textView56);
        TextView fon = (TextView)v.findViewById(R.id.textView);

        ImageView iconAseguradora=(ImageView)v.findViewById(R.id.imageView8);

        final String noAseguradora;  // atlas
        iconAseguradora.setImageResource(R.drawable.segurosatlas);
        res.setText(getString(R.string.tel_atlas));
        noAseguradora=getString(R.string.tel_atlas);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Intent intent = new Intent(Intent.ACTION_CALL);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", noAseguradora, null));
                    //intent.setData(Uri.parse("11025280"));
                    getActivity().startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

}
