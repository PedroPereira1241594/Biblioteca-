package View;
/** View para as pesquisas e estatísticas do sistema
 * @author Pedro Pereira
 * @since 2025-01-20
 */

import Controller.PesquisaEstatisticasController;
import Model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class PesquisaEstatisticasView {
    private PesquisaEstatisticasController pesquisaEstatisticasController;
    private Scanner scanner;

    /** Construtor para aceder ao controller das pesquisas/estatísticas
     * @param scanner
     * @param pesquisaEstatisticasController
     */
    public PesquisaEstatisticasView(Scanner scanner, PesquisaEstatisticasController pesquisaEstatisticasController) {
        this.scanner = scanner;
        this.pesquisaEstatisticasController = pesquisaEstatisticasController;
    }

    /** Método do enu principal das pesquisas e estatísticas
     * Diversas opções
     *  1. Pesquisar Livros/Revistas/Jornais pelo ISBN/ISSN
     *  2. Pesquisar Empréstimos e Reservas num intervalo de datas
     *  3. Tempo Médio de Empréstimos num Intervalo de Datas
     *  4. Item Mais Requisitado no Intervalo de Datas
     *  5. Utentes com Atraso Superior a N Dias
     *  0. Voltar ao menu principal...
     */
    public void exibirMenu() {
        int opcao;
        do {
            System.out.println("\n=== Menu de Pesquisas ===");
            System.out.println("1. Pesquisar Livros/Revistas/Jornais pelo ISBN/ISSN");
            System.out.println("2. Pesquisar Empréstimos e Reservas num intervalo de datas");
            System.out.println("3. Tempo Médio de Empréstimos num Intervalo de Datas");
            System.out.println("4. Item Mais Requisitado no Intervalo de Datas");
            System.out.println("5. Utentes com Atraso Superior a N Dias");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

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
                case 5:
                    mostrarUtentesComAtraso();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    /** Método auxiliar para a pesquisa de consultar Livro por ISBN ou Jornal/Revista por ISSN
     */
    private void pesquisarPorISBNouISSN() {
        int option;
        do {
            System.out.println("\nPretende pesquisar Livros ou Jornais/Revistas?");
            System.out.println("1. Livros");
            System.out.println("2. Jornais/Revistas");
            System.out.println("0. Voltar ao menu anterior...");
            System.out.print("Escolha uma opção: ");
            option = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            switch (option) {
                case 1:
                    System.out.print("\nInsira o ISBN do Livro: ");
                    String ISBN = scanner.nextLine();
                    Livro livroEncontrado = pesquisaEstatisticasController.pesquisaISBN(ISBN); // Busca pelo ISBN

                    if (livroEncontrado != null) {
                        System.out.println("\n======== Livro Encontrado ========");
                        System.out.println("Título: " + livroEncontrado.getNome());
                        System.out.println("Editora: " + livroEncontrado.getEditora());
                        System.out.println("Categoria: " + livroEncontrado.getCategoria());
                        System.out.println("Ano: " + livroEncontrado.getAno());
                        System.out.println("Autor: " + livroEncontrado.getAutor());
                        System.out.println("ISBN: " + livroEncontrado.getIsbn());
                        System.out.println("=".repeat(34));
                    } else {
                        System.out.println("Nenhum livro encontrado com o ISBN fornecido.");
                    }
                    break;
                case 2:
                    System.out.print("\nInsira o ISSN do Jornal: ");
                    String ISSN = scanner.nextLine();
                    Jornal jornalEncontrado = pesquisaEstatisticasController.pesquisaISSN(ISSN); // Busca pelo ISSN
                    if (jornalEncontrado != null) {
                        System.out.println("\n======= Jornal/Revista Encontrado =======");
                        System.out.println("Título: " + jornalEncontrado.getTitulo());
                        System.out.println("Editora: " + jornalEncontrado.getEditora());
                        System.out.println("Categoria: " + jornalEncontrado.getCategoria());
                        System.out.println("Data de Publicação: " + jornalEncontrado.getDataPublicacao());
                        System.out.println("ISSN: " + jornalEncontrado.getIssn());
                        System.out.println("=".repeat(41));
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

    // Método para mostrar os empréstimos e/ou reservados no intervalos de datas
    private void pesquisarEntreDatas() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataInicio;
        LocalDate dataFim;
        // Loop para validação das datas
        while (true) {
            try {
                System.out.print("\nInsira a Data de Início da Pesquisa (dd/MM/yyyy): ");
                dataInicio = lerData(formato);
                System.out.print("Insira a Data de Fim da Pesquisa (dd/MM/yyyy): ");
                dataFim = lerData(formato);

                // Verifica se a data de fim é anterior à data de início
                if (pesquisaEstatisticasController.verificarDataAnterior(dataInicio, dataFim)) {
                    System.out.println("Erro: A data de fim não pode ser anterior à data de início! Tente novamente.");
                } else {
                    break; // Sai do loop se as datas forem válidas
                }
            } catch (Exception e) {
                System.out.println("Erro: Entrada inválida. Por favor, insira as datas no formato correto (dd/MM/yyyy).");
            }
        }


        System.out.println("\nEscolha o que deseja pesquisar:");
        System.out.println("1. Apenas Empréstimos");
        System.out.println("2. Apenas Reservas");
        System.out.println("3. Ambos (Empréstimos e Reservas)");
        System.out.print("Opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        switch (opcao) {
            case 1:
                List<Emprestimos> emprestimos = pesquisaEstatisticasController.listarEmprestimosPorIntervalo(dataInicio, dataFim);
                mostrarEmprestimos(emprestimos);
                pesquisaEstatisticasController.contarEmprestimosEntreDatas(dataInicio, dataFim);
                break;
            case 2:
                List<Reserva> reservas = pesquisaEstatisticasController.listarReservasPorIntervalo(dataInicio, dataFim);
                mostrarReservas(reservas);
                pesquisaEstatisticasController.contarReservasEntreDatas(dataInicio, dataFim);
                break;
            case 3:
                List<Emprestimos> todosEmprestimos = pesquisaEstatisticasController.listarEmprestimosPorIntervalo(dataInicio, dataFim);
                List<Reserva> todasReservas = pesquisaEstatisticasController.listarReservasPorIntervalo(dataInicio, dataFim);
                mostrarEmprestimos(todosEmprestimos);
                mostrarReservas(todasReservas);
                pesquisaEstatisticasController.contarEmprestimosEntreDatas(dataInicio, dataFim);
                pesquisaEstatisticasController.contarReservasEntreDatas(dataInicio, dataFim);
                break;
            default:
                System.out.println("Opção inválida!");
        }

    }

    // Método auxiliar para mostrar empréstimos
    private void mostrarEmprestimos(List<Emprestimos> emprestimos) {
        if (emprestimos.isEmpty()) {
            System.out.println("\nNenhum empréstimo encontrado no intervalo de datas fornecido.");
        } else {
            // Exibir os empréstimos
            System.out.println("\n=== Lista de Empréstimos no Intervalo de Datas ===");
            System.out.printf("%-10s %-50s %-20s %-25s %-25s %-80s\n",
                    "Número", "Utente", "Data Início", "Data Prev. Devolução", "Data Devolução", "Itens Emprestados");

            for (Emprestimos emprestimo : emprestimos) {
                // Validação do nome do utente
                String utenteNome = (emprestimo.getUtente() != null && emprestimo.getUtente().getNome() != null)
                        ? emprestimo.getUtente().getNome()
                        : "Desconhecido";



                // Validação das datas
                String dataInicioStr = (emprestimo.getDataInicio() != null)
                        ? emprestimo.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "Data desconhecida";

                String dataPrevista = (emprestimo.getDataPrevistaDevolucao() != null)
                        ? emprestimo.getDataPrevistaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "Data desconhecida";

                String dataEfetiva = (emprestimo.getDataEfetivaDevolucao() != null)
                        ? emprestimo.getDataEfetivaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "Pendente";

                // Validação e construção da string de itens
                String itens = "Sem itens";
                if (emprestimo.getItens() != null && !emprestimo.getItens().isEmpty()) {
                    List<ItemEmprestavel> itensEmprestados = emprestimo.getItens();
                    itens = ""; // Inicializa vazio
                    for (int i = 0; i < itensEmprestados.size(); i++) {
                        ItemEmprestavel item = itensEmprestados.get(i);
                        if (item != null) {
                            if (item instanceof Livro) {
                                itens += "ISBN: " + ((Livro) item).getIsbn();
                            } else if (item instanceof Jornal) {
                                itens += "ISSN: " + ((Jornal) item).getIssn();
                            } else {
                                itens += "Item desconhecido";
                            }
                        } else {
                            itens += "Item desconhecido";
                        }

                        if (i < itensEmprestados.size() - 1) {
                            itens += ", ";
                        }
                    }
                }

                // Exibe as informações do empréstimo
                System.out.printf("%-10d %-50s %-20s %-25s %-25s %-80s\n",
                        emprestimo.getNumero(), utenteNome, dataInicioStr, dataPrevista, dataEfetiva, itens);
            }
        }
    }

    // Método auxiliar para mostrar reservas
    private void mostrarReservas(List<Reserva> reservas) {
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva encontrada no intervalo de datas fornecido.");
        } else {
            // Exibir as reservas
            System.out.println("\n=== Lista de Reservas no Intervalo de Datas ===");

            // Cabeçalhos das colunas
            System.out.printf("%-10s %-50s %-20s %-25s %-25s %-80s\n",
                    "Número", "Utente", "Data Registo", "Data Início", "Data Fim", "Itens Reservados");

            for (Reserva reserva : reservas) {
                // Validação do nome do utente
                String utenteNome = (reserva.getUtente() != null && reserva.getUtente().getNome() != null)
                        ? reserva.getUtente().getNome()
                        : "Desconhecido";



                // Validação das datas
                String dataRegisto = (reserva.getDataRegisto() != null)
                        ? reserva.getDataRegisto().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "Data desconhecida";

                String dataInicio = (reserva.getDataInicio() != null)
                        ? reserva.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "Data desconhecida";

                String dataFim = (reserva.getDataFim() != null)
                        ? reserva.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "Pendente";

                // Validação e construção da string de itens
                String itens = "Sem itens";
                if (reserva.getItens() != null && !reserva.getItens().isEmpty()) {
                    List<ItemEmprestavel> itensReservados = reserva.getItens();
                    itens = ""; // Inicializa vazio
                    for (int i = 0; i < itensReservados.size(); i++) {
                        ItemEmprestavel item = itensReservados.get(i);
                        if (item != null) {
                            if (item instanceof Livro) {
                                itens += "ISBN: " + ((Livro) item).getIsbn();
                            } else if (item instanceof Jornal) {
                                itens += "ISSN: " + ((Jornal) item).getIssn();
                            } else {
                                itens += "Item desconhecido";
                            }
                        } else {
                            itens += "Item desconhecido";
                        }

                        if (i < itensReservados.size() - 1) {
                            itens += ", ";
                        }
                    }
                }

                // Exibe as informações do empréstimo
                System.out.printf("%-10d %-50s %-20s %-25s %-25s %-80s\n",
                        reserva.getNumero(), utenteNome, dataRegisto, dataInicio, dataFim, itens);
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

    // Método para mostrar o tempo médio de empréstimos no intervalo de datas fornecido
    private void mostrarTempoMedioEmprestimosNoIntervalo() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataInicio;
        LocalDate dataFim;
        // Loop para validação das datas
        while (true) {
            try {
                System.out.print("\nInsira a Data de Início da Pesquisa (dd/MM/yyyy): ");
                dataInicio = lerData(formato);
                System.out.print("Insira a Data de Fim da Pesquisa (dd/MM/yyyy): ");
                dataFim = lerData(formato);

                // Verifica se a data de fim é anterior à data de início
                if (pesquisaEstatisticasController.verificarDataAnterior(dataInicio, dataFim)) {
                    System.out.println("Erro: A data de fim não pode ser anterior à data de início! Tente novamente.");
                } else {
                    break; // Sai do loop se as datas forem válidas
                }
            } catch (Exception e) {
                System.out.println("Erro: Entrada inválida. Por favor, insira as datas no formato correto (dd/MM/yyyy).");
            }
        }

        // Buscar todos os empréstimos entre as datas fornecidas
        List<Emprestimos> emprestimos = pesquisaEstatisticasController.listarEmprestimosPorIntervalo(dataInicio, dataFim);

        // Exibir o total de empréstimos encontrados no intervalo
        System.out.println("\nTotal de empréstimos realizados no intervalo: " + emprestimos.size());

        // Calcular e exibir o tempo médio dos empréstimos
        double tempoMedio = pesquisaEstatisticasController.calcularTempoMedioEmpréstimos(dataInicio, dataFim, emprestimos);
        if (tempoMedio > 0) {
            System.out.println("Tempo médio dos empréstimos (em dias): " + tempoMedio);
        } else {
            System.out.println("Não há empréstimos realizados nesse intervalo de datas.");
        }
    }

    // Método para mostrar o item mais requisitado no intervalo de datas fornecido
    private void mostrarItemMaisRequisitado() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataInicio;
        LocalDate dataFim;
        // Loop para validação das datas
        while (true) {
            try {
                System.out.print("\nInsira a Data de Início da Pesquisa (dd/MM/yyyy): ");
                dataInicio = lerData(formato);
                System.out.print("Insira a Data de Fim da Pesquisa (dd/MM/yyyy): ");
                dataFim = lerData(formato);

                // Verifica se a data de fim é anterior à data de início
                if (pesquisaEstatisticasController.verificarDataAnterior(dataInicio, dataFim)) {
                    System.out.println("Erro: A data de fim não pode ser anterior à data de início! Tente novamente.");
                } else {
                    break; // Sai do loop se as datas forem válidas
                }
            } catch (Exception e) {
                System.out.println("Erro: Entrada inválida. Por favor, insira as datas no formato correto (dd/MM/yyyy).");
            }
        }

        // Buscar o item mais requisitado no intervalo de datas
        List<String> itemMaisRequisitado = pesquisaEstatisticasController.pesquisarItensMaisRequisitados(dataInicio, dataFim);

        // Exibir o resultado
        if (itemMaisRequisitado != null) {
            System.out.println("\nO(s) item(ns) mais requisitado(s) entre as datas " + dataInicio.format(formato) + " e " + dataFim.format(formato) + " é: ");
            System.out.println(itemMaisRequisitado);
        } else {
            System.out.println("Não foram encontradas requisições no intervalo de datas fornecido.");
        }
    }

    // Método para mostrar os empréstimos com N dias de atraso
    private void mostrarUtentesComAtraso() {
        System.out.print("Insira o número de dias de atraso: ");
        int diasAtraso = scanner.nextInt();
        scanner.nextLine();  // Limpar buffer

        // Buscar empréstimos com atraso maior que o número de dias informado
        List<Emprestimos> emprestimosComAtraso = pesquisaEstatisticasController.buscarEmprestimosComAtraso(diasAtraso);

        if (emprestimosComAtraso.isEmpty()) {
            System.out.println("Nenhum empréstimo encontrado com atraso superior a " + diasAtraso + " dias.");
        } else {
            System.out.println("\n=== Lista de Utentes com Atraso ===");
            System.out.printf("%-30s %-25s %-25s\n", "Utente", "Data de Empréstimo", "Data Prev. Devolução");

            for (Emprestimos emprestimo : emprestimosComAtraso) {
                String livros = "";
                if (!livros.isEmpty()) {
                    livros = livros.substring(0, livros.length() - 2);  // Remover última vírgula
                }

                System.out.printf("%-30s %-25s %-25s\n",
                        emprestimo.getUtente().getNome(),
                        emprestimo.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        emprestimo.getDataPrevistaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
        }
    }


}
