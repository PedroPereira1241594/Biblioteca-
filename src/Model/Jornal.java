package Model;

import java.time.LocalDate;

public class Jornal {
    private String titulo;           // Título do jornal ou revista
    private String editora;          // Editora responsável
    private String categoria;        // Categoria do jornal ou revista
    private String issn;             // ISSN
    private LocalDate dataPublicacao;   // Data de publicação

    // Construtor
    public Jornal(String titulo, String editora, String categoria, String issn, LocalDate dataPublicacao) {
        this.titulo = titulo;
        this.editora = editora;
        this.categoria = categoria;
        this.issn = issn;
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
                "Título='" + titulo + '\'' +
                ", Editora='" + editora + '\'' +
                ", Categoria='" + categoria + '\'' +
                ", ISSN='" + issn + '\'' +
                ", Data de Publicação='" + dataPublicacao + '\'' +
                '}';
    }
}

