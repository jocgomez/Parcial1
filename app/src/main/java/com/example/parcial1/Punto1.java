package com.example.parcial1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class Punto1 extends Activity {

    EditText nombreApellido, cedula, eps;
    Spinner sintomaSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punto1);

        nombreApellido = (EditText) findViewById(R.id.etNombre);
        cedula = (EditText) findViewById(R.id.etCedula);
        eps = (EditText) findViewById(R.id.etEps);

        sintomaSpinner = (Spinner) findViewById(R.id.sintomas);
        String[] sintomas = {"Sin sintomas","Cuadro neurovegetativos","Trastornos de conciencia","Signos de deshidratación","Sepsis","Patologías agudas cardiovascular neurológica"};
        sintomaSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,sintomas));

    }

    public void validarNivel(View view) {

        String nombreApellidoValor = nombreApellido.getText().toString();
        try {
            int cedulaValor = Integer.parseInt(cedula.getText().toString());
        }catch (Exception e){}

        String epsValor = eps.getText().toString();

        if(sintomaSpinner.getSelectedItem().toString() != "Sin sintomas")
        {
            //Creamos la intentcion para pasar de pantalla
            Intent i = new Intent(this,ExamenGlicemia.class);

            //Ponemos los extras en el intent con CLAVE, VALOR
            i.putExtra("sintoma",sintomaSpinner.getSelectedItem().toString()+"");

            //Empezamos la intención
            startActivity(i);
        }

    }
}
