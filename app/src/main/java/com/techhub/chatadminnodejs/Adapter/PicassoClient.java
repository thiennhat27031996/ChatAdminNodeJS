package com.techhub.chatadminnodejs.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by thiennhat on 16/10/2017.
 */

public class PicassoClient {
    public static void downloading(Context context, String url, ImageView img){
        if(url!=null && url.length()>0){

            img.setVisibility(View.VISIBLE);
        }
        else{

            img.setVisibility(View.GONE);


        }

    }
}
