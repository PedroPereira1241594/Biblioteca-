package Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Emprestimos {
    private int numero;
    private Utentes utente;
    private List<ItemEmprestavel> itens;  // Alterado para ItemEmprestavel para permitir diferentes tipos de itens
    private LocalDate dataInicio;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataEfetivaDevolucao;

    // Construtor atualizado para receber LocalDate e uma lista de ItemEmprestavel
    public Emprestimos(int numero, Utentes utente, List<ItemEmprestavel> itens, LocalDate dataInicio, LocalDate dataPrevistaDevolucao, LocalDate dataEfetivaDevolucao) {
        this.numero = numero;
        this.utente = utente;
        this.itens = itens;
        this.dataInicio = dataInicio;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.dataEfetivaDevolucao = dataEfetivaDevolucao;
    }

    // Getters e Setters
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Utentes getUtente() {
        return utente;
    }

    public void setUtente(Utentes utente) {
        this.utente = utente;
    }

    public List<ItemEmprestavel> getItens() {
        return itens;
    }

    public void setItens(List<ItemEmprestavel> itens) {
        this.itens = itens;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public LocalDate getDataEfetivaDevolucao() {
        return dataEfetivaDevolucao;
    }

    public void setDataEfetivaDevolucao(LocalDate dataEfetivaDevolucao) {
        this.dataEfetivaDevolucao = dataEfetivaDevolucao;
    }

    // Método adicional para obter a data de empréstimo (igual à data de início)
    public LocalDate getDataEmprestimo() {
        return getDataInicio();
    }

    @Override
    public String toString() {
        String result = "Emprestimo [numero=" + numero +
                ", utente=" + utente.getNome() +
                ", itens=";

        // Adiciona os itens (livros ou jornais) à string
        for (ItemEmprestavel item : itens) {
            result += item.toString() + ", ";
        }

        // Remove a última vírgula e espaço
        if (!itens.isEmpty()) {
            result = result.substring(0, result.length() - 2); // Remove o ", " extra
        }

        result += ", dataInicio=" + dataInicio +
                ", dataPrevistaDevolucao=" + dataPrevistaDevolucao +
                ", dataEfetivaDevolucao=" + dataEfetivaDevolucao + "]";

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Verifica se é o mesmo objeto
        if (o == null || getClass() != o.getClass()) return false; // Verifica classe e null
        Emprestimos that = (Emprestimos) o;
        return numero == that.numero; // Compara pelo número do empréstimo (assumindo que é único)
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero); // Usa o número como base para o hash
    }
}
