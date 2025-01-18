package Controller;

import Model.Emprestimos;
import Model.Livro;
import Model.Reserva;
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
        if (utente == null) {
            System.out.println("Erro: O utente informado é inválido.");
            return;
        }

        if (livrosParaEmprestimo == null || livrosParaEmprestimo.isEmpty()) {
            System.out.println("Erro: Nenhum livro foi selecionado.");
            return;
        }

        // Verifica duplicados na lista de livros
        Set<Livro> livrosUnicos = new HashSet<>(livrosParaEmprestimo);
        if (livrosUnicos.size() < livrosParaEmprestimo.size()) {
            System.out.println("Erro: Não é permitido incluir livros repetidos no mesmo empréstimo.");
            return;
        }

        // Verifica se a data prevista de devolução é válida
        if (dataPrevistaDevolucao.isBefore(dataInicio)) {
            System.out.println("Erro: A data prevista de devolução não pode ser anterior à data de início.");
            return;
        }

        // Verifica conflitos com reservas
        for (Livro livro : livrosParaEmprestimo) {
            for (Reserva reserva : Reserva.getListaReservas()) { // Acessando a lista de reservas com o método estático
                if (reserva.getLivros().contains(livro)) {
                    LocalDate reservaInicio = reserva.getDataInicio();
                    LocalDate reservaFim = reserva.getDataFim();

                    if (!(dataPrevistaDevolucao.isBefore(reservaInicio) || dataInicio.isAfter(reservaFim))) {
                        System.out.println("Erro: O livro '" + livro.getNome() + "' com o ISBN '" + livro.getIsbn() +"' já está reservado no período solicitado.");
                        return;
                    }
                }
            }
        }

        // Verifica disponibilidade dos livros
        for (Livro livro : livrosParaEmprestimo) {
            for (Emprestimos emprestimo : emprestimos) {
                if (emprestimo.getLivros().contains(livro)) {
                    LocalDate emprestimoInicio = emprestimo.getDataInicio();
                    LocalDate emprestimoFim = emprestimo.getDataPrevistaDevolucao();

                    if (!(dataPrevistaDevolucao.isBefore(emprestimoInicio) || dataInicio.isAfter(emprestimoFim))) {
                        System.out.println("Erro: O livro '" + livro.getNome() + "' já está emprestado no período solicitado.");
                        return;
                    }
                }
            }
        }

        // Criação do empréstimo
        int numeroEmprestimo = proximoNumeroEmprestimo++;
        Emprestimos novoEmprestimo = new Emprestimos(numeroEmprestimo, utente, livrosParaEmprestimo, dataInicio, dataPrevistaDevolucao);
        emprestimos.add(novoEmprestimo);

        // Exibe detalhes do novo empréstimo
        exibirDetalhesEmprestimo(novoEmprestimo);
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
