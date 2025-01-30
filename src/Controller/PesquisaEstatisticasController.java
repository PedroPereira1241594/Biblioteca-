package Controller;

import Model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PesquisaEstatisticasController {
    private List<Livro> livros;
    private List<Jornal> jornals;
    private List<Emprestimos> emprestimos;
    private List<Reserva> reservas;
    private EmprestimosController emprestimosController;
    private ReservaController reservaController;

    // Construtor que inicializa as listas
    public PesquisaEstatisticasController(List<Livro> livros, List<Jornal> jornals, List<Emprestimos> emprestimos, List<Reserva> reservas, EmprestimosController emprestimosController, ReservaController reservaController) {
        this.livros = livros != null ? livros : new ArrayList<>();
        this.jornals = jornals != null ? jornals : new ArrayList<>();
        this.emprestimos = emprestimos != null ? emprestimos : new ArrayList<>();
        this.reservas = reservas != null ? reservas : new ArrayList<>();
        this.emprestimosController = emprestimosController;
        this.reservaController = reservaController;
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
    public List<Emprestimos> listarEmprestimosPorIntervalo(LocalDate dataInicio, LocalDate dataFim) {
        // Obter lista de empréstimos
        List<Emprestimos> emprestimosAtivos = emprestimosController.listarTodosEmprestimos();

        // Verificar se existem empréstimos registrados
        if (emprestimosAtivos == null || emprestimosAtivos.isEmpty()) {
            System.out.println("\nNenhum empréstimo registrado.");
            return new ArrayList<>();
        }

        // Filtrar os empréstimos pelo intervalo de datas
        List<Emprestimos> emprestimosFiltrados = new ArrayList<>();
        for (Emprestimos emprestimo : emprestimosAtivos) {
            LocalDate dataInicioEmprestimo = emprestimo.getDataInicio();
            LocalDate dataFimEmprestimo;

            if (emprestimo.getDataEfetivaDevolucao() != null) {
                dataFimEmprestimo = emprestimo.getDataEfetivaDevolucao();
            } else {
                dataFimEmprestimo = emprestimo.getDataPrevistaDevolucao();
            }

            if (dataInicioEmprestimo != null &&
                    (dataInicioEmprestimo.isEqual(dataInicio) || dataInicioEmprestimo.isAfter(dataInicio)) &&
                    (dataFimEmprestimo.isEqual(dataFim) || dataFimEmprestimo.isBefore(dataFim))) {
                emprestimosFiltrados.add(emprestimo);
            }
        }

        // Remover duplicados manualmente
        List<Emprestimos> emprestimosSemDuplicados = new ArrayList<>();
        for (Emprestimos emprestimo : emprestimosFiltrados) {
            boolean jaExiste = false;
            for (Emprestimos e : emprestimosSemDuplicados) {
                if (e.getNumero() == emprestimo.getNumero()) {
                    jaExiste = true;
                    break;
                }
            }
            if (!jaExiste) {
                emprestimosSemDuplicados.add(emprestimo);
            }
        }

        // Ordenar a lista manualmente usando Bubble Sort
        for (int i = 0; i < emprestimosSemDuplicados.size() - 1; i++) {
            for (int j = 0; j < emprestimosSemDuplicados.size() - i - 1; j++) {
                if (emprestimosSemDuplicados.get(j).getNumero() > emprestimosSemDuplicados.get(j + 1).getNumero()) {
                    Emprestimos temp = emprestimosSemDuplicados.get(j);
                    emprestimosSemDuplicados.set(j, emprestimosSemDuplicados.get(j + 1));
                    emprestimosSemDuplicados.set(j + 1, temp);
                }
            }
        }

        // Retorna a lista final filtrada e ordenada
        return emprestimosSemDuplicados;
    }

    public List<Reserva> listarReservasPorIntervalo(LocalDate dataInicio, LocalDate dataFim) {
        // Obter lista de reservas
        List<Reserva> reservas1 = reservaController.listarTodasReservas();

        // Verificar se existem reservas registadas
        if (reservas1 == null || reservas1.isEmpty()) {
            System.out.println("\nNenhuma reserva registada.");
            return new ArrayList<>();
        }

        // Filtrar as reservas pelo intervalo de datas
        List<Reserva> reservaFiltro = new ArrayList<>();
        for (Reserva reserva : reservas1) {
            LocalDate dataInicioReserva = reserva.getDataInicio();
            LocalDate dataFimReserva  = reserva.getDataFim();

            if (dataInicioReserva != null &&
                    (dataInicioReserva.isEqual(dataInicio) || dataInicioReserva.isAfter(dataInicio)) &&
                    (dataFimReserva.isEqual(dataFim) || dataFimReserva.isBefore(dataFim))) {
                reservaFiltro.add(reserva);
            }
        }

        // Remover duplicados manualmente
        List<Reserva> reservaSemDuplicados = new ArrayList<>();
        for (Reserva reserva : reservaFiltro) {
            boolean jaExiste = false;
            for (Reserva r : reservaSemDuplicados) {
                if (r.getNumero() == reserva.getNumero()) {
                    jaExiste = true;
                    break;
                }
            }
            if (!jaExiste) {
                reservaSemDuplicados.add(reserva);
            }
        }

        // Ordenar a lista manualmente usando Bubble Sort
        for (int i = 0; i < reservaSemDuplicados.size() - 1; i++) {
            for (int j = 0; j < reservaSemDuplicados.size() - i - 1; j++) {
                if (reservaSemDuplicados.get(j).getNumero() > reservaSemDuplicados.get(j + 1).getNumero()) {
                    Reserva temp = reservaSemDuplicados.get(j);
                    reservaSemDuplicados.set(j, reservaSemDuplicados.get(j + 1));
                    reservaSemDuplicados.set(j + 1, temp);
                }
            }
        }

        // Retorna a lista final filtrada e ordenada
        return reservaSemDuplicados;
    }

    // Método para contar o total de empréstimos em um intervalo de datas
    public void contarEmprestimosEntreDatas(LocalDate dataInicio, LocalDate dataFim) {
        long contador = 0;
        for (Emprestimos emprestimo : emprestimos) {
            LocalDate dataInicioEmprestimo = emprestimo.getDataInicio();
            LocalDate dataFimEmprestimo;

            if (emprestimo.getDataEfetivaDevolucao() != null) {
                dataFimEmprestimo = emprestimo.getDataEfetivaDevolucao();
            } else {
                dataFimEmprestimo = emprestimo.getDataPrevistaDevolucao();
            }

            if (dataInicioEmprestimo != null &&
                    (dataInicioEmprestimo.isEqual(dataInicio) || dataInicioEmprestimo.isAfter(dataInicio)) &&
                    (dataFimEmprestimo.isEqual(dataFim) || dataFimEmprestimo.isBefore(dataFim))) {
                contador++;
            }
        }
        String dataInicioFormat = dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String dataFimFormat = dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.println("\nTotal de empréstimos no intervalo de datas fornecidas '" + dataInicioFormat +"' - '" + dataFimFormat + "': " + contador);
    }


    // Método para contar o total de reservas em um intervalo de datas
    public void contarReservasEntreDatas(LocalDate dataInicio, LocalDate dataFim) {
        long contador = 0;
        for (Reserva reserva : reservas) {
            LocalDate dataInicioReserva = reserva.getDataInicio();
            LocalDate dataFimReserva  = reserva.getDataFim();
            if (dataInicioReserva != null &&
                    (dataInicioReserva.isEqual(dataInicio) || dataInicioReserva.isAfter(dataInicio)) &&
                    (dataFimReserva.isEqual(dataFim) || dataFimReserva.isBefore(dataFim))) {
                contador++;
            }
        }
        String dataInicioFormat = dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String dataFimFormat = dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.println("Total de reservas no intervalo de datas fornecidas '" + dataInicioFormat +"' - '" + dataFimFormat + "': " + contador); // Exibindo o total de reservas
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

    public List<String> pesquisarItensMaisRequisitados(LocalDate dataInicio, LocalDate dataFim) {
        List<String> itensRegistrados = new ArrayList<>();
        List<Integer> contagens = new ArrayList<>();

        // Processa os empréstimos
        for (Emprestimos emprestimo : emprestimos) {
            LocalDate dataEmprestimo = emprestimo.getDataInicio();
            LocalDate dataDevolucao = (emprestimo.getDataEfetivaDevolucao() != null) ? emprestimo.getDataEfetivaDevolucao() : emprestimo.getDataPrevistaDevolucao();

            if (!dataEmprestimo.isBefore(dataInicio) && !dataEmprestimo.isAfter(dataFim) &&
                    !dataDevolucao.isBefore(dataInicio) && !dataDevolucao.isAfter(dataFim)) {

                for (ItemEmprestavel item : emprestimo.getItens()) {
                    String identificador = (item instanceof Livro) ? ((Livro) item).getIsbn() : ((Jornal) item).getIssn();
                    String chave = item.getTitulo() + " - " + identificador;

                    adicionarOuIncrementar(itensRegistrados, contagens, chave);
                }
            }
        }

        // Processa as reservas
        for (Reserva reserva : reservas) {
            if (!reserva.getDataInicio().isBefore(dataInicio) && !reserva.getDataInicio().isAfter(dataFim) &&
                    !reserva.getDataFim().isBefore(dataInicio) && !reserva.getDataFim().isAfter(dataFim)) {

                for (ItemEmprestavel item : reserva.getItens()) {
                    String identificador = (item instanceof Livro) ? ((Livro) item).getIsbn() : ((Jornal) item).getIssn();
                    String chave = item.getTitulo() + " - " + identificador;

                    adicionarOuIncrementar(itensRegistrados, contagens, chave);
                }
            }
        }

        // Descobrir a maior contagem
        int maxRequisicoes = 0;
        for (int contagem : contagens) {
            if (contagem > maxRequisicoes) {
                maxRequisicoes = contagem;
            }
        }

        // Criar lista dos itens mais requisitados
        List<String> itensMaisRequisitados = new ArrayList<>();
        for (int i = 0; i < itensRegistrados.size(); i++) {
            if (contagens.get(i) == maxRequisicoes) {
                itensMaisRequisitados.add(itensRegistrados.get(i));
            }
        }

        return itensMaisRequisitados;
    }

    // Método auxiliar para adicionar ou incrementar a contagem de um item
    private void adicionarOuIncrementar(List<String> itensRegistrados, List<Integer> contagens, String chave) {
        for (int i = 0; i < itensRegistrados.size(); i++) {
            if (itensRegistrados.get(i).equals(chave)) {
                contagens.set(i, contagens.get(i) + 1);
                return;
            }
        }
        // Se não encontrou, adiciona um novo
        itensRegistrados.add(chave);
        contagens.add(1);
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
    public boolean verificarDataAnterior(LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim.isBefore(dataInicio)) {
            return false;
        } else {
            return true;
        }
    }


}
