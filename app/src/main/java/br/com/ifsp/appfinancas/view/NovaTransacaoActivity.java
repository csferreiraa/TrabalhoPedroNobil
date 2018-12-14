package br.com.ifsp.appfinancas.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.ifsp.appfinancas.R;
import br.com.ifsp.appfinancas.dao.TransacaoDAO;
import br.com.ifsp.appfinancas.model.Conta;
import br.com.ifsp.appfinancas.model.OrigemTransacao;
import br.com.ifsp.appfinancas.model.Transacao;

public class NovaTransacaoActivity extends AppCompatActivity {

    private EditText descricaoTransacaoEditText;
    private EditText valorTransacaoEditView;
    private RadioButton transacaoDebitoRadioButton;
    private RadioButton transacaoCreditoRadioButton;
    private Spinner spinnerOrigemTransacao;

    List<OrigemTransacao> OrigemTransacaoList;
    public static final int DEBITO = 1;
    public static final int CREDITO = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nova_transacao);

        popularSpinnerOrigemTransacao();
        pegarValoresTela();

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF424242")));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        popularSpinnerOrigemTransacao();
        pegarValoresTela();
    }

    private void popularSpinnerOrigemTransacao() {
        OrigemTransacaoList = new TransacaoDAO(this).buscarOrigemTransacao();
        List<String> origemTransacaoDescricoes = new ArrayList<>();

        for(OrigemTransacao objeto : OrigemTransacaoList){
            origemTransacaoDescricoes.add(objeto.getDescricao());
        }

        ArrayAdapter<String> centroCustoArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, origemTransacaoDescricoes);


        spinnerOrigemTransacao = findViewById(R.id.spinnerOrigemTransacao);
        spinnerOrigemTransacao.setAdapter(centroCustoArrayAdapter);
    }

    private void pegarValoresTela(){
        this.descricaoTransacaoEditText = findViewById(R.id.descricaoTransacaoEditText);
        this.valorTransacaoEditView = findViewById(R.id.valorTransacaoEditText);
        this.transacaoDebitoRadioButton = findViewById(R.id.transacaoDebitoRadioButton);
        this.transacaoCreditoRadioButton = findViewById(R.id.transacaoCreditoRadioButton);
        this.spinnerOrigemTransacao = findViewById(R.id.spinnerOrigemTransacao);
    }

    private List<String> verificarCamposPreenchidos(){
        List<String> listaErros = new ArrayList<>();

        if(descricaoTransacaoEditText.getText().toString().equals("")){
            listaErros.add("A descrição não pode ser vazia.");
        }

        if(valorTransacaoEditView.getText().toString().equals("")){
            listaErros.add("O valor não pode ser vazio");
        }

        if(!transacaoDebitoRadioButton.isChecked() && !transacaoCreditoRadioButton.isChecked()){
            listaErros.add("Selecione a natureza da operação.");
        }

        return listaErros;
    }

    public void salvarTransacao(View view){

        TextView errosCamposPreenchidos = findViewById(R.id.errosCampos);
        List<String> erros = verificarCamposPreenchidos();

        if(erros.isEmpty()){
            errosCamposPreenchidos.setVisibility(View.GONE);
            if(persistirDados()){
                setResult(RESULT_OK);
            }else{
                setResult(-13); //numero mágico aqui
            }
            finish();
        }else{
            String mensagem = "Erros de preenchimento: \n\n";
            Integer contador = 1;

            for(String erroCampo : erros){
                mensagem += contador.toString().concat(" - ").concat(erroCampo).concat("\n");
                contador++;
            }

            errosCamposPreenchidos = findViewById(R.id.errosCampos);
            errosCamposPreenchidos.setText(mensagem);
            errosCamposPreenchidos.setTextColor(Color.parseColor("#FFE85857"));
            errosCamposPreenchidos.setVisibility(View.VISIBLE);
        }
    }

    private Boolean persistirDados() {
        Transacao transacaoNova = new Transacao();
        transacaoNova.setDescricao(descricaoTransacaoEditText.getText().toString());
        transacaoNova.setValor(Double.valueOf(valorTransacaoEditView.getText().toString()));
        transacaoNova.setConta(new Conta((Integer) getIntent().getSerializableExtra("conta_id")));

        Integer indexItemClicado = spinnerOrigemTransacao.getSelectedItemPosition();
        transacaoNova.setOrigem_transacao(OrigemTransacaoList.get(indexItemClicado));

        if(transacaoDebitoRadioButton.isChecked()){
            transacaoNova.setNatureza_operacao(DEBITO);
        }else{
            transacaoNova.setNatureza_operacao(CREDITO);
        }

        TransacaoDAO transacaoDAO = new TransacaoDAO(this);
        return transacaoDAO.salvarTransacao(transacaoNova);
    }


}
