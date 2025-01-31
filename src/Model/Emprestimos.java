package Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Classe que representa um empréstimo de itens a um utente.
 * Um empréstimo contém um número de identificação, um utente associado,
 * uma lista de itens emprestados, além das datas de início, prevista e efetiva de devolução.
 */
public class Emprestimos {
    private int numero;
    private Utentes utente;
    private List<ItemEmprestavel> itens;
    private LocalDate dataInicio;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataEfetivaDevolucao;

    /**
     * Construtor da classe Emprestimos.
     *
     * @param numero Número identificador do empréstimo.
     * @param utente Utente a quem os itens foram emprestados.
     * @param itens Lista de itens emprestados.
     * @param dataInicio Data de início do empréstimo.
     * @param dataPrevistaDevolucao Data prevista para devolução dos itens.
     * @param dataEfetivaDevolucao Data efetiva da devolução dos itens (pode ser null se ainda não devolvido).
     */
    public Emprestimos(int numero, Utentes utente, List<ItemEmprestavel> itens, LocalDate dataInicio, LocalDate dataPrevistaDevolucao, LocalDate dataEfetivaDevolucao) {
        this.numero = numero;
        this.utente = utente;
        this.itens = itens;
        this.dataInicio = dataInicio;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.dataEfetivaDevolucao = dataEfetivaDevolucao;
    }

    /**
     * Obtém o número do empréstimo.
     * @return Número do empréstimo.
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Define o número do empréstimo.
     * @param numero Número do empréstimo.
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * Obtém o utente associado ao empréstimo.
     * @return Utente do empréstimo.
     */
    public Utentes getUtente() {
        return utente;
    }

    /**
     * Define o utente associado ao empréstimo.
     * @param utente Novo utente do empréstimo.
     */
    public void setUtente(Utentes utente) {
        this.utente = utente;
    }

    /**
     * Obtém a lista de itens emprestados.
     * @return Lista de itens emprestados.
     */
    public List<ItemEmprestavel> getItens() {
        return itens;
    }

    /**
     * Define a lista de itens emprestados.
     * @param itens Lista de itens a serem associados ao empréstimo.
     */
    public void setItens(List<ItemEmprestavel> itens) {
        this.itens = itens;
    }

    /**
     * Obtém a data de início do empréstimo.
     * @return Data de início do empréstimo.
     */
    public LocalDate getDataInicio() {
        return dataInicio;
    }

    /**
     * Define a data de início do empréstimo.
     * @param dataInicio Nova data de início do empréstimo.
     */
    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * Obtém a data prevista de devolução dos itens.
     * @return Data prevista de devolução.
     */
    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    /**
     * Define a data prevista de devolução dos itens.
     * @param dataPrevistaDevolucao Nova data prevista de devolução.
     */
    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    /**
     * Obtém a data efetiva de devolução dos itens.
     * @return Data efetiva de devolução (ou null se ainda não devolvido).
     */
    public LocalDate getDataEfetivaDevolucao() {
        return dataEfetivaDevolucao;
    }

    /**
     * Define a data efetiva de devolução dos itens.
     * @param dataEfetivaDevolucao Nova data efetiva de devolução.
     */
    public void setDataEfetivaDevolucao(LocalDate dataEfetivaDevolucao) {
        this.dataEfetivaDevolucao = dataEfetivaDevolucao;
    }

    /**
     * Obtém a data do empréstimo (igual à data de início).
     * @return Data do empréstimo.
     */
    public LocalDate getDataEmprestimo() {
        return getDataInicio();
    }

    /**
     * Representação textual do empréstimo, incluindo o número, utente e itens emprestados.
     * @return String representando o empréstimo.
     */
    @Override
    public String toString() {
        String result = "Emprestimo [numero=" + numero +
                ", utente=" + utente.getNome() +
                ", itens=";

        for (ItemEmprestavel item : itens) {
            result += item.toString() + ", ";
        }

        if (!itens.isEmpty()) {
            result = result.substring(0, result.length() - 2);
        }

        result += ", dataInicio=" + dataInicio +
                ", dataPrevistaDevolucao=" + dataPrevistaDevolucao +
                ", dataEfetivaDevolucao=" + dataEfetivaDevolucao + "]";

        return result;
    }

    /**
     * Verifica se dois empréstimos são iguais com base no número do empréstimo.
     * @param o Objeto a ser comparado.
     * @return true se forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emprestimos that = (Emprestimos) o;
        return numero == that.numero;
    }

    /**
     * Gera o código hash para o empréstimo baseado no número.
     * @return Código hash do empréstimo.
     */
    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}

