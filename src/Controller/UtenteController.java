package Controller;

import Model.Utentes;
import View.UtenteView;

import java.util.ArrayList;
import java.util.Scanner;

public class UtenteController {
    private final ArrayList<Utentes> utentes;
    private final UtenteView utenteView;

    public UtenteController(ArrayList<Utentes> utentes, UtenteView utenteView) {
        this.utentes = utentes;
        this.utenteView = utenteView;
    }

    public void adicionarUtente() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do utente: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o NIF: ");
        int nif = scanner.nextInt();
        System.out.print("Digite o género (1 para Masculino, 0 para Feminino): ");
        boolean genero = scanner.nextInt() == 1;
        System.out.print("Digite o contacto: ");
        int contacto = scanner.nextInt();

        Utentes utente = new Utentes(nome, nif, genero, contacto);
        utentes.add(utente);
        System.out.println("Utente adicionado com sucesso!");
    }

    public void listarUtentes() {
        utenteView.exibirUtentes(utentes);
    }

    public void editarUtente(int indice) {
        if (indice >= 0 && indice < utentes.size()) {
            Scanner scanner = new Scanner(System.in);
            Utentes utente = utentes.get(indice);

            System.out.println("Editando o utente: " + utente.getNome());
            System.out.print("Digite o novo nome (ou pressione Enter para manter): ");
            String nome = scanner.nextLine();
            if (!nome.isEmpty()) utente.setNome(nome);

            System.out.print("Digite o novo NIF (ou pressione Enter para manter): ");
            String nifStr = scanner.nextLine();
            if (!nifStr.isEmpty()) utente.setNif(Integer.parseInt(nifStr));

            System.out.print("Digite o novo género (1 para Masculino, 0 para Feminino ou pressione Enter para manter): ");
            String generoStr = scanner.nextLine();
            if (!generoStr.isEmpty()) utente.setGenero(Integer.parseInt(generoStr) == 1);

            System.out.print("Digite o novo contacto (ou pressione Enter para manter): ");
            String contactoStr = scanner.nextLine();
            if (!contactoStr.isEmpty()) utente.setContacto(Integer.parseInt(contactoStr));

            System.out.println("Utente editado com sucesso!");
        } else {
            System.out.println("Índice inválido!");
        }
    }

    public void removerUtente(int indice) {
        if (indice >= 0 && indice < utentes.size()) {
            utentes.remove(indice);
            System.out.println("Utente removido com sucesso!");
        } else {
            System.out.println("Índice inválido!");
        }
    }
}
