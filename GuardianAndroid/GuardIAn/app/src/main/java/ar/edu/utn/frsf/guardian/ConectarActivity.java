package ar.edu.utn.frsf.guardian;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ConectarActivity extends Activity {

    private EditText etIPServidor;
    private Button btnConectar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conectar);
        inicializarComponentes();

        btnConectar.setOnClickListener(v -> {
            conectar();
        });
    }

    private void conectar() {
        String ipServer = etIPServidor.getText().toString().trim();
        if (!ipServer.contains(":")) {
            ipServer += ":8080";
        }

        Intent i = new Intent(this,HablarActivity.class);
        Bundle b = new Bundle();
        i.putExtra("ipServer",ipServer);
        startActivity(i, b);
    }

    private void inicializarComponentes() {
        etIPServidor = findViewById(R.id.etIPServidor);
        btnConectar = findViewById(R.id.btnConectar);

        SharedPreferences preferencias = getApplicationContext().getSharedPreferences(getString(R.string.preferenciasHablar), Context.MODE_PRIVATE);
        preferencias.getString(getString(R.string.key_last_hablar_ip_server), "");
    }

}