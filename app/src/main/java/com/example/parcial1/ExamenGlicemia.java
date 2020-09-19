package com.example.parcial1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Random;

public class ExamenGlicemia extends Activity {

    TextView sintoma;
    double valorExamen;
    String cuadroDiabetico;

    String nombre, eps,sintomaValor;
    int cedula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen_glicemia);

        sintoma = (TextView) findViewById(R.id.tvSintoma);

        sintomaValor = getIntent().getStringExtra("sintoma");
        nombre = getIntent().getStringExtra("nombre");
        cedula = getIntent().getIntExtra("cedula",0);
        eps = getIntent().getStringExtra("eps");

        sintoma.setText(sintomaValor);

        //Simulación del examen de glicemia
        Random r = new Random();
        valorExamen = 7.0 + (40.0 - 7.0) * r.nextDouble();

    }

    public void verResultado(View view) {

        String recomendacion ="";

        if(valorExamen>=7.0 && valorExamen<13.8)
        {
            cuadroDiabetico = "HIPERGLICEMIA AISLADA";
            recomendacion = cuadroDiabetico+"\n\nEl resultado de tu examen de glicemia fue de: "+valorExamen+"\n\nRecomendaciones: \n\nIndicar glucemia en ayunas y TGP en pacientes sin diagnóstico. - Si deshidratación, rehidratación oral o EV según las demandas. - Reevaluar conducta terapéutica en diabéticos y cumplimiento de los pilares. - Reevaluar dosis de hipoglucemiantes.";
            eleccion(recomendacion);
        }else if(valorExamen>=13.8 && valorExamen<=33)
        {
            cuadroDiabetico = "CETOACIDOSIS DIABÉTICA";
            recomendacion = cuadroDiabetico+"\n\nEl resultado de tu examen de glicemia fue de: "+valorExamen+"\n\nRecomendaciones: \n\nCoordinar traslado y comenzar tratamiento. - Hidratación con Solución salina 40 ml/Kg en las primeras 4 horas. 1-2 L la primera hora. -Administrar potasio al restituirse la diuresis o signos dehipopotasemia (depresión del ST, Onda U ≤ 1mv, ondas U≤ T). - Evitarinsulina hasta desaparecer signos de hipopotasemia. - Administrarinsulina simple 0,1 U/kg EV después de hidratar.";
            eleccion(recomendacion);
        } else if(valorExamen>33)
        {
            cuadroDiabetico = "ESTADO HIPEROSMOLAR HIPERGLUCÉMICO NO CETÓSICO";
            recomendacion = cuadroDiabetico+"\n\nEl resultado de tu examen de glicemia fue de: "+valorExamen+"\n\nRecomendaciones: \n\nCoordinar traslado y comenzar tratamiento. - Hidratación con Solución salina 10-15 ml/Kg/h hasta conseguir estabilidad hemodinámica. -Administrar potasio al restituirse la diuresis o signos dehipopotasemia (depresión del ST, Onda U ≤ 1mv, ondas U≤ T).";
            eleccion(recomendacion);
        }


    }

    public void eleccion(String cadena)
    {
        //Se crea instancia para la alerta
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        //Establecemos el mensaje en la alerta
        alertbox.setMessage(cadena);

        //Elegimos la opcion positiva de la alerta
        alertbox.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            //En caso de presionar el botón realizara una acción
            public void onClick(DialogInterface dialog, int which) {
                //Se inserta en la BD
                if(consultarSiExiste(cedula)){
                    modificarBD();
                }else{
                    insertar(nombre, eps, cedula, "Con patologia", cuadroDiabetico, sintomaValor);
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

        //Creamos la intentcion para pasar de pantalla
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("vacio",true);
        //Empezamos la intención
        startActivity(i);

        mensaje("Se guardó la información correctamente");
    }

    public void mensaje(String cadena){
        //Se crea el mensaje Toast con una longitud corta
        Toast.makeText(this,cadena,Toast.LENGTH_SHORT).show();
    }

    public boolean consultarSiExiste(int cedula)
    {
        boolean existe = false;
        AdminSQLiteOpenHelperP1 admin = new AdminSQLiteOpenHelperP1(this,"HemoHearth",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor fila = bd.rawQuery("select nombre, eps, patologia, tipoPatologia, sintoma from pacientes where cedula="+cedula,null);
        if(fila.moveToFirst())
        {
            existe = true;
        }else{
            mensaje("No se encontró paciente con esa cédula");
        }
        return existe;
    }

    public void modificarBD() {
        AdminSQLiteOpenHelperP1 admin = new AdminSQLiteOpenHelperP1(this,"HemoHearth",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String sintoma = sintomaValor;
        String patologia = "Con patologia";
        String tipoPatologia = cuadroDiabetico;

        ContentValues registro = new ContentValues();
        registro.put("sintoma",sintoma);
        registro.put("patologia",patologia);
        registro.put("tipoPatologia",tipoPatologia);
        int cant = bd.update("pacientes",registro,"cedula="+cedula,null);
        bd.close();

        //Creamos la intentcion para pasar de pantalla
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("vacio",true);
        //Empezamos la intención
        startActivity(i);


        if(cant==1){
            mensaje("Se modificaron los datos correctamente");
        }else{
            mensaje("No existe paciente con dicho documento");
        }
    }
}
