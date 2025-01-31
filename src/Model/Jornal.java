package Model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe que representa um Jornal.
 * Cada jornal é identificado pelo seu ISSN.
 */
public class Jornal extends ItemEmprestavel {
    private String titulo;
    private String editora;
    private String categoria;
    private String issn;
    private LocalDate dataPublicacao;

    /**
     * Construtor da classe Jornal.
     *
     * @param issn Identificador único do jornal.
     * @param titulo Título do jornal.
     * @param categoria Categoria do jornal.
     * @param editora Editora responsável pela publicação do jornal.
     * @param dataPublicacao Data de publicação do jornal.
     */
    public Jornal(String issn, String titulo, String categoria, String editora, LocalDate dataPublicacao) {
        super(issn);
        this.titulo = titulo;
        this.categoria = categoria;
        this.editora = editora;
        this.dataPublicacao = dataPublicacao;
        this.issn = issn;
    }

    /**
     * Obtém o título do jornal.
     *
     * @return O título do jornal.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Obtém a editora do jornal.
     *
     * @return A editora do jornal.
     */
    public String getEditora() {
        return editora;
    }

    /**
     * Obtém a categoria do jornal.
     *
     * @return A categoria do jornal.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Obtém o ISSN do jornal.
     *
     * @return O ISSN do jornal.
     */
    public String getIssn() {
        return issn;
    }

    /**
     * Obtém a data de publicação do jornal.
     *
     * @return A data de publicação do jornal.
     */
    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    /**
     * Define o título do jornal.
     *
     * @param titulo O título a ser definido.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Define a editora do jornal.
     *
     * @param editora A editora a ser definida.
     */
    public void setEditora(String editora) {
        this.editora = editora;
    }

    /**
     * Define a categoria do jornal.
     *
     * @param categoria A categoria a ser definida.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Define o ISSN do jornal.
     *
     * @param issn O ISSN a ser definido.
     */
    public void setIssn(String issn) {
        this.issn = issn;
    }

    /**
     * Define a data de publicação do jornal.
     *
     * @param dataPublicacao A data de publicação a ser definida.
     */
    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    /**
     * Obtém o identificador único do jornal, que é o ISSN.
     *
     * @return O ISSN do jornal.
     */
    @Override
    public String getIdentificador() {
        return issn;
    }

    /**
     * Retorna uma representação em String do objeto Jornal.
     *
     * @return Uma string formatada com as informações do jornal.
     */
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

    /**
     * Compara este jornal com outro objeto para verificar se são iguais.
     *
     * @param obj O objeto a ser comparado.
     * @return true se os objetos forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Jornal jornal = (Jornal) obj;
        return Objects.equals(issn, jornal.issn);
    }

    /**
     * Retorna o código hash do jornal baseado no ISSN.
     *
     * @return O código hash do jornal.
     */
    @Override
    public int hashCode() {
        return Objects.hash(issn);
    }
}

