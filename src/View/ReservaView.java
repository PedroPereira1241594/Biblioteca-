package View;

import Controller.EmprestimosController;
import Controller.LivroController;
import Controller.ReservaController;
import Controller.UtenteController;
import Model.Livro;
import Model.Reserva;
import Model.Utentes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReservaView {
    private ReservaController reservaController;
    private UtenteController utenteController;
    private LivroController livroController;
    private EmprestimosController emprestimosController; // Adicionando o controller de empréstimos
    private Scanner scanner;


    public ReservaView(ReservaController reservaController, UtenteController utenteController, LivroController livroController, EmprestimosController emprestimosController) {
        this.reservaController = reservaController;
        this.utenteController = utenteController;
        this.livroController = livroController;
        this.emprestimosController = emprestimosController; // Agora está corretamente inicializado
        this.scanner = new Scanner(System.in);
    }


    public void exibirMenu() {
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
                case 5 -> listarReservas();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void criarReserva() {
        try {
            System.out.println("\n=== Criar Reserva ===");

            // Seleção do utente
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
                            return;
                        default:
                            System.out.println("Opção inválida! Tente novamente.");
                    }
                }
            }

            // Seleção dos livros
            List<Livro> livrosParaReserva = new ArrayList<>();
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
                        System.out.println("1. Adicionar Livro");
                        System.out.println("2. Tentar novamente");
                        System.out.println("0. Cancelar");
                        System.out.print("Escolha uma opção: ");
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
                    } else if (livrosParaReserva.contains(livro)) {
                        // Verifica se o livro já foi adicionado
                        System.out.println("Erro: O livro '" + livro.getNome() + "' já foi adicionado a esta reserva.");
                        livro = null; // Pedir para tentar novamente
                    }
                }
                livrosParaReserva.add(livro); // Adiciona o livro à lista de reserva
            }

            // Solicitação das datas
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            System.out.print("Data de Registo (dd/MM/yyyy): ");
            LocalDate dataRegisto = lerData(formato);

            System.out.print("Data de Início da Reserva (dd/MM/yyyy): ");
            LocalDate dataInicio = lerData(formato);

            System.out.print("Data de Fim da Reserva (dd/MM/yyyy): ");
            LocalDate dataFim = lerData(formato);

            // Verificar se algum livro está emprestado e não pode ser reservado
            for (Livro livro : livrosParaReserva) {
                if (livroController.verificarLivroEmprestado(livro)) {  // Método hipotético para verificar se o livro está emprestado
                    System.out.println("Erro: O livro '" + livro.getNome() + "' está emprestado e não pode ser reservado.");
                    return; // Impede a criação da reserva
                }
            }

            // Chama o controller para criar a reserva e captura o retorno booleano
            boolean reservaCriadaComSucesso = reservaController.criarReserva(utente, livrosParaReserva, dataRegisto, dataInicio, dataFim, emprestimosController);

            // Exibe a mensagem de sucesso ou erro com base no retorno
            if (reservaCriadaComSucesso) {
                System.out.println("Reserva criada com sucesso!");
            } else {
                System.out.println("Erro ao criar reserva.");
            }

        } catch (Exception e) {
            System.out.println("Erro ao criar reserva: " + e.getMessage());
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

        // Chama o método consultarReserva do controller
        Reserva reserva = reservaController.consultarReserva(numero);

        // Se a reserva for encontrada, exibe os detalhes
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

        // Verificar se a reserva existe
        Reserva reserva = reservaController.buscarReservaPorNumero(numero);
        if (reserva == null) {
            System.out.println("Reserva não encontrada com o número informado.");
            return; // Retorna caso a reserva não exista
        }

        System.out.println("O que você deseja atualizar?");
        System.out.println("1. Atualizar as datas da reserva");
        System.out.println("2. Alterar livros da reserva");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();  // Limpar buffer

        switch (opcao) {
            case 1:
                // Atualizar as datas
                atualizarDatasReserva(reserva, numero);
                break;
            case 2:
                // Adicionar/remover livros
                modificarLivrosReserva(reserva);
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private void atualizarDatasReserva(Reserva reserva, int numero) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Solicitar as novas datas
        System.out.print("Informe a Nova Data de Início (dd/MM/yyyy): ");
        LocalDate novaDataInicio = lerData(formato);

        LocalDate novaDataFim = null;
        boolean dataValida = false;

        while (!dataValida) {
            System.out.print("Informe a Nova Data de Fim (dd/MM/yyyy): ");
            novaDataFim = lerData(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            if (!reservaController.verificarDataAnterior(reserva.getDataInicio(), novaDataFim)) {
                System.out.println("Erro: A Data Fim não pode ser anterior à Data de Início da reserva.");
                System.out.print("Deseja tentar novamente? (S/N): ");
                char resposta = scanner.next().toUpperCase().charAt(0);
                scanner.nextLine(); // Limpar buffer
                if (resposta == 'N') {
                    System.out.println("Operação cancelada.");
                    return;
                }
            } else {
                dataValida = true;
            }
        }

        // Atualizar a reserva
        reservaController.atualizarReserva(numero, novaDataInicio, novaDataFim);
    }

    private void modificarLivrosReserva(Reserva reserva) {
        System.out.println("O que você deseja fazer com os livros da reserva?");
        System.out.println("1. Adicionar livro");
        System.out.println("2. Remover livro");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        switch (opcao) {
            case 1:
                adicionarLivroNaReserva(reserva);
                break;
            case 2:
                removerLivroDaReserva(reserva);
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private void adicionarLivroNaReserva(Reserva reserva) {
        System.out.println("\n=== Adicionar Livro à Reserva ===");

        // Solicitar o ISBN do livro a ser adicionado
        System.out.print("Informe o ISBN do livro: ");
        String isbn = scanner.nextLine();

        // Buscar o livro pelo ISBN
        Livro livro = livroController.buscarLivroPorIsbn(isbn);
        if (livro == null) {
            System.out.println("Erro: Livro com ISBN '" + isbn + "' não encontrado.");
            return;
        }

        // Verificar se o livro já está na reserva
        if (reserva.getLivros().contains(livro)) {
            System.out.println("Erro: O livro '" + livro.getNome() + "' já está na reserva.");
        } else {
            // Adicionar o livro à reserva
            reservaController.adicionarLivroNaReserva(reserva, livro);
            System.out.println("Livro '" + livro.getNome() + "' adicionado à reserva com sucesso.");
        }
    }

    private void removerLivroDaReserva(Reserva reserva) {
        System.out.println("\n=== Remover Livro da Reserva ===");

        // Solicitar o ISBN do livro a ser removido
        System.out.print("Informe o ISBN do livro a ser removido: ");
        String isbn = scanner.nextLine();

        // Buscar o livro pelo ISBN
        Livro livro = livroController.buscarLivroPorIsbn(isbn);
        if (livro == null) {
            System.out.println("Erro: Livro com ISBN '" + isbn + "' não encontrado.");
            return;
        }

        // Verificar se o livro está na reserva
        if (reserva.getLivros().contains(livro)) {
            // Remover o livro da reserva
            reservaController.removerLivroDaReserva(reserva, livro);
            System.out.println("Livro '" + livro.getNome() + "' removido da reserva com sucesso.");
        } else {
            System.out.println("Erro: O livro '" + livro.getNome() + "' não está na reserva.");
        }
    }


    private void removerReserva() {
        System.out.println("\n=== Remover Reserva ===");
        System.out.print("Número da Reserva: ");
        int numero = scanner.nextInt();
        scanner.nextLine();

        Reserva reserva = reservaController.buscarReservaPorNumero(numero);
        if (reserva == null) {
            System.out.println("Reserva não encontrada com o número informado.");
            return; // Retorna caso a reserva não exista
        }
        reservaController.removerReserva(numero);
    }

    private void listarReservas() {
        reservaController.listarReservas();
    }
}
