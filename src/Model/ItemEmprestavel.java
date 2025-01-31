package Model;

import java.time.LocalDate;

/**
 * Classe abstrata que representa um item emprestável em uma biblioteca.
 * Pode ser um livro ou um jornal/revista.
 */
public abstract class ItemEmprestavel {
    private String ISBN;
    private String ISSN;
    private String titulo;
    private String Autor;
    private String editora;
    private String categoria;
    private LocalDate dataPublicacao;
    private String identificador;

    /**
     * Construtor da classe ItemEmprestavel.
     *
     * @param identificador Identificador único do item (ISBN ou ISSN).
     */
    public ItemEmprestavel(String identificador) {
        this.identificador = identificador;
    }

    /**
     * Obtém o título do item.
     *
     * @return O título do item.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Define o título do item.
     *
     * @param titulo O título a ser definido.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtém a editora do item.
     *
     * @return A editora do item.
     */
    public String getEditora() {
        return editora;
    }

    /**
     * Define a editora do item.
     *
     * @param editora A editora a ser definida.
     */
    public void setEditora(String editora) {
        this.editora = editora;
    }

    /**
     * Obtém a categoria do item.
     *
     * @return A categoria do item.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Define a categoria do item.
     *
     * @param categoria A categoria a ser definida.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Obtém a data de publicação do item.
     *
     * @return A data de publicação do item.
     */
    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    /**
     * Define a data de publicação do item.
     *
     * @param dataPublicacao A data de publicação a ser definida.
     */
    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    /**
     * Obtém o ISBN do item.
     *
     * @return O ISBN do item.
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Define o ISBN do item.
     *
     * @param ISBN O ISBN a ser definido.
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * Obtém o ISSN do item.
     *
     * @return O ISSN do item.
     */
    public String getISSN() {
        return ISSN;
    }

    /**
     * Define o ISSN do item.
     *
     * @param ISSN O ISSN a ser definido.
     */
    public void setISSN(String ISSN) {
        this.ISSN = ISSN;
    }

    /**
     * Obtém o autor do item.
     *
     * @return O autor do item.
     */
    public String getAutor() {
        return Autor;
    }

    /**
     * Define o autor do item.
     *
     * @param autor O autor a ser definido.
     */
    public void setAutor(String autor) {
        Autor = autor;
    }

    /**
     * Método abstrato para obter o identificador do item.
     *
     * @return O identificador do item (ISBN ou ISSN).
     */
    public abstract String getIdentificador(); //Pode ser ISSN ou ISBN, dependendo do item
}

