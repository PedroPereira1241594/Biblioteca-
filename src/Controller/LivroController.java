package Controller;

import Model.Livro;
import Model.Emprestimos;
import View.LivroView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LivroController {
    private final ArrayList<Livro> livros;
    private final LivroView livroView;
    private final EmprestimosController emprestimosController; // Adicionado para acessar o controller de empréstimos

    public LivroController(ArrayList<Livro> livros, LivroView livroView, EmprestimosController emprestimosController) {
        this.livros = livros;
        this.livroView = livroView;
        this.emprestimosController = emprestimosController; // Inicialização do controller de empréstimos
    }

    public void adicionarLivro() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o ISBN: ");
        String isbn = scanner.nextLine();
        for (Livro livro : livros){
            if (livro.getIsbn().equals(isbn)){
                System.out.println("Já Existe um Livro com o Mesmo ISBN");
                return;
            }
        }
        System.out.print("Digite o nome do livro: ");
        String nome = scanner.nextLine();
        System.out.print("Digite a editora: ");
        String editora = scanner.nextLine();
        System.out.print("Digite a categoria: ");
        String categoria = scanner.nextLine();
        System.out.print("Digite o ano: ");
        int ano = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Digite o autor: ");
        String autor = scanner.nextLine();

        Livro livro = new Livro(nome, editora, categoria, ano, autor, isbn);
        livros.add(livro);
        System.out.println("Livro adicionado com sucesso!\n");
    }

    public void listarLivros() {
        livroView.exibirLivros(livros);
    }

    public void editarLivro() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insira o ISBN do Livro que Pretende Editar: ");
        String isbn = scanner.nextLine();

        Livro livro1 = null;
        for (Livro Indice : livros) {
            if (Indice.getIsbn().equals(isbn)) {
                livro1 = Indice;
                break;
            }
        }
        if (livro1 != null) {
            System.out.println("Editando o livro: " + livro1.getNome());
            System.out.print("Digite o novo nome (ou pressione Enter para manter): ");
            String nome = scanner.nextLine();
            if (!nome.isEmpty()) livro1.setNome(nome);

            System.out.print("Digite a nova editora (ou pressione Enter para manter): ");
            String editora = scanner.nextLine();
            if (!editora.isEmpty()) livro1.setEditora(editora);

            System.out.print("Digite a nova categoria (ou pressione Enter para manter): ");
            String categoria = scanner.nextLine();
            if (!categoria.isEmpty()) livro1.setCategoria(categoria);

            System.out.print("Digite o novo ano (ou pressione Enter para manter): ");
            String anoStr = scanner.nextLine();
            if (!anoStr.isEmpty()) livro1.setAno(Integer.parseInt(anoStr));

            System.out.print("Digite o novo autor (ou pressione Enter para manter): ");
            String autor = scanner.nextLine();
            if (!autor.isEmpty()) livro1.setAutor(autor);

            System.out.println("Digite o novo ISBN (ou pressione Enter para manter): ");
            isbn = scanner.nextLine();
            if (!isbn.isEmpty()) livro1.setIsbn(isbn);

            System.out.println("Livro editado com sucesso!");
        } else {
            System.out.println("ISBN inválido!");
        }
    }

    public void removerLivro(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insira o ISBN do Livro que Pretende Remover: ");
        String isbn = scanner.nextLine();

        Livro livro1 = null;
        for (Livro Indice : livros) {
            if (Indice.getIsbn().equals(isbn)) {
                livro1 = Indice;
                break;
            }
        }
        if (livro1 != null) {
            livros.remove(livro1);
            System.out.println("Livro removido com sucesso!");
        } else {
            System.out.println("ISBN inválido!");
        }
    }

    public Livro buscarLivroPorIsbn(String isbn) {
        for (Livro livro : livros) {
            if (livro.getIsbn().equalsIgnoreCase(isbn)) {
                return livro; // Retorna o livro se o ISBN for encontrado
            }
        }
        return null; // Retorna null se o livro com o ISBN não for encontrado
    }

    public boolean verificarLivroDisponivelParaEmprestimo(Livro livro) {
        // Recuperar todos os empréstimos ativos
        List<Emprestimos> emprestimosAtivos = emprestimosController.listarEmprestimosAtivos();

        for (Emprestimos emprestimo : emprestimosAtivos) {
            // Verifica se o livro está na lista de livros do empréstimo e se não tem data efetiva de devolução
            if (emprestimo.getLivros().contains(livro) && emprestimo.getDataEfetivaDevolucao() == null) {
                return false; // O livro está emprestado e ainda não foi devolvido
            }
        }
        return true; // O livro está disponível para empréstimo
    }

}
