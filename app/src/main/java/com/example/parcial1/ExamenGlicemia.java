package com.example.parcial1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class ExamenGlicemia extends Activity {

    TextView sintoma;
    double valorExamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen_glicemia);

        sintoma = (TextView) findViewById(R.id.tvSintoma);

        String sintomaValor = getIntent().getStringExtra("sintoma");
        sintoma.setText(sintomaValor);

        //Simulación del examen de glicemia
        Random r = new Random();
        valorExamen = 7.0 + (40.0 - 7.0) * r.nextDouble();

    }

    public void verResultado(View view) {

        String recomendacion ="";

        if(valorExamen>=7.0 && valorExamen<13.8)
        {
            recomendacion = "El resultado de tu examen de glicemia fue de: "+valorExamen+"\nRecomendaciones: \nIndicar glucemia en ayunas y TGP en pacientes sin diagnóstico. - Si deshidratación, rehidratación oral o EV según las demandas. - Reevaluar conducta terapéutica en diabéticos y cumplimiento de los pilares. - Reevaluar dosis de hipoglucemiantes.";
            alerta(recomendacion);
        }else if(valorExamen>=13.8 && valorExamen<=33)
        {
            recomendacion = "El resultado de tu examen de glicemia fue de: "+valorExamen+"\nRecomendaciones: \nCoordinar traslado y comenzar tratamiento. - Hidratación con Soluciónsalina 40 ml/Kg en las primeras 4 horas. 1-2 L la primera hora. -Administrar potasio al restituirse la diuresis o signos dehipopotasemia (depresión del ST, Onda U ≤ 1mv, ondas U≤ T). - Evitarinsulina hasta desaparecer signos de hipopotasemia. - Administrarinsulina simple 0,1 U/kg EV después de hidratar.";
            alerta(recomendacion);
        } else if(valorExamen>33)
        {
            recomendacion = "El resultado de tu examen de glicemia fue de: "+valorExamen+"\nRecomendaciones: \nCoordinar traslado y comenzar tratamiento. - Hidratación con SoluciónSalina 10-15 ml/Kg/h hasta conseguir estabilidad hemodinámica. -Administrar potasio al restituirse la diuresis o signos dehipopotasemia (depresión del ST, Onda U ≤ 1mv, ondas U≤ T).";
            alerta(recomendacion);
        }
    }

    public void alerta(String cadena){
        //Se crea la instancia de la alerta
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        //Seleccionamos lo que vamos a mostrar
        dialogBuilder.setMessage(cadena);
        //Elegimos un titulo y configuramos para que se pueda quitar
        dialogBuilder.setCancelable(true).setTitle("Titulo de la alerta");
        //Mostramos la alerta dialogBuilder
        dialogBuilder.create().show();
    }
}
