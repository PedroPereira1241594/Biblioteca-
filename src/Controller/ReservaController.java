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

    public ReservaController() {
        this.reservas = new ArrayList<>();
        this.contadorReservas = 1;
        this.scanner = new Scanner(System.in);
    }

    public void criarReserva(Utentes utente, List<Livro> livrosParaReserva, LocalDate dataRegisto, LocalDate dataInicio, LocalDate dataFim) {
        if (livrosParaReserva.isEmpty()) {
            System.out.println("Erro: Nenhum livro foi selecionado.");
            return;
        }

        // Verifica se há livros duplicados na reserva
        Set<Livro> livrosUnicos = new HashSet<>(livrosParaReserva);
        if (livrosUnicos.size() < livrosParaReserva.size()) {
            System.out.println("Erro: Não é possível adicionar livros repetidos na mesma reserva.");
            return;
        }

        // Valida data de fim da reserva
        while (dataFim.isBefore(dataInicio)) {
            System.out.println("Erro: A data de fim da reserva não pode ser anterior à data de início.");
                dataFim = solicitarNovaData("Introduza a nova data de fim da reserva (dd/MM/yyyy): ");
        }

        // Verifica a disponibilidade dos livros
        for (Livro livro : livrosParaReserva) {
            for (Reserva reserva : reservas) {
                if (reserva.getLivros().contains(livro)) {
                    LocalDate reservaInicio = reserva.getDataInicio();
                    LocalDate reservaFim = reserva.getDataFim();

                    if (!(dataFim.isBefore(reservaInicio) || dataInicio.isAfter(reservaFim))) {
                        System.out.println("Erro: Não é possível criar a reserva. O livro '" + livro.getNome() +
                                "' já está reservado no intervalo de datas fornecido.");
                        return;
                    }
                }
            }
        }

        // Criação e adição da reserva
        Reserva reserva = new Reserva(contadorReservas++, utente, livrosParaReserva, dataRegisto, dataInicio, dataFim);
        reservas.add(reserva);

        exibirDetalhesReserva(reserva); // Exibe detalhes da reserva criada
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
            System.out.println("Não há reservas registradas.");
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
    private void exibirDetalhesReserva(Reserva reserva) {
        System.out.println("\nReserva criada com sucesso:");
        System.out.println(reserva);
    }
}
