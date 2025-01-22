package Controller;

import Model.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LivroLoader {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Método genérico para carregar dados a partir de um arquivo
    private static List<String[]> carregarArquivo(String caminho, int expectedFields) {
        List<String[]> dados = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (!linha.isEmpty()) {
                    String[] campos = linha.split(";");
                    if (campos.length == expectedFields) {
                        dados.add(campos);
                    } else {
                        System.out.println("Linha mal formatada. Ignorada.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados do ficheiro: " + e.getMessage());
        }
        return dados;
    }

    // Método para remover o prefixo e espaços de um campo
    private static String limparCampo(String campo, String prefixo) {
        return campo.replace(prefixo, "").trim();
    }

    // Carregar Livros
    public static List<Livro> carregarLivros(String caminhoLivros) {
        List<Livro> livros = new ArrayList<>();
        List<String[]> dados = carregarArquivo(caminhoLivros, 6);
        for (String[] dadosLivro : dados) {
            String isbn = limparCampo(dadosLivro[0], "ISBN: ");
            String nome = limparCampo(dadosLivro[1], "Nome: ");
            String editora = limparCampo(dadosLivro[2], "Editora: ");
            String categoria = limparCampo(dadosLivro[3], "Categoria: ");
            int ano = Integer.parseInt(limparCampo(dadosLivro[4], "Ano: "));
            String autor = limparCampo(dadosLivro[5], "Autor: ");

            livros.add(new Livro(nome, editora, categoria, ano, autor, isbn));
        }
        return livros;
    }

    // Carregar Utentes
    public static List<Utentes> carregarUtentes(String caminhoUtentes) {
        List<Utentes> utentes = new ArrayList<>();
        List<String[]> dados = carregarArquivo(caminhoUtentes, 4);
        for (String[] dadosUtente : dados) {
            String nome = limparCampo(dadosUtente[0], "Nome: ");
            String nif = limparCampo(dadosUtente[1], "NIF: ");
            boolean genero = limparCampo(dadosUtente[2], "Género: ").equals("M");
            String contacto = limparCampo(dadosUtente[3], "Contacto: ");
            utentes.add(new Utentes(nome, nif, genero, contacto));
        }
        return utentes;
    }

    // Carregar Jornais
    public static List<Jornal> carregarJornais(String caminhoJornal) {
        List<Jornal> jornais = new ArrayList<>();
        List<String[]> dados = carregarArquivo(caminhoJornal, 5);
        for (String[] dadosJornal : dados) {
            String titulo = limparCampo(dadosJornal[0], "Titulo: ");
            String editora = limparCampo(dadosJornal[1], "Editora: ");
            String categoria = limparCampo(dadosJornal[2], "Categoria: ");
            String issn = limparCampo(dadosJornal[3], "ISSN: ");
            String dataPublicacaoStr = limparCampo(dadosJornal[4], "Data Publicação: ");

            LocalDate dataPublicacao = null;
            try {
                dataPublicacao = LocalDate.parse(dataPublicacaoStr, DATE_FORMATTER);
            } catch (Exception e) {
                System.out.println("Erro ao formatar a data de publicação para o jornal: " + dataPublicacaoStr);
            }

            if (dataPublicacao != null) {
                jornais.add(new Jornal(titulo, editora, categoria, issn, dataPublicacao));
            } else {
                System.out.println("Data de publicação inválida para o jornal: " + titulo);
            }
        }
        return jornais;
    }

    // Carregar Emprestimos
    public static List<Emprestimos> carregarEmprestimos(String caminhoEmprestimo, List<Utentes> utentes, List<Livro> livrosDisponiveis) throws IOException {
        List<Emprestimos> emprestimos = new ArrayList<>();
        List<String[]> dados = carregarArquivo(caminhoEmprestimo, 6);
        for (String[] dadosEmprestimo : dados) {
            try {
                int numero = Integer.parseInt(limparCampo(dadosEmprestimo[0], "ID: "));
                String nomeUtente = limparCampo(dadosEmprestimo[1], "Nome: ");
                String livrosEmprestadosStr = limparCampo(dadosEmprestimo[2], "ISBN: ");
                String[] titulosLivros = livrosEmprestadosStr.split(",");

                Utentes utente = buscarUtentePorNome(nomeUtente, utentes);
                if (utente == null) continue;

                List<Livro> livrosEmprestados = buscarLivrosPorIsbn(titulosLivros, livrosDisponiveis);
                LocalDate dataInicio = LocalDate.parse(limparCampo(dadosEmprestimo[3], "DataInicio: "), DATE_FORMATTER);
                LocalDate dataPrevistaDevolucao = LocalDate.parse(limparCampo(dadosEmprestimo[4], "DataPrevistaDevolução: "), DATE_FORMATTER);
                LocalDate dataEfetivaDevolucao = null;
                if (!limparCampo(dadosEmprestimo[5], "DataEfetivaDevolução: ").equals("null")) {
                    dataEfetivaDevolucao = LocalDate.parse(limparCampo(dadosEmprestimo[5], "DataEfetivaDevolução: "), DATE_FORMATTER);
                }

                emprestimos.add(new Emprestimos(numero, utente, livrosEmprestados, dataInicio, dataPrevistaDevolucao, dataEfetivaDevolucao));
            } catch (Exception e) {
                System.out.println("Erro ao processar linha: " + e.getMessage());
            }
        }
        return emprestimos;
    }

    // Carregar Reservas
    public static List<Reserva> carregarReservas(String caminhoReserva, List<Utentes> utentes, List<Livro> livrosDisponiveis) {
        List<Reserva> reservas = new ArrayList<>();
        List<String[]> dados = carregarArquivo(caminhoReserva, 6);
        for (String[] dadosReserva : dados) {
            try {
                int numeroReserva = Integer.parseInt(limparCampo(dadosReserva[0], "ID: "));
                String nomeUtente = limparCampo(dadosReserva[1], "Nome: ");
                String livrosReservadosStr = limparCampo(dadosReserva[2], "ISBN: ");
                String[] isbnLivros = livrosReservadosStr.split(",");

                Utentes utente = buscarUtentePorNome(nomeUtente, utentes);
                if (utente == null) continue;

                List<Livro> livrosReservados = buscarLivrosPorIsbn(isbnLivros, livrosDisponiveis);
                LocalDate dataRegisto = LocalDate.parse(limparCampo(dadosReserva[3], "DataRegisto: "), DATE_FORMATTER);
                LocalDate dataInicio = LocalDate.parse(limparCampo(dadosReserva[4], "DataInicioReserva: "), DATE_FORMATTER);
                LocalDate dataFim = LocalDate.parse(limparCampo(dadosReserva[5], "DataFimReserva: "), DATE_FORMATTER);

                reservas.add(new Reserva(numeroReserva, utente, livrosReservados, dataRegisto, dataInicio, dataFim));
            } catch (Exception e) {
                System.out.println("Erro ao processar linha: " + e.getMessage());
            }
        }
        return reservas;
    }

    // Busca utente pelo nome
    private static Utentes buscarUtentePorNome(String nome, List<Utentes> utentes) {
        return utentes.stream().filter(u -> u.getNome().trim().equalsIgnoreCase(nome.trim())).findFirst().orElse(null);
    }

    // Busca livros por ISBN
    private static List<Livro> buscarLivrosPorIsbn(String[] isbns, List<Livro> livrosDisponiveis) {
        List<Livro> livrosEncontrados = new ArrayList<>();
        for (String isbn : isbns) {
            Livro livro = livrosDisponiveis.stream().filter(l -> l.getIsbn().equals(isbn.trim())).findFirst().orElse(null);
            if (livro != null) {
                livrosEncontrados.add(livro);
            } else {
                System.out.println("Livro com ISBN " + isbn.trim() + " não encontrado. Ignorado.");
            }
        }
        return livrosEncontrados;
    }
}
