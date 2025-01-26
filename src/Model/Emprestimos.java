package Model;

import java.time.LocalDate;
import java.util.List;

public class Emprestimos {
    private int numero;
    private Utentes utente;
    private List<Livro> livros;
    private List<Jornal> jornais;
    private LocalDate dataInicio;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataEfetivaDevolucao;

    // Construtor atualizado para receber LocalDate
    public Emprestimos(int numero, Utentes utente, List<Livro> livros, List<Jornal> jornais,LocalDate dataInicio, LocalDate dataPrevistaDevolucao, LocalDate dataEfetivaDevolucao) {
        this.numero = numero;
        this.utente = utente;
        this.livros = livros;
        this.jornais = jornais;
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

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    public List<Jornal> getJornais() {
        return jornais;
    }

    public void setJornais(List<Jornal> jornais) {
        this.jornais = jornais;
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

    public LocalDate getDataEmprestimo() {
        return getDataInicio();
    }

    @Override
    public String toString() {
        return "Emprestimo [numero=" + numero +
                ", utente=" + utente.getNome() +
                ", livros=" + livros +
                ", jornais=" + jornais +
                ", dataInicio=" + dataInicio +
                ", dataPrevistaDevolucao=" + dataPrevistaDevolucao +
                ", dataEfetivaDevolucao=" + dataEfetivaDevolucao + "]";
    }
}
