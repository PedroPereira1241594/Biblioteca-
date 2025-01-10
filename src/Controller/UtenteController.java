package Controller;

import Model.ModificarUtentes;
import Model.Utentes;
import View.UtenteView;

public class UtenteController {
    private final ModificarUtentes modificarUtentes;
    private final UtenteView utenteView;

    public UtenteController(ModificarUtentes modificarUtentes, UtenteView utenteView) {
        this.modificarUtentes = modificarUtentes;
        this.utenteView = utenteView;
    }

    public void adicionarUtente() {
        Utentes novoUtente = utenteView.capturarDadosUtente();
        modificarUtentes.adicionarUtente(novoUtente);
        utenteView.exibirMensagem("Utente adicionado com sucesso!");
    }

    public void listarUtentes() {
        for (Utentes utente : modificarUtentes.getUtentes()) {
            utenteView.exibirUtente(utente);
        }
    }

    public void editarUtente(int indice) {
        if (indice >= 0 && indice < modificarUtentes.getUtentes().size()) {
            Utentes novoUtente = utenteView.capturarDadosUtente();
            modificarUtentes.editarUtente(indice, novoUtente);
            utenteView.exibirMensagem("Utente editado com sucesso!");
        } else {
            utenteView.exibirMensagem("Índice inválido!");
        }
    }

    public void removerUtente(int indice) {
        if (indice >= 0 && indice < modificarUtentes.getUtentes().size()) {
            modificarUtentes.removerUtente(indice);
            utenteView.exibirMensagem("Utente removido com sucesso!");
        } else {
            utenteView.exibirMensagem("Índice inválido!");
        }
    }
}
