package com.example.parcial1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Punto2 extends Activity {

    EditText nombreApellido2, nivelHemo, correoET, edad;
    TextView pAnemia;
    Spinner spinnerNH;
    RadioButton radioFem, radioMas;

    String nombreApellidoValue ="", correoValue="", resultado ="", gender ="";
    int edadValue = 0;
    double nHemoValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punto2);

        nombreApellido2 = (EditText) findViewById(R.id.etNombre2);
        nivelHemo = (EditText) findViewById(R.id.etNHemo);
        correoET = (EditText) findViewById(R.id.etCorreo);
        edad = (EditText) findViewById(R.id.etEdad);

        pAnemia = (TextView) findViewById(R.id.etAnemia);

        radioFem = (RadioButton) findViewById(R.id.rBFem);
        radioMas = (RadioButton) findViewById(R.id.rBMas);

        spinnerNH = (Spinner) findViewById(R.id.spinnerNH);
        String[] tiempo = {"Meses","Años"};
        spinnerNH.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,tiempo));

    }

    public void validarNivel2(View view){


        double edadFinal = 0;

        nombreApellidoValue = nombreApellido2.getText().toString();
        correoValue = correoET.getText().toString();

        try {
            nHemoValue = Double.parseDouble(nivelHemo.getText().toString());
            edadValue = Integer.parseInt(edad.getText().toString());

            edadFinal = calcularEdad(edadValue, spinnerNH.getSelectedItem().toString());

        }catch (Exception e){
            mensaje("Este cambio es obligatorio.");
        }

        //Edad 0-1 mes

        System.out.println(edadFinal);

        if(nHemoValue < 13.0 &&  edadFinal <= 0.08){
            resultado = "es positivo para anemia.";
            System.out.println("1");
        }else if(nHemoValue >= 13.0 &&  edadFinal <= 0.08){
            resultado = "es negativo para anemia.";
            System.out.println("1");
        }

        //Edad 1-6 meses

        else if(nHemoValue < 10.0 &&  edadFinal > 0.08 && edadFinal <= 0.5){
            resultado = "es positivo para anemia.";
            System.out.println("2");
        }else if(nHemoValue >= 10.0 &&  edadFinal > 0.08 && edadFinal <= 0.5){
            resultado = "es negativo para anemia.";
            System.out.println("2");
        }

        //Edad 6-12 meses

        else if(nHemoValue < 11.0 &&  edadFinal > 0.5 && edadFinal <= 1){
            resultado = "es positivo para anemia.";
            System.out.println("3");
        }else if(nHemoValue >= 11.0 &&  edadFinal > 0.5 && edadFinal <= 1){
            resultado = "es negativo para anemia.";
            System.out.println("3");
        }

        //Edad 1-5 años

        else if(nHemoValue < 11.5 &&  edadFinal > 1 && edadFinal <= 5){
            resultado = "es positivo para anemia.";
            System.out.println("4");
        }else if(nHemoValue >= 11.5 &&  edadFinal > 1 && edadFinal <= 5){
            resultado = "es negativo para anemia.";
            System.out.println("4");
        }

        //Edad 5-10 años

        else if(nHemoValue < 12.6 &&  edadFinal > 5 && edadFinal <= 10){
            resultado = "es positivo para anemia.";
            System.out.println("5");
        }else if(nHemoValue >= 12.6 &&  edadFinal > 5 && edadFinal <= 10){
            resultado = "es negativo para anemia.";
            System.out.println("5");
        }

        //Edad > 15 (Mujeres)

        else if(nHemoValue < 12.0 &&  edadFinal > 15 && radioFem.isChecked()){
            resultado = "es positivo para anemia.";
            System.out.println("6");
        }else if(nHemoValue >= 12.0 &&  edadFinal > 15 && radioFem.isChecked()){
            resultado = "es negativo para anemia.";
            System.out.println("6");
        }

        //Edad > 15 (Hombres)

        else if(nHemoValue < 14.0 &&  edadFinal > 15 && radioMas.isChecked()){
            resultado = "es positivo para anemia.";
            System.out.println("7");
        }else if(nHemoValue >= 14.0 &&  edadFinal > 15 && radioMas.isChecked()){
            resultado = "es negativo para anemia.";
            System.out.println("7");
        }

        eleccion("El paciente " + nombreApellidoValue + " " + resultado);

    }

    public void eleccion(String respuesta){

        //Se crea instancia para la alerta
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        //Establecemos el mensaje en la alerta
        alertbox.setMessage(respuesta);

        //Elegimos la opcion positiva de la alerta
        alertbox.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            //En caso de presionar el botón realizara una acción
            public void onClick(DialogInterface dialog, int which) {
                //Se inserta en la BD
                if(consultarSiExiste(correoValue)){
                    modificarExamen();
                }else{
                    if(radioFem.isChecked()){
                        gender = "Femenino";
                    }else if(radioMas.isChecked()){
                        gender = "Masculino";
                    }
                    insertar(nombreApellidoValue, correoValue, edadValue, gender, spinnerNH.getSelectedItem().toString(), nHemoValue, resultado);
                }
            }
        });

        //Elegimos la opcion negativa de la alerta
        alertbox.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            //En caso de presionar el botón realizara una acción
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //Se muestra la alertbox
        alertbox.show();

    }

    public boolean consultarSiExiste(String correo)
    {
        boolean existe = false;
        AdminSQLiteOpenHelperP2 admin = new AdminSQLiteOpenHelperP2(this,"HemoHearth2",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor fila = bd.rawQuery("select nombre, edad, sexo, tiempo, nHemo, anemia from pacientes2 where correo="+correo,null);
        if(fila.moveToFirst())
        {
            existe = true;
        }else{
            mensaje("No se encontró paciente con ese correo");
        }
        return existe;
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

    public void insertar(String nombre, String correoVal, int edadVal, String sexoVal, String tiempo, double nivelHemoVal, String anemia){
        AdminSQLiteOpenHelperP2 admin = new AdminSQLiteOpenHelperP2(this,"HemoHearth2",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("nombre",nombre);
        registro.put("correo",correoVal);
        registro.put("edad",edadVal);
        registro.put("sexo",sexoVal);
        registro.put("tiempo",tiempo);
        registro.put("nHemo",nivelHemoVal);
        registro.put("anemia",anemia);
        bd.insert("pacientes2",null,registro);
        bd.close();

        nombreApellido2.setText("");
        correoET.setText("");
        edad.setText("");
        spinnerNH.setSelection(0);
        nivelHemo.setText("");
        pAnemia.setText("");

        mensaje("Se guardó la información correctamente");
    }

    public void consultarBD(View view) {
        String correoV = "";
        if(correoValue == "")
        {
            mensaje("Es necesario el campo del correo");
        }else{
            try {
                correoV = correoET.getText().toString();
            }catch (Exception e){}

            AdminSQLiteOpenHelperP2 admin = new AdminSQLiteOpenHelperP2(this,"HemoHearth2",null,1);
            SQLiteDatabase bd = admin.getWritableDatabase();

            Cursor fila = bd.rawQuery("select nombre, edad, sexo, tiempo, nHemo, anemia from pacientes2 where correo=" +correoV,null);
            if(fila.moveToFirst())
            {
                nombreApellido2.setText(fila.getString(0));
                correoET.setText(fila.getString(1));
                edad.setText(fila.getString(2));
                if(fila.getString(3).equals("Masculino")){
                    radioMas.isChecked();
                }else{
                    radioFem.isChecked();
                }
                spinnerNH.setSelection(((ArrayAdapter)spinnerNH.getAdapter()).getPosition(fila.getString(4)));
                nivelHemo.setText(fila.getString(5));
                pAnemia.setText(fila.getString(6));
            }else{
                mensaje("No se encontró paciente con ese correo");
            }
        }
    }

    public void modificarBD(View view) {
        AdminSQLiteOpenHelperP2 admin = new AdminSQLiteOpenHelperP2(this,"HemoHearth2",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String correo = "";

        try{
            correo = correoValue;
        }catch (Exception e){

        }

        String nombre = nombreApellidoValue;
        int edad = edadValue;
        double nHemo = nHemoValue;
        String tiempo = spinnerNH.getSelectedItem().toString();

        ContentValues registro = new ContentValues();
        registro.put("nombre",nombre);
        registro.put("edad",edad);
        registro.put("tiempo",tiempo);
        registro.put("nHemo",nHemo);
        int cant = bd.update("pacientes2",registro,"correo="+correo,null);
        bd.close();


        if(cant==1){
            mensaje("Se modificaron los datos correctamente");
        }else{
            mensaje("No existe paciente con ese correo");
        }
    }

    public void modificarExamen() {
        AdminSQLiteOpenHelperP2 admin = new AdminSQLiteOpenHelperP2(this,"HemoHearth2",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String correo = "";

        try{
            correo = correoValue;
        }catch (Exception e){

        }

        double nHemo = nHemoValue;
        int edad = edadValue;
        String tiempo = spinnerNH.getSelectedItem().toString();

        ContentValues registro = new ContentValues();
        registro.put("edad",edad);
        registro.put("tiempo",tiempo);
        registro.put("nHemo",nHemo);
        int cant = bd.update("pacientes2",registro,"correo="+correo,null);
        bd.close();


        if(cant==1){
            mensaje("Se modificaron los datos correctamente");
        }else{
            mensaje("No existe paciente con ese correo");
        }
    }

    public void eliminarBD(View view) {
        String correoV = "";

        AdminSQLiteOpenHelperP2 admin = new AdminSQLiteOpenHelperP2(this,"HemoHearth2",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        try {
            correoV = correoValue;
        }catch (Exception e){}

        int cant = bd.delete("pacientes2","correo="+correoV,null);
        bd.close();

        nombreApellido2.setText("");
        correoET.setText("");
        edad.setText("");
        spinnerNH.setSelection(0);
        nivelHemo.setText("");

        if(cant==1){
            mensaje("Se borró la información correctamente");
        }else{
            mensaje("No existe dicha persona con ese correo");
        }
    }

}
