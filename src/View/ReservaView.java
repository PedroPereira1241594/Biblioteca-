package View;

import Controller.*;
import Model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
/**
 * Classe que apresenta a interface de visualização (View) para operações relacionadas às reservas.
 * Permite interação com o utilizador para criar, consultar, atualizar e gerir reservas.
 */
public class ReservaView {
    private final ReservaController reservaController;
    private final UtenteController utenteController;
    private final LivroController livroController;
    private final JornalController jornalController;
    private final JornalView jornalView;
    private final EmprestimosController emprestimosController; // Adicionando o controller de empréstimos
    private final Scanner scanner;

    /**
     * Construtor da classe ReservaView.
     *
     * @param reservaController       Controller responsável pelas reservas.
     * @param utenteController        Controller responsável pelos utentes.
     * @param livroController         Controller responsável pelos livros.
     * @param jornalController        Controller responsável pelos jornais.
     * @param jornalView              View para apresentar de jornais.
     * @param emprestimosController   Controller responsável pelos empréstimos.
     */
    public ReservaView(ReservaController reservaController, UtenteController utenteController, LivroController livroController, JornalController jornalController, JornalView jornalView, EmprestimosController emprestimosController) {
        this.reservaController = reservaController;
        this.utenteController = utenteController;
        this.livroController = livroController;
        this.jornalController = jornalController;
        this.jornalView = jornalView;
        this.emprestimosController = emprestimosController; // Agora está corretamente inicializado
        this.scanner = new Scanner(System.in);
    }

