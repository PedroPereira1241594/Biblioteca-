package Utente;

import java.util.ArrayList;
import java.util.Scanner;

public class ModificarUtentes {

    private ArrayList<Utentes> utente;

    public ModificarUtentes() {
        this.utente = new ArrayList<>();
    }

    Scanner scanner = new Scanner(System.in);

    //Adicionar um Utente
    public void adicionarUtente(Utentes utentes) {
        utente.add(utentes);
        System.out.println("Utente adicionado com sucesso!");
    }

    //Mostrar utentes
    public void mostrarUtentes() {
        if (utente.isEmpty()) {
            System.out.println("Não há utentes para mostrar.");
        } else {
            for (Utentes utente2 : utente) {
                System.out.println(utente2);
            }
        }
    }

    //Atualizar Utentes
    public void atualizarUtente(Scanner scanner) {
        mostrarUtentes();
        if (utente.isEmpty()) {
            System.out.println("Não há utentes para mostrar.");
            return;
        }

        System.out.print("Digite o NIF do utente para alterar os dados: ");
        int nifEncontrado = scanner.nextInt();
        scanner.nextLine();

        Utentes procurarUtente = null;
        for (Utentes X : utente) {
            if (X.getNIF() == nifEncontrado) {
                procurarUtente = X;
                break;
            }
        }
        if (procurarUtente == null) {
            System.out.println("Número inválido. Operação cancelada.");
            return;
        }

        // Novos dados do utente
        System.out.print("Novo Nome: ");
        String novoNome = scanner.nextLine();
        System.out.print("Novo NIF: ");
        int novoNIF = scanner.nextInt();
        System.out.print("Genero: M/F");
        Boolean novoGenero = scanner.nextBoolean();
        System.out.print("Novo Contacto: ");
        int novoContacto = scanner.nextInt();
        scanner.nextLine();

        // Atualizar o Utente
        procurarUtente.setNome(novoNome);
        procurarUtente.setNIF(novoNIF);
        procurarUtente.setGenero(novoGenero);
        procurarUtente.setContacto(novoContacto);
        System.out.println("Utente alterado com sucesso!");
    }

    // Remover Utentes
    public void removerUtente(Scanner scanner) {
        mostrarUtentes();
        if (utente.isEmpty()) {
            System.out.println("Nenhum Utente para remover.");
            return;
        }

        System.out.print("Digite o NIF do Utente a remover: ");
        int nifProcurado = scanner.nextInt() - 1;
        scanner.nextLine();

        Utentes utenteEncontrado = null;
        for (Utentes X : utente) {
            if (X.getNIF() == nifProcurado) {
                utenteEncontrado = X;
            }
        }

        if (utenteEncontrado == null) {
            System.out.println("NIF não encontrado");
            return;
        }

        utente.remove(utenteEncontrado);
        System.out.println("Utente removido com sucesso!");
    }
}


