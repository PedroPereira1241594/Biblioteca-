package Controller;

import Model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Scanner;

/**
 * Classe responsável por gerir reservas de itens.
 * Permite criar, consultar, atualizar, remover e listar reservas,
 * além de realizar operações relacionadas a itens reservados.
 */
public class ReservaController {
    private List<Reserva> reservas;
    private int maiorId;
    private EmprestimosController emprestimosController;
    private final List<Livro> livros;
    private final List<Jornal> jornais;

    /**
     * Construtor da classe.
     *
     * @param emprestimosController Controller de empréstimos associado.
     * @param reservas              lista de reservas existentes.
     * @param livros                lista de livros disponíveis.
     * @param jornais               lista de jornais disponíveis.
     */
    public ReservaController(EmprestimosController emprestimosController, List<Reserva> reservas, List<Livro> livros, List<Jornal> jornais) {
        this.emprestimosController = emprestimosController; // Atribui o EmprestimosController
        this.reservas = reservas; // Atribui a lista de reservas
        this.livros = livros;
        this.jornais = jornais;
        this.maiorId = calcularMaiorId(reservas);
        Scanner scanner = new Scanner(System.in);
    }

    /**
     * Calcula o maior ID presente na lista de reservas.
     *
     * @param reservas lista de reservas.
     * @return o maior ID encontrado.
     */
    private int calcularMaiorId(List<Reserva> reservas) {
        int maior = 0;
        for (Reserva reserva : reservas) {
            if (reserva.getNumero() > maior) {
                maior = reserva.getNumero();
            }
        }
        return maior;
    }

    /**
     * Define o controller de empréstimos.
     *
     * @param emprestimosController controlador de empréstimos.
     */
    public void setEmprestimosController(EmprestimosController emprestimosController) {
        this.emprestimosController = emprestimosController;
    }

    /**
     * Cria uma nova reserva.
     *
     * @param utente            utente que realiza a reserva.
     * @param itensParaReserva  itens a serem reservados.
     * @param dataRegisto       data de registro da reserva.
     * @param dataInicio        data de início da reserva.
     * @param dataFim           data de fim da reserva.
     * @param emprestimosController controller de empréstimos.
     */
    public void criarReserva(Utentes utente, List<ItemEmprestavel> itensParaReserva, LocalDate dataRegisto, LocalDate dataInicio, LocalDate dataFim, EmprestimosController emprestimosController) {
        if (utente == null) {
            System.out.println("Erro: O utente informado é inválido.");
            return;
        }

        if (itensParaReserva == null || itensParaReserva.isEmpty()) {
            System.out.println("Erro: Nenhum item foi selecionado para a reserva.");
            return;
        }

        // Valida data de fim da reserva
        if (dataFim.isBefore(dataInicio)) {
            System.out.println("Erro: A data de fim da reserva não pode ser anterior à data de início.");
            return;
        }

        // Atualiza o maiorId com base no array de reservas antes de criar a nova
        this.maiorId = calcularMaiorId(reservas);

        // Gera o próximo número da reserva
        int numeroReserva = maiorId + 1;

        // Criação da reserva
        Reserva novaReserva = new Reserva(numeroReserva, utente, new ArrayList<>(itensParaReserva), dataRegisto, dataInicio, dataFim);
        reservas.add(novaReserva);

        // Exibe detalhes da nova reserva
        exibirDetalhesReserva(novaReserva);
    }

    /**
     * Consulta uma reserva pelo número.
     *
     * @param numero número da reserva.
     * @return a reserva correspondente, ou null se não encontrada.
     */
    public Reserva consultarReserva(int numero) {
        for (Reserva reserva : reservas) {
            if (reserva.getNumero() == numero) {
                return reserva;
            }
        }
        return null;
    }

