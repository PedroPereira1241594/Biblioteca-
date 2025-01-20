package Controller;

import Model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LivroLoader {

    private static final String FICHEIRO_LIVROS = "C:\\Users\\Acer\\Desktop\\APOO\\Projecto Final 1\\Biblioteca-\\Biblioteca-\\src\\DadosExportados\\livros.txt"; // Nome do ficheiro

    // Método para carregar livros do ficheiro
    public static List<Livro> carregarLivros() {
        List<Livro> livros = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_LIVROS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 6) { // Verifica se há exatamente 6 campos
                    String isbn = dados[0];
                    String nome = dados[1];
                    String editora = dados[2];
                    String categoria = dados[3];
                    int ano = Integer.parseInt(dados[4]);
                    String autor = dados[5];

                    Livro livro = new Livro(nome, editora, categoria, ano, autor, isbn);
                    livros.add(livro);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar livros do ficheiro: " + e.getMessage());
        }
        return livros;
    }
    private static final String FICHEIRO_UTENTES = "C:\\Users\\Acer\\Desktop\\APOO\\Projecto Final 1\\Biblioteca-\\Biblioteca-\\src\\DadosExportados\\utentes.txt"; // Nome do ficheiro

    // Método para carregar utentes do ficheiro
    public static List<Utentes> carregarUtentes() {
        List<Utentes> utentes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_UTENTES))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 4) { // Verifica se há exatamente 4 campos
                    String nome = dados[0];
                    String nif = dados[1];
                    Boolean genero = Boolean.parseBoolean(dados[2]); // true para masculino, false para feminino
                    String contacto = dados[3];

                    Utentes utente = new Utentes(nome, nif, genero, contacto);
                    utentes.add(utente);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar utentes do ficheiro: " + e.getMessage());
        }
        return utentes;
    }

    private static final String FICHEIRO_JORNAIS = "C:\\Users\\Acer\\Desktop\\APOO\\Projecto Final 1\\Biblioteca-\\Biblioteca-\\src\\DadosExportados\\jornal.txt"; // Nome do ficheiro

    // Método para carregar jornais do ficheiro
    public static List<Jornal> carregarJornais() {
        List<Jornal> jornais = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_JORNAIS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 5) { // Verifica se há exatamente 5 campos
                    String titulo = dados[0];
                    String editora = dados[1];
                    String categoria = dados[2];
                    String issn = dados[3];
                    String dataPublicacao = dados[4];

                    Jornal jornal = new Jornal(titulo, editora, categoria, issn, dataPublicacao);
                    jornais.add(jornal);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar jornais do ficheiro: " + e.getMessage());
        }
        return jornais;
    }

    private static final String FICHEIRO_EMPRESTIMOS = "C:\\Users\\Acer\\Desktop\\APOO\\Projecto Final 1\\Biblioteca-\\Biblioteca-\\src\\DadosExportados\\emprestimos.txt";

    public static List<Emprestimos> carregarEmprestimos(List<Utentes> utentes, List<Livro> livrosDisponiveis) {
        List<Emprestimos> emprestimos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_EMPRESTIMOS))) {
            String linha;
            int linhaNumero = 0; // Para rastrear o número da linha
            while ((linha = br.readLine()) != null) {
                linhaNumero++;
                linha = linha.trim(); // Remove espaços em branco no início e no final da linha

                // Ignorar linhas vazias
                if (linha.isEmpty()) {
                    continue;
                }

                String[] dados = linha.split(";");
                if (dados.length < 6) {
                    System.out.println("Linha " + linhaNumero + " mal formatada. Ignorada.");
                    continue;
                }

                try {
                    int numero = Integer.parseInt(dados[0]);
                    String nifUtente = dados[1];

                    // Busca o utente correspondente
                    Utentes utente = null;
                    for (Utentes u : utentes) {
                        if (u.getNif().equals(nifUtente)) {
                            utente = u;
                            break;
                        }
                    }

                    // Se o utente não for encontrado, ignora a linha
                    if (utente == null) {
                        System.out.println("Utente com NIF " + nifUtente + " não encontrado. Linha " + linhaNumero + " ignorada.");
                        continue;
                    }

                    // Processa os títulos dos livros
                    List<Livro> livrosEmprestados = new ArrayList<>();
                    String[] titulosLivros = dados[2].split(",");
                    for (String titulo : titulosLivros) {
                        boolean livroEncontrado = false;
                        for (Livro livro : livrosDisponiveis) {
                            if (livro.getNome().equalsIgnoreCase(titulo)) {
                                livrosEmprestados.add(livro);
                                livroEncontrado = true;
                                break;
                            }
                        }
                        if (!livroEncontrado) {
                            System.out.println("Livro com título '" + titulo + "' não encontrado. Ignorado.");
                        }
                    }

                    // Processa as datas
                    LocalDate dataInicio = LocalDate.parse(dados[3]);
                    LocalDate dataPrevistaDevolucao = LocalDate.parse(dados[4]);
                    LocalDate dataEfetivaDevolucao = null;
                    if (!dados[5].equalsIgnoreCase("null") && !dados[5].isEmpty()) {
                        dataEfetivaDevolucao = LocalDate.parse(dados[5]);
                    }

                    // Cria o objeto Emprestimos e adiciona à lista
                    Emprestimos emprestimo = new Emprestimos(numero, utente, livrosEmprestados, dataInicio, dataPrevistaDevolucao, dataEfetivaDevolucao);
                    emprestimos.add(emprestimo);
                } catch (Exception e) {
                    System.out.println("Erro ao processar linha " + linhaNumero + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar empréstimos do ficheiro: " + e.getMessage());
        }

        return emprestimos;
    }
    private static final String FICHEIRO_RESERVAS = "C:\\Users\\Acer\\Desktop\\APOO\\Projecto Final 1\\Biblioteca-\\Biblioteca-\\src\\DadosExportados\\reservas.txt";  // Nome do ficheiro de reservas

    // Método para carregar reservas do ficheiro
    public static List<Reserva> carregarReservas(List<Utentes> utentes, List<Livro> livrosDisponiveis) {
        List<Reserva> reservas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_RESERVAS))) {
            String linha;
            int linhaNumero = 0; // Para rastrear o número da linha
            while ((linha = br.readLine()) != null) {
                linhaNumero++;
                linha = linha.trim(); // Remove espaços em branco no início e no final da linha

                // Ignorar linhas vazias
                if (linha.isEmpty()) {
                    System.out.println("Linha " + linhaNumero + " está vazia. Ignorada.");
                    continue;
                }

                String[] dados = linha.split(";");
                if (dados.length < 7) { // Certifica-se de que existem pelo menos 7 campos
                    System.out.println("Linha " + linhaNumero + " mal formatada. Ignorada.");
                    continue;
                }

                try {
                    int numeroReserva = Integer.parseInt(dados[0]);
                    String nomeUtente = dados[1];
                    String nifUtente = dados[2];

                    // Busca o utente correspondente
                    Utentes utente = null;
                    for (Utentes u : utentes) {
                        if (u.getNif().equals(nifUtente)) {
                            utente = u;
                            break;
                        }
                    }

                    // Se o utente não for encontrado, ignora a linha
                    if (utente == null) {
                        System.out.println("Utente com NIF " + nifUtente + " não encontrado. Linha " + linhaNumero + " ignorada.");
                        continue;
                    }

                    // Processa os livros da reserva (ISBN ou títulos dos livros)
                    List<Livro> livrosReservados = new ArrayList<>();
                    String[] titulosLivros = dados[3].split(",");
                    for (String titulo : titulosLivros) {
                        boolean livroEncontrado = false;
                        for (Livro livro : livrosDisponiveis) {
                            if (livro.getNome().equalsIgnoreCase(titulo.trim())) {
                                livrosReservados.add(livro);
                                livroEncontrado = true;
                                break;
                            }
                        }
                        if (!livroEncontrado) {
                            System.out.println("Livro com título '" + titulo + "' não encontrado. Ignorado.");
                        }
                    }

                    // Processa as datas de registro, início e fim
                    LocalDate dataRegisto = LocalDate.parse(dados[4]);
                    LocalDate dataInicio = LocalDate.parse(dados[5]);
                    LocalDate dataFim = LocalDate.parse(dados[6]);

                    // Cria o objeto Reserva e adiciona à lista de reservas
                    Reserva reserva = new Reserva(numeroReserva, utente, livrosReservados, dataRegisto, dataInicio, dataFim);
                    reservas.add(reserva);

                } catch (Exception e) {
                    System.out.println("Erro ao processar linha " + linhaNumero + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar reservas do ficheiro: " + e.getMessage());
        }

        return reservas;
    }

}