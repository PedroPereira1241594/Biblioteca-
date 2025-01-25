package Controller;

import Model.Livro;
import Model.Emprestimos;
import Model.Reserva;
import View.LivroView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LivroController {
    private final ArrayList<Livro> livros;
    private LivroView livroView;
    private final EmprestimosController emprestimosController;
    private final List<Reserva> reservas; // Adicionada a lista de reservas

    public void setLivroView(LivroView livroView) {
        this.livroView = livroView;
    }

    public LivroController(ArrayList<Livro> livros, LivroView livroView, EmprestimosController emprestimosController, List<Reserva> reservas) {
        this.livros = livros;
        this.livroView = livroView;
        this.emprestimosController = emprestimosController;
        this.reservas = reservas; // Inicializa a lista de reservas
    }

    public void adicionarLivro() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Insira o ISBN: ");
        String isbn = scanner.nextLine();
        for (Livro livro : livros) {
            if (livro.getIsbn().equals(isbn)) {
                System.out.println("Já Existe um Livro com o Mesmo ISBN");
                return;
            }
        }
        System.out.print("Introduza o nome do livro: ");
        String nome = scanner.nextLine();
        System.out.print("Introduza a editora: ");
        String editora = scanner.nextLine();
        System.out.print("Introduza a categoria: ");
        String categoria = scanner.nextLine();
        System.out.print("Introduza o ano: ");
        int ano = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Introduza o autor: ");
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
            System.out.print("Introduza o novo nome (ou pressione Enter para manter): ");
            String nome = scanner.nextLine();
            if (!nome.isEmpty()) livro1.setNome(nome);

            System.out.print("Introduza a nova editora (ou pressione Enter para manter): ");
            String editora = scanner.nextLine();
            if (!editora.isEmpty()) livro1.setEditora(editora);

            System.out.print("Introduza a nova categoria (ou pressione Enter para manter): ");
            String categoria = scanner.nextLine();
            if (!categoria.isEmpty()) livro1.setCategoria(categoria);

            System.out.print("Introduza o novo ano (ou pressione Enter para manter): ");
            String anoStr = scanner.nextLine();
            if (!anoStr.isEmpty()) livro1.setAno(Integer.parseInt(anoStr));

            System.out.print("Introduza o novo autor (ou pressione Enter para manter): ");
            String autor = scanner.nextLine();
            if (!autor.isEmpty()) livro1.setAutor(autor);

            System.out.println("Introduza o novo ISBN (ou pressione Enter para manter): ");
            isbn = scanner.nextLine();
            if (!isbn.isEmpty()) livro1.setIsbn(isbn);

            System.out.println("Livro editado com sucesso!");
        } else {
            System.out.println("ISBN inválido!");
        }
    }

    public void removerLivro() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insira o ISBN do Livro que Pretende Remover: ");
        String isbn = scanner.nextLine();

        Livro livro1 = null;
        for (Livro livro : livros) {
            if (livro.getIsbn().equals(isbn)) {
                livro1 = livro;
                break;
            }
        }

        if (livro1 == null) {
            System.out.println("ISBN inválido!");
            return;
        }

        // Verificar se o livro está associado a alguma reserva
        boolean livroReservado = false;
        for (Reserva reserva : reservas) {
            if (reserva.getLivros().contains(livro1)) {
                livroReservado = true;
                break;
            }
        }

        // Verificar se o livro está associado a algum empréstimo ativo
        boolean livroEmprestado = false;
        for (Emprestimos emprestimo : emprestimosController.listarEmprestimosAtivos()) {
            if (emprestimo.getLivros().contains(livro1)) {
                livroEmprestado = true;
                break;
            }
        }

        if (livroReservado) {
            System.out.println("Erro: O livro '" + livro1.getNome() + "' está associado a uma reserva e não pode ser removido.");
        } else if (livroEmprestado) {
            System.out.println("Erro: O livro '" + livro1.getNome() + "' está associado a um empréstimo ativo e não pode ser removido.");
        } else {
            livros.remove(livro1);
            System.out.println("Livro removido com sucesso!");
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

    public boolean verificarLivroEmprestado(Livro livro) {
        for (Emprestimos emprestimo : emprestimosController.listarEmprestimosAtivos()) {
            if (emprestimo.getLivros().contains(livro) && emprestimo.getDataEfetivaDevolucao() == null) {
                return true; // Livro emprestado, sem data de devolução
            }
        }

        return false; // Livro não está emprestado ou já devolvido
    }
}
