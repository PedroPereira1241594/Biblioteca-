package Controller;

import Model.Jornal;
import Model.Livro;
import Model.Emprestimos;
import Model.Reserva;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PesquisaController {
    private List<Livro> livros;           // Lista de livros a ser pesquisada.
    private List<Jornal> jornals;         // Lista de jornais/revistas a ser pesquisada.
    private List<Emprestimos> emprestimos; // Lista de empréstimos registrados.
    private List<Reserva> reservas;       // Lista de reservas registradas.

    // Construtor que inicializa as listas
    public PesquisaController(List<Livro> livros, List<Jornal> jornals, List<Emprestimos> emprestimos, List<Reserva> reservas) {
        this.livros = livros != null ? livros : new ArrayList<>();
        this.jornals = jornals != null ? jornals : new ArrayList<>();
        this.emprestimos = emprestimos != null ? emprestimos : new ArrayList<>();
        this.reservas = reservas != null ? reservas : new ArrayList<>();
    }

    // Método para pesquisar por ISBN
    public Livro pesquisaISBN(String ISBN) {
        for (Livro livro : livros) {
            if (livro.getIsbn().equalsIgnoreCase(ISBN)) { // Verifica se o ISBN coincide.
                return livro; // Retorna o livro encontrado.
            }
        }
        return null; // Retorna null se nenhum livro for encontrado.
    }

    // Método para pesquisar por ISSN
    public Jornal pesquisaISSN(String ISSN) {
        for (Jornal jornal : jornals) { // Iterando sobre a lista de jornais
            if (jornal.getIssn().equalsIgnoreCase(ISSN)) { // Verifica se o ISSN coincide
                return jornal; // Retorna o jornal encontrado
            }
        }
        return null; // Retorna null se nenhum jornal for encontrado.
    }

    // Método para buscar empréstimos em um intervalo de datas
    public List<Emprestimos> buscarEmprestimosEntreDatas(LocalDate dataInicio, LocalDate dataFim) {
        List<Emprestimos> resultados = new ArrayList<>();
        for (Emprestimos emprestimo : emprestimos) {
            LocalDate dataEmprestimo = emprestimo.getDataInicio();
            if ((dataEmprestimo.isEqual(dataInicio) || dataEmprestimo.isAfter(dataInicio)) &&
                    (dataEmprestimo.isEqual(dataFim) || dataEmprestimo.isBefore(dataFim))) {
                resultados.add(emprestimo);
            }
        }
        return resultados;
    }

    // Método para buscar reservas em um intervalo de datas
    public List<Reserva> buscarReservasEntreDatas(LocalDate dataInicio, LocalDate dataFim) {
        List<Reserva> resultados = new ArrayList<>();
        for (Reserva reserva : reservas) {
            LocalDate dataReserva = reserva.getDataInicio();
            if ((dataReserva.isEqual(dataInicio) || dataReserva.isAfter(dataInicio)) &&
                    (dataReserva.isEqual(dataFim) || dataReserva.isBefore(dataFim))) {
                resultados.add(reserva);
            }
        }
        return resultados;
    }
}
