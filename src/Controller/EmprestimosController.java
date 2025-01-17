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

    // Construtor
    public EmprestimosController(LivroController livroController) {
        this.emprestimos = new ArrayList<>();
        this.livroController = livroController;
        this.scanner = new Scanner(System.in); // Instanciando o scanner aqui
    }

    // Setter para LivroController (evita dependências circulares)
    public void setLivroController(LivroController livroController) {
        this.livroController = livroController;
    }

    // CRUD: Create
    public void criarEmprestimo(Utentes utente, List<Livro> livrosParaEmprestimo, LocalDate dataInicio, LocalDate dataPrevistaDevolucao) {
        if (livrosParaEmprestimo.isEmpty()) {
            System.out.println("Erro: Nenhum livro foi selecionado.");
            return;
        }

        // Verifica se há livros duplicados no empréstimo
        Set<Livro> livrosUnicos = new HashSet<>(livrosParaEmprestimo);
        if (livrosUnicos.size() < livrosParaEmprestimo.size()) {
            System.out.println("Erro: Não é possível adicionar livros repetidos no mesmo empréstimo.");
            return;
        }

        // Valida data de devolução
        while (dataPrevistaDevolucao.isBefore(dataInicio)) {
            System.out.println("Erro: A data prevista de devolução não pode ser anterior à data de início.");
            dataPrevistaDevolucao = solicitarNovaData("Digite a nova data prevista de devolução (formato: dd/MM/yyyy): ");
        }

        // Verifica a disponibilidade dos livros
        for (Livro livro : livrosParaEmprestimo) {
            for (Emprestimos emprestimo : emprestimos) {
                if (emprestimo.getLivros().contains(livro)) {
                    LocalDate emprestimoInicio = emprestimo.getDataInicio();
                    LocalDate emprestimoFim = emprestimo.getDataPrevistaDevolucao();

                    if (!(dataPrevistaDevolucao.isBefore(emprestimoInicio) || dataInicio.isAfter(emprestimoFim))) {
                        System.out.println("Não é possível criar o empréstimo. O livro '" + livro.getNome() + "' já está emprestado no intervalo de datas fornecido.");
                        return;
                    }
                }
            }
        }

        // Criação e adição do empréstimo
        int numeroEmprestimo = proximoNumeroEmprestimo++;
        Emprestimos emprestimo = new Emprestimos(numeroEmprestimo, utente, livrosParaEmprestimo, dataInicio, dataPrevistaDevolucao);
        emprestimos.add(emprestimo);

        exibirDetalhesEmprestimo(emprestimo);  // Exibe detalhes do empréstimo criado
    }

    // Exibe detalhes do empréstimo de forma estruturada
    private void exibirDetalhesEmprestimo(Emprestimos emprestimo) {
        System.out.println("\n=====================================");
        System.out.println("Número do Empréstimo: " + emprestimo.getNumero());
        System.out.println("Utente: " + emprestimo.getUtente().getNome() + " (NIF: " + emprestimo.getUtente().getNif() + ")");
        System.out.println("Data de Início: " + emprestimo.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Data Prevista de Devolução: " + emprestimo.getDataPrevistaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Livros Emprestados:");
        for (Livro livro : emprestimo.getLivros()) {
            System.out.println(" - " + livro.getNome() + " (ISBN: " + livro.getIsbn() + ")");
        }
    }

    // CRUD: Read
    public Emprestimos consultarEmprestimo(int numero) {
        Emprestimos emprestimo = emprestimos.stream()
                .filter(e -> e.getNumero() == numero)
                .findFirst()
                .orElse(null);

        if (emprestimo == null) {
            System.out.println("Erro: Empréstimo com número '" + numero + "' não encontrado.");
        } else {
            exibirDetalhesEmprestimo(emprestimo);
        }
        return emprestimo; // Retorna o empréstimo encontrado
    }

    // CRUD: Update
    public void atualizarEmprestimo(int numero, LocalDate novaDataEfetivaDevolucao) {
        Emprestimos emprestimo = consultarEmprestimo(numero);
        if (emprestimo != null) {
            boolean dataValida = false;
            while (!dataValida) {
                // Solicita nova data efetiva de devolução
                System.out.print("Informe a nova data efetiva de devolução (dd/MM/yyyy): ");
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                novaDataEfetivaDevolucao = lerData(formato);

                // Verifica se a nova data de devolução é válida em relação à data de início
                if (novaDataEfetivaDevolucao.isBefore(emprestimo.getDataInicio())) {
                    System.out.println("Erro: A data efetiva de devolução não pode ser anterior à data de início do empréstimo.");
                    // Pergunta ao usuário se deseja tentar novamente ou cancelar
                    System.out.print("Deseja tentar novamente? (S/N): ");
                    String resposta = scanner.nextLine().toUpperCase();

                    if (resposta.equals("S")) {
                        continue; // Volta ao início do loop para tentar novamente
                    } else {
                        System.out.println("Operação cancelada.");
                        return; // Cancelar operação
                    }
                } else {
                    // Se a data for válida, atualiza a data efetiva de devolução
                    emprestimo.setDataEfetivaDevolucao(novaDataEfetivaDevolucao);
                    System.out.println("Data efetiva de devolução atualizada com sucesso para: " + novaDataEfetivaDevolucao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    dataValida = true; // Encerra o loop
                }
            }
        }
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

    // Registrar devolução
    public void registrarDevolucao(int numero) {
        Emprestimos emprestimo = consultarEmprestimo(numero);
        if (emprestimo != null) {
            emprestimo.setDataEfetivaDevolucao(LocalDate.now());
            System.out.println("Devolução registrada na data: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            System.out.println("Empréstimo não encontrado.");
        }
    }

    // Método auxiliar para solicitar uma nova data do usuário
    private LocalDate solicitarNovaData(String mensagem) {
        LocalDate novaData = null;
        boolean dataValida = false;
        while (!dataValida) {
            System.out.print(mensagem);
            String dataStr = scanner.nextLine();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                novaData = LocalDate.parse(dataStr, formatter);
                dataValida = true; // A data é válida
            } catch (Exception e) {
                System.out.println("Erro: Data inválida. Por favor, use o formato dd/MM/yyyy.");
            }
        }
        return novaData;
    }

    // CRUD: Delete
    public void removerEmprestimo(int numero) {
        Emprestimos emprestimo = consultarEmprestimo(numero);
        if (emprestimo != null) {
            emprestimos.remove(emprestimo);
            System.out.println("Empréstimo removido com sucesso.");
        } else {
            System.out.println("Empréstimo não encontrado.");
        }
    }

    // Listar todos os empréstimos
    public void listarEmprestimos() {
        if (emprestimos.isEmpty()) {
            System.out.println("\nNenhum empréstimo registrado.");
        } else {
            System.out.println("\n=== Lista de Empréstimos ===");
            System.out.printf("%-10s %-25s %-25s %-20s %-25s %-30s\n",
                    "Número", "Utente", "Data Início", "Data Prev. Devolução", "Livros Emprestados", "Data Devolução");

            for (Emprestimos emprestimo : emprestimos) {
                String livros = "";
                for (Livro livro : emprestimo.getLivros()) {
                    livros += livro.getNome() + " (ISBN: " + livro.getIsbn() + "), ";
                }
                // Remover última vírgula e espaço
                if (!livros.isEmpty()) {
                    livros = livros.substring(0, livros.length() - 2);
                }

                System.out.printf("%-10d %-25s %-25s %-20s %-25s %-30s\n",
                        emprestimo.getNumero(),
                        emprestimo.getUtente().getNome(),
                        emprestimo.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        emprestimo.getDataPrevistaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        livros,
                        emprestimo.getDataEfetivaDevolucao() != null
                                ? emprestimo.getDataEfetivaDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                : "Em andamento");
            }
        }
    }

    // Listar apenas os empréstimos ativos
    public List<Emprestimos> listarEmprestimosAtivos() {
        List<Emprestimos> ativos = new ArrayList<>();
        for (Emprestimos emprestimo : emprestimos) {
            if (emprestimo.getDataEfetivaDevolucao() == null) {
                ativos.add(emprestimo);
            }
        }
        return ativos;
    }
}
