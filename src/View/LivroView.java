package View;

import Controller.LivroController;
import Model.Livro;

import java.util.ArrayList;
import java.util.Scanner;

public class LivroView {

    public void exibirLivros(ArrayList<Livro> livros) {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro Registado.");
        } else {
            System.out.println("Lista de Livros:");
            for (int i = 0; i < livros.size(); i++) {
                Livro livro = livros.get(i);
                System.out.println("Título: " + livro.getNome());
                System.out.println("Autor: " + livro.getAutor());
                System.out.println("Editora: " + livro.getEditora());
                System.out.println("Categoria: " + livro.getCategoria());
                System.out.println("Ano: " + livro.getAno());
                System.out.println("----------------------");
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
            System.out.println("0. Voltar ao menu principal");
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

