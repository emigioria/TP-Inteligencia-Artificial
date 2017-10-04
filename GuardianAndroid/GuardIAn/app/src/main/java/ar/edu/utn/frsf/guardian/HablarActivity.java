package ar.edu.utn.frsf.guardian;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompHeader;
import ua.naiksoftware.stomp.client.StompClient;

public class HablarActivity extends AppCompatActivity {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private StompClient mStompClient;
    private Gson gson = new Gson();
    private String appId;

    private RelativeLayout mainView;
    private TextView txtAccionRecibida;
    private ImageButton btnSpeak;
    private ProgressBar esperaBar;
    private RelativeLayout hablarLayout;
    private TextView txtSpeechInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hablar);
        inicializarComponentes();

        if (savedInstanceState != null) {
            txtAccionRecibida.setText(savedInstanceState.getString("speechInput"));
        }
        btnSpeak.setOnClickListener(v -> promptSpeechInput());

        String ip = getIntent().getExtras().getString("ipServer");
        new Thread(() -> conectar(ip)).start();

        esperar(true);
    }

    private void esperar(boolean esperar) {
        if (esperar) {
            esperaBar.setVisibility(View.VISIBLE);
            hablarLayout.setVisibility(View.GONE);
        } else {
            esperaBar.setVisibility(View.GONE);
            hablarLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("speechInput", txtAccionRecibida.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void inicializarComponentes() {
        mainView = findViewById(R.id.mainView);
        txtAccionRecibida = findViewById(R.id.txtAccionRecibida);
        btnSpeak = findViewById(R.id.btnSpeak);
        esperaBar = findViewById(R.id.esperaBar);
        hablarLayout = findViewById(R.id.hablarLayout);
        txtSpeechInput = findViewById(R.id.txtSpeechInput);
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Snackbar.make(mainView, getString(R.string.google_app_store_msg), Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                    .setAction("Descargar", view -> {
                        String appPackageName = "com.google.android.googlequicksearchbox&hl=es";
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    })
                    .show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String escuchado = "Escuchado: " + result.get(0);
                    runOnUiThread(() -> txtSpeechInput.setText(escuchado));
                    enviarMensaje(new Mensaje(appId, result.get(0)));
                }
                break;
            }

        }
    }

    @Override
    protected void onDestroy() {
        if (mStompClient.isConnected()) {
            mStompClient.disconnect();
        }
        super.onDestroy();
    }

    private void conectar(String ipServer) {
        mStompClient = Stomp.over(WebSocket.class, "ws://" + ipServer + "/guardian");

        appId = this.getAppIdPreferences();
        if (appId == null) {
            try {
                this.getAppIdApi(ipServer);
            } catch (Exception e) {
                e.printStackTrace();
                errorConexion();
            }
            return;
        }

        mStompClient.topic("/topic/accion-" + appId).subscribe(topicMessage -> runOnUiThread(() ->
                txtAccionRecibida.setText(txtAccionRecibida.getText() + "\n" + topicMessage.getPayload())));

        mStompClient.lifecycle().subscribe(lifecycleEvent -> runOnUiThread(() -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    SharedPreferences preferencias = getApplicationContext().getSharedPreferences(getString(R.string.preferenciasHablar), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorPreferencias = preferencias.edit();
                    editorPreferencias.putString(getString(R.string.key_last_hablar_ip_server), ipServer);
                    editorPreferencias.apply();

                    esperar(false);
                    break;
                case ERROR:
                    errorConexion();
                    break;
                case CLOSED:
                    finish();
                    break;
            }
        }));
        try {
            mStompClient.connect(Collections.singletonList(new StompHeader(StompHeader.ID, appId)));
        } catch (Exception e) {
            finish();
        }
    }

    private void errorConexion() {
        Toast.makeText(this, R.string.error_conexion, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void enviarMensaje(Mensaje mensaje) {
        mStompClient.send("/app/fraseEscuchada", gson.toJson(mensaje)).subscribe();
    }

    private String getAppIdPreferences() {
        SharedPreferences preferencias = getApplicationContext().getSharedPreferences(getString(R.string.preferenciasHablar), Context.MODE_PRIVATE);
        return preferencias.getString(getString(R.string.key_id), null);
    }

    private void getAppIdApi(String ipServer) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipServer)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService api = retrofit.create(ApiService.class);
        api.getId().enqueue(new Callback<String>() {

            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                String id = response.body();

                SharedPreferences preferencias = getApplicationContext().getSharedPreferences(getString(R.string.preferenciasHablar), Context.MODE_PRIVATE);
                SharedPreferences.Editor editorPreferencias = preferencias.edit();
                editorPreferencias.putString(getString(R.string.key_id), id);
                editorPreferencias.apply();

                HablarActivity.this.conectar(ipServer);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                errorConexion();
            }

        });
    }
}
