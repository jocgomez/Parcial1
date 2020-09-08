package com.example.parcial1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //--Asignamos las propiedades del control
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("Ejercicio 1");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Ejercicio 2");

        tab1.setIndicator("Punto 1");//Setea el nombre del tab 1
        //Intencion para cargar el contenido del tab 1
        tab1.setContent(new Intent(this,Punto1.class));

        tab2.setIndicator("Punto 2");//Setea el nombre del tab 2
        //Intencion para cargar el contenido del tab 2
        tab2.setContent(new Intent(this,Punto2.class));

        tabHost.addTab(tab1); //Agrega el tab1 al tabhost
        tabHost.addTab(tab2); //Agrega el tab2 al tabhost
    }
}
