package Model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Reserva {
    private int numero;
    private Utentes utente;
    private List<Livro> livros;
    private List<Jornal> jornais;
    private LocalDate dataRegisto;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    // Construtor atualizado para receber LocalDate
    public Reserva(int numero, Utentes utente, List<Livro> livros, List<Jornal> jornais, LocalDate dataRegisto, LocalDate dataInicio, LocalDate dataFim) {
        this.numero = numero;
        this.utente = utente;
        this.livros = livros;
        this.jornais = jornais;
        this.dataRegisto = dataRegisto;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;

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

    public List<Jornal> getJornais() {
        return jornais;
    }

    public void setJornais(List<Jornal> jornais) {
        this.jornais = jornais;
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


    @Override
    public String toString() {
        return "Reserva [numero=" + numero +
                ", utente=" + utente.getNome() +
                ", livros=" + livros +
                ", jornais=" + jornais +
                ", dataRegisto=" + dataRegisto +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim + "]";
    }
}
