package com.example.julian.agromobile.layoutmanagers;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * Created by DarioFernando on 22/07/2015.
 */
public class CustomGridLayoutManager extends GridLayoutManager {

    public CustomGridLayoutManager(Context context) {
        super(context, 2);
        setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position%3==0)
                    return 2;
                else
                    return 1;
            }
        });
    }


}
