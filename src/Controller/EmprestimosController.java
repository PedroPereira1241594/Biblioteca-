package Controller;

import Model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class EmprestimosController {
    private final List<Emprestimos> emprestimos;
    private LivroController livroController;
    private int maiorId;  // Inicializa com o maior ID dos empréstimos existentes
    private Scanner scanner;
    private ReservaController reservaController; // Adicionando a referência ao ReservaController
    private List<Livro> livros;
    private List<Jornal> jornais;

    // Construtor
    public EmprestimosController(ReservaController reservaController, List<Emprestimos> emprestimos, List<Livro> livros, List<Jornal> jornais) {
        this.emprestimos = emprestimos; // Use a mesma lista do main
        this.reservaController = reservaController;
        this.livros = livros;
        this.jornais = jornais;
        this.scanner = new Scanner(System.in);

        // Inicializa maiorId com o maior ID dos empréstimos já existentes
        this.maiorId = calcularMaiorId(emprestimos);
    }

    private int calcularMaiorId(List<Emprestimos> emprestimos) {
        int maior = 0;
        for (Emprestimos emprestimos1 : emprestimos) {
            if (emprestimos1.getNumero() > maior) {
                maior = emprestimos1.getNumero();
            }
        }
        return maior;
    }


    // Setter para LivroController (evita dependências circulares)
    public void setLivroController(LivroController livroController) {
        this.livroController = livroController;
    }

    public List<Emprestimos> getEmprestimos(){
        return emprestimos;
    }

    public void criarEmprestimo(Utentes utente, List<ItemEmprestavel> itensParaEmprestimo, LocalDate dataInicio, LocalDate dataPrevistaDevolucao, LocalDate dataEfetivaDevolucao) {
        if (utente == null) {
            System.out.println("Erro: O utente informado é inválido.");
            return;
        }
        if (itensParaEmprestimo == null || itensParaEmprestimo.isEmpty()) {
            System.out.println("Erro: Nenhum item foi selecionado.");
            return;
        }

        // Verifica duplicados na lista de itens
        Set<ItemEmprestavel> itensUnicos = new HashSet<>(itensParaEmprestimo);
        if (itensUnicos.size() < itensParaEmprestimo.size()) {
            System.out.println("Erro: Não é permitido incluir itens repetidos no mesmo empréstimo.");
            return;
        }

        // Verifica conflitos com reservas e disponibilidade de itens
        for (ItemEmprestavel item : itensParaEmprestimo) {
            if (item instanceof Livro) {
                if (reservaController.verificarItemReservado((Livro) item, dataInicio, dataPrevistaDevolucao) && verificarItemEmprestado(item, dataInicio, dataPrevistaDevolucao)) {
                    System.out.println("Erro: O livro '" + item.getIdentificador() + "' já está reservado para o período entre " + dataInicio + " e " + dataPrevistaDevolucao + " e não pode ser emprestado.");
                    return;
                }
            } else if (item instanceof Jornal) {
                // Para jornais, verifique se já está emprestado ou reservado
                if (itemPossuiEmprestimoAtivo(item, dataInicio, dataPrevistaDevolucao) && verificarItemEmprestado(item, dataInicio, dataPrevistaDevolucao)) {
                    System.out.println("Erro: O jornal '" + item.getIdentificador() + "' já está emprestado no período solicitado.");
                    return;
                }
            }
        }

        this.maiorId = calcularMaiorId(emprestimos);

        // Atualiza o número do empréstimo com o maior ID encontrado + 1
        int numeroEmprestimo = maiorId + 1;

        // Criação do empréstimo
        Emprestimos novoEmprestimo = new Emprestimos(numeroEmprestimo, utente, itensParaEmprestimo, dataInicio, dataPrevistaDevolucao, dataEfetivaDevolucao);
        emprestimos.add(novoEmprestimo);

        // Exibe detalhes do novo empréstimo
        exibirDetalhesEmprestimo(novoEmprestimo);
    }

    // Exibe detalhes do empréstimo de forma estruturada
    public void exibirDetalhesEmprestimo(Emprestimos emprestimo) {
        System.out.println("\n========== Detalhes do Empréstimo ==========");
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

        System.out.println("Itens Emprestados:");
        for (ItemEmprestavel item : emprestimo.getItens()) {
            if (item instanceof Livro) {
                System.out.println(" - Livro: " + reservaController.pesquisaISBN(item.getIdentificador()) + " (ISBN: " + ((Livro) item).getIsbn() + ")");
            } else if (item instanceof Jornal) {
                System.out.println(" - Jornal: " + reservaController.pesquisaISSN(item.getIdentificador()) + " (ISSN: " + ((Jornal) item).getIssn() + ")");
            }
        }
        System.out.println("=".repeat(44));
    }

    // CRUD: Read
    public Emprestimos consultarEmprestimo(int numero) {
        // Itera sobre a lista de empréstimos para encontrar o empréstimo pelo número
        for (Emprestimos emprestimo : emprestimos) {
            if (emprestimo.getNumero() == numero) {
                return emprestimo;  // Se encontrado, retorna o empréstimo
            }
        }
        return null; // Se não encontrar o empréstimo, retorna null
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
    public boolean removerEmprestimo(int numero) {
        Emprestimos emprestimo = consultarEmprestimo(numero);
        if (emprestimo != null) {
            emprestimos.remove(emprestimo);
            return true;
        }
        return false;
    }

    // Verifica se o item já está emprestado no período entre dataInicio e dataPrevistaDevolucao
    public boolean itemPossuiEmprestimoAtivo(ItemEmprestavel item, LocalDate dataInicio, LocalDate dataPrevistaDevolucao) {
        System.out.println("Verificando se o item está emprestado: " + item.getTitulo() + " (" + item.getISBN() + ")");

        for (Emprestimos emprestimo : emprestimos) {
            for (ItemEmprestavel itemEmprestado : emprestimo.getItens()) {
                if (item.equals(itemEmprestado)) {
                    System.out.println("Encontrado empréstimo para o item: " + item.getTitulo() + " (" + item.getISBN() + ")");
                    System.out.println("Data de Início do Empréstimo: " + emprestimo.getDataInicio());
                    System.out.println("Data Prevista de Devolução: " + emprestimo.getDataPrevistaDevolucao());

                    if ((dataInicio.isBefore(emprestimo.getDataPrevistaDevolucao()) && dataPrevistaDevolucao.isAfter(emprestimo.getDataInicio())) ||
                            (dataInicio.isBefore(emprestimo.getDataEfetivaDevolucao()) && dataPrevistaDevolucao.isAfter(emprestimo.getDataInicio()))) {
                        System.out.println("O item está emprestado no período solicitado.");
                        return true; // O item já está emprestado no período solicitado
                    }
                }
            }
        }
        return false; // O item não está emprestado no período solicitado
    }


    public boolean verificarDataAnterior(LocalDate dataInicio, LocalDate dataDevolucao) {
        if (dataDevolucao == null) {
            return true;  // Se a data efetiva de devolução for null, considera válido
        }
        return !dataDevolucao.isBefore(dataInicio);  // Verifica se a data de devolução não é anterior à data de início
    }

    public List<Emprestimos> listarTodosEmprestimos() {
        return emprestimos; // Retorna uma cópia da lista de empréstimos
    }

    public void adicionarItemEmprestimo(int numero, ItemEmprestavel item) {
        Emprestimos emprestimo = consultarEmprestimo(numero);
        if (emprestimo != null) {
            if (!emprestimo.getItens().contains(item)) {
                emprestimo.getItens().add(item);
                System.out.println("Item '" + item.getTitulo() + "' adicionado ao empréstimo com sucesso!");
            } else {
                System.out.println("O item '" + item.getTitulo() + "' já está neste empréstimo.");
            }
        } else {
            System.out.println("Erro: Empréstimo não encontrado.");
        }
    }

    public void removerItemEmprestimo(int numero, ItemEmprestavel item) {
        Emprestimos emprestimo = consultarEmprestimo(numero);
        if (emprestimo != null) {
            if (emprestimo.getItens().contains(item)) {
                emprestimo.getItens().remove(item);
                System.out.println("Item '" + item.getTitulo() + "' removido do empréstimo com sucesso!");
            } else {
                System.out.println("O item '" + item.getTitulo() + "' não está neste empréstimo.");
            }
        } else {
            System.out.println("Erro: Empréstimo não encontrado.");
        }
    }

    public boolean verificarItemEmprestado(ItemEmprestavel item, LocalDate dataInicio, LocalDate dataFim) {
        for (Emprestimos emprestimos1 : emprestimos) {
            for (ItemEmprestavel i : emprestimos1.getItens()) {
                if (i.getIdentificador().equals(item.getIdentificador())) {
                    LocalDate dataInicioEmprestimo = emprestimos1.getDataInicio();
                    LocalDate dataFimEmprestimo;

                    if(emprestimos1.getDataEfetivaDevolucao() == null){
                        dataFimEmprestimo = emprestimos1.getDataPrevistaDevolucao();
                    } else {
                        dataFimEmprestimo = emprestimos1.getDataEfetivaDevolucao();
                    }

                    // Verifica se há sobreposição de datas entre reserva e empréstimo
                    if (!(dataFim.isBefore(dataInicioEmprestimo) || dataInicio.isAfter(dataFimEmprestimo))) {
                        return true; // O item está reservado no período solicitado
                    }
                }
            }
        }
        return false; // O item não está reservado no período informado
    }


}
