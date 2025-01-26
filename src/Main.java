import Controller.*;
import Model.*;
import View.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in); // Um único Scanner para todo o programa
        String caminhoConfig = "src\\DadosExportados\\Config.txt";

        // Inicializar o leitor de configurações
        Configurações configReader = new Configurações(caminhoConfig);

        // Dados compartilhados
        ArrayList<Livro> livros = new ArrayList<>();
        ArrayList<Utentes> utentes = new ArrayList<>();
        ArrayList<Jornal> jornals = new ArrayList<>();
        ArrayList<Emprestimos> emprestimos = new ArrayList<>();
        ArrayList<Reserva> reservas = new ArrayList<>();

        // Inicialização das views e controladores
        UtenteView utenteView = new UtenteView();
        utenteView.setScanner(scanner);  // Configura o scanner
        UtenteController utenteController = new UtenteController(utentes, utenteView, reservas, emprestimos);
        utenteView.setUtenteController(utenteController); // Configura o controller

        JornalController jornalController = new JornalController(jornals, reservas, emprestimos);
        JornalView jornalView = new JornalView(jornalController);

        // Inicialização de ReservaController e EmprestimosController
        ReservaController reservaController = new ReservaController(reservas);
        EmprestimosController emprestimosController = new EmprestimosController(reservaController, emprestimos);

// Garantindo que o EmprestimosController é passado corretamente para outros controladores
        reservaController.setEmprestimosController(emprestimosController);
        LivroController livroController = new LivroController(livros, null, emprestimosController, reservas);

        LivroView livroView = new LivroView(livroController, scanner);
        livroController.setLivroView(livroView); // Configura o livroView no livroController

        emprestimosController.setLivroController(livroController);
        ReservaView reservaView = new ReservaView(reservaController, utenteController, livroController, jornalController, jornalView);
        EmprestimosView emprestimosView = new EmprestimosView(emprestimosController, utenteController, livroController, jornalController, jornalView);
        PesquisaEstatisticasController pesquisaEstatisticasController = new PesquisaEstatisticasController(livros, jornals, emprestimos, reservas);
        PesquisaEstatisticasView pesquisaEstatisticasView = new PesquisaEstatisticasView(scanner, pesquisaEstatisticasController);

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

            // Validação da entrada para o menu principal
            while (!scanner.hasNextInt()) {
                System.out.println("Por favor, insira um número válido.");
                System.out.print("Escolha uma opção: ");
                scanner.next(); // Limpa o buffer
            }
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    livroView.gerirLivros();  // Gerir livros
                    break;
                case 2:
                    jornalView.exibirMenu();  // Gerir jornais
                    break;
                case 3:
                    utenteView.gerirUtentes(utentes, reservas, emprestimos);  // Gerir utentes
                    break;
                case 4:
                    emprestimosView.exibirMenuPrincipal();  // Gerir empréstimos
                    break;
                case 5:
                    reservaView.exibirMenuPrincipal();  // Gerir reservas
                    break;
                case 6:
                    pesquisaEstatisticasView.exibirMenu();  // Pesquisas e Estatísticas
                    break;
                case 7:
                    System.out.println("1. Ler dados");
                    System.out.println("2. Guardar dados");
                    System.out.println("0. Voltar");
                    System.out.print("Escolha uma opção: ");

                    while (!scanner.hasNextInt()) {
                        System.out.println("Por favor, insira um número válido.");
                        System.out.print("Escolha uma opção: ");
                        scanner.next(); // Limpa o buffer
                    }
                    escolha = scanner.nextInt();

                    if (escolha == 1) {
                        System.out.println("\nCarregando dados...");
                        livros.addAll(ImportarDados.carregarLivros(configReader.getCaminhoLivros()));
                        utentes.addAll(ImportarDados.carregarUtentes(configReader.getCaminhoUtentes()));
                        jornals.addAll(ImportarDados.carregarJornais(configReader.getCaminhoJornal()));
                        emprestimos.addAll(ImportarDados.carregarEmprestimos(configReader.getCaminhoEmprestimo(), utentes, livros));
                        reservas.addAll(ImportarDados.carregarReservas(configReader.getCaminhoReserva(), utentes, livros));
                        System.out.println("Dados carregados com sucesso!");
                    } else if (escolha == 2) {
                        System.out.println("\nGuardando dados...");
                        ExportarDados.exportarLivros(configReader.getCaminhoLivros(), livros);
                        ExportarDados.exportarUtentes(configReader.getCaminhoUtentes(), utentes);
                        ExportarDados.exportarJornal(configReader.getCaminhoJornal(), jornals);
                        ExportarDados.exportarEmprestimos(configReader.getCaminhoEmprestimo(), emprestimos);
                        ExportarDados.exportarReservas(configReader.getCaminhoReserva(), reservas);
                        System.out.println("Dados guardados com sucesso!");
                    } else if (escolha != 0) {
                        System.out.println("\nOpção inválida. Tente novamente.");
                    }
                    break;
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