    /**
     * Atualiza as datas de uma reserva.
     *
     * @param numero          número da reserva.
     * @param novaDataInicio  nova data de início.
     * @param novaDataFim     nova data de fim.
     */
    public void atualizarReserva(int numero, LocalDate novaDataInicio, LocalDate novaDataFim) {
        Reserva reserva = consultarReserva(numero);
        if (reserva == null) {
            System.out.println("Erro: Reserva com número '" + numero + "' não encontrada.");
            return;
        }

        // Validação da data de fim não ser anterior à data de início
        if (!verificarDataAnterior(reserva.getDataInicio(), novaDataFim)) {
            System.out.println("Erro: A data de fim não pode ser anterior à data de início da reserva.");
            return;
        }

        // Atualizando as datas da reserva
        reserva.setDataInicio(novaDataInicio);
        reserva.setDataFim(novaDataFim);
        System.out.println("Reserva atualizada com sucesso!");
    }

    /**
     * Remove uma reserva pelo número.
     *
     * @param numero número da reserva.
     * @return true se a reserva foi removida, false caso contrário.
     */
    public boolean removerReserva(int numero) {
        Reserva reserva = consultarReserva(numero);
        if (reserva != null) {
            reservas.remove(reserva);
            return true;
        }
        return false;
    }

