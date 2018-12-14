package br.com.ifsp.appfinancas.model;

public class Transacao {

    private Conta conta;
    private String descricao;
    private Double valor;
    private OrigemTransacao origem_transacao;
    private Integer natureza_operacao;

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public OrigemTransacao getOrigem_transacao() {
        return origem_transacao;
    }

    public void setOrigem_transacao(OrigemTransacao origem_transacao) {
        this.origem_transacao = origem_transacao;
    }

    public Integer getNatureza_operacao() {
        return natureza_operacao;
    }

    public void setNatureza_operacao(Integer natureza_operacao) {
        this.natureza_operacao = natureza_operacao;
    }
}
