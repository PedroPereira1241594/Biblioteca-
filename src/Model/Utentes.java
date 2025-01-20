package Model;

public class Utentes {
    private String nome;
    private String nif;
    private Boolean genero;
    private String contacto;

    public Utentes(String nome, String nif, Boolean genero, String contacto) {
        this.nome = nome;
        this.nif = nif;
        this.genero = genero;
        this.contacto = contacto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Boolean getGenero() {
        return genero;
    }

    public void setGenero(Boolean genero) {
        this.genero = genero;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    @Override
    public String toString() {
        String generoStr = (genero == null) ? "Indefinido" : (genero ? "M" : "F");
        return "Nome: " + nome + "\n" + "NIF: " + nif + "\n" + "Genero: " + generoStr + "\n" + "Contacto: " + contacto;
    }
}
