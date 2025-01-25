package View;

import Controller.LivroController;
import Controller.UtenteController;
import Model.Emprestimos;
import Model.Reserva;
import Model.Utentes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UtenteView {
    private UtenteController utenteController;
    private Scanner scanner;

    // Método para configurar o UtenteController
    public void setUtenteController(UtenteController utenteController) {
        this.utenteController = utenteController;
    }

    // Método para configurar o Scanner
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void gerirUtentes(List<Utentes> utentes, List<Reserva> reservas, List<Emprestimos> emprestimos) {
        int opcao;

        do {
            System.out.println("\n=== Gestão de Utentes ===");
            System.out.println("1. Adicionar Utente");
            System.out.println("2. Consultar Utente");
            System.out.println("3. Editar Utente");
            System.out.println("4. Remover Utente");
            System.out.println("5. Listar Utentes");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");

            // Usa nextLine para evitar problemas de buffer
            while (!scanner.hasNextInt()) {
                System.out.println("Por favor, insira um número válido.");
                scanner.next();
            }
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            switch (opcao) {
                case 1:
                    utenteController.adicionarUtente();
                    break;
                case 2:
                    consultarUtente();
                    break;
                case 3:
                    utenteController.editarUtente();
                    break;
                case 4:
                    utenteController.removerUtente();
                    break;
                case 5:
                    utenteController.listarUtentesComReservasOuEmprestimos(utentes, reservas, emprestimos);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    public void exibirUtenteDetalhado(Utentes utente) {
        System.out.println("=".repeat(24));
        System.out.println("Detalhes do Utente:");
        System.out.println("Nome: " + utente.getNome());
        System.out.println("NIF: " + utente.getNif());
        System.out.println("Género: " + (utente.getGenero() ? "Masculino" : "Feminino"));
        System.out.println("Contacto: " + utente.getContacto());
        System.out.println("=".repeat(24));
    }

    private void consultarUtente() {
        System.out.println("\n=== Consultar Utente ===");
        System.out.print("NIF do Utente: ");
        String nif = scanner.nextLine();

        // Chama o método consultarUtente do controller
        Utentes utente = utenteController.consultarUtente(nif);

        // Se o utente for encontrado, exibe os detalhes
        if (utente != null) {
            exibirUtenteDetalhado(utente);
        } else {
            System.out.println("O Utente com o NIF " + nif + " não foi encontrado.");
        }
    }
}
