package Model;

import java.util.List;

public class Emprestimos {
    private int numero;
    private Utentes utente;
    private List<Livro> livros;
    private String dataInicio;
    private String dataPrevistaDevolucao;
    private String dataEfetivaDevolucao;

    public Emprestimos(int numero, Utentes utente, List<Livro> livros, String dataInicio, String dataPrevistaDevolucao) {
        this.numero = numero;
        this.utente = utente;
        this.livros = livros;
        this.dataInicio = dataInicio;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
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

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(String dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public String getDataEfetivaDevolucao() {
        return dataEfetivaDevolucao;
    }

    public void setDataEfetivaDevolucao(String dataEfetivaDevolucao) {
        this.dataEfetivaDevolucao = dataEfetivaDevolucao;
    }

    @Override
    public String toString() {
        return "Emprestimo [numero=" + numero + ", utente=" + utente.getNome() + ", livros=" + livros
                + ", dataInicio=" + dataInicio + ", dataPrevistaDevolucao=" + dataPrevistaDevolucao
                + ", dataEfetivaDevolucao=" + dataEfetivaDevolucao + "]";
    }
}
