package View;

import Controller.LivroController;
import Model.ItemEmprestavel;
import Model.Livro;

import java.util.ArrayList;
import java.util.Scanner;

public class LivroView {
    private LivroController livroController;
    private Scanner scanner;

    public LivroView(LivroController livroController, Scanner scanner) {
        this.livroController = livroController;
        this.scanner = scanner;
    }

    public void exibirLivros(ArrayList<Livro> itensEmprestaveis) {
        // Filtra apenas os itens do tipo Livro
        ArrayList<Livro> livros = new ArrayList<>();
        for (ItemEmprestavel item : itensEmprestaveis) {
            if (item instanceof Livro) {
                livros.add((Livro) item); // Adiciona o Livro à lista
            }
        }
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            System.out.println("\n=== Lista de Livros ===");
            System.out.printf("%-15s %-50s %-50s %-50s %-35s %-10s\n",
                    "ISBN", "Título", "Autor", "Editora", "Categoria", "Ano");
            System.out.println("-".repeat(210));
            for (Livro livro : livros) {
                System.out.printf("%-15s %-50s %-50s %-50s %-35s %-10d\n",
                        livro.getIsbn(),
                        livro.getNome(),
                        livro.getAutor(),
                        livro.getEditora(),
                        livro.getCategoria(),
                        livro.getAno());
            }
        }
    }

    public void gerirLivros() {
        if (livroController == null || scanner == null) {
            throw new IllegalStateException("LivroController e Scanner precisam ser configurados antes de usar gerirLivros.");
        }

        int opcao;

        do {
            System.out.println("\n=== Gestão de Livros ===");
            System.out.println("1. Adicionar Livro");
            System.out.println("2. Consultar Livro");
            System.out.println("3. Editar Livro");
            System.out.println("4. Remover Livro");
            System.out.println("5. Listar Livros");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Por favor, insira um número válido.");
                scanner.next(); // Limpa a entrada inválida
            }
            opcao = scanner.nextInt();
            scanner.nextLine();  

            switch (opcao) {
                case 1:
                    livroController.adicionarLivro();
                    break;
                case 2:
                    consultarLivro();
                    break;
                case 3:
                    livroController.editarLivro();
                    break;
                case 4:
                    livroController.removerLivro();
                    break;
                case 5:
                    livroController.listarLivros();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void consultarLivro() {
        System.out.println("\n=== Consultar Livro ===");
        System.out.print("ISBN do Livro: ");
        String isbn = scanner.nextLine();

        // Chama o método buscarLivroPorIsbn do controller
        Livro livro = livroController.buscarLivroPorIsbn(isbn);

        // Se o livro for encontrado, exibe os detalhes
        if (livro != null) {
            exibirLivroDetalhado(livro);
        } else {
            System.out.println("O Livro com o ISBN " + isbn + " não foi encontrado.");
        }
    }

    public void exibirLivroDetalhado(Livro livro) {

        System.out.println("\n========== Detalhes do Livro ===========");
        System.out.println("ISBN: " + livro.getIsbn());
        System.out.println("Título: " + livro.getNome());
        System.out.println("Autor: " + livro.getAutor());
        System.out.println("Editora: " + livro.getEditora());
        System.out.println("Categoria: " + livro.getCategoria());
        System.out.println("Ano: " + livro.getAno());
        System.out.println("=".repeat(40));
    }
}
