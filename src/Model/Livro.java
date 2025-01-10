package Model;

public class Livro {
    private String nome;
    private String editora;
    private String categoria;
    private int ano;
    private String autor;

    public Livro(String nome, String editora, String categoria, int ano, String autor) {
        this.nome = nome;
        this.editora = editora;
        this.categoria = categoria;
        this.ano = ano;
        this.autor = autor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "Livro: " + nome + "\n" + "Editora: " + editora + "\n" + "Categoria: " + categoria + "\n" + "Ano: " + ano + "\n" + "Autor: " + autor;
    }
}



