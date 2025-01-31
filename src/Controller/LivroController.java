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

public class LivroController {
    private final ArrayList<Livro> livros; // Agora é uma lista de ItemEmprestavel
    private LivroView livroView;
    private final EmprestimosController emprestimosController;
    private final List<Reserva> reservas; // Adicionada a lista de reservas
    private final List<Emprestimos> emprestimos;

    public void setLivroView(LivroView livroView) {
        this.livroView = livroView;
    }

    public LivroController(ArrayList<Livro> livros, LivroView livroView, EmprestimosController emprestimosController, List<Reserva> reservas, List<Emprestimos> emprestimos) {
        this.livros = livros;
        this.livroView = livroView;
        this.emprestimosController = emprestimosController;
        this.reservas = reservas; // Inicializa a lista de reservas
        this.emprestimos = emprestimos;
    }

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

    public void listarLivros() {
        livroView.exibirLivros(livros);
    }

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

    public void removerLivro() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insira o ISBN do Livro que Pretende Remover: ");
        String isbn = scanner.nextLine();

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

        // Verificar se o livro está associado a algum empréstimo ativo
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

    public Livro buscarLivroPorIsbn(String isbn) {
        for (ItemEmprestavel item : livros) {
            if (item instanceof Livro && ((Livro) item).getIsbn().equalsIgnoreCase(isbn)) {
                return (Livro) item; // Retorna o livro se o ISBN for encontrado
            }
        }
        return null; // Retorna null se o livro com o ISBN não for encontrado
    }


}
