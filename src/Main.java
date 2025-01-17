import Controller.EmprestimosController;
import Controller.LivroController;
import Controller.UtenteController;
import Model.Jornal;
import Model.Livro;
import Model.Utentes;
import View.EmprestimosView;
import View.JornalView;
import View.LivroView;
import View.UtenteView;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

import static Controller.ExportarDados.*;
import static View.LivroView.gerirLivros;
import static View.UtenteView.gerirUtentes;

public class Main {
    public static void main(String[] args) throws IOException {
        // Listas para armazenar livros e utentes
        ArrayList<Livro> livros = new ArrayList<>();
        ArrayList<Utentes> utentes = new ArrayList<>();
        ArrayList<Jornal> jornals = new ArrayList<>();

        // Inicialização das views
        LivroView livroView = new LivroView();
        UtenteView utenteView = new UtenteView();
        JornalView jornalView = new JornalView();

        // Inicialização do controlador de utentes
        UtenteController utenteController = new UtenteController(utentes, utenteView);

        // Inicialização do controlador de empréstimos (temporariamente sem LivroController)
        EmprestimosController emprestimosController = new EmprestimosController(null);

        // Inicialização do controlador de livros com a dependência de EmprestimosController
        LivroController livroController = new LivroController(livros, livroView, emprestimosController);

        // Atualização do EmprestimosController para usar o LivroController
        emprestimosController.setLivroController(livroController);

        // Inicialização da view de empréstimos
        EmprestimosView emprestimosView = new EmprestimosView(emprestimosController, utenteController, livroController);

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
                case 4:
                    emprestimosView.exibirMenu();  // Chamando a exibição do menu de empréstimos
                    break;
                case 0:
                    System.out.print("Tem certeza de que deseja sair? (S/N): ");
                    char confirmacao = scanner.next().toUpperCase().charAt(0);
                    if (confirmacao == 'S') {
                        // Definir os caminhos dos arquivos de exportação
                        String caminhoLivros = "C:\\Users\\Acer\\Desktop\\APOO\\Projecto Final 1\\Biblioteca-\\Biblioteca-\\src\\DadosExportados\\livros.txt";
                        String caminhoUtentes = "C:\\Users\\Acer\\Desktop\\APOO\\Projecto Final 1\\Biblioteca-\\Biblioteca-\\src\\DadosExportados\\utentes.txt";
                        String caminhoJornal = "C:\\Users\\Acer\\Desktop\\APOO\\Projecto Final 1\\Biblioteca-\\Biblioteca-\\src\\DadosExportados\\jornal.txt";
                        // Chamar os métodos para exportar livros e utentes
                        exportarLivros(caminhoLivros, livros);  // Exporta livros
                        exportarUtentes(caminhoUtentes, utentes);  // Exporta utentes
                        exportarJornal(caminhoJornal, jornals);
                        System.out.println("Saindo do sistema...");
                    } else {
                        opcao = -1; // Continua o loop
                    }
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);

        scanner.close();
    }
}
