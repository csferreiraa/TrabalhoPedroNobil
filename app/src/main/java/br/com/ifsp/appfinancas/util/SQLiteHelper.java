package br.com.ifsp.appfinancas.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static SQLiteHelper instancia;
    private static final String BANCO_FINANCANCAS = "prod_jd";
    private static  final Integer VERSAO_BANCO_DADOS = 1;

    private SQLiteHelper(Context context){
        super(context, BANCO_FINANCANCAS, null, VERSAO_BANCO_DADOS);
    }

    public static SQLiteHelper getInstance (Context context){
        if(instancia == null){
            instancia = new SQLiteHelper(context);
        }
        return instancia;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBQueryUtil.CREATE_TABLE_CONTA);
        db.execSQL(DBQueryUtil.CREATE_TABLE_TRANSACAO);
        db.execSQL(DBQueryUtil.CREATE_TABLE_ORIGEM_TRANSACAO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
