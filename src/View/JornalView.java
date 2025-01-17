package View;

import Controller.JornalController;
import java.util.Scanner;

public class JornalView {
    private JornalController jornalController;
    private Scanner scanner;

    public JornalView() {
        this.jornalController = new JornalController();
        this.scanner = new Scanner(System.in);
    }

    // Exibe o menu de opções
    public void exibirMenu() {
        int opcao;

        do {
            System.out.println("\nMenu:");
            System.out.println("1 - Criar Jornal");
            System.out.println("2 - Listar Jornais");
            System.out.println("3 - Atualizar Jornal");
            System.out.println("4 - Deletar Jornal");
            System.out.println("5 - Buscar Jornal por ISSN");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    criarJornal();
                    break;
                case 2:
                    listarJornais();
                    break;
                case 3:
                    atualizarJornal();
                    break;
                case 4:
                    deletarJornal();
                    break;
                case 5:
                    buscarJornalPorIssn();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    // Método para criar um novo jornal
    public void criarJornal() {
        System.out.println("Insira os dados do novo jornal/revista:");

        System.out.print("Título: ");
        String titulo = scanner.nextLine();

        System.out.print("Editora: ");
        String editora = scanner.nextLine();

        System.out.print("Categoria: ");
        String categoria = scanner.nextLine();

        System.out.print("ISSN: ");
        String issn = scanner.nextLine();

        System.out.print("Data de Publicação: ");
        String dataPublicacao = scanner.nextLine();

        jornalController.criarJornal(titulo, editora, categoria, issn, dataPublicacao);
    }

    // Método para listar todos os jornais
    public void listarJornais() {
        jornalController.listarJornais();
    }

    // Método para atualizar um jornal
    public void atualizarJornal() {
        System.out.print("Digite o ISSN do jornal/revista a ser atualizado: ");
        String issn = scanner.nextLine();

        System.out.println("Digite os novos dados (deixe em branco para manter o valor atual):");
        System.out.print("Novo Título: ");
        String titulo = scanner.nextLine();

        System.out.print("Nova Editora: ");
        String editora = scanner.nextLine();

        System.out.print("Nova Categoria: ");
        String categoria = scanner.nextLine();

        System.out.print("Novo ISSN: ");
        String novoIssn = scanner.nextLine();

        System.out.print("Nova Data de Publicação: ");
        String dataPublicacao = scanner.nextLine();

        jornalController.atualizarJornal(issn, titulo, editora, categoria, novoIssn, dataPublicacao);
    }

    // Método para deletar um jornal
    public void deletarJornal() {
        System.out.print("Digite o ISSN do jornal/revista a ser deletado: ");
        String issn = scanner.nextLine();

        jornalController.deletarJornal(issn);
    }

    // Método para buscar um jornal por ISSN
    public void buscarJornalPorIssn() {
        System.out.print("Digite o ISSN do jornal/revista a ser buscado: ");
        String issn = scanner.nextLine();

        var jornal = jornalController.buscarPorIssn(issn);
        if (jornal != null) {
            System.out.println("Jornal encontrado: " + jornal);
        } else {
            System.out.println("Jornal não encontrado.");
        }
    }
}
