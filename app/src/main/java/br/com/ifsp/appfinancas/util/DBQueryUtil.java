package br.com.ifsp.appfinancas.util;

public final class DBQueryUtil {

    private DBQueryUtil(){

    }

    public static final String CREATE_TABLE_CONTA = "CREATE TABLE IF NOT EXISTS conta( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "descricao TEXT NOT NULL, " +
            "saldo DECIMAL (10,2));";

    public static final String CREATE_TABLE_TRANSACAO = "CREATE TABLE IF NOT EXISTS transacao( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "descricao TEXT NOT NULL, " +
            "valor DECIMAL (10,2), " +
            "conta INTEGER, " +
            "centro_custo INTEGER, " +
            "debito INTEGER, " +
            "dias_periodicidade INTEGER, " +
            "FOREIGN KEY(conta) REFERENCES conta(id), " +
            "FOREIGN KEY(centro_custo) REFERENCES centro_cuto(id) " +
            ");";

    public static final String CREATE_TABLE_ORIGEM_TRANSACAO = "CREATE TABLE IF NOT EXISTS origem_transacao( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "descricao TEXT NOT NULL); ";

}
