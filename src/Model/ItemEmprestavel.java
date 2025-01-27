package Model;

import java.time.LocalDate;

public abstract class ItemEmprestavel {
    private String ISBN;
    private String ISSN;
    private String titulo;
    private String Autor;
    private String editora;
    private String categoria;
    private LocalDate dataPublicacao;
    private String identificador;

    // Construtor
    public ItemEmprestavel(String identificador) {
        this.identificador = identificador;
    }


        // Getters e Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getISSN() {
        return ISSN;
    }

    public void setISSN(String ISSN) {
        this.ISSN = ISSN;
    }

    public String getAutor() {
        return Autor;
    }

    public void setAutor(String autor) {
        Autor = autor;
    }

    // Método abstrato para ser implementado pelas subclasses
    public abstract String getIdentificador(); // Pode ser ISSN ou ISBN, dependendo do tipo de item
}