    /**
     * Mostra os detalhes de uma reserva.
     *
     * @param reserva reserva cujos detalhes serão apresentados.
     */
    public void exibirDetalhesReserva(Reserva reserva) {
        System.out.println("\n========== Detalhes da Reserva ===========");
        System.out.println("Número da Reserva: " + reserva.getNumero());
        System.out.println("Utente: " + reserva.getUtente().getNome() + " (NIF: " + reserva.getUtente().getNif() + ")");
        System.out.println("Data de Registo: " + reserva.getDataRegisto().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Data de Início: " + reserva.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Data de Fim: " + reserva.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        System.out.println("Itens Reservados:");
        for (ItemEmprestavel item : reserva.getItens()) {
            if (item instanceof Livro) {
                System.out.println(" - Livro: " + pesquisaISBN(item.getIdentificador()) + " (ISBN: " + ((Livro) item).getIsbn() + ")");
            } else if (item instanceof Jornal) {
                System.out.println(" - Jornal: " + pesquisaISSN(item.getIdentificador()) + " (ISSN: " + ((Jornal) item).getIssn() + ")");
            }
        }
        System.out.println("=".repeat(42));
    }

    /**
     * Verifica se uma data de fim é anterior à data de início.
     *
     * @param dataInicio data de início.
     * @param dataFim    data de fim.
     * @return true se a dataFim for anterior à dataInicio, false caso contrário.
     */
    public boolean verificarDataAnterior(LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim.isBefore(dataInicio)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adiciona um item à reserva.
     *
     * @param numero            número da reserva.
     * @param item              item a ser adicionado.
     * @param dataInicioReserva data de início da reserva.
     * @param dataFimReserva    data de fim da reserva.
     */
    public void adicionarItemNaReserva(int numero, ItemEmprestavel item, LocalDate dataInicioReserva, LocalDate dataFimReserva) {
        Reserva reserva = consultarReserva(numero);
        if (reserva != null) {
            if (!reserva.getItens().contains(item)) {
                reserva.getItens().add(item);
            }
        } else {
            System.out.println("Erro: Reserva não encontrada.");
        }
    }

    /**
     * Remove um item de uma reserva.
     *
     * @param numero número da reserva.
     * @param item   item a ser removido.
     */
    public void removerItemDaReserva(int numero, ItemEmprestavel item) {
        Reserva reserva = consultarReserva(numero);
        if (reserva != null) {
            if (reserva.getItens().contains(item)) {
                // Verifica se a reserva tem mais de um item
                if (reserva.getItens().size() > 1) {
                    reserva.getItens().remove(item);
                }
            } else {
                System.out.println("Erro: O item '" + item.getTitulo() + "' não está nesta reserva.");
            }
        } else {
            System.out.println("Erro: Reserva não encontrada.");
        }
    }

    /**
     * Verifica se um item está reservado em um intervalo de datas.
     *
     * @param item       item a ser verificado.
     * @param dataInicio data de início.
     * @param dataFim    data de fim.
     * @return true se o item está reservado, false caso contrário.
     */
    public boolean verificarItemReservado(ItemEmprestavel item, LocalDate dataInicio, LocalDate dataFim) {
        for (Reserva reserva : reservas) {
            for (ItemEmprestavel i : reserva.getItens()) {
                if (i.getIdentificador().equals(item.getIdentificador())) {
                    LocalDate dataInicioReserva = reserva.getDataInicio();
                    LocalDate dataFimReserva = reserva.getDataFim();

                    // Verifica se há sobreposição de datas entre reserva e empréstimo
                    if (!(dataFim.isBefore(dataInicioReserva) || dataInicio.isAfter(dataFimReserva))) {
                        return true; // O item está reservado no período solicitado
                    }
                }
            }
        }
        return false; // O item não está reservado no período informado
    }

    /**
     * Retorna a lista de todas as reservas.
     *
     * @return lista de reservas.
     */
    public List<Reserva> listarTodasReservas() {
        return reservas; // Retorna a lista de reservas
    }

    /**
     * Pesquisa o título de um livro pelo ISBN.
     *
     * @param ISBN código ISBN do livro.
     * @return o título do livro, ou null se não encontrado.
     */
    public String pesquisaISBN(String ISBN) {
        for (Livro livro : livros) {
            if (livro.getIsbn().equalsIgnoreCase(ISBN)) {
                return livro.getNome();
            }
        }
        return null;
    }

    /**
     * Pesquisa o título de um jornal pelo ISSN.
     *
     * @param ISSN código ISSN do jornal.
     * @return o título do jornal, ou null se não encontrado.
     */
    public String pesquisaISSN(String ISSN) {
        for (Jornal jornal : jornais) {
            if (jornal.getIssn().equalsIgnoreCase(ISSN)) {
                return jornal.getTitulo();
            }
        }
        return null;
    }

    /**
     * Converte uma reserva em um empréstimo.
     * Este método verifica se o EmprestimosController foi inicializado, consulta a reserva pelo número fornecido,
     * cria um empréstimo com base nos dados da reserva e remove a reserva após a conversão.
     * Se o EmprestimosController não estiver inicializado ou a reserva não for encontrada, uma mensagem de erro será exibida.
     *
     * @param numeroReserva O número da reserva que será convertida em empréstimo.
     * @param dataInicio A data de início do empréstimo (geralmente a mesma data de início da reserva).
     * @param dataPrevistaDevolucao A data prevista para devolução do empréstimo (geralmente a mesma data de fim da reserva).
     *
     * @see EmprestimosController#criarEmprestimo(Utentes, List, LocalDate, LocalDate, LocalDate)
     * @see #consultarReserva(int)
     * @see #removerReserva(int)
     */
    public void converterReservaEmEmprestimo(int numeroReserva, LocalDate dataInicio, LocalDate dataPrevistaDevolucao) {
        // Verifica se o EmprestimosController foi inicializado
        if (emprestimosController == null) {
            System.out.println("Erro: EmprestimosController não foi inicializado.");
            return;
        }

        // Consulta a reserva pelo número
        Reserva reserva = this.consultarReserva(numeroReserva); // Usa "this" para acessar o método da própria classe
        if (reserva == null) {
            System.out.println("Erro: Reserva com número '" + numeroReserva + "' não encontrada.");
            return;
        }

        // Cria o empréstimo com base na reserva
        emprestimosController.criarEmprestimo(reserva.getUtente(), reserva.getItens(), dataInicio, dataPrevistaDevolucao, null);

        // Remove a reserva após a conversão
        boolean reservaRemovida = this.removerReserva(numeroReserva); // Usa "this" para acessar o método da própria classe
        if (reservaRemovida) {
            System.out.println("Reserva convertida em empréstimo com sucesso!");
        } else {
            System.out.println("Erro: Não foi possível remover a reserva após a conversão.");
        }
    }

}
