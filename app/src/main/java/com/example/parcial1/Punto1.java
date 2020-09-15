package com.example.parcial1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Punto1 extends Activity {

    EditText nombreApellido, cedula, eps;
    TextView patologiaTV;
    Spinner sintomaSpinner;

    boolean vacio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punto1);

        nombreApellido = (EditText) findViewById(R.id.etNombre);
        cedula = (EditText) findViewById(R.id.etCedula);
        eps = (EditText) findViewById(R.id.etEps);

        patologiaTV = (TextView) findViewById(R.id.patologia);

        sintomaSpinner = (Spinner) findViewById(R.id.sintomas);
        String[] sintomas = {"Sin sintomas","Cuadro neurovegetativos","Trastornos de conciencia","Signos de deshidratación","Sepsis","Patologías agudas cardiovascular neurológica"};
        sintomaSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,sintomas));

        vacio = getIntent().getBooleanExtra("vacio",false);
        if(vacio)
        {
            nombreApellido.setText("");
            eps.setText("");
            cedula.setText("");
            patologiaTV.setText("");
            sintomaSpinner.setSelection(0);
        }
    }

    public void validarNivel(View view) {
        int cedulaValor=0;
        String nombreApellidoValor = nombreApellido.getText().toString();
        try {
            cedulaValor = Integer.parseInt(cedula.getText().toString());
        }catch (Exception e){}

        String epsValor = eps.getText().toString();

        if(sintomaSpinner.getSelectedItem().toString() != "Sin sintomas")
        {
            //Creamos la intentcion para pasar de pantalla
            Intent i = new Intent(this,ExamenGlicemia.class);

            //Ponemos los extras en el intent con CLAVE, VALOR
            i.putExtra("nombre",nombreApellidoValor+"");
            i.putExtra("eps",epsValor+"");
            i.putExtra("cedula",cedulaValor);
            i.putExtra("sintoma",sintomaSpinner.getSelectedItem().toString()+"");

            //Empezamos la intención
            startActivity(i);
        }else{
            insertar(nombreApellidoValor,epsValor,cedulaValor,"Sin patologia","Ninguna", sintomaSpinner.getSelectedItem().toString());
        }

    }

    public void insertar(String nombre, String epsValor, int cedulaValor, String patologia, String tipoPatologia, String sintoma){
        AdminSQLiteOpenHelperP1 admin = new AdminSQLiteOpenHelperP1(this,"HemoHearth",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("nombre",nombre);
        registro.put("eps",epsValor);
        registro.put("cedula",cedulaValor);
        registro.put("patologia",patologia);
        registro.put("tipoPatologia",tipoPatologia);
        registro.put("sintoma",sintoma);
        bd.insert("pacientes",null,registro);
        bd.close();

        nombreApellido.setText("");
        eps.setText("");
        cedula.setText("");
        patologiaTV.setText("");
        sintomaSpinner.setSelection(0);

        mensaje("Se guardó la información correctamente");
    }

    public void mensaje(String cadena){
        //Se crea el mensaje Toast con una longitud corta
        Toast.makeText(this,cadena,Toast.LENGTH_SHORT).show();
    }

    public void consultarBD(View view) {
        int cedulaValor = 0;
        if(cedula.getText().toString() == "")
        {
            mensaje("Es necesario el campo de la cédula");
        }else{
            try {
                cedulaValor = Integer.parseInt(cedula.getText().toString());
            }catch (Exception e){}

            AdminSQLiteOpenHelperP1 admin = new AdminSQLiteOpenHelperP1(this,"HemoHearth",null,1);
            SQLiteDatabase bd = admin.getWritableDatabase();

            Cursor fila = bd.rawQuery("select nombre, eps, patologia, tipoPatologia, sintoma from pacientes where cedula="+cedulaValor,null);
            if(fila.moveToFirst())
            {
                nombreApellido.setText(fila.getString(0));
                eps.setText(fila.getString(1));
                if(fila.getString(2).equals("Sin patologia")){
                    patologiaTV.setText("El paciente no tiene nigun tipo de patología");
                }else{
                    patologiaTV.setText("El paciente tiene una patología de tipo "+fila.getString(3));
                }

                sintomaSpinner.setSelection(((ArrayAdapter)sintomaSpinner.getAdapter()).getPosition(fila.getString(4)));
            }else{
                mensaje("No se encontró paciente con esa cédula");
            }
        }
    }

    public void eliminarBD(View view) {
        int cedulaValor = 0;

        AdminSQLiteOpenHelperP1 admin = new AdminSQLiteOpenHelperP1(this,"HemoHearth",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        try {
            cedulaValor = Integer.parseInt(cedula.getText().toString());
        }catch (Exception e){}

        int cant = bd.delete("pacientes","cedula="+cedulaValor,null);
        bd.close();

        nombreApellido.setText("");
        eps.setText("");
        cedula.setText("");
        patologiaTV.setText("");
        sintomaSpinner.setSelection(0);

        if(cant==1){
            mensaje("Se borró la información correctamente");
        }else{
            mensaje("No existe dicha persona con ese número de cédula");
        }
    }

    public void modificarBD(View view) {
        AdminSQLiteOpenHelperP1 admin = new AdminSQLiteOpenHelperP1(this,"HemoHearth",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        int cedulaValor = 0;
        try {
            cedulaValor = Integer.parseInt(cedula.getText().toString());
        }catch (Exception e){}
        String nombreValor = nombreApellido.getText().toString();
        String epsValor = eps.getText().toString();
        String sintomaValor = sintomaSpinner.getSelectedItem().toString();

        ContentValues registro = new ContentValues();
        registro.put("nombre",nombreValor);
        registro.put("eps",epsValor);
        registro.put("sintoma",sintomaValor);
        int cant = bd.update("pacientes",registro,"cedula="+cedulaValor,null);
        bd.close();

        if(cant==1){
            mensaje("Se modificaron los datos correctamente");
        }else{
            mensaje("No existe paciente con dicho documento");
        }
    }


}
