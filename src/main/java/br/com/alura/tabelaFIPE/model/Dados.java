package br.com.alura.tabelaFIPE.model;

public record Dados(String codigo, String nome) {
    @Override
    public String toString() {
        return "codigo: " + this.codigo + ", " + "nome: " + this.nome;
    }
}
