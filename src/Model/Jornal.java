package Model;

import java.time.LocalDate;
import java.util.Objects;

public class Jornal extends ItemEmprestavel {
    private String titulo;           // Título do jornal ou revista
    private String editora;          // Editora responsável
    private String categoria;        // Categoria do jornal ou revista
    private String issn;             // ISSN
    private LocalDate dataPublicacao;   // Data de publicação

    // Construtor
    public Jornal(String issn, String titulo, String categoria, String editora, LocalDate dataPublicacao) {
        super(issn);  // Chama o construtor da classe pai (ItemEmprestavel) com o ISSN
        this.titulo = titulo;
        this.categoria = categoria;
        this.editora = editora;
        this.dataPublicacao = dataPublicacao;
        this.issn = issn;
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
    public String getIdentificador() {
        return issn;  // Retorna o ISSN como identificador único
    }

    // Método toString adaptado para Jornal
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

    // Implementação de equals para comparar jornais com base no ISSN
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Jornal jornal = (Jornal) obj;
        return Objects.equals(issn, jornal.issn);  // Compara pelo ISSN
    }

    // Implementação de hashCode com base no ISSN
    @Override
    public int hashCode() {
        return Objects.hash(issn);  // Gera hash code com base no ISSN
    }
}