    /**
     * Mostra o menu principal de reservas, permitindo ao utilizador aceder diversas funcionalidades.
     * As opções incluem:
     * <ul>
     *   <li>Criar Reserva</li>
     *   <li>Consultar uma reserva em específico pelo id.</li>
     *   <li>Atualizar uma reserva</li>
     *   <li>Eliminar uma reserva</li>
     *   <li>Listar todas reservas da biblioteca</li>
     *   <li>Sair do menu.</li>
     * </ul>
     */
    public void exibirMenu() {
        int opcao;
        do {
            System.out.println("\n=== Menu de Reservas ===");
            System.out.println("1. Criar Reserva");
            System.out.println("2. Consultar Reserva");
            System.out.println("3. Atualizar Reserva");
            System.out.println("4. Remover Reserva");
            System.out.println("5. Listar Todas as Reservas");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); 

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

    /**
     * Cria uma reserva para um utente selecionado, incluindo itens como livros ou jornais no intervalo de datas fornecido pelo utilizador.
     */
    private void criarReserva() {
        try {
            System.out.println("\n=== Criar Reserva ===");

            // Seleção do utente
            Utentes utente = obterUtente();
            if (utente == null) {
                System.out.println("Operação cancelada.");
                return;
            }

            List<ItemEmprestavel> itensParaReserva = new ArrayList<>();

            // Loop para adicionar itens à reserva
            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Adicionar Livro à reserva");
                System.out.println("2. Adicionar Jornal/Revista à reserva");
                System.out.println("0. Finalizar seleção de itens...");
                System.out.print("Escolha uma opção: ");
                int opcaoItem = scanner.nextInt();
                scanner.nextLine();

                if (opcaoItem == 0) {
                    break; // Finaliza a seleção de itens
                }

                switch (opcaoItem) {
                    case 1:
                        List<Livro> livrosParaReserva = obterLivros();
                        itensParaReserva.addAll(livrosParaReserva);
                        break;
                    case 2:
                        List<Jornal> jornaisParaReserva = obterJornais();
                        itensParaReserva.addAll(jornaisParaReserva);
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            }

            if (itensParaReserva.isEmpty()) {
                System.out.println("Nenhum item selecionado. Operação cancelada.");
                return;
            }

            // Solicitação das datas
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataRegisto = obterData("Data de Registo (dd/MM/yyyy): ", formato);
            LocalDate dataInicio = obterData("Data de Início da Reserva (dd/MM/yyyy): ", formato);
            LocalDate dataFim = obterData("Data de Fim da Reserva (dd/MM/yyyy): ", formato);

            while (!emprestimosController.verificarDataAnterior(dataInicio, dataFim)) {
                System.out.println("Erro: A data de fim da reserva não pode ser anterior à data de início.");
                dataFim = obterData("Informe a data de fim da reserva (dd/MM/yyyy): ", formato);
            }

            // Verificar se algum item está emprestado e não pode ser reservado
            for (ItemEmprestavel item : itensParaReserva) {
                if (item instanceof Livro) {
                    if (reservaController.verificarItemReservado((Livro) item, dataInicio, dataFim)) {
                        System.out.println("Erro: O livro com o ISBN: '" + item.getIdentificador() + "' já está reservado para o período indicado entre " + dataInicio + " e " + dataFim);
                        return;
                    } else if (emprestimosController.verificarItemEmprestado((Livro) item, dataInicio, dataFim)) {
                        System.out.println("Erro: O livro com o ISBN: '" + item.getIdentificador() + "' já está emprestado para o período indicado entre " + dataInicio + " e " + dataFim);
                        return;
                    }
                } else if (item instanceof Jornal) {
                    if (reservaController.verificarItemReservado((Jornal) item, dataInicio, dataFim)) {
                        System.out.println("Erro: O jornal com ISSN: '" + item.getIdentificador() + "' já está reservado para o período indicado entre " + dataInicio + " e " + dataFim);
                        return;
                    } else if (emprestimosController.verificarItemEmprestado((Jornal) item, dataInicio, dataFim)) {
                        System.out.println("Erro: O jornal com ISSN: '" + item.getIdentificador() + "' já está emprestado para o período indicado entre " + dataInicio + " e " + dataFim);
                        return;
                    }
                }
            }
            reservaController.criarReserva(utente, itensParaReserva, dataRegisto, dataInicio, dataFim, emprestimosController);
        } catch (Exception e) {
            System.out.println("Erro ao criar reserva: " + e.getMessage());
        }
    }

    /**
     * Lê e valida uma data inserida pelo utilizador no formato dd/MM/yyyy.
     * Caso a data seja inválida, solicita novamente ao utilizador.
     *
     * @param formato O formato esperado para a data.
     * @return A data lida e validada.
     */
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

    /**
     * Permite consultar os detalhes de uma reserva com base no ID fornecido.
     *
     * O método solicita ao utilizador o ID da reserva, chama o controlador para
     * procurar as informações e mostra os detalhes, caso a reserva seja encontrada.
     * Caso contrário, emite uma mensagem a informar que a reserva não foi encontrada.
     */
    private void consultarReserva() {
        System.out.println("\n=== Consultar Reserva ===");
        System.out.print("Número da Reserva: ");
        int numero = scanner.nextInt();
        scanner.nextLine(); 

        // Chama o método consultarReserva do controller
        Reserva reserva = reservaController.consultarReserva(numero);
        
        if (reserva != null) {
            reservaController.exibirDetalhesReserva(reserva);
        } else {
            System.out.println("Reserva com o número " + numero + " não encontrada.");
        }
    }

    /**
     * Permite ao utilizador atualizar uma reserva existente, incluindo alterar
     * as datas ou os itens associados à reserva.
     */
    private void atualizarReserva() {
        System.out.println("\n=== Atualizar Reserva ===");

        // Obter o número da reserva
        System.out.print("Digite o número da reserva: ");
        int numeroReserva;
        try {
            numeroReserva = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Erro: Entrada inválida. Digite um número.");
            scanner.nextLine();
            return;
        }

        // Consultar a reserva pelo número
        Reserva reserva = reservaController.consultarReserva(numeroReserva);

        // Validar se a reserva foi encontrada
        if (reserva == null) {
            System.out.println("Erro: Reserva não encontrada.");
            return;
        }

        // Exibir opções de atualização
        System.out.println("O que você deseja atualizar?");
        System.out.println("1. Atualizar as datas da reserva");
        System.out.println("2. Alterar itens da reserva");
        System.out.println("0. Cancelar");
        System.out.print("Escolha uma opção: ");

        // Capturar a opção do usuário
        int opcao;
        try {
            opcao = scanner.nextInt();
            scanner.nextLine(); 
        } catch (InputMismatchException e) {
            System.out.println("Erro: Opção inválida. Digite um número.");
            scanner.nextLine(); 
            return;
        }

        // Processar a opção escolhida
        switch (opcao) {
            case 1:
                atualizarDatasReserva(reserva, numeroReserva);
                break;
            case 2:
                modificarItensReserva(reserva, reserva.getDataInicio(), reserva.getDataFim());
                break;
            case 0:
                System.out.println("Operação cancelada.");
                return;
            default:
                System.out.println("Erro: Opção inválida.");
        }
    }

    /**
     * Atualiza as datas de uma reserva específica, validando os novos valores.
     *
     * @param reserva A reserva a ser atualizada.
     * @param numero  O número identificador da reserva.
     */
    private void atualizarDatasReserva(Reserva reserva, int numero) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate novaDataInicio, novaDataFim;

        while (true) {
            // Solicitar a nova data de início
            System.out.print("Informe a nova data de início da reserva (dd/MM/yyyy): ");
            novaDataInicio = lerData(formato);

            // Solicitar a nova data de fim
            System.out.print("Informe a nova data de fim da reserva (dd/MM/yyyy): ");
            novaDataFim = lerData(formato);

            // Verificar se a data de fim não é anterior à data de início
            if (reservaController.verificarDataAnterior(novaDataInicio, novaDataFim)) {
                reservaController.atualizarReserva(numero, novaDataInicio, novaDataFim);
                System.out.println("Datas da reserva atualizadas com sucesso.");
                break;
            } else {
                System.out.println("Erro: A data de fim não pode ser anterior à data de início da reserva.");
                System.out.print("Deseja tentar novamente? (S/N): ");
                char resposta = scanner.next().toUpperCase().charAt(0);
                scanner.nextLine(); 
                if (resposta == 'N') {
                    System.out.println("Operação cancelada.");
                    break;
                }
            }
        }
    }

