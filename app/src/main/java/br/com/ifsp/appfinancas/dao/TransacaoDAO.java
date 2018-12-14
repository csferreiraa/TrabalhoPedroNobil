package br.com.ifsp.appfinancas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.ifsp.appfinancas.model.OrigemTransacao;
import br.com.ifsp.appfinancas.model.Transacao;
import br.com.ifsp.appfinancas.util.SQLiteHelper;

public class TransacaoDAO {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public TransacaoDAO(Context context) {
        this.dbHelper = SQLiteHelper.getInstance(context);
    }

    public static final int DEBITO = 1;
    public static final int CREDITO = 0;

    public Double buscarValorTransacoesDebito(){
        database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT SUM(valor) as total FROM transacao WHERE debito = ?", new String[] {String.valueOf(1)});

        cursor.moveToFirst();

        return cursor.getDouble(0);
    }

    public Double buscarValorTransacoesCredito(){
        database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT SUM(valor) as total FROM transacao WHERE debito = ?", new String[] {String.valueOf(0)});

        cursor.moveToFirst();

        return cursor.getDouble(0);
    }

    public List<OrigemTransacao> buscarOrigemTransacao() {
        List<OrigemTransacao> listaCentroCustoDB = new ArrayList<>();

        try {
            database = dbHelper.getReadableDatabase();


            String[] cols = new String[]{"id", "descricao"};
            Cursor cursor = database.query("origem_transacao",
                    cols,
                    null, null, null, null, "descricao");

            while (cursor.moveToNext()) {
                OrigemTransacao centroCusto = new OrigemTransacao();
                centroCusto.setId(cursor.getInt(0));
                centroCusto.setDescricao(cursor.getString(1));
                listaCentroCustoDB.add(centroCusto);
            }

            if(listaCentroCustoDB.isEmpty()){
                popularTabelaOrigemTransacao();
                return buscarOrigemTransacao(); //Recursão foi necessária para popular a tabela caso nao exista nada ainda.
            }
        } catch (Exception e) {
            Log.i("info", e.getMessage());
        }

        return listaCentroCustoDB;
    }

    private void popularTabelaOrigemTransacao(){
        database = dbHelper.getWritableDatabase();

        List<ContentValues> listaContentValues = new ArrayList<ContentValues>();

        List<String> descricaoCentroCusto = new ArrayList<>();

        descricaoCentroCusto.add("Salário");
        descricaoCentroCusto.add("Moradia");
        descricaoCentroCusto.add("Combustível");
        descricaoCentroCusto.add("Educação");
        descricaoCentroCusto.add("Alimentação");
        descricaoCentroCusto.add("Transporte");
        descricaoCentroCusto.add("Saúde");
        descricaoCentroCusto.add("Tarifas em geral");
        descricaoCentroCusto.add("Corporativo");


        for(int i = 0; i < descricaoCentroCusto.size(); i++){
            ContentValues values = new ContentValues();
            values.put("descricao", descricaoCentroCusto.get(i));
            listaContentValues.add(values);
        }

        for(ContentValues values : listaContentValues){
            database.insert("origem_transacao", null, values);
        }
    }

    public Boolean salvarTransacao(Transacao transacao) {
        try{
            database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("descricao", transacao.getDescricao());
            values.put("valor", transacao.getValor());
            values.put("conta", transacao.getConta().getId());
            values.put("centro_custo", transacao.getOrigem_transacao().getId());
            values.put("debito", transacao.getNatureza_operacao());

            database.insert("transacao", null, values);

            Double saldo = buscarSaldoContaPorId(transacao.getConta().getId());

            if(transacao.getNatureza_operacao() == DEBITO){
                saldo = saldo - transacao.getValor();
            }else{
                saldo = saldo + transacao.getValor();
            }

            //Atualizar saldo da conta
            ContentValues valor = new ContentValues();
            valor.put("saldo", saldo);
            database.update("conta", valor, "id="+transacao.getConta().getId(), null);

            database.close();
        }catch(Exception e){
            Log.i("info", e.getMessage());
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    public Double buscarSaldoContaPorId(Integer id){
        Double saldo = new Double(0);
        database = dbHelper.getReadableDatabase();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(saldo) as total FROM conta WHERE id = " + id);
        Cursor cursor = database.rawQuery(sql.toString(), null);

        cursor.moveToFirst(); // Precisa ir para o primeiro item do retorno da query, sem esse método, ocorre Exception;

        saldo = cursor.getDouble(0);

        return saldo;
    }
}
