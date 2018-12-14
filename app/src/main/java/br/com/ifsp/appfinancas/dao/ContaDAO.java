package br.com.ifsp.appfinancas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.ifsp.appfinancas.model.Conta;
import br.com.ifsp.appfinancas.util.SQLiteHelper;

public class ContaDAO {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;


    public ContaDAO(Context context){
        dbHelper = SQLiteHelper.getInstance(context);

    }

    public Boolean onSaveConta(Conta conta){

        try{
            database = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("descricao",conta.getDescricao());
            values.put("saldo",conta.getSaldo());

            database.insert("Conta",null,values);
            database.close();
            return Boolean.TRUE;
        }catch (Exception err){
            Log.i("Error", err.getMessage());
        }
        return  Boolean.FALSE;
      }

    public List<Conta> buscarContas() {
        database = dbHelper.getReadableDatabase();
        List<Conta> contasDB = new ArrayList<>();
        String[] cols = new String[]{"id", "descricao", "saldo"};
        Cursor cursor = database.query("conta", cols,null, null, null, null, "saldo");
        while (cursor.moveToNext()) {
            Conta conta = new Conta();
            conta.setId(cursor.getInt(0));
            conta.setDescricao(cursor.getString(1));
            conta.setSaldo(Double.valueOf(cursor.getString(2)));
            contasDB.add(conta);
        }
        cursor.close();
        database.close();
        return contasDB;
    }


    public Double buscarSaldoTotalContas(){
        try {
            database = dbHelper.getReadableDatabase();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT SUM(saldo) as total FROM conta");
            Cursor cursor = database.rawQuery(sql.toString(), null);

            cursor.moveToFirst(); // Precisa ir para o primeiro item do retorno da query, sem esse método, ocorre Exception;

            return cursor.getDouble(0);
        }catch(Exception e){
            Log.i("ERROR", e.getMessage());
        }

        return null;
    }
}
