package Utente;

public class Utentes {
    private String Nome;
    private int NIF;
    private Boolean Genero;
    private int Contacto;

    public Utentes(String Nome, int NIF, Boolean Genero, int Contacto) {
        this.Nome = Nome;
        this.NIF = NIF;
        this.Genero = Genero;
        this.Contacto = Contacto;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public int getNIF() {
        return NIF;
    }

    public void setNIF(int NIF) {
        this.NIF = NIF;
    }

    public Boolean getGenero() {
        return Genero;
    }

    public void setGenero(Boolean genero) {
        Genero = genero;
    }

    public int getContacto() {
        return Contacto;
    }

    public void setContacto(int contacto) {
        Contacto = contacto;
    }

    @Override
    public String toString() {
        return "Nome: " + Nome + "\n" + "NIF: " + NIF + "\n" + "Genero: " + Genero + "\n" + "Contacto: " + Contacto;
    }


}
