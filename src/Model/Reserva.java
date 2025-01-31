package Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Classe que representa uma reserva feita por um utente.
 * Cada reserva possui um número identificador, um utente associado, uma lista de itens reservados,
 * e datas que indicam o registro, início e fim da reserva.
 */
public class Reserva {
    private int numero;
    private Utentes utente;
    private List<ItemEmprestavel> itens;
    private LocalDate dataRegisto;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    /**
     * Construtor da classe Reserva.
     *
     * @param numero       Número identificador da reserva.
     * @param utente       Utente que realizou a reserva.
     * @param itens        Lista de itens reservados.
     * @param dataRegisto  Data de registro da reserva.
     * @param dataInicio   Data de início da reserva.
     * @param dataFim      Data de término da reserva.
     */
    public Reserva(int numero, Utentes utente, List<ItemEmprestavel> itens, LocalDate dataRegisto, LocalDate dataInicio, LocalDate dataFim) {
        this.numero = numero;
        this.utente = utente;
        this.itens = itens;
        this.dataRegisto = dataRegisto;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    /**
     * Obtém o número da reserva.
     *
     * @return Número da reserva.
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Define o número da reserva.
     *
     * @param numero Número da reserva.
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * Obtém o utente que realizou a reserva.
     *
     * @return Utente da reserva.
     */
    public Utentes getUtente() {
        return utente;
    }

    /**
     * Define o utente da reserva.
     *
     * @param utente Novo utente da reserva.
     */
    public void setUtente(Utentes utente) {
        this.utente = utente;
    }

    /**
     * Obtém a lista de itens reservados.
     *
     * @return Lista de itens reservados.
     */
    public List<ItemEmprestavel> getItens() {
        return itens;
    }

    /**
     * Define a lista de itens reservados.
     *
     * @param itens Nova lista de itens reservados.
     */
    public void setItens(List<ItemEmprestavel> itens) {
        this.itens = itens;
    }

    /**
     * Obtém a data de registro da reserva.
     *
     * @return Data de registro.
     */
    public LocalDate getDataRegisto() {
        return dataRegisto;
    }

    /**
     * Define a data de registro da reserva.
     *
     * @param dataRegisto Nova data de registro.
     */
    public void setDataRegisto(LocalDate dataRegisto) {
        this.dataRegisto = dataRegisto;
    }

    /**
     * Obtém a data de início da reserva.
     *
     * @return Data de início.
     */
    public LocalDate getDataInicio() {
        return dataInicio;
    }

    /**
     * Define a data de início da reserva.
     *
     * @param dataInicio Nova data de início.
     */
    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * Obtém a data de término da reserva.
     *
     * @return Data de término.
     */
    public LocalDate getDataFim() {
        return dataFim;
    }

    /**
     * Define a data de término da reserva.
     *
     * @param dataFim Nova data de término.
     */
    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    /**
     * Obtém a data de registro da reserva.
     *
     * @return Data de registro.
     */
    public LocalDate getDataReserva() {
        return getDataRegisto();
    }

    /**
     * Retorna uma representação textual da reserva.
     *
     * @return String contendo os detalhes da reserva.
     */
    @Override
    public String toString() {
        String result = "Reserva [numero=" + numero +
                ", utente=" + utente.getNome() +
                ", itens=";

        for (ItemEmprestavel item : itens) {
            result += item.toString() + ", ";
        }

        if (!itens.isEmpty()) {
            result = result.substring(0, result.length() - 2);
        }

        result += ", dataRegisto=" + dataRegisto +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim + "]";

        return result;
    }

    /**
     * Compara duas reservas com base no número identificador.
     *
     * @param o Objeto a ser comparado.
     * @return true se os números forem iguais, caso contrário, false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva that = (Reserva) o;
        return numero == that.numero;
    }

    /**
     * Retorna o código hash da reserva baseado no número identificador.
     *
     * @return Código hash da reserva.
     */
    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}
