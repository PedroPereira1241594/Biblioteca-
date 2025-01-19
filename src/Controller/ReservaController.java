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
    private int contadorReservas;
    private Scanner scanner;

    // Modificado para receber a lista de reservas do Main
    public ReservaController(List<Reserva> reservas) {
        this.reservas = reservas;  // Agora a lista de reservas vem do Main
        this.contadorReservas = 1;
        this.scanner = new Scanner(System.in);
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
            if (emprestimosController.livroPossuiEmprestimoAtivo(livro)) {
                System.out.println("Erro: Não é possível criar a reserva. O livro '" + livro.getNome() + "' está emprestado sem data de devolução efetiva.");
                return false; // Retorna false se algum livro estiver emprestado sem devolução
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

        // Se todas as verificações passarem, cria a reserva
        Reserva reserva = new Reserva(contadorReservas++, utente, livrosParaReserva, dataRegisto, dataInicio, dataFim);
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
        if (reserva != null) {
            if (novaDataFim.isBefore(novaDataInicio)) {
                System.out.println("Erro: A data de fim não pode ser anterior à data de início.");
                return;
            }
            reserva.setDataInicio(novaDataInicio);
            reserva.setDataFim(novaDataFim);
            System.out.println("Reserva atualizada com sucesso!");
            System.out.println(reserva);
        }
    }

    public void removerReserva(int numero) {
        Reserva reserva = consultarReserva(numero);
        if (reserva != null) {
            reservas.remove(reserva);
            System.out.println("Reserva removida com sucesso!");
        }
    }

    public void listarReservas() {
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
        System.out.println("\nReserva criada com sucesso:");
        System.out.println("\n=== Detalhes da Reserva ===");
        System.out.println("Número: " + reserva.getNumero());
        System.out.println("Utente: " + reserva.getUtente().getNome());
        System.out.println("Data de Registo: " + reserva.getDataRegisto().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Data de Início: " + reserva.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Data de Fim: " + reserva.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        System.out.println("Livros Reservados:");
        for (Livro livro : reserva.getLivros()) {
            System.out.println(" - " + livro.getNome() + " (ISBN: " + livro.getIsbn() + ")");
        }
    }

    public Reserva buscarReservaPorNumero(int numero) {
        for (Reserva reserva : reservas) {
            if (reserva.getNumero() == numero) {
                return reserva; // Retorna a reserva se o número corresponder
            }
        }
        return null; // Retorna null se a reserva não for encontrada
    }



}
