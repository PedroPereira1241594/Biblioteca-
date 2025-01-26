package Controller;

import Model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReservaController {
    private final List<Reserva> reservas;
    private int maiorId;
    private final Scanner scanner;
    private EmprestimosController emprestimosController;

    public ReservaController( List<Reserva> reservas) {
        this.reservas = reservas; // Atribui a lista de reservas

        // Inicializa o maiorId com o maior ID das reservas existentes
        this.maiorId = 0;
        for (Reserva reserva : reservas) {
            if (reserva.getNumero() > maiorId) {
                maiorId = reserva.getNumero(); // Atualiza maiorId com o maior ID encontrado
            }
        }

        this.scanner = new Scanner(System.in);
    }

    public void setEmprestimosController(EmprestimosController emprestimosController) {
        this.emprestimosController = emprestimosController;
    }

    public void criarReserva(Utentes utente, List<Livro> livrosParaReserva, List<Jornal> jornaisParaReserva, LocalDate dataRegisto, LocalDate dataInicio, LocalDate dataFim) {
        if (utente == null) {
            System.out.println("Erro: O utente informado é inválido.");
            return;
        }

        // Verifica se a lista de livros e jornais não está vazia
        if ((livrosParaReserva == null || livrosParaReserva.isEmpty()) && (jornaisParaReserva == null || jornaisParaReserva.isEmpty())) {
            System.out.println("Erro: Nenhum livro ou jornal foi selecionado.");
            return;
        }

        // Verifica conflitos de livros
        for (Livro livro : livrosParaReserva) {
            if (verificarLivroEmprestadoOuReservado(livro, dataInicio, dataFim)) {
                return;
            }
        }
        // Verifica conflitos de jornais
        /*for (Jornal jornal : jornaisParaReserva) {
            if (verificarJornalEmprestadoOuReservado(jornal, dataInicio, dataFim)) {
                return;
            }
        }*/

        int numeroReserva = maiorId + 1;  // Número da nova reserva será o maior ID + 1
        maiorId = numeroReserva;  // Atualiza o maiorId para o próximo valor

        // Se todas as verificações passarem, cria a reserva
        Reserva reserva = new Reserva(numeroReserva, utente, livrosParaReserva, jornaisParaReserva, dataRegisto, dataInicio, dataFim);
        reservas.add(reserva);

        // Exibe detalhes da reserva criada
        exibirDetalhesReserva(reserva);
    }

    public void exibirDetalhesReserva(Reserva reserva) {
        System.out.println("\n======= Detalhes da Reserva =======");
        System.out.println("Número: " + reserva.getNumero());
        System.out.println("Utente: " + reserva.getUtente().getNome());
        System.out.println("Data de Registo: " + reserva.getDataRegisto().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Data de Início: " + reserva.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Data de Fim: " + reserva.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        if (!reserva.getLivros().isEmpty()) {
            System.out.println("Livros Reservados:");
            for (Livro livro : reserva.getLivros()) {
                System.out.println(" - " + livro.getNome() + " (ISBN: " + livro.getIsbn() + ")");
            }
        }
        if(!reserva.getJornais().isEmpty()) {
            System.out.println("Jornais/Revista Reservados:");
            for (Jornal jornal : reserva.getJornais()) {
                System.out.println(" - " + jornal.getTitulo() + " (ISSN: " + jornal.getIssn() + ")");
            }
            System.out.println("=".repeat(35));
        }
    }

    public Reserva consultarReserva(int numero) {
        // Itera sobre a lista de reservas para encontrar a reserva pelo número
        for (Reserva reserva : reservas) {
            if (reserva.getNumero() == numero) {
                return reserva;  // Se encontrada, retorna a reserva
            }
        }
        // Se não encontrar a reserva, retorna null
        return null;
    }

    public Reserva buscarReservaPorNumero(int numero) {
        for (Reserva reserva : reservas) {
            if (reserva.getNumero() == numero) {
                exibirDetalhesReserva(reserva);
                return reserva; // Retorna a reserva se o número corresponder
            }
        }
        return null; // Retorna null se a reserva não for encontrada
    }

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

    public boolean removerReserva(int numero) {
        Reserva reserva = consultarReserva(numero);
        if (reserva != null) {
            reservas.remove(reserva);
            System.out.println("Reserva eliminada com sucesso!");
            return true;
        }
        return false;
    }

    /*public List<Reserva> listarReservas() {
        if (reservas.isEmpty()) {
            System.out.println("Não há reservas registadas.");
            return new ArrayList<>(); // Retorna uma lista vazia se não houver reservas
        } else {
            System.out.println("\n=== Lista de Reservas ===");
            // Cabeçalhos das colunas
            System.out.printf("%-10s %-20s %-20s %-20s %-25s %-25s\n", "Número", "Utente", "Data Registo", "Data Início", "Data Fim", "Itens Reservados");

            // Exibindo as reservas
            for (Reserva reserva : reservas) {
                String itensReservados = "";
                for (Object item : reserva.getItens()) {
                    if (item instanceof Livro livro) {
                        itensReservados += livro.getNome() + " (ISBN: " + livro.getIsbn() + "), ";
                    } else if (item instanceof Jornal jornal) {
                        itensReservados += jornal.getTitulo() + " (Data: " + jornal.getDataPublicacao() + "), ";
                    }
                }
                // Remover última vírgula e espaço
                if (!itensReservados.isEmpty()) {
                    itensReservados = itensReservados.substring(0, itensReservados.length() - 2);
                }

                // Exibe a linha da reserva
                System.out.printf("%-10d %-20s %-20s %-20s %-25s %-25s\n",
                        reserva.getNumero(),
                        reserva.getUtente().getNome(),
                        reserva.getDataRegisto().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        reserva.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        reserva.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        itensReservados);
            }
            return reservas; // Retorna a lista de reservas
        }
    }*/


    // Método auxiliar para solicitar uma nova data válida
    private LocalDate solicitarNovaData(String mensagem) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            try {
                System.out.print(mensagem);
                String entrada = scanner.nextLine();
                return LocalDate.parse(entrada, formato);
            } catch (Exception e) {
                System.out.println("Formato de data inválido. Tente novamente.");
            }
        }
    }

    // Método auxiliar para exibir detalhes de uma reserva


    public boolean verificarDataAnterior(LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim.isBefore(dataInicio)) {
            return false;
        } else {
            return true;
        }
    }

    // Método para adicionar livro à reserva
    /*public void adicionarItemNaReserva(Reserva reserva, Object item, LocalDate dataInicioReserva, LocalDate dataFimReserva) {
        if (reserva == null || item == null) {
            System.out.println("Erro: Reserva ou item inválido.");
            return;
        }

        // Verifica se o item é um livro ou jornal
        if (item instanceof Livro livro) {
            // Verifica se o livro já está em outra reserva ou está em empréstimo
            if (verificarLivroEmOutraReservaOuEmprestimo(livro, dataInicioReserva, dataFimReserva)) {
                return;
            }

            // Adiciona o livro à lista de itens da reserva
            reserva.getItens().add(livro);  // Usando getItens() para acessar a lista de objetos
            System.out.println("Livro '" + livro.getNome() + "' adicionado com sucesso à reserva.");

        } else if (item instanceof Jornal jornal) {
            // Verifica se o jornal já está em outra reserva ou está em empréstimo
            if (verificarJornalEmOutraReservaOuEmprestimo(jornal, dataInicioReserva, dataFimReserva)) {
                return;
            }

            // Adiciona o jornal à lista de itens da reserva
            reserva.getItens().add(jornal);  // Usando getItens() para acessar a lista de objetos
            System.out.println("Jornal '" + jornal.getTitulo() + "' adicionado com sucesso à reserva.");
        } else {
            System.out.println("Erro: Tipo de item inválido.");
        }
    }

    // Método auxiliar para verificar se o livro está em outra reserva ou empréstimo
    private boolean verificarLivroEmOutraReservaOuEmprestimoNaReserva(Livro livro, LocalDate dataInicio, LocalDate dataFim) {
        // Verifica se o livro está emprestado no intervalo de datas
        if (emprestimosController.verificarLivroEmprestado(livro, dataInicio, dataFim)) {
            return true;
        }

        // Verifica se o livro já está em outra reserva durante o intervalo de datas
        for (Reserva reserva : reservas) {
            for (Object item : reserva.getItens()) {
                if (item instanceof Livro l) {  // Verifica se o item é um livro
                    if (l.getIsbn().equals(livro.getIsbn())) {
                        if ((dataInicio.isBefore(reserva.getDataFim()) || dataInicio.isEqual(reserva.getDataFim())) &&
                                (dataFim.isAfter(reserva.getDataInicio()) || dataInicio.isEqual(reserva.getDataInicio()))) {
                            System.out.println("Erro: Livro '" + livro.getNome() + "' já está reservado para o período entre "
                                    + reserva.getDataInicio() + " e " + reserva.getDataFim() + ".");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // Método auxiliar para verificar se o jornal está em outra reserva ou empréstimo
    private boolean verificarJornalEmOutraReservaOuEmprestimo(Jornal jornal, LocalDate dataInicioReserva, LocalDate dataFimReserva) {
        // Verifica se o jornal está emprestado no intervalo de datas
        if (emprestimosController.verificarJornalEmprestado(jornal, dataInicioReserva, dataFimReserva)) {
            return true;
        }

        // Verifica se o jornal já está em outra reserva durante o intervalo de datas
        for (Reserva reserva : reservas) {
            for (Object item : reserva.getItens()) {
                if (item instanceof Jornal j) {  // Verifica se o item é um jornal
                    if (j.getTitulo().equals(jornal.getTitulo())) {
                        if ((dataInicioReserva.isBefore(reserva.getDataFim()) || dataInicioReserva.isEqual(reserva.getDataFim())) &&
                                (dataFimReserva.isAfter(reserva.getDataInicio()) || dataFimReserva.isEqual(reserva.getDataInicio()))) {
                            System.out.println("Erro: Jornal '" + jornal.getTitulo() + "' já está reservado para o período entre "
                                    + reserva.getDataInicio() + " e " + reserva.getDataFim() + ".");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // Método para remover livro da reserva
    public void removerLivroDaReserva(Reserva reserva, Livro livro) {
        if (reserva == null || livro == null) {
            System.out.println("Erro: Reserva ou livro inválido.");
            return;
        }

        // Verifica se a reserva tem mais de um livro
        long numeroLivros = reserva.getItens().stream()
                .filter(item -> item instanceof Livro)
                .count();

        if (numeroLivros <= 1) {
            System.out.println("Erro: A reserva precisa ter pelo menos dois livros para poder remover um.");
            return;
        }

        // Verifica se o livro está na lista de itens da reserva
        boolean livroRemovido = reserva.getItens().removeIf(item -> item instanceof Livro && ((Livro) item).getIsbn().equals(livro.getIsbn()));

        if (livroRemovido) {
            System.out.println("Livro '" + livro.getNome() + "' removido com sucesso da reserva.");
        } else {
            System.out.println("Erro: O livro '" + livro.getNome() + "' não está na reserva.");
        }
    }

    private boolean verificarLivroEmOutraReservaOuEmprestimo(Livro livro, LocalDate dataInicioReserva, LocalDate dataFimReserva) {

        // Verifica se o livro está emprestado no intervalo de datas
        if (emprestimosController.verificarLivroEmprestado(livro, dataInicioReserva, dataFimReserva)) {
            return true;
        }

        // Verifica se o livro já está em outra reserva durante o intervalo de datas
        for (Reserva reserva : reservas) {
            for (Object item : reserva.getItens()) {
                if (item instanceof Livro l) {  // Verifica se o item é um livro
                    if (l.getIsbn().equals(livro.getIsbn())) {  // Comparação pelo ISBN
                        // Verifica se há sobreposição de datas
                        if ((dataInicioReserva.isBefore(reserva.getDataFim()) || dataInicioReserva.isEqual(reserva.getDataFim())) &&
                                (dataFimReserva.isAfter(reserva.getDataInicio()) || dataFimReserva.isEqual(reserva.getDataInicio()))) {
                            System.out.println("Erro: Livro '" + livro.getNome() + "' já está reservado para o período entre "
                                    + reserva.getDataInicio() + " e " + reserva.getDataFim() + ".");
                            return true;
                        }
                    }
                }
            }
        }

        return false; // O livro não está emprestado nem reservado no intervalo de datas
    }*/

    public boolean verificarLivroReservado(Livro livro, LocalDate dataInicioEmprestimo, LocalDate dataFimEmprestimo) {
        for (Reserva reserva : reservas) {
            for (Object item : reserva.getLivros()) {
                if (item instanceof Livro l) {  // Verifica se o item é um livro
                    if (l.getIsbn().equals(livro.getIsbn())) {
                        LocalDate dataInicioReserva = reserva.getDataInicio();
                        LocalDate dataFimReserva = reserva.getDataFim();

                        // Verifica se há sobreposição de datas
                        if (!(dataFimEmprestimo.isBefore(dataInicioReserva) || dataInicioEmprestimo.isAfter(dataFimReserva))) {
                            return true;  // O livro está reservado no período informado
                        }
                    }
                }
            }
        }
        return false;  // O livro não está reservado no período informado
    }

    public boolean verificarJornalReservado(Jornal jornal, LocalDate dataInicioEmprestimo, LocalDate dataFimEmprestimo) {
        for (Reserva reserva : reservas) {
            for (Object item : reserva.getJornais()) {
                if (item instanceof Jornal j) {  // Verifica se o item é um livro
                    if (j.getIssn().equals(jornal.getIssn())) {
                        LocalDate dataInicioReserva = reserva.getDataInicio();
                        LocalDate dataFimReserva = reserva.getDataFim();

                        // Verifica se há sobreposição de datas
                        if (!(dataFimEmprestimo.isBefore(dataInicioReserva) || dataInicioEmprestimo.isAfter(dataFimReserva))) {
                            return true;  // O livro está reservado no período informado
                        }
                    }
                }
            }
        }
        return false;  // O livro não está reservado no período informado
    }

    private boolean verificarLivroEmprestadoOuReservado(Livro livro, LocalDate dataInicioReserva, LocalDate dataFimReserva) {

        // verifica se o livro já está reservado
        if (emprestimosController.verificarLivroEmprestado(livro, dataInicioReserva, dataFimReserva)) {
            return true;
        }

        for (Reserva reserva : reservas) {
            for (Livro l : reserva.getLivros()) {
                if (l.getIsbn().equals(livro.getIsbn())) {  // Comparação pelo ISBN
                    LocalDate dataInicioOutraReserva = reserva.getDataInicio();
                    LocalDate dataFimOutraReserva = reserva.getDataFim();

                    // Se a devolução ainda não foi registrada, usamos a data prevista para devolução
                    if (dataFimOutraReserva == null) {
                        dataFimOutraReserva = reserva.getDataFim();
                    }

                    // Verifica se há sobreposição de datas
                    if ((dataInicioReserva.isBefore(dataFimOutraReserva) || dataInicioReserva.isEqual(dataFimOutraReserva)) &&
                            (dataFimReserva.isAfter(dataInicioOutraReserva) || dataFimReserva.isEqual(dataInicioOutraReserva))) {
                        System.out.println("Erro: Livro '" + livro.getNome() + "' já está emprestado para o período entre "
                                + dataInicioOutraReserva + " e " + dataFimOutraReserva + ".");
                        return true;
                    }
                }
            }
        }

        return false; // O livro não está emprestado nem reservado no intervalo de datas
    }

    /*public boolean verificarJornalEmprestadoOuReservado(Jornal jornal, LocalDate dataInicioEmprestimo, LocalDate dataFimEmprestimo) {

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
    }*/

}