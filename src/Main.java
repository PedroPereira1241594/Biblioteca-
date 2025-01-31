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
        ArrayList<Livro> livros = new ArrayList<>();
        ArrayList<Utentes> utentes = new ArrayList<>();
        ArrayList<Jornal> jornals = new ArrayList<>();
        ArrayList<Emprestimos> emprestimos = new ArrayList<>();
        ArrayList<Reserva> reservas = new ArrayList<>();

        // Inicialização das views e controladores
        UtenteView utenteView = new UtenteView();
        utenteView.setScanner(new Scanner(System.in));    // Configura o scanner
        UtenteController utenteController = new UtenteController(utentes, utenteView, reservas, emprestimos);
        utenteView.setUtenteController(utenteController); // Configura o controller
        JornalController jornalController = new JornalController(jornals, reservas, emprestimos);
        JornalView jornalView = new JornalView(jornalController, jornals);
        ReservaController reservaController = new ReservaController(null, reservas, livros, jornals);
        EmprestimosController emprestimosController = new EmprestimosController(reservaController, emprestimos, livros, jornals);
        reservaController.setEmprestimosController(emprestimosController);
        LivroController livroController = new LivroController(livros, null, emprestimosController, reservas, emprestimos);

        // Agora configura o livroView antes de usá-lo
        LivroView livroView = new LivroView(livroController, scanner);
        livroController.setLivroView(livroView); // Configura o livroView no livroController

        emprestimosController.setLivroController(livroController);
        ReservaView reservaView = new ReservaView(reservaController, utenteController, livroController,jornalController, jornalView,emprestimosController);
        EmprestimosView emprestimosView = new EmprestimosView(emprestimosController, utenteController, livroController, jornalController);
        PesquisaEstatisticasController pesquisaEstatisticasController = new PesquisaEstatisticasController(livros, jornals, emprestimos, reservas, emprestimosController, reservaController);
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
            System.out.println("7. Carregar/Exportar dados");
            System.out.println("0. Fechar o programa...");
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
                    do {
                        System.out.println("\n=== Carregamento/Exportação de Dados ===");
                        System.out.println("1. Carregar dados (do ficheiro para o sistema)");
                        System.out.println("2. Exportar dados (do sistema para o ficheiro)");
                        System.out.println("0. Voltar ao menu principal...");
                        System.out.print("Escolha uma opção: ");
                        escolha = scanner.nextInt();
                        switch (escolha) {
                            case 1:
                                System.out.println("\nCarregando dados...");
                                livros.addAll(ImportarDados.carregarLivros(caminhoLivros));
                                utentes.addAll(ImportarDados.carregarUtentes(caminhoUtentes));
                                jornals.addAll(ImportarDados.carregarJornais(caminhoJornal));

                                // Combina as listas de itens emprestáveis
                                ArrayList<ItemEmprestavel> itens = new ArrayList<>();
                                itens.addAll(livros);
                                itens.addAll(jornals);

                                // Carrega os empréstimos usando a lista combinada
                                emprestimos.addAll(ImportarDados.carregarEmprestimos(caminhoEmprestimo, utentes, itens));
                                reservas.addAll(ImportarDados.carregarReservas(caminhoReserva, utentes, itens));
                                System.out.println("\nDados carregados com sucesso!");
                                escolha = 0;
                                break;
                            case 2:
                                System.out.println("\nGuardando dados...");
                                exportarLivros(caminhoLivros, livros);
                                exportarUtentes(caminhoUtentes, utentes);
                                exportarJornal(caminhoJornal, jornals);
                                exportarEmprestimos(caminhoEmprestimo, emprestimos);
                                exportarReservas(caminhoReserva, reservas);
                                break;
                            case 0:
                                System.out.println("Saindo...");
                                break;
                            default:
                                System.out.println("\nOpção inválida. Tente novamente.");
                        }
                    } while (escolha != 0);
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
