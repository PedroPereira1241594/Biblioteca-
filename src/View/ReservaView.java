package View;

import Controller.*;
import Model.*;
import View.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReservaView {
    private EmprestimosController emprestimosController;
    private ReservaController reservaController;
    private UtenteController utenteController;
    private LivroController livroController;
    private JornalController jornalController;
    private JornalView jornalView;
    private Scanner scanner;

    public ReservaView(
            ReservaController reservaController,
            UtenteController utenteController,
            LivroController livroController,
            JornalController jornalController,
            JornalView jornalView) {
        this.reservaController = reservaController;
        this.utenteController = utenteController;
        this.livroController = livroController;
        this.jornalController = jornalController;
        this.jornalView = jornalView;


        this.scanner = new Scanner(System.in);  // Inicializa o scanner
    }

    public void exibirMenuPrincipal() {
        int opcao;
        do {
            System.out.println("\n=== Menu de Reservas ===");
            System.out.println("1. Criar Reserva");
            System.out.println("2. Consultar Reserva");
            System.out.println("3. Atualizar Reserva");
            System.out.println("4. Remover Reserva");
            System.out.println("5. Listar Todas as Reservas");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            switch (opcao) {
                case 1 -> criarReserva();
                case 2 -> consultarReserva();
                case 3 -> atualizarReserva();
                case 4 -> removerReserva();
                //case 5 -> listarReservas();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void criarReserva() {
        try {
            System.out.println("\n=== Criar Reserva ===");

            // Seleção do utente
            Utentes utente = obterUtente();

            // Inicialização das listas
            List<Livro> livros = new ArrayList<>();
            List<Jornal> jornais = new ArrayList<>();

            // Método para selecionar os itens
            selecionarItensParaReserva(livros, jornais);

            // Solicitação das datas
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            System.out.print("Data de Registo (dd/MM/yyyy): ");
            LocalDate dataRegisto = lerData(formato);

            System.out.print("Data Início da Reserva (dd/MM/yyyy): ");
            LocalDate dataInicio = lerData(formato);

            System.out.print("Data Fim da Reserva  (dd/MM/yyyy): ");
            LocalDate dataFim = lerData(formato);

            // Verificar se algum livro/jornal está indisponível para empréstimo
            /*for (Livro livro : livros) {
                if (livroController.verificarLivroIndisponivel(livro, dataInicio, dataFim, null)) {
                    System.out.println("Erro: O livro '" + livro.getNome() + "' já está emprestado e não pode ser incluído na reserva.");
                    return; // Impede a criação do empréstimo
                }
            }

            for (Jornal jornal : jornais) {
                if (jornalController.verificarJornalIndisponivel(jornal, dataInicio, dataFim, null)) {
                    System.out.println("Erro: O jornal '" + jornal.getTitulo() + "' já está emprestado e não pode ser incluído na reserva.");
                    return; // Impede a criação do empréstimo
                }
            }*/
            // Criar o empréstimo
            reservaController.criarReserva(utente, livros, jornais, dataRegisto, dataInicio, dataFim);

            System.out.println("Reserva criado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao criar reserva: " + e.getMessage());
        }
    }

    private Utentes obterUtente() {
        Utentes utente = null;
        while (utente == null) {
            System.out.print("NIF do Utente: ");
            String nif = scanner.nextLine();
            utente = utenteController.buscarUtentePorNif(nif);

            if (utente == null) {
                System.out.println("Erro: Utente com NIF '" + nif + "' não encontrado.");
                System.out.println("\nO que você deseja realizar?");
                System.out.println("1. Adicionar Utente");
                System.out.println("2. Tentar novamente");
                System.out.println("0. Cancelar");
                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer

                switch (opcao) {
                    case 1:
                        utenteController.adicionarUtente();
                        utente = utenteController.buscarUtentePorNif(nif); // Buscar o utente novamente
                        break;
                    case 2:
                        System.out.println("Tente novamente...");
                        break;
                    case 0:
                        System.out.println("Operação cancelada.");
                        return null;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            }
        }
        return utente;
    }

    private void selecionarItensParaReserva(List<Livro> livros, List<Jornal> jornais ) {
        System.out.println("\nO que deseja reservar?");
        System.out.println("1. Livros");
        System.out.println("2. Jornais");
        System.out.println("0. Livros e Jornais");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        switch (opcao) {
            case 1:
                reservarLivros(livros);
                break;
            case 2:
                reservarJornais(jornais);
                break;
            case 0:
                reservarLivros(livros);
                reservarJornais(jornais);
                break;
            default:
                System.out.println("Opção inválida! Tente novamente.");
        }
    }

    private void exibirMenu() {
        System.out.println("1. Adicionar Livro");
        System.out.println("2. Tentar novamente");
        System.out.println("0. Cancelar");
        System.out.print("Escolha uma opção: ");
    }

    private void reservarLivros(List<Livro> livros) {
        System.out.print("Quantos livros deseja reservar? ");
        int qtdLivros = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < qtdLivros; i++) {
            Livro livro = null;
            while (livro == null) {
                System.out.print("ISBN do Livro " + (i + 1) + ": ");
                String isbn = scanner.nextLine();
                livro = livroController.buscarLivroPorIsbn(isbn);

                if (livro == null) {
                    System.out.println("Erro: Livro não encontrado. O que você deseja realizar?");
                    exibirMenu();
                    int opcao = scanner.nextInt();
                    scanner.nextLine(); // Limpar buffer

                    switch (opcao) {
                        case 1:
                            livroController.adicionarLivro();
                            livro = livroController.buscarLivroPorIsbn(isbn); // Buscar o livro novamente
                            break;
                        case 2:
                            System.out.println("Tente novamente...");
                            break;
                        case 0:
                            System.out.println("Operação cancelada.");
                            return;
                        default:
                            System.out.println("Opção inválida! Tente novamente.");
                    }
                } else if (livros.contains(livro)) {
                    System.out.println("Erro: O livro '" + livro.getNome() + "' já foi adicionado a esta reserva.");
                    livro = null;
                }
            }
            livros.add(livro);
        }
    }

    private void reservarJornais(List<Jornal> jornais) {
        System.out.print("Quantos jornais deseja reservar? ");
        int qtdJornais = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < qtdJornais; i++) {
            Jornal jornal = null;
            while (jornal == null) {
                System.out.print("ISSN do Jornal " + (i + 1) + ": ");
                String issn = scanner.nextLine();
                jornal = jornalController.procurarPorIssn(issn);

                if (jornal == null) {
                    System.out.println("Erro: Jornal não encontrado. O que você deseja realizar?");
                    exibirMenu();
                    int opcao = scanner.nextInt();
                    scanner.nextLine(); // Limpar buffer

                    switch (opcao) {
                        case 1:
                            jornalView.criarJornal();
                            jornal = jornalController.procurarPorIssn(issn); // Buscar o jornal novamente
                            break;
                        case 2:
                            System.out.println("Tente novamente...");
                            break;
                        case 0:
                            System.out.println("Operação cancelada.");
                            return;
                        default:
                            System.out.println("Opção inválida! Tente novamente.");
                    }
                } else if (jornais.contains(jornal)) {
                    System.out.println("Erro: O jornal '" + jornal.getTitulo() + "' já foi adicionado a esta reserva.");
                    jornal = null;
                }
            }
            jornais.add(jornal);
        }
    }

    private LocalDate lerData(DateTimeFormatter formato) {
        while (true) {
            try {
                String dataTexto = scanner.nextLine();
                return LocalDate.parse(dataTexto, formato);
            } catch (DateTimeParseException e) {
                System.out.print("Data inválida. Insira novamente (dd/MM/yyyy): ");
            }
        }
    }

    private void consultarReserva() {
        System.out.println("\n=== Consultar Reserva ===");
        System.out.print("Número da Reserva: ");
        int numero = scanner.nextInt();
        scanner.nextLine();  // Limpar buffer

        Reserva reserva = reservaController.consultarReserva(numero);

        if (reserva != null) {
            reservaController.exibirDetalhesReserva(reserva);
        } else {
            System.out.println("Reserva com o número " + numero + " não encontrada.");
        }
    }

    private void atualizarReserva() {
        System.out.println("\n=== Atualizar Reserva ===");
        System.out.print("Número da Reserva: ");
        int numero = scanner.nextInt();
        scanner.nextLine();

        Reserva reserva = reservaController.buscarReservaPorNumero(numero);
        if (reserva == null) {
            System.out.println("Reserva não encontrada com o número informado.");
            return;
        }

        LocalDate dataInicioReserva = reserva.getDataInicio();
        LocalDate dataFimReserva = reserva.getDataFim();

        System.out.println("O que você deseja atualizar?");
        System.out.println("1. Atualizar as datas da reserva");
        System.out.println("2. Alterar livros e jornais da reserva");
        System.out.println("0. Cancelar");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();  // Limpar buffer

        switch (opcao) {
            case 1:
                atualizarDatasReserva(reserva, numero);
                break;
            case 2:
                modificarItensReserva(reserva, dataInicioReserva, dataFimReserva);
                break;
            case 0:
                System.out.println("Operação cancelada.");
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    private void atualizarDatasReserva(Reserva reserva, int numero) {
        System.out.println("Digite a nova data de início da reserva: ");
        LocalDate novaDataInicio = lerData(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        reserva.setDataInicio(novaDataInicio);

        System.out.println("Digite a nova data de fim da reserva: ");
        LocalDate novaDataFim = lerData(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        reserva.setDataFim(novaDataFim);
        reservaController.atualizarReserva(reserva.getNumero(), novaDataInicio, novaDataFim);

        System.out.println("Datas atualizadas com sucesso!");
    }

    private void modificarItensReserva(Reserva reserva, LocalDate dataInicioReserva, LocalDate dataFimReserva) {
        List<Object> novosItens = new ArrayList<>();
        System.out.println("Adicionando e removendo itens da reserva.");

        //selecionarItensParaReserva(novosItens);
        //reserva.setItensReservados(novosItens);
        reserva.setDataInicio(dataInicioReserva);
        reserva.setDataFim(dataFimReserva);

        //reservaController.adicionarItemNaReserva(reserva, novosItens, reserva.getDataInicio(), reserva.getDataFim());
        System.out.println("Itens da reserva atualizados!");
    }

    private void removerReserva() {
        System.out.println("\n=== Remover Reserva ===");
        System.out.print("Número da Reserva: ");
        int numero = scanner.nextInt();
        scanner.nextLine();  // Limpar buffer

        boolean sucesso = reservaController.removerReserva(numero);
        if (sucesso) {
            System.out.println("Reserva removida com sucesso!");
        } else {
            System.out.println("Erro ao remover a reserva.");
        }
    }

    /*private void listarReservas() {
        System.out.println("\n=== Listar Todas as Reservas ===");
        List<Reserva> reservas = reservaController.listarReservas();

        if (reservas.isEmpty()) {
            System.out.println("Não há reservas para exibir.");
        } else {
            for (Reserva reserva : reservas) {
                reservaController.exibirDetalhesReserva(reserva);
            }
        }
    }*/
}
