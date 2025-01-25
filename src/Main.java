import Controller.*;
import Model.*;
import View.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

import static Controller.ExportarDados.*;
import static Controller.ExportarDados.exportarReservas;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in); // Cria o Scanner.
        String caminhoConfig = "src\\DadosExportados\\Config.txt";

        // Inicializar o leitor de configurações
        Configurações configReader = new Configurações(caminhoConfig);

        // Obter os caminhos do arquivo de configuração
        String caminhoLivros = configReader.getCaminhoLivros();
        String caminhoUtentes = configReader.getCaminhoUtentes();
        String caminhoJornal = configReader.getCaminhoJornal();
        String caminhoEmprestimo = configReader.getCaminhoEmprestimo();
        String caminhoReserva = configReader.getCaminhoReserva();

        // Dados compartilhados
        ArrayList<Livro> livros = new ArrayList<>(ImportarDados.carregarLivros(caminhoLivros));
        ArrayList<Utentes> utentes = new ArrayList<>(ImportarDados.carregarUtentes(caminhoUtentes));
        ArrayList<Jornal> jornals = new ArrayList<>(ImportarDados.carregarJornais(caminhoJornal));
        ArrayList<Emprestimos> emprestimos = new ArrayList<>(ImportarDados.carregarEmprestimos(caminhoEmprestimo, utentes, livros));
        ArrayList<Reserva> reservas = new ArrayList<>(ImportarDados.carregarReservas(caminhoReserva, utentes, livros));

        // Inicialização das views e controladores
        UtenteView utenteView = new UtenteView();
        utenteView.setScanner(new Scanner(System.in));    // Configura o scanner
        UtenteController utenteController = new UtenteController(utentes, utenteView, reservas, emprestimos);
        utenteView.setUtenteController(utenteController); // Configura o controller
        JornalController jornalController = new JornalController(jornals);
        JornalView jornalView = new JornalView(jornalController);
        ReservaController reservaController = new ReservaController(null, reservas);
        EmprestimosController emprestimosController = new EmprestimosController(reservaController, emprestimos);
        reservaController.setEmprestimosController(emprestimosController);
        LivroController livroController = new LivroController(livros, null, emprestimosController, reservas);

        // Agora configura o livroView antes de usá-lo
        LivroView livroView = new LivroView(livroController, scanner);
        livroController.setLivroView(livroView); // Configura o livroView no livroController

        emprestimosController.setLivroController(livroController);
        ReservaView reservaView = new ReservaView(reservaController, utenteController, livroController, emprestimosController);
        EmprestimosView emprestimosView = new EmprestimosView(emprestimosController, utenteController, livroController);
        PesquisaEstatisticasController pesquisaEstatisticasController = new PesquisaEstatisticasController(livros, jornals, emprestimos, reservas);
        PesquisaEstatisticasView pesquisaEstatisticasView = new PesquisaEstatisticasView(scanner, pesquisaEstatisticasController);

        // Carregar Ficheiros
        ImportarDados importarDados = new ImportarDados();

        int opcao;
        int escolha;

        do {
            System.out.println("\n=== Sistema de Gestão de Biblioteca ===");
            System.out.println("1. Gerir Livros");
            System.out.println("2. Gerir Jornais/Revistas");
            System.out.println("3. Gerir Utentes");
            System.out.println("4. Gerir Empréstimos");
            System.out.println("5. Gerir Reservas");
            System.out.println("6. Pesquisas/Estatísticas");
            System.out.println("7. Ler/Guardar dados");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    livroView.gerirLivros();  // Chamando o método de gerir livros diretamente no livroView
                    break;
                case 2:
                    jornalView.exibirMenu();  // Já está correto
                    break;
                case 3:
                    utenteView.gerirUtentes(utentes, reservas, emprestimos);  // Já está correto
                    break;
                case 4:
                    emprestimosView.exibirMenu();  // Já está correto
                    break;
                case 5:
                    reservaView.exibirMenu();  // Já está correto
                    break;
                case 6:
                    pesquisaEstatisticasView.exibirMenu();  // Já está correto
                    break;
                case 7:
                    System.out.println("1. Ler dados");
                    System.out.println("2. Carregar dados");
                    System.out.println("0. Voltar");
                    escolha = scanner.nextInt();
                    if (escolha == '1' ){
                        System.out.println("\nCarregando dados...");
                    } else if ( escolha == '2') {
                        System.out.println("\nGuardado dados...");
                        exportarLivros(caminhoLivros, livros);
                        exportarUtentes(caminhoUtentes, utentes);
                        exportarJornal(caminhoJornal, jornals);
                        exportarEmprestimos(caminhoEmprestimo, emprestimos);
                        exportarReservas(caminhoReserva, reservas);
                    } else if ( escolha == '0') {


                    }

                case 0:
                    System.out.print("Tem certeza de que deseja sair? (S/N): ");
                    char confirmacao = scanner.next().toUpperCase().charAt(0);
                    if (confirmacao == 'S') {
                        System.out.println("\nSaindo do sistema...");
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

