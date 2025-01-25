package Model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Reserva {
    private int numero;
    private Utentes utente;
    private List<Livro> livros;
    private LocalDate dataRegisto;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    // Lista estática para armazenar todas as reservas
    private static List<Reserva> listaReservas = new ArrayList<>();

    // Construtor atualizado para receber LocalDate
    public Reserva(int numero, Utentes utente, List<Livro> livros, LocalDate dataRegisto, LocalDate dataInicio, LocalDate dataFim) {
        this.numero = numero;
        this.utente = utente;
        this.livros = livros;
        this.dataRegisto = dataRegisto;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;

        // Adiciona a reserva à lista estática ao criar uma nova reserva
        listaReservas.add(this);
    }

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

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
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

    // Método estático para acessar a lista de reservas
    public static List<Reserva> getListaReservas() {
        return listaReservas;
    }

    @Override
    public String toString() {
        return "Emprestimo [numero=" + numero +
                ", utente=" + utente.getNome() +
                ", livros=" + livros +
                ", dataRegisto=" + dataRegisto +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim + "]";
    }
}
