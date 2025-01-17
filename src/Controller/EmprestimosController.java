package Controller;

import Model.Emprestimos;
import Model.Livro;
import Model.Utentes;

import java.util.ArrayList;
import java.util.Date; // Importação adicionada
import java.text.SimpleDateFormat; // Para formatação de datas
import java.util.List;

public class EmprestimosController {
    private List<Emprestimos> emprestimos;
    private LivroController livroController;

    public EmprestimosController() {
        this.emprestimos = new ArrayList<>();
        this.livroController = livroController;
    }

    // CRUD: Create
    public void criarEmprestimo(int numero, Utentes utente, List<String> titulosLivros, String dataInicio, String dataPrevistaDevolucao) {
        List<Livro> livrosParaEmprestimo = new ArrayList<>();
        for (String titulo : titulosLivros) {
            Livro livro = livroController.buscarLivroPorTitulo(titulo);
            if (livro != null) {
                livrosParaEmprestimo.add(livro);
            } else {
                System.out.println("Livro com título \"" + titulo + "\" não encontrado no sistema.");
                return; // Interrompe a criação caso algum livro não exista
            }
        }

        Emprestimos emprestimo = new Emprestimos(numero, utente, livrosParaEmprestimo, dataInicio, dataPrevistaDevolucao);
        emprestimos.add(emprestimo);
        System.out.println("Empréstimo criado: " + emprestimo);
    }

    // CRUD: Read
    public Emprestimos consultarEmprestimo(int numero) {
        return emprestimos.stream()
                .filter(e -> e.getNumero() == numero)
                .findFirst()
                .orElse(null);
    }

    // CRUD: Update
    public void atualizarEmprestimo(int numero, String novaDataDevolucao) {
        Emprestimos emprestimo = consultarEmprestimo(numero);
        if (emprestimo != null) {
            // Atualizar a data prevista de devolução
            emprestimo.setDataPrevistaDevolucao(novaDataDevolucao);
            System.out.println("Data prevista de devolução atualizada para: " + novaDataDevolucao);
        } else {
            System.out.println("Empréstimo não encontrado.");
        }
    }

    // Registrar devolução
    public void registrarDevolucao(int numero) {
        Emprestimos emprestimo = consultarEmprestimo(numero);
        if (emprestimo != null) {
            String dataEfetivaDevolucao = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            emprestimo.setDataEfetivaDevolucao(dataEfetivaDevolucao);
            System.out.println("Devolução registrada em: " + dataEfetivaDevolucao);
        } else {
            System.out.println("Empréstimo não encontrado.");
        }
    }

    // CRUD: Delete
    public void removerEmprestimo(int numero) {
        Emprestimos emprestimo = consultarEmprestimo(numero);
        if (emprestimo != null) {
            emprestimos.remove(emprestimo);
            System.out.println("Empréstimo removido: " + emprestimo);
        } else {
            System.out.println("Empréstimo não encontrado.");
        }
    }

    public List<Emprestimos> listarEmprestimos() {
        return emprestimos;
    }
}
