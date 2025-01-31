package Controller;

import Model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller responsável por gerir os empréstimos de itens, incluindo livros e jornais.
 * Possui operações de criação, consulta, atualização e remoção de empréstimos, além de validações
 * relacionadas a reservas e disponibilidade de itens.
 */
public class EmprestimosController {
    private final List<Emprestimos> emprestimos;
    private LivroController livroController;
    private int maiorId;  // Inicializa com o maior ID dos empréstimos existentes
    private ReservaController reservaController; // Adicionando a referência ao ReservaController

    /**
     * Construtor da classe EmprestimosController.
     *
     * @param reservaController Controller responsável por gerir as reservas.
     * @param emprestimos Lista de empréstimos existentes.
     * @param livros Lista de livros disponíveis.
     * @param jornais Lista de jornais disponíveis.
     */
    public EmprestimosController(ReservaController reservaController, List<Emprestimos> emprestimos, List<Livro> livros, List<Jornal> jornais) {
        this.emprestimos = emprestimos; // Use a mesma lista do main
        this.reservaController = reservaController;
        Scanner scanner = new Scanner(System.in);

        // Inicializa maiorId com o maior ID dos empréstimos já existentes
        this.maiorId = calcularMaiorId(emprestimos);
    }

    /**
     * Calcula o maior ID entre os empréstimos existentes.
     *
     * @param emprestimos Lista de empréstimos.
     * @return O maior ID encontrado.
     */
    private int calcularMaiorId(List<Emprestimos> emprestimos) {
        int maior = 0;
        for (Emprestimos emprestimos1 : emprestimos) {
            if (emprestimos1.getNumero() > maior) {
                maior = emprestimos1.getNumero();
            }
        }
        return maior;
    }

    /**
     * Define o controller de livros.
     *
     * @param livroController Controller de livros.
     */
    public void setLivroController(LivroController livroController) {
        this.livroController = livroController;
    }

    /**
     * Cria um novo empréstimo para um utente com itens selecionados.
     *
     * @param utente Utente que realiza o empréstimo.
     * @param itensParaEmprestimo Lista de itens a serem emprestados.
     * @param dataInicio Data de início do empréstimo.
     * @param dataPrevistaDevolucao Data prevista para devolução.
     * @param dataEfetivaDevolucao Data efetiva de devolução (opcional, pode ser null).
     */
    public void criarEmprestimo(Utentes utente, List<ItemEmprestavel> itensParaEmprestimo, LocalDate dataInicio, LocalDate dataPrevistaDevolucao, LocalDate dataEfetivaDevolucao) {
        if (utente == null) {
            System.out.println("Erro: O utente informado é inválido.");
            return;
        }
        if (itensParaEmprestimo == null || itensParaEmprestimo.isEmpty()) {
            System.out.println("Erro: Nenhum item foi selecionado.");
            return;
        }

        // Verifica duplicados na lista de itens
        for (int i = 0; i < itensParaEmprestimo.size(); i++) {
            ItemEmprestavel itemAtual = itensParaEmprestimo.get(i);
            for (int j = i + 1; j < itensParaEmprestimo.size(); j++) {
                if (itemAtual.equals(itensParaEmprestimo.get(j))) {
                    System.out.println("Erro: Não é permitido incluir itens repetidos no mesmo empréstimo.");
                    return;
                }
            }
        }


        // Verifica conflitos com reservas e disponibilidade de itens
        for (ItemEmprestavel item : itensParaEmprestimo) {
            if (item instanceof Livro) {
                if (reservaController.verificarItemReservado((Livro) item, dataInicio, dataPrevistaDevolucao) && verificarItemEmprestado(item, dataInicio, dataPrevistaDevolucao)) {
                    System.out.println("Erro: O livro '" + item.getIdentificador() + "' já está reservado para o período indicado entre " + dataInicio + " e " + dataPrevistaDevolucao + " e não pode ser emprestado.");
                    return;
                }
            } else if (item instanceof Jornal) {
                // Para jornais, verifique se já está emprestado ou reservado
                if (reservaController.verificarItemReservado((Jornal) item, dataInicio, dataPrevistaDevolucao) && verificarItemEmprestado(item, dataInicio, dataPrevistaDevolucao)) {
                    System.out.println("Erro: O jornal '" + item.getIdentificador() + "' já está emprestado no período indicado entre " + dataInicio + " e " + dataPrevistaDevolucao + " e não pode ser emprestado.");
                    return;
                }
            }
        }

        this.maiorId = calcularMaiorId(emprestimos);

        // Atualiza o número do empréstimo com o maior ID encontrado + 1
        int numeroEmprestimo = maiorId + 1;

        // Criação do empréstimo
        Emprestimos novoEmprestimo = new Emprestimos(numeroEmprestimo, utente, itensParaEmprestimo, dataInicio, dataPrevistaDevolucao, dataEfetivaDevolucao);
        emprestimos.add(novoEmprestimo);

        // Exibe detalhes do novo empréstimo
        exibirDetalhesEmprestimo(novoEmprestimo);
    }

