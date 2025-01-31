package Controller;

import Model.Livro;
import Model.ItemEmprestavel;
import Model.Emprestimos;
import Model.Reserva;
import View.LivroView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Controlador responsável por gerir operações dos livros.
 */
public class LivroController {
    private final ArrayList<Livro> livros;
    private LivroView livroView;
    private final EmprestimosController emprestimosController;
    private final List<Reserva> reservas;
    private final List<Emprestimos> emprestimos;

    /**
     * Visualização de um livro.
     *
     * @param livroView A visualização dos livros.
     */
    public void setLivroView(LivroView livroView) {
        this.livroView = livroView;
    }

    /**
     * Construtor da classe LivroController.
     *
     * @param livros Lista de livros disponíveis.
     * @param livroView Visualização do livro.
     * @param emprestimosController Controlador de empréstimos.
     * @param reservas Lista de reservas.
     * @param emprestimos Lista de empréstimos.
     */
    public LivroController(ArrayList<Livro> livros, LivroView livroView, EmprestimosController emprestimosController, List<Reserva> reservas, List<Emprestimos> emprestimos) {
        this.livros = livros;
        this.livroView = livroView;
        this.emprestimosController = emprestimosController;
        this.reservas = reservas;
        this.emprestimos = emprestimos;
    }

    /**
     * Adiciona um novo livro ao sistema.
     * São inseridos todos os dados de um Livro.
     */
    public void adicionarLivro() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nInsira o ISBN: ");
        String isbn = scanner.nextLine();
        for (ItemEmprestavel item : livros) {
            if (item instanceof Livro && ((Livro) item).getIsbn().equals(isbn)) {
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

    /**
     * Lista todos os livros disponíveis.
     */
    public void listarLivros() {
        livroView.exibirLivros(livros);
    }

    /**
     * Edição das informações de um livro existente, procurando pelo ISBN.
     */
    public void editarLivro() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insira o ISBN do Livro que Pretende Editar: ");
        String isbn = scanner.nextLine();

        Livro livro1 = null;
        for (ItemEmprestavel item : livros) {
            if (item instanceof Livro && ((Livro) item).getIsbn().equals(isbn)) {
                livro1 = (Livro) item;
                break;
            }
        }
        if (livro1 != null) {
            System.out.println("\nEditando o livro: " + livro1.getNome());
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

    /**
     * Remove um livro da lista procurando pelo ISBN.
     *
     */
    public void removerLivro() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insira o ISBN do Livro que Pretende Remover: ");
        String isbn = scanner.nextLine();

        //Procura pelo ISBN
        Livro livro1 = null;
        for (ItemEmprestavel item : livros) {
            if (item instanceof Livro && ((Livro) item).getIsbn().equals(isbn)) {
                livro1 = (Livro) item;
                break;
            }
        }
        if (livro1 == null) {
            System.out.println("ISBN inválido!");
            return;
        }

        // Verifica se o livro está associado a alguma reserva ativa
        boolean livroReservado = false;
        for (Reserva reserva : reservas) {
            for (ItemEmprestavel item : reserva.getItens()) {
                if  (item.getIdentificador().equals(livro1.getIsbn())){
                    livroReservado = true;
                    break;
                }
            }
            if (livroReservado) {
                break;
            }
        }

        // Verifica se o livro está associado a algum empréstimo ativo
        boolean livroEmprestado = false;
        for (Emprestimos emprestimos : emprestimos) {
            for (ItemEmprestavel item : emprestimos.getItens()) {
                if (item.getIdentificador().equals(livro1.getIsbn())) {
                    livroEmprestado = true;
                    break;
                }
            }
            if (livroEmprestado) {
                break;
            }
        }

        if (livroReservado && livroEmprestado) {
            System.out.println("Erro: O livro '" + livro1.getNome() + "' está associado a uma reserva, a um empréstimo e não pode ser removido.");
        } else if (livroEmprestado) {
            System.out.println("Erro: O livro '" + livro1.getNome() + "' está associado a um empréstimo e não pode ser removido.");
        } else if (livroReservado) {
            System.out.println("Erro: O livro '" + livro1.getNome() + "' está associado a uma reserva e não pode ser removido.");
        } else {
            livros.remove(livro1);
            System.out.println("Livro removido com sucesso!");
        }
    }

    /**
     * Procura um livro na coleção pelo seu ISBN.
     *
     * @param isbn O ISBN do livro a ser procurado.
     * @return O livro encontrado ou null se não existir na list.
     */
    public Livro buscarLivroPorIsbn(String isbn) {
        for (ItemEmprestavel item : livros) {
            if (item instanceof Livro && ((Livro) item).getIsbn().equalsIgnoreCase(isbn)) {
                return (Livro) item;
            }
        }
        return null;
    }
}
