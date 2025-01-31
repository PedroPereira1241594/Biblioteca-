package Controller;

import Model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para gerir pesquisas e estatísticas relacionadas a empréstimos e reservas.
 */
public class PesquisaEstatisticasController {
    private List<Livro> livros;
    private List<Jornal> jornals;
    private List<Emprestimos> emprestimos;
    private List<Reserva> reservas;
    private EmprestimosController emprestimosController;
    private ReservaController reservaController;

    /**
     * Construtor que inicializa as listas e os controllers.
     *
     * @param livros Lista de livros.
     * @param jornals Lista de jornais.
     * @param emprestimos Lista de empréstimos.
     * @param reservas Lista de reservas.
     * @param emprestimosController Controller de empréstimos.
     * @param reservaController Controller de reservas.
     */
    public PesquisaEstatisticasController(List<Livro> livros, List<Jornal> jornals, List<Emprestimos> emprestimos, List<Reserva> reservas, EmprestimosController emprestimosController, ReservaController reservaController) {
        this.livros = livros != null ? livros : new ArrayList<>();
        this.jornals = jornals != null ? jornals : new ArrayList<>();
        this.emprestimos = emprestimos != null ? emprestimos : new ArrayList<>();
        this.reservas = reservas != null ? reservas : new ArrayList<>();
        this.emprestimosController = emprestimosController;
        this.reservaController = reservaController;
    }

    /**
     * Pesquisa um livro pelo ISBN.
     *
     * @param ISBN O ISBN do livro a ser pesquisado.
     * @return O livro correspondente ou null se não encontrado.
     */
    public Livro pesquisaISBN(String ISBN) {
        for (Livro livro : livros) {
            if (livro.getIsbn().equalsIgnoreCase(ISBN)) { // Verifica se o ISBN coincide.
                return livro; // Retorna o livro encontrado.
            }
        }
        return null; // Retorna null se nenhum livro for encontrado.
    }

    /**
     * Pesquisa um jornal pelo ISSN.
     *
     * @param ISSN O ISSN do jornal a ser pesquisado.
     * @return O jornal correspondente ou null se não encontrado.
     */
    public Jornal pesquisaISSN(String ISSN) {
        for (Jornal jornal : jornals) { // Iterando sobre a lista de jornais
            if (jornal.getIssn().equalsIgnoreCase(ISSN)) { // Verifica se o ISSN coincide
                return jornal; // Retorna o jornal encontrado
            }
        }
        return null; // Retorna null se nenhum jornal for encontrado.
    }

