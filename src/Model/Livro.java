package Model;

import java.util.Objects;

/**
 * Classe que representa um livro.
 * Herda de ItemEmprestavel e possui atributos como nome, editora, categoria, ano, autor e ISBN.
 */
public class Livro extends ItemEmprestavel {
    private String nome;
    private String editora;
    private String categoria;
    private int ano;
    private String autor;
    private String isbn;

    /**
     * Construtor da classe Livro.
     *
     * @param nome      Nome do livro.
     * @param editora   Nome da editora.
     * @param categoria Categoria do livro.
     * @param ano       Ano de publicação.
     * @param autor     Nome do autor.
     * @param isbn      Código ISBN do livro.
     */
    public Livro(String nome, String editora, String categoria, int ano, String autor, String isbn) {
        super(isbn);
        this.nome = nome;
        this.editora = editora;
        this.categoria = categoria;
        this.ano = ano;
        this.autor = autor;
        this.isbn = isbn;
    }

    /**
     * Obtém o nome do livro.
     *
     * @return Nome do livro.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do livro.
     *
     * @param nome Nome do livro.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém a editora do livro.
     *
     * @return Nome da editora.
     */
    public String getEditora() {
        return editora;
    }

    /**
     * Define a editora do livro.
     *
     * @param editora Nome da editora.
     */
    public void setEditora(String editora) {
        this.editora = editora;
    }

    /**
     * Obtém a categoria do livro.
     *
     * @return Categoria do livro.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Define a categoria do livro.
     *
     * @param categoria Categoria do livro.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Obtém o ano de publicação do livro.
     *
     * @return Ano de publicação.
     */
    public int getAno() {
        return ano;
    }

    /**
     * Define o ano de publicação do livro.
     *
     * @param ano Ano de publicação.
     */
    public void setAno(int ano) {
        this.ano = ano;
    }

    /**
     * Obtém o nome do autor do livro.
     *
     * @return Nome do autor.
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Define o nome do autor do livro.
     *
     * @param autor Nome do autor.
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Obtém o código ISBN do livro.
     *
     * @return Código ISBN.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Define o código ISBN do livro.
     *
     * @param isbn Código ISBN.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Retorna uma representação textual do livro.
     *
     * @return String com os detalhes do livro.
     */
    @Override
    public String toString() {
        return "Livro: " + nome + "\n" +
                "Editora: " + editora + "\n" +
                "Categoria: " + categoria + "\n" +
                "Ano: " + ano + "\n" +
                "Autor: " + autor + "\n" +
                "ISBN: " + isbn;
    }

    /**
     * Compara dois livros com base no ISBN.
     *
     * @param obj Objeto a ser comparado.
     * @return true se os ISBNs forem iguais, caso contrário, false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Livro livro = (Livro) obj;
        return Objects.equals(isbn, livro.isbn);
    }

    /**
     * Retorna o código hash do livro, baseado no ISBN.
     *
     * @return Código hash do livro.
     */
    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    /**
     * Retorna o identificador único do livro (ISBN).
     *
     * @return Código ISBN do livro.
     */
    @Override
    public String getIdentificador() {
        return isbn;
    }
}