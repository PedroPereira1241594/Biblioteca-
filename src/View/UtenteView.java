package View;

import Model.Utentes;
import java.util.ArrayList;

public class UtenteView {

    public void exibirUtentes(ArrayList<Utentes> utentes) {
        if (utentes.isEmpty()) {
            System.out.println("Nenhum utente cadastrado.");
        } else {
            System.out.println("Lista de Utentes:");
            for (int i = 0; i < utentes.size(); i++) {
                Utentes utente = utentes.get(i);
                System.out.println("Índice: " + i);
                System.out.println("Nome: " + utente.getNome());
                System.out.println("NIF: " + utente.getNif());
                System.out.println("Gênero: " + (utente.getGenero() ? "Masculino" : "Feminino"));
                System.out.println("Contacto: " + utente.getContacto());
                System.out.println("----------------------");
            }
        }
    }

    public void exibirUtenteDetalhado(Utentes utente) {
        System.out.println("Detalhes do Utente:");
        System.out.println("Nome: " + utente.getNome());
        System.out.println("NIF: " + utente.getNif());
        System.out.println("Gênero: " + (utente.getGenero() ? "Masculino" : "Feminino"));
        System.out.println("Contacto: " + utente.getContacto());
        System.out.println("----------------------");
    }

    public void mensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