    /**
     * Lista os empréstimos em um intervalo de datas.
     *
     * @param dataInicio Data de início do intervalo.
     * @param dataFim Data de término do intervalo.
     * @return Lista de empréstimos no intervalo especificado.
     */
    public List<Emprestimos> listarEmprestimosPorIntervalo(LocalDate dataInicio, LocalDate dataFim) {
        // Obter lista de empréstimos
        List<Emprestimos> emprestimosAtivos = emprestimosController.listarTodosEmprestimos();

        // Verificar se existem empréstimos registados
        if (emprestimosAtivos == null || emprestimosAtivos.isEmpty()) {
            System.out.println("\nNenhum empréstimo registado.");
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

    /**
     * Lista as reservas em um intervalo de datas.
     *
     * @param dataInicio Data de início do intervalo.
     * @param dataFim Data de término do intervalo.
     * @return Lista de reservas no intervalo especificado.
     */
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

    /**
     * Conta o número de empréstimos em um intervalo de datas.
     *
     * @param dataInicio Data de início do intervalo.
     * @param dataFim Data de término do intervalo.
     */
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

    /**
     * Conta o total de reservas em um intervalo de datas.
     *
     * @param dataInicio a data de início do intervalo (inclusive).
     * @param dataFim a data de término do intervalo (inclusive).
     */
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

    /**
     * Calcula o tempo médio dos empréstimos em dias dentro de um intervalo de datas.
     *
     * @param dataInicio a data de início do intervalo (inclusive).
     * @param dataFim a data de término do intervalo (inclusive).
     * @param emprestimos a lista de empréstimos a serem analisados.
     * @return o tempo médio de empréstimos em dias no intervalo fornecido.
     */
    public double calcularTempoMedioEmpréstimos(LocalDate dataInicio, LocalDate dataFim, List<Emprestimos> emprestimos) {
        long totalDias = 0;
        long count = 0;

        for (Emprestimos emprestimo : emprestimos) {
            LocalDate dataInicioEmprestimo = emprestimo.getDataInicio();
            LocalDate dataFimEmprestimo = (emprestimo.getDataEfetivaDevolucao() != null)
                    ? emprestimo.getDataEfetivaDevolucao()
                    : emprestimo.getDataPrevistaDevolucao();

            if (!dataInicioEmprestimo.isBefore(dataInicio) && !dataInicioEmprestimo.isAfter(dataFim) &&
                    !dataFimEmprestimo.isBefore(dataInicio) && !dataFimEmprestimo.isAfter(dataFim)) {


                long dias = dataInicioEmprestimo.until(dataFimEmprestimo).getDays();
                totalDias += dias;
                count++;
            }
        }

        // Se não houver empréstimos no intervalo, retornamos 0 para evitar divisão por zero
        return count > 0 ? (double) totalDias / count : 0;
    }

    /**
     * Pesquisa os itens mais requisitados (emprestados ou reservados) em um intervalo de datas.
     *
     * @param dataInicio a data de início do intervalo (inclusive).
     * @param dataFim a data de término do intervalo (inclusive).
     * @return uma lista com os itens mais requisitados no intervalo fornecido.
     */
    public List<String> pesquisarItensMaisRequisitados(LocalDate dataInicio, LocalDate dataFim) {
        List<String> itensRegistados = new ArrayList<>();
        List<Integer> contagens = new ArrayList<>();

        // Processa os empréstimos
        for (Emprestimos emprestimo : emprestimos) {
            LocalDate dataEmprestimo = emprestimo.getDataInicio();
            LocalDate dataDevolucao = (emprestimo.getDataEfetivaDevolucao() != null)
                    ? emprestimo.getDataEfetivaDevolucao()
                    : emprestimo.getDataPrevistaDevolucao();

            if (!dataEmprestimo.isBefore(dataInicio) && !dataEmprestimo.isAfter(dataFim) &&
                    !dataDevolucao.isBefore(dataInicio) && !dataDevolucao.isAfter(dataFim)) {

                for (ItemEmprestavel item : emprestimo.getItens()) {
                    String chave = null;

                    if (item instanceof Livro) {
                        chave = pesquisaTituloISBN(item.getIdentificador()) + " - " + ((Livro) item).getIsbn();
                    } else if (item instanceof Jornal) {
                        chave = pesquisaTituloISSN(item.getIdentificador()) + " - " + ((Jornal) item).getIssn();
                    }

                    if (chave != null) {
                        adicionarOuIncrementar(itensRegistados, contagens, chave);
                    }
                }
            }
        }


        // Processa as reservas
        for (Reserva reserva : reservas) {
            LocalDate dataReserva = reserva.getDataInicio();
            LocalDate dataFimReserva = reserva.getDataFim();
            if (!dataReserva.isBefore(dataInicio) && !dataReserva.isAfter(dataFim) &&
                    !dataFimReserva.isBefore(dataInicio) && !dataFimReserva.isAfter(dataFim)) {

                for (ItemEmprestavel item : reserva.getItens()) {
                    String chave = null;

                    if (item instanceof Livro) {
                        chave = pesquisaTituloISBN(item.getIdentificador()) + " - " + ((Livro) item).getIsbn();
                    } else if (item instanceof Jornal) {
                        chave = pesquisaTituloISSN(item.getIdentificador()) + " - " + ((Jornal) item).getIssn();
                    }

                    if (chave != null) {
                        adicionarOuIncrementar(itensRegistados, contagens, chave);
                    }
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
        for (int i = 0; i < itensRegistados.size(); i++) {
            if (contagens.get(i) == maxRequisicoes) {
                itensMaisRequisitados.add(itensRegistados.get(i));
            }
        }

        return itensMaisRequisitados;
    }

    /**
     * Adiciona ou incrementa a contagem de um item na lista de itens registados.
     *
     * @param itensRegistados a lista de itens registados.
     * @param contagens a lista de contagens correspondentes aos itens registados.
     * @param chave a identificação única do item.
     */
    private void adicionarOuIncrementar(List<String> itensRegistados, List<Integer> contagens, String chave) {
        for (int i = 0; i < itensRegistados.size(); i++) {
            if (itensRegistados.get(i).equals(chave)) {
                contagens.set(i, contagens.get(i) + 1);
                return;
            }
        }
        // Se não encontrou, adiciona um novo
        itensRegistados.add(chave);
        contagens.add(1);
    }

    /**
     * Procura empréstimos com um atraso superior a um número especificado de dias.
     *
     * @param diasAtraso o número mínimo de dias de atraso.
     * @return uma lista de empréstimos que possuem atraso maior que o valor especificado.
     */
    public List<Emprestimos> procurarEmprestimosComAtraso(int diasAtraso) {
        List<Emprestimos> emprestimosComAtraso = new ArrayList<>();
        LocalDate hoje = LocalDate.now();

        for (Emprestimos emprestimo : emprestimos) {
            LocalDate dataDevolucaoPrevista = emprestimo.getDataPrevistaDevolucao();
            LocalDate dataDevolucao = emprestimo.getDataEfetivaDevolucao();
            if (dataDevolucaoPrevista.isBefore(hoje) && dataDevolucao == null) {
                long atraso = dataDevolucaoPrevista.until(hoje).getDays();
                if (atraso >= diasAtraso) {
                    emprestimosComAtraso.add(emprestimo);
                }
            }

        }

        return emprestimosComAtraso;
    }

    /**
     * Verifica se a data de fim é anterior à data de início.
     *
     * @param dataInicio a data de início.
     * @param dataFim a data de fim.
     * @return {@code true} se a data de fim for anterior à data de início, caso contrário, {@code false}.
     */
    public boolean verificarDataAnterior(LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim.isBefore(dataInicio)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Pesquisa o título de um livro a partir do ISBN.
     *
     * @param ISBN o identificador único do livro.
     * @return o título do livro, ou {@code null} se nenhum livro for encontrado.
     */
    public String pesquisaTituloISBN(String ISBN) {
        for (Livro livro : livros) {
            if (livro.getIsbn().equalsIgnoreCase(ISBN)) { // Verifica se o ISBN coincide.
                return livro.getNome(); // Retorna o livro encontrado.
            }
        }
        return null; // Retorna null se nenhum livro for encontrado.
    }

    /**
     * Pesquisa o título de um jornal a partir do ISSN.
     *
     * @param ISSN o identificador único do jornal.
     * @return o título do jornal, ou {@code null} se nenhum jornal for encontrado.
     */
    public String pesquisaTituloISSN(String ISSN) {
        for (Jornal jornal : jornals) { // Iterando sobre a lista de jornais
            if (jornal.getIssn().equalsIgnoreCase(ISSN)) { // Verifica se o ISSN coincide
                return jornal.getTitulo(); // Retorna o jornal encontrado
            }
        }
        return null; // Retorna null se nenhum jornal for encontrado.
    }


}
