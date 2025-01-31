package Model;

/**
 * Classe que representa um utente.
 * Contém informações como nome, NIF, gênero e contato do utente.
 */
public class Utentes {
    private String nome;
    private String nif;
    private Boolean genero;
    private String contacto;

    /**
     * Construtor da classe Utentes.
     *
     * @param nome     Nome do utente.
     * @param nif      Número de Identificação Fiscal (NIF) do utente.
     * @param genero   Gênero do utente (true para masculino, false para feminino, null para indefinido).
     * @param contacto Contato do utente.
     */
    public Utentes(String nome, String nif, Boolean genero, String contacto) {
        this.nome = nome;
        this.nif = nif;
        this.genero = genero;
        this.contacto = contacto;
    }

    /**
     * Obtém o nome do utente.
     *
     * @return Nome do utente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do utente.
     *
     * @param nome Novo nome do utente.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o NIF do utente.
     *
     * @return NIF do utente.
     */
    public String getNif() {
        return nif;
    }

    /**
     * Define o NIF do utente.
     *
     * @param nif Novo NIF do utente.
     */
    public void setNif(String nif) {
        this.nif = nif;
    }

    /**
     * Obtém o gênero do utente.
     *
     * @return Gênero do utente (true para masculino, false para feminino, null para indefinido).
     */
    public Boolean getGenero() {
        return genero;
    }

    /**
     * Define o gênero do utente.
     *
     * @param genero Novo gênero do utente.
     */
    public void setGenero(Boolean genero) {
        this.genero = genero;
    }

    /**
     * Obtém o contato do utente.
     *
     * @return Contato do utente.
     */
    public String getContacto() {
        return contacto;
    }

    /**
     * Define o contato do utente.
     *
     * @param contacto Novo contato do utente.
     */
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    /**
     * Retorna uma representação textual do utente.
     *
     * @return String contendo as informações do utente.
     */
    @Override
    public String toString() {
        String generoStr = (genero == null) ? "Indefinido" : (genero ? "M" : "F");
        return "Nome: " + nome + "\n" +
                "NIF: " + nif + "\n" +
                "Genero: " + generoStr + "\n" +
                "Contacto: " + contacto;
    }
}