    /**
     * Mostra detalhes de um empréstimo de forma estruturada.
     *
     * @param emprestimo Empréstimo a ser apresentado.
     */
    public void exibirDetalhesEmprestimo(Emprestimos emprestimo) {
        System.out.println("\n========== Detalhes do Empréstimo ==========");
        System.out.println("Número do Empréstimo: " + emprestimo.getNumero());
        System.out.println("Utente: " + emprestimo.getUtente().getNome() + " (NIF: " + emprestimo.getUtente().getNif() + ")");
        System.out.println("Data de Início: " + emprestimo.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Data Prevista de Devolução: " + emprestimo.getDataPrevistaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Verifica se a data efetiva de devolução é null
        if (emprestimo.getDataEfetivaDevolucao() != null) {
            System.out.println("Data Efetiva de Devolução: " + emprestimo.getDataEfetivaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            System.out.println("Data Efetiva de Devolução: Em aberto");
        }

        System.out.println("Itens Emprestados:");
        for (ItemEmprestavel item : emprestimo.getItens()) {
            if (item instanceof Livro) {
                System.out.println(" - Livro: " + reservaController.pesquisaISBN(item.getIdentificador()) + " (ISBN: " + ((Livro) item).getIsbn() + ")");
            } else if (item instanceof Jornal) {
                System.out.println(" - Jornal: " + reservaController.pesquisaISSN(item.getIdentificador()) + " (ISSN: " + ((Jornal) item).getIssn() + ")");
            }
        }
        System.out.println("=".repeat(44));
    }

    /**
     * Consulta um empréstimo pelo seu número.
     *
     * @param numero Número do empréstimo.
     * @return O empréstimo encontrado ou null se não existir.
     */
    public Emprestimos consultarEmprestimo(int numero) {
        for (Emprestimos emprestimo : emprestimos) {
            if (emprestimo.getNumero() == numero) {
                return emprestimo;
            }
        }
        return null; // Se não encontrar o empréstimo, retorna null
    }

    /**
     * Atualiza a data efetiva de devolução de um empréstimo.
     *
     * @param numero Número do empréstimo.
     * @param novaDataEfetivaDevolucao Nova data efetiva de devolução.
     */
    public void atualizarEmprestimo(int numero, LocalDate novaDataEfetivaDevolucao) {
        Emprestimos emprestimo = consultarEmprestimo(numero);
        if (emprestimo == null) {
            System.out.println("Erro: Empréstimo com número '" + numero + "' não encontrado.");
            return;
        }

        if (!verificarDataAnterior(emprestimo.getDataInicio(), novaDataEfetivaDevolucao)) {
            System.out.println("Erro: A data efetiva de devolução não pode ser anterior à data de início do empréstimo.");
            return;
        }

        emprestimo.setDataEfetivaDevolucao(novaDataEfetivaDevolucao);
        System.out.println("Data efetiva de devolução atualizada com sucesso para: " + novaDataEfetivaDevolucao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    /**
     * Remove um empréstimo pelo número.
     *
     * @param numero Número do empréstimo.
     * @return true se o empréstimo foi removido, false caso contrário.
     */
    public boolean removerEmprestimo(int numero) {
        Emprestimos emprestimo = consultarEmprestimo(numero);
        if (emprestimo != null) {
            emprestimos.remove(emprestimo);
            return true;
        }
        return false;
    }

    /**
     * Verifica se a data de devolução é válida em relação à data de início.
     *
     * @param dataInicio Data de início do empréstimo.
     * @param dataDevolucao Data de devolução.
     * @return true se a data de devolução não for anterior à data de início.
     */
    public boolean verificarDataAnterior(LocalDate dataInicio, LocalDate dataDevolucao) {
        if (dataDevolucao == null) {
            return true;  // Se a data efetiva de devolução for null, considera válido
        }
        return !dataDevolucao.isBefore(dataInicio);  // Verifica se a data de devolução não é anterior à data de início
    }

    /**
     * Retorna todos os empréstimos cadastrados.
     *
     * @return Lista de empréstimos.
     */
    public List<Emprestimos> listarTodosEmprestimos() {
        return emprestimos; // Retorna uma cópia da lista de empréstimos
    }

    /**
     * Adiciona um item a um empréstimo existente.
     *
     * @param numero Número do empréstimo.
     * @param item Item a ser adicionado.
     */
    public void adicionarItemEmprestimo(int numero, ItemEmprestavel item) {
        Emprestimos emprestimo = consultarEmprestimo(numero);
        if (emprestimo != null) {
            if (!emprestimo.getItens().contains(item)) {
                emprestimo.getItens().add(item);
            }
        } else {
            System.out.println("Erro: Empréstimo não encontrado.");
        }
    }

    /**
     * Remove um item de um empréstimo existente.
     *
     * @param numero Número do empréstimo.
     * @param item Item a ser removido.
     */
    public void removerItemEmprestimo(int numero, ItemEmprestavel item) {
        Emprestimos emprestimo = consultarEmprestimo(numero);
        if (emprestimo != null) {
            if (emprestimo.getItens().contains(item)) {
                emprestimo.getItens().remove(item);
            }
        } else {
            System.out.println("Erro: Empréstimo não encontrado.");
        }
    }

    /**
     * Verifica se um item está emprestado em um período especificado.
     *
     * @param item Item a ser verificado.
     * @param dataInicio Data de início do período.
     * @param dataFim Data de término do período.
     * @return true se o item estiver emprestado no período.
     */
    public boolean verificarItemEmprestado(ItemEmprestavel item, LocalDate dataInicio, LocalDate dataFim) {
        for (Emprestimos emprestimos1 : emprestimos) {
            for (ItemEmprestavel i : emprestimos1.getItens()) {
                if (i.getIdentificador().equals(item.getIdentificador())) {
                    LocalDate dataInicioEmprestimo = emprestimos1.getDataInicio();
                    LocalDate dataFimEmprestimo;

                    if (emprestimos1.getDataEfetivaDevolucao() == null) {
                        dataFimEmprestimo = emprestimos1.getDataPrevistaDevolucao();
                    } else {
                        dataFimEmprestimo = emprestimos1.getDataEfetivaDevolucao();
                    }

                    // Verifica se há sobreposição de datas entre reserva e empréstimo
                    if (!(dataFim.isBefore(dataInicioEmprestimo) || dataInicio.isAfter(dataFimEmprestimo))) {
                        return true; // O item está reservado no período solicitado
                    }
                }
            }
        }
        return false; // O item não está reservado no período informado
    }


}
