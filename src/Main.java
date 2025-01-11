import Controller.LivroController;
import Controller.UtenteController;
import Model.Livro;
import Model.Utentes;
import View.LivroView;
import View.UtenteView;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Listas para armazenar livros e utentes
        ArrayList<Livro> livros = new ArrayList<>();
        ArrayList<Utentes> utentes = new ArrayList<>();

        // Inicialização das views
        LivroView livroView = new LivroView();
        UtenteView utenteView = new UtenteView();

        // Inicialização dos controladores
        LivroController livroController = new LivroController(livros, livroView);
        UtenteController utenteController = new UtenteController(utentes, utenteView);

        // Scanner para interação no menu
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n=== Sistema de Gestão de Biblioteca ===");
            System.out.println("1. Gerir Livros");
            System.out.println("2. Gerir Utentes");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    gerirLivros(livroController, scanner);
                    break;
                case 2:
                    gerirUtentes(utenteController, scanner);
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);

        scanner.close();
    }

    private static void gerirLivros(LivroController livroController, Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n=== Gestão de Livros ===");
            System.out.println("1. Adicionar Livro");
            System.out.println("2. Listar Livros");
            System.out.println("3. Editar Livro");
            System.out.println("4. Remover Livro");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    livroController.adicionarLivro();
                    break;
                case 2:
                    livroController.listarLivros();
                    break;
                case 3:
                    livroController.editarLivro();
                    break;
                case 4:
                    livroController.removerLivro();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    private static void gerirUtentes(UtenteController utenteController, Scanner scanner) {
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
                    utenteController.listarUtentes();
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
