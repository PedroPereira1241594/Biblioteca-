package View;
/**
 * Classe responsável por apresentar a interface de utilizador para pesquisas e estatísticas.
 * Contém o menu principal e opções para pesquisar itens, empréstimos, reservas e outras estatísticas do sistema.
 *
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
    /**
     * Controlador responsável pelas operações de pesquisa e estatísticas.
     */
    private PesquisaEstatisticasController pesquisaEstatisticasController;

    /**
     * Scanner para captura de entrada do utilizador.
     */
    private Scanner scanner;

    /**
     * Construtor para inicializar o controller e o scanner.
     *
     * @param scanner Scanner para leitura de dados.
     * @param pesquisaEstatisticasController Controlador para realizar as operações de pesquisa e estatísticas.
     */
    public PesquisaEstatisticasView(Scanner scanner, PesquisaEstatisticasController pesquisaEstatisticasController) {
        this.scanner = scanner;
        this.pesquisaEstatisticasController = pesquisaEstatisticasController;
    }

    /**
     * Mostra o menu principal de pesquisas e estatísticas, permitindo ao utilizador aceder diversas funcionalidades.
     * As opções incluem:
     * <ul>
     *   <li>Pesquisar itens por ISBN/ISSN.</li>
     *   <li>Pesquisar empréstimos e reservas em intervalos de datas.</li>
     *   <li>Calcular o tempo médio de empréstimos.</li>
     *   <li>Identificar o item mais requisitado.</li>
     *   <li>Mostrar utentes com atrasos superiores a um determinado número de dias.</li>
     *   <li>Sair do menu.</li>
     * </ul>
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

    /**
     * Permite ao utilizador pesquisar itens no sistema por meio de ISBN (para livros) ou ISSN (para jornais/revistas).
     * O resultado da pesquisa é exibido no ecrã.
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
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.print("\nInsira o ISBN do Livro: ");
                    String ISBN = scanner.nextLine();
                    Livro livroEncontrado = pesquisaEstatisticasController.pesquisaISBN(ISBN); // Procura pelo ISBN

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
                    Jornal jornalEncontrado = pesquisaEstatisticasController.pesquisaISSN(ISSN); // Procurar pelo ISSN
                    if (jornalEncontrado != null) {
                        System.out.println("\n======= Jornal/Revista Encontrado =======");
                        System.out.println("Título: " + jornalEncontrado.getTitulo());
                        System.out.println("Editora: " + jornalEncontrado.getEditora());
                        System.out.println("Categoria: " + jornalEncontrado.getCategoria());
                        System.out.println("Data de Publicação: " + jornalEncontrado.getDataPublicacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
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

    /**
     * Realiza a pesquisa de empréstimos e/ou reservas em um intervalo de datas fornecido pelo utilizador.
     * Mostra os resultados encontrados no ecrã.
     */
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
        scanner.nextLine();

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

    /**
     * Mostra uma lista de empréstimos no intervalo de datas fornecido.
     *
     * @param emprestimos Lista de empréstimos a serem apresentados.
     *                    Se estiver vazia, será exibida uma mensagem informativa.
     * Cada empréstimo mostrará as seguintes informações:
     * - Número do empréstimo
     * - Nome do utente
     * - Data de início do empréstimo
     * - Data prevista para devolução
     * - Data efetiva de devolução (se já devolvido)
     * - Lista de itens emprestados (ISBN/ISSN ou "Item desconhecido" caso inválido)
     */
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

    /**
     * Mostra uma lista de reservas no intervalo de datas fornecido.
     *
     * @param reservas Lista de reservas a serem apresentadas.
     *                 Se estiver vazia, será exibida uma mensagem informativa.
     * Cada reserva exibirá as seguintes informações:
     * - Número da reserva
     * - Nome do utente
     * - Data de registo da reserva
     * - Data de início da reserva
     * - Data de término da reserva
     * - Lista de itens reservados (ISBN/ISSN ou "Item desconhecido" caso inválido)
     */
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

    /**
     * Lê e valida uma data inserida pelo utilizador no formato dd/MM/yyyy.
     * Caso a data seja inválida, solicita novamente ao utilizador.
     *
     * @param formato O formato esperado para a data.
     * @return A data lida e validada.
     */
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

    /**
     * Calcula e mostra o tempo médio de empréstimos realizados em um intervalo de datas fornecido.
     */
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

        // Procurar todos os empréstimos entre as datas fornecidas
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

    /**
     * Identifica e mostra o item ou itens mais requisitado em um intervalo de datas fornecido pelo utilizador.
     */
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

        // Procurar o item mais requisitado no intervalo de datas
        List<String> itemMaisRequisitado = pesquisaEstatisticasController.pesquisarItensMaisRequisitados(dataInicio, dataFim);

        // Exibir o resultado
        if (itemMaisRequisitado != null) {
            System.out.println("\nO(s) item(ns) mais requisitado(s) entre as datas " + dataInicio.format(formato) + " e " + dataFim.format(formato) + " é: ");
            System.out.println(itemMaisRequisitado);
        } else {
            System.out.println("Não foram encontradas requisições no intervalo de datas fornecido.");
        }
    }

    /**
     * Mostra a lista de utentes com empréstimos atrasados superiores a um número de dias especificado pelo utilizador.
     */
    private void mostrarUtentesComAtraso() {
        System.out.print("Insira o número de dias de atraso: ");
        int diasAtraso = scanner.nextInt();
        scanner.nextLine();

        // Procurar empréstimos com atraso maior que o número de dias informado
        List<Emprestimos> emprestimosComAtraso = pesquisaEstatisticasController.procurarEmprestimosComAtraso(diasAtraso);

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
