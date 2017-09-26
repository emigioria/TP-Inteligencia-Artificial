package ar.edu.utn.frsf.guardian;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.Locale;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class MainActivity extends Activity {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentes();

        conectar();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    private void inicializarComponentes() {
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
    }

    /**
     * Showing google speech input dialog
     * */
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
            Snackbar.make(findViewById(R.id.mainView), getString(R.string.google_app_store_msg), Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                    .setAction("Descargar", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String appPackageName = "com.google.android.googlequicksearchbox&hl=es";
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    })
                    .show();
        }
    }

    /**
     * Receiving speech input
     * */
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
        mStompClient.disconnect();
        super.onDestroy();
    }

    private StompClient mStompClient;

    private void conectar() {
        mStompClient = Stomp.over(WebSocket.class, "ws://192.168.0.4:8080/guardian");
        mStompClient.topic("/topic/accion").subscribe(topicMessage -> {
            runOnUiThread(() -> txtSpeechInput.setText(txtSpeechInput.getText() + "\n" + topicMessage.getPayload()));
        });
        mStompClient.connect();
    }

    private void enviarMensaje(String mensaje){
        mStompClient.send("/app/fraseEscuchada", mensaje).subscribe();
    }
}