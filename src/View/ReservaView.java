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

public class ReservaView {
    private ReservaController reservaController;
    private UtenteController utenteController;
    private LivroController livroController;
    private JornalController jornalController;
        private EmprestimosController emprestimosController; // Adicionando o controller de empréstimos
    private Scanner scanner;


    public ReservaView(ReservaController reservaController, UtenteController utenteController, LivroController livroController, EmprestimosController emprestimosController) {
        this.reservaController = reservaController;
        this.utenteController = utenteController;
        this.livroController = livroController;
        this.jornalController = jornalController;
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
                scanner.nextLine(); // Limpar buffer

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
                if (verificarItemEmprestado(item, dataInicio, dataFim)) {
                    System.out.println("Erro: O item '" + item.getTitulo() + "' está emprestado e não pode ser reservado.");
                    return;
                }
            }

            // Criar a reserva
            reservaController.criarReserva(utente, itensParaReserva, dataRegisto, dataInicio, dataFim, emprestimosController);
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

        // Obter o número da reserva
        System.out.print("Digite o número da reserva: ");
        int numeroReserva;
        try {
            numeroReserva = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
        } catch (InputMismatchException e) {
            System.out.println("Erro: Entrada inválida. Digite um número.");
            scanner.nextLine(); // Limpar buffer
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
            scanner.nextLine(); // Limpar buffer
        } catch (InputMismatchException e) {
            System.out.println("Erro: Opção inválida. Digite um número.");
            scanner.nextLine(); // Limpar buffer
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
                scanner.nextLine(); // Limpar buffer
                if (resposta == 'N') {
                    System.out.println("Operação cancelada.");
                    break;
                }
            }
        }
    }



    private void modificarItensReserva(Reserva reserva, LocalDate dataInicioReserva, LocalDate dataFimReserva) {
        System.out.println("\nO que você deseja fazer com os itens da reserva?");
        System.out.println("1. Adicionar livro");
        System.out.println("2. Remover livro");
        System.out.println("3. Adicionar jornal");
        System.out.println("4. Remover jornal");
        System.out.println("0. Cancelar");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        switch (opcao) {
            case 1 -> adicionarLivroNaReserva(reserva, dataInicioReserva, dataFimReserva);
            case 2 -> removerLivroDaReserva(reserva);
            case 3 -> adicionarJornalNaReserva(reserva, dataInicioReserva, dataFimReserva);
            case 4 -> removerJornalDaReserva(reserva);
            case 0 -> System.out.println("Operação cancelada.");
            default -> System.out.println("Opção inválida.");
        }
    }


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
                scanner.nextLine(); // Limpar buffer

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
                // Verificar se o livro já está na reserva sem usar streams
                boolean jaAdicionado = false;
                for (ItemEmprestavel item : reserva.getItens()) {
                    if (item instanceof Livro && item.equals(livro)) {
                        jaAdicionado = true;
                        break;
                    }
                }

                if (jaAdicionado) {
                    System.out.println("Erro: O livro '" + livro.getNome() + "' já está na reserva.");
                    livro = null; // Voltar ao loop para tentar novamente
                }
            }
        }

        // Adicionar o livro à reserva
        reservaController.adicionarItemNaReserva(reserva.getNumero(), livro, dataInicioReserva, dataFimReserva);
        System.out.println("Livro adicionado com sucesso à reserva.");
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

        // Verificar se o livro está na reserva sem usar streams
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
                scanner.nextLine(); // Limpar buffer

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
                boolean encontrado = false;
                for (ItemEmprestavel item : reserva.getItens()) {
                    if (item instanceof Jornal && item.equals(jornal)) {
                        encontrado = true;
                        break;
                    }
                }

                if (encontrado) {
                    System.out.println("Erro: O jornal '" + jornal.getTitulo() + "' já está na reserva.");
                    jornal = null;
                }
            }
        }

        reservaController.adicionarItemNaReserva(reserva.getNumero(), jornal, dataInicioReserva, dataFimReserva);
        System.out.println("Jornal adicionado com sucesso à reserva.");
    }

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

    private void removerReserva() {
        try {
            System.out.println("\n=== Remover Reserva ===");
            System.out.print("Número da Reserva: ");
            int numero = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            // Consultar a reserva
            Reserva reserva = reservaController.consultarReserva(numero);

            if (reserva == null) {
                System.out.println("Nenhuma reserva encontrada com o número informado.");
                return; // Encerra o método se a reserva não for encontrada
            }

            // Exibir detalhes da reserva antes de remover
            reservaController.exibirDetalhesReserva(reserva);

            // Confirmar remoção
            System.out.print("Tem certeza que deseja remover esta reserva? (S/N): ");
            char confirmacao = scanner.next().toUpperCase().charAt(0);
            scanner.nextLine(); // Limpar buffer

            if (confirmacao == 'S') {
                boolean sucesso =reservaController.removerReserva(numero);

                if (sucesso) {
                    System.out.println("Reserva removida com sucesso!");
                } else {
                    System.out.println("Erro ao tentar remover a reserva.");
                }
            } else {
                System.out.println("Operação cancelada.");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }


    public void listarReservas() {
        // Obtém a lista de reservas ativas do controlador
        List<Reserva> reservasAtivas = reservaController.listarTodasReservas();

        // Verifica se existem reservas registradas
        if (reservasAtivas == null || reservasAtivas.isEmpty()) {
            System.out.println("\nNenhuma reserva registrada.");
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

        // Exibir as reservas
        System.out.println("\n=== Lista de Reservas ===");
        System.out.printf("%-10s %-35s %-20s %-25s %-20s\n",
                "Número", "Utente", "Itens Reservados", "Data Registro", "Data Início");

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
            String dataRegistro = (reserva.getDataRegisto() != null)
                    ? reserva.getDataRegisto().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "Data desconhecida";

            String dataInicio = (reserva.getDataInicio() != null)
                    ? reserva.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "Pendente";

            // Exibe as informações da reserva
            System.out.printf("%-10d %-35s %-20s %-25s %-20s\n",
                    reserva.getNumero(), utenteNome, itens, dataRegistro, dataInicio);
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
                System.out.println("1. Adicionar Utente\n2. Tentar novamente\n0. Cancelar");
                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer

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

    private List<Jornal> obterJornais() {
        List<Jornal> jornais = new ArrayList<>();
        System.out.print("Quantos jornais deseja reservar? ");
        int qtdJornais = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < qtdJornais; i++) {
            Jornal jornal = null;
            while (jornal == null) {
                System.out.print("ISSN do Jornal " + (i + 1) + ": ");
                String nome = scanner.nextLine();
                jornal = jornalController.procurarPorIssn(jornal.getIssn());

                if (jornal == null) {
                    System.out.println("Erro: Jornal não encontrado. O que você deseja realizar?");
                    System.out.println("1. Adicionar Jornal");
                    System.out.println("2. Tentar novamente");
                    System.out.println("0. Cancelar");
                    System.out.print("Escolha uma opção: ");
                    int opcao = scanner.nextInt();
                    scanner.nextLine();

                    switch (opcao) {
                        case 1:
                            jornalController.criarJornal(jornal.getIssn(), jornal.getTitulo(), jornal.getCategoria(), jornal.getEditora(), jornal.getDataPublicacao());
                            jornal = jornalController.procurarPorIssn(jornal.getIssn());
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

    private boolean verificarItemEmprestado(ItemEmprestavel item, LocalDate dataInicio, LocalDate dataFim) {
        return emprestimosController.itemPossuiEmprestimoAtivo(item, dataInicio, dataFim);
    }


}
