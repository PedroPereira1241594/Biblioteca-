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
import java.util.stream.Collectors;

public class EmprestimosView {
    private EmprestimosController emprestimosController;
    private ReservaController reservaController;
    private UtenteController utenteController;
    private LivroController livroController;
    private JornalController jornalController;
    private JornalView jornalView;
    private Scanner scanner;

    public EmprestimosView(
                           EmprestimosController emprestimosController,
                           UtenteController utenteController,
                           LivroController livroController,
                           JornalController jornalController,
                           JornalView jornalView) {
        this.emprestimosController = emprestimosController;
        this.utenteController = utenteController;
        this.livroController = livroController;
        this.jornalController = jornalController;
        this.jornalView = jornalView;


        this.scanner = new Scanner(System.in);  // Inicializa o scanner
    }

    public void exibirMenuPrincipal() {
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
                case 5 -> emprestimosController.listarEmprestimos();
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

            // Inicialização das listas
            List<Livro> livros = new ArrayList<>();
            List<Jornal> jornais = new ArrayList<>();

            // Método para selecionar os itens
            selecionarItensParaEmprestimo(livros, jornais);

            // Solicitação das datas
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            System.out.print("Data de Início (dd/MM/yyyy): ");
            LocalDate dataInicio = lerData(formato);

            System.out.print("Data Prevista de Devolução (dd/MM/yyyy): ");
            LocalDate dataPrevistaDevolucao = lerData(formato);

            System.out.print("Data Efetiva de Devolução (dd/MM/yyyy): ");
            LocalDate dataEfetivaDevolucao = lerData(formato);

            // Verificar se algum livro/jornal está indisponível para empréstimo
            for (Livro livro : livros) {
                if (livroController.verificarLivroIndisponivel(livro, dataInicio, dataPrevistaDevolucao, dataEfetivaDevolucao)) {
                    System.out.println("Erro: O livro '" + livro.getNome() + "' já está emprestado e não pode ser incluído no empréstimo.");
                    return; // Impede a criação do empréstimo
                }
            }

            for (Jornal jornal : jornais) {
                if (jornalController.verificarJornalIndisponivel(jornal, dataInicio, dataPrevistaDevolucao, dataEfetivaDevolucao)) {
                    System.out.println("Erro: O jornal '" + jornal.getTitulo() + "' já está emprestado e não pode ser incluído no empréstimo.");
                    return; // Impede a criação do empréstimo
                }
            }
            // Criar o empréstimo
            emprestimosController.criarEmprestimo(utente, livros, jornais, dataInicio, dataPrevistaDevolucao, dataEfetivaDevolucao);

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

    private void selecionarItensParaEmprestimo(List<Livro> livros, List<Jornal> jornais ) {
        System.out.println("\nO que deseja reservar?");
        System.out.println("1. Livros");
        System.out.println("2. Jornais");
        System.out.println("0. Livros e Jornais");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        switch (opcao) {
            case 1:
                emprestarLivros(livros);
                break;
            case 2:
                emprestarJornais(jornais);
                break;
            case 0:
                emprestarLivros(livros);
                emprestarJornais(jornais);
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

    private void emprestarLivros(List<Livro> livros) {
        System.out.print("Quantos livros deseja emprestar? ");
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

    private void emprestarJornais(List<Jornal> jornais) {
        System.out.print("Quantos jornais deseja emprestar? ");
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
            System.out.println("Jornal adicionado com sucesso!");
        }
    }

    public void exibirEmprestimos(List<Emprestimos> emprestimos) {
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo registrado.");
        } else {
            System.out.println("\n=== Lista de Empréstimos ===");
            System.out.printf("%-10s %-25s %-20s %-20s %-25s %-20s %-20s\n",
                    "Número", "Utente", "Data Início", "Data Prevista", "Data Efetiva", "Livros Emprestados", "Jornais Emprestados");
            System.out.println("-----------------------------------------------------------------------------------------------------------");

            for (Emprestimos emprestimo : emprestimos) {
                String livrosEmprestados = emprestimo.getLivros().stream()
                        .map(livro -> livro.getNome()) // Supondo que "getNome" retorna o título do livro
                        .collect(Collectors.joining(", "));

                String jornaisEmprestados = emprestimo.getJornais().stream()
                        .map(jornal -> jornal.getTitulo()) // Supondo que "getTitulo" retorna o título do jornal
                        .collect(Collectors.joining(", "));

                System.out.printf("%-10d %-25s %-20s %-20s %-25s %-20s %-20s\n",
                        emprestimo.getNumero(),
                        emprestimo.getUtente().getNome(),
                        emprestimo.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        emprestimo.getDataPrevistaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        emprestimo.getDataEfetivaDevolucao() != null ? emprestimo.getDataEfetivaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Em aberto",
                        livrosEmprestados.isEmpty() ? "Nenhum" : livrosEmprestados,
                        jornaisEmprestados.isEmpty() ? "Nenhum" : jornaisEmprestados);
            }
        }
    }




































    private LocalDate lerData(DateTimeFormatter formato) {
        while (true) {
            try {
                String dataTexto = scanner.nextLine();
                return LocalDate.parse(dataTexto, formato);
            } catch (DateTimeParseException e) {
                System.out.print("Data inválida. \nInsira novamente (dd/MM/yyyy): ");
            }
        }
    }

    private void consultarEmprestimo() {
        System.out.println("\n=== Consultar Empréstimo ===");
        System.out.print("Número do Empréstimo: ");
        int numero = scanner.nextInt();
        scanner.nextLine();  // Limpar buffer

        // Chama o método consultarEmprestimo do controller
        Emprestimos emprestimo = emprestimosController.consultarEmprestimo(numero);

        // Se o empréstimo for encontrado, exibe os detalhes
        if (emprestimo != null) {
            emprestimosController.exibirDetalhesEmprestimo(emprestimo);
        } else {
            System.out.println("Empréstimo com o número " + numero + " não encontrado.");
        }
    }

    public void atualizarEmprestimo() {
        System.out.println("\n=== Atualizar Empréstimo ===");
        System.out.print("Número do Empréstimo: ");
        int numero = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        Emprestimos emprestimo = emprestimosController.buscarEmprestimoPorNumero(numero);
        if (emprestimo == null) {
            System.out.println("Erro: Empréstimo não encontrado.");
            return;
        }

        LocalDate dataInicio = emprestimo.getDataInicio();
        LocalDate dataEfetivaDevolucao = emprestimo.getDataEfetivaDevolucao();

        System.out.println("O que você deseja atualizar?");
        System.out.println("1. Atualizar as datas do empréstimo");
        System.out.println("2. Alterar livros do empréstimo");
        System.out.println("0. Cancelar");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        switch (opcao) {
            case 1:
                atualizarDatasEmprestimo(emprestimo, numero);
                break;
            case 2:
                modificarLivrosEmprestimo(emprestimo, dataInicio, dataEfetivaDevolucao);
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

        LocalDate novaDataEfetivaDevolucao = null;
        boolean dataValida = false;

        while (!dataValida) {
            System.out.print("Informe a nova data efetiva de devolução (dd/MM/yyyy): ");
            novaDataEfetivaDevolucao = lerData(formato);

            if (!emprestimosController.verificarDataAnterior(emprestimo.getDataInicio(), novaDataEfetivaDevolucao)) {
                System.out.println("Erro: A data efetiva de devolução não pode ser anterior à data de início do empréstimo.");
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

        emprestimosController.atualizarEmprestimo(numero, novaDataEfetivaDevolucao);
    }

    private void modificarLivrosEmprestimo(Emprestimos emprestimo, LocalDate dataInicio, LocalDate dataEfetivaDevolucao) {
        System.out.println("O que você deseja fazer com os livros do empréstimo?");
        System.out.println("1. Adicionar livro");
        System.out.println("2. Remover livro");
        System.out.println("0. Cancelar");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        switch (opcao) {
            case 1:
                adicionarLivroNoEmprestimo(emprestimo, dataInicio, dataEfetivaDevolucao);
                break;
            case 2:
                removerLivroDoEmprestimo(emprestimo);
                break;
            case 0:
                System.out.println("Operação cancelada.");
                return;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private void adicionarLivroNoEmprestimo(Emprestimos emprestimo, LocalDate dataInicio, LocalDate dataEfetivaDevolucao) {
        System.out.println("\n=== Adicionar Livro ao Empréstimo ===");

        System.out.print("Informe o ISBN do livro: ");
        String isbn = scanner.nextLine();

        Livro livro = livroController.buscarLivroPorIsbn(isbn);
        if (livro == null) {
            System.out.println("Erro: Livro com ISBN '" + isbn + "' não encontrado.");
            return;
        }

        if (emprestimo.getLivros().contains(livro)) {
            System.out.println("Erro: O livro '" + livro.getNome() + "' já está neste empréstimo.");
            return;
        }

        // Adicionar o livro ao empréstimo
        emprestimosController.adicionarLivroNoEmprestimo(emprestimo, livro, dataInicio, dataEfetivaDevolucao);
    }

    private void removerLivroDoEmprestimo(Emprestimos emprestimo) {
        System.out.println("\n=== Remover Livro do Empréstimo ===");

        System.out.print("Informe o ISBN do livro a ser removido: ");
        String isbn = scanner.nextLine();

        Livro livro = livroController.buscarLivroPorIsbn(isbn);
        if (livro == null) {
            System.out.println("Erro: Livro com ISBN '" + isbn + "' não encontrado.");
            return;
        }

        if (!emprestimo.getLivros().contains(livro)) {
            System.out.println("Erro: O livro '" + livro.getNome() + "' não está neste empréstimo.");
            return;
        }

        // Remover o livro do empréstimo
        emprestimosController.removerLivroDoEmprestimo(emprestimo, livro);
    }

    private void removerEmprestimo() {
        System.out.println("\n=== Remover Empréstimo ===");
        System.out.print("Número do Empréstimo: ");
        int numero = scanner.nextInt();
        scanner.nextLine();

        emprestimosController.removerEmprestimo(numero);
    }

    /*public void listarEmprestimos() {
        List<Emprestimos> emprestimosAtivos = emprestimosController.listarEmprestimosAtivos();

        if (emprestimosAtivos.isEmpty()) {
            System.out.println("\nNenhum empréstimo registado.");
        } else {
            System.out.println("\n=== Lista de Empréstimos ===");
            System.out.printf("%-10s %-35s %-20s %-25s %-70s %-25s\n",
                    "Número", "Utente", "Data Início", "Data Prev. Devolução", "Livros Emprestados", "Data Devolução");

            for (Emprestimos emprestimo : emprestimosAtivos) {
                String livros = "";
                for (Livro livro : emprestimo.getLivros()) {
                    livros += livro.getNome() + " (ISBN: " + livro.getIsbn() + "), ";
                }
                if (!livros.isEmpty()) {
                    livros = livros.substring(0, livros.length() - 2);
                }

                System.out.printf("%-10d %-35s %-20s %-25s %-70s %-25s\n",
                        emprestimo.getNumero(),
                        emprestimo.getUtente().getNome(),
                        emprestimo.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        emprestimo.getDataPrevistaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        livros,
                        emprestimo.getDataEfetivaDevolucao() != null ? emprestimo.getDataEfetivaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Em aberto");
            }
        }
    }*/





}
