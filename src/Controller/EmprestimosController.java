package Controller;

import Model.*;
import View.EmprestimosView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class EmprestimosController {
    private final List<Emprestimos> emprestimos;
    private LivroController livroController;
    private JornalController jornalController;
    private int maiorId;  // Inicializa com o maior ID dos empréstimos existentes
    private Scanner scanner;
    private ReservaController reservaController;
    // Adicionando a referência ao ReservaController

    private EmprestimosView emprestimosView;

    // Construtor
    public EmprestimosController(ReservaController reservaController, List<Emprestimos> emprestimos) {
        this.emprestimos = emprestimos; // Use a mesma lista do main
        this.reservaController = reservaController;
        this.scanner = new Scanner(System.in);

        // Inicializa maiorId com o maior ID dos empréstimos já existentes
        this.maiorId = 0;
        for (Emprestimos emprestimo : emprestimos) {
            if (emprestimo.getNumero() > maiorId) {
                maiorId = emprestimo.getNumero(); // Atualiza maiorId com o maior ID encontrado
            }
        }
    }

    // Setter para LivroController (evita dependências circulares)
    public void setLivroController(LivroController livroController) {
        this.livroController = livroController;
    }

    public void setJornalController(JornalController jornalController) {
        this.jornalController = jornalController;
    }


    public void criarEmprestimo(Utentes utente, List<Livro> livrosParaEmprestimo, List<Jornal> jornaisParaEmprestimo, LocalDate dataInicio, LocalDate dataPrevistaDevolucao, LocalDate dataEfetivaDevolucao) {
        if (utente == null) {
            System.out.println("Erro: O utente informado é inválido.");
            return;
        }

        // Verifica se a lista de livros e jornais não está vazia
        if ((livrosParaEmprestimo == null || livrosParaEmprestimo.isEmpty()) && (jornaisParaEmprestimo == null || jornaisParaEmprestimo.isEmpty())) {
            System.out.println("Erro: Nenhum livro ou jornal foi selecionado.");
            return;
        }

        // Verifica conflitos de livros
        for (Livro livro : livrosParaEmprestimo) {
            if (verificarLivroEmprestadoOuReservado(livro, dataInicio, dataPrevistaDevolucao)) {
                return;
            }
        }

        // Verifica conflitos de jornais
        for (Jornal jornal : jornaisParaEmprestimo) {
            if (verificarJornalEmprestadoOuReservado(jornal, dataInicio, dataPrevistaDevolucao)) {
                return;
            }
        }

        // Atualiza o número do empréstimo com o maior ID encontrado + 1
        int numeroEmprestimo = maiorId + 1;
        maiorId = numeroEmprestimo;

        // Criação do empréstimo com livros e jornais
        Emprestimos novoEmprestimo = new Emprestimos(numeroEmprestimo, utente, livrosParaEmprestimo, jornaisParaEmprestimo, dataInicio, dataPrevistaDevolucao, dataEfetivaDevolucao);
        emprestimos.add(novoEmprestimo);

        // Exibe detalhes do novo empréstimo
        exibirDetalhesEmprestimo(novoEmprestimo);
    }

    // Exibe detalhes do empréstimo de forma estruturada
    public void exibirDetalhesEmprestimo(Emprestimos emprestimo) {
        System.out.println("\n======= Detalhes do Empréstimo =======");
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
        if (!emprestimo.getLivros().isEmpty()) {
            System.out.println("Livros Emprestados:");
            for (Livro livro : emprestimo.getLivros()) {
                System.out.println(" - " + livro.getNome() + " (ISBN: " + livro.getIsbn() + ")");
            }
        }
        if(!emprestimo.getJornais().isEmpty()) {
            System.out.println("Jornais/Revista Emprestados:");
            for (Jornal jornal : emprestimo.getJornais()) {
                System.out.println(" - " + jornal.getTitulo() + " (ISSN: " + jornal.getIssn() + ")");
            }
            System.out.println("=".repeat(35));
        }

    }

    // CRUD: Read
    public Emprestimos consultarEmprestimo(int numero) {
        // Itera sobre a lista de empréstimos para encontrar o empréstimo pelo número
        for (Emprestimos emprestimo : emprestimos) {
            if (emprestimo.getNumero() == numero) {
                return emprestimo;  // Se encontrado, retorna o empréstimo
            }
        }
        // Se não encontrar o empréstimo, retorna null
        return null;
    }

    public Emprestimos buscarEmprestimoPorNumero(int numero) {
        // Itera sobre a lista de empréstimos para encontrar o empréstimo pelo número
        for (Emprestimos emprestimo : emprestimos) {
            if (emprestimo.getNumero() == numero) {
                exibirDetalhesEmprestimo(emprestimo);
                return emprestimo;  // Se encontrado, retorna o empréstimo
            }
        }
        // Se não encontrar o empréstimo, retorna null
        return null;
    }

    // CRUD: Update
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

    // CRUD: Delete
    public void removerEmprestimo(int numero) {
        Emprestimos emprestimo = consultarEmprestimo(numero);
        if (emprestimo != null) {
            emprestimos.remove(emprestimo);
            System.out.println("Empréstimo removido com sucesso.");
        }
    }

    // Listar apenas os empréstimos ativos
    public void listarEmprestimos() {
        emprestimosView.exibirEmprestimos(emprestimos);
    }


    // Verificação de datas no método verificarDataAnterior
    public boolean verificarDataAnterior(LocalDate dataInicio, LocalDate dataDevolucao) {
        if (dataDevolucao == null) {
            return true;  // Se a data efetiva de devolução for null, considera válido
        }
        return !dataDevolucao.isBefore(dataInicio);  // Verifica se a data de devolução não é anterior à data de início
    }

    public void adicionarLivroNoEmprestimo(Emprestimos emprestimo, Livro livro, LocalDate dataInicio, LocalDate dataEfetivaDevolucao) {
        if (emprestimo == null || livro == null) {
            System.out.println("Erro: Empréstimo ou livro inválido.");
            return;
        }

        // Verifica se o livro já está emprestado ou reservado no período
        if (verificarLivroEmprestadoOuReservado(livro, dataInicio, dataEfetivaDevolucao)) {
            return;
        }

        // Adiciona o livro à lista de livros do empréstimo
        emprestimo.getLivros().add(livro);
        System.out.println("Livro '" + livro.getNome() + "' adicionado com sucesso ao empréstimo.");
    }

    // Método para remover livro do empréstimo
    public void removerLivroDoEmprestimo(Emprestimos emprestimo, Livro livro) {
        if (emprestimo == null || livro == null) {
            System.out.println("Erro: Empréstimo ou livro inválido.");
            return;
        }

        // Verifica se o empréstimo tem mais de um livro
        if (emprestimo.getLivros().size() <= 1) {
            System.out.println("Erro: O empréstimo precisa ter pelo menos dois livros para poder remover um.");
            return;
        }

        // Verifica se o livro está na lista de livros do empréstimo
        if (emprestimo.getLivros().contains(livro)) {
            emprestimo.getLivros().remove(livro);
            System.out.println("Livro '" + livro.getNome() + "' removido com sucesso do empréstimo.");
        } else {
            System.out.println("Erro: O livro '" + livro.getNome() + "' não está no empréstimo.");
        }
    }

    private boolean verificarLivroEmprestadoOuReservado(Livro livro, LocalDate dataInicioEmprestimo, LocalDate dataFimEmprestimo) {

        // verifica se o livro já está reservado
        if (reservaController.verificarLivroReservado(livro, dataInicioEmprestimo, dataFimEmprestimo)) {
            return true;
        }

        for (Emprestimos emprestimo : emprestimos) {
            for (Livro l : emprestimo.getLivros()) {
                if (l.getIsbn().equals(livro.getIsbn())) {  // Comparação pelo ISBN
                    LocalDate dataInicioOutroEmprestimo = emprestimo.getDataInicio();
                    LocalDate dataFimOutroEmprestimo = emprestimo.getDataEfetivaDevolucao();

                    // Se a devolução ainda não foi registrada, usamos a data prevista para devolução
                    if (dataFimOutroEmprestimo == null) {
                        dataFimOutroEmprestimo = emprestimo.getDataPrevistaDevolucao();
                    }

                    // Verifica se há sobreposição de datas
                    if ((dataInicioEmprestimo.isBefore(dataFimOutroEmprestimo) || dataInicioEmprestimo.isEqual(dataFimOutroEmprestimo)) &&
                            (dataFimEmprestimo.isAfter(dataInicioOutroEmprestimo) || dataFimEmprestimo.isEqual(dataInicioOutroEmprestimo))) {
                        System.out.println("Erro: Livro '" + livro.getNome() + "' já está emprestado para o período entre "
                                + dataInicioOutroEmprestimo + " e " + dataFimOutroEmprestimo + ".");
                        return true;
                    }
                }
            }
        }

        return false; // O livro não está emprestado nem reservado no intervalo de datas
    }

    public boolean verificarLivroEmprestado(Livro livro, LocalDate dataInicioReserva, LocalDate dataFimReserva) {

        for (Emprestimos emprestimo : emprestimos) {
            for (Livro l : emprestimo.getLivros()) {
                if (l.getIsbn().equals(livro.getIsbn())) {
                    LocalDate dataInicioEmprestimo = emprestimo.getDataInicio(); // Corrigir: uso do método correto
                    LocalDate dataFimEmprestimo = emprestimo.getDataEfetivaDevolucao(); // Pode ser null

                    // Se a devolução ainda não foi registrada, usamos a data prevista
                    if (dataFimEmprestimo == null) {
                        dataFimEmprestimo = emprestimo.getDataPrevistaDevolucao();
                    }

                    // Verificar se há sobreposição de datas
                    if (!(dataFimReserva.isBefore(dataInicioEmprestimo) || dataInicioReserva.isAfter(dataFimEmprestimo))) {
                        System.out.println("Erro: Livro '" + livro.getNome() + "' já está emprestado no período da reserva.");
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void adicionarJornalNoEmprestimo(Emprestimos emprestimo, Jornal jornal, LocalDate dataInicio, LocalDate dataEfetivaDevolucao) {
        if (emprestimo == null || jornal == null) {
            System.out.println("Erro: Empréstimo ou jornal/revista inválido.");
            return;
        }

        // Verifica se o livro já está emprestado ou reservado no período
        if (verificarJornalEmprestadoOuReservado(jornal, dataInicio, dataEfetivaDevolucao)) {
            return;
        }

        // Adiciona o livro à lista de livros do empréstimo
        emprestimo.getJornais().add(jornal);
        System.out.println("Livro '" + jornal.getTitulo() + "' adicionado com sucesso ao empréstimo.");
    }

    public void removerJornalDoEmprestimo(Emprestimos emprestimo, Jornal jornal) {
        if (emprestimo == null || jornal == null) {
            System.out.println("Erro: Empréstimo ou jornal/revista inválido.");
            return;
        }

        // Verifica se o empréstimo tem mais de um livro
        if (emprestimo.getJornais().size() <= 1) {
            System.out.println("Erro: O empréstimo precisa ter pelo menos dois jornais/revistas para poder remover um.");
            return;
        }

        // Verifica se o livro está na lista de livros do empréstimo
        if (emprestimo.getJornais().contains(jornal)) {
            emprestimo.getJornais().remove(jornal);
            System.out.println("Jornal/Revista '" + jornal.getTitulo() + "' removido com sucesso do empréstimo.");
        } else {
            System.out.println("Erro: O jornal/revista '" + jornal.getTitulo() + "' não está no empréstimo.");
        }
    }

    public boolean verificarJornalEmprestado(Jornal jornal, LocalDate dataInicioReserva, LocalDate dataFimReserva) {

        for (Emprestimos emprestimo : emprestimos) {
            for (Jornal j : emprestimo.getJornais()) {
                if (j.getIssn().equals(jornal.getIssn())) {
                    LocalDate dataInicioEmprestimo = emprestimo.getDataInicio(); // Corrigir: uso do método correto
                    LocalDate dataFimEmprestimo = emprestimo.getDataEfetivaDevolucao(); // Pode ser null

                    // Se a devolução ainda não foi registrada, usamos a data prevista
                    if (dataFimEmprestimo == null) {
                        dataFimEmprestimo = emprestimo.getDataPrevistaDevolucao();
                    }

                    // Verificar se há sobreposição de datas
                    if (!(dataFimReserva.isBefore(dataInicioEmprestimo) || dataInicioReserva.isAfter(dataFimEmprestimo))) {
                        System.out.println("Erro: Jornal/Revista '" + jornal.getTitulo() + "' já está emprestado no período da reserva.");
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public boolean verificarJornalEmprestadoOuReservado(Jornal jornal, LocalDate dataInicioEmprestimo, LocalDate dataFimEmprestimo) {

        // Verifica se o jornal já está reservado
        if (reservaController.verificarJornalReservado(jornal, dataInicioEmprestimo, dataFimEmprestimo)) {
            return true;
        }

        for (Emprestimos emprestimo : emprestimos) {
            for (Jornal j : emprestimo.getJornais()) {
                if (j.getIssn().equals(jornal.getIssn())) {  // Comparação pelo ISSN
                    LocalDate dataInicioOutroEmprestimo = emprestimo.getDataInicio();
                    LocalDate dataFimOutroEmprestimo = emprestimo.getDataEfetivaDevolucao();

                    // Se a devolução ainda não foi registrada, usamos a data prevista para devolução
                    if (dataFimOutroEmprestimo == null) {
                        dataFimOutroEmprestimo = emprestimo.getDataPrevistaDevolucao();
                    }

                    // Verifica se há sobreposição de datas
                    if ((dataInicioEmprestimo.isBefore(dataFimOutroEmprestimo) || dataInicioEmprestimo.isEqual(dataFimOutroEmprestimo)) &&
                            (dataFimEmprestimo.isAfter(dataInicioOutroEmprestimo) || dataFimEmprestimo.isEqual(dataInicioOutroEmprestimo))) {
                        System.out.println("Erro: Jornal '" + jornal.getTitulo() + "' já está emprestado para o período entre "
                                + dataInicioOutroEmprestimo + " e " + dataFimOutroEmprestimo + ".");
                        return true;
                    }
                }
            }
        }

        return false; // O jornal não está emprestado nem reservado no intervalo de datas
    }




}
