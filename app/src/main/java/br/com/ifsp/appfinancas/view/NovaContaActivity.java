package br.com.ifsp.appfinancas.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.com.ifsp.appfinancas.R;
import br.com.ifsp.appfinancas.dao.ContaDAO;
import br.com.ifsp.appfinancas.model.Conta;

public class NovaContaActivity extends AppCompatActivity {

    private TextView descricaoContaView;
    private TextView saldoContaView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nova_conta);
        this.descricaoContaView = findViewById(R.id.descricao_conta_view);
        this.saldoContaView = findViewById(R.id.saldo_conta_view);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF424242")));

    }

    public void salvarConta(View view){
        if(validaCamposObrigatorios()){
          ContaDAO contaDAO = new ContaDAO(this);
          Conta conta = new Conta();
          conta.setDescricao(descricaoContaView.getText().toString());
          conta.setSaldo(Double.valueOf(saldoContaView.getText().toString()));

          if(contaDAO.onSaveConta(conta)){
              setResult(RESULT_OK);
          }
          finish();

        }else{
            Toast.makeText(this,"Campos obrigatórios não preenchidos", Toast.LENGTH_LONG).show();
        }
    }

    private Boolean validaCamposObrigatorios(){
        if(descricaoContaView.getText().toString().equals("") || saldoContaView.getText().toString().equals("")){
            return Boolean.FALSE;
        }
        return  Boolean.TRUE;
    }

}
