package Livros;

import java.util.Scanner;

public class MenuLivro {
    public static void main(String[] args) {
        Livro.Biblioteca biblioteca = new Livro.Biblioteca();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Gestão da Biblioteca ---");
            System.out.println("1. Adicionar Livros.Livro");
            System.out.println("2. Listar Livros");
            System.out.println("3. Editar Livros.Livro");
            System.out.println("4. Remover Livros.Livro");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Editora: ");
                    String editora = scanner.nextLine();
                    System.out.print("Categoria: ");
                    String categoria = scanner.nextLine();
                    System.out.print("Ano: ");
                    int ano = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Autor: ");
                    String autor = scanner.nextLine();

                    Livro livro = new Livro(nome, editora, categoria, ano, autor);
                    biblioteca.adicionarLivro(livro);
                    break;

                case 2:
                    biblioteca.listarLivros();
                    break;

                case 3:
                    biblioteca.editarLivro(scanner);
                    break;

                case 4:
                    biblioteca.removerLivro(scanner);
                    break;

                case 5:
                    System.out.println("Encerrando o programa.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
