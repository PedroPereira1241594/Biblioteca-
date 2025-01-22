package View;

import Controller.LivroController;
import Model.Livro;

import java.util.ArrayList;
import java.util.Scanner;

public class LivroView {

    public void exibirLivros(ArrayList<Livro> livros) {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            System.out.println("\n=== Lista de Livros ===");
            System.out.printf("%-15s %-35s %-30s %-30s %-20s %-10s\n",
                    "ISBN", "Título", "Autor", "Editora", "Categoria", "Ano");
            System.out.println("---------------------------------------------------------------------------------------------" +
                    "-----------------------------------------------");
            for (Livro livro : livros) {
                System.out.printf("%-15s %-35s %-30s %-30s %-20s %-10d\n",
                        livro.getIsbn(),
                        livro.getNome(),
                        livro.getAutor(),
                        livro.getEditora(),
                        livro.getCategoria(),
                        livro.getAno());
            }
        }
    }

    public static void gerirLivros(LivroController livroController, Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n=== Gestão de Livros ===");
            System.out.println("1. Adicionar Livro");
            System.out.println("2. Listar Livros");
            System.out.println("3. Editar Livro");
            System.out.println("4. Remover Livro");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    livroController.adicionarLivro();
                    break;
                case 2:
                    livroController.listarLivros();
                    break;
                case 3:
                    livroController.editarLivro();
                    break;
                case 4:
                    livroController.removerLivro();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }
}

