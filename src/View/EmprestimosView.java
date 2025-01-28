package View;

import Controller.EmprestimosController;
import Controller.JornalController;
import Controller.LivroController;
import Controller.UtenteController;
import Model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class EmprestimosView {
    private EmprestimosController emprestimosController;
    private UtenteController utenteController;
    private LivroController livroController;
    private JornalController jornalController;
    private Scanner scanner;

    public EmprestimosView(EmprestimosController emprestimosController, UtenteController utenteController, LivroController livroController, JornalController jornalController) {
        this.emprestimosController = emprestimosController;
        this.utenteController = utenteController;
        this.livroController = livroController;
        this.jornalController = jornalController;
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenu() {
        int opcao;
        do {
            System.out.println("\n=== Menu de Empréstimos ===");
            System.out.println("1. Criar Empréstimo");
            System.out.println("2. Consultar Empréstimo");
            System.out.println("3. Atualizar Empréstimo");
            System.out.println("4. Remover Empréstimo");
            System.out.println("5. Listar Todos os Empréstimos");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            switch (opcao) {
                case 1 -> criarEmprestimo();
                case 2 -> consultarEmprestimo();
                case 3 -> atualizarEmprestimo();
                case 4 -> removerEmprestimo();
                case 5 -> listarEmprestimos();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void criarEmprestimo() {
        try {
            System.out.println("\n=== Criar Empréstimo ===");

            // Seleção do utente
            Utentes utente = obterUtente();

            if (utente == null) {
                System.out.println("Operação cancelada.");
                return; // Se o utente não for encontrado e o usuário cancelar, a operação é interrompida.
            }

            List<ItemEmprestavel> itensParaEmprestimo = new ArrayList<>(); // Lista para armazenar todos os itens selecionados

            // Loop para adicionar itens ao empréstimo
            while (true) {
                // Exibir menu de escolha de item a ser emprestado
                System.out.println("\nMenu:");
                System.out.println("1. Adicionar Livro ao empréstimo");
                System.out.println("2. Adicionar Jornal/Revista ao empréstimo");
                System.out.println("0. Finalizar seleção de itens");
                System.out.print("Escolha uma opção: ");
                int opcaoItem = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer

                if (opcaoItem == 0) {
                    break; // Finaliza a seleção de itens
                }

                switch (opcaoItem) {
                    case 1:
                        // Seleção dos livros
                        List<Livro> livrosParaEmprestimo = obterLivros();
                        itensParaEmprestimo.addAll(livrosParaEmprestimo); // Adiciona todos os livros selecionados à lista
                        break;

                    case 2:
                        // Seleção dos jornais
                        List<Jornal> jornaisParaEmprestimo = obterJornais();
                        itensParaEmprestimo.addAll(jornaisParaEmprestimo); // Adiciona todos os jornais selecionados à lista
                        break;

                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            }

            if (itensParaEmprestimo.isEmpty()) {
                System.out.println("Nenhum item selecionado. Operação cancelada.");
                return;
            }

            // Solicitação das datas
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataInicio = obterData("Data de Início (dd/MM/yyyy): ", formato);
            LocalDate dataPrevistaDevolucao = obterData("Data Prevista de Devolução (dd/MM/yyyy): ", formato);

            while (!emprestimosController.verificarDataAnterior(dataInicio, dataPrevistaDevolucao)) {
                System.out.println("Erro: A data prevista de devolução não pode ser anterior à data de início.");
                dataPrevistaDevolucao = obterData("Informe a data prevista de devolução (dd/MM/yyyy): ", formato);
            }

            // Solicitar a data efetiva de devolução (se necessário)
            LocalDate dataEfetivaDevolucao = obterDataEfetivaDevolucao(dataInicio, formato);

            // Criação do empréstimo
            emprestimosController.criarEmprestimo(utente, itensParaEmprestimo, dataInicio, dataPrevistaDevolucao, dataEfetivaDevolucao);

            System.out.println("Empréstimo criado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao criar empréstimo: " + e.getMessage());
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
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        utenteController.adicionarUtente();
                        utente = utenteController.buscarUtentePorNif(nif);  // Buscar o utente novamente
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

    private List<Livro> obterLivros() {
        List<Livro> livrosParaEmprestimo = new ArrayList<>();
        System.out.print("Quantos livros vai requisitar? ");
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
                    scanner.nextLine();

                    switch (opcao) {
                        case 1:
                            livroController.adicionarLivro();
                            livro = livroController.buscarLivroPorIsbn(isbn);  // Buscar o livro novamente após adicionar
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
                } else if (livrosParaEmprestimo.contains(livro)) {
                    System.out.println("Erro: O livro '" + livro.getNome() + "' já foi adicionado a este empréstimo.");
                    livro = null;  // Se o livro já foi adicionado, pedimos para o usuário tentar novamente
                }
            }
            livrosParaEmprestimo.add(livro);  // Adiciona o livro encontrado ou recém-adicionado à lista
        }
        return livrosParaEmprestimo;
    }

    private List<Jornal> obterJornais() {
        List<Jornal> jornaisParaEmprestimo = new ArrayList<>();
        System.out.print("Quantos jornais vai requisitar? ");
        int qtdJornais = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < qtdJornais; i++) {
            Jornal jornal = null;
            while (jornal == null) {
                System.out.print("ISSN do Jornal " + (i + 1) + ": ");
                String issn = scanner.nextLine();
                jornal = jornalController.procurarPorIssn(issn);  // Busca o jornal pelo ISSN

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
                            // Caso o usuário queira adicionar um novo jornal
                            System.out.println("Adicionando um novo jornal...");
                            System.out.print("Informe o ISSN: ");
                            String novoIssn = scanner.nextLine();
                            System.out.print("Informe o título: ");
                            String titulo = scanner.nextLine();
                            System.out.print("Informe a categoria: ");
                            String categoria = scanner.nextLine();
                            System.out.print("Informe a editora: ");
                            String editora = scanner.nextLine();
                            System.out.print("Informe a data de publicação (dd/MM/yyyy): ");
                            String dataPublicacao = scanner.nextLine();
                            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            LocalDate data = LocalDate.parse(dataPublicacao, formato);
                            jornalController.criarJornal(novoIssn, titulo, categoria, editora, data);
                            jornal = jornalController.procurarPorIssn(novoIssn);  // Busca o jornal novamente após adicionar
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
                } else if (jornaisParaEmprestimo.contains(jornal)) {
                    System.out.println("Erro: O jornal '" + jornal.getTitulo() + "' já foi adicionado a este empréstimo.");
                    jornal = null;  // Se o jornal já foi adicionado, pedimos para o usuário tentar novamente
                }
            }
            jornaisParaEmprestimo.add(jornal);  // Adiciona o jornal encontrado ou recém-adicionado à lista
        }
        return jornaisParaEmprestimo;
    }

    private LocalDate obterData(String mensagem, DateTimeFormatter formato) {
        System.out.print(mensagem);
        return lerData(formato);
    }

    private LocalDate obterDataEfetivaDevolucao(LocalDate dataInicio, DateTimeFormatter formato) {
        LocalDate dataEfetivaDevolucao = null;
        System.out.print("Deseja adicionar a data efetiva de devolução? (S/N): ");
        char confirmacao = scanner.next().toUpperCase().charAt(0);
        scanner.nextLine(); // Limpar o buffer antes de ler a próxima entrada

        if (confirmacao == 'S') {
            System.out.print("Data Efetiva de Devolução (dd/MM/yyyy): ");
            dataEfetivaDevolucao = lerData(formato);

            while (!emprestimosController.verificarDataAnterior(dataInicio, dataEfetivaDevolucao)) {
                System.out.println("Erro: A data efetiva de devolução não pode ser anterior à data de início.");
                System.out.print("Informe a data efetiva de devolução (dd/MM/yyyy): ");
                dataEfetivaDevolucao = lerData(formato);
            }
        }
        return dataEfetivaDevolucao;
    }

    private LocalDate lerData(DateTimeFormatter formato) {
        while (true) {
            try {
                String dataTexto = scanner.nextLine();
                return LocalDate.parse(dataTexto, formato);
            } catch (DateTimeParseException e) {
                System.out.print("Formato de data inválido. Tente novamente (dd/MM/yyyy): ");
            }
        }
    }

    private void consultarEmprestimo() {
        System.out.println("\n=== Consultar Empréstimo ===");
        int numero = obterNumeroEmprestimo();

        Emprestimos emprestimo = emprestimosController.consultarEmprestimo(numero);

        if (emprestimo != null) {
            emprestimosController.exibirDetalhesEmprestimo(emprestimo);
        } else {
            System.out.println("Empréstimo com o número " + numero + " não encontrado.");
        }
    }

    private int obterNumeroEmprestimo() {
        System.out.print("Número do Empréstimo: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, insira um número.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public void atualizarEmprestimo() {
        System.out.println("\n=== Atualizar Empréstimo ===");

        // Obter o número do empréstimo
        System.out.print("Digite o número do empréstimo: ");
        int numeroEmprestimo = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        // Consultar o empréstimo pelo número
        Emprestimos emprestimo = emprestimosController.consultarEmprestimo(numeroEmprestimo);

        // Validar se o empréstimo foi encontrado
        if (emprestimo == null) {
            System.out.println("Erro: Empréstimo não encontrado.");
            return;
        }

        // Exibir opções de atualização
        System.out.println("O que você deseja atualizar?");
        System.out.println("1. Atualizar as datas do empréstimo");
        System.out.println("2. Alterar itens do empréstimo");
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
                atualizarDatasEmprestimo(emprestimo, numeroEmprestimo);
                break;
            case 2:
                modificarItensEmprestimo(emprestimo);
                break;
            case 0:
                System.out.println("Operação cancelada.");
                return;
            default:
                System.out.println("Opção inválida.");
        }
    }


    private void atualizarDatasEmprestimo(Emprestimos emprestimo, int numero) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate novaDataEfetivaDevolucao;

        while (true) {
            System.out.print("Informe a nova data efetiva de devolução (dd/MM/yyyy): ");
            novaDataEfetivaDevolucao = lerData(formato);

            if (emprestimosController.verificarDataAnterior(emprestimo.getDataInicio(), novaDataEfetivaDevolucao)) {
                emprestimosController.atualizarEmprestimo(numero, novaDataEfetivaDevolucao);
                System.out.println("Data efetiva de devolução atualizada com sucesso.");
                break;
            } else {
                System.out.println("Erro: A data efetiva de devolução não pode ser anterior à data de início do empréstimo.");
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

    private void modificarItensEmprestimo(Emprestimos emprestimo) {
        System.out.println("\nO que você deseja fazer com os itens do empréstimo?");
        System.out.println("1. Adicionar livro");
        System.out.println("2. Remover livro");
        System.out.println("3. Adicionar jornal");
        System.out.println("4. Remover jornal");
        System.out.println("0. Cancelar");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        switch (opcao) {
            case 1 -> adicionarLivroAoEmprestimo(emprestimo);
            case 2 -> removerLivroDoEmprestimo(emprestimo);
            case 3 -> adicionarJornalAoEmprestimo(emprestimo);
            case 4 -> removerJornalDoEmprestimo(emprestimo);
            case 0 -> System.out.println("Operação cancelada.");
            default -> System.out.println("Opção inválida.");
        }
    }

    private void adicionarLivroAoEmprestimo(Emprestimos emprestimo) {
        Livro livro = null;
        while (livro == null) {
            System.out.print("ISBN do livro a adicionar: ");
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
                    case 1 -> {
                        livroController.adicionarLivro();
                        livro = livroController.buscarLivroPorIsbn(isbn);
                    }
                    case 2 -> System.out.println("Tente novamente...");
                    case 0 -> {
                        System.out.println("Operação cancelada.");
                        return;
                    }
                    default -> System.out.println("Opção inválida!");
                }
            } else if (emprestimo.getItens().contains(livro)) {
                System.out.println("Erro: O livro '" + livro.getNome() + "' já está no empréstimo.");
                livro = null;
            }
        }

        emprestimosController.adicionarItemEmprestimo(emprestimo.getNumero(), livro);
        System.out.println("Livro adicionado com sucesso.");
    }

    private void removerLivroDoEmprestimo(Emprestimos emprestimo) {
        System.out.print("ISBN do livro a remover: ");
        String isbn = scanner.nextLine();
        Livro livro = livroController.buscarLivroPorIsbn(isbn);

        if (livro != null && emprestimo.getItens().contains(livro)) {
            emprestimosController.removerItemEmprestimo(emprestimo.getNumero(), livro);
            System.out.println("Livro removido com sucesso.");
        } else {
            System.out.println("Erro: Livro não encontrado no empréstimo.");
        }
    }

    private void adicionarJornalAoEmprestimo(Emprestimos emprestimo) {
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
            } else if (emprestimo.getItens().contains(jornal)) {
                System.out.println("Erro: O jornal '" + jornal.getTitulo() + "' já está no empréstimo.");
                jornal = null;
            }
        }

        emprestimosController.adicionarItemEmprestimo(emprestimo.getNumero(), jornal);
        System.out.println("Jornal adicionado com sucesso.");
    }

    private void removerJornalDoEmprestimo(Emprestimos emprestimo) {
        System.out.print("ISSN do jornal a remover: ");
        String issn = scanner.nextLine();
        Jornal jornal = jornalController.procurarPorIssn(issn);

        if (jornal != null && emprestimo.getItens().contains(jornal)) {
            emprestimosController.removerItemEmprestimo(emprestimo.getNumero(), jornal);
            System.out.println("Jornal removido com sucesso.");
        } else {
            System.out.println("Erro: Jornal não encontrado no empréstimo.");
        }
    }

    public void removerEmprestimo() {
        try {
            System.out.print("Insira o número do empréstimo que deseja remover: ");
            int numeroEmprestimo = scanner.nextInt();

            // Consultar o empréstimo
            Emprestimos emprestimo = emprestimosController.consultarEmprestimo(numeroEmprestimo);

            if (emprestimo == null) {
                System.out.println("Nenhum empréstimo encontrado com o número informado.");
                return; // Encerra o método se o empréstimo não for encontrado
            }

            // Exibir detalhes do empréstimo antes de remover
            emprestimosController.exibirDetalhesEmprestimo(emprestimo);

            // Remover o empréstimo
            boolean sucesso = emprestimosController.removerEmprestimo(numeroEmprestimo);

            if (sucesso) {
                System.out.println("Empréstimo removido com sucesso!");
            } else {
                System.out.println("Erro ao tentar remover o empréstimo.");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void listarEmprestimos() {
        // Obtém a lista de empréstimos ativos do controlador
        List<Emprestimos> emprestimosAtivos = emprestimosController.listarTodosEmprestimos();

        // Verifica se existem empréstimos registrados
        if (emprestimosAtivos == null || emprestimosAtivos.isEmpty()) {
            System.out.println("\nNenhum empréstimo registrado.");
            return;
        }

        // Remover duplicados manualmente
        List<Emprestimos> emprestimosSemDuplicados = new ArrayList<>();
        for (Emprestimos emprestimo : emprestimosAtivos) {
            boolean jaExiste = false;
            for (Emprestimos e : emprestimosSemDuplicados) {
                if (e.getNumero() == emprestimo.getNumero()) {
                    jaExiste = true;
                    break;
                }
            }
            if (!jaExiste) {
                emprestimosSemDuplicados.add(emprestimo);
            }
        }

        // Ordenar a lista manualmente usando Bubble Sort
        for (int i = 0; i < emprestimosSemDuplicados.size() - 1; i++) {
            for (int j = 0; j < emprestimosSemDuplicados.size() - i - 1; j++) {
                if (emprestimosSemDuplicados.get(j).getNumero() > emprestimosSemDuplicados.get(j + 1).getNumero()) {
                    Emprestimos temp = emprestimosSemDuplicados.get(j);
                    emprestimosSemDuplicados.set(j, emprestimosSemDuplicados.get(j + 1));
                    emprestimosSemDuplicados.set(j + 1, temp);
                }
            }
        }

        // Exibir os empréstimos
        System.out.println("\n=== Lista de Empréstimos ===");
        System.out.printf("%-10s %-35s %-20s %-25s %-20s\n",
                "Número", "Utente", "Itens Emprestados", "Data Início", "Data Efetiva");

        for (Emprestimos emprestimo : emprestimosSemDuplicados) {
            // Validação do nome do utente
            String utenteNome = (emprestimo.getUtente() != null && emprestimo.getUtente().getNome() != null)
                    ? emprestimo.getUtente().getNome()
                    : "Desconhecido";

            // Validação e construção da string de itens
            String itens = "Sem itens"; // Valor padrão
            if (emprestimo.getItens() != null && !emprestimo.getItens().isEmpty()) {
                List<ItemEmprestavel> itensEmprestados = emprestimo.getItens();
                itens = ""; // Inicializa vazio
                for (int i = 0; i < itensEmprestados.size(); i++) {
                    ItemEmprestavel item = itensEmprestados.get(i);
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
                    if (i < itensEmprestados.size() - 1) {
                        itens += ", ";
                    }
                }
            }

            // Validação das datas
            String dataInicio = (emprestimo.getDataInicio() != null)
                    ? emprestimo.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "Data desconhecida";

            String dataEfetiva = (emprestimo.getDataEfetivaDevolucao() != null)
                    ? emprestimo.getDataEfetivaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "Pendente";

            // Exibe as informações do empréstimo
            System.out.printf("%-10d %-35s %-20s %-25s %-20s\n",
                    emprestimo.getNumero(), utenteNome, itens, dataInicio, dataEfetiva);
        }
    }





}
