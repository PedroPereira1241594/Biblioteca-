package Model;

import java.time.LocalDate;

public class Jornal {
    private String titulo;           // Título do jornal ou revista
    private String editora;          // Editora responsável
    private String categoria;        // Categoria do jornal ou revista
    private String issn;             // ISSN
    private LocalDate dataPublicacao;   // Data de publicação

    // Construtor
    public Jornal(String issn, String titulo, String categoria, String editora, LocalDate dataPublicacao) {
        this.issn = issn;
        this.titulo = titulo;
        this.categoria = categoria;
        this.editora = editora;
        this.dataPublicacao = dataPublicacao;
    }

    // Getters
    public String getTitulo() {
        return titulo;
    }

    public String getEditora() {
        return editora;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getIssn() {
        return issn;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    // Setters
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    @Override
    public String toString() {
        return "Jornal {" +
                " ISSN='" + issn + '\'' +
                ", Título='" + titulo + '\'' +
                ", Categoria='" + categoria + '\'' +
                ", Editora='" + editora + '\'' +
                ", Data de Publicação='" + dataPublicacao + '\'' +
                '}';
    }
}

