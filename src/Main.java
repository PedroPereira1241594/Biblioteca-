import Controller.EmprestimosController;
import Controller.LivroController;
import Controller.UtenteController;
import Model.Livro;
import Model.Utentes;
import View.EmprestimosView;
import View.JornalView;
import View.LivroView;
import View.UtenteView;

import java.util.ArrayList;
import java.util.Scanner;

import static View.LivroView.gerirLivros;
import static View.UtenteView.gerirUtentes;

public class Main {
    public static void main(String[] args) {
        // Listas para armazenar livros e utentes
        ArrayList<Livro> livros = new ArrayList<>();
        ArrayList<Utentes> utentes = new ArrayList<>();

        // Inicialização das views
        LivroView livroView = new LivroView();
        UtenteView utenteView = new UtenteView();
        EmprestimosView emprestimosView = new EmprestimosView();
        JornalView jornalView = new JornalView();

        // Inicialização dos controladores
        LivroController livroController = new LivroController(livros, livroView);
        UtenteController utenteController = new UtenteController(utentes, utenteView);

        // Scanner para interação no menu
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n=== Sistema de Gestão de Biblioteca ===");
            System.out.println("1. Gerir Livros");
            System.out.println("2. Gerir Jornais/Revistas");
            System.out.println("3. Gerir Utentes");
            System.out.println("4. Gerir Empréstimos");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    gerirLivros(livroController, scanner);
                    break;
                case 2:
                    jornalView.exibirMenu();
                    break;
                case 3:
                    gerirUtentes(utenteController, scanner);
                    break;
                //case 4:
                    //emprestimosView.exibirMenu();
                   // break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);

        scanner.close();
    }

}
