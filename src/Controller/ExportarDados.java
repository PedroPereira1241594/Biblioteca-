package Controller;

import Model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportarDados {

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

    public static void exportarEmprestimos(String caminhoArquivo, ArrayList<Emprestimos> emprestimos) throws IOException {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            for (Emprestimos emprestimo : emprestimos) {
                // Variáveis para armazenar os ISSNs e ISBNs
                String issns = "";
                String isbns = "";

                // Iterar sobre os itens emprestados
                for (ItemEmprestavel item : emprestimo.getItens()) {
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

                // Garantir que ISSN e ISBN apareçam sempre, mesmo que estejam vazios
                String issnStr = "ISSN: " + (issns.isEmpty() ? "" : issns);
                String isbnStr = "ISBN: " + (isbns.isEmpty() ? "" : isbns);

                // Montar a linha de saída
                String linha = String.format(
                        "ID: %d; Nome: %s; %s; %s; DataInicio: %s; DataPrevistaDevolução: %s; DataEfetivaDevolução: %s\n",
                        emprestimo.getNumero(),
                        emprestimo.getUtente().getNome(),
                        issnStr,
                        isbnStr,
                        emprestimo.getDataInicio(),
                        emprestimo.getDataPrevistaDevolucao(),
                        emprestimo.getDataEfetivaDevolucao() == null ? "null" : emprestimo.getDataEfetivaDevolucao()
                );

                // Escrever a linha no arquivo
                writer.write(linha);
            }

            System.out.println("Empréstimos exportados com sucesso para o ficheiro!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os Empréstimos: " + e.getMessage());
        }
    }


    public static void exportarReservas(String caminhoArquivo, ArrayList<Reserva> reservas) throws IOException {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            for (Reserva reserva : reservas) {
                // Variáveis para armazenar os ISSNs e ISBNs
                String issns = "";
                String isbns = "";

                // Iterar sobre os itens reservados
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

                // Garantir que ISSN e ISBN apareçam sempre, mesmo que estejam vazios
                String issnStr = "ISSN: " + (issns.isEmpty() ? "" : issns);
                String isbnStr = "ISBN: " + (isbns.isEmpty() ? "" : isbns);

                // Montar a linha de saída
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

                // Escrever a linha no arquivo
                writer.write(linha);
            }

            System.out.println("Reservas exportados com sucesso para o ficheiro!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar as Reservas: " + e.getMessage());
        }
    }


}



