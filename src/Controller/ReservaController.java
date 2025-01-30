package Controller;

import Model.*;

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
    private List<Livro> livros;
    private List<Jornal> jornais;

    public ReservaController(EmprestimosController emprestimosController, List<Reserva> reservas, List<Livro> livros, List<Jornal> jornais) {
        this.emprestimosController = emprestimosController; // Atribui o EmprestimosController
        this.reservas = reservas; // Atribui a lista de reservas
        this.livros = livros;
        this.jornais = jornais;
        this.maiorId = calcularMaiorId(reservas);
        this.scanner = new Scanner(System.in);
    }
    private int calcularMaiorId(List<Reserva> reservas) {
        int maior = 0;
        for (Reserva reserva : reservas) {
            if (reserva.getNumero() > maior) {
                maior = reserva.getNumero();
            }
        }
        return maior;
    }

    public void setEmprestimosController(EmprestimosController emprestimosController) {
        this.emprestimosController = emprestimosController;
    }

    public void criarReserva(Utentes utente, List<ItemEmprestavel> itensParaReserva, LocalDate dataRegisto, LocalDate dataInicio, LocalDate dataFim, EmprestimosController emprestimosController) {
        if (utente == null) {
            System.out.println("Erro: O utente informado é inválido.");
            return;
        }

        if (itensParaReserva == null || itensParaReserva.isEmpty()) {
            System.out.println("Erro: Nenhum item foi selecionado para reserva.");
            return;
        }

        // Valida data de fim da reserva
        if (dataFim.isBefore(dataInicio)) {
            System.out.println("Erro: A data de fim da reserva não pode ser anterior à data de início.");
            return;
        }

        // Verifica se os itens possuem empréstimos ativos
        for (ItemEmprestavel item : itensParaReserva) {
            if (emprestimosController.itemPossuiEmprestimoAtivo(item, dataInicio, dataFim)) {
                System.out.println("Erro: O item '" + item.getIdentificador() + "' está emprestado no período solicitado.");
                return;
            }
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

    public boolean removerReserva(int numero) {
        Reserva reserva = consultarReserva(numero);
        if (reserva != null) {
            reservas.remove(reserva);
            return true;
        }
        return false;
    }

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

    public boolean verificarDataAnterior(LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim.isBefore(dataInicio)) {
            return false;
        } else {
            return true;
        }
    }

    // Método para adicionar livro à reserva
    public void adicionarItemNaReserva(int numero, ItemEmprestavel item, LocalDate dataInicioReserva, LocalDate dataFimReserva) {
        Reserva reserva = consultarReserva(numero);
        if (reserva != null) {
            if (!reserva.getItens().contains(item)) {
                reserva.getItens().add(item);
                System.out.println("Item '" + item.getTitulo() + "' adicionado com sucesso à reserva!");
            } else {
                System.out.println("O item '" + item.getTitulo() + "' já está nesta reserva.");
            }
        } else {
            System.out.println("Erro: Reserva não encontrada.");
        }
    }


    public void removerItemDaReserva(int numero, ItemEmprestavel item) {
        Reserva reserva = consultarReserva(numero);
        if (reserva != null) {
            if (reserva.getItens().contains(item)) {
                // Verifica se a reserva tem mais de um item
                if (reserva.getItens().size() > 1) {
                    reserva.getItens().remove(item);
                    System.out.println("Item '" + item.getTitulo() + "' removido com sucesso da reserva!");
                } else {
                    System.out.println("Erro: A reserva precisa ter pelo menos dois itens para poder remover um.");
                }
            } else {
                System.out.println("Erro: O item '" + item.getTitulo() + "' não está nesta reserva.");
            }
        } else {
            System.out.println("Erro: Reserva não encontrada.");
        }
    }


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

    public List<Reserva> listarTodasReservas() {
        return reservas; // Retorna a lista de reservas
    }

    // Método para pesquisar Livro por ISBN
    public String pesquisaISBN(String ISBN) {
        for (Livro livro : livros) {
            if (livro.getIsbn().equalsIgnoreCase(ISBN)) { // Verifica se o ISBN coincide.
                return livro.getNome(); // Retorna o livro encontrado.
            }
        }
        return null; // Retorna null se nenhum livro for encontrado.
    }

    // Método para pesquisar Jornal/Revista por ISSN
    public String pesquisaISSN(String ISSN) {
        for (Jornal jornal : jornais) { // Iterando sobre a lista de jornais
            if (jornal.getIssn().equalsIgnoreCase(ISSN)) { // Verifica se o ISSN coincide
                return jornal.getTitulo(); // Retorna o jornal encontrado
            }
        }
        return null; // Retorna null se nenhum jornal for encontrado.
    }


}
