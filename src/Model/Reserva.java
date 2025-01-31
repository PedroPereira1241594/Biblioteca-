package Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Reserva {
    private int numero;
    private Utentes utente;
    private List<ItemEmprestavel> itens;
    private LocalDate dataRegisto;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Reserva(int numero, Utentes utente, List<ItemEmprestavel> itens, LocalDate dataRegisto, LocalDate dataInicio, LocalDate dataFim) {
        this.numero = numero;
        this.utente = utente;
        this.itens = itens;
        this.dataRegisto = dataRegisto;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
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

    public LocalDate getDataRegisto() {
        return dataRegisto;
    }

    public void setDataRegisto(LocalDate dataRegisto) {
        this.dataRegisto = dataRegisto;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public LocalDate getDataReserva() {
        return getDataRegisto();
    }

    @Override
    public String toString() {
        String result = "Reserva [numero=" + numero +
                ", utente=" + utente.getNome() +
                ", itens=";

        // Adiciona os itens reservados à string
        for (ItemEmprestavel item : itens) {
            result += item.toString() + ", ";
        }

        // Remove a última vírgula e espaço
        if (!itens.isEmpty()) {
            result = result.substring(0, result.length() - 2);
        }

        result += ", dataRegisto=" + dataRegisto +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim + "]";

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva that = (Reserva) o;
        return numero == that.numero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}
