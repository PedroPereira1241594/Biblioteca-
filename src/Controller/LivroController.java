package Controller;

import Model.Livro;
import View.LivroView;

import java.util.ArrayList;
import java.util.Scanner;

public class LivroController {
    private final ArrayList<Livro> livros;
    private final LivroView livroView;

    public LivroController(ArrayList<Livro> livros, LivroView livroView) {
        this.livros = livros;
        this.livroView = livroView;
    }

    public void adicionarLivro() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do livro: ");
        String nome = scanner.nextLine();
        System.out.print("Digite a editora: ");
        String editora = scanner.nextLine();
        System.out.print("Digite a categoria: ");
        String categoria = scanner.nextLine();
        System.out.print("Digite o ano: ");
        int ano = scanner.nextInt();
        scanner.nextLine(); // Consumir quebra de linha
        System.out.print("Digite o autor: ");
        String autor = scanner.nextLine();

        Livro livro = new Livro(nome, editora, categoria, ano, autor);
        livros.add(livro);
        System.out.println("Livro adicionado com sucesso!");
    }

    public void listarLivros() {
        livroView.exibirLivros(livros);
    }

    public void editarLivro(int indice) {
        if (indice >= 0 && indice < livros.size()) {
            Scanner scanner = new Scanner(System.in);
            Livro livro = livros.get(indice);

            System.out.println("Editando o livro: " + livro.getNome());
            System.out.print("Digite o novo nome (ou pressione Enter para manter): ");
            String nome = scanner.nextLine();
            if (!nome.isEmpty()) livro.setNome(nome);

            System.out.print("Digite a nova editora (ou pressione Enter para manter): ");
            String editora = scanner.nextLine();
            if (!editora.isEmpty()) livro.setEditora(editora);

            System.out.print("Digite a nova categoria (ou pressione Enter para manter): ");
            String categoria = scanner.nextLine();
            if (!categoria.isEmpty()) livro.setCategoria(categoria);

            System.out.print("Digite o novo ano (ou pressione Enter para manter): ");
            String anoStr = scanner.nextLine();
            if (!anoStr.isEmpty()) livro.setAno(Integer.parseInt(anoStr));

            System.out.print("Digite o novo autor (ou pressione Enter para manter): ");
            String autor = scanner.nextLine();
            if (!autor.isEmpty()) livro.setAutor(autor);

            System.out.println("Livro editado com sucesso!");
        } else {
            System.out.println("Índice inválido!");
        }
    }

    public void removerLivro(int indice) {
        if (indice >= 0 && indice < livros.size()) {
            livros.remove(indice);
            System.out.println("Livro removido com sucesso!");
        } else {
            System.out.println("Índice inválido!");
        }
    }
    public Livro buscarLivroPorTitulo(String titulo) {
        for (Livro livro : livros) {
            if (livro.getNome().equalsIgnoreCase(titulo)) {
                return livro;
            }
        }
        return null; // Retorna null se o livro não for encontrado
    }

}
