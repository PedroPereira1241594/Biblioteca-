package View;

import Controller.UtenteController;
import Model.Emprestimos;
import Model.Reserva;
import Model.Utentes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UtenteView {

    public void exibirUtentes(ArrayList<Utentes> utentes) {
        if (utentes.isEmpty()) {
            System.out.println("Nenhum utente Registado.");
        } else {
            System.out.println("Lista de Utentes:");
            for (int i = 0; i < utentes.size(); i++) {
                Utentes utente = utentes.get(i);
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

    public static void gerirUtentes(UtenteController utenteController, List<Utentes> utentes, List<Reserva> reservas, List<Emprestimos> emprestimos, Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n=== Gestão de Utentes ===");
            System.out.println("1. Adicionar Utente");
            System.out.println("2. Listar Utentes");
            System.out.println("3. Editar Utente");
            System.out.println("4. Remover Utente");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    utenteController.adicionarUtente();
                    break;
                case 2:
                    utenteController.listarUtentesComReservasOuEmprestimos(utentes, reservas, emprestimos);
                    break;
                case 3:
                    utenteController.editarUtente();
                    break;
                case 4:
                    utenteController.removerUtente();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }
}
