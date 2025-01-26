package Controller;

import Model.*;
import Controller.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class JornalController {
    private ArrayList<Jornal> jornais;
    private List<Emprestimos> emprestimos;
    private EmprestimosController emprestimosController;
    private final List<Reserva> reservas;// Lista para armazenar os jornais

    // Construtor
    public JornalController(ArrayList<Jornal> jornais, List<Reserva> reservas, List<Emprestimos> emprestimos) {
        this.jornais = jornais;
        this.reservas = reservas;
        this.emprestimos = emprestimos;
    }

    // Método para criar um novo jornal ou uma revista
    public void criarJornal(String issn, String titulo, String categoria, String editora, LocalDate dataPublicacao) {
        try {
            Jornal novoJornal = new Jornal(issn, titulo, categoria, editora, dataPublicacao);
            jornais.add(novoJornal);
            System.out.println("Jornal/Revista adicionado com sucesso!");
        } catch (DateTimeParseException e) {
            System.out.println("Data de publicação inválida! Use o formato dd/MM/yyyy.");
        }
    }

    // Método para listar todos os jornais e revistas
    public void listarJornais() {
        if (jornais.isEmpty()) {
            System.out.println("Nenhum jornal ou revista registado.");
            return;
        }

        System.out.println("\n=== Lista de Jornais/Revistas ===");
        System.out.printf("%-20s %-35s %-20s %-25s %-15s%n",
                "ISSN", "Título", "Categoria", "Editora", "Data Publicação");
        System.out.println("-".repeat(115));

        for (Jornal jornal : jornais) {
            System.out.printf(
                    "%-20s %-35s %-20s %-25s %-15s%n",
                    jornal.getIssn(),
                    jornal.getTitulo(),
                    jornal.getCategoria(),
                    jornal.getEditora(),
                    jornal.getDataPublicacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
        }

        System.out.println("-".repeat(115));
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

        // Deletando o jornal encontrado
        System.out.println("A eliminar o jornal/revista: " + issn + "...");
        jornais.remove(jornalEncontrado);
        System.out.println("Jornal/revista removido com sucesso!");
    }

    // Método para procurar um jornal ou revista pelo ISSN
    public Jornal procurarPorIssn(String issn) {
        for (Jornal jornal : jornais) {
            if (jornal.getIssn().equalsIgnoreCase(issn)) {
                return jornal;
            }
        }
        return null;
    }

    public boolean verificarJornalIndisponivel(Jornal jornal, LocalDate dataInicio, LocalDate dataPrevistaFim, LocalDate dataFinal) {
        // Verificar empréstimos
        for (Emprestimos emprestimo : emprestimosController.listarEmprestimosAtivos()) {
            if (emprestimo.getJornais().contains(jornal)) {
                LocalDate inicioEmprestimo = emprestimo.getDataInicio();
                LocalDate fimEmprestimo = emprestimo.getDataEfetivaDevolucao();
                if(dataFinal == null) {
                    if (!(dataPrevistaFim.isBefore(inicioEmprestimo) || dataInicio.isAfter(fimEmprestimo))) {
                        return true; // O livro está emprestado no intervalo
                    }
                } else {
                    if (!(dataFinal.isBefore(inicioEmprestimo) || dataInicio.isAfter(fimEmprestimo))) {
                        return true; // O livro está emprestado no intervalo
                    }
                }
            }
        }

        // Verificar reservas
        for (Reserva reserva : reservas) {
            if (reserva.getJornais().contains(jornal)) {
                LocalDate inicioReserva = reserva.getDataInicio();
                LocalDate fimReserva = reserva.getDataFim();
                if(dataFinal == null) {
                    if (!(dataPrevistaFim.isBefore(inicioReserva) || dataInicio.isAfter(fimReserva))) {
                        return true; // O livro está reservado no intervalo
                    }
                } else {
                    if (!(dataFinal.isBefore(inicioReserva) || dataInicio.isAfter(fimReserva))) {
                        return true; // O livro está reservado no intervalo
                    }
                }
            }
        }

        return false; // O livro está disponível para o intervalo
    }
}
