package Controller;

import Model.Jornal;
import Model.Livro;
import Model.Utentes;
import Model.Emprestimos;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportarDados {

    public static void exportarLivros(String caminhoArquivo, List<Livro> livros) {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            for (Livro livro : livros) {
                writer.write(String.format("%s;%s;%s;%s;%d;%s\n",
                        livro.getIsbn(),
                        livro.getNome(),
                        livro.getEditora(),
                        livro.getCategoria(),
                        livro.getAno(),
                        livro.getAutor()));
            }
            System.out.println("Livros salvos no arquivo com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os livros: " + e.getMessage());
        }
    }

    public static void exportarUtentes(String caminhoArquivo, ArrayList<Utentes> utentes) throws IOException {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            for (Utentes utente : utentes) {
                writer.write(String.format("%s;%s;%s;%s\n",
                        utente.getNif(),
                        utente.getNome(),
                        utente.getGenero() ? "M" : "F",
                        utente.getContacto()));
            }
            System.out.println("Utentes salvos no arquivo com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os Utentes: " + e.getMessage());
        }
    }

    public static void exportarJornal(String caminhoArquivo, ArrayList<Jornal> jornals) throws IOException {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            for (Jornal jornal : jornals) {
                writer.write(String.format("%s;%s;%s;%s;%s\n",
                        jornal.getIssn(),
                        jornal.getTitulo(),
                        jornal.getCategoria(),
                        jornal.getEditora(),
                        jornal.getDataPublicacao()));
            }
            System.out.println("Jornais/Revistas salvos no arquivo com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os Jornais/Revistas: " + e.getMessage());

        }

    }
    public static void exportarEmprestimos(String caminhoArquivo, ArrayList<Emprestimos> emprestimos) throws IOException {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            for (Emprestimos emprestimo : emprestimos) {
                writer.write(String.format("%d;%s;%s;%s;%s;%s\n",
                        emprestimo.getNumero(),
                        emprestimo.getUtente().getNome(), // Exportando o nome do Utente
                        emprestimo.getLivros(), // Livros formatados
                        emprestimo.getDataInicio().toString(),
                        emprestimo.getDataPrevistaDevolucao().toString(),
                        emprestimo.getDataEfetivaDevolucao()));
            }
            System.out.println("Empréstimos salvos no arquivo com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os Empréstimos: " + e.getMessage());
        }
    }



}


