package br.com.ifsp.appfinancas.model;

public class Conta {

    private Integer id;
    private String descricao;
    private Double saldo;

   public Conta(){}

   public Conta(Integer id){
       this.id = id;
   }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
