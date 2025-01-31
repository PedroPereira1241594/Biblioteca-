package Controller;

import Model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerir os jornais/revistas do sistema.
 * Controlador para criação, atualização, listagem e remoção de jornais/revistas.
 */
public class JornalController {
    public static ArrayList<Jornal> jornais;
    private List<Reserva> reservas;
    private List<Emprestimos> emprestimos;

    /**
     * Construtor da classe JornalController.
     *
     * @param jornais Lista de jornais/revistas disponíveis.
     * @param reservas Lista de reservas realizadas.
     * @param emprestimos Lista de empréstimos ativos.
     */
    public JornalController(ArrayList<Jornal> jornais, List<Reserva> reservas, List<Emprestimos> emprestimos) {
        this.jornais = jornais;
        this.reservas = reservas;
        this.emprestimos = emprestimos;
    }

    /**
     * Cria e adiciona um novo jornal ou revista ao sistema.
     *
     * @param issn Identificador ISSN do jornal/revista.
     * @param titulo Título do jornal/revista.
     * @param categoria Categoria do jornal/revista.
     * @param editora Nome da editora.
     * @param dataPublicacao Data de publicação.
     */
    public void criarJornal(String issn, String titulo, String categoria, String editora, LocalDate dataPublicacao) {
        if (issn == null || issn.trim().isEmpty()) {
            System.out.println("Erro: ISSN inválido. Não é possível criar o jornal.");
            return;
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            System.out.println("Erro: Título inválido. Não é possível criar o jornal.");
            return;
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            System.out.println("Erro: Categoria inválida. Não é possível criar o jornal.");
            return;
        }
        if (editora == null || editora.trim().isEmpty()) {
            System.out.println("Erro: Editora inválida. Não é possível criar o jornal.");
            return;
        }
        if (dataPublicacao == null) {
            System.out.println("Erro: Data de publicação inválida.");
            return;
        }

        Jornal novoJornal = new Jornal(issn, titulo, categoria, editora, dataPublicacao);
        jornais.add(novoJornal);
        System.out.println("Jornal/Revista adicionado com sucesso!");
    }

