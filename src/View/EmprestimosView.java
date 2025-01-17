package View;

import Controller.EmprestimosController;
import Model.Emprestimos;
import Model.Livro;
import Model.Utentes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmprestimosView {
    private EmprestimosController emprestimosController;
    private Scanner scanner;

    public EmprestimosView() {
        this.emprestimosController = new EmprestimosController();
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
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

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
        System.out.println("\n=== Criar Empréstimo ===");
        System.out.print("Número do Empréstimo: ");
        int numero = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        System.out.print("Nome do Utente: ");
        String nomeUtente = scanner.nextLine();
        System.out.print("NIF do Utente: ");
        int nif = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Gênero do Utente (1 para Masculino, 0 para Feminino): ");
        boolean genero = scanner.nextInt() == 1;
        scanner.nextLine();
        System.out.print("Contato do Utente: ");
        int contacto = scanner.nextInt();
        scanner.nextLine();

        //Utentes utente = new Utentes(nomeUtente, nif, genero, contacto);

        List<String> titulosLivros = new ArrayList<>();
        System.out.print("Quantos livros no empréstimo? ");
        int qtdLivros = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < qtdLivros; i++) {
            System.out.print("Título do Livro " + (i + 1) + ": ");
            String titulo = scanner.nextLine();
            titulosLivros.add(titulo);
        }

        System.out.print("Data de Início (dd/MM/yyyy): ");
        String dataInicio = scanner.nextLine();
        System.out.print("Data Prevista de Devolução (dd/MM/yyyy): ");
        String dataPrevistaDevolucao = scanner.nextLine();

        //emprestimosController.criarEmprestimo(numero, utente, titulosLivros, dataInicio, dataPrevistaDevolucao);
    }


    private void consultarEmprestimo() {
        System.out.println("\n=== Consultar Empréstimo ===");
        System.out.print("Número do Empréstimo: ");
        int numero = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer

        Emprestimos emprestimo = emprestimosController.consultarEmprestimo(numero);
        if (emprestimo != null) {
            // Exibe os detalhes do empréstimo
            System.out.println("\nDetalhes do Empréstimo:");
            System.out.println("Número do Empréstimo: " + emprestimo.getNumero());
            System.out.println("Utente: " + emprestimo.getUtente().getNome());
            System.out.println("Data Início: " + emprestimo.getDataInicio());
            System.out.println("Data Prevista de Devolução: " + emprestimo.getDataPrevistaDevolucao());

            // Exibe a Data Efetiva de Devolução, se disponível
            if (emprestimo.getDataEfetivaDevolucao() != null) {
                System.out.println("Data Efetiva de Devolução: " + emprestimo.getDataEfetivaDevolucao());
            } else {
                System.out.println("Data Efetiva de Devolução: Pendente");
            }

            // Exibe os livros emprestados de forma detalhada
            System.out.println("\nLivros Emprestados:");
            for (Livro livro : emprestimo.getLivros()) {
                System.out.println("  Título: " + livro.getNome());
                System.out.println("  Editora: " + livro.getEditora());
                System.out.println("  Categoria: " + livro.getCategoria());
                System.out.println("  Ano: " + livro.getAno());
                System.out.println("  Autor: " + livro.getAutor());
                System.out.println("-------------------------------");
            }
        } else {
            System.out.println("Empréstimo não encontrado.");
        }
    }

    private void atualizarEmprestimo() {
        System.out.println("\n=== Atualizar Empréstimo ===");
        System.out.print("Número do Empréstimo: ");
        int numero = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer

        System.out.print("O livro foi devolvido? (s/n): ");
        char resposta = scanner.nextLine().toLowerCase().charAt(0);

        if (resposta == 's') {
            emprestimosController.registrarDevolucao(numero); // Chama o método de devolução
        } else {
            System.out.print("Nova Data Prevista de Devolução (dd/MM/yyyy): ");
            String novaDataDevolucao = scanner.nextLine();
            emprestimosController.atualizarEmprestimo(numero, novaDataDevolucao);
        }


    }

    private void removerEmprestimo() {
        System.out.println("\n=== Remover Empréstimo ===");
        System.out.print("Número do Empréstimo: ");
        int numero = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer

        emprestimosController.removerEmprestimo(numero);
    }

    private void listarEmprestimos() {
        System.out.println("\n=== Lista de Empréstimos ===");
        List<Emprestimos> emprestimos = emprestimosController.listarEmprestimos();
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo registrado.");
        } else {
            for (Emprestimos emprestimo : emprestimos) {
                // Exibe o número do empréstimo e o utente
                System.out.println("Número do Empréstimo: " + emprestimo.getNumero());
                System.out.println("Utente: " + emprestimo.getUtente().getNome());
                System.out.println("Data Início: " + emprestimo.getDataInicio());
                System.out.println("Data Prevista de Devolução: " + emprestimo.getDataPrevistaDevolucao());

                // Exibe a Data Efetiva de Devolução, se disponível
                if (emprestimo.getDataEfetivaDevolucao() != null) {
                    System.out.println("Data Efetiva de Devolução: " + emprestimo.getDataEfetivaDevolucao());
                } else {
                    System.out.println("Data Efetiva de Devolução: Pendente");
                }

                // Exibe os livros emprestados de forma detalhada
                System.out.println("Livros Emprestados:");
                for (Livro livro : emprestimo.getLivros()) {
                    System.out.println("  Título: " + livro.getNome());
                    System.out.println("  Editora: " + livro.getEditora());
                    System.out.println("  Categoria: " + livro.getCategoria());
                    System.out.println("  Ano: " + livro.getAno());
                    System.out.println("  Autor: " + livro.getAutor());
                    System.out.println("-------------------------------");
                }
            }
        }
    }
}
