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

public class JornalView {
    private ArrayList<Jornal> jornals;
    private JornalController jornalController;
    private Scanner scanner;

    public JornalView(JornalController jornalController, ArrayList<Jornal> jornals) {
        this.jornalController = jornalController;
        this.scanner = new Scanner(System.in);
    }

    // Método para exibir o menu de opções
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

    // Método para criar um novo jornal
    public void criarJornal() {
        System.out.println("Insira os dados do novo Jornal/Revista:");

        System.out.print("ISSN: ");
        String issn = scanner.nextLine();
        for (ItemEmprestavel item : jornais) {
            if (item instanceof Jornal && ((Jornal) item).getIssn().equals(issn)) {
                System.out.println("Já Existe um Jornal/Revista com o Mesmo ISBN");
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

    // Método para listar todos os jornais
    public void listarJornais() {
        jornalController.listarJornais();
    }

    // Método para atualizar um jornal
    public void atualizarJornal() {
        System.out.print("Introduza o ISSN do jornal/revista a ser atualizado: ");
        String issn = scanner.nextLine();

        System.out.println("Introduza os novos dados (deixe em branco para manter o valor atual):");
        System.out.print("Novo ISSN: ");
        String novoIssn = scanner.nextLine();

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

    // Método para eliminar um jornal
    public void eliminarJornal() {
        System.out.print("Introduza o ISSN do jornal/revista a ser eliminado: ");
        String issn = scanner.nextLine();

        jornalController.eliminarJornal(issn);
    }

    // Método para procurar um jornal por ISSN
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
            System.out.println("Data de Publicação: " + jornal.getDataPublicacao());
            System.out.println("=".repeat(43));
        } else {
            System.out.println("\nJornal não encontrado.");
            System.out.println("Certifique-se de que o ISSN introduzido está correto.");
        }
    }

}
