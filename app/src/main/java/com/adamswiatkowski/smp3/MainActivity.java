package com.adamswiatkowski.smp3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button red;
    Button green;
    Button blue;
    Button clear;
    DrawingSurface surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paint_main);

        red = (Button)findViewById(R.id.redB);
        green = (Button)findViewById(R.id.greenB);
        blue = (Button)findViewById(R.id.blueB);
        clear = (Button)findViewById(R.id.clearB);

        surface = (DrawingSurface)findViewById(R.id.drawingSurface);

        View.OnClickListener check_color = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surface.ustawKolor(((ColorDrawable)v.getBackground()).getColor());
            }
        };
        red.setOnClickListener(check_color);
        blue.setOnClickListener(check_color);
        green.setOnClickListener(check_color);
        clear.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        surface.czyscPowierzchnie();
                    }
                }
        );


    }
}
