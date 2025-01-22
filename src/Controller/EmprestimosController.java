package Controller;

import Model.Emprestimos;
import Model.Livro;
import Model.Utentes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class EmprestimosController {
    private final List<Emprestimos> emprestimos;
    private LivroController livroController;
    private static int proximoNumeroEmprestimo = 1;
    private Scanner scanner;
    private ReservaController reservaController; // Adicionando a referência ao ReservaController


    // Construtor
    public EmprestimosController(ReservaController reservaController, List<Emprestimos> emprestimos) {
        this.emprestimos = emprestimos; // Use a mesma lista do main
        this.reservaController = reservaController; // Atribui o ReservaController

    }

    // Setter para LivroController (evita dependências circulares)
    public void setLivroController(LivroController livroController) {
        this.livroController = livroController;
    }

    public void criarEmprestimo(Utentes utente, List<Livro> livrosParaEmprestimo, LocalDate dataInicio, LocalDate dataPrevistaDevolucao, LocalDate dataEfetivaDevolucao) {
        if (utente == null) {
            System.out.println("Erro: O utente informado é inválido.");
            return;
        }
        if (livrosParaEmprestimo == null || livrosParaEmprestimo.isEmpty()) {
            System.out.println("Erro: Nenhum livro foi selecionado.");
            return;
        }
        // Verifica duplicados na lista de livros
        Set<Livro> livrosUnicos = new HashSet<>(livrosParaEmprestimo);
        if (livrosUnicos.size() < livrosParaEmprestimo.size()) {
            System.out.println("Erro: Não é permitido incluir livros repetidos no mesmo empréstimo.");
            return;
        }
        // Verifica conflitos com reservas e disponibilidade de livros
        for (Livro livro : livrosParaEmprestimo) {
            // Verifica se o livro está reservado ou emprestado no período
            if (livroPossuiEmprestimoAtivo(livro, dataInicio, dataPrevistaDevolucao)) {
                System.out.println("Erro: O livro '" + livro.getNome() + "' com o ISBN '" + livro.getIsbn() + "' já está emprestado no período solicitado.");
                return;
            }
        }
        // Criação do empréstimo
        int numeroEmprestimo = proximoNumeroEmprestimo++;
        Emprestimos novoEmprestimo = new Emprestimos(numeroEmprestimo, utente, livrosParaEmprestimo, dataInicio, dataPrevistaDevolucao, dataEfetivaDevolucao);
        emprestimos.add(novoEmprestimo);

        // Exibe detalhes do novo empréstimo
        exibirDetalhesEmprestimo(novoEmprestimo);
    }

    // Exibe detalhes do empréstimo de forma estruturada
    public void exibirDetalhesEmprestimo(Emprestimos emprestimo) {
        System.out.println("\n=====================================");
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

        System.out.println("Livros Emprestados:");
        for (Livro livro : emprestimo.getLivros()) {
            System.out.println(" - " + livro.getNome() + " (ISBN: " + livro.getIsbn() + ")");
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


    private LocalDate lerData(DateTimeFormatter formato) {
        while (true) {
            try {
                String dataTexto = scanner.nextLine();
                return LocalDate.parse(dataTexto, formato);  // Retorna a data formatada
            } catch (DateTimeParseException e) {
                System.out.print("Data inválida. \nInsira novamente (dd/MM/yyyy): ");
            }
        }
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
    public List<Emprestimos> listarEmprestimosAtivos() {
        List<Emprestimos> ativos = new ArrayList<>();
        for (Emprestimos emprestimo : emprestimos) {
                ativos.add(emprestimo);
        }
        return ativos;
    }
    // Verifica se o livro já está emprestado no período entre dataInicio e dataPrevistaDevolucao
    public boolean livroPossuiEmprestimoAtivo(Livro livro, LocalDate dataInicio, LocalDate dataPrevistaDevolucao) {
        for (Emprestimos emprestimo : emprestimos) {
            for (Livro livroEmprestado : emprestimo.getLivros()) {
                // Verifica se o livro está no empréstimo
                if (livro.equals(livroEmprestado)) {
                    // Verifica se a data de devolução prevista ou efetiva do empréstimo cobre o período solicitado
                    if ((dataInicio.isBefore(emprestimo.getDataPrevistaDevolucao()) && dataPrevistaDevolucao.isAfter(emprestimo.getDataInicio())) ||
                            (dataInicio.isBefore(emprestimo.getDataEfetivaDevolucao()) && dataPrevistaDevolucao.isAfter(emprestimo.getDataInicio()))) {
                        return true; // O livro já está emprestado no período solicitado
                    }
                }
            }
        }
        return false; // O livro não está emprestado no período solicitado
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


}
