package Controller;

import Model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por exportar dados da aplicação para um txt.
 */
public class ExportarDados {

    /**
     * Exporta uma lista de livros para um txt.
     *
     * @param caminhoArquivo O caminho do txt de destino.
     * @param livros         Lista de livros a serem exportados.
     */
    public static void exportarLivros(String caminhoArquivo, List<Livro> livros) {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            for (Livro livro : livros) {
                writer.write(String.format("ISBN: %s; Nome: %s; Editora: %s; Categoria: %s; Ano: %d; Autor: %s\n",
                        livro.getIsbn(),
                        livro.getNome(),
                        livro.getEditora(),
                        livro.getCategoria(),
                        livro.getAno(),
                        livro.getAutor()));
            }
            System.out.println("\nLivros exportados com sucesso para o ficheiro!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os livros: " + e.getMessage());
        }
    }

    /**
     * Exporta uma lista de utentes para um txt.
     *
     * @param caminhoArquivo O caminho do txt de destino.
     * @param utentes        Lista de utentes a serem exportados.
     * @throws IOException Se ocorrer um erro ao escrever no txt.
     */
    public static void exportarUtentes(String caminhoArquivo, ArrayList<Utentes> utentes) throws IOException {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            for (Utentes utente : utentes) {
                writer.write(String.format("Nome: %s; NIF: %s; Genero: %s; Contacto: %s\n",
                        utente.getNome(),
                        utente.getNif(),
                        utente.getGenero() ? "M" : "F",
                        utente.getContacto()));
            }
            System.out.println("Utentes exportados com sucesso para o ficheiro!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os Utentes: " + e.getMessage());
        }
    }

    /**
     * Exporta uma lista de jornais para um txt.
     *
     * @param caminhoArquivo O caminho do txt de destino.
     * @param jornals        Lista de jornais a serem exportados.
     * @throws IOException Se ocorrer um erro ao escrever no txt.
     */
    public static void exportarJornal(String caminhoArquivo, ArrayList<Jornal> jornals) throws IOException {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            for (Jornal jornal : jornals) {
                writer.write(String.format("ISSN: %s; Titulo: %s; Categoria: %s; Editora: %s; Data Publicação: %s\n",
                        jornal.getIssn(),
                        jornal.getTitulo(),
                        jornal.getCategoria(),
                        jornal.getEditora(),
                        jornal.getDataPublicacao()));
            }
            System.out.println("Jornais/Revistas exportados com sucesso para o ficheiro!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os Jornais/Revistas: " + e.getMessage());
        }
    }

    /**
     * Exporta uma lista de empréstimos para um txt.
     *
     * @param caminhoArquivo O caminho do txt de destino.
     * @param emprestimos    Lista de empréstimos a serem exportados.
     * @throws IOException Se ocorrer um erro ao escrever no txt.
     */
    public static void exportarEmprestimos(String caminhoArquivo, ArrayList<Emprestimos> emprestimos) throws IOException {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            for (Emprestimos emprestimo : emprestimos) {
                // Variáveis para armazenar os ISSNs e ISBNs
                String issns = "";
                String isbns = "";

                // Separa os itens reservados pelo tipo de objeto
                for (ItemEmprestavel item : emprestimo.getItens()) {
                    if (item instanceof Jornal) {
                        // Adicionar ISSN (com vírgula se necessário)
                        if (!issns.isEmpty()) issns += ", ";
                        issns += ((Jornal) item).getIssn();
                    } else if (item instanceof Livro) {
                        // Adicionar ISBN (com vírgula se necessário)
                        if (!isbns.isEmpty()) isbns += ", ";
                        isbns += ((Livro) item).getIsbn();
                    }
                }

                // Montagem da linha de saída
                String linha = String.format(
                        "ID: %d; Nome: %s; ISSN: %s; ISBN: %s; DataInicio: %s; DataPrevistaDevolução: %s; DataEfetivaDevolução: %s\n",
                        emprestimo.getNumero(),
                        emprestimo.getUtente().getNome(),
                        issns.isEmpty() ? "" : issns,
                        isbns.isEmpty() ? "" : isbns,
                        emprestimo.getDataInicio(),
                        emprestimo.getDataPrevistaDevolucao(),
                        emprestimo.getDataEfetivaDevolucao() == null ? "null" : emprestimo.getDataEfetivaDevolucao()
                );
                writer.write(linha);
            }
            System.out.println("Empréstimos exportados com sucesso para o ficheiro!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os Empréstimos: " + e.getMessage());
        }
    }

    /**
     * Exporta uma lista de reservas para um txt.
     *
     * @param caminhoArquivo O caminho do txt de destino.
     * @param reservas       Lista de empréstimos a serem exportados.
     * @throws IOException Se ocorrer um erro ao escrever no txt.
     */
    public static void exportarReservas(String caminhoArquivo, ArrayList<Reserva> reservas) throws IOException {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            for (Reserva reserva : reservas) {
                // Variáveis para armazenar os ISSNs e ISBNs
                String issns = "";
                String isbns = "";

                // Separa os itens reservados pelo tipo de objeto
                for (ItemEmprestavel item : reserva.getItens()) {
                    if (item instanceof Jornal) {
                        // Adicionar ISSN (com vírgula se necessário)
                        if (!issns.isEmpty()) {
                            issns += ", ";
                        }
                        issns += ((Jornal) item).getIssn();
                    } else if (item instanceof Livro) {
                        // Adicionar ISBN (com vírgula se necessário)
                        if (!isbns.isEmpty()) {
                            isbns += ", ";
                        }
                        isbns += ((Livro) item).getIsbn();
                    }
                }

                // Garante que o ISSN e o ISBN apareçam mesmo vazios
                String issnStr = "ISSN: " + (issns.isEmpty() ? "" : issns);
                String isbnStr = "ISBN: " + (isbns.isEmpty() ? "" : isbns);

                // Montagem da linha de saída
                String linha = String.format(
                        "ID: %d; Nome: %s; %s; %s; DataRegisto: %s; DataInicioReserva: %s; DataFimReserva: %s\n",
                        reserva.getNumero(),
                        reserva.getUtente().getNome(),
                        issnStr,
                        isbnStr,
                        reserva.getDataRegisto(),
                        reserva.getDataInicio(),
                        reserva.getDataFim()
                );

                writer.write(linha);
            }

            System.out.println("Reservas exportados com sucesso para o ficheiro!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar as Reservas: " + e.getMessage());
        }
    }

}



