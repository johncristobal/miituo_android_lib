package com.miituo.miituolibrary.activities.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import com.miituo.miituolibrary.R;

import static android.content.Context.CLIPBOARD_SERVICE;

public class PromosAdapter extends PagerAdapter
{
    private LayoutInflater layoutInflater;
    Context c;
    String codigo="codigo";
    private int[] layouts = new int[]{
            R.layout.kms100,
            R.layout.kms1200};

    int kms = 100;

    public PromosAdapter(Context c,String codigo, int kms) {
        this.c=c;
        this.codigo=codigo;
        this.kms = kms;
        Log.e("tag_miituo", ""+codigo);
    }

    public void updateBanners(int codigo, String cupon) {
        this.kms=codigo;
        this.codigo=cupon;
        this.notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater = LayoutInflater.from(c);
//            View view = layoutInflater.inflate(layouts[position], container, false);
        ViewGroup view = (ViewGroup) layoutInflater.inflate(layouts[position], container, false);

        try {
            container.addView(view);
            Log.e("tag_miituo", "view");
            if(position==0){
                Log.e("tag_miituo", "en el 0");
                LinearLayout ll=(LinearLayout)view.findViewById(R.id.cntCodigo);
                TextView lbCodigo=(TextView)view.findViewById(R.id.lbCodigo);
                TextView tvKil= (TextView)view.findViewById(R.id.tvKms);
                tvKil.setText(String.format("%d km", kms));
                String share="";
                for(int i=0;i<codigo.length();i++){
                    share=share+codigo.charAt(i)+" ";
                }
                lbCodigo.setText(share.substring(0,(share.length()-1)));
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager) c.getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText(codigo, codigo);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(c,"Código copiado al portapapeles.",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }catch(Exception e){
            Log.e("tag_miituo", "en el error");
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}