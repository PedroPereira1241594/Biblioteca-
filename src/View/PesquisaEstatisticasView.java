package View;

import Controller.PesquisaEstatisticasController;
import Model.Emprestimos;
import Model.Jornal;
import Model.Livro;
import Model.Reserva;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class PesquisaEstatisticasView {
    private PesquisaEstatisticasController pesquisaEstatisticasController;
    private Scanner scanner;

    public PesquisaEstatisticasView(Scanner scanner, PesquisaEstatisticasController pesquisaEstatisticasController) {
        this.scanner = scanner;
        this.pesquisaEstatisticasController = pesquisaEstatisticasController;
    }

    public void exibirMenu() {
        int opcao;
        do {
            System.out.println("\n=== Menu de Pesquisas ===");
            System.out.println("1. Pesquisar Livros/Revistas/Jornais pelo ISBN/ISSN");
            System.out.println("2. Pesquisar Empréstimos e Reservas num intervalo de datas");
            System.out.println("3. Exibir Tempo Médio de Empréstimos em um Intervalo de Datas");
            System.out.println("4. Exibir Item Mais Requisitado no Intervalo de Datas"); // Nova opção
            System.out.println("0. Voltar ao menu anterior...");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();  // Limpar buffer

            switch (opcao) {
                case 1:
                    pesquisarPorISBNouISSN();
                    break;
                case 2:
                    pesquisarEntreDatas();
                    break;
                case 3:
                    mostrarTempoMedioEmprestimosNoIntervalo();
                    break;
                case 4:
                    mostrarItemMaisRequisitado();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }



    private void pesquisarPorISBNouISSN() {
        int option;
        do {
            System.out.println("\nPretende pesquisar Livros ou Jornais/Revistas?");
            System.out.println("1. Livros");
            System.out.println("2. Jornais/Revistas");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            option = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            switch (option) {
                case 1:
                    System.out.print("\nInsira o ISBN do Livro: ");
                    String ISBN = scanner.nextLine();
                    Livro livroEncontrado = pesquisaEstatisticasController.pesquisaISBN(ISBN); // Busca pelo ISBN

                    if (livroEncontrado != null) {
                        System.out.println("Livro encontrado:");
                        System.out.println("Título: " + livroEncontrado.getNome());
                        System.out.println("Editora: " + livroEncontrado.getEditora());
                        System.out.println("Categoria: " + livroEncontrado.getCategoria());
                        System.out.println("Ano: " + livroEncontrado.getAno());
                        System.out.println("Autor: " + livroEncontrado.getAutor());
                        System.out.println("ISBN: " + livroEncontrado.getIsbn());
                    } else {
                        System.out.println("Nenhum livro encontrado com o ISBN fornecido.");
                    }
                    break;
                case 2:
                    System.out.print("\nInsira o ISSN do Jornal: ");
                    String ISSN = scanner.nextLine();
                    Jornal jornalEncontrado = pesquisaEstatisticasController.pesquisaISSN(ISSN); // Busca pelo ISSN
                    if (jornalEncontrado != null) {
                        System.out.println("Jornal ou Revista encontrada:");
                        System.out.println("Título: " + jornalEncontrado.getTitulo());
                        System.out.println("Editora: " + jornalEncontrado.getEditora());
                        System.out.println("Categoria: " + jornalEncontrado.getCategoria());
                        System.out.println("Data de Publicação: " + jornalEncontrado.getDataPublicacao());
                        System.out.println("ISSN: " + jornalEncontrado.getIssn());
                    } else {
                        System.out.println("Nenhum jornal ou revista encontrada com o ISSN fornecido.");
                    }
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente...");
            }
        } while (option != 0);
    }

    private void pesquisarEntreDatas() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.print("\nInsira a Data de Início da Pesquisa (dd/MM/yyyy): ");
        LocalDate dataInicio = lerData(formato);
        System.out.print("Insira a Data de Fim da Pesquisa (dd/MM/yyyy): ");
        LocalDate dataFim = lerData(formato);

        System.out.println("\nEscolha o que deseja pesquisar:");
        System.out.println("1. Apenas Empréstimos");
        System.out.println("2. Apenas Reservas");
        System.out.println("3. Ambos (Empréstimos e Reservas)");
        System.out.print("Opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        switch (opcao) {
            case 1:
                List<Emprestimos> emprestimos = pesquisaEstatisticasController.buscarEmprestimosEntreDatas(dataInicio, dataFim);
                exibirEmprestimos(emprestimos);
                break;
            case 2:
                List<Reserva> reservas = pesquisaEstatisticasController.buscarReservasEntreDatas(dataInicio, dataFim);
                exibirReservas(reservas);
                break;
            case 3:
                List<Emprestimos> todosEmprestimos = pesquisaEstatisticasController.buscarEmprestimosEntreDatas(dataInicio, dataFim);
                List<Reserva> todasReservas = pesquisaEstatisticasController.buscarReservasEntreDatas(dataInicio, dataFim);
                exibirEmprestimos(todosEmprestimos);
                exibirReservas(todasReservas);
                break;
            default:
                System.out.println("Opção inválida!");
        }

        // Exibir total de empréstimos realizados no intervalo
        long totalEmprestimos = pesquisaEstatisticasController.contarEmprestimosEntreDatas(dataInicio, dataFim);
        System.out.println("\nTotal de empréstimos no intervalo de datas fornecidas '" + dataInicio +"' - '" + dataFim + "': " + totalEmprestimos); // Exibindo o total de empréstimos
    }

    // Método auxiliar para exibir empréstimos
    private void exibirEmprestimos(List<Emprestimos> emprestimos) {
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo encontrado no intervalo de datas fornecido.");
        } else {
            System.out.println("\n=== Lista de Empréstimos ===");
            // Ajustando os espaçamentos para garantir que "Livros Emprestados" tenha mais espaço
            System.out.printf("%-10s %-20s %-20s %-25s %-50s %-25s\n",
                    "Número", "Utente", "Data Início", "Data Prev. Devolução", "Livros Emprestados", "Data Devolução");

            // Exibindo os empréstimos
            for (Emprestimos emprestimo : emprestimos) {
                String livros = "";
                for (Livro livro : emprestimo.getLivros()) {
                    livros += livro.getNome() + " (ISBN: " + livro.getIsbn() + "), ";
                }
                // Remover última vírgula e espaço
                if (!livros.isEmpty()) {
                    livros = livros.substring(0, livros.length() - 2);
                }

                // Imprime o empréstimo com a lista de livros
                System.out.printf("%-10d %-20s %-20s %-25s %-50s %-25s\n",
                        emprestimo.getNumero(),
                        emprestimo.getUtente().getNome(),
                        emprestimo.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        emprestimo.getDataPrevistaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        livros,
                        emprestimo.getDataEfetivaDevolucao() != null ?
                                emprestimo.getDataEfetivaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) :
                                "Em aberto");
            }
        }
    }

    // Método auxiliar para exibir reservas
    private void exibirReservas(List<Reserva> reservas) {
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva encontrada no intervalo de datas fornecido.");
        } else {
            System.out.println("\n=== Lista de Reservas ===");
            // Cabeçalhos das colunas
            System.out.printf("%-10s %-20s %-20s %-20s %-25s %-25s\n",
                    "Número", "Utente", "Data Registo", "Data Início", "Data Fim", "Livros Reservados");

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

    // Método para ler a data no formato correto
    private LocalDate lerData(DateTimeFormatter formato) {
        while (true) {
            try {
                String dataTexto = scanner.nextLine();
                return LocalDate.parse(dataTexto, formato);
            } catch (DateTimeParseException e) {
                System.out.print("Data inválida. Tente novamente (dd/MM/yyyy): ");
            }
        }
    }

    private void mostrarTempoMedioEmprestimosNoIntervalo() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Solicitar data de início e fim para o intervalo
        System.out.print("\nInsira a Data de Início da Pesquisa (dd/MM/yyyy): ");
        LocalDate dataInicio = lerData(formato);
        System.out.print("Insira a Data de Fim da Pesquisa (dd/MM/yyyy): ");
        LocalDate dataFim = lerData(formato);

        // Buscar todos os empréstimos entre as datas fornecidas
        List<Emprestimos> emprestimos = pesquisaEstatisticasController.buscarEmprestimosEntreDatas(dataInicio, dataFim);

        // Exibir o total de empréstimos encontrados no intervalo
        System.out.println("\nTotal de empréstimos realizados no intervalo: " + emprestimos.size());

        // Calcular e exibir o tempo médio dos empréstimos
        double tempoMedio = pesquisaEstatisticasController.calcularTempoMedioEmpréstimos(dataInicio, dataFim);
        if (tempoMedio > 0) {
            System.out.println("Tempo médio dos empréstimos (em dias): " + tempoMedio);
        } else {
            System.out.println("Não há empréstimos realizados nesse intervalo de datas.");
        }
    }

    private void mostrarItemMaisRequisitado() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Solicitar data de início e fim para o intervalo
        System.out.print("\nInsira a Data de Início da Pesquisa (dd/MM/yyyy): ");
        LocalDate dataInicio = lerData(formato);
        System.out.print("Insira a Data de Fim da Pesquisa (dd/MM/yyyy): ");
        LocalDate dataFim = lerData(formato);

        // Buscar o item mais requisitado no intervalo de datas
        List<String> itemMaisRequisitado = pesquisaEstatisticasController.pesquisarItensMaisRequisitados(dataInicio, dataFim);

        // Exibir o resultado
        if (itemMaisRequisitado != null) {
            System.out.println("\nO item mais requisitado entre as datas " + dataInicio.format(formato) + " e " + dataFim.format(formato) + " é: ");
            System.out.println("Livro com o ISBN: " + itemMaisRequisitado);
        } else {
            System.out.println("Não foram encontradas requisições no intervalo de datas fornecido.");
        }
    }






}
