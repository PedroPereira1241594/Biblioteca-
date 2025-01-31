package View;

import Controller.JornalController;
import Model.ItemEmprestavel;
import Model.Jornal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import static Controller.JornalController.jornais;

/**
 * Classe responsável por gerir a interação com o utilizador para operações relacionadas a Jornais/Revistas.
 * <p>
 * Esta classe permite realizar as seguintes operações:
 * <ul>
 *     <li>Criar novos jornais/revistas</li>
 *     <li>Procurar jornais/revistas por ISSN</li>
 *     <li>Atualizar informações de jornais/revistas</li>
 *     <li>Eliminar jornais/revistas</li>
 *     <li>Listar todos os jornais/revistas registados</li>
 * </ul>
 */
public class JornalView {
    private ArrayList<Jornal> jornals;
    private JornalController jornalController;
    private Scanner scanner;

    /**
     * Construtor para inicializar a classe {@code JornalView}.
     *
     * @param jornalController O controlador responsável pelas operações relacionadas a jornais/revistas.
     * @param jornals          A lista de jornais/revistas disponíveis.
     */
    public JornalView(JornalController jornalController, ArrayList<Jornal> jornals) {
        this.jornalController = jornalController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Mostra o menu de opções para gestão de jornais/revistas e executa a operação escolhida pelo utilizador.
     */
    public void exibirMenu() {
        int opcao;

        do {
            System.out.println("\nMenu:");
            System.out.println("1. Criar Jornal/Revista");
            System.out.println("2. Procurar Jornal/Revista por ISSN");
            System.out.println("3. Atualizar Jornal/Revista");
            System.out.println("4. Eliminar Jornal/Revista");
            System.out.println("5. Listar Jornais/Revistas");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    criarJornal();
                    break;
                case 2:
                    procurarJornalPorIssn();
                    break;
                case 3:
                    atualizarJornal();
                    break;
                case 4:
                    eliminarJornal();
                    break;
                case 5:
                    listarJornais();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opcao != 0);
    }

    /**
     * Permite criar um novo jornal/revista com as informações fornecidas pelo utilizador.
     */
    public void criarJornal() {
        System.out.println("Insira os dados do novo Jornal/Revista:");

        System.out.print("ISSN: ");
        String issn = scanner.nextLine();
        for (ItemEmprestavel item : jornais) {
            if (item instanceof Jornal && ((Jornal) item).getIssn().equals(issn)) {
                System.out.println("Já Existe um Jornal/Revista com o Mesmo ISSN");
                return;
            }
        }

        System.out.print("Título: ");
        String titulo = scanner.nextLine();

        System.out.print("Categoria: ");
        String categoria = scanner.nextLine();

        System.out.print("Editora: ");
        String editora = scanner.nextLine();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.print("Data de Publicação (dd/MM/yyyy): ");
        LocalDate dataPublicacao = lerData(formato);

        jornalController.criarJornal(issn, titulo, categoria, editora, dataPublicacao);
    }

    /**
     * Lê uma data fornecida pelo utilizador no formato especificado.
     *
     * @param formato O formato esperado da data.
     * @return A data lida como um objeto {@code LocalDate}.
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
     * Lista todos os jornais/revistas disponíveis.
     */
    public void listarJornais() {
        jornalController.listarJornais();
    }

    /**
     * Permite atualizar as informações de um jornal/revista existente.
     */
    public void atualizarJornal() {
        System.out.print("Introduza o ISSN do jornal/revista a ser atualizado: ");
        String issn = scanner.nextLine();

        System.out.println("Introduza os novos dados:");
        System.out.print("Novo ISSN: ");
        String novoIssn = scanner.nextLine();
        for (ItemEmprestavel item : jornais) {
            if (item instanceof Jornal && ((Jornal) item).getIssn().equals(novoIssn)) {
                System.out.println("Já Existe um Jornal/Revista com o Mesmo ISSN");
                return;
            }
        }

        System.out.print("Novo Título: ");
        String titulo = scanner.nextLine();

        System.out.print("Nova Categoria: ");
        String categoria = scanner.nextLine();

        System.out.print("Nova Editora: ");
        String editora = scanner.nextLine();

        System.out.print("Nova Data de Publicação (dd/MM/yyyy): ");
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataPublicacao = lerData(formato);

        jornalController.atualizarJornal(issn, novoIssn, titulo, categoria, editora, dataPublicacao);
    }

    /**
     * Permite eliminar um jornal/revista a partir do ISSN fornecido pelo utilizador.
     */
    public void eliminarJornal() {
        System.out.print("Introduza o ISSN do jornal/revista a ser eliminado: ");
        String issn = scanner.nextLine();

        jornalController.eliminarJornal(issn);
    }

    /**
     * Permite procurar um jornal/revista pelo ISSN e mostrar os detalhes, se encontrado.
     */
    public void procurarJornalPorIssn() {
        System.out.print("\nIntroduza o ISSN do jornal/revista que pretende procurar: ");
        String issn = scanner.nextLine();

        var jornal = jornalController.procurarPorIssn(issn);
        if (jornal != null) {
            System.out.println("\n======= Detalhes do Jornal/Revista ========");
            System.out.println("Título: " + jornal.getTitulo());
            System.out.println("Editora: " + jornal.getEditora());
            System.out.println("Categoria: " + jornal.getCategoria());
            System.out.println("ISSN: " + jornal.getIssn());
            System.out.println("Data de Publicação: " + jornal.getDataPublicacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("=".repeat(43));
        } else {
            System.out.println("\nJornal não encontrado.");
            System.out.println("Certifique-se de que o ISSN introduzido está correto.");
        }
    }

}
