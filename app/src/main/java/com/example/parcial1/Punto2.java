package com.example.parcial1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class Punto2 extends Activity {

    EditText nombreApellido2, nivelHemoglobina, correo, edad;
    Spinner spinner;
    RadioButton radioFem, radioMas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punto2);

        nombreApellido2 = (EditText) findViewById(R.id.etNombre2);
        nivelHemoglobina = (EditText) findViewById(R.id.etNHemo);
        correo = (EditText) findViewById(R.id.etCorreo);
        edad = (EditText) findViewById(R.id.etEdad);

        radioFem = (RadioButton) findViewById(R.id.rBFem);
        radioMas = (RadioButton) findViewById(R.id.rBMas);

        spinner = (Spinner) findViewById(R.id.spinnerNH);
        String[] tiempo = {"Meses","Años"};
        spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,tiempo));

    }

    public void validarNivel2(View view){

        String resultado ="";
        int nHemoValue = 0;
        double edadFinal = 0;

        String nombreApellidoValue = nombreApellido2.getText().toString();
        String correoValue = correo.getText().toString();

        try {
            nHemoValue = Integer.parseInt(nivelHemoglobina.getText().toString());
            int edadValue = Integer.parseInt(edad.getText().toString());

            edadFinal = calcularEdad(edadValue, spinner.getSelectedItem().toString());

        }catch (Exception e){
            mensaje("Este cambio es obligatorio.");
        }

        //Edad 0-1 mes

        if(nHemoValue < 13.0 &&  edadFinal <= 0.08){
            resultado = "es positivo para anemia.";
        }else if(nHemoValue >= 13.0 &&  edadFinal <= 0.08){
            resultado = "es negativo para anemia.";
        }

        //Edad 1-6 meses

        if(nHemoValue < 10.0 &&  edadFinal > 0.08 && edadFinal <= 0.5){
            resultado = "es positivo para anemia.";
        }else if(nHemoValue >= 10.0 &&  edadFinal > 0.08 && edadFinal <= 0.5){
            resultado = "es negativo para anemia.";
        }

        //Edad 6-12 meses

        if(nHemoValue < 11.0 &&  edadFinal > 0.5 && edadFinal <= 1){
            resultado = "es positivo para anemia.";
        }else if(nHemoValue >= 11.0 &&  edadFinal > 0.5 && edadFinal <= 1){
            resultado = "es negativo para anemia.";
        }

        //Edad 1-5 años

        if(nHemoValue < 11.5 &&  edadFinal > 1 && edadFinal <= 5){
            resultado = "es positivo para anemia.";
        }else if(nHemoValue >= 11.5 &&  edadFinal > 1 && edadFinal <= 5){
            resultado = "es negativo para anemia.";
        }

        //Edad 5-10 años

        if(nHemoValue < 12.6 &&  edadFinal > 5 && edadFinal <= 10){
            resultado = "es positivo para anemia.";
        }else if(nHemoValue >= 12.6 &&  edadFinal > 5 && edadFinal <= 10){
            resultado = "es negativo para anemia.";
        }

        //Edad > 15 (Mujeres)

        if(nHemoValue < 12.0 &&  edadFinal > 15 && radioFem.isChecked()){
            resultado = "es positivo para anemia.";
        }else if(nHemoValue >= 12.0 &&  edadFinal > 15 && radioFem.isChecked()){
            resultado = "es negativo para anemia.";
        }

        //Edad > 15 (Hombres)

        if(nHemoValue < 14.0 &&  edadFinal > 15 && radioMas.isChecked()){
            resultado = "es positivo para anemia.";
        }else if(nHemoValue >= 14.0 &&  edadFinal > 15 && radioMas.isChecked()){
            resultado = "es negativo para anemia.";
        }

        alerta("El paciente " + nombreApellidoValue + " " + resultado);


    }

    public void mensaje(String cadena){
        //Se crea el mensaje Toast con una longitud corta
        Toast.makeText(this,cadena,Toast.LENGTH_SHORT).show();
    }

    public double calcularEdad(int edad, String tiempo){

        double val = 0;

        if (tiempo == "Meses"){
            val = edad/12;
        }else{
            val = edad;
        }
        return val;
    }

    public void alerta(String cadena){
        //Se crea la instancia de la alerta
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        //Seleccionamos lo que vamos a mostrar
        dialogBuilder.setMessage(cadena);
        //Elegimos un titulo y configuramos para que se pueda quitar
        dialogBuilder.setCancelable(true).setTitle("Resultado del examen de anemia");
        //Mostramos la alerta dialogBuilder
        dialogBuilder.create().show();
    }

}
