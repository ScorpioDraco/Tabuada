package com.example.tabuada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Captura o evento "Enviar" no teclado virtual
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    responder();
                    handled = true;
                }
                return handled;
            }
        });

        exercicio();
    }

    // Define o peso dos fatores que serão utilizados
    int[] fatores = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5, 6, 7, 8, 9, 4, 6, 7, 8, 9};
    int exercicios = 0;
    int gabarito = 0;
    int erros = 0;
    boolean respostaIncorreta = false;

    public void exercicio() {

        Random random = new Random();

        int aIdx = new Random().nextInt(fatores.length);
        int bIdx = new Random().nextInt(fatores.length);
        int a = fatores[aIdx];
        int b = fatores[bIdx];

        String enunciado = a + " x " + b + " =";
        ((TextView)findViewById(R.id.txtConta)).setText(enunciado);
        gabarito = a * b;

        // Traz o foco para o editText
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        // imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        // imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void responder() {

        TextView txtResultado = ((TextView)findViewById(R.id.txtResultado));
        TextView txtEstatisticas = ((TextView)findViewById(R.id.txtEstatisticas));
        EditText editText = ((EditText)findViewById(R.id.editText));

        String resposta = editText.getText().toString();

        if (!resposta.isEmpty()) {

            if (!respostaIncorreta) {
                exercicios++;
            }

            // Resposta correta
            if (Integer.parseInt(resposta) == gabarito) {
                txtResultado.setTextColor(Color.BLUE);
                txtResultado.setText("Resposta correta!!!");
                respostaIncorreta = false;
            }
            // Resposta incorreta
            else {
                txtResultado.setTextColor(Color.RED);
                txtResultado.setText("Resposta incorreta...\nTente de novo!");
                if (!respostaIncorreta) {
                    erros++;
                }
                respostaIncorreta = true;
            }

            // Imprime as estatísticas
            float acertos = (float) (exercicios - erros) * 100 / exercicios;
            NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            String acertosStr = nf.format(acertos);
            String estatistica = String.format("Exercícios realizados: %d\nQuantidade de erros: %d\nAcertos: %s%%", exercicios, erros, acertosStr);
            txtEstatisticas.setText(estatistica);

            if (!respostaIncorreta) {
                exercicio();
                editText.setText("");
                editText.requestFocus();
            }

            editText.setSelectAllOnFocus(true);
            editText.selectAll();
        }
    }
}
