package Controller;

import Model.Emprestimos;
import Model.Jornal;
import Model.ItemEmprestavel;
import Model.Reserva;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class JornalController {
    private static ArrayList<Jornal> jornais;
    private List<Reserva> reservas; // Adicionada a lista de reservas
    private List<Emprestimos> emprestimos;// Lista para armazenar os jornais

    // Construtor
    public JornalController(ArrayList<Jornal> jornais, List<Reserva> reservas, List<Emprestimos> emprestimos) {
        this.jornais = jornais;
        this.reservas = reservas;
        this.emprestimos = emprestimos;
    }

    // Método para criar um novo jornal ou uma revista
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


    // Método para listar todos os jornais e revistas
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

    // Método para atualizar um jornal ou uma revista
    public void atualizarJornal(String issn, String novoIssn, String novoTitulo, String novaCategoria, String novaEditora, LocalDate novaDataPublicacao) {
        Jornal jornalEncontrado = null;

        // Buscando o jornal pelo ISSN
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

        // Atualizando os dados do jornal encontrado
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

    // Método para eliminar um jornal ou uma revista
    public void eliminarJornal(String issn) {
        Jornal jornalEncontrado = null;

        // Buscando o jornal pelo ISSN
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

        // Verificar se o jornal está associado a alguma reserva
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

        // Verificar se o jornal está associado a algum empréstimo ativo
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
            System.out.println("Erro: O jornal/revista '" + jornalEncontrado.getTitulo() + "' está associado a uma reserva e a um empréstimo e não pode ser removido.");
        } else if (jornalEmprestado) {
            System.out.println("Erro: O jornal/revista '" + jornalEncontrado.getTitulo() + "' está associado a um empréstimo e não pode ser removido.");
        } else if (jornalReservado) {
            System.out.println("Erro: O jornal/revista '" + jornalEncontrado.getTitulo() + "' está associado a uma reserva e não pode ser removido.");
        } else {
            jornais.remove(jornalEncontrado);
            System.out.println("Jornal/revista removido com sucesso!");
        }
    }

    // Método para procurar um jornal ou revista pelo ISSN
    public Jornal procurarPorIssn(String issn) {
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


    // Método para obter todos os jornais como uma lista de ItemEmprestavel (para integração com o sistema de empréstimos)
    public static ArrayList<ItemEmprestavel> obterJornaisComoItensEmprestaveis() {
        ArrayList<ItemEmprestavel> itensEmprestaveis = new ArrayList<>();
        for (Jornal jornal : jornais) {
            itensEmprestaveis.add(jornal); // Adiciona cada jornal como um ItemEmprestavel
        }
        return itensEmprestaveis;
    }
}
