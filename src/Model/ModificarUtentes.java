package Model;

import java.util.ArrayList;

public class ModificarUtentes {
    private final ArrayList<Utentes> utentes;

    public ModificarUtentes() {
        this.utentes = new ArrayList<>();
    }

    public void adicionarUtente(Utentes utente) {
        utentes.add(utente);
    }

    public ArrayList<Utentes> getUtentes() {
        return utentes;
    }

    public void editarUtente(int indice, Utentes novoUtente) {
        if (indice >= 0 && indice < utentes.size()) {
            utentes.set(indice, novoUtente);
        }
    }

    public void removerUtente(int indice) {
        if (indice >= 0 && indice < utentes.size()) {
            utentes.remove(indice);
        }
    }
}
