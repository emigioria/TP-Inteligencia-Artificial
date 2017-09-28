package ar.edu.utn.frsf.guardian;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.Locale;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class HablarActivity extends AppCompatActivity {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private StompClient mStompClient;

    private RelativeLayout mainView;
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hablar);
        inicializarComponentes();

        if (savedInstanceState != null) {
            txtSpeechInput.setText(savedInstanceState.getString("speechInput"));
        }
        String ip = getIntent().getExtras().getString("ipServer");
        conectar(ip);

        btnSpeak.setOnClickListener(v -> promptSpeechInput());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("speechInput", txtSpeechInput.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void inicializarComponentes() {
        mainView = findViewById(R.id.mainView);
        txtSpeechInput = findViewById(R.id.txtSpeechInput);
        btnSpeak = findViewById(R.id.btnSpeak);
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

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    enviarMensaje(result.get(0));
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
        mStompClient.topic("/topic/accion").subscribe(topicMessage -> runOnUiThread(() ->
                txtSpeechInput.setText(txtSpeechInput.getText() + "\n" + topicMessage.getPayload())));

        mStompClient.lifecycle().subscribe(lifecycleEvent -> runOnUiThread(() -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    SharedPreferences preferencias = getApplicationContext().getSharedPreferences(getString(R.string.preferenciasHablar), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorPreferencias = preferencias.edit();
                    editorPreferencias.putString(getString(R.string.key_last_hablar_ip_server), ipServer);
                    editorPreferencias.apply();
                    break;
                case ERROR:
                    Toast.makeText(this, R.string.error_conexion, Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case CLOSED:
                    finish();
                    break;
            }
        }));
        try {
            mStompClient.connect();
        } catch (Exception e) {
            finish();
        }
    }

    private void enviarMensaje(String mensaje) {
        mStompClient.send("/app/fraseEscuchada", mensaje).subscribe();
    }
}
