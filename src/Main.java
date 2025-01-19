import Controller.*;
import Model.Emprestimos;
import Model.Jornal;
import Model.Livro;
import Model.Utentes;
import View.EmprestimosView;
import View.JornalView;
import View.LivroView;
import View.ReservaView;
import View.UtenteView;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

import static Controller.ExportarDados.*;
import static View.LivroView.gerirLivros;
import static View.UtenteView.gerirUtentes;

public class Main {
    public static void main(String[] args) throws IOException {

        String caminhoConfig = "src\\DadosExportados\\Config.txt";

        // Inicializar o leitor de configurações
        Configurações configReader = new Configurações(caminhoConfig);

        // Obter os caminhos do arquivo de configuração
        String caminhoLivros = configReader.getCaminhoLivros();
        String caminhoUtentes = configReader.getCaminhoUtentes();
        String caminhoJornal = configReader.getCaminhoJornal();
        String caminhoEmprestimo = configReader.getCaminhoEmprestimo();

        ArrayList<Livro> livros = new ArrayList<>();
        ArrayList<Utentes> utentes = new ArrayList<>();
        ArrayList<Jornal> jornals = new ArrayList<>();
        ArrayList<Emprestimos> emprestimos1 = new ArrayList<>();

        // Inicialização das views
        LivroView livroView = new LivroView();
        UtenteView utenteView = new UtenteView();
        JornalController jornalController = new JornalController(jornals);
        JornalView jornalView = new JornalView(jornalController);

        // Inicialização do controlador de utentes
        UtenteController utenteController = new UtenteController(utentes, utenteView);

        // Inicialização do controlador de empréstimos (temporariamente sem LivroController)
        EmprestimosController emprestimosController = new EmprestimosController(null);

        // Inicialização do controlador de livros com a dependência de EmprestimosController
        LivroController livroController = new LivroController(livros, livroView, emprestimosController);

        // Atualização do EmprestimosController para usar o LivroController
        emprestimosController.setLivroController(livroController);

        // Inicialização do controlador de reservas
        ReservaController reservaController = new ReservaController();

        // Inicialização das views de empréstimos e reservas
        EmprestimosView emprestimosView = new EmprestimosView(emprestimosController, utenteController, livroController);
        ReservaView reservaView = new ReservaView(reservaController, utenteController, livroController, emprestimosController);

        // Scanner para interação no menu
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n=== Sistema de Gestão de Biblioteca ===");
            System.out.println("1. Gerir Livros");
            System.out.println("2. Gerir Jornais/Revistas");
            System.out.println("3. Gerir Utentes");
            System.out.println("4. Gerir Empréstimos");
            System.out.println("5. Gerir Reservas");
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
                    emprestimosView.exibirMenu();
                    break;
                case 5:
                    reservaView.exibirMenu();
                    break;
                case 0:
                    System.out.print("Tem certeza de que deseja sair? (S/N): ");
                    System.out.println("Emprestimos: " + emprestimos1);
                    char confirmacao = scanner.next().toUpperCase().charAt(0);
                    if (confirmacao == 'S') {
                        // Chamar os métodos para exportar dados
                        exportarLivros(caminhoLivros, livros);  // Exporta livros
                        exportarUtentes(caminhoUtentes, utentes);  // Exporta utentes
                        exportarJornal(caminhoJornal, jornals);
                        exportarEmprestimos(caminhoEmprestimo, emprestimos1);
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
