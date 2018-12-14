package br.com.ifsp.appfinancas.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.ifsp.appfinancas.R;
import br.com.ifsp.appfinancas.adapter.ContaAdapter;
import br.com.ifsp.appfinancas.dao.ContaDAO;
import br.com.ifsp.appfinancas.dao.TransacaoDAO;
import br.com.ifsp.appfinancas.model.Conta;

public class ListaContaActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_NOVA_CONTA = 1;
    public static final int REQUEST_CODE_NOVA_TRANSACAO = 2;

    private ListView contaListView;

    private TextView saldoTotaltextView;
    private TextView saldoTotalTransacaoCreditoView;
    private TextView saldoTotalTransacaoDebitoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_conta);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF424242")));

        contaListView = findViewById(R.id.listaContaView );
        registerForContextMenu(contaListView);

        atualizarDadosTela();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Escolha a opção");
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), NovaTransacaoActivity.class);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Conta contaSelecionada = (Conta) contaListView.getItemAtPosition(info.position);
        intent.putExtra("conta_id", contaSelecionada.getId());

        startActivityForResult(intent, REQUEST_CODE_NOVA_TRANSACAO);

        return super.onContextItemSelected(item);
    }

    private void atualizarDadosTela() {

        ContaDAO contaDAO = new ContaDAO(this);
        TransacaoDAO transacaoDAO = new TransacaoDAO(this);

        contaListView = findViewById(R.id.listaContaView );

        saldoTotaltextView = findViewById(R.id.saldoTotalTextView);
        saldoTotalTransacaoCreditoView = findViewById(R.id.saldoTotalTransacaoCreditoTextView);
        saldoTotalTransacaoDebitoView = findViewById(R.id.saldoTotalTransacaoDebitoTextView);

        List<Conta> contasPersistidas = contaDAO.buscarContas();

        Double saldoTotalContas = contaDAO.buscarSaldoTotalContas();
        Double saldoTotalTransacaoCredito = transacaoDAO.buscarValorTransacoesCredito();
        Double saldoTotalTransacaoDebito = transacaoDAO.buscarValorTransacoesDebito();

        saldoTotaltextView.setText("R$ ".concat(String.valueOf(Math.round(saldoTotalContas))));
        saldoTotalTransacaoCreditoView.setText("R$ ".concat(saldoTotalTransacaoCredito.toString()));
        saldoTotalTransacaoDebitoView.setText("R$ ".concat(saldoTotalTransacaoDebito.toString()));

        if(saldoTotalContas < 0){
            saldoTotaltextView.setTextColor(Color.parseColor("#FFE85857"));
        }

        ContaAdapter adapterConta = new ContaAdapter(this, contasPersistidas);

        contaListView.setAdapter(adapterConta);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), NovaContaActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NOVA_CONTA);
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_NOVA_CONTA:
                if (resultCode == RESULT_OK) {
                    atualizarDadosTela();
                    Toast.makeText(this,"Conta Salvo com Sucesso", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this,"Erro ao salvar Conta", Toast.LENGTH_LONG).show();
                }
                break;

            case REQUEST_CODE_NOVA_TRANSACAO:
                if (resultCode == RESULT_OK) {
                    atualizarDadosTela();
                    Toast.makeText(this,"Transação salva com Sucesso", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this,"Erro ao salvar Transaçao", Toast.LENGTH_LONG).show();
                }
        }
    }

}