    /**
     * Lista todos os jornais/revistas registados no sistema.
     */
    public void listarJornais() {
        if (jornais.isEmpty()) {
            System.out.println("Nenhum jornal ou revista registado.");
            return;
        }

        System.out.println("\n=== Lista de Jornais/Revistas ===");
        System.out.printf("%-20s %-50s %-50s %-50s %-15s%n",
                "ISSN", "Título", "Categoria", "Editora", "Data Publicação");
        System.out.println("-".repeat(190));

        for (Jornal jornal : jornais) {
            System.out.printf(
                    "%-20s %-50s %-50s %-50s %-15s%n",
                    jornal.getIssn(),
                    jornal.getTitulo(),
                    jornal.getCategoria(),
                    jornal.getEditora(),
                    jornal.getDataPublicacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
        }
    }

    /**
     * Atualiza os dados de um jornal/revista já registado no sistema.
     *
     * @param issn ISSN do jornal/revista a ser atualizado.
     * @param novoIssn Novo ISSN.
     * @param novoTitulo Novo título.
     * @param novaCategoria Nova categoria.
     * @param novaEditora Nova editora.
     * @param novaDataPublicacao Nova data de publicação.
     */
    public void atualizarJornal(String issn, String novoIssn, String novoTitulo, String novaCategoria, String novaEditora, LocalDate novaDataPublicacao) {
        Jornal jornalEncontrado = null;

        // Procura o jornal pelo ISSN
        for (Jornal jornal : jornais) {
            if (jornal.getIssn().equalsIgnoreCase(issn)) {
                jornalEncontrado = jornal;
                break;
            }
        }

        if (jornalEncontrado == null) {
            System.out.println("Nenhum Jornal/Revista encontrado com o ISSN fornecido.");
            return;
        }
        System.out.printf(String.valueOf(novaDataPublicacao));

        // Atualiza os dados do jornal encontrado
        if (!novoIssn.isEmpty()) jornalEncontrado.setIssn(novoIssn);
        if (!novoTitulo.isEmpty()) jornalEncontrado.setTitulo(novoTitulo);
        if (!novaCategoria.isEmpty()) jornalEncontrado.setCategoria(novaCategoria);
        if (!novaEditora.isEmpty()) jornalEncontrado.setEditora(novaEditora);

        if (novaDataPublicacao != null) {
            try {
                jornalEncontrado.setDataPublicacao(novaDataPublicacao);
            } catch (DateTimeParseException e) {
                System.out.println("Data de publicação inválida! Utilize o formato dd/MM/yyyy.");
            }
        }

        System.out.println("Jornal/revista atualizado com sucesso!");
    }

    /**
     * Remove um jornal/revista do sistema.
     * Faz a verificação se o jornal/revista está associado a reservas ou empréstimos.
     * @param issn ISSN do jornal/revista a ser removido.
     */
    public void eliminarJornal(String issn) {
        Jornal jornalEncontrado = null;

        // Procura o jornal pelo ISSN
        for (Jornal jornal : jornais) {
            if (jornal.getIssn().equalsIgnoreCase(issn)) {
                jornalEncontrado = jornal;
                break;
            }
        }

        if (jornalEncontrado == null) {
            System.out.println("Nenhum Jornal/Revista encontrado com o ISSN fornecido.");
            return;
        }

        // Verifica se o jornal está associado a alguma reserva ativa.
        boolean jornalReservado = false;
        for (Reserva reserva : reservas) {
            for (ItemEmprestavel item : reserva.getItens()) {
                if (item.getIdentificador().equals(jornalEncontrado.getIssn())) {
                    jornalReservado = true;
                    break;
                }
            }
            if (jornalReservado) {
                break;
            }
        }

        // Verifica se o jornal está associado a algum empréstimo ativo.
        boolean jornalEmprestado = false;
        for (Emprestimos emprestimos : emprestimos) {
            for (ItemEmprestavel item : emprestimos.getItens()) {
                if (item.getIdentificador().equals(jornalEncontrado.getIssn())) {
                    jornalEmprestado = true;
                    break;
                }
            }
            if (jornalEmprestado) {
                break;
            }
        }

        if (jornalReservado && jornalEmprestado) {
            System.out.println("Erro: O jornal/revista '" + jornalEncontrado.getTitulo() + " (ISSN: " + jornalEncontrado.getIdentificador() + ")' está associado a uma reserva e a um empréstimo e não pode ser removido.");
        } else if (jornalEmprestado) {
            System.out.println("Erro: O jornal/revista '" + jornalEncontrado.getTitulo() + " (ISSN: " + jornalEncontrado.getIdentificador() + ")' está associado a um empréstimo e não pode ser removido.");
        } else if (jornalReservado) {
            System.out.println("Erro: O jornal/revista '" + jornalEncontrado.getTitulo() + " (ISSN: " + jornalEncontrado.getIdentificador() + ")' está associado a uma reserva e não pode ser removido.");
        } else {
            jornais.remove(jornalEncontrado);
            System.out.println("Jornal/revista removido com sucesso!");
        }
    }

    /**
     * Procura um jornal/revista pelo ISSN.
     *
     * @param issn ISSN do jornal/revista a ser procurado.
     * @return O jornal/revista encontrado ou null se não existir.
     */
    public Jornal procurarPorIssn(String issn) {

        // Verifica se o ISSN é existente
        if (issn == null || issn.trim().isEmpty()) {
            System.out.println("Erro: ISSN inválido para busca.");
            return null;
        }

        for (Jornal jornal : jornais) {
            if (jornal.getIssn() != null && jornal.getIssn().equalsIgnoreCase(issn)) {
                return jornal;
            }
        }
        return null;
    }

}
