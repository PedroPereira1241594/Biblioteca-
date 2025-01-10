package Model;

public class Utentes {
    private String nome;
    private int nif;
    private Boolean genero;
    private int contacto;

    public Utentes(String nome, int nif, Boolean genero, int contacto) {
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

    public int getNif() {
        return nif;
    }

    public void setNif(int nif) {
        this.nif = nif;
    }

    public Boolean getGenero() {
        return genero;
    }

    public void setGenero(Boolean genero) {
        this.genero = genero;
    }

    public int getContacto() {
        return contacto;
    }

    public void setContacto(int contacto) {
        this.contacto = contacto;
    }

    @Override
    public String toString() {
        return "Nome: " + nome + "\n" + "NIF: " + nif + "\n" + "Genero: " + (genero ? "Masculino" : "Feminino") + "\n" + "Contacto: " + contacto;
    }
}