    /**
     * Permite modificar os itens(livros ou jornais) de uma reserva, seja adicionando ou removendo itens.
     *
     * @param reserva            A reserva a ser modificada.
     * @param dataInicioReserva  Data de início da reserva.
     * @param dataFimReserva     Data de fim da reserva.
     */
    private void modificarItensReserva(Reserva reserva, LocalDate dataInicioReserva, LocalDate dataFimReserva) {
        System.out.println("\nO que você deseja fazer com os itens da reserva?");
        System.out.println("1. Adicionar livro");
        System.out.println("2. Remover livro");
        System.out.println("3. Adicionar jornal");
        System.out.println("4. Remover jornal");
        System.out.println("0. Cancelar");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();
        scanner.nextLine(); 

        switch (opcao) {
            case 1 -> adicionarLivroNaReserva(reserva, dataInicioReserva, dataFimReserva);
            case 2 -> removerLivroDaReserva(reserva);
            case 3 -> adicionarJornalNaReserva(reserva, dataInicioReserva, dataFimReserva);
            case 4 -> removerJornalDaReserva(reserva);
            case 0 -> System.out.println("Operação cancelada.");
            default -> System.out.println("Opção inválida.");
        }
    }

    /**
     * Adiciona um livro específico a uma reserva existente.
     *
     * @param reserva            A reserva onde o livro será adicionado.
     * @param dataInicioReserva  Data de início da reserva para verificar se está disponível.
     * @param dataFimReserva     Data de fim da reserva para verificar se está disponível.
     */
    private void adicionarLivroNaReserva(Reserva reserva, LocalDate dataInicioReserva, LocalDate dataFimReserva) {
        System.out.println("\n=== Adicionar Livro à Reserva ===");

        Livro livro = null;
        while (livro == null) {
            System.out.print("Informe o ISBN do livro: ");
            String isbn = scanner.nextLine();
            livro = livroController.buscarLivroPorIsbn(isbn);

            if (livro == null) {
                System.out.println("Erro: Livro não encontrado. Deseja:");
                System.out.println("1. Adicionar novo livro");
                System.out.println("2. Tentar novamente");
                System.out.println("0. Cancelar");
                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine(); 

                switch (opcao) {
                    case 1:
                        livroController.adicionarLivro();
                        livro = livroController.buscarLivroPorIsbn(isbn);
                        break;
                    case 2:
                        System.out.println("Tente novamente...");
                        break;
                    case 0:
                        System.out.println("Operação cancelada.");
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } else {
                // Verificar se o livro já está na reserva
                for (ItemEmprestavel item : reserva.getItens()) {
                    if (item instanceof Livro && item.equals(livro)) {
                        System.out.println("Erro: O livro '" + livro.getNome() + "' já está na reserva.");
                        livro = null;
                        break;
                    }
                }

                // Verificar se o livro está reservado ou emprestado para o período indicado
                if (reservaController.verificarItemReservado(livro, dataInicioReserva, dataFimReserva)) {
                    System.out.println("Erro: O livro com o ISBN: '" + livro.getIdentificador() + "' já está reservado para o período indicado entre "
                            + dataInicioReserva + " e " + dataFimReserva);
                    return;
                } else if (emprestimosController.verificarItemEmprestado(livro, dataInicioReserva, dataFimReserva)) {
                    System.out.println("Erro: O livro com o ISBN: '" + livro.getIdentificador() + "' já está emprestado para o período indicado entre "
                            + dataInicioReserva + " e " + dataFimReserva);
                    return;
                }
            }
        }

        // Adicionar o livro à reserva
        reservaController.adicionarItemNaReserva(reserva.getNumero(), livro, dataInicioReserva, dataFimReserva);
        System.out.println("Livro adicionado com sucesso à reserva.");
    }

    /**
     * Remove um livro específico de uma reserva existente.
     *
     * @param reserva A reserva da qual o livro será removido.
     */
    private void removerLivroDaReserva(Reserva reserva) {
        System.out.println("\n=== Remover Livro da Reserva ===");

        // Solicitar o ISBN do livro a ser removido
        System.out.print("Informe o ISBN do livro a ser removido: ");
        String isbn = scanner.nextLine();

        // Procura o livro pelo ISBN
        Livro livro = livroController.buscarLivroPorIsbn(isbn);
        if (livro == null) {
            System.out.println("Erro: Livro com ISBN '" + isbn + "' não encontrado.");
            return;
        }

        // Verificar se o livro está na reserva
        boolean encontrado = false;
        for (ItemEmprestavel item : reserva.getItens()) {
            if (item instanceof Livro && item.equals(livro)) {
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            // Remover o livro da reserva
            reservaController.removerItemDaReserva(reserva.getNumero(), livro);
            System.out.println("Livro removido com sucesso da reserva.");
        } else {
            System.out.println("Erro: O livro '" + livro.getNome() + "' não está na reserva.");
        }
    }

    /**
     * Adiciona um jornal a uma reserva existente.
     *
     * @param reserva           A reserva à qual o jornal será adicionado.
     * @param dataInicioReserva A data de início da reserva para verificar se está disponível.
     * @param dataFimReserva    A data de término da reserva para verificar se está disponível.
     */
    private void adicionarJornalNaReserva(Reserva reserva, LocalDate dataInicioReserva, LocalDate dataFimReserva) {
        Jornal jornal = null;
        while (jornal == null) {
            System.out.print("ISSN do jornal a adicionar: ");
            String issn = scanner.nextLine();
            jornal = jornalController.procurarPorIssn(issn);

            if (jornal == null) {
                System.out.println("Erro: Jornal não encontrado. Deseja:");
                System.out.println("1. Adicionar novo jornal");
                System.out.println("2. Tentar novamente");
                System.out.println("0. Cancelar");
                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine(); 

                switch (opcao) {
                    case 1 -> {
                        System.out.print("Informe o título: ");
                        String titulo = scanner.nextLine();
                        System.out.print("Informe a categoria: ");
                        String categoria = scanner.nextLine();
                        System.out.print("Informe a editora: ");
                        String editora = scanner.nextLine();
                        System.out.print("Informe a data de publicação (dd/MM/yyyy): ");
                        String dataTexto = scanner.nextLine();
                        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate data = LocalDate.parse(dataTexto, formato);
                        jornalController.criarJornal(issn, titulo, categoria, editora, data);
                        jornal = jornalController.procurarPorIssn(issn);
                    }
                    case 2 -> System.out.println("Tente novamente...");
                    case 0 -> {
                        System.out.println("Operação cancelada.");
                        return;
                    }
                    default -> System.out.println("Opção inválida!");
                }
            } else {
                // Verificar se o jornal já está na reserva
                for (ItemEmprestavel item : reserva.getItens()) {
                    if (item instanceof Jornal && item.equals(jornal)) {
                        System.out.println("Erro: O jornal '" + jornal.getTitulo() + "' já está na reserva.");
                        jornal = null;
                        break;
                    }
                }

                // Verificar se o jornal está reservado ou emprestado para o período indicado
                if (reservaController.verificarItemReservado(jornal, dataInicioReserva, dataFimReserva)) {
                    System.out.println("Erro: O jornal com ISSN: '" + jornal.getIdentificador() + "' já está reservado para o período indicado entre "
                            + dataInicioReserva + " e " + dataFimReserva);
                    return;
                } else if (emprestimosController.verificarItemEmprestado(jornal, dataInicioReserva, dataFimReserva)) {
                    System.out.println("Erro: O jornal com ISSN: '" + jornal.getIdentificador() + "' já está emprestado para o período indicado entre "
                            + dataInicioReserva + " e " + dataFimReserva);
                    return;
                }
            }
        }

        // Adicionar o jornal à reserva
        reservaController.adicionarItemNaReserva(reserva.getNumero(), jornal, dataInicioReserva, dataFimReserva);
        System.out.println("Jornal adicionado com sucesso à reserva.");
    }

    /**
     * Remove um jornal de uma reserva existente.
     *
     * @param reserva A reserva da qual o jornal será removido.
     */
    private void removerJornalDaReserva(Reserva reserva) {
        System.out.print("ISSN do jornal a remover: ");
        String issn = scanner.nextLine();
        Jornal jornal = jornalController.procurarPorIssn(issn);

        if (jornal != null) {
            // Verificar se o jornal está na reserva
            boolean encontrado = false;
            for (ItemEmprestavel item : reserva.getItens()) {
                if (item instanceof Jornal && item.equals(jornal)) {
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                reservaController.removerItemDaReserva(reserva.getNumero(), jornal);
                System.out.println("Jornal removido com sucesso da reserva.");
            } else {
                System.out.println("Erro: Jornal não encontrado na reserva.");
            }
        } else {
            System.out.println("Erro: Jornal com ISSN '" + issn + "' não encontrado.");
        }
    }

    /**
     * Remove uma reserva existente, caso confirmada pelo utilizador.
     */
    private void removerReserva() {
        try {
            System.out.println("\n=== Remover Reserva ===");
            System.out.print("Número da Reserva: ");
            int numero = scanner.nextInt();
            scanner.nextLine();

            // Consultar a reserva
            Reserva reserva = reservaController.consultarReserva(numero);

            if (reserva == null) {
                System.out.println("Nenhuma reserva encontrada com o número informado.");
                return; // Encerra o método se a reserva não for encontrada
            } else {

                // Exibir detalhes da reserva antes de remover
                reservaController.exibirDetalhesReserva(reserva);

                // Confirmar remoção
                System.out.print("Tem certeza que deseja remover esta reserva? (S/N): ");
                char confirmacao = scanner.next().toUpperCase().charAt(0);
                scanner.nextLine(); 

                if (confirmacao == 'S') {
                    boolean sucesso = reservaController.removerReserva(numero);

                    if (!sucesso) {
                        System.out.println("Erro ao tentar remover a reserva.");
                    } else {
                        System.out.println("Reserva removida com sucesso!");
                    }
                } else {
                    System.out.println("Operação cancelada.");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    /**
     * Lista todas as reservas, removendo duplicados e ordenando-as
     * pelo número da reserva.
     */
    public void listarReservas() {
        // Obtém a lista de reservas ativas do controller
        List<Reserva> reservasAtivas = reservaController.listarTodasReservas();

        // Verifica se existem reservas registadas
        if (reservasAtivas == null || reservasAtivas.isEmpty()) {
            System.out.println("\nNenhuma reserva registada.");
            return;
        }

        // Remover duplicados manualmente
        List<Reserva> reservasSemDuplicados = new ArrayList<>();
        for (Reserva reserva : reservasAtivas) {
            boolean jaExiste = false;
            for (Reserva r : reservasSemDuplicados) {
                if (r.getNumero() == reserva.getNumero()) {
                    jaExiste = true;
                    break;
                }
            }
            if (!jaExiste) {
                reservasSemDuplicados.add(reserva);
            }
        }

        // Ordenar a lista manualmente usando Bubble Sort
        for (int i = 0; i < reservasSemDuplicados.size() - 1; i++) {
            for (int j = 0; j < reservasSemDuplicados.size() - i - 1; j++) {
                if (reservasSemDuplicados.get(j).getNumero() > reservasSemDuplicados.get(j + 1).getNumero()) {
                    Reserva temp = reservasSemDuplicados.get(j);
                    reservasSemDuplicados.set(j, reservasSemDuplicados.get(j + 1));
                    reservasSemDuplicados.set(j + 1, temp);
                }
            }
        }

        // Mostra as reservas
        System.out.println("\n=== Lista de Reservas ===");
        System.out.printf("%-10s %-35s %-100s %-25s %-20s %-20s\n",
                "Número", "Utente", "Itens Reservados", "Data Registo", "Data Início", "Data Fim");

        for (Reserva reserva : reservasSemDuplicados) {
            // Validação do nome do utente
            String utenteNome = (reserva.getUtente() != null && reserva.getUtente().getNome() != null)
                    ? reserva.getUtente().getNome()
                    : "Desconhecido";

            // Validação e construção da string de itens
            String itens = "Sem itens"; // Valor padrão
            if (reserva.getItens() != null && !reserva.getItens().isEmpty()) {
                List<ItemEmprestavel> itensReservados = reserva.getItens();
                itens = ""; // Inicializa vazio
                for (int i = 0; i < itensReservados.size(); i++) {
                    ItemEmprestavel item = itensReservados.get(i);
                    if (item != null) {
                        if (item instanceof Livro) {
                            itens += "ISBN: " + ((Livro) item).getIsbn();
                        } else if (item instanceof Jornal) {
                            itens += "ISSN: " + ((Jornal) item).getIssn();
                        } else {
                            itens += "Item desconhecido";
                        }
                    } else {
                        itens += "Item desconhecido";
                    }

                    // Adiciona vírgula entre os itens, exceto no último
                    if (i < itensReservados.size() - 1) {
                        itens += ", ";
                    }
                }
            }

            // Validação das datas
            String dataRegisto = (reserva.getDataRegisto() != null)
                    ? reserva.getDataRegisto().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "Data desconhecida";

            String dataInicio = (reserva.getDataInicio() != null)
                    ? reserva.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "Pendente";

            String dataFim = (reserva.getDataFim() != null)
                    ? reserva.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "Pendente";

            // Exibe as informações da reserva
            System.out.printf("%-10d %-35s %-100s %-25s %-20s %-20s\n",
                    reserva.getNumero(), utenteNome, itens, dataRegisto, dataInicio, dataFim);
        }
    }

    /**
     * Obtém um utente (utilizador do sistema) a partir do NIF informado. Permite
     * criar um utente caso o NIF não seja encontrado.
     *
     * @return O utente correspondente ao NIF ou {@code null} se a operação for cancelada.
     */
    private Utentes obterUtente() {
        Utentes utente = null;
        while (utente == null) {
            System.out.print("NIF do Utente: ");
            String nif = scanner.nextLine();
            utente = utenteController.buscarUtentePorNif(nif);

            if (utente == null) {
                System.out.println("Erro: Utente com NIF '" + nif + "' não encontrado.");
                System.out.println("1. Adicionar Utente\n2. Tentar novamente\n0. Cancelar");
                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine(); 

                switch (opcao) {
                    case 1:
                        utenteController.adicionarUtente();
                        utente = utenteController.buscarUtentePorNif(nif);
                        break;
                    case 2:
                        System.out.println("Tente novamente...");
                        break;
                    case 0:
                        return null;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            }
        }
        return utente;
    }

    /**
     * Obtém uma lista de livros para reserva a partir dos ISBNs informados.
     * Permite adicionar novos livros ao sistema, se necessário.
     *
     * @return A lista de livros selecionados para reserva.
     */
    private List<Livro> obterLivros() {
        List<Livro> livros = new ArrayList<>();
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
                    System.out.println("Erro: Livro não encontrado.");
                    System.out.println("1. Adicionar Livro\n2. Tentar novamente\n0. Cancelar");
                    System.out.print("Escolha uma opção: ");
                    int opcao = scanner.nextInt();
                    scanner.nextLine();

                    switch (opcao) {
                        case 1:
                            livroController.adicionarLivro();
                            livro = livroController.buscarLivroPorIsbn(isbn);
                            break;
                        case 2:
                            System.out.println("Tente novamente...");
                            break;
                        case 0:
                            return livros;
                        default:
                            System.out.println("Opção inválida! Tente novamente.");
                    }
                } else if (livros.contains(livro)) {
                    System.out.println("Erro: O livro já foi adicionado a esta reserva.");
                    livro = null;
                }
            }
            livros.add(livro);
        }
        return livros;
    }

    /**
     * Obtém uma lista de jornais para reserva a partir dos ISSNs informados.
     * Permite adicionar novos jornais ao sistema, se necessário.
     *
     * @return A lista de jornais selecionados para reserva.
     */
    private List<Jornal> obterJornais() {
        List<Jornal> jornais = new ArrayList<>();
        int opcao;
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

                    System.out.println("1. Adicionar Jornal");
                    System.out.println("2. Tentar novamente");
                    System.out.println("0. Cancelar");
                    System.out.print("Escolha uma opção: ");
                    opcao = scanner.nextInt();
                    scanner.nextLine();

                    switch (opcao) {
                        case 1:
                            jornalView.criarJornal();
                            jornal = jornalController.procurarPorIssn(issn);
                            break;
                        case 2:
                            System.out.println("Tente novamente...");
                            break;
                        case 0:
                            return jornais;
                        default:
                            System.out.println("Opção inválida! Tente novamente.");
                    }
                } else if (jornais.contains(jornal)) {
                    System.out.println("Erro: O jornal já foi adicionado a esta reserva.");
                    jornal = null;
                }
            }
            jornais.add(jornal);
        }
        return jornais;
    }

    /**
     * Solicita ao utilizador a introdução de uma data num formato específico.
     *
     * @param mensagem A mensagem a ser apresentada ao solicitar a data.
     * @param formato  O formato esperado para a data.
     * @return A data fornecida pelo utilizador.
     */
    private LocalDate obterData(String mensagem, DateTimeFormatter formato) {
        while (true) {
            try {
                System.out.print(mensagem);
                String dataStr = scanner.nextLine();
                return LocalDate.parse(dataStr, formato);
            } catch (Exception e) {
                System.out.println("Formato de data inválido! Tente novamente.");
            }
        }
    }

}
