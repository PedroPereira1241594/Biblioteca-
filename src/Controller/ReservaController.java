package Controller;

import Model.Livro;
import Model.Reserva;
import Model.Utentes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Scanner;

public class ReservaController {
    private List<Reserva> reservas;
    private int maiorId;
    private Scanner scanner;
    private EmprestimosController emprestimosController;

    public ReservaController(EmprestimosController emprestimosController, List<Reserva> reservas) {
        this.emprestimosController = emprestimosController; // Atribui o EmprestimosController
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

    public boolean criarReserva(Utentes utente, List<Livro> livrosParaReserva, LocalDate dataRegisto, LocalDate dataInicio, LocalDate dataFim, EmprestimosController emprestimosController) {
        if (livrosParaReserva.isEmpty()) {
            System.out.println("Erro: Nenhum livro foi selecionado.");
            return false; // Retorna false se não houver livros para reservar
        }

        // Verifica se há livros duplicados na reserva
        Set<Livro> livrosUnicos = new HashSet<>(livrosParaReserva);
        if (livrosUnicos.size() < livrosParaReserva.size()) {
            System.out.println("Erro: Não é possível adicionar livros repetidos na mesma reserva.");
            return false; // Retorna false se houver livros duplicados
        }

        // Valida data de fim da reserva
        while (dataFim.isBefore(dataInicio)) {
            System.out.println("Erro: A data de fim da reserva não pode ser anterior à data de início.");
            dataFim = solicitarNovaData("Introduza a nova data de fim da reserva (dd/MM/yyyy): ");
        }

        // Verifica se os livros possuem empréstimos sem data efetiva de devolução
        for (Livro livro : livrosParaReserva) {
            if (emprestimosController.itemPossuiEmprestimoAtivo(livro, dataInicio, dataFim)) {
                System.out.println("Erro: O livro '" + livro.getNome() + "' está emprestado no intervalo de datas fornecido.");
                return false;
            }
        }

        // Verifica a disponibilidade dos livros nas reservas existentes
        for (Livro livro : livrosParaReserva) {
            for (Reserva reserva : reservas) {
                if (reserva.getLivros().contains(livro)) {
                    LocalDate reservaInicio = reserva.getDataInicio();
                    LocalDate reservaFim = reserva.getDataFim();

                    if (!(dataFim.isBefore(reservaInicio) || dataInicio.isAfter(reservaFim))) {
                        System.out.println("Erro: Não é possível criar a reserva. O livro '" + livro.getNome() +
                                "' já está reservado no intervalo de datas fornecido.");
                        return false; // Retorna false se algum livro já estiver reservado no intervalo
                    }
                }
            }
        }

        int numeroReserva = maiorId + 1;  // Número da nova reserva será o maior ID + 1
        maiorId = numeroReserva;  // Atualiza o maiorId para o próximo valor

        // Se todas as verificações passarem, cria a reserva
        Reserva reserva = new Reserva(numeroReserva, utente, livrosParaReserva, dataRegisto, dataInicio, dataFim);
        reservas.add(reserva);

        // Exibe detalhes da reserva criada
        exibirDetalhesReserva(reserva);
        return true; // Retorna true se a reserva for criada com sucesso
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

    public void removerReserva(int numero) {
        Reserva reserva = consultarReserva(numero);
        if (reserva != null) {
            reservas.remove(reserva);
            System.out.println("Reserva eliminada com sucesso!");
        }
    }

    public List<Reserva> listarReservas() {
        if (reservas.isEmpty()) {
            System.out.println("Não há reservas registadas.");
        } else {
            System.out.println("\n=== Lista de Reservas ===");
            // Cabeçalhos das colunas
            System.out.printf("%-10s %-20s %-20s %-20s %-25s %-25s\n", "Número", "Utente", "Data Registo", "Data Início", "Data Fim", "Livros Reservados");

            // Exibindo as reservas
            for (Reserva reserva : reservas) {
                String livrosReservados = "";
                for (Livro livro : reserva.getLivros()) {
                    livrosReservados += livro.getNome() + " (ISBN: " + livro.getIsbn() + "), ";
                }
                // Remover última vírgula e espaço
                if (!livrosReservados.isEmpty()) {
                    livrosReservados = livrosReservados.substring(0, livrosReservados.length() - 2);
                }

                // Exibe a linha da reserva
                System.out.printf("%-10d %-20s %-20s %-20s %-25s %-25s\n",
                        reserva.getNumero(),
                        reserva.getUtente().getNome(),
                        reserva.getDataRegisto().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        reserva.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        reserva.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        livrosReservados);
            }
        }
        return reservas;
    }

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
    public void exibirDetalhesReserva(Reserva reserva) {
        System.out.println("\n======= Detalhes da Reserva =======");
        System.out.println("Número: " + reserva.getNumero());
        System.out.println("Utente: " + reserva.getUtente().getNome());
        System.out.println("Data de Registo: " + reserva.getDataRegisto().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Data de Início: " + reserva.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Data de Fim: " + reserva.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        System.out.println("Livros Reservados:");
        for (Livro livro : reserva.getLivros()) {
            System.out.println(" - " + livro.getNome() + " (ISBN: " + livro.getIsbn() + ")");
        }
        System.out.println("=".repeat(35));
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

    public boolean verificarDataAnterior(LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim.isBefore(dataInicio)) {
            return false;
        } else {
            return true;
        }
    }

    // Método para adicionar livro à reserva
    public void adicionarLivroNaReserva(Reserva reserva, Livro livro, LocalDate dataInicioReserva, LocalDate dataFimReserva) {
        if (reserva == null || livro == null) {
            System.out.println("Erro: Reserva ou livro inválido.");
            return;
        }

        // Verifica se o livro já está em outra reserva ou está em empréstimo
        if (verificarLivroEmOutraReservaOuEmprestimo(livro, dataInicioReserva, dataFimReserva)) {

            return;
        }

        // Adiciona o livro à lista de livros da reserva
        reserva.getLivros().add(livro);
        System.out.println("Livro '" + livro.getNome() + "' adicionado com sucesso à reserva.");
    }

    // Método para remover livro da reserva
    public void removerLivroDaReserva(Reserva reserva, Livro livro) {
        if (reserva == null || livro == null) {
            System.out.println("Erro: Reserva ou livro inválido.");
            return;
        }

        // Verifica se a reserva tem mais de um livro
        if (reserva.getLivros().size() <= 1) {
            System.out.println("Erro: A reserva precisa ter pelo menos dois livros para poder remover um.");
            return;
        }

        // Verifica se o livro está na lista de livros da reserva
        if (reserva.getLivros().contains(livro)) {
            reserva.getLivros().remove(livro);
            System.out.println("Livro '" + livro.getNome() + "' removido com sucesso da reserva.");
        } else {
            System.out.println("Erro: O livro '" + livro.getNome() + "' não está na reserva.");
        }
    }

    private boolean verificarLivroEmOutraReservaOuEmprestimo(Livro livro, LocalDate dataInicioReserva, LocalDate dataFimReserva) {

        // Verifica se o livro está emprestado no intervalo de datas
        if (emprestimosController.itemPossuiEmprestimoAtivo(livro, dataInicioReserva, dataFimReserva)) {
            return true;
        }

        // Verifica se o livro já está em outra reserva durante o intervalo de datas
        for (Reserva reserva : reservas) {
            for (Livro l : reserva.getLivros()) {
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

        return false; // O livro não está emprestado nem reservado no intervalo de datas
    }

    public boolean verificarLivroReservado(Livro livro, LocalDate dataInicioEmprestimo, LocalDate dataFimEmprestimo) {
        for (Reserva reserva : reservas) {
            for (Livro l : reserva.getLivros()) {
                if (l.getIsbn().equals(livro.getIsbn())) {
                    LocalDate dataInicioReserva = reserva.getDataInicio();
                    LocalDate dataFimReserva = reserva.getDataFim();

                    // Verifica se há sobreposição de datas
                    if (!(dataFimEmprestimo.isBefore(dataInicioReserva) || dataInicioEmprestimo.isAfter(dataFimReserva))) {
                        return true;
                    }
                }
            }
        }
        return false; // O livro não está reservado no período informado
    }

}
