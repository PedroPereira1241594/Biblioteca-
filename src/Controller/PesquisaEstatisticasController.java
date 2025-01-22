package Controller;

import Model.Jornal;
import Model.Livro;
import Model.Emprestimos;
import Model.Reserva;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PesquisaEstatisticasController {
    private List<Livro> livros;
    private List<Jornal> jornals;
    private List<Emprestimos> emprestimos;
    private List<Reserva> reservas;

    // Construtor que inicializa as listas
    public PesquisaEstatisticasController(List<Livro> livros, List<Jornal> jornals, List<Emprestimos> emprestimos, List<Reserva> reservas) {
        this.livros = livros != null ? livros : new ArrayList<>();
        this.jornals = jornals != null ? jornals : new ArrayList<>();
        this.emprestimos = emprestimos != null ? emprestimos : new ArrayList<>();
        this.reservas = reservas != null ? reservas : new ArrayList<>();
    }

    // Método para pesquisar Livro por ISBN
    public Livro pesquisaISBN(String ISBN) {
        for (Livro livro : livros) {
            if (livro.getIsbn().equalsIgnoreCase(ISBN)) { // Verifica se o ISBN coincide.
                return livro; // Retorna o livro encontrado.
            }
        }
        return null; // Retorna null se nenhum livro for encontrado.
    }

    // Método para pesquisar Jornal/Revista por ISSN
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

    // Método para contar o total de empréstimos em um intervalo de datas
    public long contarEmprestimosEntreDatas(LocalDate dataInicio, LocalDate dataFim) {
        return emprestimos.stream()
                .filter(emprestimo -> {
                    LocalDate dataEmprestimo = emprestimo.getDataInicio();
                    return !dataEmprestimo.isBefore(dataInicio) && !dataEmprestimo.isAfter(dataFim);
                })
                .count(); // Retorna o número total de empréstimos no intervalo
    }

    // Método para calcular o tempo médio dos empréstimos em um intervalo de datas
    public double calcularTempoMedioEmpréstimos(LocalDate dataInicio, LocalDate dataFim) {
        long totalDias = 0;
        long count = 0;

        for (Emprestimos emprestimo : emprestimos) {
            LocalDate dataEmprestimo = emprestimo.getDataInicio();

            if (!dataEmprestimo.isBefore(dataInicio) && !dataEmprestimo.isAfter(dataFim)) {
                LocalDate dataDevolucao = emprestimo.getDataEfetivaDevolucao() != null
                        ? emprestimo.getDataEfetivaDevolucao()
                        : emprestimo.getDataPrevistaDevolucao();

                long dias = dataEmprestimo.until(dataDevolucao).getDays();
                totalDias += dias;
                count++;
            }
        }

        // Se não houver empréstimos no intervalo, retornamos 0 para evitar divisão por zero
        return count > 0 ? (double) totalDias / count : 0;
    }

    // Método para buscar o item mais requisitado (empréstimos e reservas) durante um intervalo de datas
    public List<String> pesquisarItensMaisRequisitados(LocalDate dataInicio, LocalDate dataFim) {
        // Map para armazenar a quantidade de requisições por livro/jornal
        Map<String, Integer> itemRequisitado = new HashMap<>();

        // Contabilizar os empréstimos
        for (Emprestimos emprestimo : emprestimos) {
            if (!emprestimo.getDataInicio().isBefore(dataInicio) && !emprestimo.getDataInicio().isAfter(dataFim)) {
                for (Livro livro : emprestimo.getLivros()) {
                    itemRequisitado.put(livro.getNome(), itemRequisitado.getOrDefault(livro.getNome(), 0) + 1);
                }
            }
        }

        // Contabilizar as reservas
        for (Reserva reserva : reservas) {
            if (!reserva.getDataInicio().isBefore(dataInicio) && !reserva.getDataInicio().isAfter(dataFim)) {
                for (Livro livro : reserva.getLivros()) {
                    itemRequisitado.put(livro.getNome(), itemRequisitado.getOrDefault(livro.getNome(), 0) + 1);
                }
            }
        }

        // Encontrar o valor máximo de requisições
        int maxRequisicoes = 0;
        for (Integer requisicoes : itemRequisitado.values()) {
            if (requisicoes > maxRequisicoes) {
                maxRequisicoes = requisicoes;
            }
        }

        // Adicionar todos os itens com o número máximo de requisições a uma lista
        List<String> itensMaisRequisitados = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : itemRequisitado.entrySet()) {
            if (entry.getValue() == maxRequisicoes) {
                itensMaisRequisitados.add(entry.getKey());
            }
        }

        return itensMaisRequisitados;
    }

    // Método para buscar os empréstimos com N dias de atraso
    public List<Emprestimos> buscarEmprestimosComAtraso(int diasAtraso) {
        List<Emprestimos> emprestimosComAtraso = new ArrayList<>();
        LocalDate hoje = LocalDate.now();

        for (Emprestimos emprestimo : emprestimos) {
            LocalDate dataDevolucaoPrevista = emprestimo.getDataPrevistaDevolucao();
            LocalDate dataDevolucao = emprestimo.getDataEfetivaDevolucao();
            if (dataDevolucaoPrevista.isBefore(hoje) && dataDevolucao == null) {
                long atraso = dataDevolucaoPrevista.until(hoje).getDays();
                if (atraso > diasAtraso) {
                    emprestimosComAtraso.add(emprestimo);
                }
            }

        }

        return emprestimosComAtraso;
    }


}
